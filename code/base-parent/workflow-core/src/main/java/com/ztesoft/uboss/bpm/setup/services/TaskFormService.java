package com.ztesoft.uboss.bpm.setup.services;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.setup.dao.ITaskFormDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskFormDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmFormDto;
import com.ztesoft.uboss.bpm.setup.model.BpmFormQueryDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessDefView;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import utils.UbossActionSupport;

import java.util.List;


public class TaskFormService extends UbossActionSupport {

    public int qryTaskFormList(DynamicDict dict) throws BaseAppException {
        ITaskFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(TaskFormDAOMySQL.class);

        BpmFormQueryDto bpmFormDto = (BpmFormQueryDto) BoHelper.boToDto(dict, BpmFormQueryDto.class);

        long count = taskFormDAO.qryTaskFormListCount(bpmFormDto);
        if (count > 0) {
            List<BpmFormDto> taskFormList = taskFormDAO.qryTaskFormList(bpmFormDto);
            BoHelper.listDtoToBO("FORM_LIST", taskFormList, ProcessDefView.class, dict);
        }

        dict.set("FORM_LIST_COUNT", count);

        return 0;
    }

    public int addTaskForm(DynamicDict dict) throws BaseAppException {
        ITaskFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(TaskFormDAOMySQL.class);

        BpmFormDto bpmFormDto = (BpmFormDto) BoHelper.boToDto(dict, BpmFormDto.class);

        Long formId = SeqUtil.getBackServiceDBUtil().getMaxValue("BPM_FORM", "FORM_ID");
        bpmFormDto.setFormId(formId);
        bpmFormDto.setStateDate(DateUtil.getNowDate());
        taskFormDAO.addTaskForm(bpmFormDto);

        BoHelper.dtoToBO(bpmFormDto, dict);

        return 0;
    }

    public int modTaskForm(DynamicDict dict) throws BaseAppException {
        ITaskFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(TaskFormDAOMySQL.class);

        BpmFormDto bpmFormDto = (BpmFormDto) BoHelper.boToDto(dict, BpmFormDto.class);
        bpmFormDto.setStateDate(DateUtil.getNowDate());
        taskFormDAO.modTaskForm(bpmFormDto);

        BoHelper.dtoToBO(bpmFormDto, dict);

        return 0;
    }

    public int delTaskForm(DynamicDict dict) throws BaseAppException {
        ITaskFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(TaskFormDAOMySQL.class);

        taskFormDAO.delTaskForm(dict.getLong("FORM_ID"));

        return 0;
    }

    public int qryTaskLinkForm(DynamicDict dict) throws BaseAppException {
        ITaskFormDAO taskFormDAO = SgdDaoFactory.getDaoImpl(TaskFormDAOMySQL.class);
        DynamicDict detailDict = new DynamicDict();
        BpmFormDto form = taskFormDAO.qryLinkFormDetail(dict.getLong("FORM_ID"));
        dict.set("FORM_DETAIL", BoHelper.dtoToBO(form, detailDict));
        return 0;
    }
}
