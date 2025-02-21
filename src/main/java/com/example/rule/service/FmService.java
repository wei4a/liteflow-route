package com.example.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;

import java.util.List;

public interface FmService extends IService<MSEvent> {
    void inheritMerge(MSEvent newEvent);

    void updateInherit(Long recordId, List<Long> childIds, Long id);

    MSEvent deriveNewAlarm(SupplementaryConditions contextBean, List<Long> childIds, FmPolicyRules fmPolicyRules);

    void mergeAndFinishInherit(MSEvent newEvent, List<Long> childIds, FmPolicyRules fmPolicyRules);

    void finishedReleationShip(MSEvent parent, List<Long> childIds, FmPolicyRules fmPolicyRules);
}
