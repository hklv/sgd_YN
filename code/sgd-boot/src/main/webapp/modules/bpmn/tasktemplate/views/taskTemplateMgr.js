define([
    "text!modules/bpmn/tasktemplate/templates/taskTemplate.html",
    "modules/bpmn/tasktemplate/views/processMenuViewMgr",
    "modules/bpmn/tasktemplate/views/tasktemplateViewMgr"
], function(template, dirMenuView, tasktemplateViewMgr) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function() {
            this.setViews({
                ".col-lg-3": new dirMenuView(),
                ".col-lg-9": new tasktemplateViewMgr()
            });
            
        },
        render: function() {
            this.$el.html(this.template());
            this.getView(".col-lg-3").on("menuNodeChange", this.menuNodeChange, this);
        },
        menuNodeChange: function(portal) {
            this.getView(".col-lg-9").trigger("menuNodeChange", portal);
        },

        afterRender: function() {
            this.$(".js-taskcenter-tab").tabs({
                activateOnce: true,
                activate: function(event, ui) {
                    return this.tabActivate(event, ui);
                }.bind(this)
            });
 	        this.$(".js-taskcenter-tab").tabs("option", "active",1);
            this.$(".js-taskcenter-tab").tabs("option", "active",2);
            this.$(".js-taskcenter-tab").tabs("option", "active",3);
            this.$(".js-taskcenter-tab").tabs("option", "active",4);
            this.$(".js-taskcenter-tab").tabs("option", "active",0);
        },
        tabActivate: function(event, ui) {
            var id = ui.newPanel.attr('id');
            switch (id) {
                case "tabs-distribution-rule":
                    this.requireView({
                        url: "modules/bpmn/tasktemplate/views/distributionRuleView",
                        selector: "#tabs-distribution-rule"
                    });
                    this.setSubHeight();
                    break;
                case "tabs-task-detail":
                    this.requireView({
                        url: "modules/bpmn/tasktemplate/views/taskDetailTemplateView",
                        viewOption: {
                            grid: this.$(".js-data-grid")
                        },
                        selector: "#tabs-task-detail"
                    });
                    this.setSubHeight();
                    break;
                case "tabs-associated-button":
                    this.requireView({
                        url: "modules/bpmn/tasktemplate/views/associatedbutton",
                        selector: "#tabs-associated-button"
                    });
                    this.setSubHeight();
                    break;
                case "tabs-action-listen":
                    this.requireView({
                        url: "modules/bpmn/tasktemplate/views/actionListen",
                        selector: "#tabs-action-listen"
                    });
                    this.setSubHeight();
                    break;
                case "tabs-back-reason":
                    this.requireView({
                        url: "modules/bpmn/tasktemplate/views/backReason",
                        selector: "#tabs-back-reason"
                    });
                    this.setSubHeight();
                    break;
                default:
                    break;
            }
        },
        resize: function(delta) {
            portal.utils.gridIncHeight(this.$(".js-data-grid"), delta);
            portal.utils.gridIncHeight(this.$(".js-process-dir-grid"), this.$(".container_right").height() - this.$(".js-process-dir-grid").outerHeight());
        },
        setSubHeight: function() {
            this.$(".js-taskcenter-tab > .ui-tabs-panel").outerHeight(200);
        },
        remove: function() {
            fish.off("rowChange.tasktemplate");
        }
    });
});
