package com.example.rule.service.inheritance;

import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.SupplementaryConditions;

import java.util.List;

public interface InheritanceStrategy {
    void process(SupplementaryConditions contextBean, List<Long> childIds, FmPolicyRules fmPolicyRules);
}