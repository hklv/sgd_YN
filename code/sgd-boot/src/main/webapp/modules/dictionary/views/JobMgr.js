/**
 * Title: JobMgr.js
 * Description: Job Management View
 * Author: wu.yangjin
 * Created Date: 15-5-14 上午11:01
 * Copyright: Copyright 2015,+INF ZTESOFT, Inc.
 */
define([
	'text!modules/jobmgr/templates/JobMgr.html',
	'modules/jobmgr/actions/JobMgrAction',
	'i18n!modules/jobmgr/i18n/jobmgr'
], function(jobMgrTpl, JobMgrAction, i18nJobMgr) {
	return portal.BaseView.extend({
		i18nData: fish.extend({}, i18nJobMgr),

		template: fish.compile(jobMgrTpl),

		events: {
			"click .js-jrt-grid .js-add": 'addJobRole',
			"click #tabs-active-jobs .grid .inline-disable": 'disableJob',
			"click #tabs-inactive-jobs .grid .inline-enable": 'enableJob'
		},

		initialize: function() {
		},

		render: function() {
			this.$el.html(this.template(i18nJobMgr));
            return this;
		},

		afterRender: function() {
			var $grid = this.$(".js-jrt-grid");

			$grid.jqGrid({
				colModel: [{
					name: 'ROLE_ID',
					label: '',
					key: true,
					hidden: true
				}, {
					name: 'ROLE_NAME',
					label: i18nJobMgr.JOBMGR_ROLE_NAME,
					width: "20%",
					editable: true,
					search: true
				}, {
					name: 'ROLE_CODE',
					label: i18nJobMgr.JOBMGR_ROLE_CODE,
					width: "20%",
					search: true
				}, {
					name: 'IS_LOCKED_STR',
					label: i18nJobMgr.JOBMGR_IS_LOCKED,
					width: "10%",
					align: 'center'
				}, {
					name: 'COMMENTS',
					label: i18nJobMgr.JOBMGR_REMARKS,
					search: true,
					width: "40%"
				}, {
					sortable: false,
					width: "10%",
					formatter: 'actions',
					formatoptions: {
						editbutton: false,
						delbutton: true
					}
				}],
				// toolbar: [true, 'bottom'],
				pagebar: true,
				beforeDeleteRow: function(e, rowid, rowdata) {
					JobMgrAction.qryUserInfoByJobId(this._jobdata.JOB_ID,
						function(status) {
							var userList = status.INFO_LIST || [];
							if (userList.length > 0) {
								this.remRoleFromJobAndUsers(userList, rowdata);
							} else {
								fish.confirm(i18nJobMgr.JOBMGR_DEL_ROLE_FROM_JOB_CONFIRM,function() {
										this.remRoleFromJob(rowdata);
									}.bind(this), $.noop);
							}
						}.bind(this)
					);
					return false;
				}.bind(this)
			});
			$grid.grid("navButtonAdd",[{
                caption: i18nJobMgr.COMMON_ADD,
                cssprop: "js-add"
            }]);
			$grid.prev().children("div").searchbar({target: $grid});

			var jobActive = [];
			var jobInactive = [];
			this.$(".js-job-tab").tabs({
				//activateOnce:true,
				activate: function(event, ui) {
					var id = ui.newPanel.attr('id'),
						$grid = this.$("#" + id + " .grid");
					this.$(".js-jrt-grid").jqGrid("clearData");
					switch (id) {
						case "tabs-active-jobs":
							this._jobdata && jobInactive.push(this._jobdata);
							if ($grid.hasClass("ui-jqgrid")) {
								if (jobActive.length > 0) {
									$grid.jqGrid("setSelection", jobActive.pop());
								}
							} else {
								this.initJqGridActive($grid);
								this.loadDataActive($grid);
							}
							break;
						case "tabs-inactive-jobs":
							this._jobdata && jobActive.push(this._jobdata);
							if ($grid.hasClass("ui-jqgrid")) {
								if (jobInactive.length > 0) {
									$grid.jqGrid("setSelection", jobInactive.pop());
								}
							} else {
								this.initJqGridInactive($grid);
								this.loadDataInactive($grid);
							}
							break;
					}
				}.bind(this)
			});
		},

		remRoleFromJob: function(rowdata) {
			var $grid = this.$(".js-jrt-grid");
			JobMgrAction.delRoleFromJob(this._jobdata.JOB_ID, rowdata.ROLE_ID,
				function(data) {
					this.seekBeforeRemRow($grid, rowdata);
					$grid.jqGrid('delRowData', rowdata);
					fish.success(i18nJobMgr.JOBMGR_DEL_ROLE_FROM_JOB_SUCCESS);
				}.bind(this)
			);
		},

		remRoleFromJobAndUsers0: function(userList, rowdata) {
			var $grid = this.$(".js-jrt-grid");
			JobMgrAction.delRoleFromJobAndUsers(this._jobdata.JOB_ID,
				rowdata.ROLE_ID,
				fish.pluck(userList, 'USER_ID'),
				function(status) {
					this.seekBeforeRemRow($grid, rowdata);
					$grid.jqGrid("delRowData", rowdata);
					fish.success(i18nJobMgr.JOBMGR_DEL_ROLE_FROM_JOB_USER_SUCCESS);
				}.bind(this)
			);
		},

		remRoleFromJobAndUsers: function(userList, rowdata) {
			fish.popupView({
				url: "modules/jobmgr/views/RemType",
				close: function(msg) {
					switch (msg.REM_TYPE) {
					case 'T':
						this.remRoleFromJob(rowdata);
						break;
					case 'U':
						fish.popupView({
							url: "modules/jobmgr/views/UserSelPopWin",
							viewOption: {
								JOB: this._jobdata,
								OPER_ROLES: rowdata.ROLE_NAME + ";",
								USER_INFO: userList
							},
							close: function(msg) {
								var userList = msg.USER_LIST;
								this.remRoleFromJobAndUsers0(userList, rowdata);
							}.bind(this)
						});
						break;
					default:
						break;
					}
				}.bind(this)
			});
		},

		initJqGridActive: function($grid, gridHeight) {
			$grid.jqGrid({
			    // height: gridHeight,
				colModel: [{
					name: 'DICTIONARY_ID',
					label: '',
					key: true,
					hidden: true
				}, {
					name: 'NAME',
					label: "字典类别名称",
					editable: true,
					search: true,
					width: "50%",
					editrules: "字典类别名称" + ":required;length[1~255, true]"
				}, {
					name: 'CODE',
					label: "字典类别编码",
					width: "30%",
					search: false,
					formatter: "select",
				}, {
					name: 'COMMENTS',
					label: "备注",
					editable: true,
					search: true,
					width: "50%",
					editrules: "备注" + ":required;length[1~255, true]"
				},{
					sortable: false,
					width: "20%",
					formatter: 'actions',
					formatoptions: {
						editbutton: true,
						delbutton: false,
						inlineButtonAdd: [{
							id: "job-disable",
							className: "inline-disable",
							icon: "fa fa-ban",
							title: i18nJobMgr.COMMON_DISABLE
	                    }]
					}
				}],
				onSelectRow: this.selectRowActive.bind(this),
				// toolbar: [true, 'bottom'],
				pagebar: true,
				afterAddRow: function(e, rowid, rowdata, option) {
					$grid.find("#job-disable_" + rowid).hide();
				}.bind(this),
				afterEditRow: function(e, rowid, rowdata, option) {
					$grid.find("#job-disable_" + rowid).hide();
				}.bind(this),
				beforeSaveRow: function(e, rowid, rowdata, option) {
					switch (option.oper) {
					case 'edit':
						JobMgrAction.modJob(rowid, rowdata.JOB_NAME, function(data) {
							$grid.grid("saveRow", rowid, {trigger: false});
							//$grid.jqGrid("setRowData", data);
							$grid.find("#job-disable_" + rowid).show();
							fish.success(i18nJobMgr.JOBMGR_MOD_JOB_SUCCESS);
						}.bind(this));
						break;
					case 'add':
						JobMgrAction.addJob(rowdata.JOB_NAME, 'A', function(status) {
							var job = portal.utils.filterUpperCaseKey(status);
							$grid.jqGrid("saveRow", rowid, {trigger: false});
							$grid.jqGrid('delRow', rowid, {trigger: false});
							$grid.jqGrid('addRowData', job, 'last');
							$grid.jqGrid('setSelection', job);
							fish.success(i18nJobMgr.JOBMGR_ADD_JOB_SUCCESS);
						}.bind(this));
						break;
					default:
						break;
					}
					return false;
				}.bind(this),
				afterRestoreRow: function(e, rowid, data, options) {
					var rowdata = $grid.jqGrid("getRowData", rowid);
					var prevRow = null;
					switch (options.oper) {
					case 'add':
						prevRow = $grid.jqGrid("getPrevSelection", rowdata);
						if (prevRow) {
							setTimeout(function() {
								$grid.jqGrid("setSelection", prevRow);
							}, 0);
						}
						break;
					case 'edit':
						$grid.find("#job-disable_" + rowid).show();
						return;
						break;
					default:
						break;
					}
				}.bind(this)
			});
			$grid.grid("navButtonAdd",[{
                caption: i18nJobMgr.COMMON_ADD,
                cssprop: "js-new",
                onClick: function(e) {
                	$grid.jqGrid("addRow", {
                		initdata: {
                			JOB_NAME: "",
                			STATE: "A",
                			inadd: true
                		},
                		position: "last"
                	});
				}
            }]);
			$grid.prev().children("div").searchbar({target: $grid});
		},

		initJqGridInactive: function($grid, gridHeight) {
			$grid.jqGrid({
			    // height: gridHeight,
				colModel: [{
					name: 'JOB_ID',
					label: '',
					key: true,
					hidden: true
				}, {
					name: 'JOB_NAME',
					label: i18nJobMgr.JOBMGR_JOB_NAME,
					editable: true,
					search: true,
					width: "50%",
					editrules: i18nJobMgr.JOBMGR_JOB_NAME + ":required;length[1~255, true]"
				}, {
					name: 'STATE',
					label: i18nJobMgr.JOBMGR_JOB_STATE,
					width: "30%",
					search: false,
					formatter: "select",
					formatoptions: {
						value: {
							'A': i18nJobMgr.COMMON_ACTIVE,
							'X': i18nJobMgr.COMMON_INACTIVE
						}
					}
				}, {
					sortable: false,
					width: "20%",
					formatter: 'actions',
					formatoptions: {
						editbutton: true,
						delbutton: false,
						inlineButtonAdd: [{
							id: "job-enable",
							className: "inline-enable",
							icon: "fa fa-circle-o",
							title: i18nJobMgr.COMMON_ENABLE
	                    }]
					}
				}],
				onSelectRow: this.selectRowInactive.bind(this),
				// toolbar: [true, 'bottom'],
				pagebar: true,
				afterAddRow: function(e, rowid, rowdata, option) {
					$grid.find("#job-enable_" + rowid).hide();
				}.bind(this),
				afterEditRow: function(e, rowid, rowdata, option) {
					$grid.find("#job-enable_" + rowid).hide();
				}.bind(this),
				beforeSaveRow: function(e, rowid, rowdata, option) {
					switch (option.oper) {
					case 'edit':
						JobMgrAction.modJob(rowid, rowdata.JOB_NAME, function(data) {
							$grid.grid("saveRow", rowid, {trigger: false});
							$grid.find("#job-enable_" + rowid).show();
							//$grid.jqGrid("setRowData", data);
							fish.success(i18nJobMgr.JOBMGR_MOD_JOB_SUCCESS);
						}.bind(this));
						break;
					case 'add':
						JobMgrAction.addJob(rowdata.JOB_NAME, 'X', function(status) {
							var job = portal.utils.filterUpperCaseKey(status);
							$grid.jqGrid("saveRow", rowid, {trigger: false});
							$grid.jqGrid('delRow', rowid, {trigger: false});
							$grid.jqGrid('addRowData', job, 'last');
							$grid.jqGrid('setSelection', job);
							fish.success(i18nJobMgr.JOBMGR_ADD_JOB_SUCCESS);
						}.bind(this));
						break;
					default:
						break;
					}
					return false;
				}.bind(this),
				afterRestoreRow: function(e, rowid, data, options) {
					var rowdata = $grid.jqGrid("getRowData", rowid);
					var prevRow = null;
					switch (options.oper) {
					case 'add':
						prevRow = $grid.jqGrid("getPrevSelection", rowdata);
						if (prevRow) {
							setTimeout(function() {
								$grid.jqGrid("setSelection", prevRow);
							}, 0);
						}
						break;
					case 'edit':
						$grid.find("#job-enable_" + rowid).show();
						return;
						break;
					default:
						break;
					}
				}.bind(this)
			});
			$grid.grid("navButtonAdd",[{
                caption: i18nJobMgr.COMMON_ADD,
                cssprop: "js-new",
                onClick: function(e) {
                	$grid.jqGrid("addRow", {
                		initdata: {
                			JOB_NAME: "",
                			STATE: "X",
                			inadd: true
                		},
                		position: "last"
                	});
				}
            }]);
			$grid.prev().children("div").searchbar({target: $grid});
		},
		selectRowInactive: function(e, rowid, state) {
			var that = this;
			var rowdata = that.$(".grid_inactive").jqGrid('getSelection');
			if (rowdata.inadd) {
				return;
			}
			this._jobdata = rowdata;
			JobMgrAction.qryRoles(rowdata.JOB_ID, 'A', function(data) {
				var roleList = data.ROLE_LIST || [],
					$grid = that.$(".js-jrt-grid");
				fish.forEach(roleList, function(role) {
					role.IS_LOCKED_STR = that.getBoolStr(role.IS_LOCKED);
				}, this);
				$grid.jqGrid('reloadData', roleList);
				if (roleList.length > 0) {
					$grid.jqGrid('setSelection', roleList[0]);
				}
			});
		},
		selectRowActive: function(e, rowid, state) {
			var that = this;
			var rowdata = that.$(".grid_active").jqGrid('getSelection');
			if (rowdata.inadd) {
				return;
			}
			this._jobdata = rowdata;
			JobMgrAction.qryRoles(rowdata.JOB_ID, 'A', function(data) {
				var roleList = data.ROLE_LIST || [],
					$grid = that.$(".js-jrt-grid");
				fish.forEach(roleList, function(role) {
					role.IS_LOCKED_STR = that.getBoolStr(role.IS_LOCKED);
				}, this);
				$grid.jqGrid('reloadData', roleList);
				if (roleList.length > 0) {
					$grid.jqGrid('setSelection', roleList[0]);
				}
			});
		},
		seekBeforeRemRow: function($grid, rowdata) {
			var nextrow = $grid.jqGrid("getNextSelection", rowdata),
				prevrow = $grid.jqGrid("getPrevSelection", rowdata);
			if (nextrow) {
				$grid.jqGrid("setSelection", nextrow);
			} else if (prevrow) {
				$grid.jqGrid("setSelection", prevrow);
			} else {
				this._jobdata = null;
			}
		},

		disableJob: function(e) {
			fish.confirm(i18nJobMgr.JOBMGR_DISABLE_JOB_CONFIRM,function() {
					var $grid = this.$("#tabs-active-jobs .grid"),
						rowid = $(e.target).closest("tr.jqgrow").attr("id"),
						rowdata = $grid.jqGrid("getRowData", rowid);
					JobMgrAction.disableJob(rowdata.JOB_ID,
						function() {
							this.seekBeforeRemRow($grid, rowdata);
							$grid.jqGrid("delRowData", rowdata, {trigger: false});
							fish.success(i18nJobMgr.JOBMGR_DISABLE_JOB_SUCCESS);
						}.bind(this)
					);
				}.bind(this), $.noop);
		},

		enableJob: function(e) {
			fish.confirm(i18nJobMgr.JOBMGR_ENABLE_JOB_CONFIRM,function() {
					var $grid = this.$("#tabs-inactive-jobs .grid"),
						rowid = $(e.target).closest("tr.jqgrow").attr("id"),
						rowdata = $grid.jqGrid("getRowData", rowid);
					JobMgrAction.enableJob(rowdata.JOB_ID,
						function() {
							this.seekBeforeRemRow($grid, rowdata);
							$grid.jqGrid("delRowData", rowdata, {trigger: false});
							fish.success(i18nJobMgr.JOBMGR_ENABLE_JOB_SUCCESS);
						}.bind(this)
					);
				}.bind(this), $.noop);
		},

		getBoolStr: function(bool) {
			if (bool === 'Y') {
				return i18nJobMgr.COMMON_YES;
			} else if (bool === 'N') {
				return i18nJobMgr.COMMON_NO;
			} else {
				return '';
			}
		},

		loadDataActive: function($grid) {
			JobMgrAction.qryAllJobs('A', function(data) {
				var jobList = data.JOB_LIST || [];
				$grid.jqGrid("reloadData", jobList);
				if ($grid.jqGrid("getRowData").length > 0) {
					$grid.jqGrid("setSelection", jobList[0]);
				}
			}.bind(this));
		},

		loadDataInactive: function($grid) {
			JobMgrAction.qryAllJobs('X', function(data) {
				var jobList = data.JOB_LIST || [];
				$grid.jqGrid("reloadData", jobList);
				if ($grid.jqGrid("getRowData").length > 0) {
					$grid.jqGrid("setSelection", jobList[0]);
				}
			}.bind(this));
		},

		addJobRole: function() {
			fish.popupView({
				url: "modules/jobmgr/views/RoleSelPopWin",
				viewOption: {
					JOB: this._jobdata
				},
				callback: function(popup, view) {
					this.qryJobUnmountedRoles(function(roles) {
						fish.forEach(roles, function(role) {
							role.IS_LOCKED_STR = this.getBoolStr(role.IS_LOCKED);
						}, this);
						view.loadGrid(roles);
					});
				}.bind(this),
				close: function(msg) {
					this.okSelRole(msg);
				}.bind(this)
			});
		},

		okSelRole: function(msg) {
			var $grid = this.$(".js-jrt-grid"),
				roleList = msg.ROLE_LIST;
			if (msg.ADD_TO_USER) {
				var operRoles = "";
				fish.forEach(roleList, function(role) {
					operRoles += role.ROLE_NAME + "; "
				});
				fish.popupView({
					url: "modules/jobmgr/views/UserSelPopWin",
					viewOption: {
						JOB: this._jobdata,
						OPER_ROLES: operRoles,
						USER_INFO: msg.USER_INFO
					},
					close: function(msg) {
						var userList = msg.USER_LIST;
						JobMgrAction.addRoles2JobAndUsers(this._jobdata.JOB_ID,
							fish.pluck(roleList, 'ROLE_ID'),
							fish.pluck(userList, 'USER_ID'),
							function(status) {
								$grid.jqGrid("addRowData", roleList, 'last');
								$grid.jqGrid("setSelection", roleList[0]);
								fish.success(i18nJobMgr.JOBMGR_ADD_ROLE_TO_JOB_USER_SUCCESS);
							}.bind(this)
						);
					}.bind(this)
				});
			} else {
				JobMgrAction.addRoles2Job(this._jobdata.JOB_ID,
					fish.pluck(roleList, 'ROLE_ID'), function(status) {
						$grid.jqGrid("addRowData", roleList, 'last');
						$grid.jqGrid("setSelection", roleList[0]);
						fish.success(i18nJobMgr.JOBMGR_ADD_ROLE_TO_JOB_SUCCESS);
					}.bind(this)
				);
			}
		},

		qryJobUnmountedRoles: function(success) {
			JobMgrAction.qryAllRoles(function(roles) {
				var jobId = this._jobdata.JOB_ID,
					state = "A";
				JobMgrAction.qryRoles(jobId, state, function(data) {
					var mounted = data.ROLE_LIST;
					success.call(this, fish.filter(roles, function(role) {
						return fish.where(mounted, {
							'ROLE_ID': Number(role.ROLE_ID)
						}).length === 0;
					}));
				}.bind(this));
			}.bind(this));
		},

		resize: function(delta) {
			if (this.$(".container_left").height() >= this.$(".container_right").height()) {
				portal.utils.gridIncHeight(this.$(".js-job-tab .grid:visible"), delta);
				portal.utils.gridIncHeight(this.$(".js-jrt-grid"), this.$(".container_left").height() - this.$(".container_right").height());
			} else {
				portal.utils.gridIncHeight(this.$(".js-jrt-grid"), delta);
				portal.utils.gridIncHeight(this.$('.js-job-tab .grid:visible'), this.$(".container_right").height() - this.$(".container_left").height());
			}
			//有tabspanel需要限定tabspanel的高度
//			var subHeight = this.$(".container_right").height() - this.$(".js-job-tab > .ui-tabs-nav").outerHeight();
//			this.$(".js-job-tab > .ui-tabs-panel").outerHeight(subHeight);
			//tabspanel内的grid做自适应
//			this.$(".grid:visible").jqGrid("setGridHeight", this.$(".js-job-tab > .ui-tabs-panel").height()-this.$(".js-job-tab > .ui-tabs-panel >.search-bar").outerHeight() );
		}
	});
});