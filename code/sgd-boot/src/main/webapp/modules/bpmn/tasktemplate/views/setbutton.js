define([
    'text!modules/bpmn/tasktemplate/templates/setbutton.html',
    "modules/bpmn/tasktemplate/actions/taskTemplateAction",
    "i18n!modules/bpmn/tasktemplate/i18n/taskTemplateMgr"
], function(setbuttonTemplate, taskTemplateAction, i18nData) {
    var setbuttonTemplate = portal.BaseView.extend({
        template: fish.compile(setbuttonTemplate),
        events: {
            "click .js-ok": 'ok',
            "click .js-single-in": 'buttonSingleIn',
            "click .js-all-in": 'buttonAllIn',
            "click .js-single-out": 'buttonSingleOut',
            "click .js-all-out": 'buttonAllOut'

        },
        initialize: function(options) {
            this.TEMPLATE_ID=options.TEMPLATE_ID;
            this.selectcol=[{
                name: 'BTN_ID',
                label: '',
                width: 10,
                hidden: true,
                key: true
            }, {
                name: 'BTN_NAME',
                label: "按钮名称",
                width: 150

            }, {
                name: 'ALIAS_NAME',
                label: "按钮别名",
                width: 150,
                editable: true

            }, {
                name: 'BTN_SEQ_NBR',
                label: "顺序",
                width: 100,
                editable: true

            }, {
                name: 'FORM_NAME',
                label: "关联表单",
                width: 200

            }, {
                name: 'COMMENTS',
                label: "备注",
                width: 250

            }];
            this.unselectcol=[{
                name: 'BTN_ID',
                label: '',
                width: 10,
                hidden: true,
                key: true
            }, {
                name: 'BTN_NAME',
                label: "按钮名称",
                width: 150

            }, {
                name: 'FORM_NAME',
                label: "所属表单",
                width: 150

            }, {
                name: 'COMMENTS',
                label: "备注",
                width: 250

            }];
            this.rowdata=[];
            this.rowpage=[];
        },
        render: function() {
            this.setElement(this.template(i18nData));
        },
        afterRender: function() {
            this.unselectGrid = this.$(".js-unselect-button").jqGrid({
                colModel: this.unselectcol,
                pager: true,
                pgnumbers:false,
                recordtext:"",
                pginput:false,
                rowNum: 10,
                datatype: 'json',
                pageData: function(page) {this.loadunselectGrid(false,this.TEMPLATE_ID);}.bind(this)
            });

            this.selectGrid = this.$(".js-select-button").jqGrid({
                colModel: this.selectcol,
                cellEdit: true
            });
            this.loadGrid(this.TEMPLATE_ID);
            this.loadunselectGrid(false,this.TEMPLATE_ID);

        },

        loadGrid: function (id) {
            var data=taskTemplateAction.qryButtonListByTask(id);
            var btnList=data.buttonList || [];
            this.selectGrid.jqGrid("clearData");
            this.selectGrid.jqGrid("reloadData",btnList);
        },

        loadunselectGrid: function (reset,id) {
            var pageLength = this.unselectGrid.jqGrid("getGridParam", "rowNum");
            var page = reset ? 1 : this.unselectGrid.jqGrid("getGridParam", "page");
            var ROW_SET_FORMATTER={};
            ROW_SET_FORMATTER.PAGE_SIZE=pageLength;
            ROW_SET_FORMATTER.PAGE_INDEX=page-1;
            var data=taskTemplateAction.qryBtnListNotInTemplate(id,ROW_SET_FORMATTER);
            var count=data.BFM_USER_LIST_COUNT;
            var btnList=data.buttonList || [];
            this.unselectGrid.jqGrid("clearData");
            this.unselectGrid.jqGrid("reloadData",{
                'rows': btnList,
                'page': page,
                'records': count
            });
            if(this.rowdata.length==0) {
                this.rowdata =  this.selectGrid.jqGrid("getRowData");
                for(var i=0;i<this.rowdata.length;i++){
                    this.rowpage[i]=0;
                }
            }else{
                for(var i=0;i<this.rowdata.length;i++){
                    if(this.rowpage[i]!=0){
                        var option={};
                        option.initdata=this.rowdata[i];
                        this.unselectGrid.jqGrid("addRow",option);
                    }
                }
            }
        },

        buttonSingleIn: function () {
            var unRow=this.unselectGrid.jqGrid("getSelection");
            if(unRow.BTN_ID){
                this.addOneButton(unRow,this.selectGrid.jqGrid("getRowData"));
            }else{
                fish.warn({title:'警告',message:'请选择一个可选按钮！'});
            }
        },


        buttonAllIn: function () {
            var unRows=this.unselectGrid.jqGrid("getRowData");
            if(unRows&&unRows.length>0){
                for(var i=0;i<unRows.length;i++) {
                    this.addOneButton(unRows[i], this.selectGrid.jqGrid("getRowData"));
                }
            }else{
                fish.warn({title:'警告',message:'请确认至少有一个可选按钮！'});
            }
        },

        addOneButton:function(unRow,rows){
            var flag=true;
            if(this.selectGrid.jqGrid("getRowData",unRow.BTN_ID).BTN_ID){
                flag=false;
            }
            if(flag){
                for(var i=0;i<this.rowdata.length;i++){
                    if(unRow.BTN_ID==this.rowdata[i].BTN_ID){
                        this.rowpage[i]=0;
                        break;
                    }
                }
                this.unselectGrid.jqGrid("delRowData",unRow.BTN_ID);
                unRow.ALIAS_NAME="";
                unRow.BTN_SEQ_NBR="";
                var option={};
                option.position = "first";
                option.initdata=unRow;
                this.selectGrid.jqGrid("addRow",option);
            }else{
                fish.warn({title:'警告',message:'该按钮已选择！'});
            }
        },

        buttonSingleOut:function(){
            var row=this.selectGrid.jqGrid("getSelection");
            if(row.BTN_ID){
                if(this.unselectGrid.jqGrid("getRowData",row.BTN_ID).BTN_ID){
                    this.selectGrid.jqGrid("delRowData", row.BTN_ID);
                    this.unSelectSave(row);
                }else {
                    var option = {};
                    option.initdata = row;
                    this.unselectGrid.jqGrid("addRow", option);
                    this.selectGrid.jqGrid("delRowData", row.BTN_ID);
                    this.unSelectSave(row);
                }
            }else{
                fish.warn({title:'警告',message:'请选择一个已选按钮！'});
            }
        },

        buttonAllOut:function(){
            var rows=this.selectGrid.jqGrid("getRowData");
            if(rows.length>0){
                for(var i=0;i<rows.length;i++){
                    if(this.unselectGrid.jqGrid("getRowData",rows[i].BTN_ID).BTN_ID){
                        this.selectGrid.jqGrid("delRowData", rows[i].BTN_ID);
                        this.unSelectSave(rows[i]);
                    }else {
                        var option = {};
                        option.initdata = rows[i];
                        this.unselectGrid.jqGrid("addRow", option);
                        this.selectGrid.jqGrid("delRowData", rows[i].BTN_ID);
                        this.unSelectSave(rows[i]);
                    }
                }
            }else{
                fish.warn({title:'警告',message:'请确认至少有一个已选按钮！'});
            }
        },

        unSelectSave: function (row) {
            for(var i=0;i<this.rowdata.length;i++){
                if(row.BTN_ID==this.rowdata[i].BTN_ID){
                    this.rowpage[i]=1;
                    break;
                }
            }
        },

        ok:function() {
            var rows= this.selectGrid.jqGrid("getRowData");
            var BUTTON_LIST=[];
            for(var i=0;i<rows.length;i++){
                var I={};
                I.BTN_ID=rows[i].BTN_ID;
                I.ALIAS_NAME=rows[i].ALIAS_NAME;
                I.BTN_SEQ_NBR=rows[i].BTN_SEQ_NBR;
                BUTTON_LIST[i]=I;
            }
            taskTemplateAction.addBtnToTaskTemplate(this.TEMPLATE_ID,BUTTON_LIST);
            this.popup.close("保存成功！");
        }
    });

    return setbuttonTemplate;
});