var backTemplateList = [];
var ContextXML = com.xjwgraph.ContextXML = function() {};
ContextXML.prototype = {
    setAttribute: function(a, b) {
        this[a] = b
    },
    view: function() {
        var r = this,
            g = document.createElement("div");
        for (var q in r) {
            if (q == "view" || q == "setAttribute" || q == "style" || q == "modeIds" || q == "lineIds") {
                continue
            }
            g.setAttribute(q, r[q])
        }
        var d = com.xjwgraph.Global,
            l = d.modeTool,
            n = d.baseTool,
            h = n.contextMap,
            b = new Map(),
            m = new Map(),
            e = r.modeIds
            .split(","),
            f = e.length;
        for (var j = f; j--;) {
            var p = e[j];
            b.put(p, $id(p))
        }
        var a = r.lineIds.split(","),
            o = a.length;
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
var LineXML = com.xjwgraph.LineXML = function() {};
LineXML.prototype = {
    _lineAttrProp: "attr_prop_",
    setAttribute: function(a, b) {
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
    view: function() {
        var g = this,
            a = com.xjwgraph.Global,
            f = a.lineTool,
            h = f
            .createBaseLine(g.id, g.d || g.path, g.brokenType),
            d = new BuildLine();
        d.id = h.id;
        if (this["prop"]) {
            d.prop = this["prop"];
        }
        h.style.cssText = g.style;
        h.setAttribute("strokeweight", g.strokeweight);
        h.setAttribute("strokecolor", g.strokecolor);
        h.setAttribute("brokenType", g.brokenType);
        var e = a.modeTool,
            c = a.beanXml;

        $("#" + g.id + "Title").text(g.name);
        $("#" + g.id + "Condition").text(g.condition);
        $("#" + g.id + "Condition").attr("data_type", g.data_type);
        if (BeanXML.prototype.brower() == "IE") {
            var titleText = $("#" + g.id + "Title");
            var left = (Math.abs(parseInt(g.x1) - parseInt(g.x2))) / 4;
            $(titleText).css("left", left);
            $(titleText).css("width", left * 2);
        } else {
            var titleText = $("#" + g.id + "Title");
            $(titleText).attr("x", (parseInt(g.x1) - (parseInt(g.x1) - parseInt(g.x2)) / 2));
            $(titleText).attr("y", parseInt(g.y) + 20);
            $("#" + g.id + "lineHead").attr("x", g.x1);
            $("#" + g.id + "lineMiddle").attr("x", (parseInt(g.x1) - (parseInt(g.x1) - parseInt(g.x2)) / 2));
            $("#" + g.id + "ShowTitle").attr("y", parseInt(g.y) + 20);
            $("#" + g.id + "ShowTitle").attr("x", (parseInt(g.x1) - (parseInt(g.x1) - parseInt(g.x2)) / 4));
            LineTool.prototype.create_tspan(g.id);
        }

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
    var b = this,
        a = com.xjwgraph.Global.modeTool;
    b.modeDiv = a.createBaseMode(0, 0, "", 0, "50px", "50px");
    b.backImg = a.getSonNode(b.modeDiv, "backImg");
    b.title = a.getSonNode(b.modeDiv, "title");
    b.taskTemplateId = a.getSonNode(b.modeDiv, "taskTemplateId");
    b.taskTemplateName = a.getSonNode(b.modeDiv, "taskTemplateName");
    b.resultVariable = a.getSonNode(b.modeDiv, "resultVariable");
    b.countersign = a.getSonNode(b.modeDiv, "countersign");
    b.sequential = a.getSonNode(b.modeDiv, "sequential");
    b.scriptVal = a.getSonNode(b.modeDiv, "scriptVal");
    b.endpointUrl = a.getSonNode(b.modeDiv, "endpointUrl");
    b.payloadExpression = a.getSonNode(b.modeDiv, "payloadExpression");
    b.queueName = a.getSonNode(b.modeDiv, "queueName");
    b.blockFlag = a.getSonNode(b.modeDiv, "blockFlag");
    b.taskType = a.getSonNode(b.modeDiv, "taskType");
    b.taskImg = a.getSonNode(b.modeDiv, "taskImg");
    b.sendMail = a.getSonNode(b.modeDiv, "sendMail");
};
ModeXML.prototype = {
    //	_modeAttrProp : "attr_prop_",
    setAttribute: function(b, c) {
        var a = this;
        //		if (b == "backImgSrc") {
        //			a.backImg.src = c;
        //		} else if (b == "top") {
        //			a.modeDiv.style.top = c + "px";
        //		} else if (b == "left") {
        //			a.modeDiv.style.left = c + "px";
        if (b == "backImgSrc") {
            a.backImg.src = "../draw/images/" + c + ".png";
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
            $(a.title).attr("title", c); // 设置title属性当title中字段超长，鼠标移动到任务图标上显示明细内容
            a.title.innerHTML = c;
        } else if (b == "zIndex") {
            a.modeDiv.style.zIndex = c;
        } else if (b == "taskTemplateId") {
            a.taskTemplateId.innerHTML = c;
        } else if (b == "taskTemplateName") {
            a.taskTemplateName.innerHTML = c;
        } else if (b == "resultVariableName") {
            a.resultVariable.innerHTML = c;
        } else if (b == "countersign") {
            a.countersign.innerHTML = c;
        } else if (b == "sequential") {
            a.sequential.innerHTML = c;
        } else if (b == "scriptVal") {
            a.scriptVal.innerHTML = c;
        } else if (b == "endpointUrl") {
            a.endpointUrl.innerHTML = c;
        } else if (b == "payloadExpression") {
            a.payloadExpression.innerHTML = c;
        } else if (b == "queueName") {
            a.queueName.innerHTML = c;
        } else if (b == "blockFlag") {
            a.blockFlag.innerHTML = c;
        } else if (b == "taskType") {
            a.taskType.innerHTML = c;
            $(a.backImg).attr("taskType", c);
        } else if (b == "imgName") {
            a.taskImg.innerHTML = c;
            if (c != "") {
                a.backImg.src = "../../flowimg" + c;
                $(a.title).removeClass().addClass("customTitle");
            }
        } else if (b == "sendMail") {
            a.sendMail.innerHTML = c;
        } else if (b == "sendto") {
            $(a.sendMail).attr("sendto", c);
        } else if (b == "sendtheme") {
            $(a.sendMail).attr("sendtheme", c);
        }


    },
    view: function() {
        var e = new BaseMode(),
            d = this.modeDiv,
            c = com.xjwgraph.Global,
            b = c.modeTool;
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

    create: function() {
        var a = this;
        a.doc = null;
        if (document.all) {
            var e = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0",
                "Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0",
                "MSXML2.DOMDocument", "MSXML.DOMDocument",
                "Microsoft.XMLDOM"
            ];
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
    initBeanXML: function() {
        var b = this,
            c = b.doc.createProcessingInstruction("xml",
                "version=\"1.0\"");
        b.doc.appendChild(c);
        //创建definitions节点
        var definitions = b.doc.createElement("definitions");
        definitions.setAttribute("xmlns", "http://www.omg.org/spec/BPMN/20100524/MODEL");
        definitions.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        definitions.setAttribute("xmlns:uboss", "http://www.ztesoft.com/uboss/bpmn");
        definitions.setAttribute("xmlns:activiti", "http://activiti.org/bpmn");
        definitions.setAttribute("xmlns:bpmndi", "http://www.omg.org/spec/BPMN/20100524/DI");
        definitions.setAttribute("xmlns:omgdc", "http://www.omg.org/spec/DD/20100524/DC");
        definitions.setAttribute("xmlns:omgdi", "http://www.omg.org/spec/DD/20100524/DI");
        definitions.setAttribute("typeLanguage", "http://www.w3.org/2001/XMLSchema");
        definitions.setAttribute("expressionLanguage", "http://www.w3.org/1999/XPath");
        definitions.setAttribute("targetNamespace", "http://www.ztesoft.com/uboss/bpmn");
        b.doc.appendChild(definitions);
        //创建process节点
        var a = b.doc.createElement("process");
        //获取页面processId值
        var processId = $("#processId").val();
        //如果processId为空则表示是新建流程，否则是加载的流程，同时读取原流程id
        if (processId == "") {
            //创建流程ID
            var uuid = new UUID().createUUID();
            processId = uuid;
        }
        a.setAttribute("id", processId);
        a.setAttribute("name", processId);


        //将process节点添加至definitions节点
        definitions.appendChild(a);
        var bpmndi = b.doc.createElement("bpmndi:BPMNDiagram");
        bpmndi.setAttribute("id", "BPMNDiagram_" + processId);
        definitions.appendChild(bpmndi);

        var BPMNDiagram = b.doc.getElementsByTagName("bpmndi:BPMNDiagram")[0];
        var BPMNPlane = b.doc.createElement("bpmndi:BPMNPlane");
        BPMNPlane.setAttribute("id", "BPMNPlane_" + processId);
        BPMNPlane.setAttribute("bpmnElement", processId);
        BPMNDiagram.appendChild(BPMNPlane);

        return a;
    },
    getNodeAttribute: function(f) {
        var d = [],
            b = 0,
            a = f.attributes,
            g = a.length;
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
    getText: function(d, f) {
        var e = [],
            b = 0,
            j = this;
        if (f) {
            e[b++] = "<";
            e[b++] = d.nodeName;
        }
        var h = d.childNodes,
            g = h.length;
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
    createNode: function(b, d) {
        var a = this,
            c = a.doc.createElement(b);
        //根据传递参数d是否存在判断节点创建位置，如果d不存在则创建到根节点，存在则创建在d节点下。参数b表示节点创建名称
        if (d) {
            d.appendChild(c);
        } else {
            a.root.appendChild(c);
        }
        return c;
    },
    clearNode: function() {
        var a = this;
        if (a.root) {
            var d = a.root.childNodes,
                c = d.length;
            for (var b = c; b--;) {
                a.root.removeChild(d[b]);
            }
        }
    },
    _clearNode: function() {
        var a = this;
        if (a) {
            var d = a.childNodes,
                c = d.length;
            for (var b = c; b--;) {
                a.removeChild(d[b]);
            }
        }
    },
    _formatValue: function(a) {
        a = a.replaceAll("&", "&amp;");
        a = a.replaceAll("'", "&apos;");
        a = a.replaceAll('"', "&quot;");
        a = a.replaceAll(">", "&gt;");
        a = a.replaceAll("<", "&lt;");
        return a;
    },
    _replaceValue: function(a) {
        a = a.replaceAll("&amp;gt;", ">");
        a = a.replaceAll("&amp;lt;", "<");
        return a;
    },
    toXML: function(newProcVerId) {
        var result = this.checkXml();
        if (!result) {
            return;
        }
        var a = this;
        a.clearNode();
        var c = com.xjwgraph.Global,
            d = c.baseTool,
            e = d.contextMap;
        var process = a.doc.getElementsByTagName("process")[0];
        varData.subProcessStr = ""; //保存前初始化子流程的subprocess
        d.forEach(c.modeMap, function(u) {
            var o = $id(u),
                m = o.style,
                k = o.attributes,
                j = k.length;
            /*获取页面图片名称开始*/
            var mode_Index = c.modeTool.getModeIndex(o);
            var img = $id("backImg" + mode_Index).src;
            var taskType = $("#taskType" + mode_Index).text();
            var node = "";
            if (taskType != "") {
                node = taskType;
            } else {
                node = img.substring((img.lastIndexOf("/") + 1), img.lastIndexOf("."));
            }

            /*获取页面图片名称结束*/
            // 对于不同的task插入不同的节点元素
            if (node == "userTask") {
                h = a.createNode(node);

                var taskTemplateId = $("#taskTemplateId" + mode_Index).text();
                var taskTemplateName = $("#taskTemplateName" + mode_Index).text();
                h.setAttribute("activiti:taskTemplateId", taskTemplateId);
                h.setAttribute("taskTemplateName", taskTemplateName);
                /*保存自定义图元的图片名称开始*/
                var imgName = $("#taskImg" + mode_Index).text();
                h.setAttribute("imgName", imgName);
                /*保存自定义图元的图片名称结束*/
                var extensionElements = a.createNode("extensionElements", h);
                //				var taskDefinition = a.createNode("uboss:serviceDefinition",extensionElements);
                var countersign = $("#countersign" + mode_Index).text();
                if (countersign == "true") {
                    var sequential = $("#sequential" + mode_Index).text();
                    var multiInstanceLoopCharacteristicc = a.createNode("multiInstanceLoopCharacteristics", h);
                    multiInstanceLoopCharacteristicc.setAttribute("isSequential", sequential);
                }
            } else if (node == "businessRuleTask") {
                h = a.createNode(node);
                var taskTemplateId = $("#taskTemplateId" + mode_Index).text();
                var taskTemplateName = $("#taskTemplateName" + mode_Index).text();
                var resultVariable = $("#resultVariable" + mode_Index).text();
                h.setAttribute("activiti:rules", taskTemplateId);
                h.setAttribute("activiti:taskTemplateId", taskTemplateId);
                h.setAttribute("taskTemplateName", taskTemplateName);
                h.setAttribute("activiti:resultVariableName", resultVariable);
                var extensionElements = a.createNode("extensionElements", h);

            }
            //如果是node=scriptTask则添加属性scriptFormat="juel" 
            else if (node == "scriptTask") {
                var scriptVal = $("#scriptVal" + mode_Index).text();
                h = a.createNode(node);
                h.setAttribute("activiti:scriptFormat", "groovy");
                h.setAttribute("activiti:resultVariable", "");
                var extensionElements = a.createNode("script", h);
                var script = a.doc.createTextNode("<![CDATA[" + scriptVal + "]]>");
                //					var script=a.doc.createTextNode(scriptVal);
                extensionElements.appendChild(script);

            } else if (node == "serviceTask") {
                h = a.createNode(node);
                var taskTemplateId = $("#taskTemplateId" + mode_Index).text();
                var taskTemplateName = $("#taskTemplateName" + mode_Index).text();
				console.info("serviceTask,taskTemplateId: "+taskTemplateId+"taskTemplateName: "+taskTemplateName);
                var blockFlag = $("#blockFlag" + mode_Index).text();
                h.setAttribute("activiti:taskTemplateId", taskTemplateId);
                h.setAttribute("taskTemplateName", taskTemplateName);
                h.setAttribute("blockFlag", blockFlag);
                var extensionElements = a.createNode("extensionElements", h);
                /*保存自定义图元的图片名称开始*/
                var imgName = $("#taskImg" + mode_Index).text();
                h.setAttribute("imgName", imgName);
                /*保存自定义图元的图片名称结束*/


            }
            //如果是node=esbTask则添加属性activiti:type="mule" 				
            else if (node == "esbTask") {
                var taskEndpointUrl = $("#endpointUrl" + mode_Index).text();
                var taskPayloadExpression = $("#payloadExpression" + mode_Index).text();
                var taskQueueName = $("#queueName" + mode_Index).text();
                h = a.createNode("serviceTask");
                h.setAttribute("activiti:type", "mule");
                var extensionElements = a.createNode("extensionElements", h);
                if (taskEndpointUrl != "") {
                    var endpointUrl = a.createNode("activiti:field", extensionElements);
                    endpointUrl.setAttribute("name", "endpointUrl");
                    var endpointUrlString = a.createNode("activiti:string", endpointUrl);
                    var endpointUrlText = a.doc.createTextNode(taskEndpointUrl);
                    endpointUrlString.appendChild(endpointUrlText);
                }
                if (taskPayloadExpression != "") {
                    var payloadExpression = a.createNode("activiti:field", extensionElements);
                    payloadExpression.setAttribute("name", "payloadExpression");
                    var payloadExpressionString = a.createNode("activiti:string", payloadExpression);
                    var payloadExpressionText = a.doc.createTextNode(taskPayloadExpression);
                    payloadExpressionString.appendChild(payloadExpressionText);
                }
                if (taskQueueName != "") {
                    var queueName = a.createNode("activiti:field", extensionElements);
                    queueName.setAttribute("name", "queueName");
                    var queueNameString = a.createNode("activiti:string", queueName);
                    var queueNameText = a.doc.createTextNode(taskQueueName);
                    queueNameString.appendChild(queueNameText);
                }
            } else if (node == "subProcess") {
                //					if(typeof(varData.subProcess[mode_Index])== 'undefined'){
                //						alert("子流程不能为空！");
                //						return ;
                //					}

                var subXml = varData.subProcess[mode_Index];
                //					将replace掉的activiti:重新replace，目的为了能够通过BPMN的activiti:校验 
                var replaceSubXml = subXml.replaceAll(
                        "activiti:taskTemplateId",
                        "taskTemplateId").replaceAll(
                        "activiti:resultVariableName",
                        "resultVariableName")
                    .replaceAll("activiti:scriptFormat",
                        "scriptFormat").replaceAll(
                        "activiti:resultVariable",
                        "resultVariable").replaceAll(
                        "activiti:type", "type")
                    .replaceAll("activiti:rules", "rules").replaceAll("activiti:field", "field").replaceAll("activiti:string", "string");
                var activitiSubXml = replaceSubXml.replaceAll(
                        "taskTemplateId",
                        "activiti:taskTemplateId").replaceAll(
                        "resultVariableName",
                        "activiti:resultVariableName")
                    .replaceAll("scriptFormat",
                        "activiti:scriptFormat")
                    .replaceAll("resultVariable",
                        "activiti:resultVariable")
                    .replaceAll("type", "activiti:type")
                    .replaceAll("rules", "activiti:rules").replaceAll("field", "activiti:field").replaceAll("string", "activiti:string");
                //					function getMyString(str,start,end){
                //				    	var a = str.indexOf(start);
                //				    	var b = str.indexOf(end);
                //				    	return str.substring(a,b+end.length);	
                //				    }
                if (BeanXML.prototype.brower() == "Firefox") { //在firefox下将反馈的字符串添加到varData.subProcessStr下				
                    var modelId = c.modeTool.getModeIndex(o);
                    var name = $id("title" + modelId).innerHTML;
                    var subProcess = BeanXML.prototype.getMyString(activitiSubXml, "<subProcess", "</subProcess>");
                    var subProcessReplaceFinde = BeanXML.prototype.getMyString(subProcess, "<subProcess", ">");
                    varData.subProcessStr += subProcess.replace(subProcessReplaceFinde, "<subProcess id=\"pro" + modelId + "\" name=\"" + name + "\">");

                } else {
                    h = a.createNode("subProcess");
                    var subXmlChildNode = BeanXML.prototype.loadXmlText(activitiSubXml);
                    var childNodes = subXmlChildNode.childNodes[0].childNodes[0].childNodes;

                    for (var i = 0; i < childNodes.length; i++) {
                        var childNode = childNodes[i];
                        h.appendChild(childNode.cloneNode(true)); //appendChild是将node节点转移，所以需要使用cloneNode否则全局变量中的数据会被移除
                    }
                }
            }

            //如果是node=sendTask
            else if (node == "sendTask") {
                var sendTo = $("#sendMail" + mode_Index).attr("sendto");
                var sendTheme = $("#sendMail" + mode_Index).attr("sendtheme");
                var sendTaskVal = $("#sendMail" + mode_Index).text();
                h = a.createNode(node);
                h.setAttribute("activiti:type", "mail");
                var extensionElements = a.createNode("extensionElements", h);
                var activitySendTo = a.createNode("activiti:field", extensionElements);
                activitySendTo.setAttribute("name", "to");
                activitySendTo.setAttribute("expression", sendTo);
                extensionElements.appendChild(activitySendTo);
                var activitySendTheme = a.createNode("activiti:field", extensionElements);
                activitySendTheme.setAttribute("name", "subject");
                activitySendTheme.setAttribute("expression", sendTheme);
                extensionElements.appendChild(activitySendTheme);
                var activitySendType = a.createNode("activiti:field", extensionElements);
                activitySendType.setAttribute("name", "text");
                var activitiExpression = a.createNode("activiti:expression", activitySendType);
                var sendText = a.doc.createTextNode("<![CDATA[" + sendTaskVal + "]]>");
                activitiExpression.appendChild(sendText);
                extensionElements.appendChild(activitySendType);
            } else {
                h = a.createNode(node);
            }
            if (node == "subProcess" && BeanXML.prototype.brower() == "Firefox") {

            } else {

                var p = c.modeMap.get(u);
                var t = p.prop;
                var s = c.modeTool.getModeIndex(o);
                h.setAttribute("id", "pro" + s);
                var r = $id("title" + s).innerHTML;
                h.setAttribute("name", r);

            }


        });


        d.forEach(c.lineMap, function(r) {
            var u = $id(r),
                l = a.createNode("sequenceFlow"),
                n = u.attributes,
                m = n.length,
                q = u.style;

            var g = c.lineMap.get(u.id);
            for (var t in g) {

                if (t === "prop") {
                    continue;
                }
                if (typeof(g[t]) == "string" || typeof(g[t]) == "number") {
                    if (t == "id") {
                        l.setAttribute(t, g[t]);
                    }
                } else {
                    if (typeof(g[t]) == "object") {
                        if (t == "xBaseMode") {
                            l.setAttribute("sourceRef", "pro" + (g[t] && g[t].id ? g[t].id : "").substring(6));
                            if (BaseTool.prototype.isSVG()) {
                                //如果是默认流程则将default属性配置到source图元中
                                if ($("#" + g.id + "LineType").text() == "default") {
                                    var sourceTaskType = $("#backImg" + (g[t] && g[t].id ? g[t].id : "").substring(6)).attr("taskType");
                                    if (sourceTaskType != "parallelGateway") {

                                        var taskNode = a.doc.querySelector("[id=pro" + (g[t] && g[t].id ? g[t].id : "").substring(6) + "]");
                                        //		 var taskNode=a.doc.getElementById("pro"+(g[t] && g[t].id ? g[t].id : "").substring(6));
                                        taskNode.setAttribute("default", g.id);
                                    }
                                }
                            }
                        } else if (t == "wBaseMode") {
                            l.setAttribute("targetRef", "pro" + (g[t] && g[t].id ? g[t].id : "").substring(6));
                        }
                    } else {
                        l.setAttribute(t, g[t] + " is unSupport");
                    }
                }
                l.setAttribute("name", "");
            }
            var name = $("#" + u.id + "Title").text();
            l.setAttribute("name", name);
            var conditionVal = $("#" + u.id + "Condition").text();
            var data_type = $("#" + u.id + "Condition").attr("data_type");
            if (data_type == "conditionExpression") {
                var condition = "${" + conditionVal + "}";
            } else {
                var condition = conditionVal;
            }
            if (conditionVal) {
                var conditionNode = a.createNode(data_type); //根据不同的type类型创建节点，表达式创建conditionExpression节点，服务创建serviceExpression节点
                if (BeanXML.prototype.brower() == "IE") {
                    conditionNode.text = condition;
                } else {
                    conditionNode.appendChild(this.document.createTextNode(condition));
                }
                l.appendChild(conditionNode);
            }
        });

        this.bpmndiXml();
        var b = a.getTextXml(a.doc);
        var varData2 = [];
        if (varData.data != null) {
            for (var i = 0; i < varData.data.length; i++) {
                var data2 = {};

                data2.DEFAULT_VALUE = varData.data[i].DEFAULT_VALUE;
                data2.VAR_NAME = varData.data[i].VAR_NAME;
                data2.VAR_TYPE = varData.data[i].VAR_TYPE;
                data2.VAR_COMMENTS = varData.data[i].VAR_COMMENTS;

                varData2[i] = data2;
            }
        }
        varData.data = varData2;

        var ActTacheList = [];
        for (i in varData.allActTache.buf) {
            var list = varData.allActTache.buf[i];
            if (list != undefined) {
                for (var k = 0; k < list.length; k++) {
                    ActTacheList.push(list[k]);
                }
            }
        }
        //回退设置obj转LIST 开始
        var backList = [];
        for (i in varData.exceptionFallBackObj) {
            for (var j = 0; j < varData.exceptionFallBackObj[i].length; j++) {
                backList.push(varData.exceptionFallBackObj[i][j]);
            }
        }
        //回退设置obj转LIST 结束
        var inParam = {};
        inParam.method = "deployProcessDef";
        if (flowguide == "y") {

            inParam.ID = newProcVerId;
        } else {

            inParam.ID = procVersionId;
        }
        inParam.BPMN_CONTENT = b;
        inParam.VAR_DATA = varData2;
        inParam.SERVICE_VAR_DATA = ActTacheList;
        inParam.BACK_CFG_DATA = backList;

        try {
            var result = callRemoteFunction("ProcessDefineService", inParam); //注销测试查看保存生成的XML
            //			alert("success");
            //			a.viewTextXml(b);
            console.log("保存成功");
            alert("保存成功");
            //showMessage("HINT_SAVE_OK");
            return true;


            
        } catch (e) {
            console.log("保存失败");
           // fish.error("保存失败");
            // showError("Error_INITPAGE",e);
            return false;
        }

    },
    //bpmndi创建,根据activiti5.9标准创建用来生成流程png图片的xml
    bpmndiXml: function() {
        var self = this;
        if (self.doc.childNodes[1] === undefined) {
            var plane = self.doc.childNodes[0].childNodes[1].childNodes; //非IE浏览器用来获取BPMNPlane节点

        } else {
            var plane = self.doc.getElementsByTagName("bpmndi:BPMNPlane"); //IE浏览器获取BPMNPlane节点

        }
        //清除BPMNPlane下的所有子节点

        var childs = plane[0].childNodes;
        for (var i = childs.length - 1; i >= 0; i--) {
            plane[0].removeChild(childs[i]);
        }
        var global = com.xjwgraph.Global,

            tempBaseTool = global.baseTool;
        var BPMNPlane = plane[0];
        varData.subBpminStr = "";
        tempBaseTool.forEach(global.modeMap, function(modeId) {

            var mode = $id(modeId),
                modeStyle = mode.style,
                attributes = mode.attributes,
                attributeLength = attributes.length,

                node = self.createNode("bpmndi:BPMNShape");

            var modeIndex = global.modeTool.getModeIndex(mode);
            node.setAttribute("id", "BPMNShape_" + modeIndex);


            node.setAttribute("bpmnElement", "pro" + modeIndex);

            var backImg = $id("backImg" + modeIndex),
                backImgSrc = backImg.src;
            node_omgdc = self.createNode("omgdc:Bounds");

            node_omgdc.setAttribute("y", parseInt(modeStyle.top));
            node_omgdc.setAttribute("x", parseInt(modeStyle.left));

            node_omgdc.setAttribute("width", parseInt(backImg.offsetWidth));
            node_omgdc.setAttribute("height", parseInt(backImg.offsetHeight));
            var BPMNShape = self.doc.getElementsByTagName("bpmndi:BPMNShape")[0];
            BPMNShape.appendChild(node_omgdc);
            BPMNPlane.appendChild(node);

            var taskType = $("#taskType" + modeIndex).text();
            var node = "";
            if (taskType != "") {
                node = taskType;
            } else {
                node = img.substring((img.lastIndexOf("/") + 1), img.lastIndexOf("."));
            }
            //	var node=backImgSrc.substring((backImgSrc.lastIndexOf("/") + 1), backImgSrc.lastIndexOf("."));
            if (node == "subProcess") {
                var subXml = varData.subProcess[modeIndex];
                //		    var subXmlReplace=subXml.replaceAll("xmlns:bpmndi=\"BPMN/20100524/DI\"", " ").replaceAll("xmlns:omgdc=\"DD/20100524/DC\"", " ");
                if (BeanXML.prototype.brower() == "Firefox") { //在firefox下将反馈的字符串添加到varData.subBpminStr下											
                    var subBpmin = BeanXML.prototype.getMyString(subXml, "<bpmndi:BPMNPlane>", "</bpmndi:BPMNPlane>");
                    varData.subBpminStr += subBpmin.replace("<bpmndi:BPMNPlane>", "").replace("</bpmndi:BPMNPlane>", "");

                } else {

                    var subXmlChildNode = BeanXML.prototype.loadXmlText(subXml);
                    var childNodes = subXmlChildNode.childNodes[0].childNodes[1].childNodes[0].childNodes;
                    for (var i = 0; i < childNodes.length; i++) {
                        var childNode = childNodes[i];
                        BPMNPlane.appendChild(childNode.cloneNode(true)); //appendChild是将node节点转移，所以需要使用cloneNode否则全局变量中的数据会被移除
                    }
                }
            }
        });


        tempBaseTool.forEach(global.lineMap, function(lineId) {

            var line = $id(lineId),
                arry = [],
                node = self.createNode("bpmndi:BPMNEdge"),
                attributes = line.attributes,
                attributeLength = attributes.length,
                lineStyle = line.style;


            var buildLine = global.lineMap.get(line.id);
            for (var item in buildLine) {
                if (typeof(buildLine[item]) == "string" ||
                    typeof(buildLine[item]) == "number") {
                    if (item == "id") {
                        node.setAttribute("bpmnElement", buildLine[item]);
                        node.setAttribute(item, "BPMNEdge_" + buildLine[item]);
                    } else {
                        node.setAttribute(item, buildLine[item]);
                    }
                }
            }

            //创建omgdi:waypoint节点数据		
            var str;
            for (var i = attributeLength; i--;) {

                var attribute = attributes[i];

                if (attribute.name == 'style' || attribute.name == 'marker-end') {
                    continue;
                }

                if (attribute.nodeValue) {
                    if (attribute.name == "d")
                        str = attribute.nodeValue; //获取线上的点坐标值
                }
            }


            var obj = str.split(',');
            // 先按逗号分组
            for (var i = 0; i < obj.length; i++) {
                var obj1 = obj[i].split(' ');
                // 再按空格分组
                for (var j = 0; j < obj1.length; j++) {

                    // 只取数字
                    var Num = obj1[j].replace(/[^0-9]+/ig, "");
                    if (Num != "") {
                        arry.push(Num);
                    }
                }
            }

            for (var i = 0; i < (arry.length) / 2; i++) {
                var waypoint = self.createNode("omgdi:waypoint");
                waypoint.setAttribute("x", arry[2 * i]);
                waypoint.setAttribute("y", arry[2 * i + 1]);
                node.appendChild(waypoint);
            }

            plane[0].appendChild(node);

        });

    },


    loadXmlText: function(c) {

        if (document.all && window.ActiveXObject) {
            var a = this;
            return function(c) {
                var g = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0",
                    "Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0",
                    "MSXML2.DOMDocument", "MSXML.DOMDocument",
                    "Microsoft.XMLDOM"
                ];
                var e = g.length;
                var b = null;
                for (var d = e; d--;) {
                    try {
                        b = new ActiveXObject(g[d]);
                        break;
                    } catch (f) {
                        continue
                    }
                }
                b.async = "false";
                b.loadXML(c);
                return b;
            }
        } else {
            return function(b) {
                return new DOMParser().parseFromString(b, "text/xml");
            }
        }
    }(),
    viewTextXml: function(b) {
        b = b.replaceAll("<", "&lt").replaceAll(">", "&gt");
        var c = window.open("saveXml.html", "", ""),
            a = 0,
            d = [];

        d[a++] = "<html>";
        d[a++] = "<head>";
        d[a++] = '<link href="css/flowPath.css" type="text/css" rel="stylesheet" />';
        d[a++] = '<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />';
        d[a++] = "<title></title>";
        d[a++] = "</head>";
        d[a++] = "<body>";
        d[a++] = b;
        d[a++] = "</body></html>";
        c.document.write(d.join(""));
        c.document.close();
    },
    getTextXml: function(c) {
        var b = "";
        if (c) {
            b = c.xml;
            if (!b) {
                if (c.innerHTML) {
                    b = c.innerHTML;
                } else {
                    var a = new XMLSerializer();
                    b = a.serializeToString(c);
                    if (BeanXML.prototype.brower() == "Firefox" && varData.subBpminStr != "" && varData.subProcessStr != "") {
                        b = b.replace("</process>", varData.subProcessStr + "</process>").replace("</bpmndi:BPMNPlane>", varData.subBpminStr + "</bpmndi:BPMNPlane>");
                        varData.subProcessStr = "";
                        varData.subBpminStr = "";
                    }

                }
            } else {
                b = b.replace(/\r\n\t[\t]*/g, "").replace(/>\r\n/g, ">")
                    .replace(/\r\n/g, "\n");
            }
        }
        //		b = b.replace(/\n/g, "");
        b = this._formatValue(b);
        return b;
    },
    clearHTML: function() {
        var a = com.xjwgraph.Global;
        a.undoRedoEventFactory.clear();
        a.lineTool.removeAll();
        a.modeTool.removeAll();
        a.baseTool.removeAll();
    },
    toHTML: function() {
        var self = this;
        var defaultList = [];
        self.clearHTML();

        if (!self.doc) {
            return;
        }
        var docChildNodes = self.doc.childNodes,
            docChildNodesLength = docChildNodes.length;
        for (var i = docChildNodesLength; i--;) {
            if (docChildNodes[i].nodeName == "definitions") {
                var nodeLength = self.doc.childNodes[i].childNodes.length;
                for (var j = nodeLength; j--;) {
                    if (self.doc.childNodes[i].childNodes[j].nodeName == "#text") {
                        self.doc.childNodes[i].removeChild(self.doc.childNodes[i].childNodes[j]);
                    }
                }
                self.root = self.doc.childNodes[i].childNodes[0]; //root节点，即process节点

                //获取process节点属性值，将process中的id属性值加载到页面上
                var rootAttributes = self.root.attributes;
                var rootAttributesLength = rootAttributes.length;
                for (var i = rootAttributesLength; i--;) {
                    if (rootAttributes[i].name == "id") {
                        var id = rootAttributes[i].value;
                        $("#processId").val(id);
                    }
                }
                break;
            }
        }
        if (self.root) {
            var childNodes = self.root.childNodes; //process下的多个子节点
            var childNodesLength = childNodes.length;
            for (var j = childNodesLength; j--;) {
                if (childNodes[j].nodeName == "#text") {
                    childNodes[j].parentNode.removeChild(childNodes[j]);
                }
            }
            var nodeLength = childNodes.length;
            for (var i = nodeLength; i--;) {
                //此处需要删除#text节点
                var node = childNodes[i],
                    nodeName = node.nodeName,
                    attributes = node.attributes,
                    attributesLength = attributes.length,
                    xml;
                for (var j = self.root.parentNode.childNodes[1].childNodes.length; j--;) {
                    if (self.root.parentNode.childNodes[1].childNodes[j].nodeName == "#text") {
                        self.root.parentNode.childNodes[1].removeChild(self.root.parentNode.childNodes[1].childNodes[j]);
                    }
                }
                var bpmndi = self.root.parentNode.childNodes[1].childNodes[0]; //bpmndi,流程图xml用来存放x,y坐标和图片width，height				
                var nodelength = bpmndi.childNodes.length;
                for (var j = nodelength; j--;) {
                    if (bpmndi.childNodes[j].nodeName == "#text") {
                        bpmndi.removeChild(bpmndi.childNodes[j]);
                    }
                }

                function _getModeStyle(id, nodeName) {
                    xml.setAttribute("backImgSrc", nodeName);
                    for (var x = bpmndilLength; x--;) { //bpmndil节点循环，获取有多少节点
                        var bpmndiChild = bpmndi.childNodes[x];
                        var bpmndAttributes = bpmndiChild.attributes;
                        var bpmndAttributesLength = bpmndAttributes.length;
                        if (bpmndiChild.nodeName == "bpmndi:BPMNShape") {
                            for (v = bpmndAttributesLength; v--;) {
                                var attribute = bpmndAttributes[v];
                                if (attribute.name == "id") { //获取节点中id的属性值
                                    var attributeVal = attribute.value.substring(10);
                                    if (attributeVal == id) { //如果节点id和参数id一致则获取该节点下的内容，用以获取坐标x,y的属性值
                                        var omgdc = bpmndiChild.childNodes[0];
                                        var omgdcAttributes = omgdc.attributes,
                                            omgdcAttributesLength = omgdcAttributes.length;
                                        for (o = omgdcAttributesLength; o--;) {
                                            var attribute = omgdcAttributes[o];
                                            xml.setAttribute(attribute.name, attribute.value);

                                        }

                                    }
                                }

                            }
                        }
                    }
                }
                var bpmndilLength = bpmndi.childNodes.length;
                if (nodeName != "sequenceFlow") { //获取process下除了线以外的图形节点
                    xml = new ModeXML();
                    var id;
                    for (var j = attributesLength; j--;) {
                        var attribute = attributes[j];
                        if (attribute.name == "id") {
                            id = (attribute.value).substring(3);
                            _getModeStyle(id, nodeName); //获取mode的坐标的基本样式信息
                            xml.setAttribute("id", id);
                            xml.setAttribute("zIndex", id);
                            xml.setAttribute("contentEditable", "inherit");
                        } else if (attribute.name == "default") {
                            defaultList.push(attribute.value);
                        } else {
                            xml.setAttribute(attribute.name, attribute.value);
                            if (attribute.value == "mule") {
                                xml.setAttribute("backImgSrc", "esbTask");
                            }

                        }
                    }

                    if (nodeName == "userTask") { //获取userTask节点的multiInstanceLoopCharacteristics节点
                        if (!node.childNodes[1]) { //如果没有multiInstanceLoopCharacteristics节点，会签状态为否countersign值为false
                            xml.setAttribute("countersign", "false");
                        } else {
                            xml.setAttribute("countersign", "true");
                            var attributes = node.childNodes[1].attributes;
                            var attributesLength = attributes.length;
                            for (var j = attributesLength; j--;) {
                                var attribute = attributes[j];
                                if (attribute.name == "isSequential") {
                                    xml.setAttribute("sequential", attribute.value);
                                }
                            }
                        }

                    } else if (nodeName == "subProcess") {
                        varData.subProcess[id] = "<definitions xmlns:activiti=\"activiti.org/bpmn\" xmlns:bpmndi=\"BPMN/20100524/DI\" xmlns:omgdc=\"DD/20100524/DC\" xmlns:omgdi=\"DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.ztesoft.com/uboss/bpmn\">"
                        var nodeText = this.getTextXml(node);
                        var nodeTextReplace = nodeText.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&amp;lt;", "<").replaceAll("&amp;gt;", ">");
                        varData.subProcess[id] += nodeTextReplace;
                        varData.subProcess[id] += "<bpmndi:BPMNDiagram><bpmndi:BPMNPlane>";
                        for (var x = bpmndilLength; x--;) {
                            var bpmndiChild = bpmndi.childNodes[x];
                            var bpmndAttributes = bpmndiChild.attributes;
                            var bpmndAttributesLength = bpmndAttributes.length;
                            for (v = bpmndAttributesLength; v--;) {
                                var attribute = bpmndAttributes[v];
                                if (attribute.name == "bpmnElement") {
                                    if (attribute.value.indexOf("_") != "-1") {
                                        var attributeVal = attribute.value.substring(0, attribute.value.indexOf("_"));
                                        if ("sub" + id == attributeVal) {
                                            var bpmnPlaneText = this.getTextXml(bpmndiChild);
                                            var bpmnPlaneTextReplace = bpmnPlaneText.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
                                            varData.subProcess[id] += bpmnPlaneTextReplace;
                                        }
                                    }
                                }
                            }
                        }
                        varData.subProcess[id] += "</bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>";
                    } else if (nodeName == "serviceTask") { //获取serviceTask根据type的属性是否为mule判断是esbTask							

                        if (attribute.value == "mule") {
                            var extensionElements = node.childNodes[0];
                            var extensionElementsLength = node.childNodes[0].childNodes.length;
                            for (x = extensionElementsLength; x--;) {
                                var file = extensionElements.childNodes[x];
                                var attributes = file.attributes;
                                var attributesLength = attributes.length;
                                for (var j = attributesLength; j--;) {
                                    var attribute = attributes[j];
                                    if (attribute.name == "name" && attribute.value == "endpointUrl") {
                                        var endpointUrl = file.childNodes[0].childNodes[0].nodeValue;
                                        xml.setAttribute("endpointUrl", endpointUrl);
                                    } else if (attribute.name == "name" && attribute.value == "payloadExpression") {
                                        var payloadExpression = file.childNodes[0].childNodes[0].nodeValue;
                                        xml.setAttribute("payloadExpression", payloadExpression);
                                    } else if (attribute.name == "name" && attribute.value == "queueName") {
                                        var queueName = file.childNodes[0].childNodes[0].nodeValue;
                                        xml.setAttribute("queueName", queueName);
                                    }
                                }
                            }
                        }
                    } else if (nodeName == "scriptTask") { //获取scriptTask节点的scriptVal值
                        if (typeof(node.childNodes[0].childNodes[0]) == "undefined") {
                            var scriptVal = "";
                        } else {
                            var scriptVal = node.childNodes[0].childNodes[0].nodeValue;
                        }
                        xml.setAttribute("scriptVal", scriptVal);

                    } else if (nodeName == "sendTask") { //获取scriptTask节点的scriptVal值						
                        var extensionElements = node.childNodes[0];
                        var extensionElementsLength = node.childNodes[0].childNodes.length;
                        for (x = extensionElementsLength; x--;) {
                            var file = extensionElements.childNodes[x];
                            var attributes = file.attributes;
                            var attributesLength = attributes.length;
                            for (var j = attributesLength; j--;) {
                                var attribute = attributes[j];
                                if (attribute.name == "name" && attribute.value == "to") {
                                    for (var v = attributesLength; v--;) {
                                        var sendAttribute = attributes[v];
                                        if (sendAttribute.name == "expression") {
                                            xml.setAttribute("sendto", sendAttribute.value);
                                        }
                                    }

                                } else if (attribute.name == "name" && attribute.value == "subject") {
                                    for (var v = attributesLength; v--;) {
                                        var sendAttribute = attributes[v];
                                        if (sendAttribute.name == "expression") {
                                            xml.setAttribute("sendtheme", sendAttribute.value);
                                        }
                                    }

                                } else if (attribute.name == "name" && attribute.value == "text") {
                                    var sendBody = file.childNodes[0].childNodes[0].nodeValue;
                                    xml.setAttribute("sendMail", sendBody);
                                }
                            }
                        }

                    }
                    //					else if	(nodeName == "businessRuleTask"){    //获取businessRuleTask节点的taskTemplateId值						
                    //						var businessRuleTask=node.childNodes[0].childNodes[0];
                    //						attributes = businessRuleTask.attributes,
                    //						attributesLength = attributes.length;
                    //						for (var j = attributesLength; j--;) {
                    //							var attribute = attributes[j];
                    //							xml.setAttribute(attribute.name,attribute.value);
                    //							
                    //						}
                    //					}
                    //					else if	(nodeName == "esbTask"){    //获取esbTask节点的
                    //						
                    //					}
                    xml.setAttribute("taskType", nodeName);
                } else {
                    xml = new LineXML();
                    /*设置一些线的基本属性开始*/
                    xml.setAttribute("marker-end", "url(#arrow)");
                    xml.setAttribute("style", "WIDTH: 100px; CURSOR: pointer; POSITION: absolute; HEIGHT: 100px;fill: none; stroke: black; stroke-width: 1.7999999999999998;");
                    xml.setAttribute("strokeweight", "1.35");
                    xml.setAttribute("strokecolor", "black");
                    xml.setAttribute("contentEditable", "inherit");
                    /*设置一些线的基本属性结束*/
                    /*读取node属性开始*/
                    var id;

                    if (node.childNodes[0]) {
                        var nodeName = node.childNodes[0].nodeName;
                        if (nodeName == "conditionExpression") {
                            if (this.brower() == "IE") {
                                var conditionVal = (node.childNodes[0]).text;
                                xml.setAttribute("condition", conditionVal.substring(2, conditionVal.lastIndexOf("}")));
                            } else {

                                var conditionVal = node.childNodes[0].childNodes[0].nodeValue;
                                xml.setAttribute("condition", conditionVal.substring(2, conditionVal.lastIndexOf("}")));
                            }
                        } else {
                            if (this.brower() == "IE") {
                                var conditionVal = (node.childNodes[0]).text;
                                xml.setAttribute("condition", conditionVal);
                            } else {

                                var conditionVal = node.childNodes[0].childNodes[0].nodeValue;
                                xml.setAttribute("condition", conditionVal);
                            }
                        }
                        xml.setAttribute("data_type", nodeName);
                    }
                    for (var j = attributesLength; j--;) {
                        var attribute = attributes[j];

                        function _getLineStyle(id) {
                            for (var x = bpmndilLength; x--;) { //bpmndil节点循环，获取有多少节点
                                var bpmndiChild = bpmndi.childNodes[x];
                                var bpmndAttributes = bpmndiChild.attributes;
                                var bpmndAttributesLength = bpmndAttributes.length;
                                if (bpmndiChild.nodeName == "bpmndi:BPMNEdge") {
                                    for (v = bpmndAttributesLength; v--;) {
                                        var attribute = bpmndAttributes[v];
                                        if (attribute.name == "id") {
                                            var attributeVal = attribute.value.substring(9); //获取节点中id的属性值
                                            if (attributeVal == id) { //如果节点id和参数id一致则获取该节点下的内容，用以获取坐标x,y的属性值
                                                var path;
                                                var BPMNEdgeLength = bpmndiChild.childNodes.length;
                                                if (BPMNEdgeLength == 2) {
                                                    var omgdc1 = bpmndiChild.childNodes[0];
                                                    var omgdc2 = bpmndiChild.childNodes[1];
                                                    var x1 = omgdc1.attributes[0].value;
                                                    var y1 = omgdc1.attributes[1].value;
                                                    var x2 = omgdc2.attributes[0].value;
                                                    var y2 = omgdc2.attributes[1].value;
                                                    xml.setAttribute("brokenType", "1");
                                                    xml.setAttribute("x1", x1);
                                                    xml.setAttribute("x2", x2);
                                                    var y = y1 - (y1 - y2) / 2
                                                    xml.setAttribute("y", y);
                                                    path = "m" + x1 + " " + y1 + " L " + x2 + " " + y2 + " z"; //根据坐标x,y拼接成线所需要的path
                                                } else if (BPMNEdgeLength == 4) {
                                                    var omgdc1 = bpmndiChild.childNodes[0];
                                                    var omgdc2 = bpmndiChild.childNodes[1];
                                                    var omgdc3 = bpmndiChild.childNodes[2];
                                                    var omgdc4 = bpmndiChild.childNodes[3];
                                                    var x1 = omgdc1.attributes[0].value;
                                                    var y1 = omgdc1.attributes[1].value;
                                                    var x2 = omgdc2.attributes[0].value;
                                                    var y2 = omgdc2.attributes[1].value;
                                                    var x3 = omgdc3.attributes[0].value;
                                                    var y3 = omgdc3.attributes[1].value;
                                                    var x4 = omgdc4.attributes[0].value;
                                                    var y4 = omgdc4.attributes[1].value;
                                                    xml.setAttribute("brokenType", "2");
                                                    xml.setAttribute("x1", x1);
                                                    xml.setAttribute("x2", x4);
                                                    xml.setAttribute("y", y2);
                                                    path = "M " + x1 + " " + y1 + " L " + x2 + " " + y2 + "," + x3 + " " + y3 + "," + x4 + " " + y4;
                                                }
                                                xml.setAttribute("d", path);
                                                for (var i = 0; i < bpmndAttributes.length; i++) {
                                                    if (bpmndAttributes[i].name == "xIndex") {
                                                        xml.setAttribute("xIndex", bpmndAttributes[i].value); //设置xIndex值，xIndex表示线开始的点，根据数字表示端点所处位置，数值意思参见BaseTool.js
                                                    } else if (bpmndAttributes[i].name == "wIndex") {
                                                        xml.setAttribute("wIndex", bpmndAttributes[i].value); //设置wIndex值，wIndex表示线终点位置
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        }
                        if (attribute.name == "id") {
                            id = attribute.value;
                            _getLineStyle(id);

                        }
                        if (attribute.name == "sourceRef") {
                            xml.setAttribute("xBaseMode", "module" + (attribute.value).substring(3));
                        } else if (attribute.name == "targetRef") {
                            xml.setAttribute("wBaseMode", "module" + (attribute.value).substring(3));
                        } else {
                            xml.setAttribute(attribute.name, attribute.value);
                        }
                    }
                    /*读取node属性结束*/

                }

                //				function _getModeStyle(id,nodeName){  
                //					xml.setAttribute("backImgSrc",nodeName);
                //					for (var x = bpmndilLength; x--;) {            //bpmndil节点循环，获取有多少节点
                //						var bpmndiChild = bpmndi.childNodes[x];
                //						var bpmndAttributes = bpmndiChild.attributes;			
                //						var bpmndAttributesLength = bpmndAttributes.length;
                //						if(bpmndiChild.nodeName=="bpmndi:BPMNShape"){
                //						for (v = bpmndAttributesLength; v--;) {
                //							var attribute = bpmndAttributes[v];
                //							if (attribute.name == "id") {			//获取节点中id的属性值
                //								var attributeVal = attribute.value.substring(10);
                //								if (attributeVal == id) {		//如果节点id和参数id一致则获取该节点下的内容，用以获取坐标x,y的属性值
                //									var omgdc = bpmndiChild.childNodes[0];
                //									var omgdcAttributes = omgdc.attributes, omgdcAttributesLength = omgdcAttributes.length;
                //									for (o = omgdcAttributesLength; o--;) {
                //										var attribute = omgdcAttributes[o];										
                //										xml.setAttribute(attribute.name,attribute.value);
                //										
                //										}
                //
                //									}
                //								}
                //
                //							}
                //						}
                //					}
                //				}

                //				function _getLineStyle(id){  
                //					for (var x = bpmndilLength; x--;) {            //bpmndil节点循环，获取有多少节点
                //						var bpmndiChild = bpmndi.childNodes[x];
                //						var bpmndAttributes = bpmndiChild.attributes;
                //						var bpmndAttributesLength = bpmndAttributes.length;
                //						if(bpmndiChild.nodeName=="bpmndi:BPMNEdge"){
                //						for (v = bpmndAttributesLength; v--;) {
                //							var attribute = bpmndAttributes[v];
                //							if (attribute.name == "id") {			
                //								var attributeVal = attribute.value.substring(9);   //获取节点中id的属性值
                //								if (attributeVal == id) {		//如果节点id和参数id一致则获取该节点下的内容，用以获取坐标x,y的属性值
                //									var path;
                //									var BPMNEdgeLength=bpmndiChild.childNodes.length;
                //									if(BPMNEdgeLength==2){
                //										var omgdc1=bpmndiChild.childNodes[0];
                //										var omgdc2=bpmndiChild.childNodes[1];
                //										var x1=omgdc1.attributes[0].value;
                //										var y1=omgdc1.attributes[1].value;
                //										var x2=omgdc2.attributes[0].value;
                //										var y2=omgdc2.attributes[1].value;
                //										xml.setAttribute("brokenType","1");
                //										xml.setAttribute("x1",x1);
                //										xml.setAttribute("x2",x2);
                //										var y=y1-(y1-y2)/2
                //										xml.setAttribute("y",y);
                //										path="m"+x1+" "+y1+" L "+x2+" "+y2+" z";    //根据坐标x,y拼接成线所需要的path
                //									}
                //									else if(BPMNEdgeLength==4){
                //										var omgdc1=bpmndiChild.childNodes[0];
                //										var omgdc2=bpmndiChild.childNodes[1];
                //										var omgdc3=bpmndiChild.childNodes[2];
                //										var omgdc4=bpmndiChild.childNodes[3];
                //										var x1=omgdc1.attributes[0].value;
                //										var y1=omgdc1.attributes[1].value;
                //										var x2=omgdc2.attributes[0].value;
                //										var y2=omgdc2.attributes[1].value;
                //										var x3=omgdc3.attributes[0].value;
                //										var y3=omgdc3.attributes[1].value;
                //										var x4=omgdc4.attributes[0].value;
                //										var y4=omgdc4.attributes[1].value;
                //										xml.setAttribute("brokenType","3");
                //										xml.setAttribute("x1",x1);
                //										xml.setAttribute("x2",x4);
                //										xml.setAttribute("y",y2);
                //										path="M "+x1+" "+y1+" L "+x2+" "+y2+","+x3+" "+y3+","+x4+" "+y4;
                //									}	
                //									xml.setAttribute("d",path);	
                //									for(var i=0;i<bpmndAttributes.length;i++){
                //										if(bpmndAttributes[i].name=="xIndex"){
                //											xml.setAttribute("xIndex",bpmndAttributes[i].value);//设置xIndex值，xIndex表示线开始的点，根据数字表示端点所处位置，数值意思参见BaseTool.js
                //										}else if(bpmndAttributes[i].name=="wIndex"){
                //											xml.setAttribute("wIndex",bpmndAttributes[i].value);//设置wIndex值，wIndex表示线终点位置
                //										}
                //									}
                //										
                //									}
                //								}
                //
                //							}
                //						}
                //					}
                //				}		

                xml.view();
            }

            var delays = self.delay,
                delayLength = delays.length;
            for (var i = delayLength; i--;) {
                delays[i]();
            }

            delete self.delay;

            self.delay = [];
            self.delayIndex = 0;
        }
        for (i = 0; i < defaultList.length; i++) {
            var lineId = defaultList[i];
            $("#" + lineId + "LineType").text("default");
            LineTool.prototype.lineTypeSet(lineId);
        }
    },
    brower: function() {
        var brower = "";
        if (navigator.userAgent.indexOf("MSIE") > 0) {
            brower = "IE";
        } else if (navigator.userAgent.indexOf("Chrome") > 0) {
            brower = "Chrome";
        } else if (navigator.userAgent.indexOf("Firefox") > 0) {
            brower = "Firefox";
        } else if (navigator.userAgent.indexOf("Safari") > 0) {
            brower = "Safari";
        } else {
            brower = navigator.appName;
        }
        //	var brower=navigator.appName.indexOf("Microsoft") != -1 ? 'IE' : navigator.appName;	
        return brower;
    },
    getMyString: function(str, start, end) {
        var a = str.indexOf(start);
        var b = str.indexOf(end);
        return str.substring(a, b + end.length);
    },
    checkXml: function(data) {
        var a = this;
        var sourceRefList = [];
        var targetRefList = [];
        var c = com.xjwgraph.Global,
            d = c.baseTool,
            e = d.contextMap;
        var lineList = [];
        var moduleList = [];
        var taskTemplateList = [];
        var result = true;

        function inArray(needle, array) {
            var result = false;
            if (typeof needle == "string" || typeof needle == "number") {
                var len = array.length;
                for (var i = 0; i < len; i++) {
                    if (needle === array[i]) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }

        function notInArray(needle, array) {
            var result = false;
            if (typeof needle == "string" || typeof needle == "number") {
                var len = array.length;
                for (var i = 0; i < len; i++) {
                    if (needle === array[i]) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }


        /* 对转换线做循环获取转换线两头连接的图元名称开始 */
        d.forEach(c.lineMap, function(r) {
            var u = $id(r);
            var g = c.lineMap.get(u.id);
            var obj = new Object();
            for (var t in g) {
                if (typeof(g[t]) == "object") {
                    if (t == "xBaseMode") {
                        obj["sourceRef"] = g[t] && g[t].id ? g[t].id : "";
                    } else if (t == "wBaseMode") {
                        obj["targetRef"] = g[t] && g[t].id ? g[t].id : "";
                    }
                }
            }
            obj["line"] = g.id;
            lineList.push(obj);

        });
        /*对转换线做循环获取转换线两头连接的图元名称结束*/
        /*判断转换线的两端是否都链接了图元，如果没有提示检查转换线开始*/
        for (var i = 0; i < lineList.length; i++) {
            var j = 0;
            for (var t in lineList[i]) {

                if (t == "sourceRef") {
                    sourceRefList.push(lineList[i].sourceRef);
                    j++;
                } else if (t == "targetRef") {
                    targetRefList.push(lineList[i].targetRef);
                    j++;
                }

            }
            if (j < 2) {

                showError(qryWebRes("PROCESS_CHECK"));
                return result = false;
                break;
            }

        }
        /*判断转换线的两端是否都链接了图元，如果没有提示检查转换线结束*/
        /*获取图元的类型以及图元ID开始*/
        d.forEach(c.modeMap, function(u) {
            var o = $id(u),
                m = o.style,
                k = o.attributes,
                j = k.length;
            var obj = new Object();
            /*获取页面图片名称开始*/
            var mode_Index = c.modeTool.getModeIndex(o);
            var img = $id("backImg" + mode_Index).src;
            //					var node=img.substring((img.lastIndexOf("/") + 1), img.lastIndexOf("."));
            var node = $("#backImg" + mode_Index).attr("taskType");
            if (node == "userTask" || node == "serviceTask" || node == "businessRuleTask") {
                obj["taskTemplateId"] = $("#taskTemplateId" + mode_Index).text();
                taskTemplateList.push($("#taskTemplateId" + mode_Index).text());
            }
            obj["mode_Index"] = mode_Index;
            obj["name"] = node;
            obj["module"] = "module" + mode_Index;
            /*获取页面图片名称结束*/
            moduleList.push(obj);
        });


        /*判断流程图是否绘制开始*/
        if (lineList.length == 0 || moduleList.length == 0) {
            showError(qryWebRes("DO_DRAWING_PROCESS"));
            return result = false;
        }

        /*判断流程图是否绘制结束*/
        for (var i = 0; i < moduleList.length; i++) {
            if (moduleList[i].name == "startEvent") {
                result = inArray(moduleList[i].module, sourceRefList);
                if (!result) {
                    showError(qryWebRes("STARTEVENT_CHECK"));
                    return result;
                    break;
                }
                result = inArray(moduleList[i].module, targetRefList);
                if (result) {
                    result = false;
                    showError(qryWebRes("STARTEVENT_CHECK"));
                    return result;
                    break;
                }
                result = true;
            } else if (moduleList[i].name == "endEvent") {
                result = inArray(moduleList[i].module, targetRefList);
                if (!result) {
                    showError(qryWebRes("ENDEVENT_CHECK"));
                    return result;
                    break;
                }
                result = inArray(moduleList[i].module, sourceRefList);
                if (result) {
                    result = false;
                    showError(qryWebRes("ENDEVENT_CHECK"));
                    return result;
                    break;
                }
            } else if (moduleList[i].name == "userTask" || moduleList[i].name == "serviceTask" || moduleList[i].name == "businessRuleTask") {
                if (moduleList[i].taskTemplateId == "") {
                    showError(qryWebRes("TEMPLATEID_CHECK"));
                    return result = false;
                }
            } else {
                result = inArray(moduleList[i].module, sourceRefList);
                if (!result) {
                    showError(qryWebRes("FLOW_ERR"));
                    return result;
                    break;
                }
                result = inArray(moduleList[i].module, targetRefList);
                if (!result) {
                    showError(qryWebRes("FLOW_ERR"));
                    return result;
                    break;
                }
            }
        }
        result = this.checkDefaultLine();
        if (!result) {
            showError(qryWebRes("DEFAULT_ERR"));
            return result;
        }

        if (result && data == "check") {
            var nary = taskTemplateList.sort();
            var warning = "";
            for (var i = 0; i < taskTemplateList.length; i++) {
                if (nary[i] == nary[i + 1]) {
                    warning = "Warning";

                }
            }
            if (warning == "Warning") {
                showWarning(qryWebRes("TEMPLATE_EXISTS"));
            } else {
                showMessage(qryWebRes("FLOW_OK"));
            }
        }
        return result;

    },
    backSet: function() {
        var a = this;
        var sourceRefList = [];
        var targetRefList = [];
        var c = com.xjwgraph.Global,
            d = c.baseTool,
            e = d.contextMap;
        var lineList = [];
        var moduleList = [];
        var usetaskTemplateList = [];
        /* 对转换线做循环获取转换线两头连接的图元名称开始 */
        d.forEach(c.lineMap, function(r) {
            var u = $id(r);
            var g = c.lineMap.get(u.id);
            var obj = new Object();
            for (var t in g) {
                if (typeof(g[t]) == "object") {
                    if (t == "xBaseMode") {
                        obj["sourceRef"] = g[t] && g[t].id ? g[t].id : "";
                    } else if (t == "wBaseMode") {
                        obj["targetRef"] = g[t] && g[t].id ? g[t].id : "";
                    }
                }
            }
            obj["line"] = g.id;
            lineList.push(obj);

        });

        /*获取图元的类型以及图元ID开始*/
        d.forEach(c.modeMap, function(u) {
            var o = $id(u),
                m = o.style,
                k = o.attributes,
                j = k.length;
            var obj = new Object();
            var userTaskObj = new Object();
            /*获取页面图片名称开始*/
            var mode_Index = c.modeTool.getModeIndex(o);
            var img = $id("backImg" + mode_Index).src;
            //				var node=img.substring((img.lastIndexOf("/") + 1), img.lastIndexOf("."));
            var node = $("#backImg" + mode_Index).attr("taskType");
            //				if(node=="userTask"||node=="serviceTask"){           //只对人工任务和服务任务做回退设置
            if (node == "userTask") { //只对人工任务做回退设置
                obj["taskTemplateId"] = $("#taskTemplateId" + mode_Index).text();

                userTaskObj["taskTemplateId"] = $("#taskTemplateId" + mode_Index).text();
                userTaskObj["mode_Index"] = mode_Index;
                userTaskObj["taskType"] = node;
                userTaskObj["module"] = "module" + mode_Index;
                userTaskObj["name"] = $("#taskTemplateName" + mode_Index).text();
                userTaskObj["title"] = $("#title" + mode_Index).text();
                usetaskTemplateList.push(userTaskObj);
            }
            obj["name"] = $("#taskTemplateName" + mode_Index).text();
            obj["taskTemplateId"] = $("#taskTemplateId" + mode_Index).text();
            if (node == "startEvent") { //如果是startEvent，设置回滚目标名称为开始
                obj["title"] = qryWebRes("START");
            } else {
                obj["title"] = $("#title" + mode_Index).text();
            }
            obj["mode_Index"] = mode_Index;
            obj["taskType"] = node;
            obj["module"] = "module" + mode_Index;
            /*获取页面图片名称结束*/
            moduleList.push(obj);
        });
        /*判断流程图是否绘制开始*/
        if (lineList.length == 0 || moduleList.length == 0) {
            showError(qryWebRes("DO_DRAWING_PROCESS"));
            return result = false;
        }
        var url = WebRoot + "modules/bpmn/flowdesigner/draw/fallBack.jsp";
        var input = new Object();
        input.list = [];
        input.moduleList = moduleList;
        input.lineList = lineList;
        input.usetaskTemplateList = usetaskTemplateList;
        input.exceptionFallBackObj = varData.exceptionFallBackObj;
        var fallBack_setting = qryWebRes("FALLBACK_SET");
        showModalWindow(fallBack_setting, url, 800, 400, input, function(backObj) {
            if (backObj != null) {
                varData.exceptionFallBackObj = new Object();
                varData.exceptionFallBackObj = backObj.obj;
                varData.exceptionFallBackList = backObj.list;
            }
        });



        function module(moduleList, lineList, moduleId) {}

        function getSource(lineList, targetModuleId) {
            var sourcesList = [];
            for (var i = 0; i < lineList.length; i++) {
                if (lineList[i].targetRef == targetModuleId) {
                    sourcesList.push(lineList[i].sourceRef);
                }
            }
            return sourcesList;
        }

        function getMoudleObj(moduleList, ModuleId) {
            var obj = {};
            for (var i = 0; i < moduleList.length; i++) {
                if (moduleList[i].module == ModuleId) {
                    obj = moduleList[i];
                }
            }
            return obj;
        }

    },
    checkDefaultLine: function() {
        var c = com.xjwgraph.Global,
            d = c.baseTool,
            e = d.contextMap;
        var defaultModuleList = [];
        var result = true;
        d.forEach(c.lineMap, function(r) {
            var u = $id(r);
            var g = c.lineMap.get(u.id);
            for (var t in g) {
                if (typeof(g[t]) == "object") {
                    if (t == "xBaseMode") {
                        if (BaseTool.prototype.isSVG()) {
                            //如果是默认流程则将default属性配置到source图元中
                            if ($("#" + g.id + "LineType").text() == "default") {
                                var sourceTaskType = $("#backImg" + (g[t] && g[t].id ? g[t].id : "").substring(6)).attr("taskType");
                                if (sourceTaskType != "parallelGateway") {
                                    //							     var taskNode=a.doc.getElementById("pro"+(g[t] && g[t].id ? g[t].id : "").substring(6));
                                    defaultModuleList.push(g[t] && g[t].id ? g[t].id : "");
                                }
                            }
                        }
                    }
                }
            }


        });
        var nary = defaultModuleList.sort();
        for (var i = 0; i < nary.length - 1; i++) {
            if (nary[i] == nary[i + 1]) {
                //		        alert("重复内容：" + nary[i]);
                result = false;
            }
        }
        return result;
    }
};
