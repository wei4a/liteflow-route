package com.example.rule.component.script;

import com.example.rule.model.MSEvent;
import com.example.rule.model.SupplementaryConditions;
import com.example.rule.util.ScriptLogOutput;
import com.yomahub.liteflow.script.ScriptExecuteWrap;
import com.yomahub.liteflow.script.body.CommonScriptBody;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhetherDeriveScript implements CommonScriptBody {
    @Override
    public Void body(ScriptExecuteWrap scriptExecuteWrap) {
        SupplementaryConditions spConditions =scriptExecuteWrap.cmp.getContextBean(SupplementaryConditions.class);
        ScriptLogOutput log = spConditions.getScriptLogOutput();
        log.print("关联条件开始======================================");
        //示例 网元去重之后大于阈值衍生
        List<MSEvent> childrens = spConditions.getChildrens();
        log.print("关联条件子事件数量：" + childrens.size());
        //遍历childrens 获取网元名称并去重计数
        if (CollectionUtils.isNotEmpty(childrens)) {
            Set<String> netElementNames = new HashSet<>();
            for (MSEvent children : childrens) {
                netElementNames.add(children.getDeviceName());
            }
            if (netElementNames.size() >= spConditions.getFmPolicyRules().getThreshold()) {
                spConditions.getScriptLogOutput().print("当前事件子事件大于阈值，衍生");
                spConditions.setDoDerive(true);
            } else {
                spConditions.getScriptLogOutput().print("当前事件子事件小于阈值，不进行判断");

            }
        }
        return null;
    }
}
