define([
    'text!modules/bpmn/tasktemplate/templates/taskDetailTemplate.html',
    "modules/bpmn/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr"
], function(unTranslateTemplate, taskTemplateAction, i18nData) {
    var taskDetailTemplateView = portal.BaseView.extend({
        template: fish.compile(unTranslateTemplate),
        events: {
            "click .js-form-new": 'newForm',
            "click .js-form-edit": 'editForm',
            "click .js-form-ok": 'okForm',
            "click .js-form-cancel": 'cancelForm'
        },
        initialize: function(option) {
            this.$grid = option.grid;
        },
        render: function() {
            this.$el.html(this.template(i18nData));
        },
        afterRender: function() {
            fish.on("rowChange.tasktemplate", function(rowData) {
                this.rowSelectCallback(rowData);
            }.bind(this));
            this.$templateTypeCombox = $('#templateTypeCombox').combobox({
                placeholder: '请选择',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    { name: '管理类', value: 'A' },
                    { name: '运维类', value: 'B' },
                    { name: '生产类', value: 'C' }
                ],
            });
            this.$taskTypeCombox = $('#taskTypeCombox').combobox({
               
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    { name: '人工任务', value: 'U' },
                    { name: '系统任务', value: 'S' }
                ]
            }).combobox('value','U');
            this.$taskTypeCombox.on('combobox:change', function() {
                this.taskTypeChange(this.$taskTypeCombox.combobox('value'));
            }.bind(this));
            this.$multiSiginCombox = $('#multiSiginCombox').combobox({
                placeholder: '请选择',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    { name: '否', value: 'A' },
                    { name: '是', value: 'B' }

                ]
            });
            this.$multiSiginCombox.on('combobox:change', function() {
                this.isMulSigin(this.$multiSiginCombox.combobox('value'));
            }.bind(this));
            this.$signTypeCombox = $('#signTypeCombox').combobox({
                placeholder: '请选择',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    { name: '全部通过', value: 'A' },
                    { name: '一票通过', value: 'B' },
                    { name: '百分比', value: 'C' },
                    { name: '绝对数量', value: 'D' }

                ]
            });
            this.$signTypeCombox.on('combobox:change', function() {
                this.signTypeChange(this.$signTypeCombox.combobox('value'));
            }.bind(this));
            this.$formDetailForm = this.$(".js-form-detail");
            this.$formPopEdit = this.$formDetailForm.find("[name='FORM_NAME']").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/bpmn/popwin/formselpopwin/views/formSelPopWin",
                        close: function(msg) {
                            this.$formPopEdit.popedit("setValue", {
                                Value: msg,
                                Text: msg.FORM_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "Text",
                dataValueField: "Value"
            });
        },
        rowSelectCallback: function(rowData) {

            if (rowData) {
                this.setBtnStatus(false, false);
                //   !rowData.FORM_NAME && rowData.FORM_NAME = "";
                this.taskTypeChange(rowData.TASK_TYPE);
                this.$formDetailForm.form('value', rowData).form("disable");
            } else {
                this.setBtnStatus(false, true);
                this.$formDetailForm.form('clear').form("disable");
            }
            this.editAction = null;
            this.editActionChange(this.editAction);
        },
        editActionChange: function(status) {
            var editShow = true;
            if (status && status == "NEW") { //新增
                editShow = false;
                this.$formDetailForm.form('clear').form("enable");
            } else if (status && status == "EDIT") { //编辑
                editShow = false;
                this.$formDetailForm.form("enable");
                this.$taskTypeCombox.combobox('disable');
            }
            if (editShow) {
                this.$(".js-form-cancel").parents(".col-sm-offset-6").hide();
                this.$(".js-form-cancel").parents(".col-sm-offset-6").prev().show();
            } else {
                this.$(".js-form-cancel").parents(".col-sm-offset-6").show();
                this.$(".js-form-cancel").parents(".col-sm-offset-6").prev().hide();
            }

        },
        taskTypeChange: function(taskType) {
            if (taskType == 'U') {
                this.$formDetailForm.find("[name='SERVICE_NAME']").parents(".col-md-4.col-sm-6").hide();
                this.$formDetailForm.find("[name='PARAM_PAGE']").parents(".col-md-4.col-sm-6").hide();
                this.$formDetailForm.find("[name='IS_MULTI_SIGIN']").parents(".col-md-4.col-sm-6").show();
                this.$formDetailForm.find("[name='FORM_NAME']").parents(".col-md-4.col-sm-6").show();
                this.showTab();

            } else if (taskType == 'S') {
                this.$formDetailForm.find("[name='SERVICE_NAME']").parents(".col-md-4.col-sm-6").show();
                this.$formDetailForm.find("[name='PARAM_PAGE']").parents(".col-md-4.col-sm-6").show();
                this.$formDetailForm.find("[name='IS_MULTI_SIGIN']").parents(".col-md-4.col-sm-6").hide();
                this.$formDetailForm.find("[name='FORM_NAME']").parents(".col-md-4.col-sm-6").hide();
                this.hideTab();
            }
            $(".js-taskcenter-tab").tabs("option", "active", 0);
        },
        hideTab: function() {
            $(".js-taskcenter-tab").tabs("hideTab", 1);
            $(".js-taskcenter-tab").tabs("hideTab", 2);
        },
        showTab: function() {
            $(".js-taskcenter-tab").tabs("showTab", 1);
            $(".js-taskcenter-tab").tabs("showTab", 2);
        },
        isMulSigin: function(flag) {
            if (flag == 'A') {
                this.$formDetailForm.find("[name='SIGN_TYPE']").parents(".col-md-4.col-sm-6").hide();
            } else if (flag == 'B') {
                this.$formDetailForm.find("[name='SIGN_TYPE']").parents(".col-md-4.col-sm-6").show();
            }
        },
        signTypeChange: function(val) {
            if (val == 'C' || val == 'D') {
                this.$formDetailForm.find("[name='SIGIN_VALUE']").parents(".col-md-4.col-sm-6").show();
            } else {
                this.$formDetailForm.find("[name='SIGIN_VALUE']").parents(".col-md-4.col-sm-6").hide();
            }

        },
        setBtnStatus: function(newDisabled, editDisabled) {
            this.$(".js-form-new").attr("disabled", newDisabled);
            this.$(".js-form-edit").attr("disabled", editDisabled);
        },
        editForm: function(event) {
            this.editAction = "EDIT";
            this.editActionChange(this.editAction);
        },
        newForm: function(event) {
            this.editAction = "NEW";
            this.editActionChange(this.editAction);
        },
        okForm: function(event) {
            var formData = this.$formDetailForm.form('value');
            formData.PROC_DEF_TYPE_ID = this.PROC_DEF_TYPE_ID;
            this.modFormData(formData);
            if (this.editAction == "NEW") {
                formData.ADD_OR_EDIT = "add";
                var rowData = taskTemplateAction.updateTaskTemp(formData);
                this.$grid.jqGrid('addRowData', rowData, 'last');
                this.$grid.jqGrid('setSelection', rowData);
                this.rowSelectCallback(rowData);
            } else if (this.editAction == "EDIT") {
                formData.ADD_OR_EDIT = "edit";
                formData.TEMPLATE_ID = this.rowData.TEMPLATE_ID;
                taskTemplateAction.updateTaskTemp(formData);
                this.$grid.jqGrid("setRowData", formData);
                this.rowSelectCallback(formData);
            }
        },
        modFormData: function(data) {
            if (data.TASK_TYPE == 'U') { //人工任务
                var linkForm = this.$formPopEdit.popedit("getValue");
                data.FORM_ID = linkForm.Value.FORM_ID ? linkForm.Value.FORM_ID : "";
                data.FORM_NAME = linkForm.Value.FORM_NAME ? linkForm.Value.FORM_NAME : "";
            } else if (data.TASK_TYPE == 'S') {

            }

        },
        cancelForm: function(event) {
            this.$formDetailForm.resetValid();
            this.rowSelectCallback(this.rowData);
        }
    });
    return taskDetailTemplateView;

});
