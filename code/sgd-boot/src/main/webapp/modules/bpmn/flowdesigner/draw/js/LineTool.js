//线元工具线元工具文件
var LineTool = com.xjwgraph.LineTool = function(d) {
	
	var c = this;
	c.stepIndex = PathGlobal.lineDefStep;
	c.pathBody = d;
	var e = com.xjwgraph.Global;
	c.tool = e.baseTool;
	c.moveable = false;
	c.isSVG = c.tool.isSVG();
	c.isVML = c.tool.isVML();
	c.pathBody.oncontextmenu = function(g) {
		if (!PathGlobal.rightMenu) {
			PathGlobal.rightMenu = true;
			e.baseTool.showMenu(g)
		}
		return false
	};
	c.baseLineIdIndex = PathGlobal.lineDefIndex;
	if (c.isSVG) {
		var f = document;
		c.svgBody = f.createElementNS("http://www.w3.org/2000/svg", "svg");
		c.svgBody.setAttribute("id", "svgContext");
		c.svgBody.setAttribute("style", "position:absolute;z-index:0;");
		c.svgBody.setAttribute("height", this.pathBody.scrollHeight + "px");
		c.svgBody.setAttribute("width", this.pathBody.scrollWidth + "px");
		var b = f.createElementNS("http://www.w3.org/2000/svg", "marker");
		b.setAttribute("id", "arrow");
		b.setAttribute("viewBox", "0 0 18 20");
		b.setAttribute("refX", "15");				
		b.setAttribute("refY", "10");
		b.setAttribute("markerUnits", "strokeWidth");
		b.setAttribute("markerWidth", "5");			//设置SVG箭头的大小数值越大，箭头越大
		b.setAttribute("markerHeight", "10");
		b.setAttribute("orient", "auto");
		var a = f.createElementNS("http://www.w3.org/2000/svg", "path");
		a.setAttribute("d", "M 0 0 L 20 10 L 0 20 z");
		a.setAttribute("fill", PathGlobal.lineColor);
		a.setAttribute("stroke", PathGlobal.lineColor);
		b.appendChild(a);
		
		//增加SVG画线的菱形样式
		var diamond = f.createElementNS("http://www.w3.org/2000/svg", "marker");
		diamond.setAttribute("id", "diamond");
		diamond.setAttribute("viewBox", "0 0 60 40");
		diamond.setAttribute("refX", "0");				
		diamond.setAttribute("refY", "10");
		diamond.setAttribute("markerUnits", "strokeWidth");
		diamond.setAttribute("markerWidth", "25");			
		diamond.setAttribute("markerHeight", "15");
		diamond.setAttribute("orient", "auto");
		var adiamond = f.createElementNS("http://www.w3.org/2000/svg", "path");
		adiamond.setAttribute("d", "M 20 0 L 30 10 L 20 20 L 10 10 z");
		adiamond.setAttribute("fill", "#F8F8FF");
		adiamond.setAttribute("stroke", PathGlobal.lineColor);
		diamond.appendChild(adiamond);
		
		//增加SVG画线的斜线样式
		var slash = f.createElementNS("http://www.w3.org/2000/svg", "marker");
		slash.setAttribute("id", "slash");
		slash.setAttribute("viewBox", "0 0 60 40");
		slash.setAttribute("refX", "0");				
		slash.setAttribute("refY", "10");
		slash.setAttribute("markerUnits", "strokeWidth");
		slash.setAttribute("markerWidth", "25");			
		slash.setAttribute("markerHeight", "15");
		slash.setAttribute("orient", "auto");
		var aslash = f.createElementNS("http://www.w3.org/2000/svg", "path");
		aslash.setAttribute("d", "M 20 0 L 30 20");
		aslash.setAttribute("fill", "#cccccc");
		aslash.setAttribute("stroke", PathGlobal.lineColor);
		aslash.setAttribute("stroke-width", "4");
		slash.appendChild(aslash);
		
		c.svgBody.appendChild(b);
		c.svgBody.appendChild(diamond);              //将菱形样式添加到SVG中
		c.svgBody.appendChild(slash);              //将斜线样式添加到SVG中
		c.pathBody.appendChild(c.svgBody);
		c.pathBody.addEventListener("scroll", function() {
			e.smallTool.initSmallBody();
		}, false)
	} else {
		if (c.isVML) {
			c.pathBody.attachEvent("onscroll", function() {
				e.smallTool.initSmallBody();
			})
		}
	}
};
LineTool.prototype = {
		tempLine : null,
		removeAll : function() {
			var a = this;
			a.forEach(function(b) {
				a.removeNode(b)
			})
		},
		createRect : function(f) {			
			var a, e = document;
			if (this.isSVG) {
				var d = e.createElementNS("http://www.w3.org/2000/svg", "g");
				d.setAttribute("style", "cursor: pointer;");
				var c = e.createElementNS("http://www.w3.org/2000/svg", "rect");
				c.setAttribute("stroke", "black");
				c.setAttribute("id", f);
				c.setAttribute("fill", "#00FF00");
				c.setAttribute("shape-rendering", "crispEdges");
				c.setAttribute("shapeRendering", "crispEdges");
				c.setAttribute("stroke-width", "1");
				c.setAttribute("strokeWidth", "1");
				c.setAttribute("x", "100");
				c.setAttribute("y", "100");
				c.setAttribute("width", "7");
				c.setAttribute("height", "7");
				c.style.visibility = "hidden";
				d.appendChild(c);
				a = d;
			} else {
				if (this.isVML) {
					var c = document.createElement("v:rect");
					c.setAttribute("id", f);
					var b = c.style;
					b.width = "7px";
					b.height = "7px";
					b.position = "absolute";
					b.left = "100px";
					b.top = "100px";
					b.cursor = "pointer";
					b.visibility = "hidden";
					c.fillcolor = "#00FF00";
					c.stroked = "t";
					a = c;
				}
			}
			return a;
		},
		removeNode : function(a) {
			var d = this, c = $id(a);
			var b = null;
			if (d.isVML && c) {
				b = d.pathBody;
//				b.removeChild($id(a + "Title"));
//				b.removeChild($id(a + "Condition"));
				b.removeChild($id(a + "lineHead"));
				b.removeChild($id(a + "lineMiddle"));
				b.removeChild($id(a + "lineEnd"));
			} else {
				if (d.isSVG && c) {
					b = d.svgBody;
					b.removeChild($id(a + "Title"));
					b.removeChild($id(a + "ShowTitle"));
					b.removeChild($id(a + "Condition"));
					b.removeChild($id(a + "LineType"));
					b.removeChild($id(a + "lineHead").parentNode);
					b.removeChild($id(a + "lineMiddle").parentNode);
					b.removeChild($id(a + "lineEnd").parentNode);
				}
			}
			if (c) {
				b.removeChild(c);
				var e = com.xjwgraph.Global;
				e.lineMap.remove(a);
				e.smallTool.removeLine(a)
			}
		},
		formatPath : function(a) {
			if (this.isVML) {
				a = a.replaceAll(",", " "), a = a.replaceAll("e", "z"), a = a
						.replaceAll("l", "L ")
			} else {
				a = a.replaceAll(",NaN NaN", ""), a = a.replaceAll(
						",undefined undefined", "")
			}
			return a
		},
		getNextIndex : function() {
			var a = this;
			a.baseLineIdIndex += a.stepIndex;
			return a.baseLineIdIndex
		},
		getActiveLine : function() {
			var a;
			this.forEach(function(b) {
				var c = $id(b);
				if (com.xjwgraph.Global.lineTool.isActiveMode(c)) {
					activeMode = c;
				}
			});
			return a
		},
		isActiveLine : function(a) {
			var b, c = com.xjwgraph.Global;
			if (c.lineTool.isVML) {
				b = (a.getAttribute("strokecolor") == PathGlobal.lineCheckColor)
			} else {
				if (c.lineTool.isSVG) {
					b = (a.getAttribute("style").indexOf(PathGlobal.lineCheckColor) > 0)
				}
			}
			return b;
		},
		forEach : function(c) {
			var d = com.xjwgraph.Global.lineMap.getKeys(), b = d.length;
			for ( var a = b; a--;) {
				if (c) {
					c(d[a])
				}
			}
		},
		createBaseLine : function(b, h, a) {   //b=lineId h=path a=brokenType
			var e = this, d = null, g = document;
			var c = null;
			if (e.isSVG) {
				d = g.createElementNS("http://www.w3.org/2000/svg", "path");
				d.setAttribute("id", b);
				e.setPath(d, h);
				d.setAttribute("style", "cursor:pointer; fill:none; stroke:"
						+ PathGlobal.lineColor + "; stroke-width:"
						+ +PathGlobal.strokeweight);
				d.setAttribute("stroke", "purple");
				d.setAttribute("marker-end", "url(#arrow)");
				d.setAttribute("brokenType", a);
				
				c = e.svgBody;
				
			} else {
				//设置线的名称和线的条件，SVG要多设置一个ShowTitle来做文字自动换行处理
				if (e.isVML) {
					d = g
							.createElement('<v:shape style="cursor:pointer;WIDTH:100;POSITION:absolute;HEIGHT:100" coordsize="100,100" filled="f" strokeweight="'
									+ PathGlobal.strokeweight
									+ 'px" strokecolor="'
									+ PathGlobal.lineColor + '"></v:shape>');
					var f = g.createElement("<v:stroke EndArrow='classic'/>");					
					d.appendChild(f);					
					var title = g.createElement('<v:rect style="POSITION: absolute;font-size:12;top:10px;word-break:break-all;width:30px;"></v:rect>');	  //word-break:break-all;width:10px; 文字自动换行
					title.setAttribute("id", b+"Title");
					var condition = g.createElement('<v:rect style="display:none;"></v:rect>');
					condition.setAttribute("id", b+"Condition");
					/*设置线的类型开始*/
					var lineType = g.createElement('<v:rect style="display:none;"></v:rect>');
					lineType.setAttribute("id", b+"LineType");
					/*设置线的类型结束*/
					d.appendChild(title);
					d.appendChild(condition);
					d.appendChild(lineType);
					d.setAttribute("id", b);
					d.setAttribute("brokenType", a);
					e.setPath(d, h);
					c = e.pathBody;
				}
			}
			c.appendChild(d);
			if (e.isSVG) {
			var titleText= g.createElementNS("http://www.w3.org/2000/svg", "text");
//			titleText.setAttribute("class", "title");
			titleText.setAttribute("x", "0");
			titleText.setAttribute("y", "0");
			titleText.setAttribute("id", b+"Title");
			titleText.setAttribute("style", "display:none");
			c.appendChild(titleText);
			var titleShowText= g.createElementNS("http://www.w3.org/2000/svg", "text");
			titleShowText.setAttribute("x", "0");
			titleShowText.setAttribute("y", "0");
			titleShowText.setAttribute("id", b+"ShowTitle");
			c.appendChild(titleShowText);
			var conditionText= g.createElementNS("http://www.w3.org/2000/svg", "text");
			conditionText.setAttribute("style", "display:none");
			conditionText.setAttribute("x", "0");
			conditionText.setAttribute("y", "0");
			conditionText.setAttribute("id", b+"Condition");
			conditionText.setAttribute("data_type", "");
			c.appendChild(conditionText);
			/*设置线的类型开始*/
			var lineTypeText= g.createElementNS("http://www.w3.org/2000/svg", "text");
			lineTypeText.setAttribute("x", "0");
			lineTypeText.setAttribute("y", "0");
			lineTypeText.setAttribute("id", b+"LineType");
			lineTypeText.setAttribute("style", "display:none");
			c.appendChild(lineTypeText);
			/*设置线的类型结束*/
			}
			c.appendChild(e.createRect(b + "lineHead"));
			c.appendChild(e.createRect(b + "lineMiddle"));
			c.appendChild(e.createRect(b + "lineEnd"));
			e.drag(d);
			return d;
		},
		setPath : function(a, c) {
			var b = this;
			if (b.isSVG) {
				c = c.replace("z", "");
			}
			a.setAttribute("d", c);
			a.setAttribute("path", c);
			
		},
		getMiddle : function(p, g, l) {
			p = p.replace("M", "");
			p = p.replace("m", "");
			p = p.replace("z", "");
			var o = p.split("L"), n = this, b = n.strTrim(o[1]), f, q, d;
			if (n.isSVG) {
				if (b.indexOf(",") > 0) {
					f = b.split(",");
					var e = n.strTrim(f[0]).split(" "), f = n.strTrim(f[1]).split(
							" "), c = parseInt(e[0]), m = parseInt(e[1]), a = parseInt(f[0]), k = parseInt(f[1]);
					d = [ c, m, a, k ];
					q = [ parseInt(Math.abs(c + a) / 2),
							parseInt(Math.abs(k + m) / 2) ]
				} else {
					var j = n.getLineHead(p), f = n.getLineEnd(p);
					q = [ parseInt(Math.abs(j[0] + f[0]) / 2),
							parseInt(Math.abs(j[1] + f[1]) / 2) ]
				}
			} else {
				if (n.isVML) {
					f = n.strTrim(b).split(" ");
					if (f.length > 2) {
						var c = parseInt(n.strTrim(f[0])), m = parseInt(n
								.strTrim(f[1])), a = parseInt(n.strTrim(f[2])), k = parseInt(n
								.strTrim(f[3]));
						d = [ c, m, a, k ];
						q = [ parseInt(Math.abs(c + a) / 2),
								parseInt(Math.abs(k + m) / 2) ]
					} else {
						var j = n.getLineHead(p), f = n.getLineEnd(p);
						q = [ parseInt(Math.abs(j[0] + f[0]) / 2),
								parseInt(Math.abs(j[1] + f[1]) / 2) ]
					}
				}
			}
			if (l) {
				var i = $id(g + "lineMiddle"), h = i.style;
				i.setAttribute("x", q[0] - PathGlobal.dragPointDec);
				i.setAttribute("y", q[1] - PathGlobal.dragPointDec);
				if (n.isActiveLine($id(g))) {
					h.visibility = ""
				}
				h.left = q[0] - PathGlobal.dragPointDec + "px";
				h.top = q[1] - PathGlobal.dragPointDec + "px"
			}
			return d
		},
		getLineHead : function(d) {
			d = d.replace("M", "");
			d = d.replace("m", "");
			d = d.replace("z", "");
			var e = d.split("L"), a = this.strTrim(e[0]).split(" "), c = parseInt(a[0]), b = parseInt(a[1]);			
			return [ c, b ];
		},
		getLineEnd : function(d) {
			d = d.replace("M", "");
			d = d.replace("m", "");
			d = d.replace("z", "");
			if (this.isSVG) {
				var e = d.split("L"), c = this, b = c.strTrim(e[1]), a;
				if (b.indexOf(",") > 0) {
					a = b.split(",");
					a = c.strTrim(a[a.length - 1]).split(" ");
				} else {
					a = b.split(" ");
					a = [ a[a.length - 2], a[a.length - 1] ];
				}
			} else {
				d = d.replace("L", " ");
				d = d.replace(",", " ");
				d = this.strTrim(d);
				var a = d.split(" ");
				a = [ a[a.length - 2], a[a.length - 1] ];
				return [ parseInt(a[0]), parseInt(a[1]) ];
			}
			var svgWidth=$("#svgContext").width();
			LineTool.prototype.changeSvgContext(parseInt(a[0]), parseInt(a[1]));
			return [ parseInt(a[0]), parseInt(a[1]) ];
		},
		brokenPath : function(i, a) {
			var h = this, g = h.getLineHead(i), c = g[0], f = g[1], d = h
					.getLineEnd(i), e = d[0], b = d[1];
			if (a == 2) {
				i = h.brokenVertical(c, f, e, b, i)
			} else {
				if (a == 3) {
					i = h.brokenCross(c, f, e, b, i)
				}
			}
			return i
		},
		broken : function(f, e, c, b, a, d) {
			if (a == 2) {
				d = this.brokenVertical(f, e, c, b, d)
			} else {
				if (a == 3) {
					d = this.brokenCross(f, e, c, b, d)
				}
			}
			return d
		},
		brokenVertical : function(g, f, b, a, e) {
			var h = this.getPathArray(e), c = h.length;
			if (PathGlobal.switchType || c < 5) {
				var d = a - f, e = "M " + g + " " + f + " L " + (g) + " "
						+ (f + parseInt(d / 2)) + "," + (b) + " "
						+ (a - parseInt(d / 2)) + "," + b + " " + a + " z"
			} else {
				h[0] = g;
				h[1] = f;
				h[2] = g;
				h[4] = b;
				h[5] = h[3];
				h[6] = b;
				h[7] = a;
				e = this.arrayToPath(h)
			}
			return e
		},
		brokenCross : function(g, f, b, a, e) {
			var h = this.getPathArray(e), c = h.length;
			if (PathGlobal.switchType || c < 5) {
				var d = b - g, e = "M " + g + " " + f + " L "
						+ (g + parseInt(d / 2)) + " " + (f) + ","
						+ (g + parseInt(d / 2)) + " " + (a) + "," + b + " " + a
						+ " z"
			} else {
				h[0] = g;
				h[1] = f;
				h[3] = f;
				h[4] = h[2];
				h[5] = a;
				h[6] = b;
				h[7] = a;
				e = this.arrayToPath(h)
			}
			return e
		},
		getPathArray : function(a) {
			a = a.replace("M", ""), a = a.replace("m", ""), a = a.replace("z", ""),
					a = a.replace("L", ""), a = a.replace("  ", " "), a = a
							.replaceAll(",", " ");
			var b = this.strTrim(a).split(" "), b = b.join(","), b = b.replaceAll(
					",,", ",");
			return this.strTrim(b).split(",")
		},
		arrayToPath : function(a) {
			return smallPath = "M " + a[0] + " " + a[1] + "  L " + a[2] + " "
					+ a[3] + "," + a[4] + " " + a[5] + "," + a[6] + " " + a[7]
		},
		create : function(d, g, b) {
			/*创建线的同时将线所用到的input显示，同时隐藏model的input框开始*/
			$("#modelAttr").hide();
			$("#lineAttr").show();
			//$("#lineAttr :input").val("");   
			this.cleanInput();  //将流程线所需要的参数input输入框全部清空
			/*创建线的同时将线所用到的input显示，同时隐藏model的input框结束*/
			var i = this, h = i.getNextIndex(), j = "M " + d + " " + g + " L "
					+ (d + 100) + " " + g + " z";
			if (b != 1) {
				j = "M " + d + " " + g + " L " + (d + 100) + " " + (g + 60) + " z";
				j = i.brokenPath(j, b);
			}
			var k = i.createBaseLine("line" + h, j, b), e = new BuildLine();
			e.id = "line" + h;
			var c = com.xjwgraph.Global;
			c.lineMap.put(e.id, e);
			var f = c.smallTool, a = new com.xjwgraph.UndoRedoEvent(function() {
				var n = k && k.id && $id(k.id);
				var m = null, l = k.getAttribute("id");
				if (i.isVML && n) {
					m = i.pathBody;
					m.removeChild($id(l + "Title"));
					m.removeChild($id(l + "Condition"));
					m.removeChild($id(l + "LineType"));
					m.removeChild($id(l + "lineHead"));
					m.removeChild($id(l + "lineMiddle"));
					m.removeChild($id(l + "lineEnd"));
				} else {
					if (i.isSVG && n) {
						m = i.svgBody;
						m.removeChild($id(l + "Title"));
						m.removeChild($id(l + "ShowTitle"));
						m.removeChild($id(l + "Condition"));
						m.removeChild($id(l + "LineType"));
						m.removeChild($id(l + "lineHead").parentNode);
						m.removeChild($id(l + "lineMiddle").parentNode);
						m.removeChild($id(l + "lineEnd").parentNode)
					}
				}
				if (n) {
					m.removeChild(k);
				}
				c.lineMap.remove(e.id);
				if (n) {
					f.removeLine(e.id)
				}
			}, PathGlobal.lineCreate);
			a.setRedo(function() {
				var m = null, l = k.getAttribute("id");
				if (i.isVML) {
					k.setAttribute("filled", "f");
					k.setAttribute("strokeweight", PathGlobal.strokeweight + "px");
					k.setAttribute("strokecolor", PathGlobal.lineColor);
					m = i.pathBody;
				} else {
					if (i.isSVG) {
						m = i.svgBody;
					}
				}
				m.appendChild(k);
				m.appendChild(i.createRect(l + "lineHead"));
				m.appendChild(i.createRect(l + "lineMiddle"));
				m.appendChild(i.createRect(l + "lineEnd"));
				i.drag(k);
				c.lineMap.put(k.id, e);
				f.drawLine(k)
			})
		},
		getPath : function(a) {
			var b = this, c = "";
			if (b.isSVG) {
				c = a.getAttribute("d");
			} else {
//				c = a.path + ""
				c = a.getAttribute("d");
			}
			c = b.formatPath(c);
			return c
		},
		distancePoint : function(j, g, m) {
			var k = this, l = this.getPath(m), h = k.getLineHead(l), c = k
					.getLineEnd(l), b = parseInt(h[0]), i = parseInt(h[1]), a = parseInt(c[0]), f = parseInt(c[1]), e = Math
					.abs(Math.sqrt(Math.pow(b - j, 2) + Math.pow(i - g, 2))), d = Math
					.abs(Math.sqrt(Math.pow(a - j, 2) + Math.pow(f - g, 2)));
			return e <= d
		},
		strTrim : function(b) {
			b = b.replace(/^\s+/, "");
			for ( var a = b.length - 1; a >= 0; a--) {
				if (/\S/.test(b.charAt(a))) {
					b = b.substring(0, a + 1);
					break
				}
			}
			return b
		},
		initScaling : function(a) {
			var b = this, c = com.xjwgraph.Global.smallTool;
			b.forEach(function(d) {
				var e = $id(d), h = b.formatPath(b.getPath(e)), j = b
						.getPathArray(h), g = j.length;
				for ( var f = g; f--;) {
					j[f] = parseInt(j[f] / a)
				}
				h = b.arrayToPath(j);
				b.setPath(e, h);
				b.setDragPoint(e);
				c.drawLine(e)
			})
		},
		getEndPoint : function(a) {
			var b = a.split("L");
			b[1] = this.strTrim(b[1]);
			return b[1].split(" ");
		},
		getHeadPoint : function(a) {
			var b = a.split("L");
			b[0] = this.strTrim(b[0]);
			return b[0].split(" ");
		},
		endPoint : function(b, h, f, a) {
			var c = this, e;
			if (a != 1) {
				var d = c.getLineHead(f);
				e = c.broken(parseInt(d[0]), parseInt(d[1]), b, h, a, f)
			} else {
				var g = f.split("L");
				g[0] = this.strTrim(g[0]);
				e = g[0] + " L " + b + " " + h + " z";
			}
			return e
		},
		headPoint : function(b, h, f, a) {
			var d = this, e;
			if (a != 1) {
				var c = d.getLineEnd(f);
				e = d.broken(b, h, parseInt(c[0]), parseInt(c[1]), a, f)
			} else {
				var g = f.split("L");
				g[1] = this.strTrim(g[1]);
				e = "M " + b + " " + h + " L " + g[1]
			}
			return e;
		},
		vecMultiply : function(c, b, a) {
			return ((c.x - a.x) * (b.y - a.y) - (b.x - a.x) * (c.y - a.y))
		},
		poInTrigon : function(h, g, e, f) {
			var b = this, d = b.vecMultiply(h, f, g), c = b.vecMultiply(g, f, e), a = b
					.vecMultiply(e, f, h);
			if (d * c * a == 0) {
				return false
			}
			if ((d > 0 && c > 0 && a > 0) || (d < 0 && c < 0 && a < 0)) {
				return true
			}
			return false
		},
		buildModeAndPoint : function(m, c, e, d) {
			var f = c.offsetWidth, b = c.offsetHeight, g = new Point(), a = new Point();
			a.x = parseInt(c.offsetLeft);
			a.y = parseInt(c.offsetTop);
			var l = new Point();
			l.x = parseInt(c.offsetLeft) + parseInt(f);
			l.y = parseInt(c.offsetTop);
			var k = new Point();
			k.x = parseInt(c.offsetLeft) + parseInt(f) / 2;
			k.y = parseInt(c.offsetTop) + parseInt(b) / 2;
			var i = new Point();
			i.x = e;
			i.y = d;
			var j = this;
			if (j.poInTrigon(a, l, k, i)) {
				g.x = "0px";
				g.y = f / 2 + "px";
				g.index = PathGlobal.pointTypeUp
			}
			l.x = parseInt(c.offsetLeft);
			l.y = parseInt(c.offsetTop) + parseInt(b);
			if (j.poInTrigon(a, l, k, i)) {
				g.x = b / 2 + "px";
				g.y = "0px";
				g.index = PathGlobal.pointTypeLeft
			}
			a.x = parseInt(c.offsetLeft) + parseInt(f);
			a.y = parseInt(c.offsetTop) + parseInt(b);
			if (j.poInTrigon(a, l, k, i)) {
				g.x = b + "px";
				g.y = f / 2 + "px";
				g.index = PathGlobal.pointTypeDown
			}
			l.x = parseInt(c.offsetLeft) + parseInt(f);
			l.y = parseInt(c.offsetTop);
			if (j.poInTrigon(a, l, k, i)) {
				g.x = b / 2 + "px";
				g.y = f + "px";
				g.index = PathGlobal.pointTypeRight
			}
			g.x = parseInt(c.offsetTop) + parseInt(g.x);
			g.y = parseInt(c.offsetLeft) + parseInt(g.y);
			return g;
		},
		buildLineAndMode : function(l, g, i, h, d) {
			var k = this, j = k.buildModeAndPoint(l, g, i, h), c = new BuildLine();
			c.index = j.index;
			c.id = l.id;
			k.pathLine(j.y, j.x, l, d);
			k.setDragPoint(l);
			var b = com.xjwgraph.Global, f = b.modeMap.get(g.id);
			c.type = d;
			f.lineMap.put(c.id + "-" + c.type, c);
			var e = b.lineMap.get(l.id);
			if (d) {
				e.xBaseMode = f;
				e.xIndex = j.index
			} else {
				e.wBaseMode = f;
				e.wIndex = j.index
			}
			b.lineMap.put(l.id, e);
			var a = new com.xjwgraph.UndoRedoEvent(function() {
				if (d) {
					e.xBaseMode = null
				} else {
					e.wBaseMode = null
				}
				f.lineMap.remove(c.id + "-" + c.type);
				k.setDragPoint(l)
			}, PathGlobal.buildLineAndMode);
			a.setRedo(function() {
				if (d) {
					e.xBaseMode = f
				} else {
					e.wBaseMode = f
				}
				f.lineMap.put(c.id + "-" + c.type, c);
				k.setDragPoint(l)
			})
		},
		removeAllLineAndMode : function(a, b) {
			this.removeBuildLineAndMode(a, b, true);
			this.removeBuildLineAndMode(a, b, false)
		},
		removeBuildLineAndMode : function(m, h, d) {
			var b = com.xjwgraph.Global, g = b.modeMap.get(h.id), j = g.lineMap, k = m.id
					+ "-" + d;
			if (j.containsKey(k)) {
				var l = g.lineMap, f = l.get(k), c = null, i = null;
				l.remove(k);
				var e = b.lineMap.get(m.id);
				if (e && e.xBaseMode && e.xBaseMode.id == h.id) {
					c = e.xBaseMode, e.xBaseMode = null
				} else {
					if (e && e.wBaseMode && e.wBaseMode.id == h.id) {
						e.wBaseMode = null;
						i = e.wBaseMode
					}
				}
				var a = new com.xjwgraph.UndoRedoEvent(function() {
					l.put(k, f);
					e.xBaseMode = c;
					e.wBaseMode = i
				}, PathGlobal.removeLineAndMode);
				a.setRedo(function() {
					l.remove(k);
					e.xBaseMode = null;
					e.wBaseMode = null
				})
			}
		},
		isMoveBaseMode : function(n, l, p, d) {
			var a = com.xjwgraph.Global, m = a.modeMap.getKeys(), c = m.length, h = a.modeTool;
			for ( var f = c; f--;) {
				var g = $id(m[f]), e = g.style, b = parseInt(e.left), j = g.offsetWidth
						+ b, k = parseInt(e.top), o = g.offsetHeight + k;
				if (n > b && n < j && l > k && l < o) {
					h.hiddPointer(g);
					h.flip(a.modeTool.getModeIndex(g));
					break
				} else {
					h.hiddPointer(g)
				}
			}
		},
		isCoverBaseMode : function(n, l, r, d) {
			var a = com.xjwgraph.Global, m = a.modeMap.getKeys(), c = m.length, h = a.modeTool, p = this, q = a.modeTool
					.getActiveMode();
			if (q) {
				h.hiddPointer(q);
				h.flip(a.modeTool.getModeIndex(q));
				p.buildLineAndMode(r, q, n, l, d)
			}
			for ( var f = c; f--;) {
				var g = $id(m[f]), e = g.style, b = parseInt(e.left), j = g.offsetWidth
						+ b, k = parseInt(e.top), o = g.offsetHeight + k;
				if (q && q.id == g.id) {
					continue
				} else {
					h.hiddPointer(g);
					p.removeBuildLineAndMode(r, g, d)
				}
			}
		},
		pathLine : function(e, f, b, d) {
			var c = this, h = c.getPath(b), g, a = b.getAttribute("brokenType");
			if (d) {
				g = c.headPoint(parseInt(e), parseInt(f), h, a)
			} else {
				g = c.endPoint(parseInt(e), parseInt(f), h, a)
			}
			c.setPath(b, g);
			var lineId=b.id;
			//当线发生拖拽的时候修改线上title的坐标，已便属性可以和字一起移动
			if(c.isSVG){
				var titleText=$("#"+lineId+"Title");
				var titleShowText=$("#"+lineId+"ShowTitle");
				var lineMiddle=$("#"+lineId+"lineMiddle");
				var lineHead=$("#"+lineId+"lineHead");
				var lineEnd=$("#"+lineId+"lineEnd");
				var xHead=$("#"+lineId+"lineHead").attr("x");
				var xMiddle=$("#"+lineId+"lineMiddle").attr("x");
				var x=(xMiddle-xHead)/2;
				var dx=0;
				if(xMiddle<xHead){
						dx=parseInt(xMiddle)+parseInt(x);
					}
					else{
						 dx=parseInt(xMiddle)-parseInt(x);
					}
					
				LineTool.prototype.create_tspan(lineId);
				$(titleText).attr("x",$(lineMiddle).attr("x"))
				$(titleText).attr("y",parseInt($(lineMiddle).attr("y"))+20);
				$(titleShowText).attr("x",dx);
				$(titleShowText).attr("y",parseInt($(lineMiddle).attr("y"))+20);
			}else if(c.isVML){
				var titleText=$("#"+lineId+"Title");
				var lineHeadLeft=$("#"+lineId+"lineHead").css("left");
				var lineEndLeft=$("#"+lineId+"lineEnd").css("left");
				var left=(Math.abs(parseInt(lineHeadLeft)-parseInt(lineEndLeft)))/4;
				$(titleText).css("left",left);
				$(titleText).css("width",left*2);
			}
			com.xjwgraph.Global.smallTool.drawLine(b)
		},
		clearLine : function(a) {
			var b = $id(a), e = com.xjwgraph.Global, g = e.lineTool;
			if (g.isVML) {
				b.setAttribute("strokecolor", PathGlobal.lineColor)
			} else {
				if (e.lineTool.isSVG) {
					b.setAttribute("style", "cursor:pointer; fill:none; stroke:"
							+ PathGlobal.lineColor + "; stroke-width:2")
				}
			}
			var d = $id(a + "lineHead"), c = $id(a + "lineEnd"), f = $id(a
					+ "lineMiddle");
			d.style.visibility = "hidden";
			c.style.visibility = "hidden";
			f.style.visibility = "hidden";
		},
		clear : function() {
			var b = com.xjwgraph.Global, c = b.lineTool;
			this.forEach(function(d) {
				c.clearLine(d)
			});
			PathGlobal.rightMenu = false;
			var a = $id("lineRightMenu");
			a.style.visibility = "hidden";
			c.tempLine = null;
			b.smallTool.clear()
		},
		setDragPoint : function(q) {
			var h = com.xjwgraph.Global.lineTool, p = h.formatPath(h.getPath(q)), g = q
					.getAttribute("id"), i = $id(g + "lineHead"), e = $id(g
					+ "lineEnd"), l = $id(g + "lineMiddle"), m = h.getLineHead(p), d = h
					.getLineEnd(p), o = h.getMiddle(p, g, true), k = parseInt(m[0]), c = parseInt(m[1]), b = parseInt(d[0]), n = parseInt(d[1]), f = q.style;
			i.setAttribute("x", (b - PathGlobal.dragPointDec));
			i.setAttribute("y", (n - PathGlobal.dragPointDec));
			var j = i.style;
			j.left = (b - PathGlobal.dragPointDec) + "px";
			j.top = (n - PathGlobal.dragPointDec) + "px";
			e.setAttribute("x", (k - PathGlobal.dragPointDec));
			e.setAttribute("y", (c - PathGlobal.dragPointDec));
			var a = e.style;
			a.left = (k - PathGlobal.dragPointDec) + "px";
			a.top = (c - PathGlobal.dragPointDec) + "px";
			j.zIndex = 1;
			l.style.zIndex = 1;
			a.zIndex = 1;
			if (h.isActiveLine(q)) {
				j.visibility = "";
				a.visibility = ""
			}
		},
		showProperty : function(b) {
			var i = this.tempLine, c = com.xjwgraph.Global, d = c.lineMap, f = d
					.get(i.getAttribute("id")), a = f.prop, h = $id("prop"), g = document, e = c.clientTool;
			h.style.visibility = "";
			h.innerHTML = "";
			h.appendChild(e.addProItem(a));
			e.showDialog(b, PathGlobal.lineProTitle, f)
		},
		showMenu : function(a, j) {
			PathGlobal.rightMenu = true;
			var i = this;
			i.tempLine = j;
			a = a || window.event;
			if (!a.pageX) {
				a.pageX = a.clientX
			}
			if (!a.pageY) {
				a.pageY = a.clientY
			}
			var f = a.pageX, e = a.pageY, c = com.xjwgraph.Global, b = c.lineTool.pathBody, g = c.baseTool
					.sumLeftTop(b);
			f = f - parseInt(g[0]) + parseInt(b.scrollLeft);
			e = e - parseInt(g[1]) + parseInt(b.scrollTop);
			var d = $id("lineRightMenu"), h = d.style;
			h.top = e + "px";
			h.left = f + "px";
			h.visibility = "visible";
			h.zIndex = i.getNextIndex();
		},
		showId : function(a) {
			this.show($id(a))
		},
		show : function(a) {
			if (this.isVML) {
				a.setAttribute("strokecolor", PathGlobal.lineCheckColor)
			} else {
				if (this.isSVG) {
					a.setAttribute("style", "cursor:pointer;fill:none; stroke:"
							+ PathGlobal.lineCheckColor + "; stroke-width:"
							+ PathGlobal.strokeweight)
				}
			}
			this.setDragPoint(a)
		},
		drag : function(i) {
			var b = com.xjwgraph.Global, d = i.getAttribute("id"), f = $id(d
					+ "lineHead"), c = $id(d + "lineEnd"), g = $id(d + "lineMiddle"), e = b.lineTool, a = e.pathBody, h = this;
			b.smallTool.drawLine(i);
			i.ondragstart = function() {
				return false
			};
			g.oncontextmenu = c.oncontextmenu = f.oncontextmenu = i.oncontextmenu = function(
					j) {
				e.showMenu(j, i);
				return false
			};
			c.onmousedown = f.onmousedown = i.onmousedown = function(k) {   //流程线点击事件
				k = k || window.event;
				b.modeTool.clear();
				b.lineTool.clear();
				e.moveable = true;
				var p = b.smallTool, j = e.getPath(i), r = k.clientX ? k.clientX
						: k.offsetX, o = k.clientY ? k.clientY : k.offsetY, q = b.baseTool
						.sumLeftTop(a);
				r = r - parseInt(q[0]) + parseInt(a.scrollLeft);
				o = o - parseInt(q[1]) + parseInt(a.scrollTop);
				/*选中线的同时将线所用到的input显示，同时隐藏model的input框开始*/
				$("#modelAttr").hide();
				$("#lineAttr").show();
				/*选中线的同时将线所用到的input显示，同时隐藏model的input框结束*/
				$("#lineId").val(d);
				var titleText=$("#"+d+"Title");
				$("#lineTitle").val($(titleText).text());
				/*获取流程线的类型，标准(normal)，默认(default)*/
//				var LineType=$("#"+d+"LineType").text(lineTypeText);	
//				if(LineType=="default"){
//					$("#conditionExpression").hide();
//					$("#serviceExpression").hide();
//				}
				var ConditionText=$("#"+d+"Condition");
				var data_type=$("#"+d+"Condition").attr("data_type");
				if(data_type=="serviceExpression"){
						$("#conditionExpression").hide();
						$("#serviceExpression").show();
						$("#serviceName").val($(ConditionText).text()); 
				}else{
					$("#serviceExpression").hide();
					$("#conditionExpression").show();					
					$("#lineCondition").val($(ConditionText).text());
				}
				/*将线的属性映射到界面中*/
				var LineTypeText=$("#"+d+"LineType").text();
				$("#lineType").val(LineTypeText);
				if(LineTypeText== "" ||LineTypeText=="normal"){		
					$(":radio[name='lineType'][value='normal']").attr("checked", true);
					$(":radio[name='lineType'][value='default']").attr("checked", false);					
				}else{
					$("#conditionExpression").hide();
					$("#serviceExpression").hide();
					$(":radio[name='lineType'][value='normal']").attr("checked", false);
					$(":radio[name='lineType'][value='default']").attr("checked", true);
				}
				
				if (!k.pageX) {
					k.pageX = k.clientX
				}
				if (!k.pageY) {
					k.pageY = k.clientY
				}
				var n = k.clientX - r, m = k.clientY - o, l = e.distancePoint(r, o,
						i), s = document;
				h.show(i);
				s.onmousemove = function(v) {
					v = v || window.event;
					if (e.moveable) {
						var t = v.clientX ? v.clientX : v.offsetX;
						var u = v.clientY ? v.clientY : v.offsetY;
						var w = b.baseTool.sumLeftTop(a);
						t = t - parseInt(w[0]) + parseInt(a.scrollLeft);
						u = u - parseInt(w[1]) + parseInt(a.scrollTop);
						h.pathLine(t, u, i, l);
						h.setDragPoint(i)
					}					
				};
				s.onmouseup = function(G) {
					G = G || window.event;					
					var I = G.clientX ? G.clientX : G.offsetX, x = G.clientY ? G.clientY
							: G.offsetY;
					var A = b.baseTool.sumLeftTop(a);
					I = I - parseInt(A[0]) + parseInt(a.scrollLeft);
					x = x - parseInt(A[1]) + parseInt(a.scrollTop);
					e.moveable = false;
					s.onmousemove = null;
					s.onmouseup = null;
					var u = e.getPath(i);
					if (j != u) {
						var y = e.getLineHead(j), C = e.getLineEnd(j), z = parseInt(y[0]), H = parseInt(y[1]), F = parseInt(C[0]), w = parseInt(C[1]), D = e
								.getLineHead(u), t = e.getLineEnd(u), E = parseInt(D[0]), v = parseInt(D[1]), B = parseInt(t[0]), K = parseInt(t[1]);
						if (z !== E || H !== v || F !== B || w !== K) {
							e.isCoverBaseMode(I, x, i, l)
						}
						var J = new com.xjwgraph.UndoRedoEvent(function() {
							b.lineTool.setPath(i, j);
							p.drawLine(i);
							e.setDragPoint(i)
						}, PathGlobal.lineMove);
						J.setRedo(function() {
							b.lineTool.setPath(i, u);
							p.drawLine(i);
							e.setDragPoint(i)
						})
					}
				}
			};
			g.onmousedown = function(j) {
				c.onmousedown(j);
				document.onmousemove = function(l) {
					l = l || window.event;
					if (e.moveable) {
						var m = l.clientX ? l.clientX : l.offsetX;
						var s = l.clientY ? l.clientY : l.offsetY;
						var r = b.baseTool.sumLeftTop(a);
						m = m - parseInt(r[0]) + parseInt(a.scrollLeft);
						s = s - parseInt(r[1]) + parseInt(a.scrollTop);
						var u = e.formatPath(e.getPath(i));
						var q = e.getLineHead(u), n = e.getLineEnd(u), p = $id(d
								+ "lineMiddle"), o = b.smallTool;
						o.drawLine(i);
						var k = i.getAttribute("brokenType");
						h.changeBrokenType(i, m, s);
						if (k == 3) {
							var t = "M " + q[0] + " " + q[1] + " L " + m + " "
									+ q[1] + "," + m + " " + n[1] + "," + n[0]
									+ " " + n[1] + " z";
							e.setPath(i, t);
							p.setAttribute("x", m - PathGlobal.dragPointDec);
							p.style.left = m - PathGlobal.dragPointDec + "px"
						} else {
							if (k == 2) {
								var t = "M " + q[0] + " " + q[1] + " L " + q[0]
										+ " " + s + "," + n[0] + " " + s + ","
										+ n[0] + " " + n[1] + " z";
								e.setPath(i, t);
								p.setAttribute("y", s - PathGlobal.dragPointDec);
								p.style.top = s - PathGlobal.dragPointDec + "px"
							}
						}
						e.setDragPoint(i)
					}
				}
			}
		},
		changeBrokenType : function(k, c, i) {
			var j = this.getPath(k), b = k.getAttribute("brokenType"), h = this
					.getLineHead(j), e = this.getLineEnd(j), d = (parseInt(c) > parseInt(h[0])
					&& parseInt(c) < parseInt(e[0]) || parseInt(c) < parseInt(h[0])
					&& parseInt(c) > parseInt(e[0])), a = (parseInt(i) > parseInt(h[1])
					&& parseInt(i) < parseInt(e[1]) || parseInt(i) < parseInt(h[1])
					&& parseInt(i) > parseInt(e[1])), g = (parseInt(c) < parseInt(h[0])
					&& parseInt(c) < parseInt(e[0]) || parseInt(c) > parseInt(h[0])
					&& parseInt(c) > parseInt(e[0]))
					&& a, f = (parseInt(i) < parseInt(h[1])
					&& parseInt(i) < parseInt(e[1]) || parseInt(i) > parseInt(h[1])
					&& parseInt(i) > parseInt(e[1]))
					&& d;
			if (g) {
				k.setAttribute("brokenType", 3)
			} else {
				if (f) {
					k.setAttribute("brokenType", 2)
				}
			}
		},
		changeSvgContext : function(width,height){
			if(BaseTool.prototype.isSVG()){
				/*jquery1.6不支持$('#svgContext').width()和$('#svgContext').css("width")；所以只能用如下方法去实现获取SVG的宽度和高度*/
				var svgWidth=$('#svgContext').attr("width").substring(0,$('#svgContext').attr("width").indexOf("p"));
				var svgHeight=$('#svgContext').attr("height").substring(0,$('#svgContext').attr("height").indexOf("p"));
				if(width>svgWidth){
					$("#svgContext").attr("width",width+"px");
				}
				if(height>svgHeight){
					$("#svgContext").attr("height",height+"px");
				}				
			}
		},
		create_tspan : function(lineId) {
			var xHead=$("#"+lineId+"lineHead").attr("x");
			var xMiddle=$("#"+lineId+"lineMiddle").attr("x");
			var x=$("#"+lineId+"ShowTitle").attr("x");
			var width = Math.abs(parseInt(xHead)-parseInt(xMiddle));
			if(parseInt(width)<30){
						width=30;
				}				
			$("#"+lineId+"ShowTitle tspan").remove();          //删除原有的tspan

			var text = $("#"+lineId+"Title").text();
			if(text==""){
				  text=" ";
				}
  	        var words = text.split('');     
	    //    var words = text.split(' ');          //英文字段加空格           
  	        var text_element =document.getElementById(lineId+"ShowTitle");
	       	 	var tspan_element = document.createElementNS("http://www.w3.org/2000/svg", "tspan");   // Create first tspan element
	       		var text_node = document.createTextNode(words[0]);           // Create text in tspan element

	        	tspan_element.appendChild(text_node);                           // Add tspan element to DOM
	        	text_element.appendChild(tspan_element);                        // Add text to tspan element

	        	for(var i=1; i<words.length; i++)
	        		{
	            var len = tspan_element.firstChild.data.length;             // Find number of letters in string
	            tspan_element.firstChild.data += "" + words[i];            // Add next word
					//		tspan_element.firstChild.data += " " + words[i];       //英文字段加空格
	            if (tspan_element.getComputedTextLength() > width)
	            	{
	                tspan_element.firstChild.data = tspan_element.firstChild.data.slice(0, len);    // Remove added word
	                var tspan_element = document.createElementNS("http://www.w3.org/2000/svg", "tspan");       // Create new tspan element
	                tspan_element.setAttributeNS(null, "x", x);
	                tspan_element.setAttributeNS(null, "dy", 18);
	                text_node = document.createTextNode(words[i]);
	                tspan_element.appendChild(text_node);
	                text_element.appendChild(tspan_element);
	            	}
	       			}


	    },
	    lineTypeSet : function(lineId) {
	    	 if(BaseTool.prototype.isSVG()){
	    		var lineTypeText=$("#"+lineId+"LineType").text();
	    		if(lineTypeText=="normal"){
	    			$("#"+lineId).attr("marker-start","");
	    		}else if(lineTypeText=="default"){
	    			$("#"+lineId).attr("marker-start","url(#slash)");
	    		}
	    	 }
	    },
	    cleanInput:function(){
	    	$("#lineTitle").val("");
	    	$("#lineId").val("");
	    	$("#lineCondition").val("");
	    	$("#serviceName").val("");
	    	$(":radio[name='lineType'][value='normal']").attr("checked", true);
			$(":radio[name='lineType'][value='default']").attr("checked", false);
	    }
	};