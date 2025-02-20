package com.example.rule.service;

import com.example.rule.model.MSEvent;
import com.example.rule.model.MqMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ActiveMQConsumer {
    @Resource
    private RuleMatcher ruleMatcher;
    @Resource
    private FmService fmService;
    @JmsListener(destination = "alert-topic")
    public void receiveMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MSEvent msEvent = objectMapper.readValue(message, MSEvent.class);
            ruleMatcher.executeRules(msEvent);
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
        }
    }
    @JmsListener(destination = "alert-topic-recordid")
    public void receiveMessageId(String recordId) {
        try {
            MSEvent msEvent = fmService.getById(recordId);
            ruleMatcher.executeRules(msEvent);
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
        }
    }

    /**
     * 监听延迟队列并处理消息
     *
     * @param mqMessage 规则消息
     */
    @JmsListener(destination = "rule-delay-queue")
    public void processDelayedRule(MqMessage mqMessage) {
        log.info("Received delayed rule message: {}", mqMessage);
        // 执行规则
        try {
            ruleMatcher.matchAndExecuteDelayRules(mqMessage);
        } catch (Exception e) {
            log.error("Failed to process delayed rule message: {}", e.getMessage(), e);
        }
    }
}
