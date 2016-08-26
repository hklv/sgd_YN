define([
    'text!modules/bpmn/tasktemplate/templates/distributionRuleTemplate.html',
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr",
    "modules/bpmn/tasktemplate/actions/taskTemplateAction"
], function(Template, i18nData, taskTemplateAction) {
    var distributionRuleView = portal.BaseView.extend({
        template: fish.compile(Template),
        events: {
            "click .js-form-edit": 'editForm',
            "click .js-form-ok": 'okForm',
            "click .js-form-cancel": 'cancelForm'
        },
        initialize: function(option) {
            fish.on("rowChange.tasktemplate", function(grid) {
                this.rowData=grid;
                this.rowSelectCallback(grid);
            }.bind(this));

            fish.on("beforeAdd.clear", function() {
                this.setBtnStatus(true);
                this.$qryForm.form('clear').form("disable");
            }.bind(this));

            fish.on("cancelAdd.clear", function() {
                this.$qryForm.form('clear').form("enable");
                this.$qryForm.resetValid();
                this.rowSelectCallback(this.rowData);
            }.bind(this));
        },
        render: function() {
            this.$el.html(this.template(i18nData));
        },
        afterRender: function() {
           
            this.$qryForm = this.$(".js-form-detail");
            this.$qryForm.form("disable");
            this.setBtnStatus(true);
            this.$jobPopEdit = this.$(".form-control-job").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/jobmgr/views/JobPopWin",
                        viewOption: {
                            JOB_ID: this.$jobPopEdit.popedit("getValue").JOB_ID
                        },
                        close: function(msg) {
                            this.$jobPopEdit.popedit("setValue", {
                                JOB_ID: msg.JOB_ID,
                                JOB_NAME: msg.JOB_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "JOB_NAME",
                dataValueField: "JOB_ID"
            });

            this.$userPopEdit = this.$(".form-control-user").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/usermgr/views/UserPopWin",
                        viewOption: {
                            userName: this.$userPopEdit.popedit("getValue").USER_NAME
                        },
                        close: function(msg) {
                            this.$userPopEdit.popedit("setValue", {
                                USER_ID: msg.USER_ID,
                                USER_NAME: msg.USER_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "USER_NAME",
                dataValueField: "USER_ID"
            });

            this.$rolePopEdit = this.$(".form-control-role").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/rolemgr/views/RolePopWin",
                        viewOption: {
                            ROLE_ID: this.$rolePopEdit.popedit("getValue").ROLE_ID
                        },
                        close: function(msg) {
                            this.$rolePopEdit.popedit("setValue", {
                                ROLE_ID: msg.ROLE_ID,
                                ROLE_NAME: msg.ROLE_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "ROLE_NAME",
                dataValueField: "ROLE_ID"
            });

            this.$orgPopEdit = this.$(".form-control-org").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/stafforg/views/OrgPopWin",
                        viewOption: {
                            ORG_ID: this.$orgPopEdit.popedit("getValue").ORG_ID
                        },
                        close: function(msg) {
                            this.$orgPopEdit.popedit("setValue", {
                                ORG_ID: msg.ORG_ID,
                                ORG_NAME: msg.ORG_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "ORG_NAME",
                dataValueField: "ORG_ID"
            });

            this.$servicePopEdit = this.$(".form-control-service").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/taskrepo/views/servicePopWin",
                        viewOption: {
                            ServiceName: this.$servicePopEdit.popedit("getValue").DISPATCH_SERVICE_NAME
                        },
                        close: function(msg) {
                            this.$servicePopEdit.popedit("setValue", {
                                DISPATCH_SERVICE_NAME: msg,
                                DISPATCH_SERVICE_NAME: msg
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "DISPATCH_SERVICE_NAME",
                dataValueField: "DISPATCH_SERVICE_NAME"
            });

        },

        editForm:function(){
            this.$qryForm.form('enable');
        },


        editActionChange: function(status) {
            var editShow = true;
            if (status && status == "NEW") { //新增
                editShow = false;
                this.$formDetailForm.form('clear').form("enable");
            } else if (status && status == "EDIT") { //编辑
                editShow = false;
                this.$formDetailForm.form("enable");
            }
            if (editShow) {
                this.$(".js-form-cancel").parents(".col-sm-offset-6").hide();
                this.$(".js-form-cancel").parents(".col-sm-offset-6").prev().show();
            } else {
                this.$(".js-form-cancel").parents(".col-sm-offset-6").show();
                this.$(".js-form-cancel").parents(".col-sm-offset-6").prev().hide();
            }

        },

        setBtnStatus: function(editDisabled) {
            this.$(".js-form-edit").attr("disabled", editDisabled);
        },

        editActionChange: function(status) {
            var editShow = true;
            if (status && status == "NEW") { //新增
                editShow = false;
                this.$qryForm.form('clear').form("enable");
            } else if (status && status == "EDIT") { //编辑
                editShow = false;
                this.$qryForm.form("enable");
            }
            if (editShow) {
                this.$(".js-form-cancel").parents(".col-sm-offset-6").hide();
                this.$(".js-form-cancel").parents(".col-sm-offset-6").prev().show();
            } else {
                this.$(".js-form-cancel").parents(".col-sm-offset-6").show();
                this.$(".js-form-cancel").parents(".col-sm-offset-6").prev().hide();
            }

        },

        editForm: function(event) {
            this.editAction = "EDIT";
            this.editActionChange(this.editAction);
        },

        cancelForm: function(event) {
            //fish.trigger("saveChange.test", this.rowData);
            this.$qryForm.resetValid();
            this.rowSelectCallback(this.rowData);

        },

        okForm: function(event) {
            this.rowData.ADD_OR_EDIT = "edit";
            this.rowData.JOB_ID= this.$jobPopEdit.popedit("getValue").JOB_ID;
            this.rowData.USER_ID= this.$userPopEdit.popedit("getValue").USER_ID;
            this.rowData.ORG_ID= this.$orgPopEdit.popedit("getValue").ORG_ID;
            this.rowData.ROLE_ID= this.$rolePopEdit.popedit("getValue").ROLE_ID;
            this.rowData.DISPATCH_SERVICE_NAME= this.$servicePopEdit.popedit("getValue").DISPATCH_SERVICE_NAME;

            var data=taskTemplateAction.updateTaskTemp(this.rowData);
            fish.success({title:'操作成功',message:"编辑成功"});
            fish.trigger("saveChange.reload", this.rowData);
            $(".js-taskcenter-tab").tabs("option", "active", 1);

        },

        newForm: function(event) {
            this.editAction = "NEW";
            this.editActionChange(this.editAction);
        },

        rowSelectCallback: function(rowData) {
            if (rowData) {
                this.setBtnStatus(false);
                this.$qryForm.form('clear');
                var user=undefined;
                var job=undefined;
                var role=undefined;
                var org=undefined;
                var service=undefined;
                if(rowData.USER_ID){
                    user={};
                    user.USER_ID=rowData.USER_ID;
                    user.USER_NAME=rowData.USER_NAME;
                }
                if(rowData.JOB_ID){
                    job={};
                    job.JOB_ID=rowData.JOB_ID;
                    job.JOB_NAME=rowData.JOB_NAME;
                }
                if(rowData.ROLE_ID){
                    role={};
                    role.ROLE_ID=rowData.ROLE_ID;
                    role.ROLE_NAME=rowData.ROLE_NAME;
                }
                if(rowData.ORG_ID){
                    org={};
                    org.ORG_ID=rowData.ORG_ID;
                    org.ORG_NAME=rowData.ORG_NAME;
                }
                if(rowData.DISPATCH_SERVICE_NAME){
                    service={};
                    service.DISPATCH_SERVICE_NAME=rowData.DISPATCH_SERVICE_NAME;
                }

                this.$jobPopEdit.popedit("setValue",job);
                this.$userPopEdit.popedit("setValue",user);
                this.$orgPopEdit.popedit("setValue",org);
                this.$rolePopEdit.popedit("setValue",role);
                this.$servicePopEdit.popedit("setValue",service);
                this.$qryForm.form("disable");
            } else {
                this.setBtnStatus(true);
                this.$qryForm.form('clear').form("disable");
            }
            this.editAction = null;
            this.editActionChange(this.editAction);

        }
    });
    return distributionRuleView;
});
