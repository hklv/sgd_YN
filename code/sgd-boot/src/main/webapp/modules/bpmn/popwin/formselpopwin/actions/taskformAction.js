define(function() {
    return {
        qryTaskFormList: function(id, filter) {
            var inParam = {};
            inParam.method = "qryTaskFormList";
            inParam.ROW_SET_FORMATTER = filter;
            inParam.CATG_ID = id;
            return callRemoteFunction("TaskFormService", inParam);
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
        },
        qryLinkFormList: function(filter) {

            return callRemoteFunction("TaskFormService", { method: "qryTaskFormList", "APPLY_TYPE": "A", "ROW_SET_FORMATTER": filter });
        },


    }
});
