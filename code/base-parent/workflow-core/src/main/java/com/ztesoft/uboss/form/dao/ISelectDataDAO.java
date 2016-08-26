package com.ztesoft.uboss.form.dao;

import com.ztesoft.uboss.form.model.DynSelectDataDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;

public interface ISelectDataDAO {
	
	void addSelectData(DynamicDict dict) throws BaseAppException;
	
	void addSelectData(DynSelectDataDto dynSelectDataDto) throws BaseAppException;
	
	void updateSelectData(DynamicDict dict) throws BaseAppException;
	
	void updateSelectData(DynSelectDataDto dynSelectDataDto) throws BaseAppException;
	
	void updateSelectDataState(DynamicDict dict) throws BaseAppException;
	
	DynSelectDataDto querySelectDataDetail(DynamicDict dict) throws BaseAppException;
	
	List<DynamicDict> querySelectDataList(DynamicDict dict) throws BaseAppException;
	
	List<DynSelectDataDto> querySelectDataAllList(DynamicDict dict) throws BaseAppException;
	
	boolean isHasSelectData(DynamicDict dict) throws BaseAppException;
	
}
