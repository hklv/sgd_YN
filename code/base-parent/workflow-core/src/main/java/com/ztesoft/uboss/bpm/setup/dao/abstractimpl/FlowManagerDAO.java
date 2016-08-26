package com.ztesoft.uboss.bpm.setup.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.runtime.beans.ProcessBackInfo;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.model.*;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import org.activiti.uboss.ext.ServiceParamDto;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowManagerDAO extends BusiBaseDAO implements IFlowManagerDAO {

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryProcCatg(Long catgId, Long parentCatgId,
                                         boolean ifTop) throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT  CATG_ID ID,CATG_ID,PARENT_CATG_ID,PARENT_CATG_ID,NAME,COMMENTS FROM BPM_PROC_CATG WHERE 1=1 ");
        ParamArray pa = new ParamArray();
        if (catgId != null) {
            qrySql.append(" AND CATG_ID = ?");
            pa.set("CATG_ID", catgId);
        }
        if (parentCatgId != null) {
            qrySql.append(" AND PARENT_CATG_ID = ?");
            pa.set("PARENT_CATG_ID", parentCatgId);
        }
        if (ifTop) {
            qrySql.append(" AND PARENT_CATG_ID IS NULL");
        }
        qrySql.append(" ORDER BY CATG_ID");
        return (List<DynamicDict>) query(qrySql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws BaseAppException,
                            SQLException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        String parentId = null;
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("id",
                                    "catg" + op.getString(rs, dbloop++));
                            catg.setValueByName("CATG_ID",
                                    op.getString(rs, dbloop++));
                            parentId = op.getString(rs, dbloop++);
                            if (parentId == null) {
                                catg.setValueByName("parent", null);
                            } else {
                                catg.setValueByName("parent", "catg" + parentId);
                            }
                            catg.setValueByName("PARENT_CATG_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("name",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("COMMENTS",
                                    op.getString(rs, dbloop++));

                            list.add(catg);
                        }
                        return list;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    /**
     * 查询目录列表，map形式返回
     * @param catgId
     * @param parentCatgId
     * @param ifTop
     * @return
     * @throws BaseAppException
     */
    public Map<String, DynamicDict> qryCatgMap(Long catgId, Long parentCatgId,
                                               boolean ifTop) throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT  CATG_ID ID,CATG_ID,PARENT_CATG_ID,NAME,COMMENTS FROM BPM_PROC_CATG WHERE 1=1 ORDER BY CATG_ID");
        ParamArray pa = new ParamArray();
        if (catgId != null) {
            qrySql.append(" AND CATG_ID = ?");
            pa.set("CATG_ID", catgId);
        }
        if (parentCatgId != null) {
            qrySql.append(" AND PARENT_CATG_ID = ?");
            pa.set("PARENT_CATG_ID", parentCatgId);
        }
        if (ifTop) {
            qrySql.append(" AND PARENT_CATG_ID IS NULL");
        }
        qrySql.append(" ORDER BY CATG_ID");
        return (Map<String, DynamicDict>) query(qrySql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws BaseAppException,
                            SQLException {

                        int dbloop = 1;
                        Map<String, DynamicDict> map = new HashMap<String, DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("id",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("CATG_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("PARENT_CATG_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("text",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("COMMENTS",
                                    op.getString(rs, dbloop++));

                            map.put(catg.getString("CATG_ID"), catg);
                        }
                        return map;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryProcDefType(Long defTypeId, Long catgId)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT PROC_DEF_TYPE_ID,PROC_DEF_TYPE_ID,CATG_ID,CATG_ID,NAME,COMMENTS,STATE,IS_RESERVED,STATE_DATE,CREATE_DATE FROM BPM_PROC_DEF_TYPE WHERE 1=1 AND STATE='A' ");
        ParamArray pa = new ParamArray();
        if (defTypeId != null) {
            qrySql.append("AND PROC_DEF_TYPE_ID = ?");
            pa.set("PROC_DEF_TYPE_ID", defTypeId);
        }
        if (catgId != null) {
            qrySql.append("AND CATG_ID = ?");
            pa.set("CATG_ID", catgId);
        }
        return (List<DynamicDict>) query(qrySql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws BaseAppException,
                            SQLException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict procDefType = new DynamicDict();
                            procDefType.setValueByName("id",
                                    "procType" + op.getString(rs, dbloop++));
                            procDefType.setValueByName("PROC_DEF_TYPE_ID",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("parent",
                                    "catg" + op.getString(rs, dbloop++));
                            procDefType.setValueByName("CATG_ID",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("name",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("COMMENTS",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("STATE",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("IS_RESERVED",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("STATE_DATE",
                                    op.getString(rs, dbloop++));
                            procDefType.setValueByName("CREATE_DATE",
                                    op.getString(rs, dbloop++));

                            list.add(procDefType);
                        }
                        return list;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryProcTemp(Long procTempId, Long procDefTypeId)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("select A.PROC_TEMP_ID,A.PROC_TEMP_ID,A.PROC_DEF_TYPE_ID,A.PROC_DEF_TYPE_ID,A.BIZ_KEY,A.PROCESS_NAME,A.AUTHOR,A.COMMENTS,A.CREATE_DATE,A.COMMENTS,A.OVER_TIME "
                + " from BPM_PROCESS_TEMP A,BPM_PROC_DEF_TYPE B  WHERE A.STATE='A'  AND A.PROC_DEF_TYPE_ID=B.PROC_DEF_TYPE_ID");
        ParamArray pa = new ParamArray();
        if (procTempId != null) {
            qrySql.append(" AND A.PROC_TEMP_ID = ?");
            pa.set("PROC_TEMP_ID", procTempId);
        }
        if (procDefTypeId != null) {
            qrySql.append(" AND B.PROC_DEF_TYPE_ID = ?");
            pa.set("PROC_DEF_TYPE_ID", procDefTypeId);
        }
        return (List<DynamicDict>) query(qrySql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws BaseAppException,
                            SQLException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict procTemp = new DynamicDict();
                            procTemp.setValueByName("id",
                                    "procTemp" + op.getString(rs, dbloop++));
                            procTemp.setValueByName("PROC_TEMP_ID",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("parent",
                                    "procType" + op.getString(rs, dbloop++));
                            procTemp.setValueByName("PROC_DEF_TYPE_ID",
                                    op.getString(rs, "PROC_DEF_TYPE_ID"));
                            procTemp.setValueByName("BIZ_KEY",
                                    op.getString(rs, "BIZ_KEY"));
                            procTemp.setValueByName("name",
                                    op.getString(rs, "PROCESS_NAME"));
                            procTemp.setValueByName("AUTHOR",
                                    op.getString(rs, "AUTHOR"));
                            procTemp.setValueByName("COMMENTS",
                                    op.getString(rs, "COMMENTS"));
                            procTemp.setValueByName("CREATE_DATE",
                                    op.getString(rs, "CREATE_DATE"));
                            procTemp.setValueByName("OVER_TIME",
                                    op.getString(rs, "OVER_TIME"));

                            list.add(procTemp);
                        }
                        return list;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryProcVersion(Long procVerId, Long procTempId)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("select B.PROCESS_VER_ID,B.PROC_DEF_ID,B.DEPLOY_ID,B.PROC_TEMP_ID,B.FORM_ID,C.DYN_FORM_ID,C.FORM_TYPE,B.VER,B.VER,B.VER_STATE,B.USER_ID,B.EFFECTIVE_DATE,B.EXPIRED_DATE,B.CREATE_DATE,B.COMMENTS,A.PROCESS_NAME,C.PAGE_URL "
                + " from BPM_PROCESS_TEMP_VER B LEFT JOIN BPM_FORM C ON B.FORM_ID=C.FORM_ID ,BPM_PROCESS_TEMP A  WHERE A.PROC_TEMP_ID=B.PROC_TEMP_ID AND B.STATE='A' ");
        ParamArray pa = new ParamArray();
        if (procVerId != null) {
            qrySql.append(" AND B.PROCESS_VER_ID = ?");
            pa.set("PROCESS_VER_ID", procVerId);
        }
        if (procTempId != null) {
            qrySql.append(" AND B.PROC_TEMP_ID = ?");
            pa.set("PROC_TEMP_ID", procTempId);
        }
        qrySql.append(" ORDER BY PROCESS_VER_ID DESC");
        return (List<DynamicDict>) query(qrySql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws BaseAppException,
                            SQLException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict procTemp = new DynamicDict();
                            String id = op.getString(rs, dbloop++);
                            procTemp.setValueByName("id", "procVersion" + id);
                            procTemp.setValueByName("PROCESS_VER_ID", id);
                            procTemp.setValueByName("PROC_DEF_ID",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("DEPLOY_ID",
                                    op.getString(rs, dbloop++));
                            String tempId = op.getString(rs, dbloop++);
                            // 前台label属性
                            procTemp.setValueByName("parent", "procTemp"
                                    + tempId);
                            procTemp.setValueByName("PROC_TEMP_ID", tempId);
                            procTemp.setValueByName("FORM_ID",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("DYN_FORM_ID",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("FORM_TYPE",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("VER",
                                    op.getString(rs, dbloop++));
                            // 前台label属性
                            procTemp.setValueByName("name",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("VER_STATE",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("USER_ID",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("EFFECTIVE_DATE",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("EXPIRED_DATE",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("CREATE_DATE",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("VER_COMMENTS",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("PROCESS_NAME",
                                    op.getString(rs, dbloop++));
                            procTemp.setValueByName("PAGE_URL",
                                    op.getString(rs, dbloop++));
                            list.add(procTemp);
                        }
                        return list;
                    }

                });
    }

    public void insertProcTemp(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("INSERT INTO BPM_PROCESS_TEMP")
                .append(" (PROC_TEMP_ID,PROC_DEF_TYPE_ID,OVER_TIME,BIZ_KEY,PROCESS_NAME,AUTHOR,COMMENTS,CREATE_DATE,STATE,STATE_DATE) ")
                .append(" VALUES(?,?,?,?,?,?,?,?,?,?)");

        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("PROC_TEMP_ID"));
        pa.set("", dict.getLong("PROC_DEF_TYPE_ID"));
        pa.set("", dict.getLong("OVER_TIME"));
        pa.set("", dict.getString("BIZ_KEY"));
        pa.set("", dict.getString("name"));
        pa.set("", dict.getString("AUTHOR"));
        pa.set("", dict.getString("COMMENTS"));
        pa.set("", DateUtil.string2SQLDate((String) dict
                .getValueByName("STATE_DATE")));
        pa.set("", dict.getString("STATE"));
        pa.set("", DateUtil.string2SQLDate((String) dict
                .getValueByName("STATE_DATE")));

        executeUpdate(sqlStr.toString(), pa);

    }

    public void insertCatg(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("INSERT INTO BPM_PROC_CATG")
                .append(" (CATG_ID,PARENT_CATG_ID,NAME,COMMENTS) ")
                .append(" VALUES(?,?,?,?)");

        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("CATG_ID"));
        pa.set("", dict.getLong("PARENT_CATG_ID"));
        pa.set("", dict.getString("NAME"));
        pa.set("", dict.getString("COMMENTS"));

        executeUpdate(sqlStr.toString(), pa);

    }

    public void updateCatg(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE BPM_PROC_CATG SET PARENT_CATG_ID=?,NAME=?,COMMENTS=? WHERE CATG_ID=? ");

        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("PARENT_CATG_ID"));
        pa.set("", dict.getString("NAME"));
        pa.set("", dict.getString("COMMENTS"));
        pa.set("", dict.getLong("CATG_ID"));

        executeUpdate(sqlStr.toString(), pa);
    }

    public void delCatg(DynamicDict dict) throws BaseAppException {
        String sqlStr = "DELETE FROM  BPM_PROC_CATG WHERE CATG_ID=? ";

        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("CATG_ID"));

        executeUpdate(sqlStr.toString(), pa);
    }

    public void insertProcessType(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("INSERT INTO BPM_PROC_DEF_TYPE")
                .append(" (PROC_DEF_TYPE_ID,CATG_ID,NAME,COMMENTS,STATE,IS_RESERVED,STATE_DATE,CREATE_DATE) ")
                .append(" VALUES(?,?,?,?,?,?,?,?)");
        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("PROC_DEF_TYPE_ID"));
        pa.set("", dict.getLong("CATG_ID"));
        pa.set("", dict.getString("NAME"));
        pa.set("", dict.getString("COMMENTS"));
        pa.set("", dict.getString("STATE"));
        pa.set("", dict.getString("IS_RESERVED"));
        pa.set("", dict.getDate("STATE_DATE"));
        pa.set("", dict.getDate("CREATE_DATE"));

        executeUpdate(sqlStr.toString(), pa);

    }

    public void updateProcessType(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE BPM_PROC_DEF_TYPE SET CATG_ID=?,NAME=?,COMMENTS=?,STATE=?,IS_RESERVED=?,STATE_DATE=? WHERE PROC_DEF_TYPE_ID=? ");

        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("CATG_ID"));
        pa.set("", dict.getString("NAME"));
        pa.set("", dict.getString("COMMENTS"));
        pa.set("", dict.getString("STATE"));
        pa.set("", dict.getString("IS_RESERVED"));
        pa.set("", dict.getDate("STATE_DATE"));
        pa.set("", dict.getLong("PROC_DEF_TYPE_ID"));

        executeUpdate(sqlStr.toString(), pa);
    }

    public void deleteProcessType(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("DELETE FROM  BPM_PROC_DEF_TYPE  WHERE PROC_DEF_TYPE_ID=? ");
        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("PROC_DEF_TYPE_ID"));
        executeUpdate(sqlStr.toString(), pa);
    }

    public void updateProcTemp(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE BPM_PROCESS_TEMP SET")
                .append(" PROCESS_NAME=?,BIZ_KEY=?,COMMENTS=?,STATE_DATE=? ,OVER_TIME=?")
                .append(" WHERE PROC_TEMP_ID = ?");
        ParamArray pa = new ParamArray();

        pa.set("", dict.getString("name"));
        pa.set("", dict.getString("BIZ_KEY"));
        pa.set("", dict.getString("COMMENTS"));
        pa.set("", DateUtil.string2SQLDate((String) dict
                .getValueByName("STATE_DATE")));
        pa.set("", dict.getLong("OVER_TIME"));
        pa.set("", dict.getLong("PROC_TEMP_ID"));
        executeUpdate(sqlStr.toString(), pa);
    }

    public void insertProcVersion(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("INSERT INTO BPM_PROCESS_TEMP_VER ")
                .append(" (PROCESS_VER_ID,PROC_DEF_ID,PROC_TEMP_ID,FORM_ID,VER,VER_STATE,USER_ID,EFFECTIVE_DATE,EXPIRED_DATE,STATE,STATE_DATE,CREATE_DATE,COMMENTS) ")
                .append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ParamArray pa = new ParamArray();
        pa.set("", dict.getLong("PROCESS_VER_ID"));
        pa.set("", dict.getLong("PROC_DEF_ID", false));
        pa.set("", dict.getLong("PROC_TEMP_ID"));
        pa.set("", dict.getLong("FORM_ID", false));
        pa.set("", dict.getString("VER"));
        pa.set("", dict.getString("VER_STATE"));
        pa.set("", dict.getString("USER_ID"));
        pa.set("", DateUtil.string2SQLDate(dict.getString("EFFECTIVE_DATE")));
        pa.set("", DateUtil.string2SQLDate(dict.getString("EXPIRED_DATE")));
        pa.set("", dict.getString("STATE"));
        pa.set("", DateUtil.string2SQLDate((String) dict
                .getValueByName("STATE_DATE")));
        pa.set("", DateUtil.string2SQLDate((String) dict
                .getValueByName("STATE_DATE")));
        pa.set("", dict.getString("VER_COMMENTS", false));
        executeUpdate(sqlStr.toString(), pa);
    }

    public void updateProcVersion(DynamicDict dict) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_PROCESS_TEMP_VER SET STATE_DATE=? ");

        pa.set("", DateUtil.string2SQLDate((String) dict
                .getValueByName("STATE_DATE")));
        if (dict.getString("VER", false) != null) {
            sqlStr.append(",VER=?");
            pa.set("", dict.getString("VER"));
        }

        if (dict.getString("PROC_DEF_ID") != null) {
            sqlStr.append(",PROC_DEF_ID=?");
            pa.set("", dict.getString("PROC_DEF_ID"));
        }

        if (dict.getString("VER_STATE", false) != null) {
            sqlStr.append(",VER_STATE=?");
            pa.set("", dict.getString("VER_STATE"));
        }
        if (dict.getString("USER_ID", false) != null) {
            sqlStr.append(",USER_ID=?");
            pa.set("", dict.getString("USER_ID"));
        }
        if (dict.getString("EFFECTIVE_DATE", false) != null) {
            sqlStr.append(",EFFECTIVE_DATE=?");
            pa.set("",
                    DateUtil.string2SQLDate(dict.getString("EFFECTIVE_DATE")));
        }
        if (dict.getString("EXPIRED_DATE", false) != null) {
            sqlStr.append(",EXPIRED_DATE=?");
            pa.set("", DateUtil.string2SQLDate(dict.getString("EXPIRED_DATE")));
        }
        if (dict.getString("VER_COMMENTS", false) != null) {
            sqlStr.append(",COMMENTS=?");
            pa.set("", dict.getString("VER_COMMENTS"));
        }
        sqlStr.append(" WHERE PROCESS_VER_ID = ?");
        pa.set("", dict.getLong("PROCESS_VER_ID", false));
        executeUpdate(sqlStr.toString(), pa);
    }

    /**
     * 禁用或启用指定流程模板的所有版本
     *
     * @param state
     * @param procTempVerId
     * @throws SQLException
     * @throws BaseAppException
     */
    public void actOrDisProcVersion(String state, Long procTempVerId)
            throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_PROCESS_TEMP_VER SET VER_STATE=?,STATE_DATE=? ");
        sqlStr.append(" WHERE PROCESS_VER_ID = ?");
        pa.set("", state);
        pa.set("", DateUtil.string2SQLDate(DateUtil.getCurrentDate()));
        pa.set("", procTempVerId);
        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_A(long userId, String taskListId)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET USER_ID=?, STATE='A', STATE_DATE=now(), START_TIME=now()");
        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", userId);
        pa.set("", taskListId);
        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_A(String taskListId) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='A', STATE_DATE=now()");
        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", taskListId);
        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_C(String taskListId, Date cTime,
                               String taskResult, Long userId) throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();

        if (cTime == null) {
            cTime = new Date(System.currentTimeMillis());
        }
        sqlStr.append("UPDATE BPM_TASK_LIST SET END_TIME=?,STATE='C',TASK_RESULT=?,USER_ID=?");
        pa.set("", cTime);
        sqlStr.append(" WHERE TASK_LIST_ID = ?");
        pa.set("", taskResult);
        pa.set("", userId);
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_H(String taskListId, String suspendReason)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='H', STATE_DATE=now(), STATE_REASON=? WHERE TASK_LIST_ID = ?");
        pa.set("", suspendReason);
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }

    public void modTaskState_D(String taskListId, String deleteReason)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        ParamArray pa = new ParamArray();
        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='D', STATE_DATE=now(), STATE_REASON=? WHERE TASK_LIST_ID = ?");
        pa.set("", deleteReason);
        pa.set("", taskListId);

        executeUpdate(sqlStr.toString(), pa);
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qrySiginTypeList() throws BaseAppException {
        String sql = "SELECT SIGIN_TYPE,VOTE_CNT,SIGIN_NAME FROM BPM_TASK_SIGIN_TYPE";
        return (List<DynamicDict>) query(sql, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
                                  Object para) throws SQLException, BaseAppException {

                int dbloop = 1;
                List<DynamicDict> list = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    dbloop = 1;
                    DynamicDict catg = new DynamicDict();
                    catg.setValueByName("SIGIN_TYPE",
                            op.getString(rs, dbloop++));
                    catg.setValueByName("VOTE_CNT", op.getString(rs, dbloop++));
                    catg.setValueByName("SIGIN_NAME",
                            op.getString(rs, dbloop++));

                    list.add(catg);
                }
                return list;
            }
        });
    }

    public void insertTaskTemp(DynamicDict bo) throws BaseAppException {
        Long taskTempId = bo.getLong("TEMPLATE_ID");
        String templateType = bo.getString("TEMPLATE_TYPE");
        String taskType = bo.getString("TASK_TYPE");
        String taskTempName = bo.getString("TEMPLATE_NAME");
        String taskTempCode = bo.getString("CODE");
        String commons = bo.getString("COMMENTS");
        String formId = bo.getString("FORM_ID");
        Long warnLimit = bo.getLong("WARN_LIMIT");
        Long alertLimit = bo.getLong("ALERT_LIMIT");
        Long procDefTypeId = bo.getLong("PROC_DEF_TYPE_ID");
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("INSERT INTO BPM_TASK_TEMPLATE ")
                .append(" (TEMPLATE_ID,TEMPLATE_TYPE,PROC_DEF_TYPE_ID,TASK_TYPE,FORM_ID,TEMPLATE_NAME,CODE,WARN_LIMIT,ALARM_LIMIT,COMMENTS,STATE,STATE_DATE,CREATE_DATE,PHOTO_PATH) ");

        ParamArray pa = new ParamArray();
        pa.set("", taskTempId);
        pa.set("", templateType);
        pa.set("", procDefTypeId);
        pa.set("", taskType);
        if (StringUtil.isNotEmpty(formId)) {
            sqlStr.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pa.set("", Long.valueOf(formId));
        } else {
            sqlStr.append(" VALUES(?,?,?,?,null,?,?,?,?,?,?,?,?,?)");
        }
        pa.set("", taskTempName);
        pa.set("", taskTempCode);
        pa.set("", warnLimit);
        pa.set("", alertLimit);
        pa.set("", commons);
        pa.set("", "A");
        pa.set("", DateUtil.string2SQLDate((String) bo
                .getValueByName("CURR_DATE")));
        pa.set("", DateUtil.string2SQLDate((String) bo
                .getValueByName("CURR_DATE")));
        pa.set("", bo.getString("PHOTO_PATH"));

        executeUpdate(sqlStr.toString(), pa);

    }

    public void addServiceTask(DynamicDict bo) throws BaseAppException {
        String serviceName = bo.getString("SERVICE_NAME");
        Long taskTempId = bo.getLong("TEMPLATE_ID");
        String pageParam = bo.getString("PARAM_PAGE");
        String sql = "INSERT INTO BPM_SERV_TASK (SERVICE_NAME, TEMPLATE_ID, PARAM_PAGE) VALUES(?,?,?) ";
        ParamArray pa = new ParamArray();
        pa.set("", serviceName);
        pa.set("", taskTempId);
        pa.set("", pageParam);
        executeUpdate(sql.toString(), pa);
    }

    public void addUserTask(DynamicDict bo) throws BaseAppException {
        String isMultiSigin = bo.getString("IS_MULTI_SIGIN");
        String siginType = bo.getString("SIGIN_TYPE");
        if (StringUtil.isEmpty(siginType)) {
            siginType = null;
        }
        String signValue = bo.getString("SIGIN_VALUE");
        if (StringUtil.isEmpty(signValue)) {

            signValue = null;
        }
        Long userId = bo.getLong("USER_ID");
        Long roleId = bo.getLong("ROLE_ID");
        Long jobId = bo.getLong("JOB_ID");
        Long orgId = bo.getLong("ORG_ID");
        String preAssign = bo.getString("PRE_ASSIGN");
        Long taskTempId = bo.getLong("TEMPLATE_ID");
        String serviceName = bo.getString("DISPATCH_SERVICE_NAME");
        String sql = "INSERT INTO BPM_USER_TASK (TEMPLATE_ID,IS_MULTI_SIGIN,SIGIN_TYPE,SIGIN_VALUE,USER_ID,ROLE_ID,JOB_ID,ORG_ID,PRE_ASSIGN,SERVICE_NAME) VALUES(?,?,?,?,?,?,?,?,?,?) ";
        ParamArray pa = new ParamArray();
        pa.set("", taskTempId);
        pa.set("", isMultiSigin);
        pa.set("", siginType);
        pa.set("", signValue);
        pa.set("", userId);
        pa.set("", roleId);
        pa.set("", jobId);
        pa.set("", orgId);
        pa.set("", preAssign);
        pa.set("", serviceName);
        executeUpdate(sql.toString(), pa);
    }

    public void updateTaskTemp(DynamicDict bo) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE  BPM_TASK_TEMPLATE  SET STATE_DATE=?");
        ParamArray pa = new ParamArray();
        pa.set("", DateUtil.string2SQLDate((String) bo
                .getValueByName("CURR_DATE")));
        if (bo.getString("TEMPLATE_TYPE") != null) {
            sqlStr.append(",TEMPLATE_TYPE=?");
            pa.set("", bo.getString("TEMPLATE_TYPE"));
        }

        if (bo.getString("PROC_DEF_TYPE_ID") != null) {
            sqlStr.append(",PROC_DEF_TYPE_ID=?");
            pa.set("", bo.getLong("PROC_DEF_TYPE_ID"));
        }

        if (bo.getString("FORM_ID") != null) {
            sqlStr.append(",FORM_ID=?");
            pa.set("", bo.getLong("FORM_ID"));
        }
        if (bo.getString("TEMPLATE_NAME", false) != null) {
            sqlStr.append(",TEMPLATE_NAME=?");
            pa.set("", bo.getString("TEMPLATE_NAME"));
        }
        if (bo.getString("CODE", false) != null) {
            sqlStr.append(",CODE=?");
            pa.set("", bo.getString("CODE"));
        }
        sqlStr.append(",COMMENTS=?");
        if (bo.getString("COMMENTS", false) != null) {
            pa.set("", bo.getString("COMMENTS"));
        } else {
            pa.set("", "");
        }

        if (bo.getString("WARN_LIMIT") != null) {
            sqlStr.append(",WARN_LIMIT=?");
            pa.set("", bo.getLong("WARN_LIMIT"));
        } else {
            sqlStr.append(",WARN_LIMIT=null");
        }

        if (bo.getString("ALARM_LIMIT", false) != null) {
            sqlStr.append(",ALARM_LIMIT=?");
            pa.set("", bo.getLong("ALARM_LIMIT"));
        } else {
            sqlStr.append(",ALARM_LIMIT=null");
        }
        if (bo.getString("STATE", false) != null) {
            sqlStr.append(",STATE=?");
            pa.set("", bo.getString("STATE"));
        }

        if (bo.getString("PHOTO_PATH") != null) {
            sqlStr.append(",PHOTO_PATH=?");
            pa.set("", bo.getString("PHOTO_PATH"));
        }

		/*
         * if (bo.getString("DYN_FORM_ID", false) != null) {
		 * sqlStr.append(",FORM_ID=?"); pa.set("", bo.getLong("DYN_FORM_ID"));
		 * }else{ sqlStr.append(",FORM_ID = null"); }
		 */
        sqlStr.append(" WHERE TEMPLATE_ID = ?");
        pa.set("", bo.getLong("TEMPLATE_ID"));
        executeUpdate(sqlStr.toString(), pa);
    }

    public void updateServiceTask(DynamicDict bo) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE  BPM_SERV_TASK  SET SERVICE_NAME=?, PARAM_PAGE = ? WHERE TEMPLATE_ID=?");
        ParamArray pa = new ParamArray();
        pa.set("", bo.getString("SERVICE_NAME"));
        pa.set("", bo.getString("PARAM_PAGE"));
        pa.set("", bo.getLong("TEMPLATE_ID"));
        executeUpdate(sqlStr.toString(), pa);
    }

    public void updateUserTask(DynamicDict bo) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE  BPM_USER_TASK  SET IS_MULTI_SIGIN=?");
        ParamArray pa = new ParamArray();
        pa.set("", bo.getString("IS_MULTI_SIGIN"));
        if (bo.getString("SIGIN_TYPE") != null) {
            sqlStr.append(",SIGIN_TYPE=?");
            pa.set("", bo.getString("SIGIN_TYPE"));
        }
        if (bo.getString("SIGIN_VALUE") != null) {
            sqlStr.append(",SIGIN_VALUE=?");
            pa.set("", bo.getString("SIGIN_VALUE"));
        }
        if (bo.getString("USER_ID") != null) {
            sqlStr.append(",USER_ID=?");
            pa.set("", bo.getLong("USER_ID"));
        }
        if (bo.getString("ROLE_ID") != null) {
            sqlStr.append(",ROLE_ID=?");
            pa.set("", bo.getLong("ROLE_ID"));
        }
        if (bo.getString("JOB_ID") != null) {
            sqlStr.append(",JOB_ID=?");
            pa.set("", bo.getLong("JOB_ID"));
        }
        if (bo.getString("ORG_ID") != null) {
            sqlStr.append(",ORG_ID=?");
            pa.set("", bo.getLong("ORG_ID"));
        }
        if (bo.getString("DISPATCH_SERVICE_NAME") != null) {
            sqlStr.append(",SERVICE_NAME=?");
            pa.set("", bo.getString("DISPATCH_SERVICE_NAME"));
        }
        if (bo.getString("PRE_ASSIGN") != null) {
            sqlStr.append(",PRE_ASSIGN=?");
            pa.set("", bo.getString("PRE_ASSIGN"));
        }
        sqlStr.append(" WHERE TEMPLATE_ID = ?");
        pa.set("", bo.getLong("TEMPLATE_ID"));
        executeUpdate(sqlStr.toString(), pa);
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryTaskTempList(Long tempID, Long procTypeID,
                                             String taskTemplateCode) throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT A.FORM_ID,M.FORM_NAME,A.TEMPLATE_ID,A.PROC_DEF_TYPE_ID,A.TASK_TYPE,B.TASK_NAME,TEMPLATE_NAME,CODE,A.COMMENTS,A.STATE_DATE,A.WARN_LIMIT,A.ALARM_LIMIT,"
                + "A.CREATE_DATE,A.PHOTO_PATH,C.NAME,D.SERVICE_NAME,D.PARAM_PAGE,E.IS_MULTI_SIGIN,E.SIGIN_TYPE,E.PRE_ASSIGN,E.ROLE_ID,E.JOB_ID,E.USER_ID,E.ORG_ID,E.SERVICE_NAME DISPATCH_SERVICE,F.USER_NAME,G.ROLE_NAME ROLE_NAME,H.JOB_NAME,I.ORG_NAME "
                + " FROM BPM_TASK_TEMPLATE A LEFT JOIN BPM_SERV_TASK D ON A.TEMPLATE_ID=D.TEMPLATE_ID LEFT JOIN BPM_USER_TASK E ON A.TEMPLATE_ID=E.TEMPLATE_ID LEFT JOIN BFM_USER F ON E.USER_ID=F.USER_ID LEFT JOIN BFM_ROLE G ON E.ROLE_ID=G.ROLE_ID LEFT JOIN BFM_JOB H ON E.JOB_ID=H.JOB_ID LEFT JOIN BFM_ORG I ON E.ORG_ID=I.ORG_ID LEFT JOIN BPM_FORM M ON A.FORM_ID=M.FORM_ID, "
                + "BPM_TASK_TYPE B,BPM_PROC_DEF_TYPE C"
                + "  WHERE A.STATE='A'  AND A.PROC_DEF_TYPE_ID=C.PROC_DEF_TYPE_ID AND A.TASK_TYPE=B.TASK_TYPE  AND A.TASK_TYPE IN ('U','S') ");
        ParamArray pa = new ParamArray();
        if (tempID != null) {
            qrySql.append(" AND A.TEMPLATE_ID = ?");
            pa.set("TEMPLATE_ID", tempID);
        }
        if (procTypeID != null) {
            qrySql.append(" AND A.PROC_DEF_TYPE_ID = ?");
            pa.set("PROC_DEF_TYPE_ID", procTypeID);
        }
        if (taskTemplateCode != null) {
            qrySql.append(" AND A.CODE = ?");
            pa.set("TASK_TEMPLATE_CODE", taskTemplateCode);
        }
        qrySql.append(" ORDER BY TEMPLATE_ID");
        return (List<DynamicDict>) query(qrySql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("FORM_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("FORM_NAME",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TEMPLATE_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("PROC_DEF_TYPE_ID",
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
                            catg.setValueByName("PROC_TYPE_NAME",
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

    public List<ProcessDefView> qryAllProcessDef() throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT A.*, B.VER, B.PROC_DEF_ID, B.EFFECTIVE_DATE, B.EXPIRED_DATE FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'A' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < NOW() AND B.EXPIRED_DATE>NOW()");

        ParamArray pa = new ParamArray();

        return (List<ProcessDefView>) query(qrySql.toString(), pa, null,
                new RowSetMapper<List<ProcessDefView>>() {
                    public List<ProcessDefView> mapRows(RowSetOperator op,
                                                        ResultSet rs, int colNum, Object para)
                            throws BaseAppException, SQLException {

                        List<ProcessDefView> list = new ArrayList<ProcessDefView>();
                        while (rs.next()) {

                            ProcessDefView processDefView = new ProcessDefView();

                            processDefView.setDefinitionKey(op.getString(rs,
                                    "PROC_DEF_ID"));
                            processDefView.setProcessName(op.getString(rs,
                                    "PROCESS_NAME"));
                            processDefView.setCreateDate(op.getDate(rs,
                                    "CREATE_DATE"));
                            processDefView.setEffectiveDate(op.getDate(rs,
                                    "EFFECTIVE_DATE"));
                            processDefView.setExpiredDate(op.getDate(rs,
                                    "EXPIRED_DATE"));
                            processDefView.setVer(op.getString(rs, "VER"));

                            list.add(processDefView);
                        }
                        return list;
                    }
                });
    }

    public List<BpmProcessVarDto> qryProcessVarDef(String procDefId)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT * FROM ACT_RE_PROCDEF_VAR WHERE PROC_DEF_ID = ?");
        ParamArray pa = new ParamArray();
        pa.set("", procDefId);

        return (List<BpmProcessVarDto>) query(qrySql.toString(), pa, null,
                new RowSetMapper<List<BpmProcessVarDto>>() {
                    public List<BpmProcessVarDto> mapRows(RowSetOperator op,
                                                          ResultSet rs, int colNum, Object para)
                            throws BaseAppException, SQLException {

                        List<BpmProcessVarDto> list = new ArrayList<BpmProcessVarDto>();
                        while (rs.next()) {

                            BpmProcessVarDto bpmProcessVarDto = new BpmProcessVarDto();
                            bpmProcessVarDto.setId_(op.getLong(rs, "ID_"));
                            bpmProcessVarDto.setProcDefId(op.getString(rs,
                                    "PROC_DEF_ID"));
                            bpmProcessVarDto.setVarName(op.getString(rs,
                                    "VAR_NAME"));
                            bpmProcessVarDto.setVarType(op.getString(rs,
                                    "VAR_TYPE"));
                            bpmProcessVarDto.setDefaultValue(op.getString(rs,
                                    "DEFAULT_VALUE"));

                            list.add(bpmProcessVarDto);
                        }
                        return list;
                    }
                });
    }

    public List<BpmProcessVarDto> qryProcessVarDefByProcKey(String procKey)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT A.* FROM ACT_RE_PROCDEF_VAR A, ACT_RE_PROCDEF B, ");
        qrySql.append(" (SELECT MAX(VERSION_) MAXVERSION_ FROM ACT_RE_PROCDEF WHERE KEY_ = ?) C");
        qrySql.append(" WHERE A.PROC_DEF_ID = B.ID_ AND B.VERSION_ = C.MAXVERSION_ AND B.KEY_ = ?");

        ParamArray pa = new ParamArray();
        pa.set("", procKey);
        pa.set("", procKey);

        return (List<BpmProcessVarDto>) query(qrySql.toString(), pa, null,
                new RowSetMapper<List<BpmProcessVarDto>>() {
                    public List<BpmProcessVarDto> mapRows(RowSetOperator op,
                                                          ResultSet rs, int colNum, Object para)
                            throws BaseAppException, SQLException {

                        List<BpmProcessVarDto> list = new ArrayList<BpmProcessVarDto>();
                        while (rs.next()) {

                            BpmProcessVarDto bpmProcessVarDto = new BpmProcessVarDto();
                            bpmProcessVarDto.setId_(op.getLong(rs, "ID_"));
                            bpmProcessVarDto.setProcDefId(op.getString(rs,
                                    "PROC_DEF_ID"));
                            bpmProcessVarDto.setVarName(op.getString(rs,
                                    "VAR_NAME"));
                            bpmProcessVarDto.setVarType(op.getString(rs,
                                    "VAR_TYPE"));
                            bpmProcessVarDto.setDefaultValue(op.getString(rs,
                                    "DEFAULT_VALUE"));

                            list.add(bpmProcessVarDto);
                        }
                        return list;
                    }
                });
    }

    public List<BpmProcessVarDto> qryProcessVarDefByDeployId(String deployId)
            throws BaseAppException {

        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT A.* FROM ACT_RE_PROCDEF_VAR A, ACT_RE_PROCDEF B WHERE A.PROC_DEF_ID = B.ID_ AND B.DEPLOYMENT_ID_ = ?");
        ParamArray pa = new ParamArray();
        pa.set("", deployId);

        return (List<BpmProcessVarDto>) query(qrySql.toString(), pa, null,
                new RowSetMapper<List<BpmProcessVarDto>>() {
                    public List<BpmProcessVarDto> mapRows(RowSetOperator op,
                                                          ResultSet rs, int colNum, Object para)
                            throws BaseAppException, SQLException {

                        List<BpmProcessVarDto> list = new ArrayList<BpmProcessVarDto>();
                        while (rs.next()) {

                            BpmProcessVarDto bpmProcessVarDto = new BpmProcessVarDto();
                            bpmProcessVarDto.setId_(op.getLong(rs, "ID_"));
                            bpmProcessVarDto.setProcDefId(op.getString(rs,
                                    "PROC_DEF_ID"));
                            bpmProcessVarDto.setVarName(op.getString(rs,
                                    "VAR_NAME"));
                            bpmProcessVarDto.setVarType(op.getString(rs,
                                    "VAR_TYPE"));
                            bpmProcessVarDto.setDefaultValue(op.getString(rs,
                                    "DEFAULT_VALUE"));
                            bpmProcessVarDto.setVarComments(op.getString(rs,
                                    "VAR_COMMENTS"));
                            list.add(bpmProcessVarDto);
                        }
                        return list;
                    }
                });

    }

    @SuppressWarnings({"unchecked"})
    public List<DynamicDict> qryProcCatgTree() throws BaseAppException {
        // TODO Auto-generated method stub
        String sql = "SELECT CATG_ID,PARENT_CATG_ID,NAME,COMMENTS FROM BPM_PROC_CATG ORDER BY CATG_ID";
        // START WITH PARENT_CATG_ID IS NULL CONNECT BY PRIOR CATG_ID =
        // PARENT_CATG_ID ORDER BY LEVEL
        List<DynamicDict> list = (List<DynamicDict>) query(sql, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws BaseAppException,
                            SQLException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        String parentId = null;
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("CATG_ID",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("id",
                                    "catg" + catg.getString("CATG_ID"));
                            catg.setValueByName("PARENT_CATG_ID",
                                    op.getString(rs, dbloop++));
                            if (StringUtil.isNotEmpty(catg
                                    .getString("PARENT_CATG_ID"))) {
                                catg.setValueByName(
                                        "parent",
                                        "catg"
                                                + catg.getString("PARENT_CATG_ID"));
                            }
                            catg.setValueByName("name",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("COMMENTS",
                                    op.getString(rs, dbloop++));
                            list.add(catg);
                        }
                        return list;
                    }
                });
        // 递归获取菜单
        ArrayList<DynamicDict> newList = new ArrayList<DynamicDict>();
        for (DynamicDict dictOne : list) {
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put("ID", "id");
            keyMap.put("PARENT_ID", "PARENT_CATG_ID");

            newList.add(dictOne);
            keyMap.put("PARENT_CODE", dictOne.getString(keyMap.get("ID")));
            this.recuriveList(keyMap, list, newList);
        }

        return newList;
    }

    public void modTaskState(String taskListId, String taskState, Long userId,
                             String stateReason) throws BaseAppException {

        StringBuilder builderSql = new StringBuilder();
        builderSql
                .append("UPDATE BPM_TASK_LIST SET END_TIME=NOW(),STATE=?, STATE_DATE=NOW(), STATE_REASON=?,USER_ID=? ");
        builderSql.append(" WHERE TASK_LIST_ID = ?");
        ParamArray pa = new ParamArray();
        pa.set("", taskState);
        pa.set("", stateReason);
        pa.set("", userId);
        pa.set("", taskListId);

        executeUpdate(builderSql.toString(), pa);

    }

    /**
     * 查询所有流程仿真版本
     */
    public List<ProcessDefView> qryAllSimProcessVersion(DynamicDict dynamicDict)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT A.*, B.VER, B.PROC_DEF_ID, B.EFFECTIVE_DATE, B.EXPIRED_DATE FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < sysdate() AND B.EXPIRED_DATE>sysdate()");
        qrySql.append(" [AND A.PROCESS_NAME like :PROCESS_NAME] [AND A.BIZ_KEY like :BIZ_KEY]");

        ParamMap pm = new ParamMap();
        if (StringUtil.isNotEmpty(dynamicDict.getString("PROCESS_NAME"))) {
            pm.set("PROCESS_NAME", "%" + dynamicDict.getString("PROCESS_NAME")
                    + "%");
        }

        if (StringUtil.isNotEmpty(dynamicDict.getString("BIZ_KEY"))) {
            pm.set("BIZ_KEY", "%" + dynamicDict.getString("BIZ_KEY") + "%");
        }

        return (List<ProcessDefView>) query(qrySql.toString(), pm,
                (RowSetFormatter) BoHelper.boToDto(dynamicDict.getBO("ROW_SET_FORMATTER"), RowSetFormatter.class), null,
                new RowSetMapper<List<ProcessDefView>>() {
                    public List<ProcessDefView> mapRows(RowSetOperator op,
                                                        ResultSet rs, int colNum, Object para)
                            throws BaseAppException, SQLException {

                        List<ProcessDefView> list = new ArrayList<ProcessDefView>();
                        while (rs.next()) {

                            ProcessDefView processDefView = new ProcessDefView();

                            processDefView.setDefinitionKey(op.getString(rs,
                                    "PROC_DEF_ID"));
                            processDefView.setProcessName(op.getString(rs,
                                    "PROCESS_NAME"));
                            processDefView.setCreateDate(op.getDate(rs,
                                    "CREATE_DATE"));
                            processDefView.setEffectiveDate(op.getDate(rs,
                                    "EFFECTIVE_DATE"));
                            processDefView.setExpiredDate(op.getDate(rs,
                                    "EXPIRED_DATE"));
                            processDefView.setVer(op.getString(rs, "VER"));
                            processDefView.setBizKey(op
                                    .getString(rs, "BIZ_KEY"));

                            list.add(processDefView);
                        }
                        return list;
                    }
                });
    }

    /**
     * 绑定一个form到流程版本
     */
    @Override
    public void bindFormToFlowVer(Long flowVerId, Long formId)
            throws BaseAppException {
        String sql = "UPDATE BPM_PROCESS_TEMP_VER SET FORM_ID = :FORM_ID WHERE PROCESS_VER_ID = :PROCESS_VER_ID";
        ParamMap pm = new ParamMap();
        pm.set("FORM_ID", formId);
        pm.set("PROCESS_VER_ID", flowVerId);
        this.executeUpdate(sql, pm);
    }

    @Override
    public HolderNoIdDto qryProcessHolderNum(String processInstanceId)
            throws BaseAppException {
        String sql = "SELECT HOLDER_NO, HOLDER_ID FROM BPM_TASK_HOLDER WHERE PROC_INST_ID = ?";

        return this.selectObject(sql, HolderNoIdDto.class, processInstanceId);
    }

    @Override
    public long qryAllSimProcessVersionCount(DynamicDict dynamicDict)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT count(1) FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < sysdate() AND B.EXPIRED_DATE>sysdate() ");
        qrySql.append(" [AND A.PROCESS_NAME like :PROCESS_NAME] [AND A.BIZ_KEY like :BIZ_KEY]");

        ParamMap pm = new ParamMap();

        if (StringUtil.isNotEmpty(dynamicDict.getString("PROCESS_NAME"))) {
            pm.set("PROCESS_NAME", "%" + dynamicDict.getString("PROCESS_NAME")
                    + "%");
        }

        if (StringUtil.isNotEmpty(dynamicDict.getString("BIZ_KEY"))) {
            pm.set("BIZ_KEY", "%" + dynamicDict.getString("BIZ_KEY") + "%");
        }

        return this.selectCount(qrySql.toString(), pm);
    }

    public List<TaskStatisDto> taskStateStatistic() throws BaseAppException {
        String selectSql = "SELECT COUNT(1) STATE_COUNT,STATE FROM BPM_TASK_LIST GROUP BY STATE HAVING STATE = 'I' OR STATE = 'H' OR STATE = 'H' OR STATE = 'E' ";
        return this.selectList(selectSql, TaskStatisDto.class);

    }

    @Override
    public BpmBackHolderDto selBackHolder(String holderId)
            throws BaseAppException {

        String selectSql = "SELECT A.BACK_REASON_ID, B.TAG_ACT_ID_"
                + " FROM BPM_BACK_HOLDER A, ACT_RE_BACK_CFG B WHERE A.STATE = 'A' AND A.HOLDER_ID = ?"
                + " AND A.BACK_REASON_ID = B.BACK_REASON_ID_";

        return this.selectObject(selectSql, BpmBackHolderDto.class, holderId);
    }

    @Override
    public void insertBackHolder(ProcessBackInfo processBackInfo)
            throws BaseAppException {

        String insertSql = "INSERT INTO BPM_BACK_HOLDER (BACK_ID, HOLDER_ID, BACK_REASON_ID, BACK_USER, BACK_COMMENTS, STATE, CREATED_TIME)"
                + " VALUES (:BACK_ID, :HOLDER_ID, :BACK_REASON_ID, :BACK_USER, :BACK_COMMENTS, :STATE, :CREATED_TIME)";

        this.updateObject(insertSql, processBackInfo);
    }

    @Override
    public void completeBackHolder(String recordId) throws BaseAppException {

        String updateSql = "UPDATE BPM_BACK_HOLDER SET STATE = 'C', STATE_STATE = ?, where BACK_ID =?";

        this.updateObject(updateSql, DateUtil.GetDBDateTime(), recordId);
    }

    @Override
    public void updateTaskListState(String taskListId, String state)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE=?, STATE_DATE=? WHERE TASK_LIST_ID = ?");

        this.updateObject(sqlStr.toString(), state, DateUtil.GetDBDateTime(),
                taskListId);

    }

    @Override
    public void backTaskList(String taskListId, Long userId)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("UPDATE BPM_TASK_LIST SET STATE='B', END_TIME=?, USER_ID=? WHERE TASK_LIST_ID = ?");

        this.updateObject(sqlStr.toString(), DateUtil.GetDBDateTime(), userId,
                taskListId);
    }

    @Override
    public List<TaskBackReasonInfo> qryProcessBackCfgByDeployId(String deployId)
            throws BaseAppException {

        String qrySql = "SELECT A.BACK_REASON_ID_ BACK_REASON_ID, A.PROC_DEF_ID_ PROC_DEF_ID, A.SRC_ACT_ID_ SRC_ACT_ID,"
                + " A.TAG_ACT_ID_ TAG_ACT_ID, C.TASK_TEMP_ID, C.REASON_CODE, C.REASON_NAME"
                + " FROM ACT_RE_BACK_CFG A, ACT_RE_PROCDEF B, BPM_BACK_REASON C"
                + " WHERE A.PROC_DEF_ID_ = B.ID_ AND B.DEPLOYMENT_ID_ = ? AND A.BACK_REASON_ID_ = C.BACK_REASON_ID";

        return this.selectList(qrySql, TaskBackReasonInfo.class, deployId);
    }

    @Override
    public List<DynamicDict> qryTaskTempList(DynamicDict conditionDict)
            throws BaseAppException {

        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT A.FORM_ID,M.FORM_NAME,A.TEMPLATE_ID,A.PROC_DEF_TYPE_ID,A.TEMPLATE_TYPE,A.TASK_TYPE,B.TASK_NAME,TEMPLATE_NAME,CODE,A.COMMENTS,A.STATE_DATE,A.WARN_LIMIT,A.ALARM_LIMIT,"
                + "A.CREATE_DATE,A.PHOTO_PATH,C.TYPE_NAME,D.SERVICE_NAME,D.PARAM_PAGE,E.IS_MULTI_SIGIN,E.SIGIN_TYPE,E.PRE_ASSIGN,E.ROLE_ID,E.JOB_ID,E.USER_ID,E.ORG_ID,E.SERVICE_NAME DISPATCH_SERVICE,F.USER_NAME,G.ROLE_NAME ROLE_NAME,H.JOB_NAME,I.ORG_NAME "
                + " FROM BPM_TASK_TEMPLATE A LEFT JOIN BPM_SERV_TASK D ON A.TEMPLATE_ID=D.TEMPLATE_ID LEFT JOIN BPM_USER_TASK E ON A.TEMPLATE_ID=E.TEMPLATE_ID LEFT JOIN BFM_USER F ON E.USER_ID=F.USER_ID LEFT JOIN BFM_ROLE G ON E.ROLE_ID=G.ROLE_ID LEFT JOIN BFM_JOB H ON E.JOB_ID=H.JOB_ID LEFT JOIN BFM_ORG I ON E.ORG_ID=I.ORG_ID LEFT JOIN BPM_FORM M ON A.FORM_ID=M.FORM_ID, "
                + "BPM_TASK_TYPE B,BPM_TASK_TEMPLATE_TYPE C, BPM_PROC_DEF_TYPE Q"
                + "  WHERE A.STATE='A'  AND Q.PROC_DEF_TYPE_ID = A.PROC_DEF_TYPE_ID AND A.TEMPLATE_TYPE=C.TEMPLATE_TYPE AND A.TASK_TYPE=B.TASK_TYPE  AND A.TASK_TYPE IN ('U','S') ");

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
        if (conditionDict.getLong("TEMPLATE_ID") != null) {
            qrySql.append(" AND A.TEMPLATE_ID = :TEMPLATE_ID");
            pa.set("TEMPLATE_ID",
                    conditionDict.getLong("TEMPLATE_ID"));
        }

        qrySql.append(" ORDER BY TEMPLATE_ID");

        RowSetFormatter rowSetFormatter = null;
        if (conditionDict.get("ROW_SET_FORMATTER") != null) {

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

                        //int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            //dbloop = 1;
                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("FORM_ID", op.getString(rs, "FORM_ID"));
                            catg.setValueByName("FORM_NAME", op.getString(rs, "FORM_NAME"));
                            catg.setValueByName("TEMPLATE_ID", op.getString(rs, "TEMPLATE_ID"));
                            catg.setValueByName("PROC_DEF_TYPE_ID", op.getString(rs, "PROC_DEF_TYPE_ID"));
                            catg.setValueByName("TEMPLATE_TYPE", op.getString(rs, "TEMPLATE_TYPE"));
                            catg.setValueByName("TASK_TYPE", op.getString(rs, "TASK_TYPE"));
                            catg.setValueByName("TASK_TYPE_NAME", op.getString(rs, "TASK_NAME"));
                            catg.setValueByName("TEMPLATE_NAME", op.getString(rs, "TEMPLATE_NAME"));
                            catg.setValueByName("CODE", op.getString(rs, "CODE"));
                            catg.setValueByName("COMMENTS", op.getString(rs, "COMMENTS"));
                            catg.setValueByName("STATE_DATE", op.getString(rs, "STATE_DATE"));
                            catg.setValueByName("WARN_LIMIT", op.getString(rs, "WARN_LIMIT"));
                            catg.setValueByName("ALARM_LIMIT", op.getString(rs, "ALARM_LIMIT"));
                            catg.setValueByName("CREATE_DATE", op.getString(rs, "CREATE_DATE"));
                            catg.setValueByName("PHOTO_PATH", op.getString(rs, "PHOTO_PATH"));
                            catg.setValueByName("TEMPLATE_TYPE_NAME", op.getString(rs, "TYPE_NAME"));
                            catg.setValueByName("SERVICE_NAME", op.getString(rs, "SERVICE_NAME"));
                            catg.setValueByName("PARAM_PAGE", op.getString(rs, "PARAM_PAGE"));
                            catg.setValueByName("IS_MULTI_SIGIN", op.getString(rs, "IS_MULTI_SIGIN"));
                            catg.setValueByName("SIGIN_TYPE", op.getString(rs, "SIGIN_TYPE"));
                            catg.setValueByName("PRE_ASSIGN", op.getString(rs, "PRE_ASSIGN"));
                            catg.setValueByName("ROLE_ID", op.getString(rs, "ROLE_ID"));
                            catg.setValueByName("JOB_ID", op.getString(rs, "JOB_ID"));
                            catg.setValueByName("USER_ID", op.getString(rs, "USER_ID"));
                            catg.setValueByName("ORG_ID", op.getString(rs, "ORG_ID"));
                            catg.setValueByName("DISPATCH_SERVICE_NAME", op.getString(rs, "DISPATCH_SERVICE"));
                            catg.setValueByName("USER_NAME", op.getString(rs, "USER_NAME"));
                            catg.setValueByName("ROLE_NAME", op.getString(rs, "ROLE_NAME"));
                            catg.setValueByName("JOB_NAME", op.getString(rs, "JOB_NAME"));
                            catg.setValueByName("ORG_NAME", op.getString(rs, "ORG_NAME"));
//							catg.setValueByName("FORM_ID",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("FORM_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("TEMPLATE_ID",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("PROC_DEF_TYPE_ID",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("TEMPLATE_TYPE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("TASK_TYPE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("TASK_TYPE_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("TEMPLATE_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("CODE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("COMMENTS",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("STATE_DATE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("WARN_LIMIT",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("ALARM_LIMIT",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("CREATE_DATE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("PHOTO_PATH",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("TEMPLATE_TYPE_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("SERVICE_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("PARAM_PAGE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("IS_MULTI_SIGIN",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("SIGIN_TYPE",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("PRE_ASSIGN",
//									op.getString(rs, dbloop++));
//
//							catg.setValueByName("ROLE_ID",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("JOB_ID",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("USER_ID",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("ORG_ID",
//									op.getString(rs, dbloop++));
//
//							catg.setValueByName("DISPATCH_SERVICE_NAME",
//									op.getString(rs, dbloop++));
//
//							catg.setValueByName("USER_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("ROLE_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("JOB_NAME",
//									op.getString(rs, dbloop++));
//							catg.setValueByName("ORG_NAME",
//									op.getString(rs, dbloop++));

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

    @Override
    public List<DynamicDict> qryTemplateType() throws BaseAppException {

        return (List<DynamicDict>) query(
                "SELECT TEMPLATE_TYPE, TYPE_NAME FROM BPM_TASK_TEMPLATE_TYPE",
                null, null, new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict catg = new DynamicDict();
                            catg.setValueByName("TEMPLATE_TYPE",
                                    op.getString(rs, dbloop++));
                            catg.setValueByName("TYPE_NAME",
                                    op.getString(rs, dbloop++));
                            list.add(catg);
                        }
                        return list;
                    }

                });
    }

    @Override
    public long qryTaskTempListCount(DynamicDict conditionDict)
            throws BaseAppException {

        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT COUNT(1) FROM (");
        qrySql.append("SELECT A.FORM_ID,A.PROC_DEF_TYPE_ID,M.FORM_NAME,A.TEMPLATE_ID,A.TEMPLATE_TYPE,A.TASK_TYPE,B.TASK_NAME,TEMPLATE_NAME,CODE,A.COMMENTS,A.STATE_DATE,A.WARN_LIMIT,A.ALARM_LIMIT,"
                + "A.CREATE_DATE,A.PHOTO_PATH,C.TYPE_NAME,D.SERVICE_NAME,D.PARAM_PAGE,E.IS_MULTI_SIGIN,E.SIGIN_TYPE,E.PRE_ASSIGN,E.ROLE_ID,E.JOB_ID,E.USER_ID,E.ORG_ID,E.SERVICE_NAME DISPATCH_SERVICE,F.USER_NAME,G.ROLE_NAME ROLE_NAME,H.JOB_NAME,I.ORG_NAME "
                + " FROM BPM_TASK_TEMPLATE A LEFT JOIN BPM_SERV_TASK D ON A.TEMPLATE_ID=D.TEMPLATE_ID LEFT JOIN BPM_USER_TASK E ON A.TEMPLATE_ID=E.TEMPLATE_ID LEFT JOIN BFM_USER F ON E.USER_ID=F.USER_ID LEFT JOIN BFM_ROLE G ON E.ROLE_ID=G.ROLE_ID LEFT JOIN BFM_JOB H ON E.JOB_ID=H.JOB_ID LEFT JOIN BFM_ORG I ON E.ORG_ID=I.ORG_ID LEFT JOIN BPM_FORM M ON A.FORM_ID=M.FORM_ID, "
                + "BPM_TASK_TYPE B,BPM_TASK_TEMPLATE_TYPE C, BPM_PROC_DEF_TYPE Q"
                + "  WHERE A.STATE='A' AND Q.PROC_DEF_TYPE_ID = A.PROC_DEF_TYPE_ID  AND A.TEMPLATE_TYPE=C.TEMPLATE_TYPE AND A.TASK_TYPE=B.TASK_TYPE  AND A.TASK_TYPE IN ('U','S') ");

        ParamArray pa = new ParamArray();
        if (StringUtil.isNotEmpty(conditionDict.getString("CODE"))) {
            qrySql.append(" AND A.CODE = ?");
            pa.set("CODE", conditionDict.getString("CODE"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TASK_TYPE"))) {
            qrySql.append(" AND A.TASK_TYPE = ?");
            pa.set("TASK_TYPE", conditionDict.getString("TASK_TYPE"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TEMPLATE_NAME"))) {
            qrySql.append(" AND A.TEMPLATE_NAME = ?");
            pa.set("TEMPLATE_NAME", conditionDict.getString("TEMPLATE_NAME"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TEMPLATE_TYPE"))) {
            qrySql.append(" AND A.TEMPLATE_TYPE = ?");
            pa.set("TEMPLATE_TYPE", conditionDict.getString("TEMPLATE_TYPE"));
        }
        if (StringUtil.isNotEmpty(conditionDict.getString("TEMPLATE_TYPE"))) {
            qrySql.append(" AND A.TEMPLATE_TYPE = ?");
            pa.set("TEMPLATE_TYPE", conditionDict.getString("TEMPLATE_TYPE"));
        }
        if (conditionDict.getLong("PROC_DEF_TYPE_ID") != null) {
            qrySql.append(" AND A.PROC_DEF_TYPE_ID = ?");
            pa.set("PROC_DEF_TYPE_ID",
                    conditionDict.getLong("PROC_DEF_TYPE_ID"));
        }
        if (conditionDict.getLong("TEMPLATE_ID") != null) {
            qrySql.append(" AND A.TEMPLATE_ID = ?");
            pa.set("TEMPLATE_ID",
                    conditionDict.getLong("TEMPLATE_ID"));
        }

        // qrySql.append(" ORDER BY TEMPLATE_ID");

        qrySql.append(") XX");

        return (Long) query(qrySql.toString(), pa, null,
                new RowSetMapper<Long>() {
                    public Long mapRows(RowSetOperator op, ResultSet rs,
                                        int colNum, Object para) throws SQLException,
                            BaseAppException {

                        long count = 0;
                        if (rs.next()) {
                            count = op.getLong(rs, 1);
                        }
                        return count;
                    }

                });

    }

    @Override
    public List<DynamicDict> qryPhotoTemplate(DynamicDict conditionDict)
            throws BaseAppException {

        StringBuffer qrySql = new StringBuffer();
        qrySql.append("SELECT A.FORM_ID,M.FORM_NAME,A.TEMPLATE_ID,A.TEMPLATE_TYPE,A.TASK_TYPE,B.TASK_NAME,TEMPLATE_NAME,CODE,A.COMMENTS,A.STATE_DATE,A.WARN_LIMIT,A.ALARM_LIMIT,"
                + "A.CREATE_DATE,A.PHOTO_PATH,C.TYPE_NAME,D.SERVICE_NAME,D.PARAM_PAGE,E.IS_MULTI_SIGIN,E.SIGIN_TYPE,E.PRE_ASSIGN,E.ROLE_ID,E.JOB_ID,E.USER_ID,E.ORG_ID,E.SERVICE_NAME,F.USER_NAME,G.ROLE_NAME ROLE_NAME,H.JOB_NAME,I.ORG_NAME "
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

        qrySql.append(" ORDER BY TEMPLATE_ID");

        return (List<DynamicDict>) query(qrySql.toString(), pa, null, null,
                new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
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
                            String photoPath = op.getString(rs, dbloop++);
                            if (StringUtil.isEmpty(photoPath)) {
                                continue;
                            }
                            catg.setValueByName("PHOTO_PATH", photoPath);
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

                            list.add(catg);
                        }
                        return list;
                    }

                });
    }

    @Override
    public List<ServiceParamDto> qryProcessTacheVarDefByDeployId(String deployId)
            throws BaseAppException {

        return this
                .selectList(
                        "SELECT A.* FROM ACT_RE_TACHE_VAR A, ACT_RE_PROCDEF B WHERE A.ID = B.ID_ AND B.DEPLOYMENT_ID_ = ?",
                        ServiceParamDto.class, deployId);
    }

    @Override
    public DynamicDict qryProcCatgDetail(Long catId) throws BaseAppException {

        return this.selectObject("SELECT * FROM BPM_PROC_CATG WHERE CATG_ID = ?", catId);
    }

    @Override
    public void delProcTemp(DynamicDict dict) throws BaseAppException {

        this.updateObject("delete from FROM BPM_PROCESS_TEMP WHERE PROC_TEMP_ID = ?", dict.getLong("PROC_TEMP_ID"));
        this.updateObject("delete from FROM BPM_PROCESS_TEMP_VER WHERE PROCESS_VER_ID = ?", dict.getLong("PROCESS_VER_ID"));
    }

    @Override
    public List<BpmTaskTemplateDto> qryCustomTemplate() throws BaseAppException {

        String sql = "SELECT A.TEMPLATE_ID, A.TASK_TYPE, A.TEMPLATE_TYPE, TEMPLATE_NAME, A.CODE, A.PHOTO_PATH, B.TYPE_NAME FROM BPM_TASK_TEMPLATE A, BPM_TASK_TEMPLATE_TYPE B"
                + " WHERE A.TEMPLATE_TYPE = B.TEMPLATE_TYPE AND A.PHOTO_PATH <> '' AND A.TEMPLATE_TYPE <> '' AND A.STATE = 'A' ORDER BY A.TEMPLATE_TYPE";

        return this.selectList(sql, BpmTaskTemplateDto.class);
    }
}
