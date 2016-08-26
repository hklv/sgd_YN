package com.ztesoft.uboss.bpm.setup.services;

import com.ztesoft.uboss.bpm.setup.bll.ButtonRepoManager;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import utils.UbossActionSupport;


public class ButtonRepoService extends UbossActionSupport {

    /**
     * 查询按钮库列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryButtonList(DynamicDict bo) throws BaseAppException {
        ButtonRepoManager manager = new ButtonRepoManager();
        manager.qryButtonList(bo);
    }

    /**
     * 查询按钮库列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryButtonListByTask(DynamicDict bo) throws BaseAppException {
        ButtonRepoManager manager = new ButtonRepoManager();
        manager.qryButtonListByTask(bo);
    }

    /**
     * 新增按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addButton(DynamicDict bo) throws BaseAppException {
        ButtonRepoManager manager = new ButtonRepoManager();
        manager.addButton(bo);
    }

    /**
     * 修改按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void updateButton(DynamicDict bo) throws BaseAppException {
        ButtonRepoManager manager = new ButtonRepoManager();
        manager.updateButton(bo);
    }

    /**
     * 删除按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void delButton(DynamicDict bo) throws BaseAppException {
        ButtonRepoManager manager = new ButtonRepoManager();
        manager.delButton(bo);
    }
}
