define([
	"text!modules/ItemMgr/LawMgr/templates/AddOrEditLawPopWin.html",
	"text!modules/ItemMgr/LawMgr/templates/FileUploadRowTpl.html",
	"i18n!modules/ItemMgr/LawMgr/i18n/LawMgr",
	"modules/ItemMgr/LawMgr/actions/LawMgrAction",
	"modules/ItemMgr/LawMgr/views/FileUploadRowView"
], function(template, rowTpl, i18nData, lawMgrAction, RowView){
	return portal.BaseView.extend({
		template: fish.compile(template),
		
		fileUploadTpl: fish.compile(rowTpl),
		
		events:{
			"click .js-ok": "okClick",
			"click .js-delete-file": "deleteFile"
		},
		
		initialize: function(rowData){
			this.isUpdate = false;
			if(!_.isEmpty(rowData)){
				this.rowData = rowData;
				this.isUpdate = true;
			}
		},
		
		render: function(){
			if(this.isUpdate){
				i18nData.title = "修改法规库";
			}
			else{
				i18nData.title = "新增法规库";
			}
			this.setElement(this.template(i18nData));
		},
		
		afterRender: function(){
			
			this.$form = this.$(".js-law-form").form();
			
			this.$form.find("[name='EXE_DATE']").datetimepicker({viewType:"date",format:"yyyy-mm-dd"});
			this.$form.find("[name='PUB_DATE']").datetimepicker({viewType:"date",format:"yyyy-mm-dd"});
			
			$('.js-fileupload').fileupload({
				url:"upload?moduleName=Law",
		        dataType: 'json',
		        done: function (e, data) {
		        	var fileResult = data.result.z_d_r;
					var data = {fileName: fileResult.TRUE_NAME, fileId:fileResult.ID, fileBizName:fileResult.ALIAS_NAME};
					$(".FILE_LIST_TABLE tr:last").after(new RowView(data).el);
	            }.bind(this),
	            fail: function(e, data){
	            	fish.warn("上传失败");
	            }
		   	});
		   	if(this.isUpdate && this.rowData.FILE_LIST){
		   		$.each(this.rowData.FILE_LIST, function(index,fileResult) {
					var data = {fileName: fileResult.TRUE_NAME, fileId:fileResult.ID, fileBizName:fileResult.ALIAS_NAME};
					$(".FILE_LIST_TABLE tr:last").after(new RowView(data).el);
				});
		   	}
			
			//初始化法律法规类型列表
			lawMgrAction.qryLawTypeList(function(result){
				this.lawTypeList = result.z_d_r;
				$.each(this.lawTypeList, function(idx,data) {
					data.ID = data.ID + "";
				});
				this.$form.find("[name='TYPE_ID']").combobox({
					dataTextField: 'VALUE_NAME',
					dataValueField: 'ID',
					editable: false,
					dataSource: this.lawTypeList
				});
			}.bind(this))
			.then(function(){
				if(!this.isUpdate){
					return;
				}
				this.$form.form("value",this.rowData);	
			}.bind(this));
		},
		
		deleteFile: function(e){
			return;
			var itemId = $(e.target).data("item-id");
			var tr = e.target.parentNode.parentNode;
			tr.parentNode.removeChild(tr);
		},
		
		okClick: function(){
			if (!this.$form.isValid()) {
				return;
			}
			var formData = this.$form.form("value");
			var fileData = this.$(".js-file-form").form().form("value");
			if(!_.isEmpty(fileData)){
				formData.FILE_ID = fileData.FILE;
				if(!_.isArray(fileData.FILE)){
					formData.FILE_ID = [fileData.FILE];
				}
			}
			if(this.isUpdate){
				formData.ID = this.rowData.ID;
				lawMgrAction.editLaw(formData, function(){
					this.popup.close();
				}.bind(this));
			}
			else{
				lawMgrAction.addLaw(formData, function(){
					this.popup.close();
				}.bind(this));
			}
		}
		
	});
});