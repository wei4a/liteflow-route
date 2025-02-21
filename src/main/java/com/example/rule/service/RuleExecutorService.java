package com.example.rule.service;

import com.example.rule.model.SupplementaryConditions;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class RuleExecutorService {
    @Resource
    private FlowExecutor flowExecutor;
    @Value("${rule.route}")
    private String namespace;

    public List<LiteflowResponse> executeRules(SupplementaryConditions conditions) {
        return flowExecutor.executeRouteChain(namespace, null, conditions);
    }

    public LiteflowResponse executeSingleRule(String ruleId, SupplementaryConditions conditions) {
        return flowExecutor.execute2Resp(ruleId, null, conditions);
    }
}