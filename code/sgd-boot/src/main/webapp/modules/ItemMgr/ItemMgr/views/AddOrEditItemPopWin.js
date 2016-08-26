define([
	"text!modules/ItemMgr/ItemMgr/templates/AddOrEditItemPopWin.html",
	"text!modules/ItemMgr/ItemMgr/templates/AddRowTpl.html",
	"i18n!modules/ItemMgr/ItemMgr/i18n/ItemMgr",
	"modules/ItemMgr/ItemMgr/actions/ItemMgrAction"
], function(template, addRowTpl, i18nData, itemMgrAction){
	return portal.BaseView.extend({
		template: fish.compile(template),

		rowTpl: fish.compile(addRowTpl),
		
		events:{
			"click .js-ok": "okClick",
			"click .js-add-file": "addFile",
			"click .js-delete-file":"deleteFile",
			
			"click": function(e) {
				
				if (!$(e.target).hasClass("js-ok")) {
					this.$form.resetValid();
				}
			}
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
				i18nData.title = "修改事项库";
			}
			else{
				i18nData.title = "新增事项库";
			}
			this.setElement(this.template(i18nData));
		},
		
		afterRender: function(){
			
			this.$form = this.$(".js-item-form").form();
			//查询出审批类别列表
			itemMgrAction.qrySpTypeList()
			.then(function(result){
				return result.z_d_r;
			})
			//初始化页面控件
			.then(function(spTypeList){
				this.$form.find("[name='SP_TYPE_ID']").combobox({
					dataTextField: 'VALUE_NAME',
					dataValueField: 'ID',
					editable: false,
					dataSource: spTypeList
				}).combobox("value",this.rowData.SP_TYPE_ID);
				
				//初始化组织机构弹出页
				this.$orgPopEdit = this.$form.find("[name='ORG_ID']").popedit({
					open: function() {
						fish.popupView({
							url: "modules/stafforg/views/OrgSelPopWin",
							close: function(msg) {
								this.$orgPopEdit.popedit("setValue", {
									Value: msg.ORG_ID,
									Text: msg.ORG_NAME
								});
							}.bind(this)
						});
					}.bind(this),
					dataTextField: "Text",
					dataValueField: "Value"
				});
			}.bind(this))
			//如果弹出页面用于编辑则进行赋值操作
			.then(function(){
				if(!this.isUpdate){
					return;
				}
				this.$form.form("value",this.rowData);
				
				this.$form.find("[name='SP_TYPE_ID']").combobox("value",this.rowData.SP_TYPE_ID);
				
				this.$orgPopEdit.popedit("setValue", {
					Value: this.rowData.ORG_ID,
					Text: this.rowData.ORG_NAME
				});
				$.each(this.rowData.ITEM_MATERIALS, function(index, result) {
					$(".FILE_LIST_TABLE tr:last").after(this.rowTpl(result));
				}.bind(this));
				
				var list = this.$("[name='fileFormat']");
				
				$.each(this.rowData.ITEM_MATERIALS, function(index, result) {
					list[index].value = result.FORMAT_ID;
				}.bind(this));
			}.bind(this));
		},
		
		okClick: function(){
			if (!this.$form.isValid()) {
				return;
			}
			
			var formData = this.$form.form("value");
			var fileList = [];
			if(formData.fileName){
				for(var i=0, len=formData.fileName.length;i<len;i++){
					var fileObj = {
						NAME: formData.fileName[i],
						FORMAT_ID:formData.fileFormat[i],
						REQUIREMENT: formData.fileRequirement[i],
						NUM: formData.fileNum[i],
						COMMENTS: formData.fileComments[i],
					};
					fileList.push(fileObj);
				}
				formData.FILE_LIST = fileList;
			}
			
			formData.fileName && delete formData.fileName;
			formData.fileFormat && delete formData.fileFormat;
			formData.fileRequirement && delete formData.fileRequirement;
			formData.fileNum && delete formData.fileNum;
			formData.fileComments && delete formData.fileComments;
			
			if(this.isUpdate){
				formData.ID = this.rowData.ID;
				itemMgrAction.editItem(formData, function(){
					this.popup.close();
				}.bind(this));
			}
			else{
				itemMgrAction.addItem(formData, function(){
					this.popup.close();
				}.bind(this));
			}
			
		},
		
		addFile: function(){
			$(".FILE_LIST_TABLE tr:last").after(this.rowTpl());
		},
		
		deleteFile:function(e){
			var itemId=$(e.target).attr("data-item-id");
			var tr = e.target.parentNode.parentNode;
			tr.parentNode.removeChild(tr);
		}
	});
});