define(function(){
	return {
		qryLawCount: function(param, success) {
			portal.callService("QryAllLaws", fish.clone(param), success, {
				Count: true
			});
		},
		
		qryLawList: function(param, filter, success) {
			portal.callService("QryAllLaws", fish.clone(param), success, filter);
		},
		
		qryLawTypeList: function(success){
			return portal.callService("QryDictData", {CODE:"FLFGLX"}, success);
		},
		
		addLaw: function(param, success) {
			portal.callService("AddLaws", param, success);
		},
		
		editLaw: function(param, success){
			portal.callService("UpdateLaws", param, success);
		},
		
		deleteLaw: function(param, success){
			portal.callService("DeleteLaws", param, success);
		}
	};
});
