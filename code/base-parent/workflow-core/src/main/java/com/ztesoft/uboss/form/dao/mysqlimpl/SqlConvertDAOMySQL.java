package com.ztesoft.uboss.form.dao.mysqlimpl;

import com.ztesoft.uboss.form.dao.abstractimpl.SqlConvertDAO;

public class SqlConvertDAOMySQL extends SqlConvertDAO {

	public String genFieldExistSql(String sTable, String sFieldName) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");
		sb.append(" select table_name,column_name,column_default,is_nullable,udt_name as column_Data_Type,character_maximum_length as character_length,numeric_precision  ");
		sb.append(" from information_schema.COLUMNS  ");
		sb.append(" where lower(TABLE_NAME)='" + sTable.toLowerCase() + "'");
		sb.append(" and lower(column_name)='" + sFieldName.toLowerCase()
				+ "'");

		String sql = sb.toString();
		return sql;
	}

	public String genFieldFormatSql(String sTable, String sFieldName,
			String sFieldType)  {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");

	
	      String sType = sFieldType;
	      if (sFieldType.equals("int")) {
	      	sType = "numeric";
	      } else if (sFieldType.equals("ntext")) {
	      	sType = "varchar";
	      } else if (sFieldType.equals("numeric")) {
	      	sType = "numeric";
	      } else if (sFieldType.equals("nvarchar")) {
	      	sType = "varchar";
	      }      	
	      
	   
	      
	      sb.append(" select table_name,column_name,column_default,is_nullable,udt_name as column_Data_Type,character_maximum_length as character_length,numeric_precision  ");
			sb.append(" from information_schema.COLUMNS  ");
			sb.append(" where lower(TABLE_NAME)='" + sTable.toLowerCase() + "'");
			sb.append(" and lower(column_name)='" + sFieldName.toLowerCase()+ "'");
			sb.append(" and lower(udt_name) like '%" +sType+ "%'");
	    String sql = sb.toString();
	    return sql;
	}

	public String getFuncLen()  {
		// TODO Auto-generated method stub
		
	  	return "LEN";
	}

	public String getFuncIsNull()  {
		// TODO Auto-generated method stub
		
	  	return "IsNull"; 
	}

	public String getFuncSubString()  {
		// TODO Auto-generated method stub
		
	  	return "subString"; 
	}

	public String getVarchar()  {
		// TODO Auto-generated method stub
	  	return "varchar";
	}

	public String getText()  {
		// TODO Auto-generated method stub
	  	return "text";
	}
	public String getNumber() {
		// TODO Auto-generated method stub
		return "numeric";
	}
	
	public String getDate() {
		// TODO Auto-generated method stub
		return "Date";
	}

	public String getTime() {
		// TODO Auto-generated method stub
		return "Date";
	}
	public String getBlob() {
		// TODO Auto-generated method stub
		return "blob";
	}

	public String getClob() {
		// TODO Auto-generated method stub
		return "clob";
	}

	public String getInt() {
		// TODO Auto-generated method stub
		return "numeric";
	}
}
