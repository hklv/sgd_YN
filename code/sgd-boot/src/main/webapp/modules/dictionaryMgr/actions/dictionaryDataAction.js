define(function() {
    return {
        qryDictDataCount: function(id, success) {
            portal.callService("QryDictDataList", { DICTIONARY_ID: id }, success, { Count: true });
        },
        qryDictDataByTypeId: function(id, success) {
            portal.callService("QryDictData", { DICTIONARY_ID: id }, success)
        },
        updateDictData: function(param, success) {
            portal.callService("UpdateDictData", param, success);
        },
        addDictData: function(param, success) {
            portal.callService("AddDictData", param, success)
        },
        delDictData: function(id, success) {
            portal.callService("DelDictData", { ID: id }, success);
        }
    }
});
