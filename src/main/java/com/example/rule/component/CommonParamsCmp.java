package com.example.rule.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.util.RuleUtils;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("commonParamsCmp")
public class CommonParamsCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        MSEvent msEvent = spConditions.getMsEvent();
        FmPolicyRules fmPolicyRules = spConditions.getFmPolicyRules();
        LambdaQueryWrapper<MSEvent> commonParams = spConditions.getCommonParams();
        if (fmPolicyRules.getTimeWindow() > 0) {
            commonParams.gt(MSEvent::getLastTime, RuleUtils.getRuleStartTime(msEvent, fmPolicyRules));
            commonParams.le(MSEvent::getLastTime, msEvent.getLastTime());
        }
        commonParams.eq(MSEvent::getClearTag, 0);
        commonParams.eq(MSEvent::getProjectStatus, RuleUtils.getProjectStatus(fmPolicyRules));
        if (fmPolicyRules.getInheritType() == 1) {
            if (fmPolicyRules.getPrimaryEventId().contains(msEvent.getEventId())) {
                spConditions.setSubAlarm(false);
            } else if (fmPolicyRules.getSecondaryEventId().contains(msEvent.getEventId())) {
                spConditions.setSubAlarm(true);
            }
        }
    }
}
