define(function() {
    return {
        qryHandleProData: function(id) {            
            return callRemoteFunction("BpmClientService", {method: "qryProcessTrack", PROCESS_INSTANCE_ID:id});
        },

    }
});