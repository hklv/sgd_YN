define(function(){
	return {
		queryItemList: function(success){
			portal.callService("QryItemTree", null, success);
		},
		
		//查询申请人类型
		queryApplyerType: function(){
			return portal.callService("QryDictData", {CODE:"SQRLX"});
		},
		
		//查询证件类型
		queryIDType: function(){
			return portal.callService("QryDictData", {CODE:"ZJLX"});
		},
		
		//保存受理单据
		submitAccept: function(param){
			return portal.callService("SaveAcception", param);
		},
		
		//启动受理流程
		startWorkFlow: function(procDefId){
			var formData = {};
			formData.method = "startProcess";
			formData.PROC_DEF_ID = procDefId;
			var result = callRemoteFunction("BpmClientService",formData);
			return result;
		},
		
		queryMaterialList: function(itemId){
			return portal.callService("QryMaterialsByItemId", {ITEM_ID:itemId});
		}
	}
});
