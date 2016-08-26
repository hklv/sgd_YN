package com.ztesoft.uboss.form.dao;

import com.ztesoft.zsmart.core.exception.BaseAppException;

import java.util.ArrayList;


public interface ISqlConvertDAO {

	// 通过这个方法，可以将sql格式化为各种数据库的语法，比如oracle, mysql, sqlserver, access等。
	String format(String sql);

	/**
	 * 根据条件组合选择的sql
	 * 
	 * @param sTable
	 * @param sCond
	 * @return
	 */
	String genSql(String sTable, String sCond);

	/**
	 * 根据条件组合选择的sql
	 * 
	 * @param sTable
	 * @param sField
	 * @param sCond
	 * @return
	 */
	String genSql(String sTable, String sField, String sCond);

	/**
	 * 根据条件组合选择的sql
	 * 
	 * @param iCount
	 *            :要获取的记录数量
	 * @param sTable
	 * @param sField
	 * @param sCond
	 * @return
	 */
	String genSql(int iCount, String sTable, String sField, String sCond);

	/**
	 * 根据条件组合选择的sql
	 * 
	 * @param sTable
	 * @param sField
	 * @param sCond
	 * @param sSortField
	 * @param iOrder
	 *            ：0 asc，1 desc
	 * @return
	 */
	String genSql(String sTable, String sField, String sCond,
				  String sSortField, int iOrder);

	/**
	 * 根据条件组合选择的sql
	 * 
	 * @param iCount
	 *            :要获取的记录数量
	 * @param sTable
	 * @param sField
	 * @param sCond
	 * @param sSortField
	 * @param iOrder
	 *            ：0 asc，1 desc
	 * @return
	 */
	String genSql(int iCount, String sTable, String sField,
				  String sCond, String sSortField, int iOrder);

	String genSql(int iCount, String sTable, String sField,
				  String sCond, String sSortField, int iOrder, int iPageSize);

	/**
	 * 根据条件组合计算数量的sql
	 * 
	 * @param sTable
	 * @param sCond
	 * @return
	 */
	String genCountSql(String sTable, String sCond);

	String genCountSql(String sTable, String sField, String sCond);

	String genTableIsExistSql(String sTable);

	String genFieldExistSql(String sTable, String sFieldName);

	String genFieldFormatSql(String sTable, String sFieldName,
							 String sFieldType);

	String getFuncLen();

	String getFuncIsNull();

	String getFuncSubString();

	String getVarchar();

	String getText();
	
	String getNumber();
	
	String getDate();
	
	String getTime();
	
	String getChar();
	
	String getFloat();
	
	String getBlob();
	
	String getClob();
	
	String getInt();
	
	String getToDate(String dStr);
	
	String getToTime(String dStr);
	
	ArrayList getArrayListBySql(String sql)throws BaseAppException;
	

}
