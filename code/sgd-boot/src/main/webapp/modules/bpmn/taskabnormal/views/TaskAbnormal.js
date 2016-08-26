define([
	"text!modules/bpmn/taskabnormal/templates/TaskAbnormal.html",
	"text!modules/bpmn/taskabnormal/templates/GridCellEditTemplate.html",
	"i18n!modules/bpmn/taskabnormal/i18n/TaskAbnormal",
	"modules/bpmn/taskabnormal/actions/TaskAbnormalAction"
], function(template,cellTemplate, i18nData, TaskAbnormalAction){
	return portal.BaseView.extend({
		template: fish.compile(template),
		cellTemplate: fish.compile(cellTemplate),
		
		events: {
			"click .restart": 'reStart',
			"click .js-query":'queryAbnormal',
		},
		
		initialize: function(){
			this.colModel = [
			{
				name: 'HOLDER_NO',
				label: "流程单编号",
				width: "15%"
			}, {
				name: "TASK_NAME",
				label: "任务名称",
				width: "15%"
			}, {
				name: "PROCESS_NAME",
				label: "流程名称",
				width: "15%"
			}, {
				name: "CREATE_DATE",
				label: "开始时间",
				width: "15%"
			}, {
				name: "STATE_REASON",
				label: "异常原因",
				width: "30%"
			},{
				width: "10%",
				sortable: false,
				formatter: function() {
					return this.cellTemplateHTML;
				}.bind(this)
			}];
		},
		render: function(){
			this.$el.html(this.template(i18nData));
			this.cellTemplateHTML = this.cellTemplate(i18nData);
			return this;
		},
		afterRender: function(){
			this.logBeginDate = this.$(".js-begin-login-date").datetimepicker();
            this.logEndDate = this.$(".js-end-login-date").datetimepicker();

			this.$qryForm = this.$(".js-form").form(); 
			//this.$qryForm.find("[name='START_TIME']").datetimepicker({viewType:"date", format:"yyyy-mm-dd hh:ii:ss"});
			//this.$qryForm.find("[name='END_TIME']").datetimepicker({viewType:"date", format:"yyyy-mm-dd hh:ii:ss"});
			this.$processPopEdit = this.$qryForm.find("[name='PROC_DEF_TYPE_ID']").popedit({
				open: function() {
					fish.popupView({
						url: "modules/bpmn/taskform/views/processSelPopWin",						
						close: function(msg) {							
							this.$processPopEdit.popedit("setValue", {
								Value: msg.CATG_ID,
								Text: msg.name
							});
						}.bind(this)
					});
				}.bind(this),
				dataTextField: "Text",
				dataValueField: "Value"
			});

			this.$TaskAbnormalGrid = this.$(".js-law-grid").jqGrid({
				colModel: this.colModel,
				datatype: 'json',
				pager: true,	
				pagebar:true,
				pageData: function() {this.loadGrid(false);}.bind(this), 
			});	

			this.queryAbnormal();
		},
		queryAbnormal:function(){
			 if (!this.$qryForm.isValid()) {
				return;
			}
			var data = this.$qryForm.form("value");			
			!data.HOLDER_NO && delete data.HOLDER_NO;
			!data.PROC_DEF_TYPE_ID && delete data.PROC_DEF_TYPE_ID;
			!data.TASK_NAME && delete data.TASK_NAME; 
			!data.START_TIME && delete data.START_TIME;
			!data.END_TIME && delete data.END_TIME;				
			this.qryCond = data;
			this.loadGrid(true);
		},
		loadGrid:function(reset){
			var rowNum = this.$TaskAbnormalGrid.grid("getGridParam", "rowNum"),
                page = reset ? 1 : this.$TaskAbnormalGrid.grid("getGridParam", "page"),
                sortname = this.$TaskAbnormalGrid.grid("getGridParam", "sortname"),
                sortorder = this.$TaskAbnormalGrid.grid("getGridParam", "sortorder");                
                var filter = {
                    PAGE_INDEX: page-1,
                    PAGE_SIZE: rowNum
                }; 
                var data= TaskAbnormalAction.qryTaskListByPager(this.qryCond,filter);
                if(data.taskList){
                	var count = Number(data.taskList.length);   
                }else{
                	var count = 0;
                }                  
                var dataList = data.taskList || [];                               
                this.$TaskAbnormalGrid.grid("reloadData", {
                            'rows': dataList,
                            'page': page,
                            'records': count
                        });                          
                if (dataList.length > 0) {
                    this.$TaskAbnormalGrid.grid("setSelection", dataList[0]);
                    
                 } 
		},
		reStart: function(){
			var rowData = this.$TaskAbnormalGrid.grid("getSelection");
			var EXECUTION_ID = rowData.EXECUTION_ID;
			var TASK_LIST_ID = rowData.TASK_LIST_ID;
			var data = TaskAbnormalAction.reTurnTask(EXECUTION_ID,TASK_LIST_ID);
			console.log("data:",data);
			if(data.Code==="-1"||data==="undefined"){
				//fish.info("重新执行任务失败");
			}else{
				fish.info("重新执行任务成功");
				this.loadGrid(false);
			}
			
			
		},
		resize: function(delta) {
			portal.utils.gridIncHeight(this.$TaskAbnormalGrid, delta);
		}
		
	});
});