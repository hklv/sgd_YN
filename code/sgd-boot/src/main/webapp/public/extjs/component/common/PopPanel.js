Ext.define('component.common.PopPanel', {
	extend:'Ext.panel.Panel',
	alias: 'widget.popPanel',
	border: false,
	layout: 'border',
    initComponent: function() {
        var me = this;
    	me.items = [{
    		xtype: 'panel',
    		id: 'cm_pop_west_menu',
    		title: '导航',
    		region: 'west',
			collapsible: true,
			split: true,	    		
    		width: 150,
    		layout: 'accordion'
    	},{
    		xtype: 'panel',
    		id: 'cm_pop_center_content',
			region: 'center',
			layout: 'fit',
			items: [{
				xtype: 'tabpanel',
				border: false,
				minTabWidth: 100,
                plugins : Ext.create('Ext.ux.TabCloseMenu'),
				defaults: {
					border: false,
					closable: true
				}
			}]
    	}];
        me.callParent();
        me.addMenuItems(me.basicMenu,'基础功能');
        me.addMenuItems(me.advancedMenu,'高级设置');
    },
    addMenuItems: function(menuAry,menuTitle){
    	var me = this;
    	var west_menu = Ext.getCmp('cm_pop_west_menu');
    	
    	for(var i=0; i<menuAry.length; i++) {
    		menuAry[i].leaf = true;
    	}
    	
    	var treeStore = Ext.create('Ext.data.TreeStore', {
    	    root: {
    	        expanded: true,
    	        children: menuAry
    	    }
    	});

    	var treePanel = Ext.widget('treepanel', {
    		title: menuTitle,
    	    width: 150,
    	    store: treeStore,
    	    rootVisible: false,	
    		listeners: {
            	cellclick: function(tree, td, cellIndex, record, tr, rowIndex, e, eOpts) {
            		me.addTab(record.raw);
            	}/*,
            	itemcontextmenu : function(menutree, record, items, index, e) {  
            		//禁用浏览器的右键相应事件
           	 		e.preventDefault();
           	 		e.stopEvent();
           	 		//禁用浏览器的右键相应事件
           	 		var menus = Ext.create('Ext.menu.Menu');
           	 		var link = record.raw.link;
       	 			menus.add({
       	 				text : "在新窗口中打开",
       	 				iconCls : 'toolbar-view',
    	   	  	    	handler: function() {
    	   	  	    		window.open(webRoot + link);
    	   	  	    	}
       	 	        });
           	 	    menus.showAt(e.getXY());
            	}*/
            }
    	}); 	
    	west_menu.add(treePanel);
    },
    addTab: function(recordData) {
    	var center_content = Ext.getCmp('cm_pop_center_content');
    	var center_tab = center_content.down('tabpanel');
    	
    	var tab = center_tab;
		var link = recordData.link;
		if(!(link == '' || link == null)) {
			var flag = true;
			var newTitle = recordData.text;
			var newId = newTitle+'_main_panel';
			for(var j = 0; j < tab.items.length; j++) {
				if(tab.items.getAt(j).id == newId) {
					flag = false;
					break;
				}
			}
			if(flag) {
       			var newPanel = Ext.widget('panel',{
    				xtype: 'panel',
    				layout: 'fit',
    				closable: true,
    				title: newTitle,
    				id: newId,
					items: [{
						xtype: 'component',
						autoEl: {
							tag: 'iframe',
							style: 'height: 100%; width: 100%; border: none;',
							src: webRoot + link
						},
						listeners: {
							load: {
								element: 'el',
								fn: function () {
									center_content.body.unmask();
								}
							},
							render: function () {
								center_content.body.mask('页面载入中......');
							}
						}
    				}]
			    });
				tab.add(newPanel);
    			tab.setActiveTab(newPanel);
			} else {
				tab.setActiveTab(Ext.getCmp(newId));
			}
		}
    },
    removeTab: function(recordData) {
    	var center_content = Ext.getCmp('cm_pop_center_content');
    	var tab = center_content.down('tabpanel');
    	var newTitle = recordData.text;
		var newId = newTitle+'_main_panel';
		for(var j = 0; j < tab.items.length; j++) {
			var item = tab.items.getAt(j);
			if(item.id == newId) {
				tab.remove(item);
			}
		}    	
    },
    listeners: {
    	render: function() {
    		var me = this;
    		if(me.basicMenu.length > 0 && me.basicMenu[0]['link'] != '') {
    			me.addTab(me.basicMenu[0]);
    		}
    	}
    }
});