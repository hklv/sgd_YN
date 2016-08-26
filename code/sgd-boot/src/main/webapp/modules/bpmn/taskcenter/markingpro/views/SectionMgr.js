define([
	'text!modules/bpmn/taskcenter/markingpro/templates/SectionMgrTemplate.html',
	"i18n!modules/bpmn/taskcenter/markingpro/i18n/markingMgr",
	'modules/bpmn/taskcenter/markingpro/actions/SectionAction',
],function(template,i18nData,sectionAction){

	return portal.BaseView.extend({
		template: fish.compile(template),

		events: {
			"click .js-submit": "submit",
			"click .js-cancel":"closePop"
		},
	
		initialize: function(data){
			this.holderNo=data.HOLDER_NO;
			this.taskListId = data.TASK_LIST_ID;
			this.url = data.url;
			this.btn = data.btn;
		},

		render: function(){
			this.$el.html(this.template(this.resource));
			return this;
		},
		afterRender:function(){
			this.$qryForm = this.$(".js-form").form();
			this.qryLinkForms(this.url,this.btn);
		},
		submit: function(){
			var lvar= new Object();			
            var formData = this.$qryForm.form('value');           
			lvar.TASK_RESULT=$(".opinion").val(); 
			if(!this.url){
				this.getView(".linkFormDiv").trigger("linkFomSubmit", formData,this.HOLDER_NO);
			}
			if((sectionAction.saveLinkForm(lvar,this.taskListId)).CALL_SERVICE_SUCCESS){
				this.parentView.popup.close();
			}
			
		},
		closePop:function(){
			this.parentView.popup.dismiss();
		},

		//查询环节表单
		qryLinkForms:function(url,btn){			
			if(!url&&!btn){
				$(".marking_flowOrderPro").hide();
				return;
			}
			if("2"===btn.BTN_ID){
				
			}
			if("9"===btn.BTN_ID){
				$(".opinionDiv").hide();
				$(".sectionTitle").html("增派规则");
			}
			this.requireView({
                url: this.url,
                selector: ".linkFormDiv"
            });  					
		}
	});
});