define([
    'text!modules/bpmn/tasktemplate/templates/actionListen.html',
    "modules/bpmn/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr"
], function(eventTemplate, taskTemplateAction, i18nData) {
    var eventTemplate = portal.BaseView.extend({
        template: fish.compile(eventTemplate),
        events: {
            "click .js-form-ok": 'saveEventData'
        },
        initialize: function() {
            fish.on("rowChange.tasktemplate", function(grid) {
                this.TEMPLATE_ID=grid.TEMPLATE_ID;
                this.loadEventData(grid.TEMPLATE_ID);
            }.bind(this));
            fish.on("beforeAdd.clear", function() {
                this.$form.form("clear").form("disable");
                this.$(".js-form-ok").attr("disabled", true);
            }.bind(this));

            fish.on("cancelAdd.clear", function() {
                this.loadEventData(this.TEMPLATE_ID);
            }.bind(this));
        },
        render: function() {
            this.setElement(this.template(i18nData));
        },
        afterRender: function() {

            this.$form=$(".js-form-event").form();
            this.$form.form("disable");
            this.$(".js-form-ok").attr("disabled", true);
            this.$startServicePopEdit = this.$(".form-control-start").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/taskrepo/views/servicePopWin",
                        viewOption: {
                            ServiceName: this.$returnServicePopEdit.popedit("getValue")?this.$returnServicePopEdit.popedit("getValue").service_start : undefined
                        },
                        close: function(msg) {
                            this.$startServicePopEdit.popedit("setValue", {
                                service_start: msg,
                                service_start: msg
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "service_start",
                dataValueField: "service_start"
            });

            this.$endServicePopEdit = this.$(".form-control-end").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/taskrepo/views/servicePopWin",
                        viewOption: {
                            ServiceName: this.$returnServicePopEdit.popedit("getValue")?this.$returnServicePopEdit.popedit("getValue").service_end : undefined
                        },
                        close: function(msg) {
                            this.$endServicePopEdit.popedit("setValue", {
                                service_end: msg,
                                service_end: msg
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "service_end",
                dataValueField: "service_end"
            });

            this.$returnServicePopEdit = this.$(".form-control-return").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/taskrepo/views/servicePopWin",
                        viewOption: {
                            ServiceName: this.$returnServicePopEdit.popedit("getValue")?this.$returnServicePopEdit.popedit("getValue").service_return : undefined
                        },
                        close: function(msg) {
                            this.$returnServicePopEdit.popedit("setValue", {
                                service_return: msg,
                                service_return: msg
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "service_return",
                dataValueField: "service_return"
            });
        },

        loadEventData:function(id){
            var datas = taskTemplateAction.qryTaskTemplateEvent(id);
            var events=datas.TASK_TEMPLATE_EVENTS;
            var formdata={};
            if(events) {
                for (var i = 0; i < events.length; i++) {
                    var type = events[i].EVENT_TYPE;
                    if (type == "START") {
                        formdata.service_start = events[i].SERVICE_NAME;
                        formdata.describe_start = events[i].COMMENTS;
                    } else if (type == "END") {
                        formdata.service_end = events[i].SERVICE_NAME;
                        formdata.describe_end = events[i].COMMENTS;
                    } else if (type == "BACK") {
                        formdata.service_return = events[i].SERVICE_NAME;
                        formdata.describe_return = events[i].COMMENTS;
                    }
                }
            }
            this.$form.form("clear");
            this.$form.form("value",formdata).form("enable");
            this.$(".js-form-ok").attr("disabled", false);
        },

        saveEventData:function(){
            var formData=this.$form.form("value");
            var TASK_TEMPLATE_EVENTS=[];
            if(formData.service_start){
                var I={};
                I.SERVICE_NAME=formData.service_start;
                I.TEMPLATE_ID=this.TEMPLATE_ID;
                I.EVENT_TYPE="START";
                if(formData.describe_start){
                    I.COMMENTS=formData.describe_start;
                }else{
                    I.COMMENTS="";
                }
                TASK_TEMPLATE_EVENTS[0]=I;
            }

            if(formData.service_end){
                var I={};
                I.SERVICE_NAME=formData.service_end;
                I.TEMPLATE_ID=this.TEMPLATE_ID;
                I.EVENT_TYPE="END";
                if(formData.describe_end){
                    I.COMMENTS=formData.describe_end;
                }else{
                    I.COMMENTS="";
                }
                TASK_TEMPLATE_EVENTS[1]=I;
            }

            if(formData.service_return){
                var I={};
                I.SERVICE_NAME=formData.service_return;
                I.TEMPLATE_ID=this.TEMPLATE_ID;
                I.EVENT_TYPE="BACK";
                if(formData.describe_return){
                    I.COMMENTS=formData.describe_return;
                }else{
                    I.COMMENTS="";
                }
                TASK_TEMPLATE_EVENTS[2]=I;
            }
            if(TASK_TEMPLATE_EVENTS.length==0){
                fish.warn({title:'警告',message:'请至少填写一个服务名称再提交！'});
            }

            var data=taskTemplateAction.confTaskTemplateEvent(this.TEMPLATE_ID,TASK_TEMPLATE_EVENTS);
            if(data.TASK_TEMPLATE_ID){
                fish.success({title:'操作成功',message:'编辑成功！'});
            }
            this.loadEventData( this.TEMPLATE_ID);
        }
    });

    return eventTemplate;
});