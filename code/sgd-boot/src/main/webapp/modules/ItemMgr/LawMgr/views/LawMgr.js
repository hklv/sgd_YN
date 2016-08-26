define([
	"text!modules/ItemMgr/LawMgr/templates/LawMgr.html",
	"text!modules/common/templates/GridCellRUDTemplate.html",
	"i18n!modules/ItemMgr/LawMgr/i18n/LawMgr",
	"modules/ItemMgr/LawMgr/actions/LawMgrAction"
], function(template, cellTemplate, i18nData, lawMgrAction){
	return portal.BaseView.extend({
		template: fish.compile(template),
		cellTemplate: fish.compile(cellTemplate),
		
		events: {
			"click #btnAddLaw": "addLaw",
			"click .js-query": 'queryLaw',
			"click .glyphicon-film": 'showDetail',
			"click .glyphicon-edit": 'editLaw',
			"click .glyphicon-remove": 'deleteLaw',
			
			"click": function(e) {
				
				if (!$(e.target).hasClass("js-query")) {
					this.$qryForm.resetValid();
				}
			}
		},
		
		initialize: function(){
			this.colModel = [{
				name: 'ID',
				label: '',
				key: true,
				hidden: true
			}, {
				name: 'NAME',
				label: "法律法规名称",
				width: "20%"
			}, {
				name: "TYPE_NAME",
				label: "类型",
				width: "10%"
			}, {
				name: "FILE_NO",
				label: "文号",
				width: "10%"
			}, {
				name: "SCOPE",
				label: "适用范围",
				width: "10%"
			}, {
				name: "EXE_DATE",
				label: "实施日期",
				width: "15%"
			}, {
				name: "PUB_DEPARTMENT",
				label: "颁布单位",
				width: "10%"
			}, {
				name: "PUB_DATE",
				label: "颁布日期",
				width: "15%"
			}, {
				width: "10%",
				sortable: false,
				formatter: function() {
					return this.cellTemplateHTML;
				}.bind(this)
			}];
		},
		
		render: function(){
			this.$el.html(this.template(i18nData));
			this.cellTemplateHTML = this.cellTemplate(i18nData);
			return this;
		},

		afterRender: function(){
			this.$lawGrid = this.$(".js-law-grid").jqGrid({
				colModel: this.colModel,
				pager: true,
				datatype: 'json',
				pageData: function() {this.loadGrid(false);}.bind(this),
				onDblClickRow: function(e, rowid, iRow, iCol ){
					this.showDetail();
				}.bind(this)
			});
			
			this.$qryForm = this.$(".js-form").form();
			
			this.$qryForm.find("[name='EXE_DATE_BEGIN']").datetimepicker({viewType:"date", format:"yyyy-mm-dd"});
			this.$qryForm.find("[name='EXE_DATE_END']").datetimepicker({viewType:"date", format:"yyyy-mm-dd"});
			
			
			this.$(".js-state").combobox({
				dataTextField: 'name',
				dataValueField: 'value',
				dataSource: [{
					name: "有效",
					value: 'A'
				}, {
					name: "无效",
					value: 'X'
				}]
			}).combobox('value', 'A');
			
			//初始化法律法规类型列表
			lawMgrAction.qryLawTypeList(function(result){
				this.lawTypeList = result.z_d_r;
				this.$qryForm.find("[name='TYPE_ID']").combobox({
					dataTextField: 'VALUE_NAME',
					dataValueField: 'ID',
					editable: false,
					dataSource: this.lawTypeList
				});
			}.bind(this));
			
			this.$lawGrid.grid("navButtonAdd",[{
                caption: "新增法规库",
                id: "btnAddLaw"
            }]);
            
            this.queryLaw();
		},
		
		queryLaw: function(){
			 if (!this.$qryForm.isValid()) {
				return;
			}
			var data = this.$qryForm.form("value");
			
			!data.NAME && delete data.NAME;
			!data.TYPE_ID && delete data.TYPE_ID;
			!data.EXE_DATE_BEGIN && delete data.EXE_DATE_BEGIN;
			!data.EXE_DATE_END && delete data.EXE_DATE_END;
			!data.FILE_NO && delete data.FILE_NO;
			
			this.qryCond = data;
			this.loadGrid(true);
			 
		},
		
		loadGrid: function(reset) {
			lawMgrAction.qryLawCount(this.qryCond, function(count) {
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
				
				lawMgrAction.qryLawList(this.qryCond, filter,
					function(status) {
						var dataList = status.z_d_r || [];
						//去掉日期的时分秒
						$.each(dataList, function(index, item) {
							item.PUB_DATE = fish.dateutil.format(new Date(item.PUB_DATE), 'yyyy-mm-dd');
							item.EXE_DATE = fish.dateutil.format(new Date(item.EXE_DATE), 'yyyy-mm-dd');
						});
						this.$lawGrid.grid("reloadData", {
							'rows': dataList,
							'page': page,
							'records': count
						});
						if (dataList.length > 0) {
							this.$lawGrid.grid("setSelection", dataList[0]);
						} else {
							fish.info(i18nData.HINT_SEARCH_MATCH_NULL);
						}
					}.bind(this)
				);
			}.bind(this));
		},

		addLaw: function(){
			fish.popupView({
				url: "modules/ItemMgr/LawMgr/views/AddOrEditLawPopWin",
				close: function(){
					this.loadGrid(false);
				}.bind(this)
			});
		},
		
		editLaw: function(){
			var rowData = this.$lawGrid.grid("getSelection");		
			fish.popupView({
				url: "modules/ItemMgr/LawMgr/views/AddOrEditLawPopWin",
				viewOption: rowData,
				close: function(msg) {
					this.loadGrid(false);
				}.bind(this)
			});
		},
		
		deleteLaw: function(){
			fish.confirm("确认删除该法规?",function() {
				var rowData = this.$lawGrid.grid("getSelection");
				var param = {ID: rowData.ID};
				lawMgrAction.deleteLaw(param, function(){
					this.loadGrid(false);
				}.bind(this));
			}.bind(this));
		},
		
		showDetail: function(){
			var rowData = this.$lawGrid.grid("getSelection");
			fish.popupView({
				url: "modules/ItemMgr/LawMgr/views/LawDetailPopWin",
				viewOption: rowData,
				close: function() {
					
				}.bind(this)
			});
		},
		
		resize: function(delta) {
			portal.utils.gridIncHeight(this.$lawGrid, delta);
		}
	});
});