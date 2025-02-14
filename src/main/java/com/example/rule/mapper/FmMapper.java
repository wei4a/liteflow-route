package com.example.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rule.model.MSEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FmMapper extends BaseMapper<MSEvent> {
}
