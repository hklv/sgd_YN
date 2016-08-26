define(function() {
    return {
        qryApplicationDetail:function(id){      	
        	 return portal.callService("QueryApplyInfo", {HOLDER_NO:id});
        },

         //查询申请单详细信息
        qryApplication:function(id,success){          
            portal.callService("QueryApplyInfo", {HOLDER_NO:id},success);
        },
        
        //通过ID查询申请人类型
		queryApplyerTypeById: function(id,success){
			portal.callService("QrySingleDicData", {ID:id},success);
		}
    }
});