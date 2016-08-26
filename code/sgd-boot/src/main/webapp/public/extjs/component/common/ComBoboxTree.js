Ext.define("component.common.ComBoboxTree", {
    extend: "Ext.form.field.Picker",
    alias : 'widget.comBoboxTree',
    requires: ["Ext.tree.Panel"],
    initComponent: function() {
        var me = this;
        Ext.apply(me, {
            fieldLabel: me.fieldLabel,
            labelWidth: me.labelWidth
        });
        me.callParent();
        me.init();
    },
    createPicker: function() {
        var me = this;
        var textField,idField;
        if(me.fields.length != 2) {
            textField = 'text';
            idField = 'id';
            me.fields = [textField, idField];
        } else {
            textField = me.fields[0];
            idField = me.fields[1];
        }
        me.displayField = textField;
        
        var treeproxy = Ext.create('Ext.data.MemoryProxy',{
	    	data: me.storeJson,
		    reader: {
		        type: 'json',
		        root: 'children'
		    }
	    });
        var store = Ext.create('Ext.data.TreeStore', {
		    proxy: treeproxy
        });
        me.picker = new Ext.tree.Panel({
            height: 300,
            autoScroll: true,
            floating: true,
            focusOnToFront: false,
            shadow: true,
            ownerCt: this.ownerCt,
            useArrows: true,
            store: store,
            rootVisible: false
        });
        me.picker.on({
            checkchange: function(record, checked) {
                var checkModel = me.checkModel;
                if (checkModel == 'double') {
                    var root = me.picker.getRootNode();
                    root.cascadeBy(function(node) {
                        if (node.get(textField) != record.get(textField)) {
                            node.set('checked', false);
                        }
                    });
                    if (record.get('leaf') && checked) {
                        me.hiddenValue = record.raw[idField]; // 隐藏值
                        me.setRawValue(record.get(textField)); // 显示值
                    } else {
                        record.set('checked', false);
                        me.hiddenValue = ''; // 隐藏值
                        me.setRawValue(''); // 显示值
                    }
                } else if (checkModel == 'unique') {
                	//清除子父
                	var root = me.picker.getRootNode();
                    if (checked) record.cascadeBy(function(rec) {
                    	if(record != rec) {
                    		rec.set('checked', false);
                    	}
                    });
                    pNode = record.parentNode;
                    for (; pNode != null; pNode = pNode.parentNode) {
                        pNode.set("checked", false);
                    }
                    //
                    var records = me.picker.getView().getChecked(),
                    names = [],
                    values = [];
                    Ext.Array.each(records,
                    function(rec) {
                        names.push(rec.get(textField));
                        values.push(rec.raw[idField]);
                    });
                    me.hiddenValue = values.join(';');
                    me.setRawValue(names.join(';')); // 显示值
                } else {
                    var cascade = me.cascade;
                    if (checked == true) {
                        if (cascade == 'both' || cascade == 'child' || cascade == 'parent') {
                            if (cascade == 'child' || cascade == 'both') {
                                if (!record.get("leaf") && checked) record.cascadeBy(function(record) {
                                    record.set('checked', true);
                                });
                            }
                            if (cascade == 'parent' || cascade == 'both') {
                                pNode = record.parentNode;
                                for (; pNode != null; pNode = pNode.parentNode) {
                                    pNode.set("checked", true);
                                }
                            }
                        }
                    } else if (checked == false) {
                        if (cascade == 'both' || cascade == 'child' || cascade == 'parent') {
                            if (cascade == 'child' || cascade == 'both') {
                                if (!record.get("leaf") && !checked) record.cascadeBy(function(record) {
                                    record.set('checked', false);
                                });
                            }
                        }
                    }
                    var records = me.picker.getView().getChecked(),
                    names = [],
                    values = [];
                    Ext.Array.each(records,
                    function(rec) {
                        names.push(rec.get(textField));
                        values.push(rec.raw[idField]);
                    });
                    me.hiddenValue = values.join(';');
                    me.setRawValue(names.join(';')); // 显示值
                }
            },
            itemclick: function(tree, record, item, index, e, options) {
                var checkModel = me.checkModel;
                if (checkModel == 'single') {
                    if (record.get('leaf')) {
                    	me.hiddenValue = record.raw[idField];
                        me.setRawValue(record.get(textField)); // 显示值
                    } else {
                    	if(me.selectModel == 'leaf' || me.selectModel == null) {
                    		me.hiddenValue = ''; // 隐藏值
                            me.setRawValue(''); // 显示值
                    	} else if(me.selectModel == 'all') {
                    		me.hiddenValue = record.raw[idField]; // 隐藏值
                            me.setRawValue(record.get(textField)); // 显示值
                    	}
                    }
                }
            },
            afterrender: function() {//初始化选中
            	if(me.value) {
                	var checkAry = me.value.split(';');
            		var model = me.picker.getSelectionModel();
            		var selNode = me.getNode(checkAry[0]);
            		if(selNode) {
            			model.select(selNode);	
            		}
            	}
            }
//            ,
//            itemdblclick: function(tree, record, item, index, e, eOpts) {
//            	var checkModel = me.checkModel;
//            	if (checkModel != 'undefined' && record.checked == 'undefined') {
//	            	if(record.childNodes.length == 0) {
//	            		me.picker.hide();
//	            		me.fireEvent('blur', me.picker, e);
//	            	}
//            	}
//            }
        });
        return me.picker;
    },
    alignPicker: function() {
        var me = this,
        picker, isAbove, aboveSfx = '-above';
        if (this.isExpanded) {
            picker = me.getPicker();
            if (me.matchFieldWidth) {
                picker.setWidth(me.bodyEl.getWidth());
            }
            if (picker.isFloating()) {
                picker.alignTo(me.inputEl, "", me.pickerOffset); // ""->tl
                isAbove = picker.el.getY() < me.inputEl.getY();
                me.bodyEl[isAbove ? 'addCls': 'removeCls'](me.openCls + aboveSfx);
                picker.el[isAbove ? 'addCls': 'removeCls'](picker.baseCls + aboveSfx);
            }
        }
    },
    getNode : function(nodeId, parentNode) {
		var me = this;
		if (!parentNode) {
			parentNode = me.store.getRootNode();
		}
		if (parentNode.raw[me.fields[1]] == nodeId) {
			return parentNode;
		}
		if (parentNode.isExpandable()) {
			var childNodes = parentNode.childNodes, length = 0, i;
			if (childNodes) {
				length = childNodes.length;
			}
			for (i = 0; i < length; i++) {
				var child = childNodes[i];
				if (child.isExpandable()) {
					var node = me.getNode(nodeId, child);
					if (node) {
						return node;
					}
				} else {
					if (child.raw[me.fields[1]] == nodeId) {
						return child;
					}
				}
			}
		}
		return null;
	},
	clearNodeChecked: function(nodeId, child) {
		var me = this;
//		var selNodes = me.picker.getSelectionModel().getSelection();
		var selNodes = me.picker.getChecked();
		for(var i=0; i<selNodes.length; i++) {
			if(me.checkModel != 'single') selNodes[i].set('checked', false);
		}
	},
	expandNode: function(node) {
		if(node.parentNode) {
			node.parentNode.expand();
			this.expandNode(node.parentNode);
		}
	},
	setNodeSelected: function(node) {
		var me = this;
		var model = me.picker.getSelectionModel();
		model.select(node);
	},
	setNodeChecked: function(nodeId, checked) {
		if (checked != false) {
			checked = true;
		}
		if(nodeId != null) {
			this.getNode(nodeId).set('checked', checked);
		}
	},
	setTextAndValue: function(text,value){
		this.setRawValue(text);
		this.hiddenValue=value;
	},
	setValues: function(value) {
		var me = this;
		if(value && value != '') {
			me.clearNodeChecked();
			var checkAry = value.split(';');
	        var names = [];
	        var values = [];
			for(var i=0; i<checkAry.length; i++) {
				var checkNode = me.getNode(checkAry[i]);
				if(checkNode){
		            names.push(checkNode.get(me.fields[0]));
		            values.push(checkNode.raw[me.fields[1]]);
		            me.expandNode(checkNode);
		            if(me.checkModel != 'single') checkNode.set('checked', true);
				}
			}
			me.setTextAndValue(names.join(';'), values.join(';'));
		} else {
			me.reset();
		}
	},
	setInitValue: function(){
		var me = this;
		me.setValues(me.value);
	},
	getValue: function(){
    	return this.hiddenValue;
    },
    getSubmitValue: function(){
        return this.hiddenValue;    
    },
	getTextAndValue:function(){
		return [this.getRawValue(),this.hiddenValue];
	},
	reset:function(){
		var me = this;
		me.clearNodeChecked();
		me.setTextAndValue('', '');
	},
	init: function(){
		var me = this;
		me.createPicker();
		me.store = me.picker.store;
		me.setInitValue();
	}
});