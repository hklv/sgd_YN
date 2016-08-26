define([
    "text!modules/bpmn/popwin/addtask/templates/OrgMgrTemplate.html",
    "modules/bpmn/popwin/addtask/actions/StaffOrgAction",
    "i18n!modules/bpmn/popwin/addtask/i18n/distributionTaskMgr"
], function(template, staffOrgAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_left",
        template: fish.compile(template),
        initialize: function() {
            this.colModel = [{
                name: "ORG_ID",
                key: true,
                hidden: true
            }, {
                name: 'ORG_NAME',
                label: "组织名称",
                width: "100%"
            }];
        },
        render: function() {
            this.setElement(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.dirMenuGrid = this.$(".js-process-dir-grid").jqGrid({
                colModel: this.colModel,
                treeGrid: true,
                treeIcons: {
                    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
                },
                expandColumn: "ORG_NAME",
                onSelectRow: this.changeMenuNode.bind(this)
            });
            staffOrgAction.qryStaffMasterOrgList(function(data) {
                var subList = data.ORG_LIST;
                var orgs = portal.utils.getTree(subList, "ORG_ID", "PARENT_ORG_ID", null);
                this.dirMenuGrid.jqGrid("reloadData", orgs);
                if (orgs && orgs.length > 0) {
                    this.dirMenuGrid.jqGrid("setSelection", orgs[0]);
                    var rd = this.dirMenuGrid.jqGrid("getSelection");
                    if (!rd.isLeaf) this.dirMenuGrid.jqGrid("expandNode", rd);
                }
            }.bind(this));

        },
        changeMenuNode: function() {
            var rowdata = this.dirMenuGrid.jqGrid('getSelection');
            if (rowdata.inadd) {
                return;
            }
            this.trigger("menuNodeChange", rowdata.ORG_ID);
        }

    });
});
