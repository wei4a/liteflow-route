package com.example.rule.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.google.common.collect.Lists;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@LiteflowComponent("derivedAlarmParamsCmp")
public class DerivedAlarmParamsCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        FmPolicyRules fmPolicyRules = spConditions.getFmPolicyRules();
        int inheritType = fmPolicyRules.getInheritType();
        if (inheritType == 2 || (inheritType == 1 && spConditions.isSubAlarm())) {
            LambdaQueryWrapper<MSEvent> commonParams = spConditions.getCommonParams();
            LambdaQueryWrapper<MSEvent> derivedAlarmParams = commonParams.clone();
            String primaryEventId = fmPolicyRules.getPrimaryEventId();
            // 增强异常处理
            if (primaryEventId != null && !primaryEventId.trim().isEmpty()) {
                List<String> primaryEventIds = Arrays.asList(primaryEventId.split(","));
                derivedAlarmParams.in(MSEvent::getEventId, primaryEventIds);
                spConditions.setDerivedAlarmParams(derivedAlarmParams);
            } else {
                // 处理 primaryEventId 为空或 null 的情况
                throw new IllegalArgumentException("PrimaryEventId cannot be null or empty");
            }
        }
    }
}
