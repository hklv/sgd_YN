define([
	"text!modules/ItemMgr/LawMgr/templates/FileUploadRowTpl.html"
], function(template){
	return portal.BaseView.extend({
		tagName:"tr",
		
		template: fish.compile(template),
		
		events:{
			"click .js-delete-file": "deleteFile"
		},
		
		initialize: function(data){
			this.data = data;
			this.render();
		},
		
		render: function(){
			this.$el.html(this.template(this.data));
			return this;
		},
		
		deleteFile:function(e){
			console.log($(e.target).data("item-id"));
			this.remove();
		}
	});
});