define(function() {
    return {
         terminateProcess: function(data) {
            var inParam = data;
            inParam.method = "terminateProcess";
            return callRemoteFunction("BpmClientService", inParam);
        },
    }
});
