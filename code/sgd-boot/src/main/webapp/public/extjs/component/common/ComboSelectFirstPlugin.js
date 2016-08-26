Ext.define("component.common.ComboSelectFirstPlugin", {
	extend : 'Ext.AbstractPlugin',
	alias : 'widget.comboSelectFirstPlugin',
	init : function(combo) {
		if (combo.queryMode == "local") {
			if(!Ext.isEmpty(combo.value)){
				combo.setValue(combo.value);
			}
			else if (!Ext.isEmpty(combo.store.getRange()[0])) {
				combo.setValue(combo.store.getRange()[0].data[combo.valueField]);
			}
		} else {
			combo.store.on("load", function(store, records, options) {
				if (!Ext.isEmpty(store.getRange()[0])) {
					combo.setValue(combo.value?combo.value:(store.getRange()[0].data[combo.valueField]));
				}
			})
		}
		this.callParent(arguments);
	}
});
