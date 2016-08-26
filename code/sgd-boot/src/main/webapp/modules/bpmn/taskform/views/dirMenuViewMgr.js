define([
    "text!modules/bpmn/taskform/templates/dirMenuTemplate.html",
    "modules/bpmn/taskform/actions/dirMenuAction",
    "i18n!modules/bpmn/taskform/i18n/taskformMgr"
], function(template, dirMenuAction, i18nData) {
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
            this.dirMenuGrid = this.$(".js-process-dir-grid").jqGrid({
                colModel: this.colModel,
                treeGrid: true,
                treeIcons: {
                    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
                },
                expandColumn: "name",
                onSelectRow: this.changeMenuNode.bind(this)
            });
            var data = dirMenuAction.qryRootDirList();
            var treeNodes = data.tree;
            var tasks = portal.utils.getTree(treeNodes, "CATG_ID", "PARENT_CATG_ID", null);
            this.dirMenuGrid.jqGrid("reloadData", tasks);
            if (tasks && tasks.length > 0) {
                this.dirMenuGrid.jqGrid("setSelection", tasks[0]);
                var rd = this.dirMenuGrid.jqGrid("getSelection");
               if (!rd.isLeaf) this.dirMenuGrid.jqGrid("expandNode", rd);
            }
        },
        changeMenuNode: function() {
            var rowdata = this.dirMenuGrid.jqGrid('getSelection');
            if (rowdata.inadd) {
                return;
            }
            this.trigger("menuNodeChange", rowdata.CATG_ID);
        }
    });
});
