define([
    "text!modules/bpmn/popwin/jump/templates/processLinkSelPopWin.html",
    "modules/bpmn/popwin/jump/actions/processLinkSelAction",
    "i18n!modules/bpmn/popwin/jump/i18n/processLinkSelMgr",
], function(template, processLinkSelAction, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-ok": "okClick",
            "click .js-cancel": "cancelClick"
        },
        initialize: function(option) {
            this.HOLDER_ID = option.HOLDER_ID;
            this.TASK_LIST_ID = option.TASK_LIST_ID;
            this.colModel = [{
                name: 'ACT_ID',
                label: '',
                width: "10%",
                hidden: true,
                key: true
            }, {
                name: 'ACT_NAME',
                label: "环节名称",
                width: "30%",
                search: true
            }];
        },
        render: function() {
            this.setElement(this.template(i18nData));
        },
        afterRender: function() {
            var dirMenus = null;
            this.$grid = this.$(".js-process-link-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json'
            });
            this.$(".js-searchbar").searchbar({
                target: this.$grid
            });
            this.qryProcDefAfterAct();
        },
        qryProcDefAfterAct: function() {
            var qryCond = {
                "HOLDER_ID": this.HOLDER_ID,
                "TASK_LIST_ID": this.TASK_LIST_ID
            };
            var datas = processLinkSelAction.qryProcDefAfterAct(qryCond);
            var dataList = datas.ACTIVITY_LIST || [];
            this.$grid.grid("reloadData", { 'rows': dataList });
        },
        okClick: function() {
            var process = this.$grid.jqGrid("getSelection");
            if ($.isEmptyObject(process)) {
                fish.warn("你必须至少选中一个环节");
                return;
            }
            var nextAct={
                "TASK_LIST_ID": this.TASK_LIST_ID,
                "NEXT_ACTIVITY_ID":process.ACT_ID                
            }
            var jumpData=processLinkSelAction.jumpTask(nextAct);

            this.popup.close(process);
        }

    });
});
