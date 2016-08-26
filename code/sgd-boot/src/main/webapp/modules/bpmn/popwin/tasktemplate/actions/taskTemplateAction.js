define(function() {
    return {
       qryTaskFormList: function(tasktype, proctypeid) {
            var inParam = {};
            inParam.method = "qryTaskTempList";
            inParam.PROC_DEF_TYPE_ID = proctypeid;
            inParam.TASK_TYPE = tasktype;
            //console.log("inParam:",inParam);
            return callRemoteFunction("TaskRepoService", inParam);
        },
        TaskRepoService: function(TEMPLATE_ID) {
            var inParam = {};
            inParam.method = "qryTaskTempList";
            inParam.TEMPLATE_ID = TEMPLATE_ID;
            //console.log("inParam:",inParam);
            return callRemoteFunction("TaskRepoService", inParam);
        },
        qryTaskTempList: function(PROC_DEF_TYPE_ID,TASK_TYPE) {
            var inParam = {};
            inParam.method = "qryTaskTempList";
            inParam.PROC_DEF_TYPE_ID = PROC_DEF_TYPE_ID;
            inParam.TASK_TYPE = TASK_TYPE;
            //console.log("inParam:",inParam);
            return callRemoteFunction("TaskRepoService", inParam);
        },

    }
});
