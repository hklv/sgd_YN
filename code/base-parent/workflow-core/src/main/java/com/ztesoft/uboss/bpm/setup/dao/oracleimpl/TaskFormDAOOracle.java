package com.ztesoft.uboss.bpm.setup.dao.oracleimpl;

import com.ztesoft.uboss.bpm.setup.dao.abstractimpl.TaskFormDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;


public class TaskFormDAOOracle extends TaskFormDAO {


	public int delTaskForm(long formId) throws BaseAppException {
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("UPDATE BPM_FORM SET STATE = 'X', STATE_DATE = SYSDATE WHERE FORM_ID = ?");
		
		ParamArray pa = new ParamArray();
		pa.set("", formId);
		
		return this.executeUpdate(sqlBuffer.toString(), pa);
	}

}
