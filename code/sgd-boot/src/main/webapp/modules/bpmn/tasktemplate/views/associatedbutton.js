define([
    'text!modules/bpmn/tasktemplate/templates/associatedbutton.html',
    "modules/bpmn/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr"
], function(associatedbuttonTemplate, taskTemplateAction, i18nData) {
    var associatedbuttonTemplate = portal.BaseView.extend({
        template: fish.compile(associatedbuttonTemplate),
        events: {
            "click .js-form-ok" : "buttonset"
        },
        initialize: function() {
            this.col=[{
                name: 'BTN_ID',
                label: '',
                width: 10,
                hidden: true,
                key: true
            }, {
                name: 'BTN_NAME',
                label: "按钮名称",
                width: 100

            }, {
                name: 'ALIAS_NAME',
                label: "按钮别名",
                width: 100

            }, {
                name: 'FORM_NAME',
                label: "表单名称",
                width: 200

            }, {
                name: 'BTN_SEQ_NBR',
                label: "顺序",
                width: 50

            }, {
                name: 'COMMENTS',
                label: "备注",
                width: 250

            }];
            fish.on("rowChange.tasktemplate", function(grid) {
                this.TEMPLATE_ID=grid.TEMPLATE_ID;
                this.loadGrid(grid.TEMPLATE_ID);
            }.bind(this));

            fish.on("beforeAdd.clear", function() {
                this.Grid.jqGrid("clearData");
                this.$(".js-set-button").attr("disabled",true);
            }.bind(this));

            fish.on("cancelAdd.clear", function() {
                this.Grid.jqGrid("clearData");
                this.Grid.jqGrid("reloadData",this.btnList);
                this.$(".js-set-button").attr("disabled",false);
            }.bind(this));
        },
        render: function() {
            this.$el.html(this.template(i18nData));
        },
        afterRender: function() {
            this.$(".js-set-button").attr("disabled",true);
            this.Grid = this.$(".js-button-grid").jqGrid({
                colModel: this.col,
                height:150
            });
            
        },
        
        loadGrid: function (id) {
            var data=taskTemplateAction.qryButtonListByTask(id);
            this.btnList=data.buttonList || [];
            this.Grid.jqGrid("clearData");
            this.Grid.jqGrid("reloadData",this.btnList);
            this.$(".js-set-button").attr("disabled",false);
        },

        buttonset: function() { //选择菜单
            var options = {
                "TEMPLATE_ID":this.TEMPLATE_ID
            };
            fish.popupView({
                url:'modules/bpmn/tasktemplate/views/setbutton',
                viewOption: options,
                close:function(data){
                    this.loadGrid(this.TEMPLATE_ID);
                    fish.success({title:'操作成功',message:data});
                }.bind(this)
            });

        }
    });
    return associatedbuttonTemplate;

});
