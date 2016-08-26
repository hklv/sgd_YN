define([
	'text!modules/jobmgr/templates/RemType.html',
	'i18n!modules/jobmgr/i18n/jobmgr'
], function(RemTypeTpl, i18nJobMgr) {
	return portal.BaseView.extend({
		template: fish.compile(RemTypeTpl),

		events: {
			"click .js-ok": "ok",
			"click .js-cancel": "cancel"
		},

		initialize: function() {
		},

		render: function() {
			this.setElement(this.template(i18nJobMgr));
		},

		afterRender: function() {
			this.$("form").form();
			this.$("form select").combobox();
		},

		ok: function() {
			if (this.$("form").isValid()) {
				this.popup.close(this.$("form").form('value'));
			}
		}
	});
});