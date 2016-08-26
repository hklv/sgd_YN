define([
    "text!modules/bpmn/popwin/processselpopwin/templates/processSelPopWin.html",
    "modules/bpmn/popwin/processselpopwin/actions/dirMenuAction",
    "i18n!modules/bpmn/popwin/processselpopwin/i18n/taskformMgr"
], function(template, dirMenuAction, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        events: {
            "click .js-ok": "okClick",
            "click .js-cancel": "cancelClick"
        },
        initialize: function(options) {
            this.colModel = [{
                name: 'CATG_ID',
                label: '',
                key: true,
                hidden: true
            }, {
                name: 'name',
                label: "流程目录",
                width: "100%",
                search: true,
                sortable: false
            }];
            this.options = options;
            this.resource = fish.extend({}, i18nData, this.options.resource || {});
        },
        render: function() {
            this.setElement(this.template(this.resource));
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
            });
            this.$(".js-searchbar").searchbar({
                target: this.dirMenuGrid
            });           
            var data = dirMenuAction.qryRootDirList();
            var treeNodes = data.tree;
            var tasks = portal.utils.getTree(treeNodes, "CATG_ID", "PARENT_CATG_ID", null);
            this.dirMenuGrid.jqGrid("reloadData", tasks);
            if (tasks && tasks.length > 0) {
                this.dirMenuGrid.jqGrid("setSelection", tasks[0]);
                var rd = this.dirMenuGrid.jqGrid("getSelection");
                if (!rd.isLeaf) {
                    this.dirMenuGrid.jqGrid("expandNode", rd);
                }
            }

        },
        okClick: function() {
            var process = this.dirMenuGrid.jqGrid("getSelection");
            if ($.isEmptyObject(process)) {
                fish.warn("你必须至少选中一个流程");
                return;
            }
            this.popup.close(process);
        }

    });
});
