package com.example.rule.service;

import com.example.rule.model.MSEvent;
import com.example.rule.model.MqMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.ObjectMessage;

@Service
@Slf4j
public class ActiveMQProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String destination, MSEvent alert) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(alert);
            jmsTemplate.convertAndSend(destination, message);
        } catch (Exception e) {
            log.error("Failed to send message: {}", e.getMessage(), e);
        }
    }
    /**
     * 发送延迟消息
     *
     * @param mqMessage 规则消息
     */
    public void sendDelayedRule(MqMessage mqMessage) {
        jmsTemplate.send("rule-delay-queue", session -> {
            ObjectMessage objectMessage = session.createObjectMessage(mqMessage);
            objectMessage.setLongProperty("AMQ_SCHEDULED_DELAY", mqMessage.getDelayTime()); // 设置延迟时间
            return objectMessage;
        });
        log.info("Delayed rule message sent: {}", mqMessage);
    }
}
