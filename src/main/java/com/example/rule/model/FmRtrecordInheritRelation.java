package com.example.rule.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实时关联告警关系表
 * @TableName FM_RTRECORD_INHERIT_RELATION
 */
@TableName(value ="FM_RTRECORD_INHERIT_RELATION")
@Data
public class FmRtrecordInheritRelation implements Serializable {
    /**
     * 父告警ID，对应FM_RTRECORD表RECORD_ID
     */
    private Long parentId;

    /**
     * 子告警ID，对应FM_RTRECORD表RECORD_ID
     */
    private Long childId;

    /**
     * 规则ID，对应FM_POLICY_RULES表ID
     */
    private Long ruleId;

    /**
     * 关联类型
     */
    private Long inheritType;

    /**
     * 
     */
    private Date ruleTime;

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
        FmRtrecordInheritRelation other = (FmRtrecordInheritRelation) that;
        return (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getChildId() == null ? other.getChildId() == null : this.getChildId().equals(other.getChildId()))
            && (this.getRuleId() == null ? other.getRuleId() == null : this.getRuleId().equals(other.getRuleId()))
            && (this.getInheritType() == null ? other.getInheritType() == null : this.getInheritType().equals(other.getInheritType()))
            && (this.getRuleTime() == null ? other.getRuleTime() == null : this.getRuleTime().equals(other.getRuleTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getChildId() == null) ? 0 : getChildId().hashCode());
        result = prime * result + ((getRuleId() == null) ? 0 : getRuleId().hashCode());
        result = prime * result + ((getInheritType() == null) ? 0 : getInheritType().hashCode());
        result = prime * result + ((getRuleTime() == null) ? 0 : getRuleTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", parentId=").append(parentId);
        sb.append(", childId=").append(childId);
        sb.append(", ruleId=").append(ruleId);
        sb.append(", inheritType=").append(inheritType);
        sb.append(", ruleTime=").append(ruleTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}