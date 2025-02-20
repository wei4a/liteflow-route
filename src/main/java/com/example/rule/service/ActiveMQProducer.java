package com.example.rule.service;

import com.example.rule.model.MSEvent;
import com.example.rule.model.MqMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Message;

@Service
@Slf4j
public class ActiveMQProducer {
    @Autowired
    private JmsTemplate jmsTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(String destination, MSEvent alert) {
        try {
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
        ActiveMQTopic destination = new ActiveMQTopic("rule-delay-queue");
        try {
            String s = objectMapper.writeValueAsString(mqMessage);
            jmsTemplate.send(destination, session -> {
                Message msg = session.createTextMessage(s);
                msg.setLongProperty("AMQ_SCHEDULED_DELAY", mqMessage.getDelayTime());
                return msg;
            });
        } catch (JmsException e) {
            log.error("Failed to send delayed rule message: {}", e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert message to JSON: {}", e.getMessage(), e);
        }
        log.info("Delayed rule message sent: {}", mqMessage);
    }
}
