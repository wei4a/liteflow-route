package com.example.rule.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rule.service.FmPolicyRuleService;
import com.example.rule.service.FmService;
import com.example.rule.service.RuleMatcher;
import com.example.rule.util.ScriptLogOutput;
import com.yomahub.liteflow.script.annotation.ScriptBean;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@ScriptBean("supplementaryConditions")
@Data
@Component
public class SupplementaryConditions {
    @Resource
    private FmService fmService;
    @Resource
    private FmPolicyRuleService fmPolicyRuleService;
    @Resource
    public ScriptLogOutput scriptLogOutput= new ScriptLogOutput();
    @Resource
    public RuleMatcher ruleMatcher;
    //查询子告警条件
    LambdaQueryWrapper<MSEvent> triggeredParams = Wrappers.lambdaQuery();
    //查询父告警条件
    LambdaQueryWrapper<MSEvent> derivedAlarmParams = Wrappers.lambdaQuery();
    //通用条件
    LambdaQueryWrapper<MSEvent> CommonParams = Wrappers.lambdaQuery();
    //告警信息
    public MSEvent msEvent;
    //规则信息
    public FmPolicyRules fmPolicyRules;
    //衍生父事件
    public List<MSEvent> parents = new ArrayList<>();
    public String primaryEventId;
    public List<MSEvent> childrens = new ArrayList<>();
    //sourceId组成条件
    public List<String> sourceIdConds = new ArrayList<>();
    public boolean doDerive = false;
    public boolean doMainSubRelation = false;
    public boolean doUpdateEvent = false;
    public boolean subAlarm = true;
    public int inheritType;
    public int delayedAlarm;//0:立即执行，1:延迟执行
}
