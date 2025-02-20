package com.example.rule.service.imp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rule.mapper.FmMapper;
import com.example.rule.model.*;
import com.example.rule.service.FmEventDefineService;
import com.example.rule.service.FmService;
import com.example.rule.util.RuleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FmServiceImp extends ServiceImpl<FmMapper, MSEvent> implements FmService {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private FmEventDefineService fmEventDefineService;
    @Override
    public void inheritMerge(MSEvent msEvent) {
        String string = RuleUtils.getLenString(msEvent.getDescr(), 4000);
        msEvent.setDescr(string);
        String sproc = "{call P_FM_INHERIT_MERGER_EVENT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatementCallback<MSEvent> csc = new CallableStatementCallback<MSEvent>() {
            public MSEvent doInCallableStatement(CallableStatement st)
                    throws SQLException, DataAccessException {
                try {
                    int i = 1;
                    st.setString(i++, msEvent.getEventId());
                    st.setString(i++, msEvent.getObjectId());
                    st.setString(i++, msEvent.getSourceId());
                    st.setString(i++, msEvent.getKeyValue());
                    st.setString(i++, msEvent.getDescr());
                    st.setInt(i++, msEvent.getCounts());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getOccurTime()));
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getDiscoverTime()));
                    st.setInt(i++, msEvent.getAckTag());
                    st.setString(i++, msEvent.getAckUser());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getAckTime()));
                    st.setInt(i++, msEvent.getClearTag());
                    st.setString(i++, msEvent.getClearUser());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getClearTime()));
                    st.setLong(i++, msEvent.getRestoreId());
                    st.setInt(i++, msEvent.getRestoreTag());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getRestoreTime()));
                    st.setInt(i++, msEvent.getInheritTag());
                    st.setInt(i++, msEvent.getDispTag());
                    st.setString(i++, msEvent.getDispUser());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getDispTime()));
                    st.setString(i++, msEvent.getDispRules());
                    st.setString(i++, msEvent.getDispFaultCause());
                    st.setString(i++, msEvent.getDispUndoCause());
                    st.setString(i++, msEvent.getDispId());
                    st.setString(i++, msEvent.getDispStatus());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getDispSyncTime()));
                    st.setString(i++, msEvent.getSourceSystem());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getSourceStartTime()));
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getSourceEndTime()));
                    st.setString(i++, msEvent.getSourceName());
                    st.setInt(i++, msEvent.getProjectStatus());
                    st.setString(i++, msEvent.getProjectStatusSub());
                    st.setString(i++, msEvent.getProjectName());
                    st.setString(i++, msEvent.getProjectId());
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getProjectStartTime()));
                    st.setTimestamp(i++, RuleUtils.toTimestamp(msEvent.getProjectEndTime()));
                    st.setInt(i++, msEvent.getErrorTag());
                    st.setString(i++, msEvent.getErrorInfo());
                    st.setInt(i++, msEvent.getReportTag());
                    st.setString(i++, msEvent.getCollectionType());
                    st.setString(i++, msEvent.getFaultProbableCause());
                    st.setInt(i++, msEvent.getRemarkTag());
                    st.setString(i++, msEvent.getRemark());
                    st.setLong(i++, msEvent.getParas1());
                    st.setLong(i++, msEvent.getParas2());
                    st.setLong(i++, msEvent.getParas3());
                    st.setLong(i++, msEvent.getParas4());
                    st.setLong(i++, msEvent.getParas5());
                    st.setLong(i++, msEvent.getParas6());
                    st.setLong(i++, msEvent.getParas7());
                    st.setLong(i++, msEvent.getParas8());
                    st.setLong(i++, msEvent.getParas9());
                    st.setLong(i++, msEvent.getParas10());
                    st.setLong(i++, msEvent.getParas11());
                    st.setLong(i++, msEvent.getParas12());
                    st.setLong(i++, msEvent.getParas13());
                    st.setLong(i++, msEvent.getParas14());
                    st.setLong(i++, msEvent.getParas15());
                    st.setLong(i++, msEvent.getParas16());
                    st.setLong(i++, msEvent.getParas17());
                    st.setLong(i++, msEvent.getParas18());
                    st.setLong(i++, msEvent.getParas19());
                    st.setLong(i++, msEvent.getParas20());
                    st.setString(i++, msEvent.getParasa());
                    st.setString(i++, msEvent.getParasb());
                    st.setString(i++, msEvent.getParasc());
                    st.setString(i++, msEvent.getParasd());
                    st.setString(i++, msEvent.getParase());
                    st.setString(i++, msEvent.getParasf());
                    st.setString(i++, msEvent.getParasg());
                    st.setString(i++, msEvent.getParash());
                    st.setString(i++, msEvent.getParasi());
                    st.setString(i++, msEvent.getParasj());
                    st.setString(i++, msEvent.getParask());
                    st.setString(i++, msEvent.getParasl());
                    st.setString(i++, msEvent.getParasm());
                    st.setString(i++, msEvent.getParasn());
                    st.setString(i++, msEvent.getParaso());
                    st.setString(i++, msEvent.getParasp());
                    st.setString(i++, msEvent.getParasq());
                    st.setString(i++, msEvent.getParasr());
                    st.setString(i++, msEvent.getParass());
                    st.setString(i++, msEvent.getParast());
                    st.setString(i++, msEvent.getParasu());
                    st.setString(i++, msEvent.getParasv());
                    st.setString(i++, msEvent.getParasw());
                    st.setString(i++, msEvent.getParasx());
                    st.setString(i++, msEvent.getParasy());
                    st.setString(i++, msEvent.getParasz());
                    st.setString(i++, msEvent.getObjectName());
                    st.setString(i++, msEvent.getObjectAlias());
                    st.setString(i++, msEvent.getObjectIp());
                    st.setString(i++, msEvent.getObjectDesc());
                    st.setString(i++, msEvent.getObjectType());
                    st.setString(i++, msEvent.getDeviceId());
                    st.setString(i++, msEvent.getDeviceName());
                    st.setString(i++, msEvent.getDeviceAlias());
                    st.setString(i++, msEvent.getDeviceIp());
                    st.setString(i++, msEvent.getDeviceSpecialty());
                    st.setString(i++, msEvent.getDeviceSpecialtySub());
                    st.setString(i++, msEvent.getDeviceService());
                    st.setString(i++, msEvent.getDeviceServiceSub());
                    st.setString(i++, msEvent.getDeviceNetwork());
                    st.setString(i++, msEvent.getDeviceNetworkSub());
                    st.setString(i++, msEvent.getDeviceNetworkLevel());
                    st.setString(i++, msEvent.getDeviceLocation());
                    st.setString(i++, msEvent.getDeviceType());
                    st.setString(i++, msEvent.getDeviceVersion());
                    st.setString(i++, msEvent.getDeviceDesc());
                    st.setString(i++, msEvent.getDeviceVendor());
                    st.setString(i++, msEvent.getDeviceProvince());
                    st.setString(i++, msEvent.getDeviceCity());
                    st.setString(i++, msEvent.getDeviceCounty());
                    st.setString(i++, msEvent.getDeviceSite());
                    st.setString(i++, msEvent.getDeviceSiteLevel());
                    st.setString(i++, msEvent.getDeviceRoom());
                    st.setString(i++, msEvent.getProperty1());
                    st.setString(i++, msEvent.getProperty2());
                    st.setString(i++, msEvent.getProperty3());
                    st.setString(i++, msEvent.getProperty4());
                    st.setString(i++, msEvent.getProperty5());
                    st.setString(i++, msEvent.getProperty6());
                    st.setString(i++, msEvent.getProperty7());
                    st.setString(i++, msEvent.getProperty8());
                    st.setString(i++, msEvent.getProperty9());
                    st.setString(i++, msEvent.getProperty10());
                    st.setString(i++, msEvent.getProperty11());
                    st.setString(i++, msEvent.getProperty12());
                    st.setString(i++, msEvent.getProperty13());
                    st.setString(i++, msEvent.getProperty14());
                    st.setString(i++, msEvent.getProperty15());
                    st.setString(i++, msEvent.getVendorEventId());
                    st.setString(i++, msEvent.getVendorEventFacl());
                    st.setString(i++, msEvent.getVendorEventSeve());
                    st.setString(i++, msEvent.getTitle());
                    st.setString(i++, msEvent.getName());
                    st.setInt(i++, msEvent.getSeverity());
                    st.setString(i++, msEvent.getComments());
                    st.setInt(i++, msEvent.getFacility());
                    st.setString(i++, msEvent.getFacilityLogic());
                    st.setString(i++, msEvent.getFacilityLogicSub());
                    st.setString(i++, msEvent.getReferenceDevice());
                    st.setString(i++, msEvent.getReferenceService());
                    st.setInt(i++, msEvent.getHarvestType());
                    st.setString(i++, msEvent.getCategory());
                    st.setInt(i++, msEvent.getIsReport());
                    st.setString(i++, msEvent.getEvent1());
                    st.setString(i++, msEvent.getEvent2());
                    st.setString(i++, msEvent.getEvent3());
                    st.setString(i++, msEvent.getEvent4());
                    st.setString(i++, msEvent.getEvent5());
                    st.setString(i++, msEvent.getProperty16());
                    st.setString(i++, msEvent.getProperty17());
                    st.setString(i++, msEvent.getProperty18());
                    st.setString(i++, msEvent.getProperty19());
                    st.setString(i++, msEvent.getProperty20());
                    st.setString(i++, msEvent.getProperty21());
                    st.setString(i++, msEvent.getProperty22());
                    st.setString(i++, msEvent.getProperty23());
                    st.setString(i++, msEvent.getProperty24());
                    st.setString(i++, msEvent.getProperty25());
                    st.setString(i++, msEvent.getProperty26());
                    st.setString(i++, msEvent.getProperty27());
                    st.setString(i++, msEvent.getProperty28());
                    st.setString(i++, msEvent.getProperty29());
                    st.setString(i++, msEvent.getProperty30());
                    st.setString(i++, msEvent.getProperty31());
                    st.setString(i++, msEvent.getProperty32());
                    st.setString(i++, msEvent.getProperty33());
                    st.setString(i++, msEvent.getProperty34());
                    st.setString(i++, msEvent.getProperty35());
                    st.setString(i++, msEvent.getProperty36());
                    st.setString(i++, msEvent.getProperty37());
                    st.setString(i++, msEvent.getProperty38());
                    st.setString(i++, msEvent.getProperty39());
                    st.setString(i++, msEvent.getProperty40());
                    st.setString(i++, msEvent.getProperty41());
                    st.setString(i++, msEvent.getProperty42());
                    st.setString(i++, msEvent.getProperty43());
                    st.setString(i++, msEvent.getProperty44());
                    st.setString(i++, msEvent.getProperty45());
                    int outIndex = i;
                    st.registerOutParameter(i++, Types.INTEGER);
                    st.registerOutParameter(i++, Types.TIMESTAMP);
                    st.registerOutParameter(i++, Types.TIMESTAMP);
                    st.registerOutParameter(i++, Types.TIMESTAMP);
                    st.registerOutParameter(i++, Types.INTEGER);

                    st.execute();
                    // 获取返回值
                    long recordId = st.getLong(outIndex++);
                    Date startTime = st.getTimestamp(outIndex++);
                    Date lastTime = st.getTimestamp(outIndex++);
                    Date saveTime = st.getTimestamp(outIndex++);
                    int type = st.getInt(outIndex++);
                    // 数字啊
                    if (type < 0) {
                        throw new SQLException(
                                "告警入库失败, 执行存储过程P_FM_INHERIT_MERGER_EVENT异常, 返回值：" + type);
                    } else if (type == 1) {
                        msEvent.setOperationType(OperationType.INSERT);
                    } else if (type == 2) {
                        msEvent.setOperationType(OperationType.UPDATE);
                    }

                    msEvent.setRecordId(recordId);
                    msEvent.setStartTime(startTime);
                    msEvent.setLastTime(lastTime);
                    msEvent.setSaveTime(saveTime);
                    return msEvent;
                } catch (SQLException e) {
                    log.error("执行存储过程 P_FM_INHERIT_MERGER_EVENT 异常", e);
                    throw e;
                }
            }

        };
        jdbcTemplate.execute(sproc, csc);
    }
    @Override
    public void updateInherit(Long pRecordId, List<Long> childIds, Long ruleId) {
        String sproc = "{call P_FM_UPDATE_INHERIT(?,?,?,?,?)}";
        log.info(RuleUtils.parseRecordIds(childIds));
        // ORA-01795: 列表中的最大表达式数为 1000
        final List<Long> subList;
        if (childIds.size() > 1000) {
            subList = childIds.subList(0, 1000);
        } else {
            subList = childIds;
        }
        CallableStatementCallback<Object> csc = new CallableStatementCallback<Object>() {
            public Object doInCallableStatement(CallableStatement st)
                    throws SQLException, DataAccessException {
                st.setLong(1, pRecordId);
                st.setString(2, RuleUtils.parseRecordIds(subList));
                st.setNull(3, Types.VARCHAR);
                st.setNull(4, Types.VARCHAR);
                st.setLong(5, ruleId);
                return st.execute();
            }
        };
        jdbcTemplate.execute(sproc, csc);
    }

    @Override
    public MSEvent deriveNewAlarm(SupplementaryConditions contextBean, List<Long> childIds, FmPolicyRules fmPolicyRules) {
        FmEventDefine eventDefine = fmEventDefineService.getOne(Wrappers.lambdaQuery(FmEventDefine.class)
                .eq(FmEventDefine::getEventId, fmPolicyRules.getPrimaryEventId()));
        if (eventDefine == null) {
            log.error("未定义衍生告警事件ID.");
            return null;
        }
        List<MSEvent> childrens = contextBean.getChildrens();
        MSEvent earliest = RuleUtils.parseChilds(childrens, childIds);
        MSEvent event = new MSEvent();
        event.setSourceId(RuleUtils.convertSourceId(contextBean.getSourceIdConds(), fmPolicyRules));
        event.setDescr(RuleUtils.generatorDesc(earliest, fmPolicyRules)); // 告警正文
        RuleUtils.fillEventTime(event, earliest.getOccurTime());
        RuleUtils.fill(event, earliest);
        RuleUtils.fillEventDefine(event, eventDefine);
        return event;
    }

}
