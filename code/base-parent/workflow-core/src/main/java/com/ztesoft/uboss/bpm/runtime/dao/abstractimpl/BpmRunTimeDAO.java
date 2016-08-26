package com.ztesoft.uboss.bpm.runtime.dao.abstractimpl;

import java.util.List;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.runtime.beans.ProcessRunVar;
import com.ztesoft.uboss.bpm.runtime.beans.TaskListInfo;
import com.ztesoft.uboss.bpm.runtime.dao.IBpmRunTimeDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;


public class BpmRunTimeDAO extends BusiBaseDAO implements IBpmRunTimeDAO {

	public List<TaskListInfo> qryTaskList(String processInstanceId)
			throws BaseAppException {

		String sql = "SELECT A.TASK_LIST_ID, A.TASK_NAME TASK_LIST_NAME,A.TASK_RESULT ,C.FORM_ID, A.TASK_TYPE, A.STATE TASK_STATE,"
				+ " A.EXECUTION_ID, A.USER_ID, A.CREATE_DATE, A.END_TIME, B.USER_NAME, F.DYN_FORM_ID, F.FORM_TYPE, F.PAGE_URL "
				+ " FROM BPM_TASK_LIST A LEFT JOIN BFM_USER B "
				+ " ON A.USER_ID = B.USER_ID "
				+ "  left join BPM_TASK_TEMPLATE C on  A.TEMPLATE_ID = C.TEMPLATE_ID "
				+ "  left join bpm_form F on  C.FORM_ID = F.FORM_ID "
				+ " WHERE A.PROC_INST_ID = ?  ORDER BY A.act_inst_id,A.CREATE_DATE";

		return this.selectList(sql, TaskListInfo.class, processInstanceId);
	}

	public String qryTaskHolderState(String processInstanceId)
			throws BaseAppException {

		String sql = "SELECT HOLDER_STATE FROM BPM_TASK_HOLDER WHERE PROC_INST_ID = ?";

		return this.selectObject(sql, String.class, processInstanceId);
	}

	public String qryTaskId(String taskListId) throws BaseAppException {
		String sql = "SELECT TASK_ID FROM BPM_TASK_LIST WHERE TASK_LIST_ID = ?";

		return this.selectObject(sql, String.class, taskListId);
	}

	@Override
	public int updatePorcessState(String processInstanceId, String updateState,
			String updateReason) throws BaseAppException {

		String sql = "UPDATE BPM_TASK_HOLDER SET HOLDER_STATE = ?, DELETE_REASON = ?  WHERE PROC_INST_ID = ?";

		return this.updateObject(sql, updateState, updateReason,
				processInstanceId);
	}

	public List<ProcessRunVar> qryProcessVar(String processInstanceId,
			String varName) throws BaseAppException {
		String sql = "SELECT TYPE_ vartype,name_ varname,text_ vartext,double_ vardouble,long_ vardate FROM ACT_RU_VARIABLE T WHERE T.PROC_INST_ID_=? and NAME_=?";
		return this.selectList(sql, ProcessRunVar.class, processInstanceId,
				varName);
	}

	public List<ProcessRunVar> qryAllProcessVar(String processInstanceId)
			throws BaseAppException {
		String sql = "SELECT TYPE_ vartype,name_ varname,text_ vartext,double_ vardouble,long_ vardate FROM ACT_RU_VARIABLE T WHERE T.PROC_INST_ID_=?";
		return this.selectList(sql, ProcessRunVar.class, processInstanceId);
	}

	@Override
	public int updatePorcessStateByHolderId(String holderId,
			String updateState, String updateReason) throws BaseAppException {
		String sql = "UPDATE BPM_TASK_HOLDER SET HOLDER_STATE = ?, DELETE_REASON = ?  WHERE HOLDER_ID = ?";

		return this.updateObject(sql, updateState, updateReason,
				holderId);
	}

	@Override
	public List<TaskListInfo> qryTaskListByHolderId(String holderId)
			throws BaseAppException {
		
		String sql = "SELECT A.TASK_LIST_ID, A.TASK_NAME TASK_LIST_NAME,A.TASK_RESULT ,C.FORM_ID, A.TASK_TYPE, A.STATE TASK_STATE,"
			+ " A.EXECUTION_ID, A.USER_ID, A.CREATE_DATE, A.END_TIME, B.USER_NAME, F.DYN_FORM_ID, F.FORM_TYPE, F.PAGE_URL "
			+ " FROM BPM_TASK_LIST A LEFT JOIN BFM_USER B "
			+ " ON A.USER_ID = B.USER_ID "
			+ "  left join BPM_TASK_TEMPLATE C on  A.TEMPLATE_ID = C.TEMPLATE_ID "
			+ "  left join bpm_form F on  C.FORM_ID = F.FORM_ID "
			+ " WHERE A.HOLDER_ID = ?  ORDER BY A.ACT_INST_ID,A.CREATE_DATE";

		return this.selectList(sql, TaskListInfo.class, holderId);
		
	}

	@Override
	public String qryTaskHolderStateByHolderId(String holderId)
			throws BaseAppException {
		
		String sql = "SELECT HOLDER_STATE FROM BPM_TASK_HOLDER WHERE HOLDER_ID = ?";

		return this.selectObject(sql, String.class, holderId);
	}

	@Override
	public String qryProcInstIdByHolderId(String holderId)
			throws BaseAppException {
		
		String sql = "SELECT PROC_INST_ID FROM BPM_TASK_HOLDER WHERE HOLDER_ID = ?";

		return this.selectObject(sql, String.class, holderId);
	}

	@Override
	public List<TaskListInfo> qryTaskListByHolderNo(String holderNo)
			throws BaseAppException {
		
		String sql = "SELECT A.TASK_LIST_ID, A.TASK_NAME TASK_LIST_NAME,A.TASK_RESULT ,C.FORM_ID, A.TASK_TYPE, A.STATE TASK_STATE,"
			+ " A.EXECUTION_ID, A.USER_ID, A.CREATE_DATE, A.END_TIME, B.USER_NAME "
			+ " FROM BPM_TASK_LIST A LEFT JOIN BFM_USER B"
			+ " ON A.USER_ID = B.USER_ID"
			+ "  left join BPM_TASK_TEMPLATE C on  A.TEMPLATE_ID = C.TEMPLATE_ID, BPM_TASK_HOLDER D "
			+ " WHERE A.HOLDER_ID = D.HOLDER_ID AND D.HOLDER_NO = ? ORDER BY A.ACT_INST_ID,A.CREATE_DATE";

		return this.selectList(sql, TaskListInfo.class, holderNo);
	}

}
