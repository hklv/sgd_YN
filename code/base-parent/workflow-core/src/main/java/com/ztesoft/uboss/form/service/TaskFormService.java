package com.ztesoft.uboss.form.service;

import com.ztesoft.uboss.form.bll.TaskFormManager;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import utils.UbossActionSupport;

/**
 * 任务环节表单
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/7/14
 */
public class TaskFormService extends UbossActionSupport {

    public int addTaskForm(DynamicDict dict) throws BaseAppException {
        new TaskFormManager().addTaskForm(dict);
        return 0;
    }

    public int qryTaskForm(DynamicDict dict) throws BaseAppException {
        new TaskFormManager().qryTaskFormByHolderNo(dict);
        return 0;
    }
}
