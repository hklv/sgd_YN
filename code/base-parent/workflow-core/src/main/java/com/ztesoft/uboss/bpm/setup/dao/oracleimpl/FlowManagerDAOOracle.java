package com.ztesoft.uboss.bpm.setup.dao.oracleimpl;

import com.ztesoft.uboss.bpm.setup.dao.abstractimpl.FlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.model.ProcessDefView;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FlowManagerDAOOracle extends FlowManagerDAO {

    public List<ProcessDefView> qryAllProcessDef() throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT A.*, B.VER, B.PROC_DEF_ID, B.EFFECTIVE_DATE, B.EXPIRED_DATE FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'A' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < SYSDATE AND B.EXPIRED_DATE>SYSDATE");

        ParamArray pa = new ParamArray();

        return (List<ProcessDefView>) query(qrySql.toString(), pa, null, new RowSetMapper<List<ProcessDefView>>() {
            public List<ProcessDefView> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para)
                    throws BaseAppException, SQLException {

                List<ProcessDefView> list = new ArrayList<ProcessDefView>();
                while (rs.next()) {

                    ProcessDefView processDefView = new ProcessDefView();

                    processDefView.setDefinitionKey(op.getString(rs, "PROC_DEF_ID"));
                    processDefView.setProcessName(op.getString(rs, "PROCESS_NAME"));
                    processDefView.setCreateDate(op.getDate(rs, "CREATE_DATE"));
                    processDefView.setEffectiveDate(op.getDate(rs, "EFFECTIVE_DATE"));
                    processDefView.setExpiredDate(op.getDate(rs, "EXPIRED_DATE"));
                    processDefView.setVer(op.getString(rs, "VER"));

                    list.add(processDefView);
                }
                return list;
            }
        });
    }

    public void modTaskState_A(long userId, String taskListId) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET USER_ID=?, STATE='A', STATE_DATE=SYSDATE");
        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", userId);
        pa.set("", taskListId);
        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_A(String taskListId) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='A', STATE_DATE=SYSDATE");
        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", taskListId);
        executeUpdate(sqlStr.toString(), pa);
    }


    public void modTaskState_C(String taskListId, Date cTime) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();

        if (cTime == null) {
            sqlStr.append("UPDATE BPM_TASK_LIST SET END_TIME=SYSDATE,STATE='C', STATE_DATE=SYSDATE");
        } else {
            sqlStr.append("UPDATE BPM_TASK_LIST SET END_TIME=?,STATE='C', STATE_DATE=?");
            pa.set("", cTime);
            pa.set("", cTime);
        }

        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }


    public void modTaskState_H(String taskListId, String suspendReason) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='H', STATE_DATE=SYSDATE, STATE_REASON=? WHERE TASK_LIST_ID = ?");
        pa.set("", suspendReason);
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_D(String taskListId, String deleteReason)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='D', STATE_DATE=SYSDATE, STATE_REASON=? WHERE TASK_LIST_ID = ?");
        pa.set("", deleteReason);
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }

    /**
     * 查询所有流程仿真版本
     */
    public List<ProcessDefView> qryAllSimProcessVersion()
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT A.*, B.VER, B.PROC_DEF_ID, B.EFFECTIVE_DATE, B.EXPIRED_DATE FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < SYSDATE AND B.EXPIRED_DATE>SYSDATE");

        ParamArray pa = new ParamArray();

        return (List<ProcessDefView>) query(qrySql.toString(), pa, null, new RowSetMapper<List<ProcessDefView>>() {
            public List<ProcessDefView> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para)
                    throws BaseAppException, SQLException {

                List<ProcessDefView> list = new ArrayList<ProcessDefView>();
                while (rs.next()) {

                    ProcessDefView processDefView = new ProcessDefView();

                    processDefView.setDefinitionKey(op.getString(rs, "PROC_DEF_ID"));
                    processDefView.setProcessName(op.getString(rs, "PROCESS_NAME"));
                    processDefView.setCreateDate(op.getDate(rs, "CREATE_DATE"));
                    processDefView.setEffectiveDate(op.getDate(rs, "EFFECTIVE_DATE"));
                    processDefView.setExpiredDate(op.getDate(rs, "EXPIRED_DATE"));
                    processDefView.setVer(op.getString(rs, "VER"));
                    processDefView.setBizKey(op.getString(rs, "BIZ_KEY"));

                    list.add(processDefView);
                }
                return list;
            }
        });
    }

    public List<ProcessDefView> qryAllSimProcessVersion(DynamicDict dynamicDict)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT A.*, B.VER, B.PROC_DEF_ID, B.EFFECTIVE_DATE, B.EXPIRED_DATE, B.FORM_ID FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < SYSDATE AND B.EXPIRED_DATE>SYSDATE");
        qrySql.append(" [AND A.PROCESS_NAME like :PROCESS_NAME] [AND A.BIZ_KEY like :BIZ_KEY]");

        ParamMap pm = new ParamMap();
        if (StringUtil.isNotEmpty(dynamicDict.getString("PROCESS_NAME"))) {
            pm.set("PROCESS_NAME", "%" + dynamicDict.getString("PROCESS_NAME") + "%");
        }

        if (StringUtil.isNotEmpty(dynamicDict.getString("BIZ_KEY"))) {
            pm.set("BIZ_KEY", "%" + dynamicDict.getString("BIZ_KEY") + "%");
        }

        return (List<ProcessDefView>) query(qrySql.toString(), pm, (RowSetFormatter) BoHelper.boToDto(dynamicDict.getBO("ROW_SET_FORMATTER"), RowSetFormatter.class), null, new RowSetMapper<List<ProcessDefView>>() {
            public List<ProcessDefView> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para)
                    throws BaseAppException, SQLException {

                List<ProcessDefView> list = new ArrayList<ProcessDefView>();
                while (rs.next()) {

                    ProcessDefView processDefView = new ProcessDefView();

                    processDefView.setDefinitionKey(op.getString(rs, "PROC_DEF_ID"));
                    processDefView.setProcessName(op.getString(rs, "PROCESS_NAME"));
                    processDefView.setCreateDate(op.getDate(rs, "CREATE_DATE"));
                    processDefView.setEffectiveDate(op.getDate(rs, "EFFECTIVE_DATE"));
                    processDefView.setExpiredDate(op.getDate(rs, "EXPIRED_DATE"));
                    processDefView.setVer(op.getString(rs, "VER"));
                    processDefView.setProcBizKey(op.getString(rs, "BIZ_KEY"));
                    processDefView.setFormId(op.getLong(rs, "FORM_ID"));

                    list.add(processDefView);
                }
                return list;
            }
        });
    }


    public void modTaskState(String taskListId, String taskState, Long userId,
                             String stateReason) throws BaseAppException {

        StringBuilder builderSql = new StringBuilder();
        builderSql.append("UPDATE BPM_TASK_LIST SET END_TIME=SYSDATE,STATE=?, " +
                "STATE_DATE=SYSDATE, STATE_REASON=?,USER_ID=? ");
        builderSql.append(" WHERE TASK_LIST_ID = ?");
        ParamArray pa = new ParamArray();
        pa.set("", taskState);
        pa.set("", stateReason);
        pa.set("", userId);
        pa.set("", taskListId);

        executeUpdate(builderSql.toString(), pa);

    }


    @Override
    public long qryAllSimProcessVersionCount(DynamicDict dynamicDict)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT count(1) FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < SYSDATE AND B.EXPIRED_DATE>SYSDATE");
        qrySql.append(" [AND A.PROCESS_NAME like :PROCESS_NAME] [AND A.BIZ_KEY like :BIZ_KEY]");

        ParamMap pm = new ParamMap();

        if (StringUtil.isNotEmpty(dynamicDict.getString("PROCESS_NAME"))) {
            pm.set("PROCESS_NAME", "%" + dynamicDict.getString("PROCESS_NAME") + "%");
        }

        if (StringUtil.isNotEmpty(dynamicDict.getString("BIZ_KEY"))) {
            pm.set("BIZ_KEY", "%" + dynamicDict.getString("BIZ_KEY") + "%");
        }

        return this.selectCount(qrySql.toString(), pm);
    }

    public void modTaskState_C(String taskListId, Date cTime, String taskResult, Long userId) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();

        if (cTime == null) {
            sqlStr.append("UPDATE BPM_TASK_LIST SET END_TIME=SYSDATE,STATE='C',TASK_RESULT=?,USER_ID=?");
        } else {
            sqlStr.append("UPDATE BPM_TASK_LIST SET END_TIME=?,STATE='C',TASK_RESULT=?,USER_ID=?");
            pa.set("", cTime);
        }

        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", taskResult);
        pa.set("", userId);
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }

    @Override
    public List<DynamicDict> qryTaskTempList(DynamicDict conditionDict)
            throws BaseAppException {

        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT A.FORM_ID,M.FORM_NAME,A.TEMPLATE_ID,A.TEMPLATE_TYPE,A.TASK_TYPE,B.TASK_NAME,TEMPLATE_NAME,CODE,A.COMMENTS,A.STATE_DATE,A.WARN_LIMIT,A.ALARM_LIMIT,"
                + "A.CREATE_DATE,A.PHOTO_PATH,C.TYPE_NAME,D.SERVICE_NAME,D.PARAM_PAGE,E.IS_MULTI_SIGIN,E.SIGIN_TYPE,E.PRE_ASSIGN,E.ROLE_ID,E.JOB_ID,E.USER_ID,E.ORG_ID,E.SERVICE_NAME DISPATCH_SERVICE,F.USER_NAME,G.ROLE_NAME ROLE_NAME,H.JOB_NAME,I.ORG_NAME "
                + " FROM BPM_TASK_TEMPLATE A LEFT JOIN BPM_SERV_TASK D ON A.TEMPLATE_ID=D.TEMPLATE_ID LEFT JOIN BPM_USER_TASK E ON A.TEMPLATE_ID=E.TEMPLATE_ID LEFT JOIN BFM_USER F ON E.USER_ID=F.USER_ID LEFT JOIN BFM_ROLE G ON E.ROLE_ID=G.ROLE_ID LEFT JOIN BFM_JOB H ON E.JOB_ID=H.JOB_ID LEFT JOIN BFM_ORG I ON E.ORG_ID=I.ORG_ID LEFT JOIN BPM_FORM M ON A.FORM_ID=M.FORM_ID, "
                + "BPM_TASK_TYPE B,BPM_TASK_TEMPLATE_TYPE C"
                + "  WHERE A.STATE='A'  AND A.TEMPLATE_TYPE=C.TEMPLATE_TYPE AND A.TASK_TYPE=B.TASK_TYPE  AND A.TASK_TYPE IN ('U','S') ");

        ParamMap pa = new ParamMap();
        if (StringUtil.isNotEmpty(conditionDict.getString("CODE"))) {
            qrySql.append(" AND A.CODE = :CODE");
            pa.set("CODE", conditionDict.getString("CODE"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TASK_TYPE"))) {
            qrySql.append(" AND A.TASK_TYPE = :TASK_TYPE");
            pa.set("TASK_TYPE", conditionDict.getString("TASK_TYPE"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TEMPLATE_NAME"))) {
            qrySql.append(" AND A.TEMPLATE_NAME = :TEMPLATE_NAME");
            pa.set("TEMPLATE_NAME", conditionDict.getString("TEMPLATE_NAME"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TEMPLATE_TYPE"))) {
            qrySql.append(" AND A.TEMPLATE_TYPE = :TEMPLATE_TYPE");
            pa.set("TEMPLATE_TYPE", conditionDict.getString("TEMPLATE_TYPE"));
        }
        if (conditionDict.getLong("PROC_DEF_TYPE_ID") != null) {
            qrySql.append(" AND A.PROC_DEF_TYPE_ID = :PROC_DEF_TYPE_ID");
            pa.set("PROC_DEF_TYPE_ID",
                    conditionDict.getLong("PROC_DEF_TYPE_ID"));
        }

        qrySql.append(" ORDER BY TEMPLATE_ID");

        final Map<String, Integer> indexMap = new HashMap<String, Integer>();
        indexMap.put("dbloop", 1);
        RowSetFormatter rowSetFormatter = null;
        if (conditionDict.get("ROW_SET_FORMATTER") != null) {

            indexMap.put("dbloop", 2);
            rowSetFormatter = new RowSetFormatter();
            rowSetFormatter.setPageIndex(conditionDict.getLong(
                    "ROW_SET_FORMATTER.PAGE_INDEX").intValue());
            rowSetFormatter.setPageSize(conditionDict.getLong(
                    "ROW_SET_FORMATTER.PAGE_SIZE").intValue());
        }

        return (List<DynamicDict>) query(qrySql.toString(), pa,
                rowSetFormatter, null, new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {

                            dbloop = indexMap.get("dbloop");

                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("FORM_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("FORM_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TEMPLATE_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TEMPLATE_TYPE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TASK_TYPE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TASK_TYPE_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TEMPLATE_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("CODE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("COMMENTS",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("STATE_DATE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("WARN_LIMIT",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("ALARM_LIMIT",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("CREATE_DATE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("PHOTO_PATH",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TEMPLATE_TYPE_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("SERVICE_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("PARAM_PAGE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("IS_MULTI_SIGIN",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("SIGIN_TYPE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("PRE_ASSIGN",
                                    op.getString(rs, dbloop++));

                            catg.setValueByName("ROLE_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("JOB_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("USER_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("ORG_ID",
                                    op.getString(rs, dbloop++));

                            catg.setValueByName("DISPATCH_SERVICE_NAME",
                                    op.getString(rs, dbloop++));

                            catg.setValueByName("USER_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("ROLE_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("JOB_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("ORG_NAME",
                                    op.getString(rs, dbloop++));

                            // catg.setValueByName("FORM_ID",op.getString(rs,
                            // dbloop++));
                            // catg.setValueByName("FORM_NAME",op.getString(rs,
                            // dbloop++));
                            list.add(catg);
                        }
                        return list;
                    }

                });
    }
}
