package com.example.rule.component;

import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.service.FmPolicyRuleService;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.slf4j.MDC;

@LiteflowComponent("delayRulesJudgeCmp")
public class DelayRulesJudgeCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        MDC.put("requestId", this.getChainId());
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        int delayedAlarm = spConditions.getDelayedAlarm();
        MSEvent msEvent = spConditions.getMsEvent();
        FmPolicyRules fmPolicyRules = spConditions.getFmPolicyRules();
        FmPolicyRuleService fmPolicyRuleService = spConditions.getFmPolicyRuleService();
        if (fmPolicyRules.getIsDelay()>0&&delayedAlarm<1) {
            //延迟规则
            fmPolicyRuleService.processDelayedRule(msEvent, fmPolicyRules);
            this.setIsEnd(true);
        }
    }
}
