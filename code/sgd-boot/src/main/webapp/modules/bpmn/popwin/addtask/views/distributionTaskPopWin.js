define([
    "text!modules/bpmn/popwin/addtask/templates/distributionTaskPopWin.html",
    "modules/bpmn/popwin/addtask/actions/distributionTaskAction",
    "i18n!modules/bpmn/popwin/addtask/i18n/distributionTaskMgr",
], function(template, action, i18nData) {
    var distributionTaskMView = portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function() {},
        render: function() {
            this.setElement(this.template(i18nData));
        },
        afterRender: function() {
            this.$staffPopEdit = this.$("[name='STAFF_ID']").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/bpmn/taskcenter/addtask/views/StaffOrgSelPopWin",
                        close: function(msg) {
                            this.$staffPopEdit.popedit("setValue", {
                                Value: msg.STAFF_ID,
                                Text: msg.STAFF_NAME
                            });
  
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "Text",
                dataValueField: "Value"
            });

            this.$rolePopEdit = this.$("[name='ROLE_ID']").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/rolemgr/views/RolePopWin",
                        viewOption: {
                            ROLE_ID: this.$rolePopEdit.popedit("getValue").Value
                        },
                        close: function(msg) {
                            this.$rolePopEdit.popedit("setValue", {
                                Value: msg.ROLE_ID,
                                Text: msg.ROLE_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "Text",
                dataValueField: "Value"
            });

            this.$orgPopEdit = this.$("[name='ORG_ID']").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/stafforg/views/OrgPopWin",
                        viewOption: {
                            ORG_ID: this.$orgPopEdit.popedit("getValue").Value
                        },
                        close: function(msg) {
                            this.$orgPopEdit.popedit("setValue", {
                                Value: msg.ORG_ID,
                                Text: msg.ORG_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "Text",
                dataValueField: "Value"
            });
            this.$jobPopEdit = this.$("[name='JOB_ID']").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/jobmgr/views/JobPopWin",
                        viewOption: {
                            JOB_ID: this.$jobPopEdit.popedit("getValue").Value
                        },
                        close: function(msg) {
                            this.$jobPopEdit.popedit("setValue", {
                                Value: msg.JOB_ID,
                                Text: msg.JOB_NAME
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "Text",
                dataValueField: "Value"
            });



        }
    });
    return distributionTaskMView;
});
