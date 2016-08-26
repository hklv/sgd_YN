define([
    "text!modules/bpmn/flowdesigner/templates/FlowCatgTemplate.html",
    "modules/bpmn/flowdesigner/actions/flowdesignerAction",
    "i18n!modules/bpmn/flowdesigner/i18n/flowdesignerMgr",
], function(template, action, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
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
        }
    });
});
