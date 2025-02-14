package com.example.rule.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rule.mapper.FmPolicyRuleMapper;
import com.example.rule.model.FmPolicyRules;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RuleUpdateTask implements CommandLineRunner {
    @Resource
    private FmPolicyRuleMapper fmPolicyRuleMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Redis 中规则数据的 Key 前缀
     */
    private static final String RULE_KEY_PREFIX = "rule:";

    /**
     * 规则数据的过期时间（单位：分钟）
     */
    private static final long BASE_EXPIRE_TIME = 30;

    /**
     * 过期时间的随机偏移量范围（单位：分钟）
     */
    private static final long EXPIRE_TIME_OFFSET = 10;

    /**
     * 随机数生成器
     */
    private final Random random = new Random();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 定时更新规则信息（每 30 分钟执行一次）
     */
    @Scheduled(fixedDelay = 30 * 60 * 1000) // 每 30 分钟执行一次
    public void updateRulesNew() {
        log.info("Starting scheduled rule update task...");
        // 从数据库加载所有规则
        List<FmPolicyRules> rules = fmPolicyRuleMapper.selectList(Wrappers.lambdaQuery(FmPolicyRules.class).eq(FmPolicyRules::getIsDeploy, 1));
        if (rules.isEmpty()) {
            log.info("No rules found to update.");
            return;
        }
        processRules(rules);
    }

    private void processRules(List<FmPolicyRules> rules) {
        Map<String, FmPolicyRules> map = rules.stream().collect(Collectors.toMap(FmPolicyRules::getRuleName,
                rule -> rule, (existing, replacement) -> existing));
        for (Map.Entry<String, FmPolicyRules> entry : map.entrySet()) {
            String key = entry.getKey();
            FmPolicyRules rule = entry.getValue();
            String ruleGroupKey = RULE_KEY_PREFIX + key;
            try {
                // 删除旧的 Redis 缓存
                redisTemplate.delete(ruleGroupKey);
                // 将新的规则信息存储到 Redis Set 中
                SetOperations<String, String> setOps = redisTemplate.opsForSet();
                // 将规则对象序列化为 JSON 字符串
                String ruleJson = objectMapper.writeValueAsString(rule);
                // 存储到 Redis Set 中
                setOps.add(ruleGroupKey, ruleJson);
                log.info("Rule cached in Redis: {} (group: {})", rule.getId(), key);
                // 统一设置过期时间
                long expireTime = BASE_EXPIRE_TIME + random.nextInt((int) EXPIRE_TIME_OFFSET);
                redisTemplate.expire(ruleGroupKey, expireTime, TimeUnit.MINUTES);
                log.info("Rules updated for group: {} (expire in {} minutes)", key, expireTime);
            } catch (Exception e) {
                log.error("Failed to serialize rule: {}", e.getMessage());
            }
        }
    }

    /**
     * 定时更新规则信息（每 30 分钟执行一次）
     */
    //@Scheduled(fixedDelay = 30 * 60 * 1000) // 每 30 分钟执行一次
    public void updateRules() {
        log.info("Starting scheduled rule update task...");
        // 从数据库加载所有规则
        List<FmPolicyRules> rules = fmPolicyRuleMapper.selectList(Wrappers.lambdaQuery(FmPolicyRules.class).eq(FmPolicyRules::getIsDeploy, 1));
        if (rules.isEmpty()) {
            log.info("No rules found to update.");
            return;
        }
        // 按规则组 ID 分组
        Map<String, List<FmPolicyRules>> eventIddRules = groupRulesByField(rules, FmPolicyRules::getSecondaryEventId);
        // 按规则组 title 分组
        Map<String, List<FmPolicyRules>> titleRules = groupRulesByField(rules, FmPolicyRules::getPrimaryTitle);
        // 处理分组规则
        processGroupedRules(eventIddRules);
        processGroupedRules(titleRules);
    }

    /**
     * 处理分组后的规则
     *
     * @param groupedRules 分组后的规则映射
     */
    private void processGroupedRules(Map<String, List<FmPolicyRules>> groupedRules) {
        for (Map.Entry<String, List<FmPolicyRules>> entry : groupedRules.entrySet()) {
            String key = entry.getKey();
            List<FmPolicyRules> ruleList = entry.getValue();
            if (CollectionUtils.isEmpty(ruleList)) {
                continue;
            }
            // 删除旧的 Redis 缓存
            String ruleGroupKey = RULE_KEY_PREFIX + key;
            redisTemplate.delete(ruleGroupKey);

            // 将新的规则信息存储到 Redis Set 中
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            for (FmPolicyRules rule : ruleList) {
                try {
                    // 将规则对象序列化为 JSON 字符串
                    String ruleJson = String.valueOf(rule.getId());
                    // 存储到 Redis Set 中
                    setOps.add(ruleGroupKey, ruleJson);
                    log.info("Rule cached in Redis: {} (group: {})", rule.getId(), key);
                } catch (Exception e) {
                    log.error("Failed to serialize rule: {}", e.getMessage());
                }
            }

            // 统一设置过期时间
            long expireTime = BASE_EXPIRE_TIME + random.nextInt((int) EXPIRE_TIME_OFFSET);
            redisTemplate.expire(ruleGroupKey, expireTime, TimeUnit.MINUTES);
            log.info("Rules updated for group: {} (expire in {} minutes)", key, expireTime);
        }
    }

    /**
     * 根据指定字段对规则进行分组
     *
     * @param rules          规则列表
     * @param fieldExtractor 字段提取器
     * @return 分组后的规则映射
     */
    private Map<String, List<FmPolicyRules>> groupRulesByField(List<FmPolicyRules> rules, Function<FmPolicyRules, String> fieldExtractor) {
        return rules.stream()
                .filter(rule -> StringUtils.isNotBlank(fieldExtractor.apply(rule)))
                .flatMap(rule -> Arrays.stream(fieldExtractor.apply(rule).split(","))
                        .filter(StringUtils::isNotBlank)
                        .map(String::trim)
                        .map(key -> new AbstractMap.SimpleEntry<>(key, rule)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    @Override
    public void run(String... args) {
        log.info("Loading rules on startup...");
        updateRules();
    }
}
