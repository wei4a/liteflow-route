package com.example.rule.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MqMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer ruleId;
    private Long delayTime;
    private Long recordId;
    private String eventId;
    private String title;
}
