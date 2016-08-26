define([
    "text!modules/bpmn/flowdesigner/templates/ProcDefTemplate.html",
    "modules/bpmn/flowdesigner/actions/flowdesignerAction",
    "i18n!modules/bpmn/flowdesigner/i18n/flowdesignerMgr"
], function(template, action, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-form-new": 'newForm'
        },
        initialize: function(option) {
            this.rowData = option.data;           
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$formDetailForm = $(".js-form-detail").form();
            this.$formDetailForm.form('value', this.rowData).form("disable");
        },
        newForm: function(event) {
            var parentView = this.parentView;
            parentView.requireView({
                url: parentView.viewUrl[2],
                selector: ".js-form-detail",
                viewOption: { data: this.rowData, NEW: 'Y' }
            });
        }
    });
});
