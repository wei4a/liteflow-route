package com.example.rule.config;

import com.example.rule.service.inheritance.DeriveInheritanceStrategy;
import com.example.rule.service.inheritance.InheritanceStrategy;
import com.example.rule.service.inheritance.MainSubInheritanceStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RuleConfig {
    @Bean
    public Map<Integer, InheritanceStrategy> inheritanceStrategyMap(
            DeriveInheritanceStrategy deriveStrategy,
            MainSubInheritanceStrategy mainSubStrategy) {
        Map<Integer, InheritanceStrategy> strategyMap = new HashMap<>();
        strategyMap.put(2, deriveStrategy);
        strategyMap.put(1, mainSubStrategy);
        return strategyMap;
    }
}