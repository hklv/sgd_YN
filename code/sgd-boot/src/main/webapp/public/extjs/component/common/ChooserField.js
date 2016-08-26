Ext.define("component.common.ChooserField", {
    extend: "component.common.TextButtonField",
    alias: "widget.chooserField",
    requires: ["component.common.Chooser"],
    initComponent: function() {
        var me = this;
//        var window = Ext.create("component.common.Chooser",{
//        	fileStore: me.fileStore,
//        	dirStore: me.dirStore,
//        	title: me.title,
//		 	target: me
//		});
//        me.window = window;
        me.callParent(arguments);
    },
    handler: function() {
    	var me = this;
		me.window.show();
    }
});