var gridData;
var fileData;
var fileCataType;
var picMgr;
var fileuploader;
var showHistoryWin;
Ext.QuickTips.init();
Ext.define('Ext.chooser.MainPanel', {
    extend: 'Ext.panel.Panel',
    uses: [
        'Ext.layout.container.Border',
        'Ext.form.field.Text',
        'Ext.form.field.ComboBox',
        'Ext.toolbar.TextItem',
        'Ext.layout.container.Fit'
    ],
    alias: "widget.chooserpanel",
    layout: 'border',
    border: false,
    /**
     * initComponent is a great place to put any code that needs to be run when a new instance of a component is
     * created. Here we just specify the items that will go into our Window, plus the Buttons that we want to appear
     * at the bottom. Finally we call the superclass initComponent.
     */
    initComponent: function() {
    	var me = this;
    	var allDatas = me.fileStore.proxy.data;
    	if(!allDatas) {
    		allDatas = [];
    	}
    	this.allDatas = allDatas;//储存使用
    	for(var i=0; i<allDatas.length; i++) {
    		me.fileFormat(allDatas[i]);
    	}
    	
    	var gridStore = Ext.create('Ext.data.Store', {
    		 fields : ['SEQ','OA_FILE_LOG_ID', 'NAME', 'FILE_CODE', 'VERSION_NUM','VALUE_NAME',
    		 'DICTIONARY_ID', 'Al_STATE','UPDATE_DATE','VALUE_NAME'],
    			 proxy : Ext.create('Ext.ux.data.PagingMemoryProxy', {
    			 data : gridData
    		 }),
    		 pageSize : 10
    	 });

    	 gridStore.load({
    	 params : {
    		 start : 0,
    		 limit : 10
    	 }
    	 });
    	 
    	  showHistoryWin = Ext.create('Ext.window.Window', {
    			closeAction : 'hide',
    			constrain : true,
    			title: '历史版本',
    			plain : true,
    			modal : true,
    			width : 600,
    			height : 300,
    			layout : 'border',
    			animCollapse : true,
    			constrain : true,
    			animateTarget : Ext.getBody(),
    			items : [{
    						xtype : 'panel',
    						region : 'center',
    						layout : 'fit',
    						frame : true,
    						items : [{
    							xtype : 'grid',
    							id : 'showHistoryPanel',
    							store : gridStore,
    							forceFit : true,
    							selModel : {
    								selType : 'checkboxmodel'
    							},
    							tbar : [{
    										text : '下载',
    										id : 'uploadButton',
    										iconCls : 'downloadIcon',
    										handler : function() {
    											var selected = Ext.getCmp('showHistoryPanel').selModel.getSelection()[0];
    					         				if(!selected) {
    					         					Ext.Msg.alert('消息','请先选中需要下载的文件。');
    					         					return;
    					         				}
    					         				me._fileDownloadHistory(selected);
    										}
    									}],
    							bbar : {
    								xtype : 'pagingtoolbar',
    								displayInfo : true,
    								store : gridStore,
    								layout : 'hbox'
    							},
    							columns : [{
    										text: '序号',
    										dataIndex: 'SEQ'
    									},{
    										text : '文件名称',
    										dataIndex : 'NAME'
    									}, {
    										text : '文件编号',
    										dataIndex : 'FILE_CODE'
    									}, {
    										text : '版本号',
    										dataIndex : 'VERSION_NUM'
    									}, {
    										text : '类型',
    										dataIndex : 'VALUE_NAME'
    									}, {
    										text : '状态',
    										dataIndex : 'Al_STATE'
    									}, {
    										text : '修改时间',
    										dataIndex : 'UPDATE_DATE'
    									}],
    							listeners : {
    								'itemdblclick' : function(gridItem, record) {
    								}
    							}
    						}]
    					}]
    				// dockedItems : [gridBtnsPanel]
    			});
    	 
    	 fileuploader =  Ext.create('component.common.FileUploadPanel',{
	    	url: me.url,
	    	callback: function(result) {//上传文件成功后的回调
	    		
    			me._filesAdd(me.doFilesAdd, result);
	    	},
	    	failure  : function(
					m,
					o) {
				var res = Ext.decode(o.response.responseText);
				Ext.Msg.alert("提示",res.result);

			}
	    });	
    	
    	var uploaderWin = Ext.widget("window",{
    		id : 'uploaderWin',
    		title : '上传文件',
        	closeAction: 'hide',
		    constrain: true,
		    plain: true,
		    modal: true,
			width: 400,
			height: 130,
		    layout: 'fit',
		    animCollapse : true,
		    constrain : true,
		    animateTarget : Ext.getBody(),
		    items:[fileuploader]
		});
    	
    	me.uploaderWin = uploaderWin;
    	
    	var fileEditorForm = Ext.widget('form',{
    		layout: 'column',
    		border: false,
	 		bodyPadding: 10,
	 		defaults: {
//	   	 		labelAlign: 'right',
	   	 		padding: '5px 10px 5px 10px',
	   	 		columnWidth: 1,
	 	 		labelWidth: 50
	   	 	},
	   	 	defaultType: 'textfield',
			items: [{
				fieldLabel: '名称',
	 	    	name: 'name',
	 	    	allowBlank: false
			}],
			buttons: ['->',{
				text: '确定',
				formBind : true,
				disabled : true,
				handler: function() {
					var act = this.up('window').act;
					var fileName = fileEditorForm.form.findField('name').getValue();
					if(act == 'add') {
						var pid = '';
						if(me.currentNode) {
							pid = me.currentNode.raw.id;
						}
						var fileData = {
//							id: 100,
							text: fileName,
							pid: pid,
							children: []
						};
						me._folderAdd(me.doFolderAdd, fileData);
					} else if(act == 'edit') {
						var fileObj = me.currentFile;
						fileObj.raw['name'] = fileName;
						me._fileEdit(me.doFileEdit, fileObj);	
					}
				}
			},{
				text: '取消',
				handler: function() {
					this.up('window').hide();
				}
			}]
    	});
    	
    	var fileEditorWin = Ext.widget("window",{
        	closeAction: 'hide',
		    constrain: true,
		    plain: true,
		    modal: true,
			width: 300,
			height: 120,
		    layout: 'fit',
		    animCollapse : true,
		    constrain : true,
		    animateTarget : Ext.getBody(),
		    items:[fileEditorForm]
		});
    	
    	me.fileEditorWin = fileEditorWin;
    	
    	//占用进度
    	if(!me.progressBar) {
    		me.progressBar = {
    			text:'',value:0
    		}
    	}
        this.items = [{
            xtype: 'panel',
            region: 'center',
            layout: 'border',
            items: [{
            	xtype: 'panel',
                region: 'center',
                border: false,
                layout: 'fit',
                items: {
                    xtype: 'iconbrowser',
                    store: this.fileStore,
                    autoScroll: true,
                    listeners: {
                        scope: this,//listenres中的this均代表此处指定
                        selectionchange: this.onIconSelect,
                        itemdblclick: this.fireImageSelected,
                        deselect: this.showDirInfo,
                        load: function() {
                        	setFileDatas
                        }
                    }
                },
                tbar: [{
                	
         			text : '返回',
         			iconCls : 'back',
         			mark: 'back',
         			handler : function() {
         				if(!me.currentNode) return;
         				var parentNode = me.currentNode.parentNode;
         				if(parentNode && parentNode.data.id != 'root') {
             				me.updateFileDatas(parentNode);
         				}
         			}
                },{
         			text : '添加文件',
         			iconCls :'page_addIcon',
         			mark: 'upload',
         			handler : function() {
         				if(me.currentNode){
         					if(me.currentNode.data.id=='root'){
         						Ext.Msg.alert("提示", '请先选择文件夹。');
    							return;
         					}
         					
         					if(me.uploadAction) {
             					me.uploadAction();
             				} else {
                 				uploaderWin.show();
             				}
						}else{
							Ext.Msg.alert("提示", '请先创建文件夹再添加文件。');
							return;
						}
         			
         			}
                },{
         			text : '新建文件夹',
         			iconCls :'addIcon',
         			mark: 'add',
         			handler : function() {
         				if(me.currentNode){
         					if(me.currentNode.data.id=='root'){
         						Ext.Msg.alert("提示", '请先选择父文件夹。');
    							return;
         					}}
         					fileEditorWin.setTitle(this.text);
             				fileEditorWin.act = 'add';
             				fileEditorForm.form.reset();
             				fileEditorWin.show();
         			}
                },{
         			text : '修改',
         			iconCls :'page_edit_1Icon',
         			mark: 'edit',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];
         				if(!selected) {
         					if(picMgr){
         						Ext.Msg.alert('消息','请先选中需要修改的图片。');
             					return;
         					}else{
         						Ext.Msg.alert('消息','请先选中需要修改的目录或文件。');
             					return;
         					}
         					
         				}
         				if(selected.data.type != 'folder' && me.editAction) {
							me.editAction();
						} else {
	         				fileEditorWin.setTitle(this.text);
	         				fileEditorWin.act = 'edit';
	         				fileEditorForm.form.findField('name').setValue(me.currentFile.raw.name);
	         				fileEditorWin.show();
						}
         			}
                },{
         			text : '共享',
         			iconCls : 'share',
         			mark: 'share',
         			handler : function() {    
         				
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];         			
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中需要共享的目录或文件。');
         					return;
         				}
         				me.doShare(selected);
         			}
                },{
         			text : '选择文件',
         			iconCls : 'selectFileIcon',
         			mark: 'selectFileIcon',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中文件。');
         					return;
         				}
         				if(selected.raw.type=='folder'){
         					Ext.Msg.alert('消息','很抱歉，只能选择文件。');
         					return;
         				}
         				me.select(selected);
         			}
                },{
         			text : '授权',
         			iconCls : 'shouquan',
         			mark: 'shouquan',
         			handler : function() { 
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];         			
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中需要授权的目录或文件。');
         					return;
         				}
         				me.doShare(selected);
         			}
                },{
         			text : '取消收藏',
         			iconCls : 'cannelShouChang',
         			mark: 'cannelShouChang',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];         			
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中需要取消收藏的目录或文件。');
         					return;
         				}
         				me._cannelShouChang(me.cannelShouChang,selected);
         			}
                },
                {
         			text : '取消共享',
         			iconCls : 'cannelShare',
         			mark: 'cannelShare',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];         			
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中需要取消共享的目录或文件。');
         					return;
         				}
         				me._cannelShare(me.cannelShare,selected);
         			}
                },{
         			text : '删除',
         			iconCls : 'noIcon',
         			mark: 'remove',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];
         				if(!selected) {
         					if(picMgr){
         						Ext.Msg.alert('消息','请先选中需要删除的图片。');
             					return;
         					}else{
         						Ext.Msg.alert('消息','请先选中需要删除的目录或文件。');
             					return;
         					}
         				}
         				me._fileRemove(me.doFileRemove, me.currentFile);
         			}
                },{
         			text : '下载',
         			iconCls : 'downloadIcon',
         			mark: 'download',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中需要下载的文件。');
         					return;
         				}
         				if(selected.raw.type=='folder'){
         					Ext.Msg.alert('消息','很抱歉，只能下载文件。');
         					return;
         				}
         				me._fileDownload(selected);
         			}
                },{
         			text : '收藏',
         			iconCls : 'shouChang',
         			mark: 'shouChang',
         			handler : function() {    
         				
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];         			
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中需要收藏的目录或文件。');
         					return;
         				}
         				me.shouChang(selected);
         			}
                },{
                	xtype: 'cycle',
                	mark: 'moveTo',
       				text: '移动到',
         			renderTo : Ext.getBody(),
						menu : {
							id : 'view-type-menu',
							items : [{
										text : '',
										iconCls : 'folderUpIcon',
										checked: true
									}, {
										text : '个人硬盘',
										iconCls : 'folderUpIcon'
										
									}, {
										text : '公共硬盘',
										iconCls : 'folderUpIcon'
									}]
						},
						changeHandler : function(cycleBtn, activeItem) {
							var selected = me.down('iconbrowser').selModel.getSelection()[0];
	         				if(!selected) {
	         					Ext.Msg.alert('消息','请先选中需要移动的目录或文件。');
	         					return;
	         				}
							me.doMoveToFileDisk(activeItem.text,selected);
						}
                },{
         			text : '设置默认背景',
         			iconCls : 'defaultQiangZhi',
         			mark: 'defaultQiangZhi',
         			handler : function() {
         				var selected = me.down('iconbrowser').selModel.getSelection()[0];
         				if(!selected) {
         					Ext.Msg.alert('消息','请先选中设置默认背景图片。');
         					return;
         				}
         				me.setDesktop(selected);
         			}
                },{
         			text : '历史版本',
         			iconCls : 'log',
         			mark: 'log',
         			handler : function() {

         				var selected = me.down('iconbrowser').selModel.getSelection()[0];
         				if(!selected) {        					
         					Ext.Msg.alert('消息','请先选中需要查看的文件。');
         					return;
         				}else{
         					if(selected.raw.type=='folder'){
         						Ext.Msg.alert('消息','请先选中需要查看的文件。');
             					return;
         					}
         				}
    
         				gridData = me.doShowHistory();         			
         				gridStore.proxy.data = gridData;
						gridStore.reload();
         				showHistoryWin.show();
         			}
                },'->',{
	                xtype: 'textfield',
	                mark: 'search',
	                name : 'filter',
	                fieldLabel: '关键字',
	                labelAlign: 'right',
	                labelWidth: 55,
	                width: 150,
	                listeners: {
	                    scope : this,
	                    buffer: 50,
	                    change: this.filter
	                }
                },' ',{
                    xtype: 'combo',
                    mark: 'order',
                    fieldLabel: '排序',
                    labelAlign: 'right',
                    labelWidth: 55,
                    width: 150,
                    valueField: 'field',
                    displayField: 'label',
                    value: '名称',
                    editable: false,
                    store: Ext.create('Ext.data.Store', {
                        fields: ['field', 'label'],
                        proxy : {
                            type: 'memory',
                            data: [{label: '名称', field: 'name'}, {label: '类型', field: 'type'},{label: '时间', field: 'createDate'}]
                        }
                    }),
                    listeners: {
                        scope : this,
                        select: this.sort
                    }
            	}]
            },{
	            xtype: 'infopanel',
	            region: 'south',
	            border: false,
//	            split: true,
	            height: 80,
	            listeners: {
	            	afterrender: this.initDirInfo
	            }
            }]
    	},{
            xtype: 'treepanel',
            region: 'west',
            title: '目录信息',
    	    rootVisible: false,
			collapsible: true,
            split: true,
            store: this.dirStore,
            width: 150,
            listeners: {
            	itemclick: this.dirSelect,
            	afterrender: this.defaultSelect,
            	itemcontextmenu : this.rightClick
            }
        },{
        	xtype: 'panel',
            region: 'north',
            height: 30,
            layout: 'fit',
        	bodyStyle: 'border-width:1px 1px 0 1px;',//panel下有效
        	mark: 'chooser_tab',
            items: [{
            	xtype: 'toolbar',
            	border: false,
         		items: [{
         			text : '个人硬盘',
         			rawText: '个人硬盘',
         			mark: 'diskbtn',
         			handler : function() {
         				var tool = this.up('toolbar');
         				var btns = Ext.ComponentQuery.query('*[mark=diskbtn]', tool);
         				for(var i=0; i<btns.length; i++) {
         					btns[i].setText(btns[i].rawText);
         				}
     					this.setText('<b>'+this.text+'</b>');
     					
     					fileCataType = 'F1';
     					getData(fileCataType);
         			}
                },'-',{
         			text : '公共硬盘',
         			rawText: '公共硬盘',
         			mark: 'diskbtn',
         			handler : function() {
         				var tool = this.up('toolbar');
         				var btns = Ext.ComponentQuery.query('*[mark=diskbtn]', tool);
         				for(var i=0; i<btns.length; i++) {
         					btns[i].setText(btns[i].rawText);
         				}
     					this.setText('<b>'+this.text+'</b>');
     					
         				fileCataType = 'F2';
         				getData(fileCataType);
         			}
                },'-',{
         			text : '共享硬盘',
         			rawText : '共享硬盘',
         			mark: 'diskbtn',
         			handler : function() {
         				var tool = this.up('toolbar');
         				var btns = Ext.ComponentQuery.query('*[mark=diskbtn]', tool);
         				for(var i=0; i<btns.length; i++) {
         					btns[i].setText(btns[i].rawText);
         				}
     					this.setText('<b>'+this.text+'</b>');
     					
     					fileCataType = 'SHARE';
        				getData(fileCataType);
            				
         			}
                },'-',{
         			text : '我的收藏',
         			rawText : '我的收藏',
         			mark: 'diskbtn',
         			handler : function() {
         				var tool = this.up('toolbar');
         				var btns = Ext.ComponentQuery.query('*[mark=diskbtn]', tool);
         				for(var i=0; i<btns.length; i++) {
         					btns[i].setText(btns[i].rawText);
         				}
     					this.setText('<b>'+this.text+'</b>');
     					fileCataType = 'S';
        				getData(fileCataType);
         			}
                },'->',{
                	xtype: 'progressbar',
                	mark: 'progress',
                	margins: '0 15px 0 0',
                	width: 250,
                	value: me.progressBar.value,
                	text: me.progressBar.text
                }]
            }]
        }];
        
//        this.buttons = [me.buttons];
        
        this.callParent(arguments);
        
        /**
         * Specifies a new event that this component will fire when the user selects an item. The event is fired by the
         * fireImageSelected function below. Other components can listen to this event and take action when it is fired
         */
        this.addEvents(
            /**
             * @event selected
             * Fired whenever the user selects an image by double clicked it or clicking the window's OK button
             * @param {Ext.data.Model} image The image that was selected
             */
            'selected',
            'selectionchange'
        );
        
        this.updateFileDatas();
        
        me.loadVisibleCfg(me.visibleCfg);
    },
    
    /**
     * @private
     * Called whenever the user types in the Filter textfield. Filters the DataView's store
     */
    filter: function(field, newValue) {
        var store = this.down('iconbrowser').store,
            view = this.down('dataview'),
            selModel = view.getSelectionModel(),
            selection = selModel.getSelection()[0];
        
        store.suspendEvents();
        store.clearFilter();
        store.filter({
            property: 'name',
            anyMatch: true,
            value   : newValue
        });
        store.resumeEvents();
        if (selection && store.indexOf(selection) === -1) {
            selModel.clearSelections();
            this.down('infopanel').clear();
        }
        view.refresh();
        
    },
    
    /**
     * @private
     * Called whenever the user changes the sort field using the top toolbar's combobox
     */
    sort: function() {
        var field = this.down('combobox').getValue();
        if(field=='createDate'){
        	  this.down('dataview').store.sort(field,'desc');
        }else{
        	  this.down('dataview').store.sort(field);
        }
    },
    
    /**
     * Called whenever the user clicks on an item in the DataView. This tells the info panel in the east region to
     * display the details of the image that was clicked on
     */
    onIconSelect: function(dataview, selections) {
        var selected = selections[0];
        if (selected) {
        	this.fireEvent('selectionchange', selected);
            this.down('infopanel').loadRecord(selected);
            this.currentFile = selected;//保存当前文件
        }
    },
    
    /**
     * Fires the 'selected' event, informing other components that an image has been selected
     */
    fireImageSelected: function() {
        var selectedImage = this.down('iconbrowser').selModel.getSelection()[0];
        
        if (selectedImage) {
//            this.fireEvent('selected', selectedImage);
        	if(selectedImage.raw['type'] == 'folder') {
        		this.updateFileDatas(selectedImage.raw['nodeObj']);
        	} else {
        		this.fireEvent('selected', selectedImage); 
        	}
//            this.hide();
        }
    },
    loadVisibleCfg: function(visibleCfg) {
    	var me = this;
    	me.visibleCfg=visibleCfg;
    	var treepanel = me.down('treepanel');
        treepanel.setVisible(false);
        var chooser_tab = Ext.ComponentQuery.query('panel[mark=chooser_tab]', me)[0];
        chooser_tab.setVisible(false);
        var infopanel = me.down('infopanel');
        infopanel.setVisible(false);
        var viewSet = ['moveTo','back','upload','add','edit','share','selectFileIcon','remove','search','order','shouquan','cannelShouChang','cannelShare','log','download','defaultQiangZhi','shouChang'];
		var progress_bar = me.down('progressbar');
		if(progress_bar) progress_bar.setVisible(false);
		for(var i=0; i<viewSet.length; i++) {
			var tempObj = Ext.ComponentQuery.query('*[mark='+viewSet[i]+']', me)[0];
			if(tempObj) {
				tempObj.setVisible(false);
			}
		}

        if(visibleCfg) {//板块显示
        	if(visibleCfg.tree) {
                treepanel.setVisible(true);
        	}
        	if(visibleCfg.tab) {
                chooser_tab.setVisible(true);
        	}
        	if(visibleCfg.info) {
        		infopanel.setVisible(true);
        	}
        	if(visibleCfg.progress) {
        		if(progress_bar) progress_bar.setVisible(true);
        	} 
        	if(visibleCfg.menu) {
        		for(var i=0; i<viewSet.length; i++) {
        			if(visibleCfg.menu[viewSet[i]]) {
        				var tempObj = Ext.ComponentQuery.query('*[mark='+viewSet[i]+']', me)[0];
        				if(tempObj) {
        					tempObj.setVisible(true);
        				}
        			}
        		}
        	}

        }   	
    },
    updateUrl: function(url){
    	fileuploader.url=url;
    },
    dirSelect: function(chooser, record, item, index, e, options)  {
    	var me = this.up('chooserpanel');//!this代表的对象不同
    	me.updateFileDatas(record);
    },
    updateFileDatas: function(selNode) {
    	var me = this;
    	if(!selNode) {
    		selNode = me.dirStore.getRootNode().childNodes[0]; 
    	}
    	me.currentNode = selNode;//当前选中树节点
		var model = me.down('treepanel').getSelectionModel();
		model.select(selNode);//改变选中状态
		
    	var fileDatas = [];
    	//添加目录
    	var childNodes = [];
    	if(selNode && selNode.hasChildNodes) {
    		childNodes = selNode.childNodes;
    	}
    	for(var i=0; i<childNodes.length; i++) {
    		fileDatas.push({
    	        name: childNodes[i].data['text'],//用data方便修改
    	        thumb: 'folder.png',
    	        type: 'folder',
    	        createDate: childNodes[i].raw['createDate'],
    	        author: childNodes[i].raw['author'],
    	        nodeId: childNodes[i].raw['id'],
    	        desc: '文件夹',
    	        properties: childNodes[i].raw,
    	        nodeObj: childNodes[i]
    		});
    	}
    	//添加文件
    	var allDatas = me.allDatas;//需要支持更新文件
    	for(var i=0; i<allDatas.length; i++) {
    		if(selNode) {//判断是否存在目录树
        		if(parseInt(allDatas[i]['dirId']) == parseInt(selNode.raw['id'])) {
        			fileDatas.push(allDatas[i]);
        		}
    		} else {
    			fileDatas.push(allDatas[i]);
    		}
    	}
    	
    	if(me.thumbCfg) {
        	for(var i=0; i<fileDatas.length; i++) {
        		fileDatas[i]['_tpl_width'] = me.thumbCfg.width;
        		fileDatas[i]['_tpl_height'] = me.thumbCfg.height;
        		if(me.thumbCfg.showimg) fileDatas[i]['_tpl_showimg'] = me.thumbCfg.showimg;
        	}
        	me.reconfig_fields('add', ['_tpl_width','_tpl_height','_tpl_showimg']);
    	}
    	
    	me.reconfig_fields('add',['_tpl_short_name','_tpl_id','_tpl_mark_text']);
    	//文件名截取
    	for(var i=0; i<fileDatas.length; i++) {
    		fileDatas[i]['_tpl_short_name'] = fileDatas[i]['name'].length > 13 ? me._substr(fileDatas[i]['name'],13) + '...' : fileDatas[i]['name'];
    		fileDatas[i]['_tpl_id'] = fileDatas[i]['type'] == 'folder' ? fileDatas[i]['nodeId'] : fileDatas[i]['id'];
    	}    	
    	
    	me.fileNum = fileDatas.length;//用于显示文件数量
    	if(me.fileNum == 0) {
    		fileDatas = [{
    			thumb: 'nofile.jpg',
    			type: 'nofile'
    		}];
    	}
    	me.fileStore.proxy.data = fileDatas;
    	me.fileStore.reload();
    	
    	me.showDirInfo();
    	
    	//提示
//    	var items = me.down('iconbrowser').store.data.items;
//    	me.tipSet = [];//销毁
//    	for(var i=0; i<items.length; i++) {
//    		var targetId;
//    		if(items[i].data.type == 'folder') {
//    			targetId = '_tpl_f_'+items[i].raw.nodeId;
//    		} else {
//    			targetId = '_tpl_d_'+items[i].raw.id;
//    		}
//    		var ele = Ext.get('targetId');
//    		var tip = Ext.create('Ext.tip.ToolTip', {
//    		    target: ele,
//    		    html: items[i].data.name
//    		});
//    		me.tipSet.push(tip);
//    	}

    	
//    	me.down('iconbrowser').on('itemmouseenter',function(browser, record, item, index, e, eOpts){
//    		var tip = Ext.create('Ext.tip.ToolTip', {
//    		    target: item,
//    		    html: record.data.name
//    		});
//    	});
    },
    setFileDatas: function(fileDatas) {
    	var me = this;
    	me.allDatas = fileDatas;
    	for(var i=0; i<me.allDatas.length; i++) {
    		me.fileFormat(me.allDatas[i]);
    	}
    	me.updateFileDatas();
    },
    setDirDatas: function(dirDatas) {
    	var me = this;
    	me.dirStore.proxy.data = dirDatas;
    	me.dirStore.reload();
    },
    showDirInfo: function() {
    	this.down('infopanel').loadRecord({
    		data: {
    			thumb: 'folder.png',
    			fileNum: this.fileNum,
    			showDir: true
    		}
    	});
    },
    initDirInfo: function() {
    	this.loadRecord({
    		data: {
    			thumb: 'folder.png',
    			fileNum: this.up('chooserpanel').fileNum,//!
    			showDir: true
    		}
    	});
    },
    defaultSelect: function() {
    	var firstNode = this.store.getRootNode().childNodes[0];
		var model = this.getSelectionModel();
		model.select(firstNode);  	
    },
    fileFormat: function(data) {
    	var fileTypeSet = ['doc','docx','xls','xlsx','ppt','pptx','pdf','png','jpg','gif','zip','rar','txt','mp3','pic','GD','GW'];
    	if(fileTypeSet.indexOf(data['type']) != -1) {
    		data['thumb'] = data['type']+'.png';
		} else {
			data['thumb'] = 'file.png';
		}
    	data['desc'] = data['type']+'文件';
    },
    fileEdit: function(fileObj) {
    	var me = this;
    	if(fileObj.raw['type'] == 'folder') {
    		var selNode = me.down('treepanel').store.getNodeById(fileObj.raw['nodeId']);
			selNode.set('text', fileObj.raw['name']);
			selNode.set('type', fileObj.raw['type']);
			selNode.set('fileSize', fileObj.raw['fileSize']);
			selNode.commit();
    	} else {
    		var allDatas = me.allDatas;
        	for(var i=0; i<allDatas.length; i++) {
        		if(parseInt(allDatas[i]['id']) == parseInt(fileObj.raw['id'])) {
        			allDatas[i]['name'] = fileObj.raw['name'];
        			break;
        		}
        	}
    	}
    	me.updateFileDatas(me.currentNode);
    	me.fileEditorWin.hide();
    },
    filesAdd: function(fileDatas) {
    	var me = this;
    	for(var i=0; i<fileDatas.length; i++ ){
    		if(me.currentNode) fileDatas[i]['dirId'] = me.currentNode.data.id;
			me.fileFormat(fileDatas[i]);
			me.allDatas.push(fileDatas[i]);
		}
		me.updateFileDatas(me.currentNode);
		me.fileEditorWin.hide();
    },
    folderAdd: function(fileData) {
    	var me = this, node;
    	var node = me.currentNode;
    	if(!node) {
    		node = me.down('treepanel').store.getRootNode();
    	}
    	node.appendChild(fileData);
    	node.expand();
		me.updateFileDatas(node);
		me.fileEditorWin.hide();
    },
    fileRemove: function(fileObj) {
    	var me = this;
    	if(fileObj == null) return;
    	if(fileObj.raw['type'] == 'folder') {
    		var selNode = me.down('treepanel').store.getNodeById(fileObj.raw['nodeId']);
			selNode.remove();
    	} else {
    		var allDatas = me.allDatas, tempDatas = [];
        	for(var i=0; i<allDatas.length; i++) {
        		if(parseInt(allDatas[i]['id']) != parseInt(fileObj.raw['id'])) {
        			tempDatas.push(allDatas[i]);
        		}
        	}
        	me.allDatas = tempDatas;
    	}
    	me.updateFileDatas(me.currentNode);
    	me.currentFile = null;
    	me.fileEditorWin.hide();
    },
    dirRemove: function(node) {
		var me = this;
		var parentNode = node.parentNode;
		node.remove();
		me.updateFileDatas(parentNode);
    },
    _folderAdd: function(fn, fileData) {
    	var me = this;
    	var result = fn(fileData);
    	if(result.success) {
        	fileData.id = result.data.id;
        	for(key in result.data) {
        		fileData[key] = result.data[key];
        	}
    		me.folderAdd(fileData);
    	} else {
    		Ext.Msg.alert('消息','新增失败!');
    	}
    },
    _fileEdit: function(fn, fileObj) {
    	var me = this;
    	var result = fn(fileObj);
    	if(result.success) {
    		me.fileEdit(fileObj);
    	} else {
    		Ext.Msg.alert('消息','编辑失败!');
    	}
    },
    _fileRemove: function(fn, fileObj) {
    	var me = this;
    	Ext.Msg.confirm('消息','确认删除吗？',function(btn){
			if('yes' == btn){
				var result = fn(fileObj);
		    	if(result.success) {
		    		me.fileRemove(fileObj);
		    	} else {
		    		Ext.Msg.alert('消息','该目录含有子目录或文件，请先删除子目录或文件。');
		    	}
			}
    	});
    },
    _cannelShouChang: function(fn, fileObj) {
    	var me = this;
    	Ext.Msg.confirm('消息','确认取消收藏吗？',function(btn){
			if('yes' == btn){
				var result = fn(fileObj);
		    	if(result.success) {
		    	
		    	} else {
		    		Ext.Msg.alert('消息','取消收藏失败。');
		    	}
			}
    	});
    },
    _cannelShare: function(fn, fileObj) {
    	var me = this;
    	Ext.Msg.confirm('消息','确认取消共享吗？',function(btn){
			if('yes' == btn){
				var result = fn(fileObj);
		    	if(result.success) {
		    	} else {
		    		Ext.Msg.alert('消息','取消共享失败。');
		    	}
			}
    	});
    },
    _filesAdd: function(fn, jsonRes) {
    	var me = this;
    	var result = fn(jsonRes);
    	if(result.success) {
    		me.filesAdd(result.data);
    		Ext.getCmp('uploaderWin').hide();
    	} else {
    		Ext.Msg.alert('消息','上传失败!');
    	}	
    },
    _dirRemove: function(fn, node) {
    	var me = this;
     	Ext.Msg.confirm('消息','确认删除吗？',function(btn){
			if('yes' == btn){
				var result = fn(node);
		    	if(result.success) {
		    		me.doDirRemove(fileObj);
		    	} else {
		    		Ext.Msg.alert('消息','该目录含有子目录或文件，请先删除子目录或文件。');
		    	}
			}
    	});
    
    },
    _substr: function(str,len) {
    	var strlen = 0; 
    	var s = "";
    	for(var i = 0;i < str.length;i++) {
    		if(str.charCodeAt(i) > 128) {
    			strlen += 2;
    			}else{ 
    			strlen++;
    		}
    		s += str.charAt(i);
    		if(strlen >= len){ 
    			return s ;
    		}
    	}
    	return s;
    },
    reconfig_fields: function(method, fieldsAry) {
    	var me = this;
    	var exsitFields = me.fileStore.proxy.model.getFields();
    	var tempFields = [];
    	for(var i=0; i<exsitFields.length; i++) {
    		tempFields.push(exsitFields[i]['name']);
    	}
    	if(method == "add") {
        	for(var i=0; i<fieldsAry.length; i++) {
        		tempFields.push(fieldsAry[i]);
        	}
    	} else if(method == "remove") {
        	for(var i=0; i<fieldsAry.length; i++) {
        		for(var j=0; j<tempFields.length; j++) {
        			if(tempFields[j] == fieldsAry[i]) {
        				tempFields.splice(i,1);
        				i--;
            		}
        		}
        	}   		
    	}
    	me.fileStore.proxy.model.setFields(tempFields);
    },
    rightClick: function(tree, record, item, index, e, eOpts) {
		// 禁用浏览器的右键相应事件
		e.preventDefault();
		e.stopEvent();
		var me = tree.up('chooserpanel');
    	if(me.visibleCfg && me.visibleCfg.treemenu) {
    		var menu = Ext.create('Ext.menu.Menu',{mark: 'chooser_menu'});
    		menu.add([{
    			text : "删除",
    			iconCls : 'noIcon',
    			handler : function() {
    			 	Ext.Msg.confirm('消息','确认删除吗？',function(btn){
    					if('yes' == btn){
    				    		me.doDirRemove(record);
    					}
    		    	});
    					//	me._dirRemove(me.doDirRemove, record);
    			}
    		}]);
    		menu.showAt(e.getXY());
    	} 
	}
});
