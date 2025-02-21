package com.example.rule.service.inheritance;

import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.service.FmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DeriveInheritanceStrategy implements InheritanceStrategy {
    @Resource
    private FmService fmService;

    @Override
    public void process(SupplementaryConditions contextBean, List<Long> childIds, FmPolicyRules fmPolicyRules) {
        if (contextBean.isDoDerive()) {
            MSEvent newEvent = fmService.deriveNewAlarm(contextBean, childIds, fmPolicyRules);
            fmService.mergeAndFinishInherit(newEvent, childIds, fmPolicyRules);
        }
    }
}