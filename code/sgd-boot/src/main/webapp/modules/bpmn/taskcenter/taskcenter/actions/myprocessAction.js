define(function() {
    return {
        qryTaskFormList: function(id) {
            return callRemoteFunction("TaskFormService", { method: "qryTaskFormList", CATG_ID: id });
        },
        delFormData: function(id) {
            return callRemoteFunction("TaskFormService", { method: "delTaskForm", FORM_ID: id });
        },
        updateFormData: function(param) {
            var inParam = param;
            inParam.method = "modTaskForm";
            return callRemoteFunction("TaskFormService", inParam);
        },
        addFormData: function(param) {
            var inParam = param;
            inParam.method = "addTaskForm";
            return callRemoteFunction("TaskFormService", inParam);
        }
    }
});
