define(function(){
	return {
		qryItemCount: function(param, success) {
			portal.callService("QryAllItems", fish.clone(param), success, {
				Count: true
			});
		},
		
		qrySpTypeList: function(){
			return portal.callService("QryDictData", {CODE:"XZSPLB"});
		},
		
		qryItemList: function(param, filter, success) {
			portal.callService("QryAllItems", fish.clone(param), success, filter);
		},
		
		addItem: function(param, success) {
			portal.callService("AddItem", param, success);
		},
		
		editItem: function(param, success){
			portal.callService("UpdateItem", param, success);
		},
		
		deleteItem: function(param, success){
			portal.callService("DeleteItem", param, success);
		}
	};
});
