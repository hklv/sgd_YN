define(function() {
    return {
        qryRootDirList: function() {
            return callRemoteFunction("TaskRepoService", { method: "initCatgTree" });
        },
        qryTaskFormList: function(id) {
            return callRemoteFunction("TaskFormService", { method: "qryTaskFormList", CATG_ID: id });
        },
        TaskRepoService: function(TEMPLATE_ID) {
            var inParam = {};
            inParam.method = "qryTaskTempList";
            inParam.TEMPLATE_ID = TEMPLATE_ID;
            //console.log("inParam:",inParam);
            return callRemoteFunction("TaskRepoService", inParam);
        },
        


    }
});
