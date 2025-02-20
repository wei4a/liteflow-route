package com.example.rule.component;

import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.MDC;

import java.util.List;

@LiteflowComponent("doExistsDerivedAlarmCmp")
public class DoExistsDerivedAlarmCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        MDC.put("requestId", this.getChainId());
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        FmPolicyRules fmPolicyRules = spConditions.getFmPolicyRules();
        if (fmPolicyRules.getInheritType() == 2) {
            List<MSEvent> parents = spConditions.getParents();
            if (CollectionUtils.isNotEmpty(parents)) {
                spConditions.setDoDerive(true);
            }
        } else if (fmPolicyRules.getInheritType() == 1) {
            if(spConditions.isSubAlarm()){
                List<MSEvent> parents = spConditions.getParents();
                if(CollectionUtils.isNotEmpty(parents)){
                    spConditions.setDoMainSubRelation(true);
                }
            }else {
                List<MSEvent> childrens = spConditions.getChildrens();
                if(CollectionUtils.isNotEmpty(childrens)){
                    spConditions.setDoMainSubRelation(true);
                }
            }
        }
    }

}
