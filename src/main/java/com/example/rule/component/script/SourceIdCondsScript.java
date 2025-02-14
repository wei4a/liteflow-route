package com.example.rule.component.script;

import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.yomahub.liteflow.script.ScriptExecuteWrap;
import com.yomahub.liteflow.script.body.CommonScriptBody;
import com.yomahub.liteflow.spi.holder.ContextAwareHolder;

import java.util.List;

public class SourceIdCondsScript implements CommonScriptBody {
    @Override
    public Void body(ScriptExecuteWrap scriptExecuteWrap) {
        SupplementaryConditions spConditions = ContextAwareHolder.loadContextAware().getBean(SupplementaryConditions.class);
        //示例相同网元
        List<String> sourceIdConds = spConditions.getSourceIdConds();
        MSEvent msEvent = spConditions.getMsEvent();
        sourceIdConds.add(msEvent.getDeviceName());
        return null;
    }
}
