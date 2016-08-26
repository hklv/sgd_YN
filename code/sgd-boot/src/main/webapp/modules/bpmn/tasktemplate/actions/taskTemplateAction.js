define(function() {
    return {
        qryTaskTempListByPager: function(qryCond, id, filter) {
            var inParam = qryCond;
            inParam.method = "qryTaskTempList";
            inParam.PROC_DEF_TYPE_ID = id;
            inParam.ROW_SET_FORMATTER = filter;
            return callRemoteFunction("TaskRepoService", inParam);
        },
        updateTaskTemp: function(param) {
            var inParam = param;
            inParam.method = "addTaskTemp";
            return callRemoteFunction("TaskRepoService", inParam);
        },
        delTaskTemp: function(id) {
            return callRemoteFunction("TaskRepoService", { "method": "addTaskTemp", "ADD_OR_EDIT": "delete", "TEMPLATE_ID": id });
        },
        qryButtonListByTask: function(id) {
            return callRemoteFunction("ButtonRepo", { "method": "qryButtonListByTask",  "TEMPLATE_ID": id });
        },
        qryBtnListNotInTemplate: function(id,filter) {
            return callRemoteFunction("TaskRepoService", { "method": "qryBtnListNotInTemplate",  "TEMPLATE_ID": id, "ROW_SET_FORMATTER": filter});
        },
        addBtnToTaskTemplate: function(id,buttunlist) {
            return callRemoteFunction("TaskRepoService", { "method": "addBtnToTaskTemplate",  "TEMPLATE_ID": id, "BUTTON_LIST": buttunlist});
        },
        qryTaskTemplateEvent: function(id) {
            return callRemoteFunction("TaskRepoService", { "method": "qryTaskTemplateEvent",  "TASK_TEMPLATE_ID": id});
        },
        confTaskTemplateEvent: function(id,TASK_TEMPLATE_EVENTS) {
            return callRemoteFunction("TaskRepoService", { "method": "confTaskTemplateEvent",  "TASK_TEMPLATE_ID": id,  "TASK_TEMPLATE_EVENTS": TASK_TEMPLATE_EVENTS});
        },
        addTaskTemplateBackReason: function(id,REASON_CODE,REASON_NAME,REASON_COMMENTS) {
            return callRemoteFunction("TaskRepoService", { "method": "addTaskTemplateBackReason",  "TASK_TEMP_ID": id,  "REASON_CODE": REASON_CODE,  "REASON_NAME": REASON_NAME,  "REASON_COMMENTS": REASON_COMMENTS});
        },
        qryTaskTemplateBackReason: function(id) {
            return callRemoteFunction("TaskRepoService", { "method": "qryTaskTemplateBackReason",  "TEMPLATE_ID": id});
        },
        delTaskTemplateBackReason: function(id) {
            return callRemoteFunction("TaskRepoService", { "method": "delTaskTemplateBackReason",  "BACK_REASON_ID": id});
        },
        modTaskTemplateBackReason: function(BACK_REASON_ID,REASON_CODE,REASON_NAME,REASON_COMMENTS) {
            return callRemoteFunction("TaskRepoService", { "method": "modTaskTemplateBackReason",  "BACK_REASON_ID": BACK_REASON_ID,  "REASON_CODE": REASON_CODE,  "REASON_NAME": REASON_NAME,  "REASON_COMMENTS": REASON_COMMENTS});
        },

    }
});
