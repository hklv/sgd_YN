/**
 * @class Ext.chooser.InfoPanel
 * @extends Ext.panel.Panel
 * @author Ed Spencer
 * 
 * This panel subclass just displays information about an image. We have a simple template set via the tpl property,
 * and a single function (loadRecord) which updates the contents with information about another image.
 */


Ext.define('Ext.chooser.InfoPanel', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.infopanel',
    bodyStyle: '-moz-box-shadow:0 5px 10px #ccd9ea inset;-webkit-box-shadow:0 5px 10px #ccd9ea inset;box-shadow:0 5px 10px #ccd9ea inset;background:#f1f5fb;',
    tpl: [
		'<div class="img-chooser-dlg">',
	        '<div class="details">',
	            '<tpl for=".">',
	                    (!Ext.isIE6? '<img class="thum-img" src="'+componentPath+'/common/chooser/icons/{thumb}" />' : 
	                    '<div class="thum-img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'icons/{thumb}\')"></div>'),
	                '<div class="details-info">',
	                '<tpl if="showDir == true">',
	                    '<p><span>{fileNum} 个对象</span></p>',
	                '<tpl else>',
		                '<p><b>{name}</b> <span>{desc}</span></p>',
	                    '<p><span>创建时间：{createDate}</span></p>',
	                    '<p><span>文件大小：{fileSize}</span><span>创建人：{author}</span></p>',
	                '</tpl>',
	                '</div>',
	            '</tpl>',
	        '</div>',
        '</div>',
    ],
    afterRender: function(){
        this.callParent();
        if (!Ext.isWebKit) {
            this.el.on('click', function(){
                alert('The Sencha Touch examples are intended to work on WebKit browsers. They may not display correctly in other browsers.');
            }, this, {delegate: 'a'});
        }    
    },

    /**
     * Loads a given image record into the panel. Animates the newly-updated panel in from the left over 250ms.
     */
    loadRecord: function(image) {
//        this.body.hide();
    	
		if(typeof(this.tpl.length) == 'undefined') {//排除还未形成tpl的情况,不太好的判断方法
			this.tpl.overwrite(this.body, image.data);
		}
        
//        this.body.slideIn('l', {
//            duration: 250
//        });
    },
    clear: function(){
        this.body.update('');
    }
});