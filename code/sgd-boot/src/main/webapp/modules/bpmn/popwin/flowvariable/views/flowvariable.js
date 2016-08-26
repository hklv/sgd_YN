define([
    'text!modules/bpmn/popwin/flowvariable/templates/flowvariable.html',
    'modules/dirmenumgr/models/DirOrMenuItem',
    'modules/dirmenumgr/actions/DirMenuAction',
    'modules/bpmn/popwin/flowvariable/actions/flowVariableAction',
    'i18n!modules/bpmn/popwin/flowvariable/i18n/flowdesignerMgr'
], function (menuMgrTpl, DirOrMenuItem, dirMenuAction, procVariablesAction, i18nDirMenuMgr) {
    return portal.BaseView.extend({
        template: fish.compile(menuMgrTpl),

        events: {
            "click .js-submit": "addProcessVariables",
            "click .js-cancel": "cancel",
            "click .js-procVar-new": "newProcVar",
            "click .js-procVar-edit": "editProcVar"
        },
        initialize: function (options) {
            this.procVersionId = options.procVersionId;
            this.colModel = [{
                name: 'ID_',
                key: true,
                hidden: true
            }, {
                name: 'VAR_NAME',
                label: "流程变量",
                search: true,
                width: "20%"
            }, {
                name: 'VAR_TYPE',
                label: "变量类型",
                search: true,
                width: "20%"
            }, {
                name: 'DEFAULT_VALUE',
                label: "默认值",
                width: "20%"
            }, {
                name: 'VAR_COMMENTS',
                label: "说明",
                width: "20%"
            }, {
                sortable: false,
                label: "操作",
                width: "20%",
                formatter: 'actions',
                formatoptions: {
                    editbutton: false,
                    delbutton: true
                }
            }];
            this.on("delRow", this.delRowConfirm);
        },
        render: function () {
            this.setElement(this.template(i18nDirMenuMgr));
            return this;
        },
        afterRender: function () {
            var variableType = [{
                TEXT: "String",
                VALUE: "String"
            }, {
                TEXT: "Long",
                VALUE: "Long"
            }, {
                TEXT: "Date",
                VALUE: "Date"
            }, {
                TEXT: "Double",
                VALUE: "Double"
            }, {
                TEXT: "Boolean",
                VALUE: "Boolean"
            }];
            this.$('#VariableType').combobox({
                dataTextField: 'TEXT',
                dataValueField: 'VALUE',
                editable: false,
                dataSource: variableType
            }).combobox("value", "String");
            this.$procVarForm = this.$(".js-form");
            this.$grid = this.$(".grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                onSelectRow: this.rowSelectCallback.bind(this),
                beforeDeleteRow: function (e, rowid, data) {
                    fish.confirm("你确定要删除该流程变量吗？", function () {
                        this.trigger("delRow", data);
                    }.bind(this), $.noop);
                    return false;
                }.bind(this),
            });
            this.$grid.prev().children().searchbar({
                target: this.$grid
            });
            this.loadGrid();
        },
        addProcessVariables: function () {
            if (this.$procVarForm.isValid()) {
                var procVarForm = this.$procVarForm.form("value");
                procVarForm.PROCESS_VER_ID = this.procVersionId;
                var procVar = procVariablesAction.addProcessVariables(procVarForm);
                if (this.editAction == "Add" && procVar) {
                    fish.success("添加成功");
                    this.$grid.jqGrid('addRowData', procVar, 'last');
                    this.$grid.jqGrid('setSelection', procVar);
                    this.rowSelectCallback();
                    this.$(".js-procVar-edit").attr("disabled", false);
                } else if (this.editAction == "Edit" && procVar) {
                    this.getData();
                    this.$grid.jqGrid("setSelection", procVar);
                    fish.success("编辑成功");
                }
            }
        },
        loadGrid: function () {
            this.getData();
            if (this.dataList.length > 0) {
                this.$grid.grid("setSelection", this.dataList[0]);
                this.$(".js-procVar-edit").attr("disabled", false);
            } else {
                this.$(".js-procVar-edit").attr("disabled", true);
            }
        },
        getData: function () {
            var datas = procVariablesAction.queryPorcessVarDef(this.procVersionId);
            this.dataList = datas.PROC_DEF_VAR_LIST || [];
            this.$grid.grid("reloadData", {
                'rows': this.dataList
            });
        },
        rowSelectCallback: function () {
            var rowData = this.$grid.jqGrid('getSelection');
            this.$procVarForm.form('value', rowData).form("disable");
            this.$(".js-cancel").parent().hide();
            this.$(".js-cancel").parent().prev().show();
        },
        newProcVar: function () {
            this.$procVarForm.form('clear').form("enable");
            this.$(".js-procVar-new").parent().hide();
            this.$(".js-procVar-new").parent().next().show();
            this.editAction = "Add";
        },
        editProcVar: function () {
            this.$procVarForm.form("enable");
            this.$(".js-procVar-edit").parent().hide();
            this.$(".js-procVar-edit").parent().next().show();
            this.editAction = "Edit";
        },
        cancel: function () {
            this.$(".js-cancel").parent().hide();
            this.$(".js-cancel").parent().prev().show();
            this.rowSelectCallback();
        },
        delRowConfirm: function (data) {
            var rowData = data;
            if (procVariablesAction.delProcessVariable(rowData.ID_)) {
                var nextrow = this.$grid.jqGrid("getNextSelection", rowData),
                    prevrow = this.$grid.jqGrid("getPrevSelection", rowData);
                if (nextrow) {
                    this.$grid.jqGrid('setSelection', nextrow);
                } else if (prevrow) {
                    this.$grid.jqGrid('setSelection', prevrow);
                } else {
                    this.$procVarForm.form("clear").form("disable");
                    this.$(".js-procVar-edit").attr("disabled", true);
                }
                this.$grid.jqGrid('delRowData', rowData, {trigger: false});
                fish.success("删除成功");
            }
        }
    });
});