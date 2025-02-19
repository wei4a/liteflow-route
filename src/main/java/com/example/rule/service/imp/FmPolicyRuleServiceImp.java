package com.example.rule.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rule.mapper.FmPolicyRuleMapper;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.MqMessage;
import com.example.rule.service.ActiveMQProducer;
import com.example.rule.service.FmPolicyRuleService;
import com.example.rule.util.RuleUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@Slf4j
public class FmPolicyRuleServiceImp extends ServiceImpl<FmPolicyRuleMapper, FmPolicyRules> implements FmPolicyRuleService {
    @Resource
    private FmPolicyRuleMapper fmPolicyRuleMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ActiveMQProducer activeMQProducer;
    /**
     * Redis 中规则组的 Key 前缀
     */
    private static final String RULE_GROUP_KEY_PREFIX = "rules:";
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

    @Override
    public void addRule(FmPolicyRules rule) {
        // 插入规则
        fmPolicyRuleMapper.insert(rule);
        String secondaryEventId = rule.getSecondaryEventId();
        if (StringUtils.isNotBlank(secondaryEventId)) {
            addRuleToRedis(rule, secondaryEventId);
        }
        log.info("Rule added and cached in Redis: {}", rule.getId());
    }

    public FmPolicyRules getRule(String ruleName) {
        String ruleGroupKey = RULE_GROUP_KEY_PREFIX + ruleName;
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        Set<String> ruleJsons = setOps.members(ruleGroupKey);
        if (CollectionUtils.isEmpty(ruleJsons)) {
            // 使用分布式锁确保同一时间只有一个线程加载数据到 Redis 中
            RLock lock = redissonClient.getLock("lock:" + ruleGroupKey);
            try {
                if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                    // 再次检查 Redis 中是否有数据，防止多线程竞争
                    ruleJsons = setOps.members(ruleGroupKey);
                    if (CollectionUtils.isEmpty(ruleJsons)) {
                        // 如果 Redis 中没有数据，从数据库加载
                        FmPolicyRules rule = fmPolicyRuleMapper.selectOne(Wrappers.lambdaQuery(FmPolicyRules.class)
                                .eq(FmPolicyRules::getRuleName, ruleName));
                        if (!Objects.isNull(rule)) {
                            // 将规则信息存储到 Redis Set 中
                            try {
                                String ruleJson = objectMapper.writeValueAsString(rule);
                                setOps.add(ruleGroupKey, ruleJson);
                            } catch (Exception e) {
                                log.error("Failed to serialize rule: {}", e.getMessage());
                            }
                            redisTemplate.expire(ruleGroupKey, 1, TimeUnit.HOURS); // 设置过期时间为1小时
                        }
                        return rule;
                    }
                }
            } catch (InterruptedException e) {
                log.error("Failed to acquire lock for event : {}", e.getMessage());
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        try {
            List<String> list = Lists.newArrayList(ruleJsons);
            return objectMapper.readValue(list.get(0), FmPolicyRules.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize rule", e);
        }
    }

    @Override
    public void processDelayedRule(MSEvent msEvent, FmPolicyRules rule) {
        long timeReal = RuleUtils.getDelayTimeReal(msEvent, rule);
        MqMessage mqMessage = RuleUtils.getMqMessage(msEvent, rule, timeReal);
        activeMQProducer.sendDelayedRule(mqMessage);
        log.info("Delayed rule message sent: {}", mqMessage);
    }

    /**
     * 将规则添加到 Redis 中
     *
     * @param rule             规则对象
     * @param secondaryEventId 二级事件 ID
     */
    private void addRuleToRedis(FmPolicyRules rule, String secondaryEventId) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        try {
            String ruleJson = String.valueOf(rule.getId());
            Arrays.stream(secondaryEventId.split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .forEach(trimmedEventId -> {
                        String ruleGroupKey = RULE_GROUP_KEY_PREFIX + trimmedEventId;
                        redisTemplate.execute(new SessionCallback<Object>() {
                            @Override
                            public Object execute(RedisOperations operations) throws DataAccessException {
                                operations.multi();
                                setOps.add(ruleGroupKey, ruleJson);
                                long expireTime = BASE_EXPIRE_TIME + random.nextInt((int) EXPIRE_TIME_OFFSET);
                                operations.expire(ruleGroupKey, expireTime, TimeUnit.MINUTES);
                                return operations.exec();
                            }
                        });
                    });
        } catch (Exception e) {
            log.error("Failed to serialize rule: {}", e.getMessage());
        }
    }


    @Override
    public List<FmPolicyRules> getRulesByEventId(String eventId) {
        return getRulesByKey(eventId, rule -> Wrappers.lambdaQuery(FmPolicyRules.class)
                .like(FmPolicyRules::getSecondaryEventId, eventId));
    }

    @Override
    public List<FmPolicyRules> getRulesByTitle(String title) {
        return getRulesByKey(title, rule -> Wrappers.lambdaQuery(FmPolicyRules.class)
                .like(FmPolicyRules::getPrimaryTitle, title));
    }

    /**
     * 根据键获取规则列表
     *
     * @param key   键
     * @param query 查询条件构造器
     * @return 规则列表
     */
    private List<FmPolicyRules> getRulesByKey(String key, Function<FmPolicyRules, LambdaQueryWrapper<FmPolicyRules>> query) {
        String ruleGroupKey = RULE_GROUP_KEY_PREFIX + key;
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        Set<String> ruleJsons = setOps.members(ruleGroupKey);
        if (CollectionUtils.isEmpty(ruleJsons)) {
            // 使用分布式锁确保同一时间只有一个线程加载数据到 Redis 中
            RLock lock = redissonClient.getLock("lock:" + ruleGroupKey);
            try {
                if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                    // 再次检查 Redis 中是否有数据，防止多线程竞争
                    ruleJsons = setOps.members(ruleGroupKey);
                    if (CollectionUtils.isEmpty(ruleJsons)) {
                        // 如果 Redis 中没有数据，从数据库加载
                        LambdaQueryWrapper<FmPolicyRules> queryWrapper = query.apply(null);
                        List<FmPolicyRules> rules = fmPolicyRuleMapper.selectList(queryWrapper);
                        if (!rules.isEmpty()) {
                            // 将规则信息存储到 Redis Set 中
                            rules.forEach(rule -> {
                                try {
                                    String ruleJson = String.valueOf(rule.getId());
                                    setOps.add(ruleGroupKey, ruleJson);
                                } catch (Exception e) {
                                    log.error("Failed to serialize rule: {}", e.getMessage());
                                }
                            });
                            redisTemplate.expire(ruleGroupKey, 1, TimeUnit.HOURS); // 设置过期时间为1小时
                        }
                        return rules;
                    }
                }
            } catch (InterruptedException e) {
                log.error("Failed to acquire lock for event {}: {}", key, e.getMessage());
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        //根据规则ID查询规则信息
        List<Long> ruleIds = ruleJsons.stream().map(Long::parseLong).collect(Collectors.toList());
        return fmPolicyRuleMapper.selectList(Wrappers.lambdaQuery(FmPolicyRules.class).in(FmPolicyRules::getId, ruleIds));
    }

    @Override
    @Transactional
    public void deleteRule(Long id) {
        // 删除数据库中的规则
        FmPolicyRules rule = fmPolicyRuleMapper.selectById(id);
        if (rule != null) {
            int i = fmPolicyRuleMapper.deleteById(id);
            if (i > 0) {
                deleteRuleFromRedis(rule);
            }
            log.info("Rule deleted and cache updated: {}", id);
        }
    }

    /**
     * 从 Redis 中删除规则
     *
     * @param rule 规则对象
     */
    private void deleteRuleFromRedis(FmPolicyRules rule) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        try {
            String ruleJson = String.valueOf(rule.getId());
            Arrays.stream(rule.getSecondaryEventId().split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .forEach(trimmedEventId -> {
                        String ruleGroupKey = RULE_GROUP_KEY_PREFIX + trimmedEventId;
                        setOps.remove(ruleGroupKey, ruleJson);
                    });
            Arrays.stream(rule.getSecondaryEventId().split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .forEach(trimmedTitle -> {
                        String ruleGroupKey = RULE_GROUP_KEY_PREFIX + trimmedTitle;
                        setOps.remove(ruleGroupKey, ruleJson);
                    });
        } catch (Exception e) {
            log.error("Failed to serialize rule: {}", e.getMessage());
        }
    }
}
