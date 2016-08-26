define([
	"text!modules/SynthAccept/SynthAcceptMgr/templates/ItemFileRowTpl.html"
], function(template){
	return portal.BaseView.extend({
		tagName:"tr",
		
		template: fish.compile(template),
		
		events:{
			
		},
		
		initialize: function(data){
			this.data = data;
			this.render();
		},
		
		render: function(){
			this.$el.html(this.template(this.data));
			return this;
		},
		
		afterRender: function(){
			this.$('.js-fileupload').fileupload({
				url:'upload?moduleName=Accept',
		        dataType: 'json',
		        done: function (e, data) {
		        	var fileResult = data.result.z_d_r;
					this.$(".js-path").html("<a href='download4Sgd?fileName="+fileResult.ALIAS_NAME+"'>"+fileResult.TRUE_NAME+"</a>");
					this.$("[name='FILE_ID']").val(fileResult.ID);
	            }.bind(this),
	            fail: function(e, data){
	            	fish.warn(e);
	            }.bind(this)
		   	});	
		},
		
		deleteFile:function(e){
			console.log($(e.target).data("item-id"));
			this.remove();
		}
	});
});