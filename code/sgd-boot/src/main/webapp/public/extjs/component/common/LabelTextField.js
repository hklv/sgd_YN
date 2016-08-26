Ext.define('component.common.LabelTextField', {
    extend: 'Ext.container.Container',
    alias: 'widget.labeltextfield',
    layout: 'column',
    initComponent: function() {
        var me = this;
        me.items = [{
        	xtype: 'label',
			columnWidth: 0.2,
			text: me.labelText+":"
		},{
			xtype: 'component',
			mark: 'content',
			columnWidth: 0.8
        }];
        me.callParent(arguments);
    },
    getValue: function() {
    	return this.down('component[mark=content]').html;
    },
    setValue: function(htmlContent) {
    	this.down('component[mark=content]').update(htmlContent);
    }
});