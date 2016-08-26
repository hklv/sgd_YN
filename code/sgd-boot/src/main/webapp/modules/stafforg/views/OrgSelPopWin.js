/** [组织单选的弹出框] */
define([
	"text!modules/stafforg/templates/OrgMultiSelPopWin.html",
	"i18n!modules/stafforg/i18n/stafforg",
	"modules/stafforg/actions/StaffOrgAction"
], function(selTemplate, i18nStaffOrg, staffOrgAction) {
	return portal.BaseView.extend({
		template: fish.compile(selTemplate),

		events: {
			"click .js-ok": "okClick",
			"click .js-cancel": "cancelClick"
		},
		initialize: function(options) {
			this.options = options;
			if (this.options.resource) {
				this.resource = fish.extend({}, i18nStaffOrg, this.options.resource);
			} else {
				this.resource = fish.extend({}, i18nStaffOrg);
			}
			this.selectedOrgs = this.options.selectedOrgs;
		},

		render: function() {
			this.setElement(this.template(this.resource));
		},

		afterRender: function() {
			var $grid = this.orgTree = this.$(".js-org-grid").jqGrid({
				autowidth: true,
				colModel: [{
					name: 'ORG_ID',
					label: '',
					key: true,
					hidden: true
				}, {
					name: 'ORG_NAME',
					label: this.resource.STAFFORG_ORG_NAME,
					width: 200,
					search: true
				}, {
					name: 'ORG_CODE',
					label: this.resource.STAFFORG_ORG_CODE,
					width: 200,
					search: true
				}],
				treeGrid: true,
				treeIcons: {
					plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
				},
				ExpandColumn: "ORG_NAME",
				multiselect: false
			});
			this.$(".js-searchbar").searchbar({
				target: this.orgTree
			});

			this.$("form").addClass("invisible");

			staffOrgAction.qryRootOrgListByStaffId(function(data) {
				var root = data;
				staffOrgAction.qryStaffMasterOrgList(function(data) {
					var subList = data.ORG_LIST;
					for (var i = 0; i < root.length; i++) {
						root[i].expanded = true;
					}
					for (var i = 0; i < subList.length; i++) {
						subList[i].expanded = true;
					}
					for (var rootKey in root) {
						var rootItem = root[rootKey];
						for (var subKey in subList) {
							var subItem = subList[subKey];
							if (rootItem.ORG_ID == subItem.ORG_ID) {
								subItem.PARENT_ORG_ID = null;
								break;
							}
						}
					}
					var orgs = portal.utils.getTree(subList, "ORG_ID", "PARENT_ORG_ID", null);
					this.orgTree.jqGrid("reloadData", orgs);
					if (this.selectedOrgs && this.selectedOrgs.length > 0) {
						var selectedOrgIds = [];
						for (var i = 0; i < this.selectedOrgs.length; i++) {
							selectedOrgIds[i] = this.selectedOrgs[i].ORG_ID;
						}
						this.orgTree.jqGrid("setCheckRows", selectedOrgIds, true);
						this.orgTree.jqGrid("setCheckDisabled", selectedOrgIds, true);
					}
					if (orgs && orgs.length > 0) {
						this.orgTree.jqGrid("setSelection", 0).jqGrid("expandNode", this.orgTree.jqGrid("getSelection"));
					}
				}.bind(this));
			}.bind(this));
		},

		okClick: function() {
			var selOrgs = this.orgTree.jqGrid("getSelection");
			if($.isEmptyObject(selOrgs)){
				fish.warn(this.resource.STAFFORG_ORG_MUL_SEL_NOT_EMPYT);
				return;
			}
			this.popup.close(selOrgs);
		}
	});
});