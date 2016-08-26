package com.ztesoft.uboss.form.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.form.dao.ISqlConvertDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlConvertDAO extends BusiBaseDAO implements ISqlConvertDAO {

	public String format(String sql)  {
		// TODO Auto-generated method stub
		return sql;
	}

	public String genSql(String sTable, String sCond)  {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select * from ");
	    sb.append(sTable);
	    if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
	    	 if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 
	    	 }
	    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" and ");
	    	 }
	    }
	    sb.append(sCond);
	    String sql = sb.toString();
	    return sql;
	}

	public String genSql(String sTable, String sField, String sCond)
			 {
		// TODO Auto-generated method stub
		
	    StringBuffer sb = new StringBuffer(" select ");
	    sb.append(sField);
	    sb.append(" from ");
	    sb.append(sTable);
	    if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
             if(sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 sb.append(" ");
	    	 }
	    	 else if( sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" ");
	    	 }else{
	    		 sb.append(" and ");
	    	 }
             sb.append(sCond);
	    }
	    //return SzSqlConvert.format( sb.toString() );
	    
	    String sql = sb.toString();
	    
	    return sql;
	}

	public String genSql(int iCount, String sTable, String sField, String sCond)
			 {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select ");
	    
	    sb.append(sField);
	    sb.append(" from ");
	    sb.append(sTable);
	    if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
            if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 
	    	 }
	    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" and ");
	    	 }
	    }
	    
	   
	    //return SzSqlConvert.format( sb.toString() );
	    String sql = sb.toString();
	    return sql;
	}

	public String genSql(String sTable, String sField, String sCond,
			String sSortField, int iOrder)  {
		// TODO Auto-generated method stub
		 StringBuffer sb = new StringBuffer(" select  ");
		    sb.append(sField);
		    sb.append(" from ");
		    sb.append(sTable);
		    if(sCond!=null && sCond.trim().length()>0){
		    	 sb.append(" where 1=1  ");
	             if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
		    		 
		    	 }
		    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
		    		 sb.append(" and ");
		    	 }
		    }
		    sb.append(" order by ");
		    sb.append(sSortField);
		    if ( iOrder == 0 ) {
		      sb.append(" asc");
		    } else {
		      sb.append(" desc");
		    }
		    //return SzSqlConvert.format( sb.toString() );
		    String sql = sb.toString();
		    return sql;
	}

	public String genSql(int iCount, String sTable, String sField,
			String sCond, String sSortField, int iOrder)
			 {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select ");

	    sb.append(sField);
	    sb.append(" from ");
	    sb.append(sTable);
	    if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
            if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 
	    	 }
	    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" and ");
	    	 }
	    }
	    

	    sb.append(" order by ");
	    sb.append(sSortField);
	    if ( iOrder == 0 ) {
	      sb.append(" asc");
	    } else {
	      sb.append(" desc");
	    }
	  
	    //return SzSqlConvert.format( sb.toString() );
	    String sql = sb.toString();
	    return sql;
	}

	public String genSql(int iCount, String sTable, String sField,
			String sCond, String sSortField, int iOrder, int iPageSize)
			 {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select ");

		sb.append(sField);
		sb.append(" from ");
		sb.append(sTable);
		if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
            if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 
	    	 }
	    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" and ");
	    	 }
	    }

		sb.append(" order by ");
		sb.append(sSortField);
		if (iOrder == 0) {
			sb.append(" asc");
		} else {
			sb.append(" desc");
		}
		
		// return SzSqlConvert.format( sb.toString() );
		String sql = sb.toString();
		return sql;
	}

	public String genCountSql(String sTable, String sCond)
			 {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select count(*) from ");
	    sb.append(sTable);
	    if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
            if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 
	    	 }
	    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" and ");
	    	 }
	    }
	    //return SzSqlConvert.format( sb.toString() );
	    String sql = sb.toString();
	    return sql;
	}

	public String genCountSql(String sTable, String sField, String sCond)
			 {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select ");
	    sb.append("count(");
	    sb.append(sField);
	    sb.append(")");
	    sb.append(" from ");
	    sb.append(sTable);
	    if(sCond!=null && sCond.trim().length()>0){
	    	 sb.append(" where 1=1  ");
            if(!sCond.trim().substring(0,6).toLowerCase().startsWith("order ")){
	    		 
	    	 }
	    	 else if( !sCond.trim().substring(0,3).toLowerCase().startsWith("and")){
	    		 sb.append(" and ");
	    	 }
	    }
	    String sql = sb.toString();
	    //System.out.println(sql);
	    return sql;
	}

	public String genTableIsExistSql(String sTable)  {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");
	    
	    String sql = sb.toString();
	    return sql;
	}

	public String genFieldExistSql(String sTable, String sFieldName)
			 {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");
	    
	    String sql = sb.toString();
	    return sql;
	}

	public String genFieldFormatSql(String sTable, String sFieldName,
			String sFieldType)  {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");

	    
	      String sType = sFieldType;
	      if (sFieldType.equals("int")) {
	      	sType = "int";
	      } else if (sFieldType.equals("ntext")) {
	      	sType = "text";
	      } else if (sFieldType.equals("numeric")) {
	      	sType = "double";
	      } else if (sFieldType.equals("nvarchar")) {
	      	sType = "varchar";
	      }      	
	      sb.append(" show columns FROM ");
	      sb.append(sTable);
	      sb.append(" WHERE Field='");      
	      sb.append(sFieldName);
	      sb.append("' and Type like'");
	      sb.append(sType);    	
	      sb.append("%'");
	    
	    String sql = sb.toString();
	    return sql;
	}

	public String getFuncLen()  {
		// TODO Auto-generated method stub
		String sFunc = "LEN";
	  	
	  	return sFunc;
	}

	public String getFuncIsNull()  {
		// TODO Auto-generated method stub
		String sFunc = "IsNull";
	  	
	  	return sFunc; 
	}

	public String getFuncSubString()  {
		// TODO Auto-generated method stub
		String sFunc = "subString";
	  	
	  	return sFunc; 
	}

	public String getVarchar()  {
		// TODO Auto-generated method stub
		String s = "nvarchar";
	  	
	  	return s;
	}

	public String getText()  {
		// TODO Auto-generated method stub
		String s = "text";
	  	
	  	return s;
	}
	
	public String getSaveSql(String tableName,List fieldList){
		StringBuffer sql=new StringBuffer();
		
		return sql.toString();
	}
	public ArrayList getArrayListBySql(String sql) throws BaseAppException {
		// TODO Auto-generated method stub
		if(sql==null || sql.trim().length()<=10)return null;
		
		return (ArrayList<Map>) this.query(sql, null, null, new RowSetMapper<List<Map>>() {
            public List<Map> mapRows(RowSetOperator op, ResultSet rs, int colNum,
									 Object para) throws SQLException, BaseAppException {
            	
            	List<Map> list = new ArrayList<Map>();
                
            	String colNames[] = op.getColNames();
				while (rs.next()) {
					Map temp = new HashMap();
					for (int i = 0; i < colNames.length; i++) {
						temp.put(colNames[i].toLowerCase(),
								op.getValue(rs, colNames[i]));
					}
					list.add(temp);
				}
                return list;
            }
        });
		
	}

	public String getNumber() {
		// TODO Auto-generated method stub
		return "NUMBER";
	}

	public String getDate() {
		// TODO Auto-generated method stub
		return "Date";
	}

	public String getTime() {
		// TODO Auto-generated method stub
		return "Date";
	}

	public String getChar() {
		// TODO Auto-generated method stub
		return "char";
	}

	public String getFloat() {
		// TODO Auto-generated method stub
		return "float";
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
		return "number";
	}

	public String getToDate(String dStr) {
		// TODO Auto-generated method stub
		
		return "to_date('"+dStr+"','yyyy-mm-dd')";
	}

	public String getToTime(String dStr) {
		// TODO Auto-generated method stub
		return "to_date('"+dStr+"','yyyy-mm-dd HH:MI:SS')";
	}
}
