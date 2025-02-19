package com.example.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;

import java.util.List;

public interface FmPolicyRuleService extends IService<FmPolicyRules> {
    void addRule(FmPolicyRules rule);

    List<FmPolicyRules> getRulesByEventId(String groupId);

    List<FmPolicyRules> getRulesByTitle(String title);

    void deleteRule(Long id);
    FmPolicyRules getRule(String ruleName);
    void processDelayedRule(MSEvent msEvent, FmPolicyRules rule);
}
