package com.ztesoft.uboss.bpm.setup.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.ButtonQueryCondition;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.SeqUtil;

import java.util.List;


public class ButtonRepoManager {
    /**
     * 查询按钮库列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryButtonList(DynamicDict bo) throws BaseAppException {
        ButtonQueryCondition condition = (ButtonQueryCondition) BoHelper.boToDto(bo, ButtonQueryCondition.class);
        List<DynamicDict> buttonList = getWorkItemDao().qryButtonList(bo, condition);
        bo.set("buttonList", buttonList);
    }

    /**
     * 查询工单关联的按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryButtonListByTask(DynamicDict bo) throws BaseAppException {
        List<DynamicDict> buttonList = getWorkItemDao().qryBtnListByTemp(bo.getLong("TEMPLATE_ID"));
        bo.set("buttonList", buttonList);
    }

    /**
     * 新增按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addButton(DynamicDict bo) throws BaseAppException {
        Long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BPM_BUTTON", "BTN_ID");
        bo.set("BTN_ID", id);
        bo.set("STATE_DATE", DateUtil.getCurrentDate());
        getWorkItemDao().addButton(bo);
    }

    /**
     * 修改按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void updateButton(DynamicDict bo) throws BaseAppException {
        bo.set("STATE_DATE", DateUtil.getCurrentDate());
        getWorkItemDao().updateButton(bo);
    }

    /**
     * 删除按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void delButton(DynamicDict bo) throws BaseAppException {
        bo.set("STATE_DATE", DateUtil.getCurrentDate());
        getWorkItemDao().delButton(bo);
    }

    private ITaskTemplateDAO taskTemplateDAO = null;

    private ITaskTemplateDAO getWorkItemDao() throws BaseAppException {
        if (taskTemplateDAO == null) {
            taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        }
        return taskTemplateDAO;
    }
}
