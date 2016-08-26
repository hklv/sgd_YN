package com.ztesoft.uboss.form.dao.mysqlimpl;

import com.ztesoft.uboss.form.dao.abstractimpl.FormDbDAO;
import com.ztesoft.uboss.form.model.DynFormDbDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FormDbDAOMySQL extends FormDbDAO {
	
   public boolean isExistTableByName(String tableName)throws BaseAppException {
	   ParamMap pm = new ParamMap();
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder
				.append(" select count(*) cnt  ");
		sqlbuilder.append(" from information_schema.COLUMNS  ");
		sqlbuilder.append(" where lower(TABLE_NAME)=:TABLE_NAME ");
		pm.set("TABLE_NAME", tableName.trim().toLowerCase());
		
		return (boolean) this.query(sqlbuilder.toString(), pm, null, null, new RowSetMapper<Boolean>() {
            public Boolean mapRows(RowSetOperator op, ResultSet rs, int colNum,
								   Object para) throws SQLException, BaseAppException {
            	
            	long count = 0;
                
                if (rs.next()) 
                {
                	count = op.getLong(rs, 1);
                }
                if(count>0)return true;
                else return false;
            }
        });
	}
	public List<DynFormDbDto> queryTableColumnList(String tableName)
	throws BaseAppException {
		// TODO Auto-generated method stub
		ParamMap pm = new ParamMap();
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder
				.append(" select table_name,column_name,column_default,is_nullable,udt_name as column_Data_Type,character_maximum_length as character_length,numeric_precision  ");
		sqlbuilder.append(" from information_schema.COLUMNS  ");
		sqlbuilder.append(" where TABLE_NAME=:TABLE_NAME ");
		pm.set("TABLE_NAME", tableName);
		return this.selectObject(sqlbuilder.toString(), DynFormDbDto.class, pm);
	}
}
