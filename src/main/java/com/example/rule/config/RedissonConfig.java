package com.example.rule.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedissonConfig {

    @Value("${redisson.singleServerConfig.address}")
    private String redisAddress;

    @Value("${redisson.singleServerConfig.password}")
    private String redisPassword;
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        try {
            config.useSingleServer()
                    .setAddress(redisAddress)
                    .setPassword(redisPassword);
            return Redisson.create(config);
        } catch (Exception e) {
           log.error("RedissonClient init error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Redisson client", e);
        }
    }
}
