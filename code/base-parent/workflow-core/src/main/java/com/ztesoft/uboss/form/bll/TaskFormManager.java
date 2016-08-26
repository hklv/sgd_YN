package com.ztesoft.uboss.form.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.form.dao.ITaskFormDAO;
import com.ztesoft.uboss.form.dao.abstractimpl.TaskFormDAO;
import com.ztesoft.uboss.form.model.TaskFormTestDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;

/**
 * 环节表单服务
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/7/14
 */
public class TaskFormManager {
    public ITaskFormDAO getTaskFormDAO() throws BaseAppException {
        return SgdDaoFactory.getDaoImpl(TaskFormDAO.class);
    }

    public TaskFormTestDto qryTaskFormByHolderNo(DynamicDict dict) throws BaseAppException {
        return getTaskFormDAO().qryTaskFromTestByHolderNo((String) dict.get("holderNo"));
    }

    public void addTaskForm(DynamicDict dict) throws BaseAppException {
        getTaskFormDAO().saveTaskFormTest((TaskFormTestDto) BoHelper.boToDto(dict, TaskFormTestDto.class));
    }
}
