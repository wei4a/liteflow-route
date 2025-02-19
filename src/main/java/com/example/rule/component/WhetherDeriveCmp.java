package com.example.rule.component;

import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@LiteflowComponent("whetherDeriveCmp")
@Slf4j
public class WhetherDeriveCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        List<MSEvent> childrens = spConditions.getChildrens();
        List<MSEvent> parents = spConditions.getParents();
        FmPolicyRules fmPolicyRules = spConditions.getFmPolicyRules();
        if (fmPolicyRules.getInheritType() == 2) {
            if (CollectionUtils.isNotEmpty(parents)) {
                log.info("父告警个数{}告警已经衍生", parents.size());
                spConditions.setDoDerive(false);
                this.setIsEnd(true);
            } else {
                spConditions.setDoDerive(true);
                //衍生
                if (CollectionUtils.isEmpty(childrens)) {
                    log.info("当前事件没有子事件，不需要进行判断");
                    spConditions.setDoDerive(false);
                    this.setIsEnd(true);
                }
                if (childrens.size() < fmPolicyRules.getThreshold()) {
                    log.info("当前事件子事件小于阈值，不需要进行判断");
                    spConditions.setDoDerive(false);
                    this.setIsEnd(true);
                }
            }
        } else if (fmPolicyRules.getInheritType() == 1) {
            spConditions.setDoMainSubRelation(true);
            //主次
            if (CollectionUtils.isEmpty(childrens) && !spConditions.isSubAlarm()) {
                log.info("当前事件没有子事件，不需要进行判断");
                spConditions.setDoMainSubRelation(false);
                this.setIsEnd(true);
            } else if (CollectionUtils.isEmpty(parents) && spConditions.isSubAlarm()) {
                log.info("当前事件没有父事件，不需要进行判断");
                spConditions.setDoMainSubRelation(false);
                this.setIsEnd(true);
            }
        }
    }
}
