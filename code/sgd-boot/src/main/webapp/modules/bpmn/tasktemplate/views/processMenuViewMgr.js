define([
    "text!modules/bpmn/tasktemplate/templates/processMenuTemplate.html",
    "modules/bpmn/tasktemplate/actions/processMenuAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr",
], function(template, processMenuAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_left",
        template: fish.compile(template),
        initialize: function() {
            this.colModel = [{
                name: "CATG_ID",
                key: true,
                hidden: true
            }, {
                name: 'name',
                label: "流程目录",
                width: "100%"
            }];
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            var dirMenus = null;
            this.dirMenuGrid = this.$(".js-process-dir-grid").jqGrid({
                autowidth: true,
                colModel: this.colModel,
                treeGrid: true,
                expandColumn: "name",
                treeIcons: {
                    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
                },
                onSelectRow: this.changeMenuNode.bind(this)
            });
            var data = processMenuAction.qryRootDirList();
            var treeNodes = this.modDataToTreeNodes(data.tree);
            this.dirMenuGrid.jqGrid("reloadData", treeNodes);
        },
        changeMenuNode: function() {
            var rowdata = this.dirMenuGrid.jqGrid('getSelection');
            if (!rowdata.isLeaf) {
                return;
            }
            this.trigger("menuNodeChange", rowdata.PROC_DEF_TYPE_ID);
        },
        modDataToTreeNodes: function(data) {
            var treeNode = [];
            $.each(data, function(index, item) {
                if (item.CATG_ID && item.PROC_DEF_TYPE_ID) {
                    item.PARENT_CATG_ID = item.CATG_ID;
                    item.CATG_ID = item.CATG_ID + "_" + item.PROC_DEF_TYPE_ID;
                }
            });
            treeNode = portal.utils.getTree(data, "CATG_ID", "PARENT_CATG_ID", null);
            return treeNode;
        }
    });
});
