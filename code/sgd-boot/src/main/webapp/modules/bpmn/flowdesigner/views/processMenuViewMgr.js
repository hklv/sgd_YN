define([
    "text!modules/bpmn/flowdesigner/templates/processMenuTemplate.html",
    "modules/bpmn/flowdesigner/actions/flowdesignerAction",
    "i18n!modules/bpmn/flowdesigner/i18n/flowdesignerMgr",
], function(template, flowdesignerAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_left",
        template: fish.compile(template),
        initialize: function() {
            this.colModel = [{
                name: "id",
                key: true,
                hidden: true
            }, {
                name: 'name',
                label: "流程目录",
                width: "100%"
            }];
            this.viewUrl = [
                'modules/bpmn/flowdesigner/views/FlowCatgMgr',
                'modules/bpmn/flowdesigner/views/ProcDefMgr',
                'modules/bpmn/flowdesigner/views/ProcTempMgr',
                'modules/bpmn/flowdesigner/views/ProcVersionMgr'
            ]
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
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
            var data = flowdesignerAction.qryFlowTree();
            var treeNodes = this.modDataToTreeNodes(data.tree);
            this.dirMenuGrid.jqGrid("reloadData", treeNodes);
            if (treeNodes.length > 0) {
                this.dirMenuGrid.jqGrid("setSelection", treeNodes[0]);
            }

        },

        changeMenuNode: function() {
            var rowdata = this.dirMenuGrid.jqGrid('getSelection');           
            var typeId = -1;
            if (rowdata.CATG_ID) {
                typeId = 0;
                if (rowdata.PROC_DEF_TYPE_ID) {
                    typeId = 1;
                }
            } else if (rowdata.PROC_TEMP_ID) {
                typeId = 2;
                if (rowdata.PROCESS_VER_ID) {
                    typeId = 3;
                    var selectRow = this.dirMenuGrid.jqGrid("getRowData", rowdata.parent);
                    var parent = this.dirMenuGrid.jqGrid("getNodeParent", selectRow);                                     
                    this.trigger("menuNodeChange", rowdata,parent.id);
                }
            }
            this.showDetailView(typeId, rowdata);
        }, 
        modDataToTreeNodes: function(data) {
            var treeNode = [];
            var stateName = { 'A': '(激活)', 'B': '(编辑)', 'C': '(禁用)', 'S': '(仿真)' }
            $.each(data, function(index, item) {
                if (item.VER_STATE) {
                    item.name = item.name + ' ' + stateName[item.VER_STATE];
                }
            });
            treeNode = portal.utils.getTree(data, "id", "parent", null);
            return treeNode;
        },
        showDetailView: function(typeId, rowdata) {
            this.requireView({
                url: this.viewUrl[typeId],
                selector: ".js-form-detail",
                viewOption: { data: rowdata }
            });
        }
    });
});
