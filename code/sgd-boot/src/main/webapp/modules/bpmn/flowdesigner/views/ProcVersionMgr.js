define([
    "text!modules/bpmn/flowdesigner/templates/ProcVersionTemplate.html",
    "modules/bpmn/flowdesigner/actions/flowdesignerAction",
    "i18n!modules/bpmn/flowdesigner/i18n/flowdesignerMgr",
], function(template, action, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-form-edit": 'editForm',
            "click .js-form-ok": 'okForm'
        },
        initialize: function(option) {
            this.rowData = option.data;
            this.NEW = option.NEW;
            this.verState = [{
                TEXT: "激活",
                VALUE: "A"
            }, {
                TEXT: "编辑",
                VALUE: "B"
            }, {
                TEXT: "禁用",
                VALUE: "C"
            }, {
                TEXT: "仿真",
                VALUE: "S"
            }];
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$('#stateCombox').combobox({
                dataTextField: 'TEXT',
                dataValueField: 'VALUE',
                editable: false,
                dataSource: this.verState
            });
            this.logBeginDate = this.$(".js-begin-login-date").datetimepicker();
            this.logEndDate = this.$(".js-end-login-date").datetimepicker();
            this.$formDetailForm = $(".js-form-detail").form();
            if (this.NEW) {
                this.editAction = "NEW";
                this.editActionChange(this.editAction);
                this.$formDetailForm.form("enable");
            } else if (this.rowData) {
                this.$formDetailForm.form('value', this.rowData).form("disable");
            }
        },
        editForm: function(event) {
            this.editAction = "EDIT";
            this.editActionChange(this.editAction);
            if (this.rowData.VER_STATE === 'B' || this.rowData.VER_STATE === 'S') {
                this.$formDetailForm.form('enable');
            } else {
                this.$formDetailForm.find("[name='VER_STATE']").combobox('enable');
            }
            //combox可选择项切换
            this.changeComboxDataSource(this.rowData.VER_STATE);
        },
        okForm: function() {
            if (!this.$formDetailForm.isValid()) {
                return;
            } //校验成功
            var formData = this.$formDetailForm.form('value');
            formData.PROC_TEMP_ID = this.rowData.PROC_TEMP_ID;
            formData.PROCESS_VER_ID = this.rowData.PROCESS_VER_ID;
            if (this.editAction == "NEW") {
                formData.ADD_OR_EDIT = "add";
                var procTempData = action.addProcVersion(formData);
                //添加节点 退回上一级视图
                var $grid = this.parentView.dirMenuGrid;
                var selectRow = $grid.grid("getRowData", this.rowData.id);
                $grid.grid("addChildNode", procTempData, selectRow);
                this.goBackPriorView();
            } else if (this.editAction == "EDIT") {
                //版本状态切换
                var verStateInParam = {
                    VER_STATE: this.$('#stateCombox').combobox('value'),
                    PROCESS_VER_ID: this.rowData.PROCESS_VER_ID,
                    PROC_TEMP_ID: this.rowData.PROC_TEMP_ID,
                };
                action.actOrDisProcVersion(verStateInParam);
                if (this.rowData.VER_STATE === 'B' || this.rowData.VER_STATE === 'S') {
                    var formData = this.$formDetailForm.form('value');
                    formData.ADD_OR_EDIT = "edit";
                    formData.VER_STATE && delete formData.VER_STATE;
                    action.addProcVersion(formData);
                }
            }
        },
        editActionChange: function(status) {
            var cancelBtnShow = false;
            if (status === "NEW") { //新增
                cancelBtnShow = true;
                this.$('#stateCombox').combobox({
                    dataSource: [{
                        TEXT: "激活",
                        VALUE: "A"
                    }]
                }).combobox('value', 'A');
            } else if (status === "EDIT") { //编辑              
            }
            if (cancelBtnShow) {
                this.$(".js-form-cancel").show();
                this.$(".js-form-edit").hide();
            } else {
                this.$(".js-form-cancel").hide();
                this.$(".js-form-edit").show();
            }
        },

        changeComboxDataSource: function(verState) {
            var dataSourceAfterChange;
            switch (verState) {
                case 'A':
                case 'C':
                    this.verState.splice(1, 1);
                    dataSourceAfterChange = this.verState;
                    break;
                case 'B':
                    this.verState.splice(2, 1);
                    dataSourceAfterChange = this.verState;
                    break;
                case 'S':
                    dataSourceAfterChange = this.verState;
                    break;
                default:
                    dataSourceAfterChange = [];
                    break;

            }
            this.$('#stateCombox').combobox({ dataSource: dataSourceAfterChange });
        },
        goBackPriorView: function() {
            var parentView = this.parentView;
            parentView.requireView({
                url: parentView.viewUrl[2],
                selector: ".js-form-detail",
                viewOption: { data: this.rowData }
            });
        }
    });
});
