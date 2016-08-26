define(function() {
    return {  
         saveLinkForm: function(param,id,success) { 
         	var lvar  = param;          
            return callRemoteFunction("WorkItemService", {method: "completeTask",LVAR:lvar,TASK_LIST_ID:id},success);
        },
         qryLinkForm: function(id) {          
            return callRemoteFunction("TaskFormService", {method: "qryTaskLinkForm",FORM_ID:id});
        }
    }
});