/**
 * 文本框+按钮组件
 */
Ext.define('component.common.TextButtonField', {
    extend: 'Ext.form.field.Trigger',
    alias: 'widget.textButtonField',
    trigger1Cls: Ext.baseCSSPrefix + 'form-search-trigger',
    trigger2Cls: Ext.baseCSSPrefix + 'form-clear-trigger',
    initComponent: function() {
        var me = this;
        me.hiddenValue=me.value;
        me.value=me.text;
        if(me.value == null || me.hiddenValue == null) {
        	me.clear();
        }
        me.callParent(arguments);
    },
	onFocus:function(){
		if(!this.editable){
			this.onTrigger1Click(arguments);
			this.blur();
		}
	},
    afterRender: function(){
        this.callParent();
    },
    clear:function(){
    	this.setTextAndValue("","");
    },
	reset:function(){
		this.setTextAndValue("","");
	},
	setTextAndValue:function(text,value){
		this.setRawValue(text);
		this.hiddenValue=value;
	},
	getTextAndValue:function(){
		return [this.getRawValue(),this.hiddenValue];
	},
	getValue:function() {
		return this.hiddenValue;
	},
	getSubmitValue:function() {//提交form需要
		return this.hiddenValue;
	},
    onTrigger1Click : function(){
    	var me=this;
    	if(this.handler){
    		this.handler(me);
    	}
    },
    onTrigger2Click : function(){
    	this.clear();
    }
});