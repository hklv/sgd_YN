define([
	'text!modules/bpmn/taskcenter/markingpro/templates/FlowHeadTemplate.html',
	"i18n!modules/bpmn/taskcenter/markingpro/i18n/markingMgr",
	'modules/bpmn/taskcenter/markingpro/actions/markingProAction',
],function(template,i18nData,markingProAction){

	return portal.BaseView.extend({
		template: fish.compile(template),

		initialize: function(data){
			this.taskListId = data.TASK_LIST_ID;
		},

		render: function(){
			this.$el.html(this.template());
			return this;
		},
		afterRender:function(){
			this.qryFlowHeadInfo(this.taskListId);			
		},
			//通过taskListId查询流程单相关信息
		qryFlowHeadInfo:function(taskListId){			
			var datas = markingProAction.qryMarkingProData(taskListId);
			var task = datas.taskDetail;
			this.$(".HOLDER_NO").append(task.HOLDER_NO);
			this.$(".TASK_NAME").append(task.TASK_NAME);
			this.$(".CREATE_DATE").append(task.CREATE_DATE);
			this.$(".DUE_DATE").append(task.DUE_DATE);
			this.$(".DESCRIPTION").append(task.DESCRIPTION);
		}
	});
});