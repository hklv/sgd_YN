define([
    "text!modules/bpmn/flowdesigner/templates/flowdesignerDrawTemplate.html",
    "modules/bpmn/flowdesigner/actions/flowdesignerAction",
    "i18n!modules/bpmn/flowdesigner/i18n/flowdesignerMgr",
], function(template, flowdesignerAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_right",
        template: fish.compile(template),
        initialize: function() {
            this.srcUrl = 'http://10.45.8.13:8080/portal/modules/bpmn/flowdesigner/draw/flowdesigner.jsp';
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },

        afterRender: function() {
            this.on("menuNodeChange", this.menuNodeChange, this);
        },
        menuNodeChange: function(rowData,id) {           
            this.ID = id;
            var option = {
                procVersionId: rowData.PROCESS_VER_ID,
                deployId: rowData.DEPLOY_ID,
                procTypeId: rowData.PROC_TEMP_ID
            }
            var path = this.srcUrl + '?procVersionId=' + rowData.PROCESS_VER_ID +
                "&deployId=" + rowData.DEPLOY_ID + "&procTypeId=" + rowData.PROC_TEMP_ID;
            this.$("#flow-designer").attr('src', path);
            var iframe = document.getElementById("flow-designer");
            var that = this;
            if (iframe.attachEvent) { //IE
                iframe.attachEvent("onload", function() {
                    that.bindPopupViews(option);
                });
            } else { //Others
                iframe.onload = function() {
                    that.bindPopupViews(option);
                };
            }
        },
        bindPopupViews: function(option) {
            $("#flow-designer").contents().find("#flowvariable").click(function() {
                fish.popupView({
                    url: "modules/bpmn/flowdesigner/popupbox/flowvariable/views/flowvariable",
                    viewOption: option,
                    close: function() {}.bind(this)
                });
            });
            $("#flow-designer").contents().find("#exceptionsetting").click(function() {
                fish.popupView({
                    url: "modules/bpmn/flowdesigner/popupbox/exceptionsetting/views/exceptionsetting",
                    viewOption: option,
                    close: function() {}.bind(this)
                });
            });       
            $("#flow-designer").contents().find("#tasktemplate").click(function() {
                  var taskType = {
                TASK_TYPE: $("#flow-designer").contents().find("#inputTitle").val() === '系统' ? 'S' : 'U',
                TEMP_ID: $("#flow-designer").contents().find("#taskTemplateId").val(),
                ID: this.ID
            };
                option = fish.extend({}, option, taskType);
                fish.popupView({
                    url: "modules/bpmn/flowdesigner/popupbox/tasktemplate/views/tasktemplate",
                    height: 550,
                    viewOption: option,
                    close: function(msg) {
                        $("#flow-designer").contents().find("#taskTemplateName").val(msg.TEMPLATE_NAME);
                    }.bind(this)
                });
            }.bind(this));
        }
    });
});
