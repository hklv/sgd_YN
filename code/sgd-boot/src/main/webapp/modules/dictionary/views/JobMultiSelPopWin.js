define([
	"text!modules/jobmgr/templates/JobMultiSelPopWinTemplate.html",
	"i18n!modules/jobmgr/i18n/jobmgr",
	"modules/jobmgr/actions/JobMgrAction"
], function(jobTemplate, i18nJob, jobAction) {
	return portal.BaseView.extend({
		template: fish.compile(jobTemplate),

		events: {
			"click .js-ok": "okClick",
			"click .js-cancel": "cancelClick"
		},

		initialize: function(options) {
			this.options = options;
			if (this.options.resource) {
				this.resource = fish.extend({}, i18nJob, this.options.resource);
			} else {
				this.resource = fish.extend({}, i18nJob);
			}
		},

		render: function() {
			this.setElement(this.template(this.resource));
		},

		afterRender: function() {
			this.jobGrid = this.$(".js-job-grid").jqGrid({
				autowidth: true,
				colModel: [{
					name: 'JOB_ID',
					label: '',
					key: true,
					hidden: true
				}, {
					name: 'JOB_NAME',
					label: this.resource.JOBMGR_JOB_NAME,
					width: 500,
					search: true
				}],
				multiselect: true
			});
			jobAction.qryAllJobs("A", function(data) {
				if (data && data.JOB_LIST) {
					this.jobGrid.jqGrid("reloadData", data.JOB_LIST);
				}
				if (this.options.selectedJob) {
					var selected = [];
					for (var i in this.options.selectedJob) {
						selected[selected.length] = this.options.selectedJob[i].JOB_ID;
					}
					this.jobGrid.jqGrid("setCheckRows", selected);
				}
			}.bind(this));
			this.$(".js-searchbar").searchbar({
				target: this.jobGrid
			});
		},

		okClick: function() {
			var selectedRows = this.jobGrid.jqGrid("getCheckRows");
			if (!this.options.canSelectedEmpty) { //可以选择空的，默认是不能的
				if (selectedRows && selectedRows.length < 0) {
					fish.warn(this.resource.JOBMGR_MUST_SEL_ROW);
					return;
				}
			}
			this.popup.close(selectedRows);
		}
	});
});