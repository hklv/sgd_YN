define([
    "text!modules/commonbiz/templates/DictionTypeMgrTemplate.html",
    "modules/commonbiz/actions/DictionaryAction",
    "i18n!modules/commonbiz/i18n/dictionarymgr",
    "text!modules/common/templates/GridCellDeleteTemplate.html",
    "modules/areamgr/actions/AreaAction"
], function(dictionTypeMgrTpl, dictionaryAction, dictionaryMgr, gridCellDelTemplate) {
    return portal.BaseView.extend({
        className: "container_left",

        template: fish.compile(dictionTypeMgrTpl),
        cellDelTemplate: fish.compile(gridCellDelTemplate),

        events: {

        },

        initialize: function() {
            //this.typeGTree = null;
            //this.jobResource = fish.extend({}, {
            //    MULTI_JOB_SEL_TITLE: i18nStaffOrg.STAFFORG_ORG_JOB_SELECTOR
            //});
            //this.leaderResource = fish.extend({}, {
            //    STAFF_SEL_POP_WIN: i18nStaffOrg.STAFFORG_ORG_LEADER_SELECTOR
            //});
        },

        render: function() {
            this.$el.html(this.template(dictionaryMgr));
            this.delHtml = this.cellDelTemplate(dictionaryMgr);
            return this;
        },

        afterRender: function() { //dom加载完成的事件
            this.typeGTree = this.$(".js-dictionaryType-grid").jqGrid({
                // height:320,
                colModel: [{
                    name: "id",
                    label: "Id",
                    hidden: true,
                    key: true
                }, {
                    name: "name",
                    label: "字典类别名称",
                    search: true,
                    width: "25%"
                }, {
                    name: "code",
                    label: "字典类别编码",
                    width: "25%"
                }, {
                    name: "state",
                    label: "状态",
                    width: "25%"
                }, {
                    name: "comments",
                    label: "备注",
                    width: "25%"
                },{
                    width: "10%",
                    formatter: function() {
                        return this.delHtml;
                    }.bind(this)
                }],
                treeGrid: true,
                exportFeature: function () {
                    return {
                        serviceName: "qryDictionaryTypeList"
                    }
                }.bind(this),
                treeIcons: {
                    plus: 'glyphicon glyphicon-folder-close',
                    minus: 'glyphicon glyphicon-folder-open',
                    leaf: 'glyphicon glyphicon-file'
                },
                expandColumn: "ORG_NAME",
                pager: true,
                datatype: 'json',
                //pageData: function() {this.pageData(false);}.bind(this),
                //onSelectRow: this.rowSelectCallback.bind(this)
                //onChangeRow: this.onChangeRow.bind(this)
            });
            this.$(".js-searchbar").searchbar({
                target: this.$typeGrid
            });
            var data = dictionaryAction.qryDictionaryTypeList();
            console.log(data);
            var orgs = portal.utils.getTree(data, "id", null, null);
            console.log(orgs);
            this.typeGTree.jqGrid("reloadData", {
                'rows': [{name:"测试法律库",code:"刑法类"}]
            });

            //if (orgs && orgs.length > 0) {
            //    this.typeGTree.jqGrid("setSelection", orgs[0]);
            //    var rd = this.typeGTree.jqGrid("getSelection");
            //    if(!rd.isLeaf)
            //        this.typeGTree.jqGrid("expandNode", rd);
            //}
            //dictionaryAction.qryRootOrgListByStaffId(function(data) {
            //    var root = data;
            //    staffOrgAction.qryStaffMasterOrgList(function(data) {
            //        var subList = data.ORG_LIST;
            //        for (var rootKey in root) {
            //            var rootItem = root[rootKey];
            //            for (var subKey in subList) {
            //                var subItem = subList[subKey];
            //                if (rootItem.ORG_ID == subItem.ORG_ID) {
            //                    subItem.PARENT_ORG_ID = null;
            //                    break;
            //                }
            //            }
            //        }
            //        var orgs = portal.utils.getTree(subList, "ORG_ID", "PARENT_ORG_ID", null);
            //        this.typeGTree.jqGrid("reloadData", orgs);
            //        if (orgs && orgs.length > 0) {
            //            this.typeGTree.jqGrid("setSelection", orgs[0]);
            //            var rd = this.typeGTree.jqGrid("getSelection");
            //            if(!rd.isLeaf)
            //                this.typeGTree.jqGrid("expandNode", rd);
            //        }
            //    }.bind(this));
            //}.bind(this));
        },
        //pageData: function(reset, postLoad) {
        //    var qryCondition = this.userQryCondition.toJSON();
        //    UserMgrAction.qryUserListCount(qryCondition, function(data) {
        //        var count = Number(data);
        //        var pageLength = this.$userGrid.jqGrid("getGridParam", "rowNum"),
        //            page = reset ? 1 : this.$userGrid.jqGrid("getGridParam", "page"),
        //            sortname = this.$userGrid.jqGrid("getGridParam", "sortname"),
        //            sortorder = this.$userGrid.jqGrid("getGridParam", "sortorder");
        //        var filter = {
        //            PageIndex: page-1,
        //            PageLen: pageLength
        //        };
        //        if(sortname){
        //            filter.OrderFields = sortname + " " + sortorder;
        //        }
        //        UserMgrAction.qryUserListByPageInfo(qryCondition, filter,
        //            function(data) {
        //                var userList = data || [];
        //                fish.forEach(userList, function(user) {
        //                    this.normalizeDate(user);
        //                }, this);
        //                this.$userGrid.jqGrid("reloadData", {
        //                    'rows': userList,
        //                    'page': page,
        //                    'records': count
        //                });
        //                if (fish.isFunction(postLoad)) {
        //                    postLoad.call(this);
        //                } else {
        //                    if (userList.length > 0) {
        //                        this.$userGrid.jqGrid("setSelection", userList[0]);
        //                    } else {
        //                        this.$(".js-user-detail").form('clear');
        //                        this.$(".js-user-detail").form('disable');
        //                        fish.info(I18N.HINT_SEARCH_MATCH_NULL);
        //                    }
        //                }
        //            }.bind(this)
        //        );
        //    }.bind(this));
        //},

    });
    var data = dictionaryAction.qryDictionaryTypeList();
    console.log(data);
});