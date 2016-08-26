define([
	"text!modules/ItemMgr/ItemMgr/templates/ItemDetailPopWin.html",
	"i18n!modules/ItemMgr/ItemMgr/i18n/ItemMgr",
	"text!modules/ItemMgr/ItemMgr/templates/AddRowTpl.html"
], function(template, i18nData, addRowTpl){
	return portal.BaseView.extend({
		template: fish.compile(template),
		
		rowTpl: fish.compile(addRowTpl),
		
		events:{
			"click .btn-success": function(){
				var params = {PAGE_INDEX:0,PAGE_SIZE:20};
				var list = callRemoteFunction("ButtonRepo",{method:"qryButtonList",ROW_SET_FORMATTER:params});
				console.log(list);
			}
		},
		
		initialize: function(rowData){
			this.rowData = rowData;
		},
		
		render: function(){
			this.setElement(this.template(i18nData));
		},
		
		afterRender: function(){
			this.$form = this.$(".js-detail-form").form();
			this.$form.form("value",this.rowData);
			$.each(this.rowData.ITEM_MATERIALS, function(index, result) {
				$(".FILE_LIST_TABLE tr:last").after(this.rowTpl(result));
			}.bind(this));
		}
	});
})