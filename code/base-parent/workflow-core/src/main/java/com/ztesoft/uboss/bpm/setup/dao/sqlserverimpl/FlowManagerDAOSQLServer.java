package com.ztesoft.uboss.bpm.setup.dao.sqlserverimpl;

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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FlowManagerDAOSQLServer extends FlowManagerDAO {


    /**
     * 查询所有流程仿真版本
     */
    public List<ProcessDefView> qryAllSimProcessVersion(DynamicDict dynamicDict)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT A.*, B.VER, B.PROC_DEF_ID, B.EFFECTIVE_DATE, B.EXPIRED_DATE FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < getdate() AND B.EXPIRED_DATE>getdate()");
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

                    list.add(processDefView);
                }
                return list;
            }
        });
    }

    @Override
    public long qryAllSimProcessVersionCount(DynamicDict dynamicDict)
            throws BaseAppException {
        StringBuffer qrySql = new StringBuffer();

        qrySql.append("SELECT count(1) FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B");
        qrySql.append(" WHERE A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.VER_STATE = 'S' AND B.STATE = 'A' AND B.EFFECTIVE_DATE < getdate() AND B.EXPIRED_DATE>getdate()");
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

    public void modTaskState(String taskListId, String taskState, Long userId,
                             String stateReason) throws BaseAppException {

        StringBuilder builderSql = new StringBuilder();
        builderSql.append("UPDATE BPM_TASK_LIST SET END_TIME=getdate(),STATE=?, STATE_DATE=NOW(), STATE_REASON=?,USER_ID=? ");
        builderSql.append(" WHERE TASK_LIST_ID = ?");
        ParamArray pa = new ParamArray();
        pa.set("", taskState);
        pa.set("", stateReason);
        pa.set("", userId);
        pa.set("", taskListId);

        executeUpdate(builderSql.toString(), pa);
    }
}
