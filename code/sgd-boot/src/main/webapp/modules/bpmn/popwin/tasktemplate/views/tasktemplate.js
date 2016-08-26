define([
    "text!modules/bpmn/popwin/tasktemplate/templates/tasktemplate.html",
    "modules/bpmn/popwin/tasktemplate/views/processMenuViewMgr",
    "modules/bpmn/popwin/tasktemplate/views/tasktemplateViewMgr"
], function(template, dirMenuView, tasktemplateViewMgr) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function(option) {
            this.rowdata={};
            this.rowdata.tasktype=option.TASK_TYPE;
            this.setViews({
                ".js-left-div": new dirMenuView(option),
                ".js-right-div": new tasktemplateViewMgr(option)
            }); 
        },
        render: function() {
            this.setElement(this.template());            
            this.getView(".js-left-div").on("menuNodeChange", this.menuNodeChange, this);
        },
        menuNodeChange: function(rowdata) {
            this.getView(".js-right-div").trigger("menuNodeChange", rowdata);
        },
        resize: function(delta) {
            delta=400;
            portal.utils.gridIncHeight(this.$(".js-data-grid"), delta);  
            portal.utils.gridIncHeight(this.$(".js-process-dir-grid"), this.$(".container_right").height() - this.$(".container_left").height());
        }
        
   
    });
});
