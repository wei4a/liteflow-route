package com.example.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rule.model.MSEvent;

import java.util.List;

public interface FmService extends IService<MSEvent> {
    void inheritMerge(MSEvent newEvent);

    void updateInherit(Long recordId, List<Long> childIds, Long id);
}
