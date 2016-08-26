define([
    "text!modules/bpmn/flowdesigner/templates/ProcTempTemplate.html",
    "modules/bpmn/flowdesigner/actions/flowdesignerAction",
    "i18n!modules/bpmn/flowdesigner/i18n/flowdesignerMgr",
], function(template, action, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-form-edit": 'editForm',
            "click .js-form-ok": 'okForm',
            "click .js-form-cancel": 'cancelForm',
            "click .js-form-new": 'newForm'
        },
        initialize: function(option) {
            this.rowData = option ? option.data : option;            
            !this.rowData.OVER_TIME && (this.rowData.OVER_TIME = "") != undefined;
            this.NEW = option.NEW;

        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$formDetailForm = $(".js-form-detail").form();
            if (this.NEW) {
                this.editAction = "NEW";
                this.editActionChange(this.editAction);
            } else if (this.rowData) {
                this.$formDetailForm.form('value', this.rowData).form("disable");
            }
        },
        editForm: function(event) {
            this.editAction = "EDIT";
            this.editActionChange(this.editAction);
        },
        newForm: function(event) {
            var parentView = this.parentView;
            parentView.requireView({
                url: parentView.viewUrl[3],
                selector: ".js-form-detail",
                viewOption: { data: this.rowData, NEW: 'Y' }
            });
        },
        okForm: function(event) {
            if (!this.$formDetailForm.isValid()) {
                return;
            } //校验成功
            var formData = this.$formDetailForm.form('value');
            formData.PROC_DEF_TYPE_ID = this.rowData.PROC_DEF_TYPE_ID;
            if (this.editAction == "NEW") {
                formData.ADD_OR_EDIT = "add";
                var procTempData = action.addProcTemp(formData);
                //添加节点 退回上一级视图
                var $grid = this.parentView.dirMenuGrid;
                var selectRow = $grid.grid("getRowData", this.rowData.id);
                $grid.grid("addChildNode", procTempData, selectRow);
                this.goBackPriorView();

            } else if (this.editAction == "EDIT") {
                formData.PROC_TEMP_ID = this.rowData.PROC_TEMP_ID;
                formData.ADD_OR_EDIT = "edit";
                action.addProcTemp(formData);
            }
            this.editActionChange(null);
        },
        cancelForm: function(event) {
            //new='Y'退回上一级试图
            if (this.NEW == 'Y') {
                this.goBackPriorView();
            } else {
                this.$formDetailForm.form('value', this.rowData).form("disable");
            }
        },
        editActionChange: function(status) {
            var editShow = true;
            if (status === "NEW") { //新增
                editShow = false;
                this.$formDetailForm.form('clear').form("enable");
            } else if (status === "EDIT") { //编辑
                editShow = false;
                this.$formDetailForm.form("enable");
            } else {
                this.$formDetailForm.form("disable");
            }
            if (editShow) {
                this.$(".js-form-cancel").parent().hide();
                this.$(".js-form-cancel").parent().prev().show();
            } else {
                this.$(".js-form-cancel").parent().show();
                this.$(".js-form-cancel").parent().prev().hide();
            }
        },
        goBackPriorView: function() {
            var parentView = this.parentView;
            parentView.requireView({
                url: parentView.viewUrl[1],
                selector: ".js-form-detail",
                viewOption: { data: this.rowData }
            });
        }
    });
});
