package com.ztesoft.uboss.bpm.test.dao.abstractimpl;

import com.ztesoft.uboss.bpm.test.dao.IStaticFormDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.BaseDAO;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.sql.ResultSet;
import java.sql.SQLException;


public class StaticFormDAO extends BaseDAO implements IStaticFormDAO {

	public int createForm(DynamicDict dict) throws BaseAppException {
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("INSERT INTO SFORM_LOAN (LOANID, PERSON, MONEY,MONEY_FOR,FLOWID,CREATE_DATE)" +
				" VALUES (?, ?, ?,?,?,?)");
		
		ParamArray pa = new ParamArray();
		pa.set("", dict.getLong("LOAN_ID"));
		pa.set("", dict.getString("PERSON"));
		pa.set("", dict.getString("MONEY"));
		
		pa.set("", dict.getString("MONEY_FOR"));
		pa.set("", dict.getString("FLOW_ID"));
		pa.set("", dict.getDate("CREATE_DATE"));
		
		return this.executeUpdate(sqlBuffer.toString(), pa);
	}


	@SuppressWarnings("unchecked")
	public DynamicDict qryForm(DynamicDict dict) throws BaseAppException{
		 StringBuffer sql = new StringBuffer();
	        sql.append("SELECT LOANID, PERSON, MONEY,MONEY_FOR,FLOWID,CREATE_DATE FROM SFORM_LOAN WHERE FLOWID = ?");
	        ParamArray pa = new ParamArray();
	        pa.set("", dict.getString("FLOWID"));
	        
	        return (DynamicDict) this.query(sql.toString(), pa, null, new RowSetMapper() {
	            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
									  Object para) throws SQLException, BaseAppException {
	                DynamicDict temp = null;
	                if (rs.next()) {
	                    temp = new DynamicDict();
	                    temp.set("LOANID", rs.getObject("LOANID"));
	                    temp.set("PERSON", rs.getObject("PERSON"));
	                    temp.set("MONEY", rs.getObject("MONEY"));
	                    temp.set("MONEY_FOR", rs.getObject("MONEY_FOR"));
	                    temp.set("FLOWID", rs.getObject("FLOWID"));
	                    temp.set("CREATE_DATE", rs.getObject("CREATE_DATE"));
	                    
	                }
	                return temp;
	            }
	        });
	}


	public int createManagerForm(DynamicDict dict) throws BaseAppException {
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("INSERT INTO SFORM_LOAN_MANAGER (ID, PASS, FLOWID,CREATE_DATE)" +
				" VALUES (?, ?, ?,?)");
		
		ParamArray pa = new ParamArray();
		pa.set("", dict.getLong("ID"));
		pa.set("", dict.getString("PASS"));
		pa.set("", dict.getString("FLOW_ID"));
		pa.set("", dict.getDate("CREATE_DATE"));
		
		return this.executeUpdate(sqlBuffer.toString(), pa);
	}

}
