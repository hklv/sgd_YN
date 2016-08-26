package com.ztesoft.uboss.bpm.setup.dao;

import com.ztesoft.uboss.bpm.setup.model.BpmFormDto;
import com.ztesoft.uboss.bpm.setup.model.BpmFormQueryDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;

import java.util.List;

public interface ITaskFormDAO {
    int addTaskForm(BpmFormDto bpmFormDto) throws BaseAppException;

    int delTaskForm(long formId) throws BaseAppException;

    int modTaskForm(BpmFormDto bpmFormDto) throws BaseAppException;

    long qryTaskFormListCount(BpmFormQueryDto bpmFormDto) throws BaseAppException;

    List<BpmFormDto> qryTaskFormList(BpmFormQueryDto bpmFormDto) throws BaseAppException;

    BpmFormDto qryLinkFormDetail(long formId) throws BaseAppException;
}
