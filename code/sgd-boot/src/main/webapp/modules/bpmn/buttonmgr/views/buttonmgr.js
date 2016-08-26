define([
    'text!modules/bpmn/buttonmgr/templates/buttonMgrTemplate.html',
    '../actions/ButtonMgrAction',
    'modules/bpmn/buttonmgr/models/ButtonItem',
], function(buttonMgrTpl, buttonMgrAction, buttonItem) {
    return portal.BaseView.extend({
        template: fish.compile(buttonMgrTpl),
        events: {
            "click .js-btn-new": 'newBtn',
            "click .js-btn-edit": 'editBtn',
            "click .js-btn-ok": 'ok',
            "click .js-btn-cancel": 'cancel'         
        },
        initialize: function() {
            this.colModel = [{
                name: 'BTN_ID',
                label: '',
                key: true,
                hidden: true
            }, {
                name: 'BTN_NAME',
                label: "按钮名称",
                width: "30%",
                editable: true,
                search: true
            }, {
                name: 'PAGE_URL',
                label: "URL",
                editable: true,
                width: "30%"
            }, {
                name: 'COMMENTS',
                label: "备注",
                editable: true,
                width: "30%"
            }, {
                formatter: 'actions',
                sortable: false,
                width: "10%",
                formatoptions: {
                    editbutton: false,
                    delbutton: true
                }
            }];
            this.buttonItem = new buttonItem();
            this.listenTo(this.buttonItem, "change", this.BtnItemUpdated);
            this.on("delrow", this.delRowConfirm);
        },
        render: function() {
            this.$el.html(this.template());
        },
        afterRender: function() {
            this.$grid = this.$(".js-btn-grid").jqGrid({
                cached: true,
                colModel: this.colModel,
                pager: true,
                datatype: 'json',
                gridComplete: function() {
                    var $grid = this.$(".js-btn-grid");
                    $grid.find("span[data-action='del']").tooltip({
                        placement: 'bottom'
                    });
                }.bind(this),
                onSelectRow: this.rowSelectCallback.bind(this),
                beforeDeleteRow: function(e, rowid, data) {
                    fish.confirm("你确定要删除该按钮吗？", function() {
                        this.trigger("delrow", data);
                    }.bind(this), $.noop);
                    return false;
                }.bind(this),
                pageData: function(page, rowNum, sortname, sortorder) {
                    this.loadGrid();

                }.bind(this)
            });
            this.$grid.prev().find("[data-toggle='searchbar']").searchbar({ target: this.$grid });
            this.loadGrid();
            this.$(".js-btn-detail").form();
        },
        //载入数据
        loadGrid: function($grid, page, rowNum, sortname, sortorder) {
            var page = this.$grid.jqGrid("getGridParam", "page")
                //if (!page) page = 1;
            if (!rowNum) rowNum = this.$grid.jqGrid("getGridParam", "rowNum");
            var filter = {
                PAGE_INDEX: page - 1,
                PAGE_SIZE: rowNum
            };
            if (sortname) {
                filter.OrderFields = sortname + " " + sortorder;
            }
            //查询所有按钮接口
            var data = buttonMgrAction.qryButtonList(filter);
            console.log(data);
            var buttonList = data.buttonList||[];
            var count = data.BFM_USER_LIST_COUNT;
            this.$grid.jqGrid("reloadData", {
                'rows': buttonList,
                'page': page,
                'records': count
            });
            if (buttonList.length > 0) {
                this.$grid.jqGrid("setSelection", buttonList[0]);
            }
        },
        //选择事件
        rowSelectCallback: function(e, rowid, state) {
            var $grid = this.$(".js-btn-grid"),
                rowdata = $grid.jqGrid('getRowData', rowid);
            this.buttonItem.clear({ silent: true });
            this.buttonItem.set(rowdata);
            //按钮ID
            this.binId = rowdata.BTN_ID;
        },
        BtnItemUpdated: function() {
            var $form = this.$(".js-btn-detail");
            $form.form("value", this.buttonItem.toJSON());
            $form.form("disable");
            //切换按钮隐藏
            this.$(".js-btn-cancel").parent().hide();
            this.$(".js-btn-cancel").parent().prev().show();
        },
        newBtn: function() {
            this.$(".js-btn-new").parent().hide();
            this.$(".js-btn-new").parent().next().show();
            this.$(".js-btn-detail").form('enable');
            this.$(".js-btn-detail").form('clear');
            this.$(".js-btn-ok").data("type", "new");
        },

        editBtn: function() {
            this.$(".js-btn-edit").parent().hide();
            this.$(".js-btn-edit").parent().next().show();
            this.$(".js-btn-detail").form('enable');
            this.$(".js-btn-ok").data("type", "edit");
        },
        delRowConfirm: function(msg) {
            var rowdata = msg,
                $grid = this.$(".js-btn-grid");
            if (buttonMgrAction.delButton(rowdata.BTN_ID)) {
                var nextrow = $grid.jqGrid("getNextSelection", rowdata),
                    prevrow = $grid.jqGrid("getPrevSelection", rowdata);
                if (nextrow) {
                    $grid.jqGrid('setSelection', nextrow);
                } else if (prevrow) {
                    $grid.jqGrid('setSelection', prevrow);
                }
                $grid.jqGrid('delRowData', rowdata, { trigger: false });
                fish.success("删除成功");
            };
        },
        ok: function() {
            var $grid = this.$(".js-btn-grid"),
                $ok = this.$(".js-btn-ok"),
                $form = this.$(".js-btn-detail");
            switch ($ok.data("type")) {
                case "new":
                    if ($form.isValid()) {
                        var inputbtn = new buttonItem($form.form("value"));
                        if (buttonMgrAction.addButtonList(inputbtn.toJSON())) {                           
                            fish.success("添加成功");
                            this.buttonItem.clear({ silent: true });
                            $grid.jqGrid("addRowData", this.buttonItem.toJSON(), 'last');
                            $grid.jqGrid("setSelection", this.buttonItem.toJSON());
                            this.loadGrid();
                        }
                    }
                    break;
                case "edit":
                    if ($form.isValid()) {
                        var inputBtn = new buttonItem($form.form("value"));                        
                        inputBtn.attributes.BTN_ID = this.binId;                        
                        if (buttonMgrAction.updateButton(inputBtn.toJSON())) {
                            fish.success("编辑成功");
                            $grid.jqGrid("setSelection", inputBtn.toJSON());
                            this.loadGrid();
                        }
                    }
                    break;
                default:
                    break;
            }
        },
        cancel: function() {
            this.$(".js-btn-cancel").parent().hide();
            this.$(".js-btn-cancel").parent().prev().show();
            this.$(".js-btn-detail").form('disable');
            this.$(".js-btn-detail").resetValid();
            this.buttonItem.trigger("change", this.buttonItem);
        },
        resize: function(delta) {
            //计算左边高度
            portal.utils.gridIncHeight(this.$(".js-btn-grid"), delta);           
            var subHeight = this.$(".container_left").height() - this.$(".js-btn-tab > .ui-tabs-nav").outerHeight()
            this.$(".js-btn-tab > .ui-tabs-panel").outerHeight(subHeight);

        }
    });
});
