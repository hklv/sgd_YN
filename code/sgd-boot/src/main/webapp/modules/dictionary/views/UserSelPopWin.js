define([
	'modules/jobmgr/actions/JobMgrAction',
	'text!modules/jobmgr/templates/UserSelPopWin.html',
	'i18n!modules/jobmgr/i18n/jobmgr'
], function(JobMgrAction, UserSelPopWinTpl, i18nJobMgr) {
	return portal.BaseView.extend({
		template: fish.compile(UserSelPopWinTpl),

		events: {
			"click .js-ok": 'ok'
		},

		initialize: function(options) {
			this._userInfo = options.USER_INFO;
			this._operRoles = options.OPER_ROLES;
			this._job = options.JOB;

			this.colModel = [{
				name: 'USER_ID',
				label: '',
				key: true,
				hidden: true
			}, {
				name: 'USER_NAME',
				label: i18nJobMgr.JOBMGR_USER_NAME,
				width: "20%",
				search: true
			}, {
				name: 'USER_CODE',
				label: i18nJobMgr.JOBMGR_USER_CODE,
				width: "20%",
				search: true
			}, {
				name: 'STAFF_NAME',
				label: i18nJobMgr.JOBMGR_STAFF_NAME,
				width: "20%",
				search: true
			}, {
				name: 'ORG_NAME',
				label: i18nJobMgr.JOBMGR_ORG_NAME,
				width: "20%",
				search: true
			}, {
				name: 'AREA_NAME',
				label: i18nJobMgr.JOBMGR_AREA_NAME,
				width: "20%",
				search: true
			}];
		},

		render: function() {
			this.setElement(this.template(i18nJobMgr));
		},

		afterRender: function() {
			var $grid = this.$(".grid");

			$grid.jqGrid({
				//height: 345,
				data: this._userInfo,
				colModel: this.colModel,
				multiselect: true
			});
			$grid.prev().children("div").searchbar({target: $grid});
			$grid.jqGrid("setSelection", this._userInfo[0]);

			this.$(".js-oper-roles").append(this._operRoles);
			this.$(".js-job-name").append(this._job.JOB_NAME);
		},

		ok: function() {
			var userList = this.$(".grid").jqGrid("getCheckRows");
			if (userList.length > 0) {
				this.popup.close({
					USER_LIST: userList
				});
			} else {
				fish.info(i18nJobMgr.JOBMGR_PLS_SEL_USER);
			}
		}
	});
});