define([
    "text!modules/bpmn/taskcenter/taskcenter/templates/taskcenterMgrTemplate.html",
    "i18n!modules/bpmn/taskcenter/taskcenter/i18n/taskcenterMgr",
], function(template, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        render: function() {
            this.$el.html(this.template(i18nData));
        },
        afterRender: function() {
            this.$(".js-taskcenter-tab").tabs({
                activateOnce: true,
                activate: function(event, ui) {
                    return this.tabActivate(event, ui);
                }.bind(this)
            });
        },
        tabActivate: function(event, ui) {
            var id = ui.newPanel.attr('id');
            switch (id) {
                case "tabs-taskcenter":
                    this.requireView({
                        url: "modules/bpmn/taskcenter/taskcenter/views/taskcenterViewMgr",
                        selector: "#tabs-taskcenter"
                    });
                    break;
                case "tabs-myprocess":
                    this.requireView({
                        url: "modules/bpmn/taskcenter/taskcenter/views/myprocessViewMgr",
                        selector: "#tabs-myprocess"
                    });
                    break;
                default:
                    break;
            }
        },
        resize: function(delta) {
            portal.utils.incHeight(this.$(".js-taskcenter-tab > .ui-tabs-panel"), delta);
        }
    });
});
