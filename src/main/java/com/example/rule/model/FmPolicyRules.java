package com.example.rule.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName FM_POLICY_RULES_NEW
 */
@TableName(value = "FM_POLICY_RULES_NEW")
@Data
public class FmPolicyRules implements Serializable {

    private Long id;
    private String ruleName;
    private String department;
    private String modifyUser;
    private String userPhone;
    private String relationship;
    private String description;
    private int inheritType;
    private String primaryTitle;
    private String secondaryTitle;
    private int validated;
    private String triggerCondition;
    private String locateInfo;
    private String irmsInfo;
    private int timeWindow;
    private int threshold;
    private String clearMode;
    private String subject;
    private String relationType;
    private int isDeploy;
    private String paras1;
    private String paras2;
    private String paras3;
    private String paras4;
    private String paras5;
    private Date modifyTime;
    private String publicOpinionLevel;
    private String publicOpinionTitle;
    private String publicOpinionFlag;
    private String weatherWarningTime;
    private String projectStatus;
    private int isCopy;
    private String beanid;
    private int isAssociatedScene;
    private String sceneName;
    private String areaName;
    private String subRegion1;
    private String subRegion2;
    private String subRegion3;
    private String subRegion4;
    private String yqSplicingConditions;
    private int isDelay;
    private int delayTime;
    private String primaryEventId;
    private String secondaryEventId;
    private String elData;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}