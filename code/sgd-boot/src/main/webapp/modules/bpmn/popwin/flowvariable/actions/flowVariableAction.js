define(function () {
    return {
        addProcessVariables: function (flowVariables) {
            var inParam = flowVariables;
            inParam.method = "addProcessVariables";
            return callRemoteFunction("ProcessDefineService", inParam);
        },
        queryPorcessVarDef: function (param) {
            var inParam = {};
            inParam.PROCESS_VER_ID = param;
            inParam.method = "qryProcVarList";
            return callRemoteFunction("ProcessDefineService", inParam);
        },
        delProcessVariable: function (param) {
            var inParam = {};
            inParam.method = "delProcVariable";
            inParam.ID_ = param;
            return callRemoteFunction("ProcessDefineService", inParam);
        }
    }
});
