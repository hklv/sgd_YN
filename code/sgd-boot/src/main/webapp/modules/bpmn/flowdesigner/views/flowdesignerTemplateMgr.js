define([
    "text!modules/bpmn/flowdesigner/templates/flowdesignerTemplate.html",
    "modules/bpmn/flowdesigner/views/processMenuViewMgr",
    "modules/bpmn/flowdesigner/views/flowdesignerViewMgr"
], function(template, dirMenuView, flowdesignerViewMgr) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function() {
            this.setViews({
                ".js-left-div": new dirMenuView(),
                ".js-right-div": new flowdesignerViewMgr()
            });
        },
        render: function() {
            this.$el.html(this.template());
            this.getView(".js-left-div").on("menuNodeChange", this.menuNodeChange, this);
            return this;
        },
        menuNodeChange: function(data,id) {
            this.getView(".js-right-div").trigger("menuNodeChange", data,id);
        },
        resize: function(delta) {
            portal.utils.gridIncHeight(this.$(".js-process-dir-grid"), 400);
        }
    });
});
