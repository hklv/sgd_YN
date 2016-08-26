define([
    "text!modules/commonbiz/templates/DictionDataMgrTemplate.html",
    "../actions/DictionaryAction",
    "i18n!modules/commonbiz/i18n/dictionarymgr",
    "text!modules/common/templates/GridCellDeleteTemplate.html",
    "modules/areamgr/actions/AreaAction"
], function(dictionDataMgrTpl, dictionaryAction, dictionaryMgr, gridCellDelTemplate, areaAction) {
    return portal.BaseView.extend({
        className: "container_right",

        template: fish.compile(dictionDataMgrTpl),
        cellDelTemplate: fish.compile(gridCellDelTemplate),

        events: {

        },

        initialize: function() {

        },

        render: function() {
            this.$el.html(this.template(dictionaryMgr));
            //this.delHtml = this.cellDelTemplate(dictionaryMgr);
            return this;
        },

        afterRender: function() { //dom加载完成的事件
            this.orgTree = this.$(".js-dictionaryData-grid").jqGrid({
                // height:320,
                colModel: [{
                    name: "id",
                    label: "id",
                    hidden: true,
                    key: true
                },{
                    name: "typeId",
                    label: "typeId",
                    hidden: true,
                    key: true
                }, {
                    name: "valueName",
                    label: "值名称",
                    width: "25%"
                }, {
                    name: "valueCode",
                    label: "值编码",
                    width: "25%"
                }, {
                    name: "isLocked",
                    label: "是否锁定",
                    width: "25%"
                }, {
                    name: "status",
                    label: "状态",
                    width: "25%"
                }, {
                    name: "notes",
                    label: "备注",
                    width: "25%"
                }],
                treeGrid: true,
                exportFeature: function () {
                    return {
                        serviceName: "QryStaffMasterOrgList"
                    }
                }.bind(this),
                treeIcons: {
                    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
                },
                expandColumn: "ORG_NAME",
                pagebar: true,
                //onSelectRow: this.rowSelectCallback.bind(this),
                //onChangeRow: this.onChangeRow.bind(this)
            });


        }
    });
});