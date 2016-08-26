define([
	'text!modules/bpmn/taskcenter/markingpro/templates/MarkingMgrTemplate.html',
	"i18n!modules/bpmn/taskcenter/markingpro/i18n/markingMgr",
	'modules/bpmn/taskcenter/markingpro/actions/markingProAction',
	'modules/bpmn/taskcenter/markingpro/views/Acceptance',
	'modules/bpmn/taskcenter/markingpro/views/HandleProMgr',
	'modules/bpmn/taskcenter/markingpro/views/SectionMgr',
	'modules/bpmn/taskcenter/markingpro/views/FlowHead',
], function(template,i18nData,markingProAction,AcceptanceView,HandleProView,SectionView,FlowHeadView) {

	return portal.BaseView.extend({

			template: fish.compile(template),

			initialize: function(rowData){
				console.log("从任务中心传过来的参数："+JSON.stringify(rowData));  
				this.setViews({
					".marking_baseInfo": new FlowHeadView(rowData),
					".marking_requestForm": new AcceptanceView(rowData),
					".marking_process": new HandleProView(rowData),
					".marking_flowOrderPro": new SectionView(rowData)
				});
				if(!rowData.btn){
					this.resource =  fish.extend({TITLE:"查看"}, i18nData);
				}else{
					this.resource = fish.extend({TITLE:rowData.btn.BTN_NAME}, i18nData);
				}
					

			},
			render: function(){
				this.setElement(this.template(this.resource));
				return this;
			},

			afterRender: function() {
				this.$(".modal-body").niceScroll({
					cursorcolor: '#1d5987',
					cursorwidth: "10px",
					cursoropacitymax: "0.4"
				}); 	
			}		
	});

});