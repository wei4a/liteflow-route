package com.example.rule.component;

import com.example.rule.model.SupplementaryConditions;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("clearTagCmp")
public class ClearTagCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        SupplementaryConditions spConditions = this.getContextBean(SupplementaryConditions.class);
    }
}
