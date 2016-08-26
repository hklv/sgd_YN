Ext.ns('uboss.gis');



/**
 * @class Ext.chooser.IconBrowser
 * @extends Ext.view.View
 * @author Ed Spencer
 * 
 * This is a really basic subclass of Ext.view.View. All we're really doing here is providing the template that dataview
 * should use (the tpl property below), and a Store to get the data from. In this case we're loading data from a JSON
 * file over AJAX.
 */
Ext.define('uboss.gis.UbossMap', {
	mixins: {
        observable: 'Ext.util.Observable'
    },
	uses: [
		'Ext.button.Button',
		'Ext.data.proxy.Ajax',
		'Ext.chooser.InfoPanel',
		'Ext.chooser.IconBrowser',
		'Ext.chooser.Window',
		'Ext.ux.DataView.Animated',
		'Ext.toolbar.Spacer'
	],
	constructor: function (config) {
        // The Observable constructor copies all of the properties of `config` on
        // to `this` using Ext.apply. Further, the `listeners` property is
        // processed to add listeners.
        //

        this.mixins.observable.constructor.call(this, config);
		this.addEvents(
            'render',
            'quit',
			'afterSetUp'
        );
		
		
		 /*
	     * Here is where we create the window from which the user can select images to insert into the 'images' div.
	     * This window is a simple subclass of Ext.window.Window, and you can see its source code in Window.js.
	     * All we do here is attach a listener for when the 'selected' event is fired - when this happens it means
	     * the user has double clicked an image in the window so we call our insertSelectedImage function to add it
	     * to the DOM (see below).
	     */
	    this.markWin = Ext.create('Ext.chooser.Window', {
	        id: 'markWin-dlg'
	    });
		
		var SHADOW_Z_INDEX = 10;
		var MARKER_Z_INDEX = 11;
		var DIAMETER = 200;
		var NUMBER_OF_FEATURES = 15;
		
		var myUObject = this;
		
		var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
		renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
		
		this.navigationHistory = new OpenLayers.Control.NavigationHistory();
		
		
		/**
		* 测量符合
		*/
		var measureSymbolizers = {
            "Point": {
                pointRadius: 4,
                graphicName: "square",
                fillColor: "white",
                fillOpacity: 1,
                strokeWidth: 1,
                strokeOpacity: 1,
                strokeColor: "#333333"
            },
            "Line": {
                strokeWidth: 3,
                strokeOpacity: 1,
                strokeColor: "red",
                strokeDashstyle: "dash"
            },
            "Polygon": {
                strokeWidth: 2,
                strokeOpacity: 1,
                strokeColor: "red",
                fillColor: "white",
                fillOpacity: 0.3
            }
        };
		/**
		* 测量工具的显示样式设置
		*/
        var measureStyle = new OpenLayers.Style();
        measureStyle.addRules([new OpenLayers.Rule({symbolizer: measureSymbolizers})]);
        this.measureStyleMap = new OpenLayers.StyleMap({"default": measureStyle}); 
		
		this.measureWindow = new Ext.Window({
					layout:'fit',
					tbar : [
							{
								text : '<font style="font-size:large">重测</font>',
								handler : function() {
								myUObject.toolbars['measureLine'].cancel();
								myUObject.toolbars['measurePolygon'].cancel();
								document.getElementById('output').innerHTML = "";
								},
								scale :'large'
							}
					],
					title : '<font style="font-size:large">测量数据</font>',
					width:200,
					height:120,
					closeAction:'hide',
					html :'<div id="output" style="padding-top: 10px; padding-left: 10px;font-size:large"> </div>',
					pageX :500,
					pageY :0
		});		

		var arr = new Array();
		for(var i=1;i<42;i++){
		  var name = "Marker"+i;
		  var a = new OpenLayers.Rule({
								symbolizer: {
									externalGraphic: "public/script/ui/ztesoft/gis/icons/"+name+".png",
									graphicOpacity: 1.0,
									graphicYOffset: -30,
									graphicZIndex: MARKER_Z_INDEX,
									pointRadius: 20
								},
								filter: new OpenLayers.Filter.Comparison({
									type: "==",
									property: "iType",
									value: name
								})
							})
		  arr.push(a);
		}

		var iconArr = ["normal","severe"];
			for(var i=0;i<iconArr.length;i++){
				  var name = iconArr[i];
				  var a = new OpenLayers.Rule({
										symbolizer: {
											externalGraphic: "public/script/ui/ztesoft/gis/event_icons/"+name+".png",
											graphicOpacity: 1.0,
											graphicYOffset: -15,
											graphicZIndex: MARKER_Z_INDEX,
											pointRadius: 10
										},
										filter: new OpenLayers.Filter.Comparison({
											type: "==",
											property: "iType",
											value: name
										})
									})
				  arr.push(a);
				}
			
		 this.informationLayer = new OpenLayers.Layer.Vector("information", {
				styleMap: new OpenLayers.StyleMap({
					'default': new OpenLayers.Style(null, {
						rules: arr
					}),
					"select": new OpenLayers.Style(null, {
						rules: [
								new OpenLayers.Rule({
									symbolizer: {
									//	externalGraphic: "public/script/ui/ztesoft/gis/icons/Marker4.png",
										graphicOpacity: 1.0,
										graphicYOffset: -30,
										graphicZIndex: MARKER_Z_INDEX,
										pointRadius: 20
									}
								})
							]
						})
					}),
				    eventListeners:{
						'featureselected':function(evt){
							var feature = evt.feature;
							var html = feature.featureData.html
							var popup = new OpenLayers.Popup.FramedCloud("popup",
								OpenLayers.LonLat.fromString(feature.geometry.toShortString()),
								null,
								"<div>"+html+"</div>",
								null,
								true,
								function(evt){
									myUObject.onPopupClose3(feature);
								}
							);
							feature.popup = popup;
							//debugger;
							myUObject.map.addPopup(popup);
							myUObject.informationLayer.selectedFeatures.push(feature);
							//如果传入了点击回调方法
							if(myUObject.eventClick){
								myUObject.eventClick(feature.featureData.eventId);
							}
							
						},
						'featureunselected':function(evt){
							var feature = evt.feature;
							myUObject.onPopupClose3(feature)
						}
				},
				renderers: renderer
			});

		this.informationDrawControls = new OpenLayers.Control.DrawFeature(this.informationLayer,
            OpenLayers.Handler.Point);
		this.informationDrawControls.events.on({'featureadded' : function(a){
				var inforDiv = Ext.getDom('inforDiv');
				if(inforDiv){
				  inforDiv = inforDiv.innerHTML;
				}else{
				  inforDiv = "";
				}
			myUObject.informationAdd(a,myUObject,inforDiv);
		}});
		/**
		* 工具箱
		*/
		this.toolbars = {
			   'navigate':new OpenLayers.Control.Navigation({
						displayClass: 'olControlNavigate',
						title: "Pan view"
				}),
				'zoomBox' : new OpenLayers.Control.ZoomBox({
					displayClass: 'olControlZoomBox',
					alwaysZoom:true,
					title:"Zoom in for area"
						
				}),
				'zoomIn' : new OpenLayers.Control.ZoomIn({
					displayClass: 'olControlZoomIn',
					title: "Zoom in"
						
				}),
				'zoomOut' : new OpenLayers.Control.ZoomOut({
					displayClass: 'olControlZoomOut',
					title: "Zoom out"
				}),	
				'zoomMax' : new OpenLayers.Control.ZoomToMaxExtent({
					displayClass: 'olControlZoomToMaxExtent',
					title: "Entire area"
				}),	
				'previousView' :  new OpenLayers.Control.Button({
					displayClass: 'olControlPreviousView',
					trigger: function(a){
						var aa = myUObject.navigationHistory
						aa.previousTrigger();
					},
					title:"Previous View"
				}),
				'nextView' :  new OpenLayers.Control.Button({
					displayClass: 'olControlNextView',
					trigger: function(a){
					    var aa = myUObject.navigationHistory
						aa.nextTrigger();
					},
					title:"Next View"
				}),
				
				'mark' : new OpenLayers.Control.Button({
					displayClass: 'olControlMarkButton',trigger: function(a){myUObject.showMarkWind(a,myUObject)},
					title: "Mark"
				 }),
				 'select' : new OpenLayers.Control.Button({
					displayClass: 'olControlSelectTargetButton',trigger: function(a){myUObject.selectFunction(a,myUObject)},
					title: "Select"
				 }),
				 'showAllTarget' : new OpenLayers.Control.Button({
					displayClass: 'olControlShowAllTargetButton',trigger: function(a){myUObject.showAllTargetFunction(a,myUObject)},
					title: "Show  targets"
				 }),
				 'trackFirstTarget' : new OpenLayers.Control.Button({
					displayClass: 'olControlTrackFirstTargetButton',trigger: function(a){myUObject.trackFirstTargetFunction(a,myUObject)},
					title: "Tracking a target"
				 }),
				 'information' : new OpenLayers.Control.Button({
					displayClass: 'olControlInformationButton',trigger: function(a){myUObject.informationFunction(a,myUObject);},
					title: "Click for info"
				 }),
				 'measureLine' : new OpenLayers.Control.Measure(
					OpenLayers.Handler.Path, {
							displayClass: 'olControlDrawFeaturePath',
							persist: true,
							handlerOptions: {
								layerOptions: {
									renderers: renderer,
									styleMap: this.measureStyleMap
								}
							},
							title: "Measure  distance"
						}
				 ),
				 'measurePolygon' : new OpenLayers.Control.Measure(
					OpenLayers.Handler.Polygon, {
						displayClass: 'olControlDrawFeaturePolygon',
						persist: true,
						handlerOptions: {
							layerOptions: {
								renderers: renderer,
								styleMap: this.measureStyleMap
							}
						},
						title: "Measure area"
					}
				)
			};
		
		
		/**
		*	给工具添加自定义事件处理函数
		*/
		for(var key in this.toolbars){
		    if(key=="mark") continue;
        	var control = this.toolbars[key];
        	if(key=="measureLine" || key=="measurePolygon"){
                control.geodesic = true;
                control.setImmediate(true);
                control.events.on({
                    "measure": function(event){myUObject.handleMeasurements(event,myUObject);},
                    "measurepartial": function(event){myUObject.handleMeasurements(event,myUObject);},
                    "activate" : function(a){myUObject.buttonAction(a,myUObject);} 
                });
        	}else{
        		control.events.on({
                    "activate" : function(a){myUObject.buttonAction(a,myUObject);} 
                });
        	}
        	control.setName(key);
        }
		
		
		
		 this.selectControl =  new OpenLayers.Control.SelectFeature(
        		[this.informationLayer,],
                {
                    clickout: true, toggle: false,
                    multiple: false, hover: false,
                    toggleKey: "ctrlKey", // ctrl key removes from selection
                    multipleKey: "shiftKey", // shift key adds to selection
                    box : true,
                    onSelect : function(a){
        			  myUObject.selectFeatureFunction(a,myUObject);
        			},
                    onUnselect : function(a){
        			  myUObject.unselectFeatureFunction(a,myUObject);
        			}
                }
            );
			/**
			* 右边工具条
			*/
			var rightTools = new OpenLayers.Control.Panel({
				displayClass: 'olControlMarkPanel'
			});
			
			/**
			* 左边工具条
			*/	
			var leftTools =  new OpenLayers.Control.Panel({
				displayClass: 'olControlMapToolbar'
			});
			
			leftTools.addControls([this.toolbars["zoomIn"],this.toolbars["zoomOut"],this.toolbars["zoomBox"],this.toolbars["navigate"],this.toolbars['select']]);
			
			rightTools.addControls([this.toolbars["mark"]]);
//初始化
		if(!this.map)
		{
				this.map = new OpenLayers.Map('umap');
				
				/*var gphy = new OpenLayers.Layer.Google(
					"Google Physical",
					{type: google.maps.MapTypeId.TERRAIN}
				);
				var gmap = new OpenLayers.Layer.Google(
					"Google Streets", // the default
					{numZoomLevels: 20}
				);
				var ghyb = new OpenLayers.Layer.Google(
					"Google Hybrid",
					{type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
				);
				var gsat = new OpenLayers.Layer.Google(
					"Google Satellite",
					{type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22}
				);
				this.map.addLayers([gphy, gmap, ghyb, gsat]);
*/				
				var layer1 = new OpenLayers.Layer.TMS("Map", website, {
					'type' : 'png',
					'getURL' : get_my_url
				});
				this.map.addLayer(layer1);
				

				// Google.v3 uses EPSG:900913 as projection, so we have to
				// transform our coordinates
				this.map.setCenter(new OpenLayers.LonLat(57.605318,-20.231275).transform(
					new OpenLayers.Projection("EPSG:4326"),
					this.map.getProjectionObject()
				), 12);
		}
	    
		/*
		 将工具条显示到地图中
		*/
	    this.map.addControls([
					leftTools,
					//rightTools,
					//new OpenLayers.Control.OverviewMap({maximized: false  }),//鹰眼
					this.selectControl,
					this.navigationHistory,
					//new OpenLayers.Control.LayerSwitcher(),
					new OpenLayers.Control.MousePosition(),
					this.informationDrawControls
		]);
	 
	    this.map.addLayers([this.informationLayer]);
		this.fireEvent('render')
	},
	handleMeasurements  : function(event,myself){
		myself.measureWindow.show();
        var geometry = event.geometry;
        var units = event.units;
        var order = event.order;
        var measure = event.measure;
        var element = document.getElementById('output');
        var out = "";
        if(order == 1) {
            out += "长度: " + measure.toFixed(3) + " " + units;
        } else {
            out += "面积: " + measure.toFixed(3) + " " + units + "<sup>2</" + "sup>";
        }
        element.innerHTML = out;
	},
	buttonAction : function(a,myself){
		  for(var key in myself.toolbars){
	        	var control = myself.toolbars[key];
	        	if(a.object.name != key && control.active)
	        	{
	        		control.deactivate();
	        	}
	        	
	       }
		  
		
		if(a.object.name != "select"){
			if( myself.selectControl.active)
			{
				myself.selectControl.deactivate();
				myself.unSelectAll();
			}			
		}
		
		
		if (a.object.name != "information")
		{
			if( myself.informationDrawControls.active)
			{
				myself.informationDrawControls.deactivate();
			}		
		}
		
	},
	selectFeatures : [],
	selectFeatureFunction : function(a,myself){
           if(myself.selectFeatures.indexOf(a)==-1)
           {
        	   myself.selectFeatures.push(a);
           }
	},
	unselectFeatureFunction : function(a,myself){
		    if(myself.selectFeatures.indexOf(a)>-1)
		    {
			   myself.selectFeatures.remove(a);
		    }	
	},
	selectEventFeature:function(eventId){
		if(eventId == null){
			return;
		}
//		debugger;
		if(this.informationLayer.selectedFeatures.length > 0){
			for(var i=0;i<this.informationLayer.selectedFeatures.length;i++){
				if(this.informationLayer.selectedFeatures[i].getFeatureData().eventId == eventId){
					return;
				}
			}
		}
		this.unSelectAll();
//		return;
		var myUObject = this;
		
		var features = this.informationLayer.features;
		for(var i=0;i<features.length;i++){
			var feature = features[i];
			if(eventId == feature.getFeatureData().eventId){
				var html = feature.featureData.html
				var popup = new OpenLayers.Popup.FramedCloud("popup",
					OpenLayers.LonLat.fromString(feature.geometry.toShortString()),
					null,
					"<div>"+html+"</div>",
					null,
					true,
					function(evt){
						myUObject.onPopupClose3(feature);
					}
				);
				feature.popup = popup;
				myUObject.map.addPopup(feature.popup);
				this.informationLayer.selectedFeatures.push(feature);
				return;
			}
		}
	},
	selectAll:function(){
		var myUObject = this;
		var features = this.informationLayer.features;
		for(var i=0;i<features.length;i++){
			var feature = features[i];
			var html = feature.featureData.html
			var popup = new OpenLayers.Popup.FramedCloud("popup",
				OpenLayers.LonLat.fromString(feature.geometry.toShortString()),
				null,
				"<div>"+html+"</div>",
				null,
				true,
				function(evt){
					myUObject.onPopupClose3(feature);
				}
			);
			feature.popup = popup;
			this.map.addPopup(feature.popup);
			this.informationLayer.selectedFeatures.push(feature);
		}
		
	},
	unSelectAll : function(){
//		debugger;
		for(var i=0;i<this.informationLayer.selectedFeatures.length;i++)
		{
			var b = this.informationLayer.selectedFeatures[i];
			b.renderIntent = "default";
			//if(b.layer){
				b.layer.redraw();
			//}
			this.onPopupClose3(b);
		}
		this.informationLayer.selectedFeatures.length = 0;
		this.informationLayer.selectedFeatures = [];
		
	},
	unSelectFeature : function(features){
	        if(features.renderIntent != "default")
			{
			this.unselectFeatureFunction(features,this);
			features.renderIntent = "default";
			features.layer.redraw();
			}
	},
	
	//可以选择事件
	selectFunction : function(a,myself){
	   	 if(myself.toolbars['select'].active)
		 {
	   		myself.toolbars['select'].deactivate();
	   		myself.selectControl.deactivate();
	   		myself.unSelectAll();
		 }
		 else
		 {
			 myself.onPopupClose();
			 myself.toolbars['select'].activate();
			 myself.selectControl.activate();
		 }
	   
	},
	
	informationAdd : function(a,myself,html){
	    var featureData = {html:html};
		this.onPopupClose();
		var feature = a.feature;
		feature.setFeatureData(featureData);
		feature.attributes = {
	        		iType: this.markWin.getSelectedImage()
	    };
		this.inormationFeatures = feature;
		this.informationLayer.addFeatures([feature]);
		this.showInformationPopup(feature,html);
		
	},
	onPopupClose : function(feature){
	    if(this.inormationFeatures && this.inormationFeatures.popup)
		{
	        this.map.removePopup(this.inormationFeatures.popup);
	        this.inormationFeatures.popup.destroy();
	        this.inormationFeatures.popup = null;	
		}
	},
	onPopupClose3 : function(feature){
//		debugger;
			if(feature.popup)
			{
				this.map.removePopup(feature.popup);
				feature.popup.destroy();
				feature.popup = null;	
			}
			this.unSelectFeature(feature)
	},
	onPopupClose4 : function(feature){
		
			if(feature.popup)
			{
				this.map.removePopup(feature.popup);
				feature.popup.destroy();
				feature.popup = null;	
			}
	},
	onPopupClose2 : function(feature){
	    if(this.inormationFeatures && this.inormationFeatures.popup)
		{
	    	this.informationLayer.removeAllFeatures();
	        this.map.removePopup(this.inormationFeatures.popup);
	        this.inormationFeatures.popup.destroy();
	        this.inormationFeatures.popup = null;	
		}
	},	
	informationFunction : function(a,myself){
	
		var b = myself.toolbars['information'];
	   	 if(b.active)
		 {
	   		b.deactivate();
			if( myself.informationDrawControls.active)
			{
				myself.informationDrawControls.deactivate();
			}	
		 }
		 else
		 {
			b.activate();
			myself.informationDrawControls.activate();
		 }
		
	},
	showInformationPopup : function(feature,html,ifpop){
			var myUObject = this;
			var popup = new OpenLayers.Popup.FramedCloud("chicken", 
                feature.geometry.getBounds().getCenterLonLat(),
                null,
                "<div>"+html+"</div>",
                null, true, function(evt){myUObject.onPopupClose3(feature)});
				feature.popup = popup;
				if(ifpop){
					this.map.addPopup(popup);
				}
	
	},
	/**
	* @json : {
				lat :  纬度
				lon :  经度
				html : 显示的信息
	          }
	*/
	showInformation : function(json){
		var point = new OpenLayers.Geometry.Point(json.lon,json.lat).transform(
					new OpenLayers.Projection("EPSG:4326"),
					this.map.getProjectionObject()
				);
		var feature = new OpenLayers.Feature.Vector(point);
		feature.setFeatureData(json);
		feature.attributes = {
	        		iType:  this.markWin.getSelectedImage(json.icon)
	    };
		
		this.informationLayer.addFeatures([feature]);
		this.showInformationPopup(feature,json.html,json.ifpop);
	},
	showInformations : function(arr){
		this.informationLayer.removeAllFeatures();
		if(arr == null || arr.length == 0){
			return;
		}
	    for(var i=0;i<arr.length;i++)
		{
		   this.showInformation(arr[i]);
		}
	},
	showMarkWind : function(a,myself){
	var b = myself.toolbars['mark'];
	var win = this.markWin;
	var but = win.getButton();
	if(!but){win.setButton(b)}
		 if(b.active)
		 {
	   		b.deactivate();
			win.hide();
		 }
		 else
		 {
			b.activate();
			win.show();
		 }
	    
	}
});

