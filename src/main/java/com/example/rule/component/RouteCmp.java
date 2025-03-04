package com.example.rule.component;

import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.RouteDatas;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.service.FmPolicyRuleService;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeBooleanComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

@LiteflowComponent("routeCmp")
@Slf4j
public class RouteCmp extends NodeBooleanComponent {
    @Override
    public boolean processBoolean() throws Exception {
        MDC.put("requestId", this.getChainId());
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
        FmPolicyRuleService fmPolicyRuleService = spConditions.getFmPolicyRuleService();
        MSEvent msEvent = spConditions.getMsEvent();
        RouteDatas cmpData = this.getCmpData(RouteDatas.class);
        String ruleName = cmpData.getRuleName();
        String secondaryTitle = cmpData.getSecondaryTitle();
        String secondaryEventId = cmpData.getSecondaryEventId();
        if (StringUtils.contains(secondaryEventId, msEvent.getEventId())
                || StringUtils.contains(secondaryTitle, msEvent.getTitle())) {
            FmPolicyRules fmPolicyRules = fmPolicyRuleService.getRule(ruleName);
            if (fmPolicyRules.getIsDeploy() == 0) {
                log.error("规则{}未部署，请检查规则配置.", ruleName);
                return false;
            }
            spConditions.setPrimaryEventId(fmPolicyRules.getPrimaryEventId());
            spConditions.setFmPolicyRules(fmPolicyRules);
            log.info("规则{}匹配成功.", ruleName);
            return true;
        }
        log.info("告警{} 规则{}匹配失败.", msEvent.getRecordId(), ruleName);
        return false;
    }
}
