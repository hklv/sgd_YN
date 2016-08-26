define(function(){
	return {
		qryTaskList: function() {            
            return callRemoteFunction("WorkItemService", {method: "qryabnoramlTaskList"});
        },
        qryTaskListByPager: function(qryCond,filter) {  
        	var inParam =qryCond;
            inParam.method="qryabnoramlTaskList";
            inParam.ROW_SET_FORMATTER=filter;
            return callRemoteFunction("WorkItemService", inParam);
        },
        reTurnTask: function(EXECUTION_ID,TASK_LIST_ID){
        	var inParam = {};  
            inParam.EXECUTION_ID = EXECUTION_ID;
            inParam.TASK_LIST_ID = TASK_LIST_ID;
            inParam.method="reDoSysTask";
            return callRemoteFunction("WorkItemService", inParam);
        }
		
	};
});
