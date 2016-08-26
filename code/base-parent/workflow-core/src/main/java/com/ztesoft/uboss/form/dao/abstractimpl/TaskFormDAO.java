package com.ztesoft.uboss.form.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.form.dao.ITaskFormDAO;
import com.ztesoft.uboss.form.model.TaskFormTestDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.utils.SeqUtil;

/**
 * TaskFormDAO
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/7/14
 */
public class TaskFormDAO extends BusiBaseDAO implements ITaskFormDAO {
    @Override
    public TaskFormTestDto qryTaskFromTestByHolderNo(String holderNo) throws BaseAppException {
        ParamMap pm = new ParamMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * ");
        sql.append("FROM TFM_TASK_FORM_TEST where HOLDER_NO = :HOLDER_NO");
        pm.set("HOLDER_NO", holderNo);
        return this.selectObject(sql.toString(), TaskFormTestDto.class, pm);
    }

    @Override
    public void saveTaskFormTest(TaskFormTestDto taskFormTestDto) throws BaseAppException {
        long formId = SeqUtil.getBackServiceDBUtil().getMaxValue("TFM_TASK_FORM_TEST", "id");
        taskFormTestDto.setId(formId);
        StringBuilder sql = new StringBuilder();
        sql.append("insert into TFM_TASK_FORM_TEST (");
        sql.append("ID, IS_MATCH_NATIONAL_STANDARD, IS_APPROVAL, HOLDER_NO)");
        sql.append(" values (:ID,:IS_MATCH_NATIONAL_STANDARD,:IS_APPROVAL,:HOLDER_NO)");
        this.updateObject(sql.toString(), taskFormTestDto);
    }
}
