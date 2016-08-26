define([
    'text!modules/bpmn/popwin/exceptionsetting/templates/exceptionsetting.html',
    'modules/bpmn/popwin/exceptionsetting/actions/exceptionsettingAction',
    'i18n!modules/bpmn/popwin/exceptionsetting/i18n/exceptionSettingMgr'
], function (template, exceptionAction, i18nDirMenuMgr) {
    return portal.BaseView.extend({
        template: fish.compile(template),

        initialize: function (options) {
            this.colModel = [{
                name: 'ID_',
                key: true,
                hidden: true
            }, {
                name: 'VAR_NAME',
                label: "发生异常的活动",
                search: true,
                width: "20%"
            }, {
                name: 'VAR_TYPE',
                label: "异常原因",
                search: true,
                width: "20%"
            }, {
                name: 'DEFAULT_VALUE',
                label: "回滚流程目标活动",
                width: "20%"
            }, {
                sortable: false,
                label: "操作",
                width: "20%",
                formatter: 'actions',
                formatoptions: {
                    editbutton: false,
                    delbutton: true
                }
            }];
            //this.on("delRow", this.delRowConfirm);
        },
        render: function () {
            this.setElement(this.template(i18nDirMenuMgr));
            return this;
        },
        afterRender: function () {
            this.$grid = this.$(".grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',         
            });
            this.$grid.prev().children().searchbar({
                target: this.$grid
            });
            this.$grid.grid("navButtonAdd",[{
                caption: "新增法规库",
                id: "btnAddLaw"
            }]);
        },
        
       
    });
});