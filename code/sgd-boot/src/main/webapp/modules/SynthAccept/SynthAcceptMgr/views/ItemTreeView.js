define([
	"text!modules/SynthAccept/SynthAcceptMgr/templates/ItemTreeTemplate.html",
	"i18n!modules/SynthAccept/SynthAcceptMgr/i18n/SynthAccept"
], function(template, i18nData){
	return portal.BaseView.extend({
		className:"container_left",
		template: fish.compile(template),
	
		events: {
			
		},
		
		initialize: function(eventModel){
			this.eventModel = eventModel;
			this.colModel = [{
				name: "ORG_ID",
				key: true,
				hidden: true
			}, {
				name: "ORG_NAME",
				label: "事项名称",
				search: true,
				width: "50%"
			}];
		},
		
		render: function(){
			this.$el.html(this.template(i18nData));
			return this;
		},
		
		afterRender: function(){
			this.$grid = this.$(".js-itemtree-grid").grid({
				colModel: this.colModel,
				treeGrid: true,
				treeIcons: {
				    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
				},
				expandColumn: "ORG_NAME",
				onSelectRow: function(){
					var rowData = this.$grid.grid("getSelection");
					//如果选择的条目类型为事项，则刷新右侧视图
					if(rowData.ORG_TYPE === "I"){
						this.eventModel.trigger("newAcceptView",rowData);	
					}
				}.bind(this)
			});
			this.$(".js-searchbar").searchbar({
				target: this.$grid
			});
			
			this.queryItem();
		},
		
		subResize :function(delta){
			portal.utils.gridIncHeight(this.$(".js-itemtree-grid"), delta);
		},
		
		queryItem: function(){
			portal.callService("QryItemTree",null,function(result){
				var list = result.z_d_r;
				var orgs = portal.utils.getTree(list, "ORG_ID", "PARENT_ORG_ID", null);
				this.$grid.grid("reloadData",orgs);
				//默认打开第一个事项
				var firstItem = _.find(list, function(item){
					return item.ORG_TYPE === "I";
				});
				this.$grid.grid("setSelection", firstItem);
			}.bind(this));
		}
	});
});