package com.example.rule.model;

import lombok.Data;

import java.util.Date;

@Data
public class MSEventInherit extends MSEvent {
    private static final long serialVersionUID = 1L;
    private long ruleId;
    private String ruleName;
    private String relationType;
    private String subject;
    private int inheritType;
    private Date ruleTime;
    private int coreMaxlevel;
    private int coreCounts;
    private int wirelessMaxlevel;
    private int wirelessCounts;
    private int dataMaxlevel;
    private int dataCounts;
    private int transmissionMaxlevel;
    private int transmissionCounts;
    private int signalingMaxlevel;
    private int signalingCounts;
    private String rootCause;
    private int statistic1;
    private int statistic2;
    private int statistic3;
    private int statistic4;
    private int statistic5;
    private int statistic6;
    private int statistic7;
    private int statistic8;
    private int statistic9;
    private int statistic10;
    private int statistic11;
    private int statistic12;
    private int statistic13;
    private int statistic14;
}
