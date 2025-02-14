package com.example.rule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@MapperScan("com.example.rule.mapper")
public class RuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuleApplication.class, args);
    }

}
