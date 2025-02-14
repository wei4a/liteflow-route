package com.example.rule.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.util.RuleUtils;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@LiteflowComponent("triggeredParamsCmp")
@Slf4j
public class TriggeredParamsCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        FmPolicyRules policyRule = spConditions.getFmPolicyRules();
        int inheritType = policyRule.getInheritType();
        if (inheritType == 2|| (inheritType == 1 && !spConditions.isSubAlarm())){
            LambdaQueryWrapper<MSEvent> commonParams = spConditions.getCommonParams();
            LambdaQueryWrapper<MSEvent> triggeredParams = commonParams.clone();
            if (StringUtils.isNotEmpty(policyRule.getSecondaryEventId())) {
                try {
                    triggeredParams.in(MSEvent::getEventId, RuleUtils.convertStringToList(policyRule.getSecondaryEventId()));
                } catch (Exception e) {
                    log.error("Failed to convert SecondaryEventId to list", e);
                }
            } else if (StringUtils.isNotEmpty(policyRule.getSecondaryTitle())) {
                try {
                    triggeredParams.in(MSEvent::getTitle, RuleUtils.convertStringToList(policyRule.getSecondaryTitle()));
                } catch (Exception e) {
                    log.error("Failed to convert SecondaryTitle to list", e);
                }
            }
            spConditions.setTriggeredParams(triggeredParams);
        }

    }
}
