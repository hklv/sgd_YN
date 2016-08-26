define([
    'text!modules/bpmn/tasktemplate/templates/backReason.html',
    "modules/bpmn/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr"
], function(backReasonTemplate, taskTemplateAction, i18nData) {
    var backReasonTemplate = portal.BaseView.extend({
        template: fish.compile(backReasonTemplate),
        events: {
            "click .js-form-add": 'addReason',
            "click .js-form-del": 'delReason',
            "click .js-form-modify": 'modifyReason'
        },
        initialize: function() {
            this.col=[{
                name: 'BACK_REASON_ID',
                label: '',
                width: 10,
                hidden: true,
                key: true
            }, {
                name: 'REASON_NAME',
                label: "回退原因",
                width: 100

            }, {
                name: 'REASON_CODE',
                label: "原因编码",
                width: 100

            }];
            fish.on("rowChange.tasktemplate", function(grid) {
                this.TEMPLATE_ID=grid.TEMPLATE_ID;
                this.loadGrid();
            }.bind(this));
            fish.on("beforeAdd.clear", function() {
                this.Grid.jqGrid("clearData");
                this.$(".js-form-add").attr("disabled",true);
                this.$(".js-form-del").attr("disabled",true);
                this.$(".js-form-modify").attr("disabled",true);
            }.bind(this));

            fish.on("cancelAdd.clear", function() {
                this.loadGrid();
            }.bind(this));
        },
        render: function() {
            this.setElement(this.template(i18nData));
        },
        afterRender: function() {
            this.Grid = $(".js-back-grid").jqGrid({
                colModel: this.col,
                height:150
            });
            this.$(".js-form-add").attr("disabled",true);
            this.$(".js-form-del").attr("disabled",true);
            this.$(".js-form-modify").attr("disabled",true);
        },

        loadGrid: function(){
            var data=taskTemplateAction.qryTaskTemplateBackReason(this.TEMPLATE_ID);
            var reasons=data.TASK_TEMPLATE_BACK_REASONS;
            this.Grid.jqGrid("clearData");
            this.Grid.jqGrid("reloadData",reasons);
            this.$(".js-form-add").attr("disabled",false);
            this.$(".js-form-del").attr("disabled",false);
            this.$(".js-form-modify").attr("disabled",false);
        },

        addReason: function(){
            var options = {
                "TEMPLATE_ID":this.TEMPLATE_ID,
                "type":"new"
            };
            fish.popupView({
                url:'modules/bpmn/tasktemplate/views/addAndModifyBackReason',
                viewOption: options,
                close:function(data){
                    this.loadGrid();
                    fish.success({title:'操作成功',message:data});
                }.bind(this)
            });
        },

        modifyReason: function(){
            var row=this.Grid.jqGrid("getSelection");
            if(row.BACK_REASON_ID) {
                var options = {
                    "TEMPLATE_ID": this.TEMPLATE_ID,
                    "type": "edit",
                };
                options.row=row;
                fish.popupView({
                    url: 'modules/bpmn/tasktemplate/views/addAndModifyBackReason',
                    viewOption: options,
                    close: function (data) {
                        this.loadGrid();
                        fish.success({title: '操作成功！', message: data});
                    }.bind(this)
                });
            }else{
                fish.warn({title:'警告',message:'请选择要修改的行！'});
            }
        },

        delReason: function(){
            var row=this.Grid.jqGrid("getSelection");
            if(row.BACK_REASON_ID){
                var data=taskTemplateAction.delTaskTemplateBackReason(row.BACK_REASON_ID);
                if(data.BACK_REASON_ID){
                    fish.success({title:'操作成功',message:'删除成功！'});
                    this.loadGrid();
                }else{
                    fish.warn({title:'警告',message:'删除失败！'});
                }
            }else{
                fish.warn({title:'警告',message:'请选择要删除的行！'});
            }
        }
    });

    return backReasonTemplate;
});