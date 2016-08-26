define([
    "text!modules/bpmn/popwin/goBack/templates/processLinkSelPopWin.html",
    "modules/bpmn/popwin/goBack/actions/processLinkSelAction",
    "i18n!modules/bpmn/popwin/goBack/i18n/processLinkSelMgr",
], function(template, processLinkSelAction, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-ok": "okClick",
            "click .js-cancel": "cancelClick"
        },
        initialize: function(option) {
            this.HOLDER_ID = option.HOLDER_ID;
            this.TASK_LIST_ID = option.TASK_LIST_ID;
        },
        render: function() {
            this.setElement(this.template(i18nData));
        },
        afterRender: function() {
            this.$form = this.$(".js-form").form();

        },
        okClick: function() {
            var data = this.$form.form("value");
            data.HOLDER_ID = this.HOLDER_ID;
            processLinkSelAction.terminateProcess(data);
            this.popup.close(data);
        }

    });
});
