//模元工具. 模元工具文件
var ModeTool = com.xjwgraph.ModeTool = function(a) {
	var b = this;
	b.moveable = false;
	b.optionMode;
	b.baseModeIdIndex = PathGlobal.modeDefIndex;
	b.stepIndex = PathGlobal.modeDefStep;
	b.pathBody = a;
	b.tempMode
};
ModeTool.prototype = {
		initScaling : function(a) {
			var b = this, c = com.xjwgraph.Global.smallTool;
			
			b
					.forEach(function(l) {
						var g = $id(l), f = g.style, h = b.getModeIndex(g), i = $id("content"
								+ h), j = $id("backImg" + h), m = i.style, k = j.style, e = parseInt(parseInt(g.offsetWidth)
								/ a)
								+ "px", d = parseInt(parseInt(g.offsetHeight) / a)
								+ "px";
						f.top = parseInt(parseInt(f.top) / a) + "px";
						f.left = parseInt(parseInt(f.left) / a) + "px";
						m.width = e;
						m.height = d;
						k.width = e;
						k.height = d;
						f.width = e;
						f.height = d;
						b.showPointer(g);
						c.drawMode(g);
					})
		},
		showMenu : function(a, f) {
			PathGlobal.rightMenu = true;
			var j = this;
			j.tempMode = f.parentNode;
			a = a || window.event;
			if (!a.pageX) {
				a.pageX = a.clientX
			}
			if (!a.pageY) {
				a.pageY = a.clientY
			}
			var e = a.pageX, d = a.pageY, c = com.xjwgraph.Global, b = c.lineTool.pathBody, h = c.baseTool
					.sumLeftTop(b);
			e = e - parseInt(h[0]) + parseInt(b.scrollLeft);
			d = d - parseInt(h[1]) + parseInt(b.scrollTop);
			var g = $id("rightMenu"), i = g.style;
			i.top = d + "px";
			i.left = e + "px";
			i.visibility = "visible";
			i.zIndex = j.getNextIndex()
		},
		showProperty : function(e) {
			var d = com.xjwgraph.Global, c = d.modeMap, b = c.get(this.tempMode.id), h = b.prop, g = document, a = $id("prop"), f = d.clientTool;
			a.style.visibility = "";
			a.innerHTML = "";
			a.appendChild(f.addProItem(h));
			f.showDialog(e, PathGlobal.modeProTitle, b)
		},
		removeAll : function() {
			var a = this;
			a.forEach(function(b) {
				a.removeNode(b)
			})
		},
		removeNode : function(d) {
			var c = $id(d);
			if (c) {
				var b = com.xjwgraph.Global, a = b.lineTool.pathBody;
				this.hiddPointer(c);
				b.modeMap.remove(c.id);
				b.smallTool.removeMode(c.id);
				a.removeChild(c)
			}
		},
		cutter : function() {
			var c = this, b = c.tempMode.id, e = com.xjwgraph.Global, g = e.modeMap
					.get(b), f = $id(b), a = e.lineTool.pathBody;
			c.removeNode(b);
			var d = new com.xjwgraph.UndoRedoEvent(function() {
				e.modeMap.put(b, g);
				a.appendChild(f);
				c.showPointer(f);
				e.smallTool.drawMode(f)
			}, PathGlobal.modeCutter);
			d.setRedo(function() {
				c.removeNode(b)
			})
		},
		duplicate : function() {
			var e = com.xjwgraph.Global, c = e.lineTool.pathBody, b = this, a = e.modeTool
					.copy(b.tempMode), f = e.modeMap.get(a.id);
			e.modeTool.hiddPointer(b.tempMode);
			e.smallTool.drawMode(a);
			var d = new com.xjwgraph.UndoRedoEvent(function() {
				b.removeNode(a.id)
			}, PathGlobal.modeDuplicate);
			d.setRedo(function() {
				e.modeMap.put(a.id, f);
				c.appendChild(a);
				b.showPointer(a);
				e.smallTool.drawMode(a)
			})
		},
		del : function() {
			var a = this;
			a.cutter();
			a.tempMode = null
		},
		getNextIndex : function() {
			var a = this;
			a.baseModeIdIndex += a.stepIndex;
			return a.baseModeIdIndex
		},
		setClass : function(b, a) {
			if (b) {
				b.setAttribute("class", a);
				b.setAttribute("className", a)
			}
		},
		//创建拖动的图标
		createBaseMode : function(o, f, a, i, e, l,baseMode) {
			 var baseModeTaskTemplateName="";
    var baseModeTaskType="";
    var baseModeTaskTemplateId="";
    var baseImg="";
    var taskMode="";
    if(baseMode===undefined){
    	}
    	else{
     baseModeTaskTemplateName=$(baseMode).find('div').attr('taskName');
     baseModeTaskType=$(baseMode).find('div').attr('taskType');
     baseModeTaskTemplateId=$(baseMode).find('div').attr('taskId');
     taskMode =$(baseMode).find('div').attr('taskMode');
     if(taskMode=="AD"){
     baseImg=$(baseMode).find('img').attr('src').substring($(baseMode).find('img').attr('src').lastIndexOf("/") + 1);
  	}
   }

		var p = this, u = document, j = u.createElement("div"), t = u
					.createElement("div"), q = u.createElement("div"), s = u
					.createElement("img");
		//创建taskTemplateIdDiv,countersignDiv,sequentialDiv
		var taskTemplateIdDiv = u.createElement("div");
			p.setClass(taskTemplateIdDiv, "taskTemplateId");
		var taskTemplateNameDiv = u.createElement("div");
			p.setClass(taskTemplateNameDiv, "taskTemplateName");	
		var countersignDiv = u.createElement("div");
			p.setClass(countersignDiv, "countersign");	
		var sequentialDiv = u.createElement("div");
			p.setClass(sequentialDiv, "sequential");	
		var resultVariableDiv = u.createElement("div");
			p.setClass(resultVariableDiv, "resultVariable");
		var scriptValDiv = u.createElement("div");
			p.setClass(scriptValDiv, "scriptVal");	
		var endpointUrlDiv = u.createElement("div");
			p.setClass(endpointUrlDiv, "endpointUrl");
		var payloadExpressionDiv = u.createElement("div");
			p.setClass(payloadExpressionDiv, "payloadExpression");
		var queueNameDiv = u.createElement("div");
			p.setClass(queueNameDiv, "queueName");
		var blockFlagDiv = u.createElement("div");
			p.setClass(blockFlagDiv, "blockFlag");	
			var taskTypeDiv = u.createElement("div");
			p.setClass(taskTypeDiv, "taskType");
		var taskImgDiv = u.createElement("div");
			p.setClass(taskImgDiv, "taskImg");		
		var sendMailDiv = u.createElement("div");
			p.setClass(sendMailDiv, "sendMail");			
			j.appendChild(t);
			j.appendChild(taskTemplateIdDiv);
			j.appendChild(taskTemplateNameDiv);
			j.appendChild(resultVariableDiv);
			j.appendChild(countersignDiv);
			j.appendChild(sequentialDiv);
			j.appendChild(scriptValDiv);
			j.appendChild(endpointUrlDiv);
			j.appendChild(payloadExpressionDiv);
			j.appendChild(queueNameDiv);
			j.appendChild(blockFlagDiv);
			j.appendChild(taskTypeDiv);
			j.appendChild(taskImgDiv);
			j.appendChild(sendMailDiv);
			j.appendChild(q);
			q.appendChild(s);
			var n = j.style;
			n.top = o + "px";
			n.left = f + "px";
			n.zIndex = i;
			p.setClass(j, "module");
			p.setClass(t, "title");
			p.setClass(q, "content");
			j.id = "module" + i;
			t.id = "title" + i;
			q.id = "content" + i;
			taskTemplateIdDiv.id = "taskTemplateId" + i;
			taskTemplateIdDiv.style.display="none";
			taskTemplateNameDiv.id = "taskTemplateName" + i;
			taskTemplateNameDiv.style.display="none";
			resultVariableDiv.id = "resultVariable" + i;
			resultVariableDiv.style.display="none";
			countersignDiv.id = "countersign" + i;
			countersignDiv.style.display="none";
			sequentialDiv.id = "sequential" + i;
			sequentialDiv.style.display="none";
			scriptValDiv.id = "scriptVal" + i;
			scriptValDiv.style.display="none";
			endpointUrlDiv.id = "endpointUrl" + i;
			endpointUrlDiv.style.display="none";
			blockFlagDiv.id = "blockFlag" + i;
			blockFlagDiv.style.display="none";
			payloadExpressionDiv.id = "payloadExpression" + i;
			payloadExpressionDiv.style.display="none";
			queueNameDiv.id = "queueName" + i;
			queueNameDiv.style.display="none";
			taskTypeDiv.id = "taskType" + i;
			taskTypeDiv.style.display="none";
			taskImgDiv.id = "taskImg" + i;
			taskImgDiv.style.display="none";
			sendMailDiv.id = "sendMail" + i;
			sendMailDiv.style.display="none";
			$(sendMailDiv).attr("sendto","");                      //初始化收件人
			$(sendMailDiv).attr("sendtheme","");	               //初始化主题
			$(taskTemplateIdDiv).text(baseModeTaskTemplateId);
			$(taskTemplateNameDiv).text(baseModeTaskTemplateName);
			$(taskTypeDiv).text(baseModeTaskType);
			$(taskImgDiv).text(baseImg);
			$(t).text(baseModeTaskTemplateName);
			$(blockFlagDiv).text("N");//初始化阻塞标志为N，为serviceTask使用
			s.id = "backImg" + i;		
			//当创建的图标类型为TASK的时候，图标宽度设置为100px
			var imgName=a.substring((a.lastIndexOf("/") + 1), a.lastIndexOf("."));
			if(imgName.substring(imgName.length-4)=="Task"||imgName=="subProcess")
			{
				e="100px";
			}
			s.style.width = e;
			s.style.height = l;
			s.src = a;
			$(s).attr("taskType",""+baseModeTaskType);
			var k = u.createElement("div"), d = u.createElement("div"), m = u
					.createElement("div"), c = u.createElement("div"), h = u
					.createElement("div"), b = u.createElement("div"), g = u
					.createElement("div"), r = u.createElement("div");
			p.setClass(k, "top_left");
			p.setClass(d, "top_middle");
			p.setClass(m, "top_right");
			p.setClass(c, "middle_left");
			p.setClass(h, "middle_right");
			p.setClass(b, "bottom_left");
			p.setClass(g, "bottom_middle");
			p.setClass(r, "bottom_right");
			k.id = "top_left" + i;
			d.id = "top_middle" + i;
			m.id = "top_right" + i;
			c.id = "middle_left" + i;
			h.id = "middle_right" + i;
			b.id = "bottom_left" + i;
			g.id = "bottom_middle" + i;
			r.id = "bottom_right" + i;
			j.appendChild(k);
			j.appendChild(d);
			j.appendChild(m);
			j.appendChild(c);
			j.appendChild(h);
			j.appendChild(b);
			j.appendChild(g);
			j.appendChild(r);
			return j;
		},
		//将图元区选中的图标创建到绘图区
		create : function(f, c, j,baseMode) {		
			var taskType=$(baseMode).find('div').attr('taskType');
						
			$("#modelAttr").show();
			$("#lineAttr").hide();
			var taskText=$("#taskText");
			
			BaseTool.prototype.showtaskTemplate(taskType);
				
			var k = this, i = k.getNextIndex(), g = document, h = k.createBaseMode(
					f, c, j, i, "50px", "50px",baseMode);
			k.pathBody.appendChild(h);
			var d = new BaseMode();
			d.id = h.id;			
			var b = com.xjwgraph.Global;
			b.modeMap.put(d.id, d);
			this.initEvent(i);
			b.smallTool.drawMode(h);
			var e = b.modeTool;
			e.flip(i);
			var a = new com.xjwgraph.UndoRedoEvent(function() {
				if ($id(h.id)) {
					b.smallTool.removeMode(h.id);
					e.pathBody.removeChild(h);
					b.modeMap.remove(h.id)
				}
			}, PathGlobal.modeCreate);
			a.setRedo(function() {
				b.modeMap.put(d.id, d);
				e.pathBody.appendChild(h);
				e.showPointer(h);
				e.changeBaseModeAndLine(h, true);
				b.smallTool.drawMode(h)
			})
		},
		initEvent : function(a) {
			var b = com.xjwgraph.Global.modeTool;
			b.drag($id("content" + a));
			b.dragPoint($id("top_left" + a));
			b.dragPoint($id("top_middle" + a));
			b.dragPoint($id("top_right" + a));
			b.dragPoint($id("middle_left" + a));
			b.dragPoint($id("middle_right" + a));
			b.dragPoint($id("bottom_left" + a));
			b.dragPoint($id("bottom_middle" + a));
			b.dragPoint($id("bottom_right" + a));
			$id("content" + a).onclick = function() {
				b.showPointer($id("module" + a))
			}
		},
		clear : function() {
			
			var b = com.xjwgraph.Global, a = b.modeTool;
			this.forEach(function(c) {
				a.hiddPointer($id(c))
			});
			b.smallTool.clear()
		},
		
		forEach : function(d) {
			var a = com.xjwgraph.Global.modeMap.getKeys(), b = a.length;
			for ( var c = b; c--;) {
				if (d) {
					d(a[c]);
				}
			}
			stopEvent = true;
		},
		hiddPointer : function(e) {
			var d = this.getModeIndex(e);
			$id("module" + d).style.visibility = "hidden";
			$id("top_left" + d).style.visibility = "hidden";
			$id("top_middle" + d).style.visibility = "hidden";
			$id("top_right" + d).style.visibility = "hidden";
			$id("middle_left" + d).style.visibility = "hidden";
			$id("middle_right" + d).style.visibility = "hidden";
			$id("bottom_left" + d).style.visibility = "hidden";
			$id("bottom_middle" + d).style.visibility = "hidden";
			$id("bottom_right" + d).style.visibility = "hidden";
			var c = $id("rightMenu");
			c.style.visibility = "hidden";
			PathGlobal.rightMenu = false;
			var a = $id("topCross"), b = $id("leftCross");
			a.style.visibility = "hidden";
			b.style.visibility = "hidden";
			com.xjwgraph.Global.smallTool.clearMode($id("small" + e.id))
		},
		getModeIndex : function(b) {
			var a;
			
			if (b.className == "module") {
				a = 6
			} else {
				if (b.className == "content") {
					a = 7
				}
			}
			return b.id.substr(a)
		},
		showPointer : function(a) {
			this.showPointerId(this.getModeIndex(a))
		},
		showPointerId : function(i) {			
			var d = $id("smallmodule" + i), p = com.xjwgraph.Global;
			if (d) {
				var a = d.style;
				a.borderWidth = "1px";
				a.borderColor = p.smallTool.checkColor;
				a.borderStyle = "solid";
			}
			
			p.controlTool.print(i);
		},
		drag : function(f) {
			var b = f.parentNode, e = b.style, c = com.xjwgraph.Global, a = c.modeTool, d = c.lineTool;
			f.ondragstart = function() {
				return false
			};
			
			
		},
		findModeLine : function(g, b) {
			var a = com.xjwgraph.Global.modeMap.get(g.id), f = a.lineMap, h = f
					.getKeys(), d = h.length;
			for ( var c = d; c--;) {
				var e = f.get(h[c]);
				if (b.id == e.id) {
					return e
				}
			}
			return null
		},
		changeLineType : function(k, f, b) {
			var i = this, j = com.xjwgraph.Global.lineMap.get(k.id), a = j.xBaseMode, h = j.wBaseMode, d, e;
			if (a) {
				d = $id(a.id)
			}
			if (h) {
				e = $id(h.id)
			}
			if (a && a.id == f.id) {
				if (e && d) {
					var c = i.findModeLine(e, k);
					if (e.offsetTop - (d.offsetTop + d.offsetHeight) > 0) {
						b.index = PathGlobal.pointTypeDown;
						c.index = PathGlobal.pointTypeUp
					} else {
						if (d.offsetTop - (e.offsetTop + e.offsetHeight) > 0) {
							b.index = PathGlobal.pointTypeUp;
							c.index = PathGlobal.pointTypeDown
						} else {
							if (d.offsetLeft - (e.offsetLeft + e.offsetWidth) > 0) {
								b.index = PathGlobal.pointTypeLeft;
								c.index = PathGlobal.pointTypeRight
							} else {
								if (e.offsetLeft - (d.offsetLeft + d.offsetWidth) > 0) {
									b.index = PathGlobal.pointTypeRight;
									c.index = PathGlobal.pointTypeLeft
								}
							}
						}
					}
					this.changeBaseModeAndLine(e, false)
				}
			}
			if (h && h.id == f.id) {
				if (e && d) {
					var g = i.findModeLine(d, k);
					if (d.offsetTop - (e.offsetTop + e.offsetHeight) > 0) {
						b.index = PathGlobal.pointTypeDown;
						g.index = PathGlobal.pointTypeUp
					} else {
						if (e.offsetTop - (d.offsetTop + d.offsetHeight) > 0) {
							b.index = PathGlobal.pointTypeUp;
							g.index = PathGlobal.pointTypeDown
						} else {
							if (e.offsetLeft - (d.offsetLeft + d.offsetWidth) > 0) {
								b.index = PathGlobal.pointTypeLeft;
								g.index = PathGlobal.pointTypeRight
							} else {
								if (d.offsetLeft - (e.offsetLeft + e.offsetWidth) > 0) {
									b.index = PathGlobal.pointTypeRight;
									g.index = PathGlobal.pointTypeLeft
								}
							}
						}
					}
					i.changeBaseModeAndLine(d, false)
				}
			}
			return b
		},
		changeBaseModeAndLine : function(j, l) {
			var q = this, o = 0, m = 0, b = com.xjwgraph.Global, g = b.modeMap
					.get(j.id), n = g.lineMap, d = n.getKeys(), a = d.length;
			for ( var e = a; e--;) {
				var c = n.get(d[e]), r = $id(c.id);
				if (r) {
					if (l && PathGlobal.isAutoLineType) {
						q.changeLineType(r, j, c);
					}
					var p = j.offsetWidth, f = j.offsetHeight;					
					if (c.index == PathGlobal.pointTypeUp) {
						o = 0;
						m = p / 2
					} 
					else if (c.index == PathGlobal.pointTypeLeft) {
							o = f / 2;
							m = 0
						} 
					else if (c.index == PathGlobal.pointTypeDown) {
							o = f;
							m = p / 2
						} 
					else if (c.index == PathGlobal.pointTypeRight) {
							o = f / 2;
							m = p
						}
							
					o += parseInt(j.offsetTop);
					m += parseInt(j.offsetLeft);
					var k = b.lineTool;
					k.pathLine(m, o, r, c.type);
					k.setDragPoint(r)
				}
			}
		},
		dragPoint : function(a) {
			var b = com.xjwgraph.Global;
			
		},
		isActiveMode : function(a) {
			return a.style.visibility == "visible"
		},
		getActiveMode : function() {
			var b, a = com.xjwgraph.Global.modeTool;
			
			this.forEach(function(d) {
				var c = $id(d);
				if (a.isActiveMode(c)) {
					b = c
				}
			});
			return b
		},
		getSonNode : function(e, b) {
			for ( var c = e.firstChild; c != null; c = c.nextSibling) {
				if (c.nodeType == 1) {
					var d = c.className;
					if (d == b) {
						return c
					}
					if (d == "content" && b == "backImg") {
						for ( var a = c.firstChild; a != null; a = a.nextSibling) {
							if (a.nodeType == 1) {
								return a
							}
						}
					}
				}
			}
		},
		setIndex : function(e, d) {
			e.id = "module" + d;
			for ( var b = e.firstChild; b != null; b = b.nextSibling) {
				if (b.nodeType == 1) {
					var c = b.className;
					b.id = c + d;
					if (c == "content") {
						for ( var a = b.firstChild; a != null; a = a.nextSibling) {
							if (a.nodeType == 1) {
								a.id = "backImg" + d;
								break
							}
						}
					}
				}
			}
			return e
		},
		copy : function(h) {
			var c = this, b = h.cloneNode(true), g = c.getNextIndex();
			c.setIndex(b, g);
			var e = b.style;
			e.left = parseInt(e.left) + PathGlobal.copyModeDec + "px";
			e.top = parseInt(e.top) + PathGlobal.copyModeDec + "px";
			var a = new BaseMode();
			a.id = b.id;
			var d = com.xjwgraph.Global;
			d.modeMap.put(a.id, a);
			var f = d.lineTool;
			f.pathBody.appendChild(b);
			this.initEvent(g);
			return b
		},
		//图片滑动翻转动画效果
		flip : function(g, a,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal) {
			var c = com.xjwgraph.Global.modeMap.get("module" + g);
			if (c.isFilp) {
				return
			}
			
			c.isFilp = true;
			var f = $id("backImg" + g), e = f.height, d = $id("content" + g), i = d.style;
			c.modeHeigh = e;
			i.width = f.width + "px";
			i.fontSize = (e - parseInt(e * 0.15)) + "px";
			i.lineHeight = e + "px";
			i.height = e + "px";
			var b = $id(c.id), h = b.style;
			h.width = f.width + "px";
			h.height = e + "px";
			c.inc = c.modeHeigh / 10;			
			this.doFlip(g, a,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal);
		},
		doFlip : function(h, d,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal) {

			var b = $id("backImg" + h);
			if (!b) {
				return
			}
			var e = com.xjwgraph.Global, c = b.height, g = e.modeMap.get("module"
					+ h);
			c = c - g.inc;
			if (c < 1) {
				c = 1
			}
			if (c <= 1) {
				g.inc = -g.inc
			} else {
				if (c >= g.modeHeigh) {
					$id("backImg" + h).style.height = g.modeHeigh + "px";
					g.modeHeigh = 0;
					g.isFilp = false;
					g.inc = -g.inc;
					var a = $id("content" + h).style;
					a.width = 0 + "px";
					a.height = 0 + "px";
					a.lineHeight = 0 + "px";
					a.fontSize = 0 + "px";
					var f = $id("title" + h);
					var taskTemplateIdDiv=$id("taskTemplateId"+h);	
					var taskTemplateNameDiv=$id("taskTemplateName"+h);	
					var resultVariableDiv=$id("resultVariable"+h);	
					var countersignDiv=$id("countersign"+h);
					var sequentialDiv=$id("sequential"+h);
					if (d == "undefined" || !d) {
					} else {
						f.innerHTML = d;
					}
					
					
					//判断TaskTemplate值是否存在，
					if (TaskTemplateIdHTML == 'undefined' || !TaskTemplateIdHTML) {
					} else {
						taskTemplateIdDiv.innerHTML = TaskTemplateIdHTML;
					}
					if (TaskTemplateNameHTML == 'undefined' || !TaskTemplateNameHTML) {
					} else {
						taskTemplateNameDiv.innerHTML = TaskTemplateNameHTML;
					}
					if (resultVariableHTML == 'undefined' || !resultVariableHTML) {
					} else {
						resultVariableDiv.innerHTML = resultVariableHTML;
					}
					if (countersignVal == 'undefined' || !countersignVal) {
					} else {
						countersignDiv.innerHTML = countersignVal;
					}
					if (sequentialVal == 'undefined' || !sequentialVal) {
					} else {
						sequentialDiv.innerHTML = sequentialVal;
					}
					this.showPointerId(h);
					return
				} else {
					$id("backImg" + h).style.height = c + "px";
				}
			}
			setTimeout(function() {
				e.modeTool.doFlip(h, d,TaskTemplateIdHTML,TaskTemplateNameHTML,resultVariableHTML,countersignVal,sequentialVal)
			}, PathGlobal.pauseTime)
		},
		isModeCross : function(k) {
			var e = parseInt(k.offsetLeft), c = k.offsetWidth + e, a = parseInt(k.offsetWidth / 2)
					+ e, h = parseInt(k.offsetTop), l = k.offsetHeight + h, g = parseInt(k.offsetHeight / 2)
					+ h, n = $id("leftCross"), w = $id("topCross"), u = n.style, x = w.style, d = com.xjwgraph.Global.modeMap
					.getKeys(), s = false, j = false, m = d.length;
			for ( var t = m; t--;) {
				var o = $id(d[t]);
				if (k.id == o.id) {
					continue
				}
				var f = parseInt(o.offsetLeft), q = o.offsetWidth + f, b = parseInt(o.offsetWidth / 2)
						+ f, p = parseInt(o.offsetTop), r = o.offsetHeight + p, v = parseInt(o.offsetHeight / 2)
						+ p;
				if (e == f || e == q) {
					n.style.left = e;
					j = true;
					n.style.visibility = "visible"
				}
				if (c == f || c == q) {
					u.left = c;
					j = true;
					u.visibility = "visible"
				}
				if (a == f || a == b) {
					u.left = a;
					j = true;
					u.visibility = "visible"
				}
				if (a == q) {
					u.left = a;
					j = true;
					u.visibility = "visible"
				}
				if (e == b || c == b) {
					u.left = b;
					j = true;
					u.visibility = "visible"
				}
				if (h == p || h == r) {
					x.top = h;
					s = true;
					x.visibility = "visible"
				}
				if (h == v || v == g || v == l) {
					x.top = v;
					s = true;
					x.visibility = "visible"
				}
				if (g == p || g == r) {
					x.top = g;
					s = true;
					x.visibility = "visible"
				}
				if (v == g) {
					x.top = v;
					s = true;
					x.visibility = "visible"
				}
				if (l == p || l == r) {
					x.top = l;
					s = true;
					x.visibility = "visible"
				}
			}
			if (!s) {
				x.visibility = "hidden"
			}
			if (!j) {
				u.visibility = "hidden"
			}
		}
	};