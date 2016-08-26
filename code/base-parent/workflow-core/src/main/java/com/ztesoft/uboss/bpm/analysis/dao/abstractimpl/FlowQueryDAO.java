package com.ztesoft.uboss.bpm.analysis.dao.abstractimpl;

import java.util.List;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.analysis.dao.IFlowQueryDAO;
import com.ztesoft.uboss.bpm.analysis.model.DelayedFlowQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.DelayedFlows;
import com.ztesoft.zsmart.core.exception.BaseAppException;

public class FlowQueryDAO extends BusiBaseDAO implements IFlowQueryDAO {

	@Override
	public List<DelayedFlows> selectDelayedFlows(
			DelayedFlowQryCondition qryCondition) throws BaseAppException {
		// TODO Auto-generated method stub
		return null;
	}

}
