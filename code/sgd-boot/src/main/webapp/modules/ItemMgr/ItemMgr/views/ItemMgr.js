define([
	"text!modules/ItemMgr/ItemMgr/templates/ItemMgr.html",
	"text!modules/common/templates/GridCellRUDTemplate.html",
	"i18n!modules/ItemMgr/ItemMgr/i18n/ItemMgr",
	"modules/ItemMgr/ItemMgr/actions/ItemMgrAction"
], function(template, cellTemplate, i18nData, itemMgrAction){
	return portal.BaseView.extend({
		template: fish.compile(template),
		cellTemplate: fish.compile(cellTemplate),
		
		events:{
			"click .js-query": "qryItem",
			"click 	#btnAddItem": "addItem",
			"click .glyphicon-film": 'showDetail',
			"click .glyphicon-edit": 'editItem',
			"click .glyphicon-remove": 'deleteItem',
			"click 	#btnAddTest": "addItemTest",
			"click"	: "handleErrMsg"
		},
		
		initialize: function(){
			this.colModel = [{
				name: 'ID',
				key: true,
				hidden: true
			}, {
				name: "NAME",
				label: "事项名称",
				width: "20%"
			}, {
				name: "ITEM_CODE",
				label: "事项编码",
				width: "10%"
			}, {
				name: "SP_TYPE_NAME",
				label: "审批类别",
				width: "10%"
			}, {
				name: "ORG_NAME",
				label: "所属部门",
				width: "15%"
			}, {
				name: "SP_OBJECT",
				label: "审批对象",
				width: "15%"
			}, {
				name: "LEGAL_PERIOD",
				label: "法定期限",
				width: "10%"
			}, {
				name: "PROMISE_PERIOD",
				label: "承诺期限",
				width: "10%"
			}, {
				width: "10%",
				formatter: function() {
					return this.cellTemplateHtml;
				}.bind(this)
			}];
		},
		
		render: function(){
			this.$el.html(this.template(i18nData));
			this.cellTemplateHtml = this.cellTemplate(i18nData);
			return this;
		},
		
		afterRender: function(){
			this.$itemGrid = this.$(".js-item-grid").jqGrid({
				colModel: this.colModel,
				pager: true,
				datatype: 'json',
				onDblClickRow: function(e, rowid, iRow, iCol ){
					this.showDetail();
				}.bind(this),
				pageData: function() {
					this.loadGrid(false);
				}.bind(this)
			});
			
			this.$qryForm = this.$(".js-form").form();
			//初始化审批类别列表
			itemMgrAction.qrySpTypeList().then(function(result){
				this.spTypeList = result.z_d_r;
				this.$qryForm.find("[name='SP_TYPE_ID']").combobox({
					dataTextField: 'VALUE_NAME',
					dataValueField: 'ID',
					editable: false,
					dataSource: this.spTypeList
				});
			}.bind(this));
			
			//初始化组织机构弹出页
			this.$orgPopEdit = this.$qryForm.find("[name='ORG_ID']").popedit({
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
			
			this.$itemGrid.grid("navButtonAdd",[{
                caption: "新增事项",
                id: "btnAddItem"
            },{
                caption: "供彩云测试",
                id: "btnAddTest"
            }]);
            
            this.qryItem();
		},
		
		resize: function(delta) {
			portal.utils.gridIncHeight(this.$itemGrid, delta);
		},
		
		handleErrMsg: function(e) {
			if (!$(e.target).hasClass("js-query")) {
				this.$qryForm.resetValid();
			}
		},
		
		qryItem: function(){
			if (!this.$qryForm.isValid()) {
				return;
			}
			var data = this.$qryForm.form("value");
			
			!data.NAME && delete data.NAME;
			!data.ITEM_CODE && delete data.ITEM_CODE;
			!data.SP_TYPE_ID && delete data.SP_TYPE_ID;
			!data.ORG_ID && delete data.ORG_ID;
			
			this.qryCond = data;
			this.loadGrid(true);
		},
		
		loadGrid: function(reset) {
			itemMgrAction.qryItemCount(this.qryCond, function(count) {
				var count = Number(count.CNT),
				rowNum = this.$(".grid").grid("getGridParam", "rowNum"),
				page = reset ? 1 : this.$(".grid").grid("getGridParam", "page"),
				sortname = this.$(".grid").grid("getGridParam", "sortname"),
				sortorder = this.$(".grid").grid("getGridParam", "sortorder");
				
				var filter = {
					PageIndex: page-1,
					PageLen: rowNum
				};
				if(sortname){
					filter.OrderFields = sortname + " " + sortorder;
				}
				
				itemMgrAction.qryItemList(this.qryCond, filter,
					function(status) {
						var dataList = status.z_d_r || [];
						this.$itemGrid.grid("reloadData", {
							'rows': dataList,
							'page': page,
							'records': count
						});
						if (dataList.length > 0) {
							this.$itemGrid.grid("setSelection", dataList[0]);
						} else {
							fish.info(i18nData.HINT_SEARCH_MATCH_NULL);
						}
					}.bind(this)
				);
			}.bind(this));
		},
		
		showDetail:function(){
			var rowData = this.$itemGrid.grid("getSelection");
			fish.popupView({
				url: "modules/ItemMgr/ItemMgr/views/ItemDetailPopWin",
				viewOption: rowData,
				close: function(msg) {
					
				}.bind(this)
			});
		},
		
		addItem: function(){
			fish.popupView({
				url: "modules/ItemMgr/ItemMgr/views/AddOrEditItemPopWin",
				close: function(msg) {
					this.loadGrid(false);
				}.bind(this)
			});
		},
		
		editItem: function(){
			var rowData = this.$itemGrid.grid("getSelection");		
			fish.popupView({
				url: "modules/ItemMgr/ItemMgr/views/AddOrEditItemPopWin",
				viewOption: rowData,
				close: function(msg) {
					this.loadGrid(false);
				}.bind(this)
			});
		},
		
		deleteItem: function(){
			fish.confirm("确认删除该事项?",function() {
				var rowData = this.$itemGrid.grid("getSelection");
				var param = {ID: rowData.ID};
				itemMgrAction.deleteItem(param, function(){
					this.loadGrid(false);
				}.bind(this));
			}.bind(this));
		},
		
		addItemTest: function(){
			fish.popupView({
				url: "modules/ItemMgr/ItemMgr/views/AddItemPopWinTest",
				close: function(msg) {
					
				}.bind(this)
			});
		}
	});
	
})