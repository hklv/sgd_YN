Ext.Loader.setPath('Ext.chooser', './public/extjs/component/common/chooser');

Ext.define("component.common.Chooser", {
    extend: "Ext.chooser.MainPanel",
    alias: "widget.chooser",
    requires: [
       'Ext.button.Button',
       'Ext.data.proxy.Ajax',
       'Ext.chooser.InfoPanel',
       'Ext.chooser.IconBrowser',
       'Ext.ux.DataView.Animated',
       'Ext.toolbar.Spacer'
    ],
    listeners: {
//        selected: function(image) {
//        	this.target.setValue(image.get('url'));
//        }
    }
});