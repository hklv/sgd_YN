define(function() {
    return {
        qryDictTypeCount: function(success) { //pager
            portal.callService("QryDictList", {}, success, { Count: true });
        },
        qryAllDictType: function(filter, success) {
            portal.callService("QryDictList", {}, success, filter);
        },
        addDictType: function(param, success) {
            portal.callService("AddDict", param, success)
        },
        updateDictType: function(param, success) {
            portal.callService("UpdateDict", param, success);
        },
        delDictType: function(typeId, success) {
            portal.callService("DeleteDict", { ID: typeId }, success);
        }
    }
});
