define([
    "text!modules/bpmn/popwin/addtask/templates/StaffUnderOrg.html",
    "modules/bpmn/popwin/addtask/actions/StaffOrgAction",
    "i18n!modules/bpmn/popwin/addtask/i18n/distributionTaskMgr"
], function(template, staffOrgAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_right",
        template: fish.compile(template),
   
        initialize: function() {
            this.colModel = [{
                name: 'STAFF_ID',
                key: true,
                hidden: true
            }, {
                name: 'STAFF_NAME',
                label: '员工姓名',
                width: "15%",
                sortable: false,
                search: true
            }];
            this.on("menuNodeChange", this.menuNodeChange, this);
        },
        render: function() {
            this.setElement(this.template(i18nData));
            return this;

        },
        afterRender: function(contentHeight) {
            this.staffGrid = this.$(".js-staff-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json'
              
            });
            this.$(".js-searchbar").searchbar({
                target: this.staffGrid
            });
        },
        menuNodeChange: function(id) {
            this.ORG_ID = id;
            this.qryStaffUserListByOrg(id);
        },
        qryStaffUserListByOrg: function(id) {
            staffOrgAction.queryActiveStaffByOrg(id, function(data) {
                var staffs = data || [];
                this.staffGrid.jqGrid("reloadData", { 'rows': staffs });
            }.bind(this));
        }



    });
});
