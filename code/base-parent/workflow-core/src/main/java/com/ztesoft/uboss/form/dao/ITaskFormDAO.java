package com.ztesoft.uboss.form.dao;

import com.ztesoft.uboss.form.model.TaskFormTestDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;

/**
 * 环节表单的DAO接口
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/7/14
 */
public interface ITaskFormDAO {
    TaskFormTestDto qryTaskFromTestByHolderNo(String holderNo) throws BaseAppException;

    void saveTaskFormTest(TaskFormTestDto taskFormTestDto) throws BaseAppException;
}
