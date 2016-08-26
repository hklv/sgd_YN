
package com.ztesoft.uboss.bpm.webservice.impl;

import com.ztesoft.uboss.bpm.webservice.IUflowService;
import com.ztesoft.uboss.bpm.webservice.model.FlowOperationReq;
import com.ztesoft.uboss.bpm.webservice.model.FlowOperationResp;
import com.ztesoft.uboss.bpm.webservice.model.TaskOperationReq;
import com.ztesoft.uboss.bpm.webservice.model.TaskOperationResp;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;

import java.util.ArrayList;
import java.util.List;


public class UflowService implements IUflowService {

    int FLOW_ACTION_START = 100;
    int FLOW_ACTION_BLOCK = 101;
    int FLOW_ACTION_ACTIVE = 102;
    int FLOW_ACTION_TERMINATE = 103;
    int FLOW_ACTION_WITHDRAW = 104;

    int TASK_ACTION_COMPLETE = 200;
    int TASK_ACTION_CHECKOUT = 201;
    int TASK_ACTION_BACK = 202;
    int TASK_ACTION_FORWARD = 203;
    int TASK_ACTION_ADDCHILD = 204;

    int RESULT_SUCESS = 0;
    int RESULT_FAIL = -1;

    @SuppressWarnings("unchecked")
    @Override
    public FlowOperationResp operateFlow(FlowOperationReq req) {

        FlowOperationResp resp = new FlowOperationResp();
        DynamicDict dict = null;

        try {

            dict = getUbossDynamicDict();
        } catch (BaseAppException e) {

            resp.setResultCode(RESULT_FAIL);
            resp.setErrorMsg("get uboss DynamicDict error:" + e.getMessage());
            return resp;
        }


        if (FLOW_ACTION_START == req.getAction()) {

            try {

                dict.serviceName = "BpmClientService";
                dict.set("method", "startProcessByBizKey");
                dict.set("PROC_BIZ_KEY", req.getFlowDefKey());

                ServiceFlow.callService(dict, true);

                List<DynamicDict> nextTaskLists = dict.getList("TASK_LIST");

                List<String> nextTaskListIds = new ArrayList<String>(nextTaskLists.size());
                for (DynamicDict nextTask : nextTaskLists) {

                    nextTaskListIds.add(nextTask.getString("TASK_LIST_ID"));
                }

                resp.setCurrTaskListIds(nextTaskListIds);
                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }
        } else if (FLOW_ACTION_BLOCK == req.getAction()) {

            try {

                dict.serviceName = "BpmClientService";
                dict.set("method", "suspendProcess");
                dict.set("HOLDER_ID", req.getHolderId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }

        } else if (FLOW_ACTION_ACTIVE == req.getAction()) {

            try {

                dict.serviceName = "BpmClientService";
                dict.set("method", "unSuspendProcess");
                dict.set("HOLDER_ID", req.getHolderId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }

        } else if (FLOW_ACTION_TERMINATE == req.getAction()) {

            try {

                dict.serviceName = "BpmClientService";
                dict.set("method", "terminateProcess");
                dict.set("HOLDER_ID", req.getHolderId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }

        } else if (FLOW_ACTION_WITHDRAW == req.getAction()) {

            try {

                dict.serviceName = "BpmClientService";
                dict.set("method", "backProcess");
                dict.set("HOLDER_ID", req.getHolderId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }
        } else {

            resp.setResultCode(RESULT_FAIL);
            resp.setErrorMsg("unsupport flow action :" + req.getAction());
        }

        return resp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TaskOperationResp operateTask(TaskOperationReq req) {

        TaskOperationResp resp = new TaskOperationResp();
        DynamicDict dict = null;

        try {

            dict = getUbossDynamicDict();
        } catch (BaseAppException e) {

            resp.setResultCode(RESULT_FAIL);
            resp.setErrorMsg("get uboss DynamicDict error:" + e.getMessage());
            return resp;
        }

        if (TASK_ACTION_COMPLETE == req.getAction()) {

            try {

                dict.serviceName = "WorkItemService";
                dict.set("method", "completeTask");
                dict.set("TASK_LIST_ID", req.getTaskListId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }
        } else if (TASK_ACTION_CHECKOUT == req.getAction()) {

            try {

                dict.serviceName = "WorkItemService";
                dict.set("method", "claimTask");
                dict.set("TASK_LIST_ID", req.getTaskListId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }
        } else if (TASK_ACTION_BACK == req.getAction()) {

            try {

                dict.serviceName = "BpmClientService";
                dict.set("method", "backTask");
                dict.set("TASK_LIST_ID", req.getTaskListId());
                dict.set("BACK_REASON_ID", req.getBackReasonId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }
        } else if (TASK_ACTION_FORWARD == req.getAction()) {

            try {

                dict.serviceName = "WorkItemService";
                dict.set("method", "forwardTask");
                dict.set("TASK_LIST_ID", req.getTaskListId());
                DynamicDict dict2 = new DynamicDict();
                dict.set("ASSGIN_IFNO", dict2);
                dict2.set("USER_ID", req.getAssginUserId());

                resp.setResultCode(RESULT_SUCESS);
            } catch (Exception e) {

                resp.setResultCode(RESULT_FAIL);
                resp.setErrorMsg(e.getMessage());
            }
        } else {

            resp.setResultCode(RESULT_FAIL);
            resp.setErrorMsg("unsupport task action :" + req.getAction());
        }

        return resp;
    }

    private DynamicDict getUbossDynamicDict() throws BaseAppException {

        DynamicDict dict = new DynamicDict();
        DynamicDict uboss_session = new DynamicDict();
        uboss_session.set("user-name", "webservice");
        dict.set("zsmart_session", uboss_session);

        return dict;
    }

}
