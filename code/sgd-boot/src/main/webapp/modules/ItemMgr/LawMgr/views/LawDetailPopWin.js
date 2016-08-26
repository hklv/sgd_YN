define([
	"text!modules/ItemMgr/LawMgr/templates/LawDetailPopWin.html",
	"i18n!modules/ItemMgr/LawMgr/i18n/LawMgr",
	"modules/ItemMgr/LawMgr/views/FileUploadRowView"
], function(template, i18nData, RowView){
	return portal.BaseView.extend({
		template: fish.compile(template),
		
		events:{
			
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
			if(this.rowData.FILE_LIST){
				$.each(this.rowData.FILE_LIST, function(index,fileResult) {
					var data = {fileName: fileResult.TRUE_NAME, fileId:fileResult.ID, fileBizName:fileResult.ALIAS_NAME};
					$(".FILE_LIST_TABLE tr:last").after(new RowView(data).el);
				});
			}
		}
	});
})