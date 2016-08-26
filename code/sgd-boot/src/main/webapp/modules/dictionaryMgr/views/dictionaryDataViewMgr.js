define([
    "text!modules/dictionaryMgr/templates/dictionaryData.html",
    "modules/dictionaryMgr/actions/dictionaryDataAction",
    "i18n!modules/dictionaryMgr/i18n/dictionaryMgr"
], function(template, dictDataAction, i18nData) {
    return portal.BaseView.extend({
        className: "container_right",
        template: fish.compile(template),
        initialize: function() {
            this.colModel = [{
                name: 'DICTIONARY_ID',
                label: 'DICTIONARY_ID',
                hidden: true
            }, {
                name: 'ID',
                label: "ID",
                hidden: true
            }, {
                name: 'VALUE_NAME',
                label: "值名称",
                width: "40%",
                editable: true,
                search: true,
                editrules: "值名称" + ":required;length[1~255, true]"
            }, {
                name: 'VALUE_CODE',
                label: "值编码",
                width: "20%",
                search: true,
                editable: true,
                editrules: "值编码" + ":required;length[1~255, true]"
            }, {
                name: 'COMMENTS',
                label: "备注",
                editable: true,
                width: "20%"
            }, {
                sortable: false,
                width: "20%",
                formatter: 'actions',
                formatoptions: {
                    editbutton: true,
                    delbutton: true
                }
            }];
            this.on("dictTypeChange", this.dictTypeChange, this);
        },
        render: function() {
            this.$el.html(this.template(i18nData));
            return this;
        },
        afterRender: function() {
            this.$grid = this.$(".js-data-grid").jqGrid({
                colModel: this.colModel,
                pagebar: true,
                datatype: 'json',
                beforeEditRow: function(e, rowid, rowdata, option) {
                    if (option.oper == 'edit') {
                        this.$grid.jqGrid("setColProp", "VALUE_CODE", { editable: false });
                    } else {
                        this.$grid.jqGrid("setColProp", "VALUE_CODE", { editable: true });
                    }
                }.bind(this),
                afterRestoreRow: function(e, rowid, data, options) {
                    var rowdata = this.$grid.jqGrid("getRowData", rowid);
                    var prevRow = null;
                    switch (options.oper) {
                        case 'add':
                            prevRow = this.$grid.jqGrid("getPrevSelection", rowdata);
                            if (prevRow) {
                                 this.$grid.jqGrid("setSelection", prevRow);                             
                            }
                            break;
                        case 'edit':                     
                            break;
                        default:
                            break;
                    }
                }.bind(this),
                beforeSaveRow: function(e, rowid, rowdata, option) {
                    var dictData = {
                        DICTIONARY_ID: this.dictType.ID,
                        VALUE_NAME: rowdata.VALUE_NAME,
                        VALUE_CODE: rowdata.VALUE_CODE,
                        COMMENTS: rowdata.COMMENTS
                    }
                    switch (option.oper) {
                        case 'edit':
                            dictData.ID = rowdata.ID,
                                dictDataAction.updateDictData(dictData, function(data) {
                                    this.$grid.grid("saveRow", rowid, { trigger: false });

                                }.bind(this));
                            break;
                        case 'add':
                            dictDataAction.addDictData(dictData, function(data) {
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
                    fish.confirm("你确定删除该数据字典数据吗？", function() {
                        this.remDictData(rowdata);
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
                            DICTIONARY_ID: this.dictType.ID,
                            VALUE_NAME: "",
                            VALUE_COD: "",
                            COMMENTS: "",
                            STATE: "",
                            inadd: true
                        },
                        position: "last"
                    });
                }.bind(this)
            }]);
            this.$(".js-searchbar").searchbar({
                target: this.$grid
            });
        },
        dictTypeChange: function(dictType) {
            this.dictType = dictType;
            this.qryDictData();
        },
        qryDictData: function() {
            if (this.dictType) {
                var id = this.dictType.ID;
                dictDataAction.qryDictDataByTypeId(id, function(status) {
                    var dataList = status.z_d_r || [];
                    this.$grid.grid("reloadData", { 'rows': dataList });
                    if (dataList.length > 0) {
                        this.$grid.grid("setSelection", dataList[0]);
                    }
                }.bind(this));
            }
        },
        remDictData: function(rowdata) {
            dictDataAction.delDictData(rowdata.ID, function(data) {
                this.$grid.jqGrid('delRowData', rowdata);
                portal.utils.seekBeforeRemRow(this.$grid, rowdata);
            }.bind(this));
        }
    });
});
