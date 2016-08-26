define([
    "text!modules/bpmn/taskform/templates/taskform.html",
    "modules/bpmn/taskform/actions/taskformAction",
    "i18n!modules/bpmn/taskform/i18n/taskformMgr",
], function(template, taskformAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_right",
        template: fish.compile(template),
        events: {
            "click .js-form-new": 'newForm',
            "click .js-form-edit": 'editForm',
            "click .js-form-ok": 'okForm',
            "click .js-form-cancel": 'cancelForm',
        },
        editAction: null,
        initialize: function() {
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
            }, {
                name: 'APPLY_TYPE',
                label: "使用场景",
                width: "30%",
                editrules: "使用场景" + ":required;length[1~255, true]",
                formatter: 'select',
                formatoptions: {
                    value: { 'A': "环节表单", 'B': "流程表单" }
                }
            }, {
                formatter: 'actions',
                sortable: false,
                width: "10%",
                formatoptions: {
                    delbutton: true,
                    editbutton: false
                }
            }];
            this.on("menuNodeChange", this.menuNodeChange, this);
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$grid = this.$(".js-taskform-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                pager: true,
                pageData: function() { this.qryTaskFormList(this.catgId, false); }.bind(this),
                onSelectRow: this.rowSelectCallback.bind(this),
                beforeDeleteRow: function(e, rowid, rowdata) {
                    fish.confirm("你确定删除该流程表单吗?", function() {
                        this.remFormData(rowdata);
                    }.bind(this), $.noop);
                    return false;
                }.bind(this)
            });
            this.$(".js-searchbar").searchbar({
                target: this.$grid
            });
            this.$comboboxFormType = $('#formTypeCombox').combobox({
                placeholder: '请选择',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    { name: '手工表单', value: 'S' },
                    { name: '动态表单', value: 'D' }
                ]
            });
            this.$comboboxApplyType = $('#applyTypeCombox').combobox({
                placeholder: '请选择',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    { name: '环节表单', value: 'A' },
                    { name: '流程表单', value: 'B' }
                ]
            });
            this.$formDetail = this.$(".js-form-detail");
            this.$formDetail.find("[name='APPLY_TYPE']").combobox();
        },
        menuNodeChange: function(id) {
            console.info(id);
            this.catgId = id;
            this.qryTaskFormList(id,true);
        },
        qryTaskFormList: function(id, reset) {
            var thatGrid = this.$(".js-taskform-grid"),
                rowNum = thatGrid.grid("getGridParam", "rowNum"),
                page = reset ? 1 : thatGrid.grid("getGridParam", "page"),
                sortname = thatGrid.grid("getGridParam", "sortname"),
                sortorder = thatGrid.grid("getGridParam", "sortorder");
            var filter = {
                PAGE_INDEX: page - 1,
                PAGE_SIZE: rowNum
            };
            var datas = taskformAction.qryTaskFormList(id, filter);
            var count = Number(datas.FORM_LIST_COUNT);
            var dataList = datas.FORM_LIST || [];
            thatGrid.grid("reloadData", {
                'rows': dataList,
                'page': page,
                'records': count
            });
            if (dataList.length > 0) {
                thatGrid.grid("setSelection", dataList[0]);
            }
        },
        rowSelectCallback: function() {
            var rowdata = this.$grid.jqGrid('getSelection');
            this.$formDetail.form('value', rowdata).form("disable");
            this.editAction = null;
            this.editActionChange(this.editAction);
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
            if (this.$formDetail.isValid()) { //校验成功
                var formData = this.$formDetail.form('value');
                if (this.editAction == "NEW") {
                    formData.CATG_ID = this.catgId;
                    var rowData = taskformAction.addFormData(formData);
                    this.$grid.jqGrid('addRowData', rowData, 'last');
                    this.$grid.jqGrid('setSelection', rowData);
                    this.rowSelectCallback();
                } else if (this.editAction == "EDIT") {
                    var rowdata = this.$grid.jqGrid("getSelection");
                    formData.CATG_ID = this.catgId;
                    formData.FORM_ID = rowdata.FORM_ID;
                    taskformAction.updateFormData(formData);
                    this.$grid.jqGrid("setRowData", formData); //更新DOM                        
                    this.rowSelectCallback();
                }
            }
        },
        cancelForm: function(event) {
            this.$formDetail.resetValid();
            this.rowSelectCallback();
        },
        editActionChange: function(status) {
            var editShow = true;
            if (status && status == "NEW") { //新增
                editShow = false;
                this.$formDetail.form('clear').form("enable");
                this.$comboboxFormType.combobox('value', 'S');
                this.$comboboxApplyType.combobox('value', 'A');
            } else if (status && status == "EDIT") { //编辑
                editShow = false;
                this.$formDetail.form("enable");
            }

            if (editShow) {
                this.$(".js-form-cancel").parent().hide();
                this.$(".js-form-cancel").parent().prev().show();
            } else {
                this.$(".js-form-cancel").parent().show();
                this.$(".js-form-cancel").parent().prev().hide();
            }

        },
        remFormData: function(rowdata) {
            var data = taskformAction.delFormData(rowdata.FORM_ID);
            if (data.CALL_SERVICE_SUCCESS) {
                this.$grid.jqGrid('delRowData', rowdata);
                portal.utils.seekBeforeRemRow(this.$grid, rowdata);
            }
        }
    });

});
