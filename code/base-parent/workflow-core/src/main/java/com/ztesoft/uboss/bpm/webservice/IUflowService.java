package com.ztesoft.uboss.bpm.webservice;

import com.ztesoft.uboss.bpm.webservice.model.FlowOperationReq;
import com.ztesoft.uboss.bpm.webservice.model.FlowOperationResp;
import com.ztesoft.uboss.bpm.webservice.model.TaskOperationReq;
import com.ztesoft.uboss.bpm.webservice.model.TaskOperationResp;

public interface IUflowService {

    /**
     * 流程操作
     *
     * @param req
     * @return resp
     */
    FlowOperationResp operateFlow(FlowOperationReq req);

    /**
     * 任务操作
     *
     * @param req
     * @return resp
     */
    TaskOperationResp operateTask(TaskOperationReq req);
}
