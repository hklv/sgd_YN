define([
    "text!modules/bpmn/popwin/tasktemplate/templates/tasktempTemplate.html",
    "modules/bpmn/popwin/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/popwin/tasktemplate/i18n/taskTemplateMgr",
], function(template, taskTemplateAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_right",
        template: fish.compile(template),
        events: {
            "click .js-ok": "onClick"
        },
        initialize: function(option) {          
            this.option=option;
            this.colModel = [{
                name: 'TEMPLATE_ID',
                label: '任务ID',
                width: "25%",
                sortable :false
            }, {
                name: 'TEMPLATE_NAME',
                label: "任务名称",
                width: "25%",
                sortable :false
            }, {
                name: 'TASK_TYPE',
                label: "任务类型",
                width: "25%",
                sortable :false,
                formatter: 'select',
                formatoptions: {
                    value: { 'U': "人工任务", 'S': "系统任务" }
                }
            }, {
                name: 'CODE',
                label: "任务编码",
                width: "25%",
                sortable :false
            }];
            this.on("menuNodeChange", this.menuNodeChange, this);
            
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
                this.$grid = this.$(".js-data-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                //pager: true,
                //pageData: function() { this.loadGrid(this.id, false); }.bind(this),
                //onSelectRow: this.rowSelectCallback.bind(this),
            });
            var result = taskTemplateAction.TaskRepoService(this.option.TEMP_ID);
            var PROC_DEF_TYPE_ID = result.taskList[0].PROC_DEF_TYPE_ID;
            var proctasktyep = result.taskList[0].TASK_TYPE;
            var procData = taskTemplateAction.qryTaskTempList(PROC_DEF_TYPE_ID,proctasktyep);
            var templateData = procData.taskList || [];
            this.$(".js-data-grid").grid("reloadData", {
                'rows': templateData
            });
        },
        menuNodeChange: function(rowdata) {
            var tempIdResult = taskTemplateAction.TaskRepoService(this.option.TEMP_ID);
            var taskType = tempIdResult.taskList[0].TASK_TYPE;
            this.qryTaskFormList(taskType, rowdata.PROC_DEF_TYPE_ID);
        },
        qryTaskFormList: function(tasktype, proctypeid) {
            var thatGrid = this.$(".js-data-grid");
            var datas = taskTemplateAction.qryTaskFormList(tasktype, proctypeid);
            var dataList = datas.taskList || [];
            thatGrid.grid("reloadData", {
                'rows': dataList
            });
        },
        onClick: function() {
            var process = this.$grid.jqGrid("getSelection");
            //console.log("process:",process);
            /*if ($.isEmptyObject(process)) {
                fish.warn("你必须至少选中一个流程");
                return;
            }*/
            this.parentView.popup.close(process);
        }

      
    });
});
