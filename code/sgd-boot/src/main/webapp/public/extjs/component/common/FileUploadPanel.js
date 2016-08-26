/*!
* 非flash多文件上传
*/
Ext.define('component.common.FileUploadPanel', {
    extend: 'Ext.form.FormPanel',
    border: false,
    initComponent: function() {
    	var me = this;
    	Ext.apply(me, {
    		fileUpload : true,
    		autoScroll : true,
    		bodyStyle: 'padding: 10px 10px 0 10px;',
            items : [{
				_itemId : 'uf_1',
                xtype: 'container',
                title: '文件',
		  		layout: { 
		 			type: 'hbox', 
		 	  	    align: 'middle', 
		  		    pack: 'left'
		  		},
		  		padding: '0 0 10px 0',
                items: [
                    {
						xtype: 'fileuploadfield',
			            emptyText: '请选择文件...',			            
			            name: 'file',
			            width: 300,
			            buttonText: '选择',
			            allowBlank: false
			            
//			            buttonCfg: {
//			                iconCls: 'upload-icon'
//			            }
					},
                    {
                        xtype: 'button',
                        margins: '5 5 5 5px',
                        text: '上传',
                        textAlign : 'center',
                        width:60,
                        scope : this,
                        handler : this.toSubmit,
                        formBind:true
                    }]
                }
    		]/*,
    		tbar : [
    			{text:'添加',iconCls:'add',handler:this.addField,scope:this},'-',
    			{text:'上传',iconCls:'up',handler:this.toSubmit,scope:this,formBind:true}
    		]*/
    	});
    	me.callParent();
    },
    toSubmit : function(){//文件上传
    	var me = this;
    	this.submit({
            url: this.url,
            success: function(m, o) {
            	var res = Ext.decode(o.response.responseText);
            	me.callback(res);
            },
            failure: function(m, o) {
            }
        });
	},
	addField : function(){//添加文件上传选择框
		var n = this.maxItems || 8 ;
		var k = this.getNextItemNum();
		if(this.items.length>=n){
			Ext.Msg.show({
				title : '提示',
				msg : '最大上传文件数量为'+n,
				icon : Ext.Msg.INFO,
				width : 230,
				buttons : Ext.Msg.OK
			});
			return;
		}else{
			this.add({
				_itemId : 'uf_'+k,
                xtype: 'container',
                title: '文件',
		  		layout: { 
		 			type: 'hbox', 
		 	  	    align: 'middle', 
		  		    pack: 'left'
		  		},
		  		padding: '0 0 10px 0',
                items: [
                    {
						xtype: 'fileuploadfield',
			            emptyText: '请选择文件...',			            
			            name: 'file',
			            width: 300,
			            buttonText: '上传',
			            allowBlank: false
//			            buttonCfg: {
//			                iconCls: 'upload-icon'
//			            }
					}/*,
                    {
                        xtype: 'button',
                        text: '删除',
                        margins: '0 0 0 50px',
                        scope : this,
                        _ownerCtId : 'uf_'+k,
                        handler : this.removeField
                    }*/
                ]
            });
			this.doLayout();
		}		
	},
	getNextItemNum : function(){//获取准备添加的文件选择框索引
		var n = this.items.length || 0;
		return Number(n)+1;
	},
	removeField : function(btn){//移除文件选择框
		var itemId = btn._ownerCtId;
		var items = this.items.items;
		for(var i=0;i<items.length;i++){
			if(items[i]._itemId == itemId){
				this.remove(items[i]);
				this.doLayout();
				return;
			}
		}
	}
});