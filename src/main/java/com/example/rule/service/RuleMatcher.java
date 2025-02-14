package com.example.rule.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rule.model.*;
import com.example.rule.util.RuleUtils;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RuleMatcher {
    @Resource
    private FlowExecutor flowExecutor;
    @Resource
    private FmPolicyRuleService fmPolicyRuleService;
    @Resource
    private ActiveMQProducer activeMQProducer;
    @Resource
    private FmService fmService;
    @Resource
    private FmEventDefineService fmEventDefineService;
    private final Object lock = new Object();

    public void matchAndExecuteRules(MSEvent msEvent) {
        List<FmPolicyRules> rulesByEventId = fmPolicyRuleService.getRulesByEventId(msEvent.getEventId());
        List<FmPolicyRules> rulesByTitle = fmPolicyRuleService.getRulesByTitle(msEvent.getTitle());
        List<FmPolicyRules> rules= getCombinedRules(rulesByEventId, rulesByTitle);
        if (CollectionUtils.isNotEmpty(rules)) {
            for (FmPolicyRules rule : rules) {
                int delayTime = rule.getDelayTime();
                if (rule.getIsDeploy() == 0) {
                    log.info("规则未启用 Rule not executed: {}", rule.getId());
                    continue;
                }
                if (delayTime > 0) {
                    // 延迟执行规则
                    processDelayedRule(msEvent, rule);
                } else {
                    executeRule(msEvent, rule, Integer.parseInt(rule.getId().toString()), "Rule executed successfully: {}", "Rule execution failed: {}");
                }
                log.info("Rule executed: {}", rule.getId());
            }
        }
    }
    public void processDelayedRule(MSEvent msEvent, FmPolicyRules rule) {
        long timeReal = getDelayTimeReal(msEvent, rule);
        MqMessage mqMessage = getMqMessage(msEvent, rule, timeReal);
        activeMQProducer.sendDelayedRule(mqMessage);
        log.info("Delayed rule message sent: {}", mqMessage);
    }
    /**
     * 合并规则列表并根据规则ID去重
     * @param rules 根据eventId获取的规则列表
     * @param rulesByTitle 根据title获取的规则列表
     * @return 去重后的规则列表
     */
    private List<FmPolicyRules> getCombinedRules(List<FmPolicyRules> rules, List<FmPolicyRules> rulesByTitle) {
        List<FmPolicyRules> allRules = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rules)) {
            allRules.addAll(rules);
        }
        if (CollectionUtils.isNotEmpty(rulesByTitle)) {
            allRules.addAll(rulesByTitle);
        }
        // 根据规则ID去重
        Map<Long, FmPolicyRules> uniqueRulesMap = allRules.stream()
                .collect(Collectors.toMap(FmPolicyRules::getId, rule -> rule, (existing, replacement) -> existing));
        return new ArrayList<>(uniqueRulesMap.values());
    }
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
        LiteflowResponse response = flowExecutor.execute2Resp(String.valueOf(ruleId), null, conditions);
        if (response.isSuccess()) {
            log.info(successMsg, ruleId);
            SupplementaryConditions contextBean = response.getContextBean(SupplementaryConditions.class);
            int inheritType = contextBean.getFmPolicyRules().getInheritType();
            List<Long> childIds = new ArrayList<>();
            if (inheritType == 2) {
                if (contextBean.doDerive) {
                    MSEvent newEvent = deriveNewAlarm(contextBean, childIds, fmPolicyRules);
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

    private MSEvent deriveNewAlarm(SupplementaryConditions contextBean, List<Long> childIds, FmPolicyRules fmPolicyRules) {
        FmEventDefine eventDefine = fmEventDefineService.getOne(Wrappers.lambdaQuery(FmEventDefine.class)
                .eq(FmEventDefine::getEventId, fmPolicyRules.getPrimaryEventId()));
        if (eventDefine == null) {
            log.error("未定义衍生告警事件ID.");
            return null;
        }
        List<MSEvent> childrens = contextBean.getChildrens();
        MSEvent earliest = parseChilds(childrens, childIds);
        MSEvent event = new MSEvent();
        event.setSourceId(convertSourceId(contextBean.getSourceIdConds(), fmPolicyRules));
        event.setDescr(generatorDesc(earliest, fmPolicyRules)); // 告警正文
        RuleUtils.fillEventTime(event, earliest.getOccurTime());
        RuleUtils.fill(event, earliest);
        RuleUtils.fillEventDefine(event, eventDefine);
        return event;
    }

    private String generatorDesc(MSEvent earliest, FmPolicyRules fmPolicyRules) {
        StringBuilder sb = new StringBuilder();
        sb.append("省份:[河南省]")
                .append(" 地市:[")
                .append(earliest.getDeviceCity())
                .append("] 网元:[")
                .append(earliest.getDeviceName())
                .append("] 告警标题:")
                .append(fmPolicyRules.getPrimaryTitle())
                .append(" 告警产生时间:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sb.append(dateFormat.format(earliest.getOccurTime()));
        } catch (Exception e) {
            log.error("日期格式化异常", e);
        }
        return sb.toString();
    }

    protected String convertSourceId(List<String> values, FmPolicyRules policyRule) {
        StringBuilder sb = new StringBuilder();
        sb.append("MIX-").append(policyRule.getPrimaryEventId()).append("_");
        for (String value : values) {
            sb.append(value).append("_");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        sb.append(dateFormat.format(new Date()));
        return sb.toString();
    }

    private MSEvent parseChilds(List<MSEvent> childrens, List<Long> childIds) {
        MSEvent earliest = null;
        for (MSEvent event : childrens) {
            childIds.add(event.getRecordId());
            if (earliest == null || earliest.getOccurTime().after(event.getOccurTime())) {
                earliest = event;
            }
        }
        return earliest;
    }

    private SupplementaryConditions getSupplementaryConditions(MSEvent msEvent, FmPolicyRules fmPolicyRules) {
        SupplementaryConditions conditions = new SupplementaryConditions();
        conditions.setMsEvent(msEvent);
        conditions.setFmPolicyRuleService(fmPolicyRuleService);
        conditions.setFmService(fmService);
        conditions.setFmPolicyRules(fmPolicyRules);
        return conditions;
    }

    private static MqMessage getMqMessage(MSEvent msEvent, FmPolicyRules rule, long timeReal) {
        MqMessage mqMessage = new MqMessage();
        mqMessage.setRuleId(Integer.parseInt(rule.getId().toString()));
        mqMessage.setDelayTime(timeReal);
        mqMessage.setRecordId(msEvent.getRecordId());
        mqMessage.setTitle(msEvent.getTitle());
        return mqMessage;
    }

    private static long getDelayTimeReal(MSEvent msEvent, FmPolicyRules rule) {
        long timeStart = msEvent.getLastTime().getTime() + rule.getDelayTime() * 60 * 1000L;
        long timeNow = new Date().getTime();
        return timeStart - timeNow;
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
                        FmPolicyRules fmPolicyRules = contextBean.getFmPolicyRules();
                        log.info(  " execute success, ruleId:{}", response.getChainId());
                        int inheritType = fmPolicyRules.getInheritType();
                        List<Long> childIds = new ArrayList<>();
                        if (inheritType == 2) {
                            if (contextBean.doDerive) {
                                MSEvent newEvent = deriveNewAlarm(contextBean, childIds, fmPolicyRules);
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
