package com.ztesoft.uboss.form.dao;

import com.ztesoft.uboss.form.model.DynFormDbDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;

public interface IFormDbDAO {
	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws BaseAppException
	 */
	boolean isExistTableByName(String tableName)throws BaseAppException;
	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws BaseAppException
	 */
	List <DynFormDbDto> queryTableColumnList(String tableName) throws BaseAppException;
	/**
	 * 
	 * @param dynFormDbDto
	 * @throws BaseAppException
	 */
	void createTable(DynFormDbDto dynFormDbDto) throws BaseAppException;
	/**
	 * 	
	 * @param dict
	 * @throws BaseAppException
	 */
	void createTable(DynamicDict dict) throws BaseAppException;
	/**
	 * 
	 * @param dynFormDbDto
	 * @throws BaseAppException
	 */
	void updateTable(DynFormDbDto dynFormDbDto) throws BaseAppException;
	/**
	 * 
	 * @param dict
	 * @throws BaseAppException
	 */
	void updateTable(DynamicDict dict) throws BaseAppException;

	
	void createTable(String sql)throws BaseAppException;
	
	void dropTable(String sTableName)throws BaseAppException;
	
	void modifyTable(String sql) throws BaseAppException;
	
	void addColumn(String sTableName, String sField, String sFieldType, String sFieldLen)throws BaseAppException;
	
	void addColumn(String sTableName, String sField, String sFieldType, String sFieldLen, String defaultValue)throws BaseAppException;
	
	void dropColumn(String sTableName, String sField)throws BaseAppException;
	
	

}
