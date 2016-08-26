define(function() {
    return {
        qryFlowTree: function() {
            return callRemoteFunction("ProcessDefineService", { method: "qryFlowTree" });
        },
        actOrDisProcVersion: function(verStateInParam) {
            var inParam = verStateInParam;
            inParam.method = "actOrDisProcVersion";
            return callRemoteFunction("ProcessDefineService", inParam);
        },
        addProcVersion: function(formData) {
            var inParam = formData;
            inParam.method = "addProcVersion";
            return callRemoteFunction("ProcessDefineService", inParam);
        },
        addProcTemp: function(formData) {
            var inParam = formData;
            inParam.method = "addProcTemp";
            return callRemoteFunction("ProcessDefineService", inParam);
        },
        qryProcTemp: function(id) {
            return callRemoteFunction("ProcessDefineService", { "method": "qryProcTemp", "PROC_TEMP_ID": id });
        }
    }
});
