/**
 * @class Ext.chooser.IconBrowser
 * @extends Ext.view.View
 * @author Ed Spencer
 * 
 * This is a really basic subclass of Ext.view.View. All we're really doing here is providing the template that dataview
 * should use (the tpl property below), and a Store to get the data from. In this case we're loading data from a JSON
 * file over AJAX.
 */
Ext.define('Ext.chooser.IconBrowser', {
    extend: 'Ext.view.View',
    alias: 'widget.iconbrowser',
    
    uses: 'Ext.data.Store',
    
	singleSelect: true,
    overItemCls: 'x-view-over',
    itemSelector: 'div.thumb-wrap',
    tpl: [
         '<div class="img-chooser-view">',
            '<tpl for=".">',
            	'<tpl if="type ==  \'nofile\'">',
	                '<div class="nofile"><img src="'+componentPath+'/common/chooser/icons/{thumb}" /></div>',
                '<tpl else>',
	                '<div class="thumb-wrap" ',
                	'<tpl if="type ==  \'folder\'">',
	                	'id="_tpl_f_{_tpl_id}"',
	                '<tpl else>',
	                	'id="_tpl_d_{_tpl_id}"',
	                '</tpl>',
	                ' data-qtip="{name}">',
	                    '<div class="thumb" style="width:{_tpl_width}px;height:{_tpl_height}px;">',
	                    '<tpl if="type ==  \'pic\' && _tpl_showimg == true">',
	                    	'<img src="{url}" />',
	                    '<tpl else>',
	                    	(!Ext.isIE6? '<img src="'+componentPath+'/common/chooser/icons/{thumb}" />' : 
	                    	'<div style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'icons/{thumb}\')"></div>'),
	                	'</tpl>',
	                	'</div>',
	                    '<span>{_tpl_short_name}</span>',
	                    '<tpl if="_tpl_mark_text && _tpl_mark_text.length &gt; 0">',
	                    '<div class="thumb-mark">',
	                    '<tpl for="_tpl_mark_text">',
		                    '{text}<br />',
		                '</tpl>',
		                '</div>',
	                    '</tpl>',
	                '</div>',
                '</tpl>',
            '</tpl>',
         '</div>'
    ],
    
    initComponent: function() {
    	//静态数据的读取过程不应该放在这里
        this.callParent(arguments);
        this.store.sort();
    }
});

//图标
function loadCssFile(filename){
    var fileref = document.createElement('link');
    fileref.setAttribute("rel","stylesheet");
    fileref.setAttribute("type","text/css");
    fileref.setAttribute("href",filename);
    if(typeof fileref != "undefined"){
        document.getElementsByTagName("head")[0].appendChild(fileref);
    }
}
loadCssFile(componentPath+'/common/chooser/chooser.css');