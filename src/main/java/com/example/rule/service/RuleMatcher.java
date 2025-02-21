package com.example.rule.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.MqMessage;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.service.inheritance.InheritanceStrategy;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RuleMatcher {
    @Resource
    private FmPolicyRuleService fmPolicyRuleService;
    @Resource
    private FmService fmService;
    @Resource
    private RuleExecutorService ruleExecutorService;
    @Resource
    private Map<Integer, InheritanceStrategy> inheritanceStrategyMap;


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
     *
     * @param msEvent
     * @param fmPolicyRules
     * @param ruleId
     * @param successMsg
     * @param errorMsg
     */
    private void executeRule(MSEvent msEvent, FmPolicyRules fmPolicyRules, Integer ruleId, String successMsg, String errorMsg) {
        SupplementaryConditions conditions = getSupplementaryConditions(msEvent, fmPolicyRules);
        conditions.setDelayedAlarm(1);
        LiteflowResponse response = ruleExecutorService.executeSingleRule(String.valueOf(ruleId), conditions);
        if (response.isSuccess()) {
            log.info(successMsg, ruleId);
            SupplementaryConditions contextBean = response.getContextBean(SupplementaryConditions.class);
            handleInheritance(contextBean, fmPolicyRules);

        } else {
            log.info(errorMsg, response.getCause());
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
     *
     * @param msEvent
     */
    public void executeRules(MSEvent msEvent) {
        try {
            SupplementaryConditions conditions = assembleSupplementaryConditions(msEvent);
            List<LiteflowResponse> liteflowResponses = ruleExecutorService.executeRules(conditions);
            for (LiteflowResponse response : liteflowResponses) {
                if (response.isSuccess()) {
                    try {
                        SupplementaryConditions contextBean = response.getContextBean(SupplementaryConditions.class);
                        Long recordId = contextBean.getMsEvent().getRecordId();
                        FmPolicyRules fmPolicyRules = contextBean.getFmPolicyRules();
                        log.info("execute success,recordId, ruleId:{}", recordId, response.getChainId());
                        handleInheritance(contextBean, fmPolicyRules);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.info(" execute fail, ruleId:{}", response.getChainId());
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

    private void handleInheritance(SupplementaryConditions contextBean, FmPolicyRules fmPolicyRules) {
        int inheritType = fmPolicyRules.getInheritType();
        InheritanceStrategy strategy = inheritanceStrategyMap.get(inheritType);
        if (strategy != null) {
            List<Long> childIds = new ArrayList<>();
            strategy.process(contextBean, childIds, fmPolicyRules);
        } else {
            log.info("未找到对应的继承策略处理器：{}", inheritType);
        }
    }
}
