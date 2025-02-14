package com.example.rule.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName FM_EVENT_DEFINE
 */
@TableName(value ="FM_EVENT_DEFINE")
@Data
public class FmEventDefine implements Serializable {
    /**
     * ：事件ID
     */
    private String eventId;

    /**
     * 所属专业
     */
    private String deviceSpecialty;

    /**
     * 所属业务
     */
    private String deviceService;

    /**
     * 所属子业务
     */
    private String deviceServiceSub;

    /**
     * 设备厂家
     */
    private String deviceVendor;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 适用的厂家版本号

     */
    private String deviceVersion;

    /**
     * ：厂家告警号
     */
    private String vendorEventId;

    /**
     * ：厂家原始告警类别
     */
    private String vendorEventFacl;

    /**
     * ：厂家原始告警级别
     */
    private String vendorEventSeve;

    /**
     * ：告警标题
     */
    private String title;

    /**
     * ：告警标准名
     */
    private String name;

    /**
     * ：事件级别
     */
    private Integer severity;

    /**
     * ：告警解释
     */
    private String comments;

    /**
     * ：告警类别
     */
    private Integer facility;

    /**
     * ：告警逻辑分类
     */
    private String facilityLogic;

    /**
     * ：告警逻辑子类
     */
    private String facilityLogicSub;

    /**
     * ：该事件对设备的影响
     */
    private String referenceDevice;

    /**
     * ：该事件对业务的影响
     */
    private String referenceService;

    /**
     * 关联标识
     */
    private String associateFlag;

    /**
     * ：事件类型
     */
    private Integer harvestType;

    /**
     * ：告警种类（故障、事件、恢复）
     */
    private String category;

    /**
     * ：是否需要上报集团(否0是1)
     */
    private Integer isReport;

    /**
     * 文件名
     */
    private String specFileName;

    /**
     * 文件内行数
     */
    private String specRowNum;

    /**
     * 修改用户
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 告警对象类型
     */
    private String event1;

    /**
     * 设备单板(传输网特有)
     */
    private String event2;

    /**
     * 层速率(传输网特有)
     */
    private String event3;

    /**
     * 区域级别 0：小区；1：地市；2：其它(性能事件特有)
     */
    private String event4;

    /**
     * 
     */
    private String event5;

    /**
     * 告警解释辅助字段
     */
    private String event6;

    /**
     * 映射方式
     */
    private String event7;

    /**
     * 
     */
    private String event8;

    /**
     * 是否与对端网元相关(否、是)
     */
    private String event9;

    /**
     * eventOverviewId
     */
    private String event10;

    /**
     * 统一版本详情ID
     */
    private String event11;

    /**
     * 是否启用（不启用，启用）
     */
    private String event12;

    /**
     * 
     */
    private String event13;

    /**
     * 加载关键字，用于eventparser加载用
     */
    private String event14;

    /**
     * 形如k$$k$$$$,约定顺序：厂家告警ID,告警标题,厂家,告警对象类型,设备类型,层速率,设备单板,适用的厂家版本号,厂家告警级别
     */
    private String event15;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FmEventDefine other = (FmEventDefine) that;
        return (this.getEventId() == null ? other.getEventId() == null : this.getEventId().equals(other.getEventId()))
            && (this.getDeviceSpecialty() == null ? other.getDeviceSpecialty() == null : this.getDeviceSpecialty().equals(other.getDeviceSpecialty()))
            && (this.getDeviceService() == null ? other.getDeviceService() == null : this.getDeviceService().equals(other.getDeviceService()))
            && (this.getDeviceServiceSub() == null ? other.getDeviceServiceSub() == null : this.getDeviceServiceSub().equals(other.getDeviceServiceSub()))
            && (this.getDeviceVendor() == null ? other.getDeviceVendor() == null : this.getDeviceVendor().equals(other.getDeviceVendor()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()))
            && (this.getDeviceVersion() == null ? other.getDeviceVersion() == null : this.getDeviceVersion().equals(other.getDeviceVersion()))
            && (this.getVendorEventId() == null ? other.getVendorEventId() == null : this.getVendorEventId().equals(other.getVendorEventId()))
            && (this.getVendorEventFacl() == null ? other.getVendorEventFacl() == null : this.getVendorEventFacl().equals(other.getVendorEventFacl()))
            && (this.getVendorEventSeve() == null ? other.getVendorEventSeve() == null : this.getVendorEventSeve().equals(other.getVendorEventSeve()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSeverity() == null ? other.getSeverity() == null : this.getSeverity().equals(other.getSeverity()))
            && (this.getComments() == null ? other.getComments() == null : this.getComments().equals(other.getComments()))
            && (this.getFacility() == null ? other.getFacility() == null : this.getFacility().equals(other.getFacility()))
            && (this.getFacilityLogic() == null ? other.getFacilityLogic() == null : this.getFacilityLogic().equals(other.getFacilityLogic()))
            && (this.getFacilityLogicSub() == null ? other.getFacilityLogicSub() == null : this.getFacilityLogicSub().equals(other.getFacilityLogicSub()))
            && (this.getReferenceDevice() == null ? other.getReferenceDevice() == null : this.getReferenceDevice().equals(other.getReferenceDevice()))
            && (this.getReferenceService() == null ? other.getReferenceService() == null : this.getReferenceService().equals(other.getReferenceService()))
            && (this.getAssociateFlag() == null ? other.getAssociateFlag() == null : this.getAssociateFlag().equals(other.getAssociateFlag()))
            && (this.getHarvestType() == null ? other.getHarvestType() == null : this.getHarvestType().equals(other.getHarvestType()))
            && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
            && (this.getIsReport() == null ? other.getIsReport() == null : this.getIsReport().equals(other.getIsReport()))
            && (this.getSpecFileName() == null ? other.getSpecFileName() == null : this.getSpecFileName().equals(other.getSpecFileName()))
            && (this.getSpecRowNum() == null ? other.getSpecRowNum() == null : this.getSpecRowNum().equals(other.getSpecRowNum()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
            && (this.getEvent1() == null ? other.getEvent1() == null : this.getEvent1().equals(other.getEvent1()))
            && (this.getEvent2() == null ? other.getEvent2() == null : this.getEvent2().equals(other.getEvent2()))
            && (this.getEvent3() == null ? other.getEvent3() == null : this.getEvent3().equals(other.getEvent3()))
            && (this.getEvent4() == null ? other.getEvent4() == null : this.getEvent4().equals(other.getEvent4()))
            && (this.getEvent5() == null ? other.getEvent5() == null : this.getEvent5().equals(other.getEvent5()))
            && (this.getEvent6() == null ? other.getEvent6() == null : this.getEvent6().equals(other.getEvent6()))
            && (this.getEvent7() == null ? other.getEvent7() == null : this.getEvent7().equals(other.getEvent7()))
            && (this.getEvent8() == null ? other.getEvent8() == null : this.getEvent8().equals(other.getEvent8()))
            && (this.getEvent9() == null ? other.getEvent9() == null : this.getEvent9().equals(other.getEvent9()))
            && (this.getEvent10() == null ? other.getEvent10() == null : this.getEvent10().equals(other.getEvent10()))
            && (this.getEvent11() == null ? other.getEvent11() == null : this.getEvent11().equals(other.getEvent11()))
            && (this.getEvent12() == null ? other.getEvent12() == null : this.getEvent12().equals(other.getEvent12()))
            && (this.getEvent13() == null ? other.getEvent13() == null : this.getEvent13().equals(other.getEvent13()))
            && (this.getEvent14() == null ? other.getEvent14() == null : this.getEvent14().equals(other.getEvent14()))
            && (this.getEvent15() == null ? other.getEvent15() == null : this.getEvent15().equals(other.getEvent15()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEventId() == null) ? 0 : getEventId().hashCode());
        result = prime * result + ((getDeviceSpecialty() == null) ? 0 : getDeviceSpecialty().hashCode());
        result = prime * result + ((getDeviceService() == null) ? 0 : getDeviceService().hashCode());
        result = prime * result + ((getDeviceServiceSub() == null) ? 0 : getDeviceServiceSub().hashCode());
        result = prime * result + ((getDeviceVendor() == null) ? 0 : getDeviceVendor().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        result = prime * result + ((getDeviceVersion() == null) ? 0 : getDeviceVersion().hashCode());
        result = prime * result + ((getVendorEventId() == null) ? 0 : getVendorEventId().hashCode());
        result = prime * result + ((getVendorEventFacl() == null) ? 0 : getVendorEventFacl().hashCode());
        result = prime * result + ((getVendorEventSeve() == null) ? 0 : getVendorEventSeve().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSeverity() == null) ? 0 : getSeverity().hashCode());
        result = prime * result + ((getComments() == null) ? 0 : getComments().hashCode());
        result = prime * result + ((getFacility() == null) ? 0 : getFacility().hashCode());
        result = prime * result + ((getFacilityLogic() == null) ? 0 : getFacilityLogic().hashCode());
        result = prime * result + ((getFacilityLogicSub() == null) ? 0 : getFacilityLogicSub().hashCode());
        result = prime * result + ((getReferenceDevice() == null) ? 0 : getReferenceDevice().hashCode());
        result = prime * result + ((getReferenceService() == null) ? 0 : getReferenceService().hashCode());
        result = prime * result + ((getAssociateFlag() == null) ? 0 : getAssociateFlag().hashCode());
        result = prime * result + ((getHarvestType() == null) ? 0 : getHarvestType().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getIsReport() == null) ? 0 : getIsReport().hashCode());
        result = prime * result + ((getSpecFileName() == null) ? 0 : getSpecFileName().hashCode());
        result = prime * result + ((getSpecRowNum() == null) ? 0 : getSpecRowNum().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getEvent1() == null) ? 0 : getEvent1().hashCode());
        result = prime * result + ((getEvent2() == null) ? 0 : getEvent2().hashCode());
        result = prime * result + ((getEvent3() == null) ? 0 : getEvent3().hashCode());
        result = prime * result + ((getEvent4() == null) ? 0 : getEvent4().hashCode());
        result = prime * result + ((getEvent5() == null) ? 0 : getEvent5().hashCode());
        result = prime * result + ((getEvent6() == null) ? 0 : getEvent6().hashCode());
        result = prime * result + ((getEvent7() == null) ? 0 : getEvent7().hashCode());
        result = prime * result + ((getEvent8() == null) ? 0 : getEvent8().hashCode());
        result = prime * result + ((getEvent9() == null) ? 0 : getEvent9().hashCode());
        result = prime * result + ((getEvent10() == null) ? 0 : getEvent10().hashCode());
        result = prime * result + ((getEvent11() == null) ? 0 : getEvent11().hashCode());
        result = prime * result + ((getEvent12() == null) ? 0 : getEvent12().hashCode());
        result = prime * result + ((getEvent13() == null) ? 0 : getEvent13().hashCode());
        result = prime * result + ((getEvent14() == null) ? 0 : getEvent14().hashCode());
        result = prime * result + ((getEvent15() == null) ? 0 : getEvent15().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", eventId=").append(eventId);
        sb.append(", deviceSpecialty=").append(deviceSpecialty);
        sb.append(", deviceService=").append(deviceService);
        sb.append(", deviceServiceSub=").append(deviceServiceSub);
        sb.append(", deviceVendor=").append(deviceVendor);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", deviceVersion=").append(deviceVersion);
        sb.append(", vendorEventId=").append(vendorEventId);
        sb.append(", vendorEventFacl=").append(vendorEventFacl);
        sb.append(", vendorEventSeve=").append(vendorEventSeve);
        sb.append(", title=").append(title);
        sb.append(", name=").append(name);
        sb.append(", severity=").append(severity);
        sb.append(", comments=").append(comments);
        sb.append(", facility=").append(facility);
        sb.append(", facilityLogic=").append(facilityLogic);
        sb.append(", facilityLogicSub=").append(facilityLogicSub);
        sb.append(", referenceDevice=").append(referenceDevice);
        sb.append(", referenceService=").append(referenceService);
        sb.append(", associateFlag=").append(associateFlag);
        sb.append(", harvestType=").append(harvestType);
        sb.append(", category=").append(category);
        sb.append(", isReport=").append(isReport);
        sb.append(", specFileName=").append(specFileName);
        sb.append(", specRowNum=").append(specRowNum);
        sb.append(", modifyUser=").append(modifyUser);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", event1=").append(event1);
        sb.append(", event2=").append(event2);
        sb.append(", event3=").append(event3);
        sb.append(", event4=").append(event4);
        sb.append(", event5=").append(event5);
        sb.append(", event6=").append(event6);
        sb.append(", event7=").append(event7);
        sb.append(", event8=").append(event8);
        sb.append(", event9=").append(event9);
        sb.append(", event10=").append(event10);
        sb.append(", event11=").append(event11);
        sb.append(", event12=").append(event12);
        sb.append(", event13=").append(event13);
        sb.append(", event14=").append(event14);
        sb.append(", event15=").append(event15);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}