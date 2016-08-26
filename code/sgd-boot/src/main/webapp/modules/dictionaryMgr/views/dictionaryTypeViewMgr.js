define([
    "text!modules/dictionaryMgr/templates/dictionaryType.html",
    "modules/dictionaryMgr/actions/dictionaryTypeAction",
    "i18n!modules/dictionaryMgr/i18n/dictionaryMgr",
], function(template, dictTypeAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_left",
        template: fish.compile(template),
        initialize: function() {
            this.colModel = [{
                name: 'ID',
                label: 'ID',
                hidden: true,
                key: true
            }, {
                name: 'NAME',
                label: "字典类别名称",
                editable: true,
                search: true,
                width: "30%",
                editrules: "字典类别名称" + ":required;length[1~255, true]"
            }, {
                name: 'CODE',
                label: "字典类别编码",
                search: true,
                editable: true,
                width: "30%",
                editrules: "字典类别编码" + ":required;length[1~255, true]"
            }, {
                name: 'COMMENTS',
                label: "备注",
                editable: true,
                width: "20%",
                editrules: "备注" + "length[1~255, true]"
            }, {
                sortable: false,
                width: "20%",
                formatter: 'actions',
                formatoptions: {
                    editbutton: true,
                    delbutton: true,
                }
            }];
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$grid = this.$(".js-type-grid").jqGrid({
                colModel: this.colModel,
                datatype: 'json',
                pager: true,
                pagebar: true,
                onSelectRow: this.changeRow.bind(this),
                pageData: function() { this.loadDictTypeData(false); }.bind(this),
                beforeEditRow: function(e, rowid, rowdata, option) {
                    if (option.oper == 'edit') {
                        this.$grid.jqGrid("setColProp", "CODE", { editable: false });
                    } else {
                        this.$grid.jqGrid("setColProp", "CODE", { editable: true });
                    }
                }.bind(this),
                afterRestoreRow: function(e, rowid, data, options) {
                    var rowdata = this.$grid.jqGrid("getRowData", rowid);
                    var prevRow = null;
                    switch (options.oper) {
                        case 'add':
                            prevRow = this.$grid.jqGrid("getPrevSelection", rowdata);
                            if (prevRow) {
                                setTimeout(function() {
                                    this.$grid.jqGrid("setSelection", prevRow);
                                }.bind(this), 0);
                            }
                            break;
                        case 'edit':
                            break;
                        default:
                            break;
                    }
                }.bind(this),
                beforeSaveRow: function(e, rowid, rowdata, option) {
                    var dictTypeData = {
                        CODE: rowdata.CODE,
                        NAME: rowdata.NAME,
                        COMMENTS: rowdata.COMMENTS
                    };
                    switch (option.oper) {
                        case 'edit':
                            dictTypeData.ID = rowdata.ID,
                                dictTypeAction.updateDictType(dictTypeData, function(data) {
                                    this.$grid.grid("saveRow", rowid, { trigger: false });
                                }.bind(this));
                            break;
                        case 'add':
                            dictTypeAction.addDictType(dictTypeData, function(data) {
                                var addData = portal.utils.filterUpperCaseKey(data);
                                this.$grid.jqGrid("saveRow", rowid, { trigger: false });
                                this.$grid.jqGrid('delRow', rowid, { trigger: false });
                                this.$grid.jqGrid('addRowData', addData, 'last');
                                this.$grid.jqGrid('setSelection', addData);
                            }.bind(this));
                            break;
                        default:
                            break;
                    }
                    return false;
                }.bind(this),
                beforeDeleteRow: function(e, rowid, rowdata) {
                    fish.confirm("你确定删除该数据字典类型吗？", function() {
                        this.remDictType(rowdata);
                    }.bind(this), $.noop);
                    return false;
                }.bind(this)

            });
            this.$grid.grid("navButtonAdd", [{
                caption: "新增",
                cssprop: "js-add",
                onClick: function() {
                    this.$grid.jqGrid("addRow", {
                        initdata: {
                            ID: "",
                            NAME: "",
                            CODE: "",
                            COMMENTS: "",
                            inadd: true
                        },
                        position: "last"
                    });
                }.bind(this)
            }]);
            this.$(".js-searchbar").searchbar({
                target: this.$grid
            });
            this.loadDictTypeData(true);
        },
        remDictType: function(rowdata) {
            dictTypeAction.delDictType(rowdata.ID,
                function(data) {
                    if (data.isSuccess) {
                        this.$grid.jqGrid('delRowData', rowdata);
                        portal.utils.seekBeforeRemRow(this.$grid, rowdata);
                    }
                }.bind(this)
            );
        },
        loadDictTypeData: function(reset) {
            dictTypeAction.qryDictTypeCount(function(count) {
                var count = Number(count.CNT),
                    rowNum = this.$(".js-type-grid").grid("getGridParam", "rowNum"),
                    page = reset ? 1 : this.$(".js-type-grid").grid("getGridParam", "page"),
                    sortname = this.$(".js-type-grid").grid("getGridParam", "sortname"),
                    sortorder = this.$(".js-type-grid").grid("getGridParam", "sortorder");
                var filter = {
                    PageIndex: page - 1,
                    PageLen: rowNum
                };
                if (sortname) {
                    filter.OrderFields = sortname + " " + sortorder;
                }
                dictTypeAction.qryAllDictType(filter,
                    function(status) {
                        var dataList = status.z_d_r || [];
                        this.$grid.grid("reloadData", {
                            'rows': dataList,
                            'page': page,
                            'records': count
                        });
                        if (dataList.length > 0) {
                            this.$grid.grid("setSelection", dataList[0]);
                        } else {
                            fish.info(i18nData.HINT_SEARCH_MATCH_NULL);
                        }
                    }.bind(this)
                );
            }.bind(this));
        },
        changeRow: function() {
            var rowdata = this.$grid.jqGrid('getSelection');
            if (rowdata.inadd) {
                return;
            }
            this.trigger("dictTypeChange", rowdata);
        }
    });
});
