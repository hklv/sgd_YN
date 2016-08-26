define([
	'text!modules/bpmn/taskcenter/markingpro/templates/HandleProMgrTemplate.html',
	"i18n!modules/bpmn/taskcenter/markingpro/i18n/markingMgr",
	'modules/bpmn/taskcenter/markingpro/actions/HandleProAction',
], function(template,i18nData,handleProAction) {

	return portal.BaseView.extend({

			template: fish.compile(template),

			initialize: function(data){
				this.procInstId = data.PROC_INST_ID;
				this.colModel = [{
					name: 'TASK_LIST_ID',
					label: '',
					key: true,
					hidden: true
				},{
					name: 'TASK_LIST_NAME',
					label: '任务名称',
					width: "20%"
				},{
					name: 'TASK_TYPE_STR',
					label: '任务类型',
					width: "10%"
				},{
					name: 'TASK_STATE_STR',
					label: '状态',
					width: "10%"
				}, {
					name: 'USER_NAME',
					label: '拥有者',
					width: "10%"
				}, {
					name: 'TASK_RESULT',
					label: '处理结果',
					width: "20%"
				}, {
					name: 'CREATE_DATE',
					label: '创建日期',
					width: "15%"
				},{
					name: 'END_TIME',
					label: '结束时间',
					width: "15%"
				}];

			},

			render: function(){
				this.$el.html(this.template());
				return this;
			},

			afterRender:function(){
				this.$handleProGrid = this.$(".js-handlePro-grid").grid({
					colModel: this.colModel,
					datatype: 'json',
					height:'120px',
					rownumbers:true,
				});
				this.loadData();
			},

			loadData: function() {
				var datas = handleProAction.qryHandleProData(this.procInstId);
				var dataList = datas.TASK_LIST||[];

				this.$(".js-handlePro-grid").grid("reloadData",{
						'rows': dataList,
					});
			}
	});
			
});