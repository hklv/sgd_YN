define([
	"text!modules/SynthAccept/SynthAcceptMgr/templates/SynthAcceptMgr.html",
	"i18n!modules/SynthAccept/SynthAcceptMgr/i18n/SynthAccept",
	"modules/SynthAccept/SynthAcceptMgr/views/ItemTreeView",
	"modules/SynthAccept/SynthAcceptMgr/views/AcceptView"
], function(template, i18nData, ItemTreeView, AcceptView){
	return portal.BaseView.extend({
		template: fish.compile(template),

		initialize: function(){
			this.eventModel = _.extend({},fish.Events);
			this.setViews({
				".col-md-3": new ItemTreeView(this.eventModel)
			});
		},
		
		render: function(){
			this.$el.html(this.template(i18nData));
			this.eventModel.on("newAcceptView", this.newAcceptView, this);
		},
		
		afterRender: function(){
			
		},
		
		resize: function(delta) {
        	this.getView(".col-md-3").subResize(delta);
        	if(this.getView(".col-md-9")){
        		this.getView(".col-md-9").subResize(this.$(".col-md-3").height());	
        	}
       	},
       	
       	newAcceptView: function(itemRowData){
       		this.setView(".col-md-9", new AcceptView(itemRowData, this.eventModel));
       		this.renderViews(".col-md-9");
       		this.getView(".col-md-9").subResize(this.$(".col-md-3").height());
       	}
	});
});