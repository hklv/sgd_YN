package com.ztesoft.uboss.bpm.setup.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.model.ButtonQueryCondition;
import com.ztesoft.uboss.bpm.setup.model.TaskEventDto;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.JdbcTemplate;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.ParamObject;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;
import org.activiti.uboss.ext.ServiceParamDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TaskTemplateDAO extends BusiBaseDAO implements ITaskTemplateDAO {

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryButtonList(DynamicDict bo, ButtonQueryCondition condition) throws BaseAppException {
        String sql = "select A.BTN_ID,A.BTN_NAME,A.COMMENTS,A.STATE,A.STATE_DATE,A.PAGE_URL  "
                + " FROM BPM_BUTTON A WHERE A.STATE='A' [AND A.BTN_NAME LIKE :BTN_NAME] ORDER BY BTN_ID DESC";

        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "BTN_NAME", bo.getString("BTN_NAME_Q"));
        String countSql = new StringBuilder().append("select count(*) cnt from BPM_BUTTON A WHERE A.STATE='A' [AND A.BTN_NAME LIKE :BTN_NAME]")
                .toString();
        Integer count = this.query(countSql, pm, null, null, new RowSetMapper<Integer>() {
            public Integer mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException, BaseAppException {
                Integer count = 0;
                if (rs.next()) {
                    count = op.getInteger(rs, 1);
                }
                return count;
            }
        });

        bo.set("BFM_USER_LIST_COUNT", count);
        if (count == 0) {
            return null;
        }
        return (List<DynamicDict>) this.query(sql, pm, condition.getRowSetFormatter(), null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException, BaseAppException {
                List<DynamicDict> btnList = new ArrayList();
                while (rs.next()) {
                    int flag = 1;
                    DynamicDict temp = new DynamicDict();
                    temp.set("BTN_ID", op.getString(rs, "BTN_ID"));
                    temp.set("BTN_NAME", op.getString(rs, "BTN_NAME"));
                    temp.set("COMMENTS", op.getString(rs, "COMMENTS"));
                    temp.set("STATE", op.getString(rs, "STATE"));
                    temp.set("STATE_DATE", op.getValue(rs, "STATE_DATE"));
                    temp.set("PAGE_URL", op.getString(rs, "PAGE_URL"));
                    btnList.add(temp);
                }
                return btnList;
            }
        });
    }

    public int addButton(DynamicDict bo) throws BaseAppException {
        String sql = "INSERT INTO BPM_BUTTON (BTN_ID,PAGE_URL, BTN_NAME, COMMENTS, STATE, STATE_DATE) VALUES (?, ?, ?, ?, 'A',?)";

        ParamArray pa = new ParamArray();
        pa.set("", bo.getLong("BTN_ID"));
        pa.set("", bo.getString("PAGE_URL"));
        pa.set("", bo.getString("BTN_NAME"));
        pa.set("", bo.getString("COMMENTS"));
        pa.set("", bo.getDate("STATE_DATE"));

        return this.executeUpdate(sql, pa);

    }

    public int updateButton(DynamicDict bo) throws BaseAppException {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("UPDATE BPM_BUTTON SET PAGE_URL = ?, BTN_NAME= ?, COMMENTS = ?,STATE_DATE=? WHERE BTN_ID = ?");

        ParamArray pa = new ParamArray();
        pa.set("", bo.getString("PAGE_URL"));
        pa.set("", bo.getString("BTN_NAME"));
        pa.set("", bo.getString("COMMENTS"));
        pa.set("", bo.getDate("STATE_DATE"));
        pa.set("", bo.getLong("BTN_ID"));

        return this.executeUpdate(sqlBuffer.toString(), pa);

    }

    public int delButton(DynamicDict bo) throws BaseAppException {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("UPDATE BPM_BUTTON SET STATE = 'X', STATE_DATE = ? WHERE BTN_ID = ?");
        ParamArray pa = new ParamArray();
        pa.set("", bo.getDate("STATE_DATE"));
        pa.set("", bo.getLong("BTN_ID"));

        return this.executeUpdate(sqlBuffer.toString(), pa);

    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryBtnListByTemp(Long templateId) throws BaseAppException {
        String sql = "SELECT A.BTN_ID,A.ALIAS_NAME,A.BTN_SEQ_NBR,B.BTN_NAME,B.COMMENTS,B.PAGE_URL from BPM_TASK_BUTTON A ,BPM_BUTTON B WHERE A.BTN_ID=B.BTN_ID AND B.STATE='A' [AND A.TEMPLATE_ID = :TEMPLATE_ID] ORDER BY A.BTN_SEQ_NBR";
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "TEMPLATE_ID", templateId);

        return (List<DynamicDict>) this.query(sql, pm, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException, BaseAppException {
                List<DynamicDict> btnList = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    int flag = 1;
                    DynamicDict temp = new DynamicDict();
                    temp.set("BTN_ID", op.getString(rs, "BTN_ID"));
                    temp.set("ALIAS_NAME", op.getString(rs, "ALIAS_NAME"));
                    temp.set("BTN_SEQ_NBR", op.getString(rs, "BTN_SEQ_NBR"));
                    temp.set("BTN_NAME", op.getString(rs, "BTN_NAME"));
                    temp.set("COMMENTS", op.getString(rs, "COMMENTS"));
                    temp.set("PAGE_URL", op.getString(rs, "PAGE_URL"));
                    btnList.add(temp);
                }
                return btnList;
            }
        });
    }

    /**
     * 查询不跟流程模板关联的按钮
     *
     * @param bo
     * @param condition
     * @return
     * @throws BaseAppException
     */
    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryBtnListNotInTemplate(DynamicDict bo, ButtonQueryCondition condition) throws BaseAppException {
        String sql = "select A.BTN_ID,A.BTN_NAME,A.COMMENTS,A.PAGE_URL,A.STATE,A.STATE_DATE " + "FROM BPM_BUTTON A "
                // +" left join BPM_FORM B on A.FORM_ID = B.FORM_ID"
                + " WHERE A.STATE='A' "
                + "[AND A.BTN_NAME LIKE :BTN_NAME]  AND A.BTN_ID NOT IN (SELECT BTN_ID FROM BPM_TASK_BUTTON  WHERE TEMPLATE_ID = :TEMPLATE_ID)";
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "TEMPLATE_ID", bo.getLong("TEMPLATE_ID"));
        if (StringUtil.isNotEmpty(bo.getString("BTN_NAME"))) {
            ParamMapHelper.setValue(pm, "BTN_NAME", "%" + bo.getString("BTN_NAME") + "%");
        }

        // ParamMapHelper.setValue(pm, "FORM_ID", bo.getLong("FORM_ID"));
        String countSql = new StringBuilder().append("select count(*) cnt from (").append(sql).append(") a").toString();
        Long count = this.query(countSql, pm, null, null, new RowSetMapper<Long>() {
            public Long mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException, BaseAppException {
                long count = 0;
                if (rs.next()) {
                    count = op.getLong(rs, 1);
                }
                return count;
            }
        });

        bo.set("BFM_USER_LIST_COUNT", count);
        if (count == 0) {
            return null;
        }
        return (List<DynamicDict>) this.query(sql, pm, condition.getRowSetFormatter(), null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException, BaseAppException {
                List<DynamicDict> btnList = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    int flag = 1;
                    DynamicDict temp = new DynamicDict();
                    temp.set("BTN_ID", op.getLong(rs, "BTN_ID"));
                    temp.set("PAGE_URL", op.getString(rs, "PAGE_URL"));
                    temp.set("BTN_NAME", op.getString(rs, "BTN_NAME"));
                    temp.set("COMMENTS", op.getString(rs, "COMMENTS"));
                    btnList.add(temp);
                }
                return btnList;
            }
        });
    }

    /**
     * 删除任务模板按钮<br>
     *
     * @param taskId
     * @throws BaseAppException <br>
     */
    public int deleteTaskBtnByTaskId(Long taskId) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("DELETE FROM BPM_TASK_BUTTON WHERE TEMPLATE_ID = ?");
        ParamArray pa = new ParamArray();
        pa.set("", taskId);
        return executeUpdate(sqlStr.toString(), pa);
    }

    /**
     * 插入流程模板按钮
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int[] insertTaskBtns(DynamicDict dict) throws BaseAppException {
        String sqlStr = "INSERT INTO BPM_TASK_BUTTON (BTN_ID,TEMPLATE_ID,ALIAS_NAME,BTN_SEQ_NBR) "
                + " VALUES (:BTN_ID,:TEMPLATE_ID,:ALIAS_NAME,:BTN_SEQ_NBR)";
        ArrayList<DynamicDict> buttonList = (ArrayList<DynamicDict>) dict.getList("BUTTON_LIST");
        if (buttonList == null || buttonList.size() == 0) {
            return null;
        }
        Long templateId = dict.getLong("TEMPLATE_ID");
        ParamObject[] paraList = ParamObject.newParamObjectList(4, buttonList.size());
        DynamicDict temp = null;
        for (int i = 0; i < buttonList.size(); i++) {
            temp = (DynamicDict) buttonList.get(i);
            paraList[0].setBatchElement("BTN_ID", buttonList.get(i).getLong("BTN_ID"), i);
            paraList[1].setBatchElement("TEMPLATE_ID", templateId, i);
            paraList[2].setBatchElement("ALIAS_NAME", buttonList.get(i).getString("ALIAS_NAME"), i);
            paraList[3].setBatchElement("BTN_SEQ_NBR", buttonList.get(i).getLong("BTN_SEQ_NBR"), i);
        }
        return executeBatch(sqlStr, ParamMap.wrap(paraList));
    }

    @Override
    public int delTaskTempEvent(Long templateId) throws BaseAppException {

        String delSql = "DELETE FROM BPM_TASK_EVENT WHERE TEMPLATE_ID = ?";

        return this.updateObject(delSql, templateId);
    }

    @Override
    public void addTaskTempEvent(List<TaskEventDto> events) throws BaseAppException {

        String insertsql = "INSERT INTO BPM_TASK_EVENT(TEMPLATE_ID, SERVICE_NAME, EVENT_TYPE, COMMENTS)"
                + " VALUES (:TEMPLATE_ID, :SERVICE_NAME, :EVENT_TYPE, :COMMENTS)";

        int j = 0;
        ParamObject[] paraList = ParamObject.newParamObjectList(4, events.size());
        for (j = 0; j < events.size(); j++) {
            TaskEventDto event = events.get(j);

            paraList[0].setBatchElement("TEMPLATE_ID", event.getTemplateId(), j);
            paraList[1].setBatchElement("SERVICE_NAME", event.getServiceName(), j);
            paraList[2].setBatchElement("EVENT_TYPE", event.getEventType(), j);
            paraList[3].setBatchElement("COMMENTS", event.getComments(), j);
        }

        JdbcTemplate.executeBatch(JdbcUtil4SGD.getDefaultDbService(), insertsql, ParamMap.wrap(paraList), true);
    }

    @Override
    public List<TaskEventDto> qryTaskTempEvent(Long templateId) throws BaseAppException {

        String insertsql = "SELECT TEMPLATE_ID, SERVICE_NAME, EVENT_TYPE, COMMENTS FROM BPM_TASK_EVENT WHERE TEMPLATE_ID = ?";

        return this.selectList(insertsql, TaskEventDto.class, templateId);
    }

    @Override
    public List<TaskBackReasonInfo> qryTaskBackReason(Long templateId) throws BaseAppException {

        String selSql = "SELECT A.BACK_REASON_ID, A.TASK_TEMP_ID, A.REASON_CODE, A.REASON_NAME, A.REASON_COMMENTS"
                + " FROM BPM_BACK_REASON A WHERE A.TASK_TEMP_ID = ? AND A.STATE = 'A'";

        return this.selectList(selSql, TaskBackReasonInfo.class, templateId);
    }

    @Override
    public void addTaskBackReason(TaskBackReasonInfo taskBackReasonInfo) throws BaseAppException {

        Long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BPM_BACK_REASON", "BACK_REASON_ID");
        taskBackReasonInfo.setBackReasonId(id);

        taskBackReasonInfo.setState("A");
        taskBackReasonInfo.setStateDate(DateUtil.GetDBDateTime());

        String insertSql = "INSERT INTO BPM_BACK_REASON (BACK_REASON_ID, TASK_TEMP_ID, REASON_CODE, REASON_NAME, STATE, STATE_DATE, REASON_COMMENTS)"
                + " VALUES (:BACK_REASON_ID, :TASK_TEMP_ID, :REASON_CODE, :REASON_NAME, :STATE, :STATE_DATE, :REASON_COMMENTS)";

        this.updateObject(insertSql, taskBackReasonInfo);
    }

    @Override
    public void modTaskBackReason(TaskBackReasonInfo taskBackReasonInfo) throws BaseAppException {

        String updateSql = "UPDATE BPM_BACK_REASON SET REASON_CODE = :REASON_CODE, REASON_NAME = :REASON_NAME, REASON_COMMENTS = :REASON_COMMENTS"
                + " WHERE BACK_REASON_ID = :BACK_REASON_ID";

        this.updateObject(updateSql, taskBackReasonInfo);
    }

    @Override
    public void delTaskBackReason(long backReasonId) throws BaseAppException {

        String delSql = "UPDATE BPM_BACK_REASON SET STATE = 'X', STATE_DATE = ? WHERE BACK_REASON_ID = ?";

        this.updateObject(delSql, DateUtil.GetDBDateTime(), backReasonId);
    }

    //表列名对应不上
    @Override
    public void addServiceVar(ServiceParamDto serviceVarDto) throws BaseAppException {
        String insertSql = "INSERT INTO BPM_SERV_TASK_PARA (VAR_NAME,VAR_CODE,TEMPLATE_ID,SCOPE,VAR_TYPE,DEFAULT_VALUE,COMMENTS) "
                + "VALUES(:VAR_NAME,:VAR_CODE,:TEMPLATE_ID,:SCOPE,:VAR_TYPE,:DEFAULT_VALUE,:COMMENTS)";

        this.updateObject(insertSql, serviceVarDto);

    }

    @Override
    public void modServiceVar(ServiceParamDto serviceVarDto) throws BaseAppException {
        String updateSql = "UPDATE BPM_SERV_TASK_PARA SET VAR_NAME = :VAR_NAME,SCOPE=:SCOPE,"
                + "VAR_TYPE=:VAR_TYPE,DEFAULT_VALUE=:DEFAULT_VALUE,COMMENTS=:COMMENTS WHERE VAR_CODE = :VAR_CODE AND TEMPLATE_ID = :TEMPLATE_ID ";

        this.updateObject(updateSql, serviceVarDto);

    }

    @Override
    public void delServiceVar(ServiceParamDto serviceVarDto) throws BaseAppException {
        String delSql = "DELETE FROM  BPM_SERV_TASK_PARA WHERE VAR_CODE = :VAR_CODE AND TEMPLATE_ID = :TEMPLATE_ID";

        this.updateObject(delSql, serviceVarDto);

    }

    @Override
    public List<ServiceParamDto> qrySerParamByTemplatId(Long templateId) throws BaseAppException {
        String sql = "SELECT * FROM BPM_SERV_TASK_PARA WHERE TEMPLATE_ID = ?";

        return this.selectList(sql, ServiceParamDto.class, templateId);
    }
}
