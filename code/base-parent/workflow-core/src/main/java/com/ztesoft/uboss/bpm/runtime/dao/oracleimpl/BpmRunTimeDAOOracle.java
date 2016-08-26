package com.ztesoft.uboss.bpm.runtime.dao.oracleimpl;

import java.util.List;

import com.ztesoft.uboss.bpm.runtime.beans.TaskListInfo;
import com.ztesoft.uboss.bpm.runtime.dao.abstractimpl.BpmRunTimeDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;


public class BpmRunTimeDAOOracle extends BpmRunTimeDAO {

	public List<TaskListInfo> qryTaskList(String processInstanceId)
			throws BaseAppException {

		String sql = "SELECT A.TASK_LIST_ID, A.TASK_NAME TASK_LIST_NAME,A.TASK_RESULT ,C.FORM_ID, A.TASK_TYPE, A.STATE TASK_STATE,"
				+ " A.EXECUTION_ID, A.USER_ID, A.CREATE_DATE, A.END_TIME, B.USER_NAME ,D.FORM_TYPE,D.page_url,D.dyn_form_id,e.holder_no"
				+ " FROM BPM_TASK_LIST A LEFT JOIN BFM_USER B "
				+ " ON A.USER_ID = B.USER_ID "
				+ "  left join BPM_TASK_TEMPLATE C on  A.TEMPLATE_ID = C.TEMPLATE_ID " +
				" LEFT JOIN bpm_form d on c.form_id=d.form_id,BPM_TASK_HOLDER e"
				+ " WHERE A.PROC_INST_ID = ? and a.HOLDER_ID=e.HOLDER_ID  ORDER BY to_number(A.ACT_INST_ID),A.CREATE_DATE";

		return this.selectList(sql, TaskListInfo.class, processInstanceId);
	}
}
