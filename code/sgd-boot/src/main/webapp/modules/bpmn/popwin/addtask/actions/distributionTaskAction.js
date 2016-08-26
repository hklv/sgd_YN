define(function() {
    return {
        qryProcDefAfterAct: function(qryCond) {
            var inParam = qryCond;
            inParam.method = "qryProcDefAfterAct";
            return callRemoteFunction("BpmClientService", inParam);
        },
        jumpTask: function(nextAct) {
            var inParam = nextAct;
            inParam.method = "jumpTask";
            return callRemoteFunction("WorkItemService", inParam);
        },
    }
});
