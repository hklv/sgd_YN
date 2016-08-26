var SmallMapTool = com.xjwgraph.SmallMapTool = function(c, a) {
	var b = this;
	b.smallMap = $id(c);
	b.body = $id(a);
	b.multiple = b.getMultiple();
	b.defaultColor = PathGlobal.defaultColor;
	b.checkColor = "#00ff00";
	b.widthPercent = b.smallMap.offsetWidth / parseInt(b.body.scrollWidth)
			/ b.multiple;
	b.heightPercent = b.smallMap.offsetHeight / parseInt(b.body.scrollHeight)
			/ b.multiple;
	b.percent = 0;
	b.initPercent();
	b.smallModeMap = new Map();
	b.smallLineMap = new Map();
	var d = document;
	if (com.xjwgraph.Global.lineTool.isSVG) {
		b.svgBody = d.createElementNS("http://www.w3.org/2000/svg", "svg");
		b.svgBody.setAttribute("id", "smallSvgContext");
		b.svgBody.setAttribute("height", "100%");
		b.svgBody.setAttribute("width", "100%");
		b.svgBody.setAttribute("style", "position:absolute;z-index:0;");
		b.smallMap.appendChild(b.svgBody)
	}
	b.smallBody = d.createElement("div");
	b.smallBody.id = "smallBodyId";
	var e = b.smallBody.style;
	e.fontSize = "0px";
	e.borderWidth = "2px";
	e.borderColor = "#0F0";
	e.borderStyle = "solid";
	e.position = "absolute";
	e.cursor = "pointer";
	b.drag(b.smallBody);
	b.smallMap.appendChild(b.smallBody);
	b.scalingDiv = d.createElement("div");
	b.scalingDiv.id = "scalingId";
	b.scalingDiv.setAttribute("class", "scaling");
	b.scalingDiv.setAttribute("className", "scaling");
	b.dragScaling(b.scalingDiv);
	b.smallMap.appendChild(b.scalingDiv);
	b.initSmallBody()
};
SmallMapTool.prototype = {
	dragScaling : function(b) {
		var a = com.xjwgraph.Global;
		b.ondragstart = function() {
			return false
		};
		b.onmousedown = function(c) {
			var l = 1, h = a.smallTool, e = b.style, d = h.smallBody.style, f = d.width, k = d.height;
			c = c || window.event;
			if (b.setCapture) {
				b.setCapture()
			} else {
				if (window.captureEvents) {
					window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
				}
			}
			var i = c.layerX ? c.layerX : c.offsetX, g = c.layerY ? c.layerY
					: c.offsetY, j = document;
			j.onmousemove = function(s) {
				s = s || window.event;
				if (!s.pageX) {
					s.pageX = s.clientX
				}
				if (!s.pageY) {
					s.pageY = s.clientY
				}
				var o = s.pageX - i, m = s.pageY - g, q = h.smallMap, p = a.baseTool
						.sumLeftTop(q);
				o = o - parseInt(p[0]);
				m = m - parseInt(p[1]);
				if (a.baseTool.isIE) {
					o = o - 4;
					m = m - 4
				}
				e.left = o + "px";
				e.top = m + "px";
				var r = o + 1 - parseInt(d.left), n = m + 1 - parseInt(d.top);
				if (r < 1) {
					r = 1
				}
				if (n < 1) {
					n = 1
				}
				d.width = r + "px";
				d.height = n + "px";
				l = parseInt(d.width) / parseInt(f);
				if (l < PathGlobal.defaultMaxMag) {
					l = PathGlobal.defaultMaxMag
				} else {
					if (l > PathGlobal.defaultMinMag) {
						l = PathGlobal.defaultMinMag
					}
				}
				a.undoRedoEventFactory.clear()
			};
			j.onmouseup = function(m) {
				m = m || window.event;
				if (b.releaseCapture) {
					b.releaseCapture()
				} else {
					if (window.releaseEvents) {
						window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
					}
				}
				a.lineTool.initScaling(l);
				a.modeTool.initScaling(l);
				a.baseTool.initScaling(l);
				h.initSmallBody();
				j.onmousemove = null;
				j.onmouseup = null
			}
		}
	},
	drag : function(a) {
		var b = com.xjwgraph.Global;
		a.ondragstart = function() {
			return false
		};
		a.onmousedown = function(d) {
			d = d || window.event;
			if (a.setCapture) {
				a.setCapture()
			} else {
				if (window.captureEvents) {
					window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
				}
			}
			var c = d.layerX ? d.layerX : d.offsetX, f = d.layerY ? d.layerY
					: d.offsetY, e = document;
			e.onmousemove = function(m) {
				m = m || window.event;
				if (!m.pageX) {
					m.pageX = m.clientX
				}
				if (!m.pageY) {
					m.pageY = m.clientY
				}
				var h = m.pageX - c, g = m.pageY - f, l = b.smallTool, k = l.smallMap, j = b.baseTool
						.sumLeftTop(k);
				h = h - parseInt(j[0]);
				g = g - parseInt(j[1]);
				if (h < 0) {
					h = 0
				}
				if (g < 0) {
					g = 0
				}
				a.style.left = h + "px";
				a.style.top = g + "px";
				var n = b.lineTool, l = b.smallTool, i = $id(n.pathBody.id);
				i.scrollLeft = parseInt(h / l.widthPercent);
				i.scrollTop = parseInt(g / l.heightPercent);
				l.initSmallBody()
			};
			e.onmouseup = function(g) {
				g = g || window.event;
				if (a.releaseCapture) {
					a.releaseCapture()
				} else {
					if (window.releaseEvents) {
						window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
					}
				}
				e.onmousemove = null;
				e.onmouseup = null
			}
		}
	},
	initSmallBody : function() {
		var a = this, e = a.smallBody.style;		
		e.left = a.body.scrollLeft * a.widthPercent + "px";
		e.top = a.body.scrollTop * a.heightPercent + "px";
		e.width = a.body.offsetWidth * a.widthPercent + "px";
		e.height = a.body.offsetHeight * a.heightPercent + "px";
		var b = a.scalingDiv.style, d = parseInt(e.left) + parseInt(e.width), c = parseInt(e.top)
				+ parseInt(e.height);		
		if (com.xjwgraph.Global.baseTool.isIE) {
			d = d - 4;
			c = c - 4;
		}
		b.left = d + "px";
		b.top = c + "px";
	},
	getMultiple : function() {
		var c = this, b = c.body.style, a = (parseInt(c.body.scrollWidth) + parseInt(c.body.scrollHeight))
				/ (parseInt(b.width) + parseInt(b.height));
		if (a > 1.82) {
			a = a - 0.82
		} else {
			if (a < 1.2) {
				a = 1.2
			}
		}
		return a;		
	},
	initPercent : function() {
		var a = this;
		a.multiple = a.getMultiple();
		a.widthPercent = a.smallMap.offsetWidth / parseInt(a.body.scrollWidth);
		a.heightPercent = a.smallMap.offsetHeight
				/ parseInt(a.body.scrollHeight);
		a.percent = a.heightPercent <= a.widthPercent ? a.heightPercent
				: a.widthPercent;
	},
	activeMode : function(c) {
		var a = com.xjwgraph.Global;
		a.modeTool.clear();
		var b = $id("small" + c).style;
		b.borderWidth = "1px";
		b.borderColor = a.smallTool.checkColor;
		b.borderStyle = "solid";
		stopEvent = true
	},
	drawMode : function(e) {
		var b = null, a = this;
		if ($id("small" + e.id)) {
			b = $id("small" + e.id)
		} else {
			b = document.createElement("img");
			b.ondragstart = function() {
				return false
			};
			b.id = "small" + e.id;
			b.style.position = "absolute";
			b.onclick = function() {
				var f = com.xjwgraph.Global;
				f.modeTool.flip(e.id.substring("modelu".length));
				f.smallTool.activeMode(e.id)
			};
			a.smallMap.appendChild(b)
		}
		a.smallModeMap.put(b.id, b);
		b = $id(b.id);
		var c = e.id.replace("module", "backImg");
		b.src = $id(c).src;
		var d = b.style;
		d.fontSize = "0px";
		d.left = e.offsetLeft * a.widthPercent + "px";
		d.top = e.offsetTop * a.heightPercent + "px";
		d.width = e.offsetWidth * a.percent + "px";
		d.height = e.offsetHeight * a.percent + "px";
		d.borderWidth = "1px";
		d.borderColor = com.xjwgraph.Global.smallTool.checkColor;
		d.borderStyle = "solid"
	},
	_smallPath : function(e) {
		var d = com.xjwgraph.Global.lineTool, f = d.getPathArray(e), c = f.length, a = this;
		for ( var b = c; b--;) {
			if (b % 2 == 1) {
				f[b] = parseInt(f[b] * a.heightPercent)
			} else {
				f[b] = parseInt(f[b] * a.widthPercent)
			}
		}
		return d.arrayToPath(f)
	},
	drawLine : function(m) {
		var d = com.xjwgraph.Global, h = d.lineTool, l = h.getPath(m), i = h
				.getLineHead(l), f = h.getLineEnd(l), j = this, e = parseInt(i[0])
				* j.widthPercent, c = parseInt(i[1]) * j.heightPercent, a = parseInt(f[0])
				* j.widthPercent, k = parseInt(f[1]) * j.heightPercent, b = m
				.getAttribute("brokenType");
		l = j._smallPath(l);
		var g = null;
		if (j.smallLineMap.get("small" + m.id) && $id("small" + m.id)) {
			g = $id("small" + m.id);
			d.lineTool.setPath(g, l)
		} else {
			if (h.isSVG) {
				g = document.createElementNS("http://www.w3.org/2000/svg",
						"path");
				g.setAttribute("id", "small" + m.id);
				g.setAttribute("style", "fill:none; stroke:"
						+ PathGlobal.lineColor + "; stroke-width:1");
				d.lineTool.setPath(g, l);
				j.svgBody.appendChild(g)
			} else {
				if (h.isVML) {
					g = document
							.createElement('<v:shape id="small'
									+ m.id
									+ '" style="WIDTH:100;POSITION:absolute;HEIGHT:100" coordsize="100,100" filled="f" strokeweight="1px" strokecolor="'
									+ PathGlobal.lineColor + '" path="' + l
									+ '"></v:shape>');
					j.smallMap.appendChild(g)
				}
			}
			j.smallLineMap.put(g.id, g)
		}
	},
	drawAll : function() {
		var a = this;
		a.drawAllMode();
		a.drawAllLine()
	},
	drawAllMode : function() {
		var a = com.xjwgraph.Global;
		a.modeTool.forEach(function(b) {
			a.smallTool.drawMode($id(b))
		})
	},
	drawAllLine : function() {
		var a = com.xjwgraph.Global;
		a.lineTool.forEach(function(b) {
			a.smallTool.drawLine($id(b))
		})
	},
	removeAll : function() {
		var a = this;
		a.removeAllMode();
		a.removeAllLine()
	},
	removeAllMode : function() {
		com.xjwgraph.Global.modeTool.forEach(this.removeMode)
	},
	removeAllLine : function() {
		com.xjwgraph.Global.lineTool.forEach(this.removeLine)
	},
	removeMode : function(b) {
		var a = $id("small" + b);
		this.smallModeMap.remove(a.id);
		a.parentNode.removeChild(a)
	},
	removeLine : function(a) {
		var b = $id("small" + a);
		b.parentNode.removeChild(b)
	},
	clear : function() {
		this.forEachMode(this.clearMode)
	},
	clearMode : function(a) {
		if (a) {
			if (a.id.indexOf("module") > 0) {
				a.style.border = "none"
			}
		}
	},
	forEachMode : function(d) {
		var b = this;
		var a = b.smallMap.childNodes.length;
		for ( var c = a; c--;) {
			if (d) {
				d(b.smallMap.childNodes[c])
			}
		}
	},
	initDraw : function() {
		var a = this;
		a.removeAll();
		a.initPercent();
		a.drawAll()
	}
};