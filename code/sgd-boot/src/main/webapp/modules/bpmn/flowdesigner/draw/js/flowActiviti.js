var ContextXML = com.xjwgraph.ContextXML = function() {
};
ContextXML.prototype = {
	setAttribute : function(a, b) {
		this[a] = b
	},
	view : function() {
		var r = this, g = document.createElement("div");
		for (var q in r) {
			if (q == "view" || q == "setAttribute" || q == "style"
					|| q == "modeIds" || q == "lineIds") {
				continue
			}
			g.setAttribute(q, r[q]);
		}
		var d = com.xjwgraph.Global, l = d.modeTool, n = d.baseTool, h = n.contextMap, b = new Map(), m = new Map(), e = r.modeIds
				.split(","), f = e.length;
		for (var j = f; j--;) {
			var p = e[j];
			b.put(p, $id(p))
		}
		var a = r.lineIds.split(","), o = a.length;
		for (var j = o; j--;) {
			var k = a[j];
			m.put(k, $id(k))
		}
		function c(s, i) {
			this.contextModeMap = s;
			this.contextLineMap = i
		}
		var c = new c(b, m);
		h.put(r.id, c);
		g.style.cssText = r.style;
		var n = d.baseTool;
		n.pathBody.appendChild(g);
		n.contextDivDrag(g, c)
	}
};
var LineXML = com.xjwgraph.LineXML = function() {
};
LineXML.prototype = {
		_lineAttrProp : "attr_prop_",
		setAttribute : function(a, b) {
			if (a.indexOf(this._lineAttrProp) > -1) {
				if (this["prop"] == null) {
					this["prop"] = {}
				}
				a = a.substring(this._lineAttrProp.length);
				this["prop"][a] = b;
			} else {
				this[a] = b;
			}
		},
		view : function() {
			var g = this, a = com.xjwgraph.Global, f = a.lineTool, h = f
					.createBaseLine(g.id, g.d || g.path, g.brokenType), d = new BuildLine();
			d.id = h.id;
			if (this["prop"]) {
				d.prop = this["prop"];
			}
			h.style.cssText = g.style;
			h.setAttribute("strokeweight", g.strokeweight);
			h.setAttribute("strokecolor", g.strokecolor);
			h.setAttribute("brokenType", g.brokenType);
			var e = a.modeTool, c = a.beanXml;	
			
//			$("#"+g.id+"Title").text(g.name);
//			$("#"+g.id+"Condition").text(g.condition);
//			if(BeanXML.prototype.brower()=="IE"){
//				var titleText=$("#"+g.id+"Title");
//				var left=(Math.abs(parseInt(g.x1)-parseInt(g.x2)))/2;				
//				$(titleText).css("left",left);
//			}else{
//				var titleText=$("#"+g.id+"Title");
//				$(titleText).attr("x",(parseInt(g.x1)-(parseInt(g.x1)-parseInt(g.x2))/2));
//				$(titleText).attr("y",parseInt(g.y)+20);
//			}
			
			if (g.xBaseMode) {
				var i = function() {
					var j = a.modeMap.get(g.xBaseMode);
					d.xBaseMode = j;
					d.xIndex = g.xIndex;
					var k = new BuildLine();
					k.id = g.id;
					k.type = true;
					k.index = g.xIndex;
					j.lineMap.put(g.id + "-true", k);
				};
				
				if (a.modeMap.get(g.xBaseMode)) {
					i();
				} else {
					c.delay[c.delayIndex++] = i;
				}
			}
			if (g.wBaseMode) {
				var b = function() {
					var j = a.modeMap.get(g.wBaseMode);
					d.wBaseMode = j;
					d.wIndex = g.wIndex;
					var k = new BuildLine();
					k.id = g.id;
					k.type = false;
					k.index = g.wIndex;
					j.lineMap.put(g.id + "-false", k);
				};
				if (a.modeMap.get(g.wBaseMode)) {
					b();
				} else {
					c.delay[c.delayIndex++] = b;
				}
			}
			a.smallTool.drawLine(h);
			a.lineMap.put(d.id, d);
			f.baseLineIdIndex = parseInt(g.id.substring(4)) + 1;
		}
};
var ModeXML = com.xjwgraph.ModeXML = function() {
	var b = this, a = com.xjwgraph.Global.modeTool;
	b.modeDiv = a.createBaseMode(0, 0, "", 0, "50px", "50px");
	b.backImg = a.getSonNode(b.modeDiv, "backImg");	
	b.title = a.getSonNode(b.modeDiv, "title");	
};
ModeXML.prototype = {
	setAttribute : function(b, c) {		
		var a = this;		
		if (b == "backImgSrc") {	
			a.backImg.src ="../draw/images/"+c+".png";			
		} else if (b == "y") {
			a.modeDiv.style.top = c + "px";
		} else if (b == "x") {
			a.modeDiv.style.left = c + "px";
		} else if (b == "width") {
			a.modeDiv.style.widht = c + "px";
			a.backImg.style.width = c + "px";
		} else if (b == "height") {
			a.modeDiv.style.height = c + "px";
			a.backImg.style.height = c + "px";
		} else if (b == "id") {
			com.xjwgraph.Global.modeTool.setIndex(a.modeDiv, c);
//		} else if (b == "title") {
		} else if (b == "name") {
			$(a.title).attr("title",c); // 设置title属性当title中字段超长，鼠标移动到任务图标上显示明细内容
			a.title.innerHTML = c;
		} else if (b == "zIndex") {
			a.modeDiv.style.zIndex = c;
		}
								
					
	},
	view : function() {
		var e = new BaseMode(), d = this.modeDiv, c = com.xjwgraph.Global, b = c.modeTool;		
		b.pathBody.appendChild(d);
		var a = b.getModeIndex(d);
		e.id = d.id;
		if (this["prop"]) {
			e.prop = this["prop"];
		}
		c.modeMap.put(e.id, e);
		b.initEvent(a);
		b.showPointerId(a);
		b.hiddPointer(d);
		c.smallTool.drawMode(d);
		b.baseModeIdIndex = parseInt(a) + 1;
		
	}
};
var BeanXML = com.xjwgraph.BeanXML = function() {
	var a = this;
	a.delay = [];
	a.delayIndex = 0;
	a.doc = null;
	a.create();
	a.root = a.initBeanXML();
};
BeanXML.prototype = {
	
	create : function() {
		var a = this;
		a.doc = null;
		if (document.all) {
			var e = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0",
					"Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0",
					"MSXML2.DOMDocument", "MSXML.DOMDocument",
					"Microsoft.XMLDOM"];
			var c = e.length;
			for (var b = c; b--;) {
				try {
					a.doc = new ActiveXObject(e[b]);
					break
				} catch (d) {
					continue
				}
			}
		} else {
			a.doc = document.implementation.createDocument("", "", null)
		}
	},
	initBeanXML : function() {
		var b = this, c = b.doc.createProcessingInstruction("xml",
		"version=\"1.0\"");
		b.doc.appendChild(c);
		//创建definitions节点
		var definitions=b.doc.createElement("definitions");
		definitions.setAttribute("xmlns","http://www.omg.org/spec/BPMN/20100524/MODEL");
		definitions.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		definitions.setAttribute("xmlns:uboss","http://www.ztesoft.com/uboss/bpmn");
		definitions.setAttribute("xmlns:activiti","http://activiti.org/bpmn");
		definitions.setAttribute("xmlns:bpmndi","http://www.omg.org/spec/BPMN/20100524/DI");
		definitions.setAttribute("xmlns:omgdc","http://www.omg.org/spec/DD/20100524/DC");
		definitions.setAttribute("xmlns:omgdi","http://www.omg.org/spec/DD/20100524/DI");
		definitions.setAttribute("typeLanguage","http://www.w3.org/2001/XMLSchema");
		definitions.setAttribute("expressionLanguage","http://www.w3.org/1999/XPath");
		definitions.setAttribute("targetNamespace","http://www.ztesoft.com/uboss/bpmn");
		b.doc.appendChild(definitions);
		
		//创建process节点
		var a = b.doc.createElement("process");
		//获取页面processId值
		var processId=$("#processId").val();
		//如果processId为空则表示是新建流程，否则是加载的流程，同时读取原流程id
		if(processId==""){
			//创建流程ID
			var uuid = new UUID().createUUID(); 
			processId=uuid;
		}
		a.setAttribute("id",processId);
		a.setAttribute("name",processId);
		
		
		//将process节点添加至definitions节点
		definitions.appendChild(a);
		var bpmndi=b.doc.createElement("bpmndi:BPMNDiagram");
		bpmndi.setAttribute("id","BPMNDiagram_"+processId);
		definitions.appendChild(bpmndi);
		
		var BPMNDiagram =b.doc.getElementsByTagName("bpmndi:BPMNDiagram")[0];
		var BPMNPlane=b.doc.createElement("bpmndi:BPMNPlane");
		BPMNPlane.setAttribute("id","BPMNPlane_"+processId);
		BPMNPlane.setAttribute("bpmnElement",processId);
		BPMNDiagram.appendChild(BPMNPlane);
		
		return a;
	},
	getNodeAttribute : function(f) {
		var d = [], b = 0, a = f.attributes, g = a.length;
		for (var c = g; c--;) {
			var e = a[c];
			d[b++] = " ";
			d[b++] = e.nodeName;
			d[b++] = '="';
			d[b++] = e.nodeValue;
			d[b++] = '"'
		}
		return d.join("");
	},
	getText : function(d, f) {
		var e = [], b = 0, j = this;
		if (f) {
			e[b++] = "<";
			e[b++] = d.nodeName;
		}
		var h = d.childNodes, g = h.length;
		for (var c = g; c--;) {
			var a = h[c];
			e[b++] = "<";
			e[b++] = a.nodeName;
			if (a.nodeType == 1) {
				e[b++] = j.getNodeAttribute(a)
			}
			e[b++] = ">";
			if (a.hasChildNodes()) {
				e[b++] = j.getText(a, false)
			}
			e[b++] = "</";
			e[b++] = a.nodeName;
			e[b++] = ">"
		}
		if (f) {
			e[b++] = "</";
			e[b++] = d.nodeName;
			e[b++] = ">"
		}
		return e.join("")
	},
	createNode : function(b, d) {
		var a = this, c = a.doc.createElement(b);
	//根据传递参数d是否存在判断节点创建位置，如果d不存在则创建到根节点，存在则创建在d节点下。参数b表示节点创建名称
		if (d) {
			d.appendChild(c);
		} else {
			a.root.appendChild(c);
		}
		return c;
	},
	clearNode : function() {
		var a = this;		
		if (a.root) {
			var d = a.root.childNodes, c = d.length;
			for (var b = c; b--;) {
				a.root.removeChild(d[b]);
			}
		}
	},
	_clearNode : function() {
		var a = this;
		if (a) {
			var d = a.childNodes, c = d.length;
			for (var b = c; b--;) {
				a.removeChild(d[b]);
			}
		}
	},
	_formatValue : function(a) {
		a = a.replaceAll("&", "&amp;");
		a = a.replaceAll("'", "&apos;");
		a = a.replaceAll('"', "&quot;");
		a = a.replaceAll(">", "&gt;");
		a = a.replaceAll("<", "&lt;");
		return a
	},	


	getTextXml : function(c) {
		var b = "";
		if (c) {
			b = c.xml;
			if (!b) {
				if (c.innerHTML) {
					b = c.innerHTML;
				} 
				else {
					var a = new XMLSerializer();
					b = a.serializeToString(c);
				}
			} else {
				b = b.replace(/\r\n\t[\t]*/g, "").replace(/>\r\n/g, ">")
						.replace(/\r\n/g, "\n")
			}
		}
		b = b.replace(/\n/g, "");
		b = this._formatValue(b);
		return b;
	},
	clearHTML : function() {
		var a = com.xjwgraph.Global;		
		a.undoRedoEventFactory.clear();
		a.lineTool.removeAll();
		a.modeTool.removeAll();
		a.baseTool.removeAll();
	},
	toHTML : function(obj) {
		var self = this;
		self.clearHTML();
		var x=70;
		var y=70;
		xml = new ModeXML();	
		xml.setAttribute("backImgSrc", "startEvent");
		xml.setAttribute("zIndex", "0");	
		xml.setAttribute("id", "0");
		xml.setAttribute("contentEditable","inherit");	
		xml.setAttribute("height","50");	
		xml.setAttribute("width","50");	
		xml.setAttribute("name","");	
		xml.setAttribute("x",x);	
		xml.setAttribute("y",y);	
		xml.view();
		
		for (var i=0;i<obj.length;i++){
		linexml = new LineXML();
		modexml = new ModeXML();	
		linexml.setAttribute("brokenType","1");
		var pointStart,pointEnd;
		if(i==0){
			pointStart=(x+=52);
			pointEnd=(x+=50);
			linexml.setAttribute("d", "m "+pointStart+" 95 L "+pointEnd+" 95 z");
		}else{
			pointStart=(x+=102);
			pointEnd=(x+=50);
			linexml.setAttribute("d", "m "+pointStart+" 95 L "+pointEnd+" 95 z");
		}
		linexml.setAttribute("id", "line"+i);
		linexml.setAttribute("contentEditable","inherit");	
		linexml.setAttribute("marker-end","url(#arrow)");	
		linexml.setAttribute("strokecolor","black");	
		linexml.setAttribute("strokeweight","1.35");	
		linexml.setAttribute("style","WIDTH: 100px; CURSOR: pointer; POSITION: absolute; HEIGHT: 100px;fill: none; stroke: black; stroke-width: 1.7999999999999998");	
		linexml.setAttribute("wBaseMode","module"+(i+1));	
		linexml.setAttribute("wIndex","4");			
		linexml.setAttribute("xBaseMode","module"+i);		
		linexml.setAttribute("xIndex","5");		
		linexml.view();		
		modexml.setAttribute("zIndex", i+1);	
		modexml.setAttribute("id", (i+1));
		modexml.setAttribute("contentEditable","inherit");	
		modexml.setAttribute("height","50");
		if(obj[i].TASK_LIST_NAME=="end"){
			modexml.setAttribute("width","50");	
			modexml.setAttribute("backImgSrc", "endEvent");
			modexml.setAttribute("name","");	
		}else{
			modexml.setAttribute("width","100");
			modexml.setAttribute("backImgSrc", obj[i].TASK_STATE);
			modexml.setAttribute("name",obj[i].TASK_LIST_NAME);	
		}
		modexml.setAttribute("x",pointEnd+5);	
		modexml.setAttribute("y",y);
		modexml.view();
		if(obj[i].TASK_LIST_NAME!="end"){
		var moduleDiv=$("#module"+(i+1));
		var BeginTime=qryWebRes("BEGIN_TIME");
		var endTime=qryWebRes("END_TIME");
		var Operator=qryWebRes("OPERATOR");
		var showTitle=""+BeginTime+"："+obj[i].CREATE_DATE+"\n"+endTime+" : "+obj[i].END_TIME+"\n"+Operator+" : "+obj[i].USER_NAME;
		$(moduleDiv).attr("title",showTitle);
		}


		}

//			var delays = self.delay,
//			delayLength = delays.length;
//			for (var i = delayLength; i--;) {
//				delays[i]();
//			}
//			
//			delete self.delay;
//			
//			self.delay = [];
//			self.delayIndex = 0;
		
	},
	brower :function(){
	var brower=navigator.appName.indexOf("Microsoft") != -1 ? 'IE' : navigator.appName;	
		return brower;
	}
};