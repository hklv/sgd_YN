/**
 * Created by DELL on 2016/5/17.
 */
define(function() {
    return {
        qryButtonList: function(param) {
            return callRemoteFunction("ButtonRepo", {method: "qryButtonList", ROW_SET_FORMATTER: param});
        },
        addButtonList: function(param) {
            return callRemoteFunction("ButtonRepo", {
                method: "addButton",
                BTN_ID: param.BTN_ID,
                BTN_NAME: param.BTN_NAME,
                COMMENTS: param.COMMENTS,
                PAGE_URL: param.PAGE_URL
            });
        },
        updateButton: function(param) {
            return callRemoteFunction("ButtonRepo", {
                method: "updateButton",
                BTN_ID: param.BTN_ID,
                BTN_NAME: param.BTN_NAME,
                COMMENTS: param.COMMENTS,
                PAGE_URL: param.PAGE_URL
            });
        },
        delButton: function(param) {
            return callRemoteFunction("ButtonRepo", {
                method: "delButton",
                BTN_ID: param,
            });
        },
    };
});


