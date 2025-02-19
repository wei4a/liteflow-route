package com.example.rule.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.service.FmService;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@LiteflowComponent("parentAndChildCmp")
@Slf4j
public class ParentAndChildCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        List<MSEvent> parents = spConditions.getParents();
        List<MSEvent> childrens = spConditions.getChildrens();
        FmPolicyRules fmPolicyRules = spConditions.getFmPolicyRules();
        FmService fmService = spConditions.getFmService();
        LambdaQueryWrapper<MSEvent> derivedAlarmParams = spConditions.getDerivedAlarmParams();
        LambdaQueryWrapper<MSEvent> triggeredParams = spConditions.getTriggeredParams();
        Page<MSEvent> page = new Page<>(1, 1000);
        if(fmPolicyRules.getInheritType()==2){
            //衍生
            Page<MSEvent> derivedAlarms = fmService.page(page, derivedAlarmParams);
            log.info("父告警个数{}告警已经衍生", derivedAlarms.getTotal());
            parents.addAll(derivedAlarms.getRecords());

            Page<MSEvent> childs = fmService.page(page, triggeredParams);
            log.info("子告警个数{}告警已经关联", childs.getTotal());
            childrens.addAll(childs.getRecords());
        }else if(fmPolicyRules.getInheritType()==1){
            //主次关联
            if(spConditions.isSubAlarm()){
                childrens.add(spConditions.getMsEvent());
                Page<MSEvent> derivedAlarms = spConditions.getFmService().page(page, derivedAlarmParams);
                parents.addAll(derivedAlarms.getRecords());
            }else {
                parents.add(spConditions.getMsEvent());
                Page<MSEvent> childs = spConditions.getFmService().page(page, triggeredParams);
                childrens.addAll(childs.getRecords());
            }
        }
    }
}
