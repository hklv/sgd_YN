/*
 * This example features a window with a DataView from which the user can select images to add to a <div> on the page.
 * To create the example we create simple subclasses of Window, DataView and Panel. When the user selects an image
 * we just add it to the page using the insertSelectedImage function below.
 * 
 * Our subclasses all sit under the Ext.chooser namespace so the first thing we do is tell Ext's class loader that it
 * can find those classes in this directory (InfoPanel.js, IconBrowser.js and Window.js). Then we just need to require
 * those files and pass in an onReady callback that will be called as soon as everything is loaded.
 */
Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('uboss.gis', 'public/script/ui/ztesoft/gis/js');
Ext.Loader.setPath('Ext.chooser', 'public/script/ui/ztesoft/gis/js');
Ext.Loader.setPath('Ext.ux', 'public/script/ui/ztesoft/gis/js/ux');

OpenLayers.Control.prototype.setName = function(name){
	this.name = name;
}
OpenLayers.Feature.Vector.prototype.setMyLayer = function(layer){
	this.myLayer = layer;
}
OpenLayers.Feature.Vector.prototype.setFeatureData = function(data){
    this.featureData = data;
}
OpenLayers.Feature.Vector.prototype.getFeatureData = function(data){
    return this.featureData;
}
Array.prototype.remove = function(b) {
	var a = this.indexOf(b);
	if (a >= 0) {
	this.splice(a, 1);
	return true;
	}
	return false;
};
Ext.require([
 'uboss.gis.UbossMap'
]);
var map, layer1,layer2; //complex object of type OpenLayers.Map
var markers,marker,popup;  
var markArr=new Array();

Ext.onReady(function() {
	var bounds = new OpenLayers.Bounds();
	bounds.extend(new OpenLayers.LonLat(56.95472,-19.64501)); 
	bounds.extend(new OpenLayers.LonLat(58.00666,-20.62973)); 
	//bounds.toBBOX(); // returns 4,5,5,6
	//比例尺——屏幕上1米代表多少地图坐标单位；分辨率——屏幕上一个像素代表多少地图坐标单位
		map = new OpenLayers.Map("umap", {
		//maxExtent : new OpenLayers.Bounds(57.24380,-20.54906, 58.08975,-19.82469),
		numZoomLevels : 15,
		
		//maxScale:1/10000,
		//minScale:1/1000000,
		//maxExtent: bounds,   
		maxResolution : 156543.0339,  //- ''float'' --layer能够显示的最大解析度，分辨率
		//minResolution:"auto",
		//minResolution : 1000,  //- ''float'' -- layer能够显示的最小解析度
		//maxExtent: new OpenLayers.Bounds(56.95472, -19.64501, 58.00666, -20.62973),

		//minScale: 5000,  //- ''float'' --layer能够显示的最小比例尺
		units : 'm',
		projection : "EPSG:900913",
		displayProjection : new OpenLayers.Projection("EPSG:4326")
	});

	layer1 = new OpenLayers.Layer.TMS("Map", website, {
		'type' : 'png',
		'getURL' : get_my_url
	});

	layer2 = new OpenLayers.Layer.TMS("Sitelite", website, {
		'type' : 'png',
		'getURL' : get_my_url2
	});
	map.addLayer(layer1);
	//map.addLayer(layer2);

	//enableMarkerLayer(map);

	//map.addControl(new OpenLayers.Control.Scale());
	//map.addControl(new OpenLayers.Control.MousePosition());
	//map.addControl(new OpenLayers.Control.LayerSwitcher());
	//添加平移缩放工具条
	//	map.addControl(new OpenLayers.Control.PanZoomBar({position: new OpenLayers.Pixel(2, 15)}));  
	map.addControl(new OpenLayers.Control.Navigation()); //双击放大,平移
	// map.addControl(new OpenLayers.Control.OverviewMap());  //添加鹰眼图  

	//地图初始化定位 lon:x-axis coodinate
	//lat: y-axis coondinate
	var lonLat = new OpenLayers.LonLat(57.605318,-20.231275);
	lonLat.transform(map.displayProjection, map.getProjectionObject());

	map.setCenter(lonLat, 11);
	/**
	 * 构造UbossMap地图
	 * */
	    var googleMap = new uboss.gis.UbossMap({
			id: 'googleMap',
			map : map,
			listeners: {
				render: function() {
					// By default, "this" will be the object that fired the event.
				}
			}
		});
	  
	    var inParam = {};
		inParam.method = "qryTopNEvent";
		var topEventList = callRemoteFunction("EventManagerService", inParam).topEventList;
		if(topEventList == null){
			return;
		}

		var eventList = [];
		
		var eventlat = topEventList[0].GIS_LAT*1;
		var eventlon = topEventList[0].GIS_LON*1;
		eventList[0] = {lat:eventlat,lon:eventlon,html:"<a onclick='eventClick();''><B>"+topEventList[0].EVENT_NAME+"</B></a>",ifpop:true};
		
		googleMap.showInformations(eventList);

		
/*
		googleMap.showInformations([
					{lat:-20.231275, lon:57.605318,  html:"<a onclick='eventClick();''><B>鍙戠敓涓ラ噸浜嬫晠</B></a>"},
				   {lat:-20.241275, lon:57.615318,  html:'<B>鏍囩顔�</B>'},
				   {lat:-20.251275, lon:57.625318,  html:'<B>鏍囩顔�</B>'},
				])*/

		
   
});

function eventClick(){
	//alert("yes");
}
