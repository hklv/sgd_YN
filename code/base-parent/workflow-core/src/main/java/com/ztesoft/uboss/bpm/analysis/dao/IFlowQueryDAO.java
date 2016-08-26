package com.ztesoft.uboss.bpm.analysis.dao;

import com.ztesoft.uboss.bpm.analysis.model.DelayedFlowQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.DelayedFlows;
import com.ztesoft.zsmart.core.exception.BaseAppException;

import java.util.List;

public interface IFlowQueryDAO {
	
	List<DelayedFlows> selectDelayedFlows(DelayedFlowQryCondition qryCondition) throws BaseAppException;
	
	
}
