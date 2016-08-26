define([
    "text!modules/bpmn/popwin/tasktemplate/templates/processMenuTemplate.html",
    "modules/bpmn/popwin/tasktemplate/actions/processMenuAction",
    "i18n!modules/bpmn/popwin/tasktemplate/i18n/taskTemplateMgr",
], function(template, processMenuAction, i18nData) { 
    return portal.BaseView.extend({
        className: "container_left",
        template: fish.compile(template),
        initialize: function(option) {
            this.option=option;
            this.colModel = [{
                name: "id",
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
                //expandColClick: true,
                expandColumn: "name",
                treeIcons: {
                    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
                },
                onSelectRow: this.changeMenuNode.bind(this),
                
            });
            var data = processMenuAction.qryRootDirList();         
            var treeNodes = this.modDataToTreeNodes(data.tree);
            this.dirMenuGrid.jqGrid("reloadData", treeNodes);
            var result = processMenuAction.TaskRepoService(this.option.TEMP_ID);
            var PROC_DEF_TYPE_ID = result.taskList[0].PROC_DEF_TYPE_ID;
            var proceId = "procType"+PROC_DEF_TYPE_ID;
            var selectRow = this.dirMenuGrid.grid("getRowData", proceId);
            this.queryNode(selectRow);
            this.dirMenuGrid.grid("setSelection",selectRow);
        },
        changeMenuNode: function() {
            var rowdata = this.dirMenuGrid.jqGrid('getSelection');
            if (!rowdata.isLeaf) {
                return;
            }
            this.trigger("menuNodeChange", rowdata);
        },
        modDataToTreeNodes: function(data) {
            var treeNode = [];
            /*$.each(data, function(index, item) {
                if (item.CATG_ID && item.PROC_DEF_TYPE_ID) {
                    item.PARENT_CATG_ID = item.CATG_ID;
                    item.CATG_ID = item.CATG_ID + "_" + item.PROC_DEF_TYPE_ID;
                }
            });
            treeNode = portal.utils.getTree(data, "CATG_ID", "PARENT_CATG_ID", null);*/
            treeNode = portal.utils.getTree(data, "id", "parent", null);
            return treeNode;
        },
        queryNode:function(selectRow){                  
            var parent = this.dirMenuGrid.grid("getNodeParent", selectRow);
            if(parent==null){              
                return;
            }else{
                this.dirMenuGrid.grid("expandNode", parent);
                //this.changeMenuNode();
                this.queryNode(parent); 

            }
        }

    });
});
