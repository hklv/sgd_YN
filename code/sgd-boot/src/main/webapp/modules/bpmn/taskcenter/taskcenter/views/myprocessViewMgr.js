define([
    "text!modules/bpmn/taskcenter/taskcenter/templates/myprocessTemplate.html",
    "modules/bpmn/taskcenter/taskcenter/actions/myprocessAction",
    "i18n!modules/bpmn/taskcenter/taskcenter/i18n/taskcenterMgr",
], function(template, myprocessAction, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function(option) {
            this.colModel = [{
                name: 'FORM_ID',
                label: 'ID',
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
            }];
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            var processStatus = [{
                TEXT: "正在处理",
                VALUE: "LOGIN_SUCCESS"
            }, {
                TEXT: "完成",
                VALUE: "LOGIN_SUCCESS"
            }, {
                TEXT: "回退",
                VALUE: "LOGIN_SUCCESS"
            }, {
                TEXT: "撤回",
                VALUE: "LOGIN_SUCCESS"
            }, {
                TEXT: "挂起",
                VALUE: "LOGIN_SUCCESS"
            }, {
                TEXT: "废弃",
                VALUE: "LOGIN_SUCCESS"
            }];
            this.$("#processStatusCombox").combobox({
                dataTextField: 'TEXT',
                dataValueField: 'VALUE',
                dataSource: processStatus
            });
            this.logBeginDate = this.$(".js-begin-login-date").datetimepicker();
            this.logEndDate = this.$(".js-end-login-date").datetimepicker();
            var endDate = new Date();
            this.logEndDate.datetimepicker("setDate", endDate);
            var t = endDate.getTime() - 1000 * 60 * 60 * 24;
            var beginDate = new Date(t);
            this.logBeginDate.datetimepicker("setDate", beginDate);
            this.$logGrid = this.$(".js-login-log-grid").jqGrid({
                colModel: this.colModel,
                pager: true,
                datatype: 'json',
                pageData: function(page) { this.loadGrid(false); }.bind(this)
            });

        },
        resize: function(delta) {
            portal.utils.gridIncHeight(this.$logGrid, delta);
        }
    });
});
