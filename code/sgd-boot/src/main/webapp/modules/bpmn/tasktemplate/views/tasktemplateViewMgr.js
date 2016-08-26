define([
    "text!modules/bpmn/tasktemplate/templates/tasktempTemplate.html",
    "modules/bpmn/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr",
], function(template, taskTemplateAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_right",
        template: fish.compile(template),
        events: {
            "click .js-query": "qryTaskTempList"
        },
        initialize: function() {
            this.colModel = [{
                name: 'TEMPLATE_ID',
                label: '',
                width: "10%",
                hidden: true,
                key: true
            }, {
                name: 'TEMPLATE_NAME',
                label: "任务名称",
                width: "30%"

            }, {
                name: 'CODE',
                label: "任务编码",
                width: "30%"

            }, {
                name: 'TASK_TYPE',
                label: "任务类型",
                width: "30%",
                formatter: 'select',
                formatoptions: {
                    value: { 'U': "人工任务", 'S': "系统任务" }
                }
            }, {
                name: 'TEMPLATE_TYPE',
                label: "模板类型",
                width: "30%",
                formatter: 'select',
                formatoptions: {
                    value: { 'A': "管理类", 'B': "运维类", 'C': "生产类" }
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
            fish.on("saveChange.reload", this.saveChange, this);
            fish.on("add.reload", this.loadGridAfterAdd, this);
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$grid = this.$(".js-data-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                pager: true,
                pageData: function() { this.loadGrid(this.id, false); }.bind(this),
                onSelectRow: this.rowSelectCallback.bind(this),
                beforeDeleteRow: function(e, rowid, rowdata) {
                    fish.confirm("你确定删除该任务模板吗", function() {
                        this.remTaskTemp(rowdata);
                    }.bind(this), $.noop);
                    return false;
                }.bind(this)

            });
            this.$(".js-searchbar").searchbar({
                target: this.$grid
            });
            this.$qryForm = this.$(".js-form").form();
            var taskStatus = [{
                TEXT: "人工任务",
                VALUE: "U"
            }, {
                TEXT: "系统任务",
                VALUE: "S"
            }];
            this.$('#taskStatusCombox').combobox({
                placeholder: '请选择',
                dataTextField: 'TEXT',
                dataValueField: 'VALUE',
                editable: false,
                dataSource: taskStatus
            });
        },
        menuNodeChange: function(id) {
            this.id = id;
            this.qryCond = {};
            this.loadGrid(this.id, true);

        },
        loadGrid: function(id, reset) {
            var thatGrid = this.$(".js-data-grid"),
                rowNum = thatGrid.grid("getGridParam", "rowNum"),
                page = reset ? 1 : thatGrid.grid("getGridParam", "page"),
                sortname = thatGrid.grid("getGridParam", "sortname"),
                sortorder = thatGrid.grid("getGridParam", "sortorder");
            var filter = {
                PAGE_INDEX: page - 1,
                PAGE_SIZE: rowNum
            };
            var datas = taskTemplateAction.qryTaskTempListByPager(this.qryCond, id, filter);
            var count = Number(datas.taskList_count);
            var dataList = datas.taskList || [];
            thatGrid.grid("reloadData", {
                'rows': dataList,
                'page': page,
                'records': count
            });
            if (dataList.length > 0) {
                thatGrid.grid("setSelection", dataList[0]);
            } else {
                fish.trigger("rowChange.tasktemplate", null);
            }
        },

        loadGridAfterAdd: function(TEMPLATE_ID) {
            var thatGrid = this.$(".js-data-grid");
            var rowNum = thatGrid.grid("getGridParam", "rowNum");
            var page = 1;
            var filter = {
                PAGE_INDEX: 0,
                PAGE_SIZE: rowNum
            };
            var datas = taskTemplateAction.qryTaskTempListByPager(this.qryCond, this.id, filter);
            var count = Number(datas.taskList_count);
            var dataList = datas.taskList || [];
            var flag=true;
            for(var i=0;i<dataList.length;i++){
                if(dataList[i].TEMPLATE_ID==TEMPLATE_ID){
                    flag=false;
                    break;
                }
            }
            if(flag){
                for (var i = 1; i < (count / rowNum) + 1; i++) {
                    page++;
                    var filter = {
                        PAGE_INDEX: i,
                        PAGE_SIZE: rowNum
                    };
                    datas = taskTemplateAction.qryTaskTempListByPager(this.qryCond, this.id, filter);
                    count = Number(datas.taskList_count);
                    dataList = datas.taskList || [];
                    for(var i=0;i<dataList.length;i++){
                        if(dataList[i].TEMPLATE_ID==TEMPLATE_ID){
                            break;
                        }
                    }
                }
            }
            thatGrid.grid("reloadData", {
                'rows': dataList,
                'page': page,
                'records': count
            });
            thatGrid.grid("setSelection", TEMPLATE_ID);
        },

        qryTaskTempList: function() {
            if (!this.$qryForm.isValid()) {
                return;
            }
            var data = this.$qryForm.form("value");
            !data.TEMPLATE_NAME && delete data.TEMPLATE_NAME;
            !data.TASK_TYPE && delete data.TASK_TYPE;
            this.qryCond = data;
            this.loadGrid(this.id, true);
        },
        remTaskTemp: function(rowdata) {
            var data = taskTemplateAction.delTaskTemp(rowdata.TEMPLATE_ID);
            if (data.CALL_SERVICE_SUCCESS) {
                this.$grid.jqGrid('delRowData', rowdata);
                portal.utils.seekBeforeRemRow(this.$grid, rowdata);
            }
        },
        rowSelectCallback: function() {
            var rowdata = this.$grid.jqGrid("getSelection");
            fish.trigger("rowChange.tasktemplate", rowdata);
        },
        saveChange:function (row) {
            this.loadGrid(this.id, false);
            if(this.$grid.jqGrid("getRowData",row.TEMPLATE_ID)) {
                this.$grid.jqGrid("setSelection", row.TEMPLATE_ID);
            }else{
                var rows=this.$grid.jqGrid("getRowData");
                this.$grid.jqGrid("setSelection", rows[0].TEMPLATE_ID);
            }
        }
    });
});
