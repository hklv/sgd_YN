define(function() {
    return {
        qryRootDirList: function() {
            return callRemoteFunction("TaskRepoService", { method: "initCatgTree" });
        },
        qryTaskFormList: function(id) {
            return callRemoteFunction("TaskFormService", { method: "qryTaskFormList", CATG_ID: id });
        },


    }
});
