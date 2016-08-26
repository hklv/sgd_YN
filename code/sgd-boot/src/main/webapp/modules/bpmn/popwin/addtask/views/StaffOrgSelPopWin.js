define([
    "text!modules/bpmn/popwin/addtask/templates/StaffOrgTemplate.html",
    "modules/bpmn/popwin/addtask/views/OrgMgr",
    "modules/bpmn/popwin/addtask/views/StaffMgr"
], function(tpl, orgMgrView, staffMgrView) {
    var StaffOrgMrgView = portal.BaseView.extend({
        template: fish.compile(tpl),
        events: {
            "click .js-ok": "okClick",
            "click .js-cancel": "cancelClick"
        },
        initialize: function() {
            this.setViews({
                ".js-left-div": new orgMgrView(),
                ".js-right-div": new staffMgrView()
            });
        },
        render: function() {
            this.setElement(this.template());
            this.getView(".js-left-div").on("menuNodeChange", this.menuNodeChange, this);
        },
        menuNodeChange: function(portal) {
            this.getView(".js-right-div").trigger("menuNodeChange", portal);
        },
        afterRender: function() {},
        resize: function(delta) {
            var staffGridHeight=300;
            portal.utils.gridIncHeight(this.$(".js-staff-grid"), staffGridHeight);
            portal.utils.gridIncHeight(this.$(".js-process-dir-grid"), this.$(".js-right-div").height() - this.$(".js-left-div").height());
        },
        okClick: function() {
            var selarrrow = this.getView('.js-right-div').staffGrid.grid("getSelection");
            if ($.isEmptyObject(selarrrow)) {
                fish.warn("你必须至少选中一个用户");
                return;
            }
            this.popup.close(selarrrow);
        }

    });
    return StaffOrgMrgView;
});
