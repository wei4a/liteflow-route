package com.example.rule.util;

import com.example.rule.model.FmEventDefine;
import com.example.rule.model.FmPolicyRules;
import com.example.rule.model.MSEvent;
import com.example.rule.model.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RuleUtils {
    public static final String PROJECT_STATUS = "project_status";
    public static final int NONE_PROJECT_STATUS = 0;
    public static final int IS_PROJECT_STATUS = 1;
    public static void getProjectStatus(Map<String, Object> paramMap, FmPolicyRules policyRule) {
        String projectStatus = policyRule.getProjectStatus();
        if (StringUtils.isNotBlank(policyRule.getProjectStatus())) {
            if (StringUtils.equals(projectStatus, "在网")) {
                paramMap.put(PROJECT_STATUS, NONE_PROJECT_STATUS);
            } else {
                if (StringUtils.equals(projectStatus, "工程")) {
                    paramMap.put(PROJECT_STATUS, IS_PROJECT_STATUS);
                }
            }
        }
    }
    public static int getProjectStatus(FmPolicyRules policyRule) {
        String projectStatus = policyRule.getProjectStatus();
        if (StringUtils.isBlank(projectStatus)) {
            return NONE_PROJECT_STATUS;
        }
        if (StringUtils.equals(projectStatus, "在网")) {
            return NONE_PROJECT_STATUS;
        } else if (StringUtils.equals(projectStatus, "工程")) {
            return IS_PROJECT_STATUS;
        }
        return NONE_PROJECT_STATUS;
    }
    public static Date getRuleStartTime(MSEvent msEvent, FmPolicyRules policyRule) {
        int timeWindow = policyRule.getTimeWindow();
        ZonedDateTime zonedDateTime = msEvent.getLastTime().toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime startTime = zonedDateTime.minus(Duration.ofMinutes(timeWindow));
        return Date.from(startTime.toInstant());
    }

    public static List<String> convertStringToList(String string) {
        if (StringUtils.isEmpty(string)) {
            return new ArrayList<>();
        }
        String[] ss = string.trim().split(",");
        return Arrays.asList(ss);
    }

    public static void fillEventTime(MSEvent event, Date occurTime) {
        event.setOccurTime(occurTime);
        event.setDiscoverTime(new Date());
    }

    public static void fill(MSEvent newEvent, MSEvent srcEvent) {
        newEvent.setSourceSystem(srcEvent.getSourceSystem());
        newEvent.setFaultProbableCause(srcEvent.getFaultProbableCause());
        newEvent.setParas9(srcEvent.getParas9());
        newEvent.setParasv(srcEvent.getParasv());
        newEvent.setParasu(srcEvent.getParasu());
        newEvent.setObjectId(srcEvent.getObjectId());
        newEvent.setObjectName(srcEvent.getObjectName());
        newEvent.setObjectAlias(srcEvent.getObjectAlias());
        newEvent.setObjectIp(srcEvent.getObjectIp());
        newEvent.setObjectDesc(srcEvent.getObjectDesc());
        newEvent.setObjectType(srcEvent.getObjectType());
        newEvent.setDeviceId(srcEvent.getDeviceId());
        newEvent.setDeviceName(srcEvent.getDeviceName());
        newEvent.setDeviceAlias(srcEvent.getDeviceAlias());
        newEvent.setDeviceDesc(srcEvent.getDeviceDesc());
        newEvent.setDeviceIp(srcEvent.getDeviceIp());
        newEvent.setDeviceSpecialty(srcEvent.getDeviceSpecialty());
        newEvent.setDeviceSpecialtySub(srcEvent.getDeviceSpecialtySub());
        newEvent.setDeviceService(srcEvent.getDeviceService());
        newEvent.setDeviceServiceSub(srcEvent.getDeviceServiceSub());
        newEvent.setDeviceNetwork(srcEvent.getDeviceNetwork());
        newEvent.setDeviceNetworkSub(srcEvent.getDeviceNetworkSub());
        newEvent.setDeviceNetworkLevel(srcEvent.getDeviceNetworkLevel());
        newEvent.setDeviceLocation(srcEvent.getDeviceLocation());
        newEvent.setDeviceType(srcEvent.getDeviceType());
        newEvent.setDeviceVersion(srcEvent.getDeviceVersion());
        newEvent.setDeviceDesc(srcEvent.getDeviceDesc());
        newEvent.setDeviceVendor(srcEvent.getDeviceVendor());
        newEvent.setDeviceProvince(srcEvent.getDeviceProvince());
        newEvent.setDeviceCity(srcEvent.getDeviceCity());
        newEvent.setDeviceCounty(srcEvent.getDeviceCounty());
        newEvent.setDeviceSite(srcEvent.getDeviceSite());
        newEvent.setDeviceSiteLevel(srcEvent.getDeviceSiteLevel());
        newEvent.setDeviceRoom(srcEvent.getDeviceRoom());
        newEvent.setProperty1(srcEvent.getProperty1());
        newEvent.setProperty2(srcEvent.getProperty2());
        newEvent.setProperty3(srcEvent.getProperty3());
        newEvent.setProperty4(srcEvent.getProperty4());
        newEvent.setProperty5(srcEvent.getProperty5());
        newEvent.setProperty6(srcEvent.getProperty6());
        newEvent.setProperty7(srcEvent.getProperty7());
        newEvent.setProperty8(srcEvent.getProperty8());
        newEvent.setProperty9(srcEvent.getProperty9());
        newEvent.setProperty10(srcEvent.getProperty10());
        newEvent.setProperty11(srcEvent.getProperty11());
        newEvent.setProperty12(srcEvent.getProperty12());
        newEvent.setProperty13(srcEvent.getProperty13());
        newEvent.setProperty14(srcEvent.getProperty14());
        newEvent.setProperty15(srcEvent.getProperty15());
        newEvent.setProperty16(srcEvent.getProperty16());
        newEvent.setProperty17(srcEvent.getProperty17());
        newEvent.setProperty18(srcEvent.getProperty18());
        newEvent.setProperty19(srcEvent.getProperty19());
        newEvent.setProperty20(srcEvent.getProperty20());
        newEvent.setProperty21(srcEvent.getProperty21());
        newEvent.setProperty22(srcEvent.getProperty22());
        newEvent.setProperty23(srcEvent.getProperty23());
        newEvent.setProperty24(srcEvent.getProperty24());
        newEvent.setProperty25(srcEvent.getProperty25());
        newEvent.setProperty26(srcEvent.getProperty26());
        newEvent.setProperty27(srcEvent.getProperty27());
        newEvent.setProperty28(srcEvent.getProperty28());
        newEvent.setProperty29(srcEvent.getProperty29());
        newEvent.setProperty30(srcEvent.getProperty30());
        newEvent.setProperty31(srcEvent.getProperty31());
        newEvent.setProperty32(srcEvent.getProperty32());
        newEvent.setProperty33(srcEvent.getProperty33());
        newEvent.setProperty34(srcEvent.getProperty34());
        newEvent.setProperty35(srcEvent.getProperty35());
        newEvent.setProperty36(srcEvent.getProperty36());
        newEvent.setProperty37(srcEvent.getProperty37());
        newEvent.setProperty38(srcEvent.getProperty38());
        newEvent.setProperty39(srcEvent.getProperty39());
        newEvent.setProperty40(srcEvent.getProperty40());
        newEvent.setProperty41(srcEvent.getProperty41());
        newEvent.setProperty42(srcEvent.getProperty42());
        newEvent.setProperty43(srcEvent.getProperty43());
        newEvent.setProperty44(srcEvent.getProperty44());
        newEvent.setProperty45(srcEvent.getProperty45());
        newEvent.setProjectStatus(srcEvent.getProjectStatus());
    }

    public static void fillEventDefine(MSEvent msEvent, FmEventDefine eventDefine) {
        msEvent.setEventId(eventDefine.getEventId());
        msEvent.setVendorEventId(eventDefine.getVendorEventId());
        msEvent.setVendorEventFacl(eventDefine.getVendorEventFacl());
        msEvent.setVendorEventSeve(eventDefine.getVendorEventSeve());
        msEvent.setTitle(eventDefine.getTitle());
        msEvent.setName(eventDefine.getName());
        msEvent.setSeverity(eventDefine.getSeverity());
        msEvent.setComments(eventDefine.getComments());
        msEvent.setFacility(eventDefine.getFacility());
        msEvent.setFacilityLogic(eventDefine.getFacilityLogic());
        msEvent.setFacilityLogicSub(eventDefine.getFacilityLogicSub());
        msEvent.setReferenceDevice(eventDefine.getReferenceDevice());
        msEvent.setReferenceService(eventDefine.getReferenceService());
        msEvent.setHarvestType(eventDefine.getHarvestType());
        msEvent.setCategory(eventDefine.getCategory());
        msEvent.setIsReport(eventDefine.getIsReport());
        msEvent.setEvent1(eventDefine.getEvent1());
        msEvent.setEvent2(eventDefine.getEvent2());
        msEvent.setEvent3(eventDefine.getEvent3());
        msEvent.setEvent4(eventDefine.getEvent4());
        msEvent.setEvent5(eventDefine.getEvent5());
        /* 动环二次标准化 填充PARAS18标识*/
        if (StringUtils.isNotBlank(eventDefine.getDeviceSpecialty())) {
            if ("动环".equals(eventDefine.getDeviceSpecialty()) || "动力环境".equals(eventDefine.getDeviceSpecialty())) {
                msEvent.setParas18(eventDefine.getHarvestType());
            }
        }
    }
    public static String getLenString(String descr, int len) {
        if (descr == null || descr.length() == 0) {
            return descr;
        }
        byte[] bytes = descr.getBytes();
        if (bytes.length > len) {
            int tempLen = new String(bytes, 0, len).length();
            descr = descr.substring(0, tempLen);
            // 防止最后一个字符的长度不是一个字节数
            if (descr.getBytes().length > len) {
                descr = descr.substring(0, tempLen - 1);
            }
        }
        return descr;
    }
    public static Timestamp toTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return new Timestamp(date.getTime());
        }
    }
    public static String parseRecordIds(List<Long> recordIds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recordIds.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(recordIds.get(i));
        }
        return sb.toString();
    }

    public static List<FmPolicyRules> getCombinedRules(List<FmPolicyRules> rules, List<FmPolicyRules> rulesByTitle) {
        List<FmPolicyRules> allRules = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rules)) {
            allRules.addAll(rules);
        }
        if (CollectionUtils.isNotEmpty(rulesByTitle)) {
            allRules.addAll(rulesByTitle);
        }
        // 根据规则ID去重
        Map<Long, FmPolicyRules> uniqueRulesMap = allRules.stream()
                .collect(Collectors.toMap(FmPolicyRules::getId, rule -> rule, (existing, replacement) -> existing));
        return new ArrayList<>(uniqueRulesMap.values());
    }

    public static long getDelayTimeReal(MSEvent msEvent, FmPolicyRules rule) {
        long timeStart = msEvent.getLastTime().getTime() + rule.getDelayTime() * 60 * 1000L;
        long timeNow = new Date().getTime();
        return timeStart - timeNow;
    }
    public static MqMessage getMqMessage(MSEvent msEvent, FmPolicyRules rule, long timeReal) {
        MqMessage mqMessage = new MqMessage();
        mqMessage.setRuleId(Integer.parseInt(rule.getId().toString()));
        mqMessage.setDelayTime(timeReal);
        mqMessage.setRecordId(msEvent.getRecordId());
        mqMessage.setTitle(msEvent.getTitle());
        return mqMessage;
    }

    public static MSEvent parseChilds(List<MSEvent> childrens, List<Long> childIds) {
        MSEvent earliest = null;
        for (MSEvent event : childrens) {
            childIds.add(event.getRecordId());
            if (earliest == null || earliest.getOccurTime().after(event.getOccurTime())) {
                earliest = event;
            }
        }
        return earliest;
    }

    public static String convertSourceId(List<String> values, FmPolicyRules policyRule) {
        StringBuilder sb = new StringBuilder();
        sb.append("MIX-").append(policyRule.getPrimaryEventId()).append("_");
        for (String value : values) {
            sb.append(value).append("_");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        sb.append(dateFormat.format(new Date()));
        return sb.toString();
    }

    public static String generatorDesc(MSEvent earliest, FmPolicyRules fmPolicyRules) {
        StringBuilder sb = new StringBuilder();
        sb.append("省份:[河南省]")
                .append(" 地市:[")
                .append(earliest.getDeviceCity())
                .append("] 网元:[")
                .append(earliest.getDeviceName())
                .append("] 告警标题:")
                .append(fmPolicyRules.getPrimaryTitle())
                .append(" 告警产生时间:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sb.append(dateFormat.format(earliest.getOccurTime()));
        } catch (Exception e) {
            log.error("日期格式化异常", e);
        }
        return sb.toString();
    }
}
