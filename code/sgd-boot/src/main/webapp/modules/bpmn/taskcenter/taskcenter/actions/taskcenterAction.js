define(function() {
    return {
        qryTaskList: function() {
            return callRemoteFunction("WorkItemService", { method: "qryTaskList" });
        },
        qryTaskListByPager: function(qryCond, filter) {
            var inParam = qryCond;
            inParam.method = "qryTaskList";
            inParam.ROW_SET_FORMATTER = filter;
            return callRemoteFunction("WorkItemService", inParam);
        },
        qryButtonListByTask: function(id) {
            return callRemoteFunction("ButtonRepo", { method: "qryButtonListByTask", TEMPLATE_ID: id });
        }
    }
});
