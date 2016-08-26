package com.ztesoft.uboss.bpm.test;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.test.dao.abstractimpl.StaticFormDAO;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import utils.UbossActionSupport;


public class StaticFormService extends UbossActionSupport {

    public int createForm(DynamicDict dict) throws BaseAppException {
        StaticFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(StaticFormDAO.class);

        Long loanId = SeqUtil.getBackServiceDBUtil().getMaxValue("SFORM_LOAN", "LOANID");
        dict.set("LOAN_ID", loanId);
        dict.set("CREATE_DATE", DateUtil.getNowDate());
        taskFormDAO.createForm(dict);

        return 0;
    }

    public int qryForm(DynamicDict dict) throws BaseAppException {
        StaticFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(StaticFormDAO.class);
        DynamicDict form = taskFormDAO.qryForm(dict);
        if (form != null) {
            dict.valueMap = form.valueMap;
        }
        return 0;
    }

    public int createManagerForm(DynamicDict dict) throws BaseAppException {
        StaticFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(StaticFormDAO.class);
        Long id = SeqUtil.getBackServiceDBUtil().getMaxValue("SFORM_LOAN_MANAGER", "ID");
        dict.set("ID", id);
        dict.set("CREATE_DATE", DateUtil.getNowDate());
        taskFormDAO.createManagerForm(dict);

        return 0;
    }
}
