Ext.define("component.common.FileUploadField", {
    extend: "component.common.TextButtonField",
    alias: "widget.mutifileuploder",
    requires: ["component.common.FileUploadPanel"],
    trigger2Cls: '',
    initComponent: function() {
        var me = this;
        var window = Ext.widget("window",{
        	closeAction: 'hide',
		    constrain: true,
		    plain: true,
		    modal: true,
			width: 300,
			height: 280,
		    layout: 'fit',
		    animCollapse : true,
		    constrain : true,
		    animateTarget : Ext.getBody(),
		    items:[Ext.create('component.common.FileUploadPanel',{
		    	url: me.url
		    })]
		});
        me.window = window;
        me.callParent(arguments);
    },
    handler: function() {
    	var me = this;
		me.window.show();
    }
});