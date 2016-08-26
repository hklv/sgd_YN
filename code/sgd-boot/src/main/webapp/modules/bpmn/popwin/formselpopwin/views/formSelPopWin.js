define([
    "text!modules/bpmn/popwin/formselpopwin/templates/formSelPopWin.html",
    "modules/bpmn/popwin/formselpopwin/actions/taskformAction",
    "i18n!modules/bpmn/popwin/formselpopwin/i18n/taskformMgr",
], function(template, taskformAction, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-ok": "okClick",
            "click .js-cancel": "cancelClick"
        },
        initialize: function() {
            this.colModel = [{
                name: 'FORM_ID',
                label: '',
                width: "10%",
                hidden: true,
                key: true
            }, {
                name: 'FORM_NAME',
                label: "表单名称",
                width: "30%",
                editable: true,
                search: true,
                editrules: "表单名称" + ":required;length[1~255, true]"
            }, {
                name: 'FORM_TYPE',
                label: "表单类型",
                width: "30%",
                editrules: "表单类型" + ":required;length[1~255, true]",
                formatter: 'select',
                formatoptions: {
                    value: { 'S': "手工表单", 'D': "动态表单" }
                }
            }, {
                name: 'APPLY_TYPE',
                label: "使用场景",
                width: "30%",
                editrules: "使用场景" + ":required;length[1~255, true]",
                formatter: 'select',
                formatoptions: {
                    value: { 'A': "环节表单", 'B': "流程表单" }
                }
            }];
            this.resource = i18nData;
        },
        render: function() {
            this.setElement(this.template(this.resource));
        },
        afterRender: function() {
            var dirMenus = null;
            this.dirMenuGrid = this.$(".js-dir-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                pager: true,
                pageData: function() { this.qryTaskFormList(false); }.bind(this),
            });
            this.$(".js-searchbar").searchbar({
                target: this.dirMenuGrid
            });
            this.qryTaskFormList(true);
        },
        qryTaskFormList: function(reset) {
            var thatGrid = this.$(".js-dir-grid"),
                rowNum = thatGrid.grid("getGridParam", "rowNum"),
                page = reset ? 1 : thatGrid.grid("getGridParam", "page"),
                sortname = thatGrid.grid("getGridParam", "sortname"),
                sortorder = thatGrid.grid("getGridParam", "sortorder");
            var filter = {
                PAGE_INDEX: page - 1,
                PAGE_SIZE: rowNum
            };
            var datas = taskformAction.qryLinkFormList(filter);
            var count = Number(datas.FORM_LIST_COUNT);
            var dataList = datas.FORM_LIST || [];
            thatGrid.grid("reloadData", {
                'rows': dataList,
                'page': page,
                'records': count
            });
        },
        okClick: function() {
            var process = this.dirMenuGrid.jqGrid("getSelection");
            if ($.isEmptyObject(process)) {
                fish.warn("你必须至少选中一个流程");
                return;
            }
            this.popup.close(process);
        }

    });
});
