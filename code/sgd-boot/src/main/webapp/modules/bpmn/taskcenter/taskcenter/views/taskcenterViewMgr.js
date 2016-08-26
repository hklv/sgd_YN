define([
    "text!modules/bpmn/taskcenter/taskcenter/templates/taskcenterTemplate.html",
    "text!modules/bpmn/taskcenter/taskcenter/templates/flowTemplate.html",
    "modules/bpmn/taskcenter/taskcenter/actions/taskcenterAction",
    "i18n!modules/bpmn/taskcenter/taskcenter/i18n/taskcenterMgr",
], function(template,flowtemplate, taskcenterAction, i18nData) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        flowtemplate: fish.compile(flowtemplate),
        events: { 
            "click .js-query": "qryTaskList",
            "click .flow" : "flowPic"
        },
        initialize: function() {
            this.colModel = [{
                name: 'HOLDER_ID',
                key: true,
                hidden: true
            }, {
                name: 'TEMPLATE_ID',
                hidden: true
            }, {
                name: 'PROC_INST_ID',
                hidden: true
            }, {
                name: 'HOLDER_NO',
                label: '流程单编号',
                width: "10%"
            }, {
                name: 'STATE',
                label: "状态",
                width: "10%",
                editable: true,
                search: true,
                formatter: 'select',
                formatoptions: {
                    value: { 'C': "完成", 'I': "待处理", "A": "处理中", "H": "挂起" }
                }

            }, {
                name: 'TASK_NAME',
                label: "任务名称",
                width: "10%"

            }, {
                name: 'PROCESS_NAME',
                label: "流程名称",
                width: "10%"


            }, {
                name: 'CREATE_DATE',
                label: "创建时间",
                width: "10%"

            }, {
                name: 'OWNER_NAME',
                label: "拥有者",
                width: "10%"

            }, {
                name: 'OWNER_NAME',
                label: "处理人",
                width: "10%"

            }, {
                name: 'DIRECTION',
                label: "方向",
                width: "10%",
                formatter: 'select',
                formatoptions: {
                    value: { 'F': "正常", 'B': "回退" }
                }

            },{
                label: "操作",
                width:"7%",
                sortable: false,
                formatter: function() {
                    return this.flowtemplateHTML;
                }.bind(this)
            }];
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            this.flowtemplateHTML = this.flowtemplate(i18nData);
            return this;
        },
        afterRender: function(a) {
            var taskStatus = [{
                TEXT: "待处理",
                VALUE: "I"
            }, {
                TEXT: "处理中",
                VALUE: "A"
            }, {
                TEXT: "挂起",
                VALUE: "H"
            }, {
                TEXT: "已完成",
                VALUE: "C"
            }];
            this.$('#taskStatusCombox').combobox({
                dataTextField: 'TEXT',
                dataValueField: 'VALUE',
                editable: false,
                dataSource: taskStatus
            }).combobox("value", "I");

            var processStatus = [{
                TEXT: "正在处理",
                VALUE: "A"
            }, {
                TEXT: "完成",
                VALUE: "C"
            }, {
                TEXT: "回退",
                VALUE: "K"
            }, {
                TEXT: "撤回",
                VALUE: "F"
            }, {
                TEXT: "挂起",
                VALUE: "B"
            }, {
                TEXT: "废弃",
                VALUE: "T"
            }];
            this.$("#processStatusCombox").combobox({
                placeholder: '请选择',
                dataTextField: 'TEXT',
                dataValueField: 'VALUE',
                editable: false,
                dataSource: processStatus
            });
            this.logBeginDate = this.$(".js-begin-login-date").datetimepicker();
            this.logEndDate = this.$(".js-end-login-date").datetimepicker();
            this.$qryForm = this.$(".js-form").form();
            this.$processPopEdit = this.$qryForm.find("[name='PROC_DEF_TYPE_ID']").popedit({
                open: function() {
                    fish.popupView({
                        url: "modules/bpmn/popwin/processselpopwin/views/processSelPopWin",
                        close: function(msg) {
                            this.$processPopEdit.popedit("setValue", {
                                Value: msg.CATG_ID,
                                Text: msg.name
                            });
                        }.bind(this)
                    });
                }.bind(this),
                dataTextField: "Text",
                dataValueField: "Value"
            });

            this.$grid = this.$(".js-data-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                pager: true,
                pagebar: true,
                pageData: function() { this.loadGrid(false); }.bind(this),
                onSelectRow: this.rowSelectCallback.bind(this)

            });
            this.qryTaskList();
        },
        loadGrid: function(reset) {
            var rowNum = this.$grid.grid("getGridParam", "rowNum"),
                page = reset ? 1 : this.$grid.grid("getGridParam", "page"),
                sortname = this.$grid.grid("getGridParam", "sortname"),
                sortorder = this.$grid.grid("getGridParam", "sortorder");
            var filter = {
                PAGE_INDEX: page - 1,
                PAGE_SIZE: rowNum
            };
            var data = taskcenterAction.qryTaskListByPager(this.qryCond, filter);
            var count = Number(data.BFM_USER_LIST_COUNT);
            var dataList = data.taskList || [];
            this.$grid.grid("reloadData", {
                'rows': dataList,
                'page': page,
                'records': count
            });
            if (dataList.length > 0) {
                this.$grid.grid("setSelection", dataList[0]);
            } else {
                //先删除原来的按钮
                var findnav = this.$(".ui-nav-btn-group");
                findnav.remove();
            }

        },

        flowPic:function(){
            var selectData = this.$grid.grid("getSelection");
            fish.popupView({
                    url: "modules/bpmn/popwin/flowerpicture/views/flowpic",
                    viewOption: selectData,
                    //close: function() {}.bind(this)
                });
        },

        qryTaskList: function() {
            if (!this.$qryForm.isValid()) {
                return;
            }
            var data = this.$qryForm.form("value");
            !data.HOLDER_NO && delete data.HOLDER_NO;
            !data.PROC_DEF_TYPE_ID && delete data.PROC_DEF_TYPE_ID;
            !data.PROCESS_NAME && delete data.PROCESS_NAME;
            !data.TASK_STATE && delete data.TASK_STATE;
            !data.TASK_NAME && delete data.TASK_NAME;
            !data.HOLDER_STATE && delete data.HOLDER_STATE;
            !data.FROM_DATE && delete data.FROM_DATE;
            !data.TO_DATE && delete data.TO_DATE;
            this.qryCond = data;
            this.loadGrid(true);
        },
        rowSelectCallback: function() {
            var rowdata = this.$grid.jqGrid('getSelection');
            var that = this,
                defaultUrl = 'modules/bpmn/taskcenter/markingpro/views/MarkingMgr';
            var option = {
                    viewOption: {
                        TASK_LIST_ID: rowdata.TASK_LIST_ID,
                        HOLDER_ID: rowdata.HOLDER_ID,
                        HOLDER_NO: rowdata.HOLDER_NO,
                        PROC_INST_ID: rowdata.PROC_INST_ID,
                        url: rowdata.TASK_FORM_PAGE_URL
                    },
                    close: function(msg) {
                        that.loadGrid(false);
                    }
                },
                defaultOption = {
                    url: defaultUrl,
                    height: 550,
                    width: '70%',
                },
                btnOptions = [];
            if (rowdata.STATE == 'C') {
                btnOptions[0] = {
                    caption: '查看',
                    cssprop: "js-add",
                    onClick: function() {
                        var tempOption = fish.extend({}, option, defaultOption);
                        tempOption.viewOption.btn = tempOption.viewOption.url = "";
                        fish.popupView(tempOption);
                    }
                };

            } else {
                var data = taskcenterAction.qryButtonListByTask(rowdata.TEMPLATE_ID),
                    btnList = data.buttonList;
                if (!btnList) { //先删除原来的按钮
                    var findnav = this.$(".ui-nav-btn-group");
                    findnav.remove();
                    return;
                }
                $.each(btnList, function(index, item) {
                    btnOptions.push({
                        caption: item.BTN_NAME,
                        cssprop: "js-add",
                        onClick: function() {
                            if (item.BTN_NAME === '增派' || item.BTN_NAME === '转派' || item.BTN_NAME === '阅处') {
                                var tempOption = fish.extend({}, option, defaultOption);
                                tempOption.viewOption.url = rowdata.TASK_FORM_PAGE_URL;
                                if (item.BTN_NAME != '阅处') { tempOption.viewOption.url = item.PAGE_URL; }

                            } else {
                                var tempOption = option;
                                tempOption.url = item.PAGE_URL;
                            }
                            tempOption.viewOption.btn= item;
                            fish.popupView(tempOption);
                        }
                    });
                });


            }
            var findnav = this.$(".ui-nav-btn-group");
            findnav.remove();
            this.$grid.grid("navButtonAdd", btnOptions);

        },
        resize: function(delta) {
            portal.utils.gridIncHeight(this.$grid, delta);
        }
    });
});
