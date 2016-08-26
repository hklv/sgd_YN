define([
    "text!modules/bpmn/taskform/templates/taskformTemplate.html",
    "modules/bpmn/taskform/views/dirMenuViewMgr",
    "modules/bpmn/taskform/views/taskformViewMgr"
], function(template, dirMenuView, taskformView) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function() {
            this.setViews({
                ".col-lg-3": new dirMenuView(),
                ".col-lg-9": new taskformView()
            });
        },
        render: function() {
            this.$el.html(this.template());
            this.getView(".col-lg-3").on("menuNodeChange", this.menuNodeChange, this);
        },
        menuNodeChange: function(id) {
            this.getView(".col-lg-9").trigger("menuNodeChange", id);
        },
        afterRender: function() {},
        resize: function(delta) {
            portal.utils.gridIncHeight(this.$(".js-taskform-grid"), delta);
            portal.utils.gridIncHeight(this.$(".js-process-dir-grid"), this.$(".container_right").height() - this.$(".container_left").height());
        }
    });
});
