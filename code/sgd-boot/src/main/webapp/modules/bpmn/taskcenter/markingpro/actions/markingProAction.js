define(function() {
    return {
        qryMarkingProData: function(id) {            
            return callRemoteFunction("WorkItemService", {method: "qryTaskDetai",TASK_LIST_ID:id});
        },

         qryButtonInfo: function(param,id,success) { 
         	var lvar  = param;          
            return callRemoteFunction("WorkItemService", {method: "completeTask",LVAR:lvar,TASK_LIST_ID:id},success);
        }
    }
});