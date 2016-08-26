/**
 * @class Ext.chooser.Window
 * @extends Ext.window.Window
 * @author Ed Spencer
 * 
 * This is a simple subclass of the built-in Ext.window.Window class. Although it weighs in at 100+ lines, most of this
 * is just configuration. This Window class uses a border layout and creates a DataView in the central region and an
 * information panel in the east. It also sets up a toolbar to enable sorting and filtering of the items in the 
 * DataView. We add a few simple methods to the class at the bottom, see the comments inline for details.
 */
Ext.define('Ext.chooser.Window', {
    extend: 'Ext.window.Window',
    uses: [
        'Ext.layout.container.Border',
        'Ext.form.field.Text',
        'Ext.form.field.ComboBox',
        'Ext.toolbar.TextItem'
    ],
    
    height: 450,
    width : 600,
    title : '选择一个标记图标',
    closeAction: 'hide',
    layout: 'border',
    // modal: true,
    border: false,
    bodyBorder: false,
    
    /**
     * initComponent is a great place to put any code that needs to be run when a new instance of a component is
     * created. Here we just specify the items that will go into our Window, plus the Buttons that we want to appear
     * at the bottom. Finally we call the superclass initComponent.
     */
    initComponent: function() {
        this.items = [
            {
                xtype: 'panel',
                region: 'center',
                autoScroll: true,
                
                items: {
                    xtype: 'iconbrowser',
                    id: 'img-chooser-view',
                    listeners: {
                        scope: this,
                        selectionchange: this.onIconSelect,
                        itemdblclick: this.fireImageSelected
                    }
                }
            }
        ];
        /*
        this.buttons = [
            {
                text: '确定',
                scope: this,
                handler: this.fireImageSelected
            },
            {
                text: '取消',
                scope: this,
                handler: function() {
                    this.hide();
                }
            }
        ];
        */
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
            'selected'
        );
    },
    
 
    
    /**
     * Called whenever the user clicks on an item in the DataView. This tells the info panel in the east region to
     * display the details of the image that was clicked on
     */
    onIconSelect: function(dataview, selections) {
        var selected = selections[0];
    },
    setButton : function(a){
	    this.button = a;
	},
	getButton : function(){
	    return this.button;
	},
    /**
     * Fires the 'selected' event, informing other components that an image has been selected
     */
	
    fireImageSelected: function() {
        var selectedImage = this.down('iconbrowser').selModel.getSelection()[0];
        
        if (selectedImage) {
			this.selectedImage = selectedImage;
            this.fireEvent('selected', selectedImage);
            this.hide();
			this.button.deactivate();
        }
    },
	getSelectedImage : function(icon){
		if(icon){
			return icon;
		}
		if(!this.selectedImage) {
				return "Marker1"
		}
		     return this.selectedImage.get("id")
		}
	});