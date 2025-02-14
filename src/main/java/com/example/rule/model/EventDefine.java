package com.example.rule.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("fm_event_define")
public class EventDefine implements Serializable, Cloneable{

    private static final long serialVersionUID = 1L;

    private String eventId;
    private String deviceSpecialty;
    private String deviceService;
    private String deviceServiceSub;
    private String deviceVendor;
    private String deviceType;
    private String deviceVersion;
    private String vendorEventId;
    private String vendorEventFacl;
    private String vendorEventSeve;
    private String title;
    private String name;
    private int severity;
    private String comments;
    private int facility;
    private String facilityLogic;
    private String facilityLogicSub;
    private String referenceDevice;
    private String referenceService;
    private String associateFlag;
    private int harvestType;
    private String category;
    private int isReport;
    private String specFileName;
    private String specRowNum;
    private String modifyUser;
    private Date modifyTime;
    private String event1;
    private String event2;
    private String event3;
    private String event4;
    private String event5;
    private String event6;
    private String event7;
    private String event8;
    private String event9;
    private String event10;
    private String event11;
    private String event12;
    private String event13;
    private String event14;
    private String event15;

    @Override
    public EventDefine clone() {
        try {
            EventDefine clone = (EventDefine) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
