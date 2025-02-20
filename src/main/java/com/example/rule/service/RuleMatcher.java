package com.example.rule.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rule.model.*;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RuleMatcher {
    @Resource
    private FlowExecutor flowExecutor;
    @Resource
    private FmPolicyRuleService fmPolicyRuleService;
    @Resource
    private FmService fmService;
    private final Object lock = new Object();

    public void matchAndExecuteDelayRules(MqMessage mqMessage) {
        log.info("Executing rule: {}", mqMessage.getRuleId());
        // 这里添加具体的规则执行逻辑
        Integer ruleId = mqMessage.getRuleId();
        Long recordId = mqMessage.getRecordId();
        MSEvent msEvent = fmService.getOne(Wrappers.lambdaQuery(MSEvent.class).eq(MSEvent::getRecordId, recordId));
        if (msEvent.getClearTag() > 0) {
            log.info("告警已清除，ruleId:{},recordId:{},msEvent.getClearTag():{}", ruleId, recordId, msEvent.getClearTag());
        } else {
            FmPolicyRules fmPolicyRules = fmPolicyRuleService.getById(ruleId);
            if (fmPolicyRules.getIsDelay() == 0) {
                log.info("规则未启用或者已经停止 ruleId:{},recordId:{}", ruleId, recordId);
            } else {
                executeRule(msEvent, fmPolicyRules, ruleId, "delayRule executed successfully: {}", "delayRule execution failed: {}");
            }
        }

    }

    /**
     * 单个规则执行
     * @param msEvent
     * @param fmPolicyRules
     * @param ruleId
     * @param successMsg
     * @param errorMsg
     */
    private void executeRule(MSEvent msEvent, FmPolicyRules fmPolicyRules, Integer ruleId, String successMsg, String errorMsg) {
        SupplementaryConditions conditions = getSupplementaryConditions(msEvent, fmPolicyRules);
        conditions.setDelayedAlarm(1);
        LiteflowResponse response = flowExecutor.execute2Resp(String.valueOf(ruleId), null, conditions);
        if (response.isSuccess()) {
            log.info(successMsg, ruleId);
            SupplementaryConditions contextBean = response.getContextBean(SupplementaryConditions.class);
            int inheritType = contextBean.getFmPolicyRules().getInheritType();
            List<Long> childIds = new ArrayList<>();
            if (inheritType == 2) {
                if (contextBean.doDerive) {
                    MSEvent newEvent = fmService.deriveNewAlarm(contextBean, childIds, fmPolicyRules);
                    mergeAndFinishInherit(newEvent, childIds, fmPolicyRules);
                }
            }else if (inheritType == 1) {
                if (contextBean.doMainSubRelation) {
                    List<MSEvent> parents = contextBean.getParents();
                    List<MSEvent> childrens = contextBean.getChildrens();
                    for (MSEvent children : childrens) {
                        childIds.add(children.getRecordId());
                    }
                    for (MSEvent parent : parents) {
                        finishedReleationShip(parent, childIds, fmPolicyRules);
                    }
                }
            }

        } else {
            log.info(errorMsg, response.getCause());
        }
    }

    private void mergeAndFinishInherit(MSEvent newEvent, List<Long> childIds, FmPolicyRules fmPolicyRules) {
        if (newEvent == null) {
            log.error("Derive fail! {}.", fmPolicyRules.getRuleName());
            return;
        }
        synchronized (lock) {
            fmService.inheritMerge(newEvent);
        }
        log.info("{} recordid {}.", newEvent.getOperationType() == OperationType.INSERT ? "newEvent" : "updateEvent", newEvent.getRecordId());
        if (newEvent.getRecordId() > 0) {
            log.info("衍生告警 主告警{}, 次告警{}.", newEvent.getRecordId(), childIds);
            finishedReleationShip(newEvent, childIds, fmPolicyRules);
        } else {
            log.info("Derive fail! {}.", newEvent.getRecordId());
        }
    }

    private void finishedReleationShip(MSEvent newEvent, List<Long> childIds, FmPolicyRules policyRule) {
        try {
            synchronized (lock) {
                fmService.updateInherit(newEvent.getRecordId(), childIds, policyRule.getId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private SupplementaryConditions getSupplementaryConditions(MSEvent msEvent, FmPolicyRules fmPolicyRules) {
        SupplementaryConditions conditions = new SupplementaryConditions();
        conditions.setMsEvent(msEvent);
        conditions.setFmPolicyRuleService(fmPolicyRuleService);
        conditions.setFmService(fmService);
        conditions.setFmPolicyRules(fmPolicyRules);
        return conditions;
    }

    /**
     * 带有决策路由的规则执行
     * @param msEvent
     */
    public void executeRules(MSEvent msEvent) {
        try {
            SupplementaryConditions conditions = assembleSupplementaryConditions(msEvent);
            List<LiteflowResponse> liteflowResponses = flowExecutor.executeRouteChain("liteflow-route",null, conditions);
            for (LiteflowResponse response : liteflowResponses) {
                if (response.isSuccess()) {
                    try {
                        SupplementaryConditions contextBean = response.getContextBean(SupplementaryConditions.class);
                        Long recordId = contextBean.getMsEvent().getRecordId();
                        FmPolicyRules fmPolicyRules = contextBean.getFmPolicyRules();
                        log.info("execute success,recordId, ruleId:{}", recordId, response.getChainId());
                        int inheritType = fmPolicyRules.getInheritType();
                        List<Long> childIds = new ArrayList<>();
                        if (inheritType == 2) {
                            if (contextBean.isDoDerive()) {
                                MSEvent newEvent = fmService.deriveNewAlarm(contextBean, childIds, fmPolicyRules);
                                mergeAndFinishInherit(newEvent, childIds, fmPolicyRules);
                            }
                        }else if (inheritType == 1) {
                            if (contextBean.doMainSubRelation) {
                                List<MSEvent> parents = contextBean.getParents();
                                List<MSEvent> childrens = contextBean.getChildrens();
                                for (MSEvent children : childrens) {
                                    childIds.add(children.getRecordId());
                                }
                                for (MSEvent parent : parents) {
                                    finishedReleationShip(parent, childIds, fmPolicyRules);
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.info(  " execute fail, ruleId:{}", response.getChainId());
                }
            }

        } catch (Exception e) {
            log.error("Failed to execute rules: {}", e.getMessage(), e);
        }
    }

    private SupplementaryConditions assembleSupplementaryConditions(MSEvent msEvent) {
        SupplementaryConditions conditions = new SupplementaryConditions();
        conditions.setMsEvent(msEvent);
        conditions.setFmPolicyRuleService(fmPolicyRuleService);
        conditions.setFmService(fmService);
        return conditions;
    }
}
