var client = com.xjwgraph.ClientTool = function(a) {
	this.dialog = $("#" + a.prop);
	$id(a.prop).style.width = a.dialogWidth || 328 + "px";
};
client.prototype = {
	_deepCopyProp : function(a) {
		var c = {};
		for ( var b in a) {
			c[b] = a[b]
		}
		return c
	},
	_isDiffJson : function(d, c) {
		var b = false;
		for ( var a in d) {
			if (d[a] !== c[a]) {
				b = true;
				break
			}
		}
		return b
	},
	_close : function(b) {
		var a = $(".panel-tool-close");
		if (a.length > 0) {
			a = a[0];
			a.click(b)
		}
	},
	showDialog : function(c, e, d) {
		var b = this._deepCopyProp(d.prop), a = this;
		this.dialog.dialog({
			title : e,
			modal : true,
			_attri_prop : null,
			buttons : [ {
				text : "ok",
				handler : function() {
					var i = d.prop;
					for ( var f in i) {
						var h = $id("lineAttr_" + f);
						i[f] = h.value
					}
					a._close(c);
					var g = new com.xjwgraph.UndoRedoEvent(function() {
						d.prop = b
					}, PathGlobal.editProp);
					g.setRedo(function() {
						d.prop = i
					})
				}
			}, {
				text : "rest",
				handler : function() {
					var h = d.prop = b;
					for ( var f in h) {
						var g = $id("lineAttr_" + f);
						g.value = h[f]
					}
				}
			} ]
		})
	},
	addProItem : function(e) {
		var c = document, d = c.createElement("div");
		var b = "<table>";
		for ( var a in e) {
			b += '<tr><td align="right">&nbsp;' + a
					+ '&nbsp;:&nbsp;</td><td><input type="text" id="lineAttr_'
					+ a + '" value="' + e[a] + '" /></td></tr>'
		}
		b += "</table>";
		d.innerHTML = b;
		return d
	}
};
var Utils = com.xjwgraph.Utils = {
	create : function(e) {
		this.global = com.xjwgraph.Global;
		this.global.modeMap = new com.xjwgraph.Map();
		this.global.lineMap = new com.xjwgraph.Map();
		this.global.beanXml = new com.xjwgraph.BeanXML();
		var g = e.contextBody, a = $id(g), h = e.width, c = e.height;
		this.global.baseTool = new com.xjwgraph.BaseTool(a, h, c);
		this.global.modeTool = new com.xjwgraph.ModeTool(a);
		this.global.lineTool = new com.xjwgraph.LineTool(a);
		this.global.clientTool = new com.xjwgraph.ClientTool(e);
		var b = e.smallMap;
		this.global.smallTool = new com.xjwgraph.SmallMapTool(b, g);
		var f = $id(e.mainControl);		
		this.global.controlTool = new com.xjwgraph.ControlTool(f);
		var d = e.historyMessage;
		this.global.undoRedoEventFactory = new com.xjwgraph.UndoRedoEventFactory(
				d);
		this.global.undoRedoEventFactory.init();
		return this;
	},
	showLinePro : function() {
		this.global.lineTool.showProperty()
	},
	showModePro : function() {
	//	this.global.modeTool.showServiceTaskParams();
		this.global.modeTool.showProperty();
	},
	
	toMerge : function() {
		this.global.baseTool.toMerge()
	},
	toSplit : function() {
		this.global.baseTool.toSeparate();
	},
	toTop : function() {
		this.global.modeTool.toTop();
	},
	toBottom : function() {
		this.global.modeTool.toBottom();
	},
	printView : function() {
		this.global.baseTool.printView();
	},
	undo : function() {
		this.global.undoRedoEventFactory.undo();
	},
	redo : function() {
		this.global.undoRedoEventFactory.redo();
	},
	saveXml : function(verId) {
		this.global.beanXml.toXML(verId);
	},
	checkXml : function(data) {
		this.global.beanXml.checkXml(data);
	},
	backSet : function() {
		this.global.beanXml.backSet();
	},
	loadXml : function() {
		this.global.beanXml.toHTML();
	},
	loadTextXml : function(xmlText) {
//	loadTextXml : function() {	
//		var xmlText='<?xml version="1.0"?><definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:uboss="http://www.ztesoft.com/uboss/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.ztesoft.com/uboss/bpmn"><process id="C56FF53C0970000167E9136A1151AB70" name="C56FF53C0970000167E9136A1151AB70"><endEvent id="pro16" name=""/><userTask activiti:taskTemplateId="3344" id="pro14" name=""><extensionElements/></userTask><startEvent id="pro12" name=""/><sequenceFlow id="line14" name="" sourceRef="pro14" targetRef="pro16"/><sequenceFlow id="line12" name="" sourceRef="pro12" targetRef="pro14"/></process><bpmndi:BPMNDiagram id="BPMNDiagram_C56FF53C0970000167E9136A1151AB70"><bpmndi:BPMNPlane id="BPMNPlane_C56FF53C0970000167E9136A1151AB70" bpmnElement="C56FF53C0970000167E9136A1151AB70"><bpmndi:BPMNShape id="BPMNShape_16" bpmnElement="pro16"><omgdc:Bounds y="83" x="522" width="50" height="50"/></bpmndi:BPMNShape><bpmndi:BPMNShape id="BPMNShape_14" bpmnElement="pro14"><omgdc:Bounds y="86" x="324" width="50" height="50"/></bpmndi:BPMNShape><bpmndi:BPMNShape id="BPMNShape_12" bpmnElement="pro12"><omgdc:Bounds y="84" x="157" width="50" height="50"/></bpmndi:BPMNShape><bpmndi:BPMNEdge bpmnElement="line14" id="BPMNEdge_line14" xIndex="5" wIndex="4"><omgdi:waypoint x="376" y="112"/><omgdi:waypoint x="522" y="109"/></bpmndi:BPMNEdge><bpmndi:BPMNEdge bpmnElement="line12" id="BPMNEdge_line12" xIndex="5" wIndex="4"><omgdi:waypoint x="209" y="110"/><omgdi:waypoint x="324" y="112"/></bpmndi:BPMNEdge></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>';
//		var xmlText='<?xml version="1.0"?><definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:uboss="http://www.ztesoft.com/uboss/bpmn" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.ztesoft.com/uboss/bpmn"><process id="C57F0604AA8000017785BD3022A01BD9" name="C57F0604AA8000017785BD3022A01BD9"><endEvent id="pro16" name=""/><userTask activiti:taskTemplateId="" taskTemplateName="" id="pro14" name=""><extensionElements/></userTask><startEvent id="pro12" name=""/><sequenceFlow id="line14" name="" targetRef="pro16" sourceRef="pro14"/><sequenceFlow id="line12" name="" targetRef="pro14" sourceRef="pro12"/></process><bpmndi:BPMNDiagram id="BPMNDiagram_C57F0604AA8000017785BD3022A01BD9"><bpmndi:BPMNPlane id="BPMNPlane_C57F0604AA8000017785BD3022A01BD9" bpmnElement="C57F0604AA8000017785BD3022A01BD9"><bpmndi:BPMNShape id="BPMNShape_16" bpmnElement="pro16"><omgdc:Bounds y="52" x="355" width="50" height="50"/></bpmndi:BPMNShape><bpmndi:BPMNShape id="BPMNShape_14" bpmnElement="pro14"><omgdc:Bounds y="53" x="173" width="100" height="50"/></bpmndi:BPMNShape><bpmndi:BPMNShape id="BPMNShape_12" bpmnElement="pro12"><omgdc:Bounds y="55" x="48" width="50" height="50"/></bpmndi:BPMNShape><bpmndi:BPMNEdge bpmnElement="line14" id="BPMNEdge_line14" wIndex="4" xIndex="5"><omgdi:waypoint x="275" y="79"/><omgdi:waypoint x="355" y="78"/></bpmndi:BPMNEdge><bpmndi:BPMNEdge bpmnElement="line12" id="BPMNEdge_line12" wIndex="4" xIndex="5"><omgdi:waypoint x="100" y="81"/><omgdi:waypoint x="173" y="79"/></bpmndi:BPMNEdge></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>';
//		var xmlText='<?xml version="1.0"?><definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:uboss="http://www.ztesoft.com/uboss/bpmn" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.ztesoft.com/uboss/bpmn"><process id="C58397A3BDA00001937918381ED01909" name="C58397A3BDA00001937918381ED01909"><scriptTask activiti:scriptFormat="juel" activiti:resultVariable="" id="pro12" name="test"><script><![CDATA[aaa]]</script></scriptTask></process><bpmndi:BPMNDiagram id="BPMNDiagram_C58397A3BDA00001937918381ED01909"><bpmndi:BPMNPlane id="BPMNPlane_C58397A3BDA00001937918381ED01909" bpmnElement="C58397A3BDA00001937918381ED01909"><bpmndi:BPMNShape id="BPMNShape_12" bpmnElement="pro12"><omgdc:Bounds y="119" x="83" width="100" height="50"/></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>';
		var xml=xmlText.replaceAll("activiti:", "");
		this.global.beanXml.doc = null;
		this.global.beanXml.doc = this.global.beanXml.loadXmlText(xml);
		this.global.beanXml.toHTML();
	},
	clearHtml : function() {
		this.global.beanXml.clearHTML();
	},
	copyNode : function() {
		keyDownFactory.copyNode()
	},
	removeNode : function() {
		keyDownFactory.removeNode()
	},
	alignLeft : function() {
		this.global.baseTool.toLeft();
		this.global.baseTool.clearContext()
	},
	alignRight : function() {
		this.global.baseTool.toRight();
		this.global.baseTool.clearContext()
	},
	verticalCenter : function() {
		this.global.baseTool.toMiddleWidth();
		this.global.baseTool.clearContext()
	},
	alignTop : function() {
		this.global.baseTool.toTop();
		this.global.baseTool.clearContext()
	},
	horizontalCenter : function() {
		this.global.baseTool.toMiddleHeight();
		this.global.baseTool.clearContext()
	},
	bottomAlignment : function() {
		this.global.baseTool.toBottom();
		this.global.baseTool.clearContext()
	},
	nodeDrag : function(b, a, c) {
		this.global.baseTool.drag(b, a, c)
	},
	closeAutoLineType : function() {
		PathGlobal.isAutoLineType = false
	},
	openAutoLineType : function() {
		PathGlobal.isAutoLineType = true
	},
	loadSubTextXml : function(xmlText) {
		var xml=xmlText.replaceAll("activiti:", "");
		this.global.beanXml.doc = null;
		this.global.beanXml.doc = this.global.beanXml.loadXmlText(xml);
		this.global.beanXml.toHTML();
		}
};