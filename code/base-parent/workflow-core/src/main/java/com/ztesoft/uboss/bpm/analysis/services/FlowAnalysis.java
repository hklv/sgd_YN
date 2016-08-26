package com.ztesoft.uboss.bpm.analysis.services;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.analysis.dao.IFlowQueryDAO;
import com.ztesoft.uboss.bpm.analysis.dao.dmimpl.FlowQueryDAODm;
import com.ztesoft.uboss.bpm.analysis.model.DelayedFlowQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.DelayedFlows;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import utils.UbossActionSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowAnalysis extends UbossActionSupport {

    /**
     * 统计流程超时总数
     */
    public int analyseDelayedFlow(DynamicDict dict) throws BaseAppException, IllegalAccessException, InstantiationException {

        if (dict.getDate("YEAR") == null) {

            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_6);

            //默认统计当前年份
            dict.set("YEAR", DateUtil.string2SQLDate(sdf.format(DateUtil.GetDBDateTime()), DateUtil.DATE_FORMAT_6));
        }

        IFlowQueryDAO dao = SgdDaoFactory.getDaoImpl(FlowQueryDAODm.class);
        List<DelayedFlows> delayedFlows = dao.selectDelayedFlows((DelayedFlowQryCondition) BoHelper.boToDto(dict, DelayedFlowQryCondition.class));

        Map<Integer, Long> delayedFlowsMap = new HashMap<Integer, Long>();

        long total = 0;

        for (int i = 0; i < delayedFlows.size(); i++) {

            delayedFlowsMap.put(Integer.valueOf(DateUtil.date2String(delayedFlows.get(i).getAnalyseMonth(), "MM")), delayedFlows.get(i).getDelayedFlowNumber());
            total += delayedFlows.get(i).getDelayedFlowNumber();
        }

        dict.set("DELAYED_FLOWS_TOTAL", total);

        List<DynamicDict> delayNum = new ArrayList<DynamicDict>();

        for (int i = 0; i < 12; i++) {

            DynamicDict dict2 = new DynamicDict();

            if (delayedFlowsMap.get(Integer.valueOf(i)) == null) {

                dict2.set("NUM", 0);
            } else {

                dict2.set("NUM", delayedFlowsMap.get(Integer.valueOf(i)));
            }

            delayNum.add(dict2);
        }

        dict.set("DELAYED_FLOWS", delayNum);

        return 0;
    }
}
