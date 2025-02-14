package com.example.rule.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName FM_POLICY_RULES_SCRIPT
 */
@TableName(value ="FM_POLICY_RULES_SCRIPT")
@Data
public class FmPolicyRulesScript implements Serializable {
    /**
     * 
     */
    private String scriptId;

    /**
     * 
     */
    private String scriptName;

    /**
     * 
     */
    private String scriptData;

    /**
     * 
     */
    private String scriptType;

    /**
     * 
     */
    private String scriptLanguage;
    /**
     *
     */
    private String scriptKey;

    /**
     * 
     */
    private Long enable;

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
        FmPolicyRulesScript other = (FmPolicyRulesScript) that;
        return (this.getScriptId() == null ? other.getScriptId() == null : this.getScriptId().equals(other.getScriptId()))
            && (this.getScriptName() == null ? other.getScriptName() == null : this.getScriptName().equals(other.getScriptName()))
            && (this.getScriptData() == null ? other.getScriptData() == null : this.getScriptData().equals(other.getScriptData()))
            && (this.getScriptType() == null ? other.getScriptType() == null : this.getScriptType().equals(other.getScriptType()))
            && (this.getScriptLanguage() == null ? other.getScriptLanguage() == null : this.getScriptLanguage().equals(other.getScriptLanguage()))
            && (this.getEnable() == null ? other.getEnable() == null : this.getEnable().equals(other.getEnable()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getScriptId() == null) ? 0 : getScriptId().hashCode());
        result = prime * result + ((getScriptName() == null) ? 0 : getScriptName().hashCode());
        result = prime * result + ((getScriptData() == null) ? 0 : getScriptData().hashCode());
        result = prime * result + ((getScriptType() == null) ? 0 : getScriptType().hashCode());
        result = prime * result + ((getScriptLanguage() == null) ? 0 : getScriptLanguage().hashCode());
        result = prime * result + ((getEnable() == null) ? 0 : getEnable().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", scriptId=").append(scriptId);
        sb.append(", scriptName=").append(scriptName);
        sb.append(", scriptData=").append(scriptData);
        sb.append(", scriptType=").append(scriptType);
        sb.append(", scriptLanguage=").append(scriptLanguage);
        sb.append(", enable=").append(enable);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}