//定义全局 
var com = {};
com.xjwgraph = {};
var PathGlobal = com.xjwgraph.PathGlobal = {
	pointTypeLeftUp : 1,
	pointTypeUp : 2,
	pointTypeUpRight : 3,
	pointTypeLeft : 4,
	pointTypeRight : 5,
	pointTypeLeftDown : 6,
	pointTypeDown : 7,
	pointTypeDownRight : 8,
	lineDefIndex : 10,
	lineDefStep : 2,
	modeDefIndex : 10,
	modeDefStep : 2,
	modeInc : 3,
	pauseTime : 10,
	modeHeigh : 0,
	copyModeDec : 10,
	rightMenu : false,
	isPixel : true,
	isAutoLineType : false,
	maxEvent : 17,
	minHeight : 32,
	minWidth : 32,
	selectColor : "C5E7F6",
	clearBoderColor : "blue",
	defaultColor : "green",
	defaultMaxMag : 0.5,
	defaultMinMag : 2,
	lineColor : "black",
	lineCheckColor : "red",
	strokeweight : 1.8,
	dragPointDec : 3,
	switchType : false,
	newGraph : "\u65b0\u5efa\u56fe\u5c42",
	modeCreate : "\u521b\u5efa\u6a21\u5143",
	lineCreate : "\u521b\u5efa\u7ebf\u5143",
	modeMove : "\u79fb\u52a8\u6a21\u5143",
	lineMove : "\u79fb\u52a8\u7ebf\u5143",
	modeDragPoint : "\u7f29\u653e\u6a21\u5143",
	updateMode : "\u7f16\u8f91\u6a21\u5143",
	updateLine : "\u7f16\u8f91\u7ebf\u5143",
	copyMode : "\u62f7\u8d1d\u6a21\u5143",
	removeMode : "\u5220\u9664\u6a21\u5143",
	remodeLine : "\u5220\u9664\u7ebf\u5143",
	baseClear : "\u9009\u62e9\u6a21\u5143",
	toMerge : "\u7ec4\u5408\u6a21\u5143",
	toSeparate : "\u89e3\u7ec4\u6a21\u5143",
	clearContext : "\u6e05\u9664\u533a\u57df",
	contextDivDrag : "\u79fb\u52a8\u533a\u57df",
	toLeft : "\u5de6\u5bf9\u9f50",
	toRight : "\u53f3\u5bf9\u9f50",
	toMiddleWidth : "\u5782\u76f4\u5c45\u4e2d",
	toTop : "\u9876\u5bf9\u9f50",
	toMiddleHeight : "\u6c34\u5e73\u5c45\u4e2d",
	toBottom : "\u5e95\u5bf9\u9f50",
	buildLineAndMode : "\u7ed1\u5b9a\u7ebf\u5143",
	removeLineAndMode : "\u79fb\u9664\u7ed1\u5b9a",
	modeCutter : "\u526a\u5207\u6a21\u5143",
	modeDuplicate : "\u590d\u5236\u6a21\u578b",
	eventName : "\u89e6\u53d1\u4e8b\u4ef6",
	lineProTitle : "\u7ebf\u5c5e\u6027\u8bbe\u7f6e",
	modeProTitle : "\u7f16\u8f91\u6a21\u5143",
	editProp : "\u7f16\u8f91\u5c5e\u6027"
};
//事件工厂
var UndoRedoEventFactory = com.xjwgraph.UndoRedoEventFactory = function(b) {
	var a = this;
	a.hisMessageDiv = b;
	a.stack = [];
	a.index = 0;
	a.redo = function() {
		stopEvent = true;
		var d = a.stack[a.index];
		if (d) {
			d.redo();
			this.index++
		}
		var c = a.stack.length;
		if (this.index > c) {
			this.index = c
		}
		a.history()
	};
	a.undo = function() {
		stopEvent = true;
		var c = a.stack[a.index - 1];
		if (c) {
			c.undo();
			a.index--
		}
		if (this.index < 1) {
			a.index = 1
		}
		a.history()
	};
	a.addEvent = function(c) {
		a.stack.splice(a.index, (a.stack.length - a.index++), c);
		if (a.stack.length > PathGlobal.maxEvent) {
			a.stack.splice(0, 1);
			this.index = PathGlobal.maxEvent
		}
		a.history()
	}
};
UndoRedoEventFactory.prototype = {
	init : function() {
		var a = new com.xjwgraph.UndoRedoEvent(function() {
		}, PathGlobal.newGraph);
		a.setRedo(function() {
		})
	},
	onHistory : function(c, b) {
		var a = this;
		c.onclick = function() {
			var f = com.xjwgraph.Global;
			f.lineTool.clear();
			f.modeTool.clear();
			stopEvent = true;
			if (b > a.index) {
				for ( var d = a.index; d <= b - 1; d++) {
					var e = a.stack[d];
					if (e && e.redo) {
						e.redo()
					}
				}
			} else {
				if (b < a.index) {
					for ( var d = a.index; d >= b; d--) {
						var e = a.stack[d];
						if (e && e.undo) {
							e.undo()
						}
					}
				}
			}
			a.index = b;
			a.history()
		}
	},
	history : function() {
		var f = $id(this.hisMessageDiv);
		if (!f) {
			return
		}
		var a = this;
		f.innerHTML = "";
		var h = a.stack.length, e = document, c = e.createDocumentFragment();
		for ( var b = 0; b < h; b++) {
			var g = e.createElement("div");
			g.style.cssText = "position:relative;text-align:center;border-bottom:1px dotted #cccccc; width:112px;height:20px;line-height:20px;";
			g.innerHTML = a.stack[b].name;
			var d = b + 1;
			a.onHistory(g, d);
			if ((a.index) == d) {
				g.style.backgroundColor = PathGlobal.selectColor
			}
			c.appendChild(g)
		}
		f.appendChild(c)
	},
	clear : function() {
		var a = this;
		delete a.stack;
		a.stack = [];
		a.index = 0;
		a.history();
	}
};
var UndoRedoEvent = com.xjwgraph.UndoRedoEvent = function(c, b) {
	var a = this;
	a.name = b ? b : PathGlobal.eventName;
	a.undo = c;
	a.redo;
	com.xjwgraph.Global.undoRedoEventFactory.addEvent(a);
	a.setUndo = function(d) {
		a.undo = d
	};
	a.setRedo = function(d) {
		a.redo = d
	}
};
//基础工具类
var BaseTool = com.xjwgraph.BaseTool = function(c, d, a) {
	
	var b = this;
	b.pathBody = c;
	b.checkColor = "#00ff00";
	b.areaDiv = document.createElement("div");	
	b.initEndDiv(d, a);
	b.initPathBody(b.pathBody);
	b.contextMoveAbale = false;
	b.contextMoveAttempt = false;
	b.contextMap = new Map();
	b.tempContextId = null;
	b.checkBrowser()
};
BaseTool.prototype = {
		initScaling : function(a) {			
			var b = this, d = com.xjwgraph.Global, c = d.smallTool, e = d.lineTool;
			b
					.forEach(
							b.contextMap,
							function(h) {
								var j = $id(h), m = j.style;
								m.top = 0 + "px";
								m.left = 0 + "px";
								var l = 0, g = 0, i = b.contextMap.get(h), k = i.contextModeMap, f = i.contextLineMap;
								b
										.forEach(
												f,
												function(n) {
													var o = $id(n), s = e
															.getPath(o), t = e
															.getPathArray(s), r = t.length;
													for ( var q = r; q--;) {
														var p = (q % 2 == 1);
														if (p
																&& (parseInt(m.top) > t[q] || parseInt(m.top) == 0)) {
															m.top = t[q] - 2 + "px"
														}
														if (!p
																&& (parseInt(m.left) > t[q] || parseInt(m.left) == 0)) {
															m.left = t[q] - 2
																	+ "px"
														}
														if (p
																&& (parseInt(g) < parseInt(t[q]))) {
															g = t[q]
														}
														if (!p
																&& (parseInt(l) < parseInt(t[q]))) {
															l = t[q]
														}
													}
												});
								b
										.forEach(
												k,
												function(u) {
													var s = $id(u), p = s.style, v = parseInt(p.top), o = parseInt(p.left), r = parseInt(s.offsetWidth), t = parseInt(s.offsetHeight), n = parseInt(m.top), q = parseInt(m.left);
													if (n > v || n == 0) {
														m.top = v + "px"
													}
													if (q > o || q == 0) {
														m.left = o + "px"
													}
													if (l < r + o) {
														l = r + o
													}
													if (g < t + v) {
														g = t + v
													}
												});
								m.width = l - parseInt(m.left) + "px";
								m.height = g - parseInt(m.top) + "px"
							})
		},
		getOptionMap : function(b) {			
//			var a = this, e = null;
//			if (b) {
//				e = a.contextMap.get(b).contextModeMap
//			} else {
//				var c = com.xjwgraph.Global;
//				e = new Map();
//				e.putAll(c.modeMap);
//				var d = c.baseTool;
//				d.forEach(d.contextMap, function(f) {
//					var h = d.contextMap.get(f).contextModeMap, g = $id(f), i = g
//							.getAttribute("groups");
//					d.forEach(h, function(j) {
//						if (i == "true" || i) {
//							e.remove(j)
//						}
//					})
//				})
//			}
//			return e
		},
		sumLeftTop : function(a, b, c) {},
		
		showMenu : function(b, i) {},
		toSeparate : function() {
			var b = this, a = [], c = 0;
			b.forEach(b.contextMap, function(f) {
				var g = $id(f);
				if (g.style.borderColor == PathGlobal.defaultColor) {
					a[c++] = g;
					g.setAttribute("groups", false)
				}
			});
			var e = com.xjwgraph.Global;
			var d = new com.xjwgraph.UndoRedoEvent(function() {
				var h = a.length, g = e.baseTool;
				for ( var f = h; f--;) {
					var j = a[f];
					j.setAttribute("groups", true)
				}
			}, PathGlobal.toSeparate);
			d.setRedo(function() {
				var h = a.length, g = e.baseTool;
				for ( var f = h; f--;) {
					var j = a[f];
					j.setAttribute("groups", true)
				}
			});
			b.clearContext()
		},
		checkBrowser : function() {
			var b = navigator.userAgent.toLowerCase();
			check = function(c) {
				return c.test(b)
			};
			var a = this;
			a.isOpera = check(/opera/);
			a.isIE = !a.isOpera && check(/msie/);
			a.isIE7 = a.isIE && check(/msie 7/);
			a.isIE8 = a.isIE && check(/msie 8/);
			a.isIE6 = a.isIE && !a.isIE7 && !a.isIE8;
			a.isChrome = check(/chrome/);
			a.isWebKit = check(/webkit/);
			a.isSafari = !a.isChrome && check(/safari/);
			a.isSafari2 = a.isSafari && check(/applewebkit\/4/);
			a.isSafari3 = a.isSafari && check(/version\/3/);
			a.isSafari4 = a.isSafari && check(/version\/4/);
			a.isGecko = !a.isWebKit && check(/gecko/);
			a.isGecko2 = a.isGecko && check(/rv:1\.8/);
			a.isGecko3 = a.isGecko && check(/rv:1\.9/)
		},
		getBrowserName : function() {
			var a = this;
			if (a.isIE) {
				if (a.isIE8) {
					return "IE8"
				} else {
					if (a.isIE7) {
						return "IE7"
					} else {
						if (a.isIE6) {
							return "IE6"
						} else {
							return "IE"
						}
					}
				}
			}
			if (a.isChrome) {
				return "CHROME"
			} else {
				if (a.isWebKit) {
					return "WEBKIT"
				} else {
					if (a.isOpera) {
						return "OPERA"
					} else {
						if (a.isGecko) {
							return "GECKO"
						} else {
							if (a.isGecko2) {
								return "GECKO2"
							} else {
								if (a.isGecko3) {
									return "GECKO3"
								}
							}
						}
					}
				}
			}
			if (a.isSafari) {
				return "SAFARI"
			} else {
				if (a.isSafari2) {
					return "SAFARI2"
				} else {
					if (a.isSafari3) {
						return "SAFARI3"
					} else {
						if (a.isSafari4) {
							return "SAFARI4"
						}
					}
				}
			}
		},

		toMerge : function() {
			
			var b = this, a = [], c = 0;
			b.forEach(b.contextMap, function(f) {
				var g = $id(f);
				if (g.style.borderColor == PathGlobal.defaultColor) {
					a[c++] = g;
					g.setAttribute("groups", true);
					g.oncontextmenu = function() {
						return false
					}
				}
			});
			var e = com.xjwgraph.Global;
			var d = new com.xjwgraph.UndoRedoEvent(function() {
				var h = a.length, g = e.baseTool;
				for ( var f = h; f--;) {
					var j = a[f];
					j.setAttribute("groups", false);
					j.oncontextmenu = function(i) {
						PathGlobal.rightMenu = true;
						g.showMenu(i, this.id);
						return false
					}
				}
			}, PathGlobal.toMerge);
			d.setRedo(function() {
				var h = a.length, g = e.baseTool;
				for ( var f = h; f--;) {
					var j = a[f];
					j.setAttribute("groups", true);
					j.oncontextmenu = function(i) {
						return false
					}
				}
			})
		},
		forEach : function(e, c) {
			var d = e.getKeys(), a = d.length;
			for ( var b = a; b--;) {
				if (c) {
					c(d[b])
				}
			}
		},
		removeAll : function() {
			var a = this, b = com.xjwgraph.Global.baseTool;
			a.forEach(a.contextMap, function(c) {
				var d = $id(c);
				b.contextMap.remove(c);
				b.pathBody.removeChild(d)
			})
		},
		clearContext : function() {
			var b = this;
			b.tempContextId = null;
			var a = [], f = [], c = 0, e = com.xjwgraph.Global.baseTool;
			b.forEach(b.contextMap, function(i) {
				var j = $id(i), h = j.style;
				h.borderColor = PathGlobal.clearBoderColor;
				h.filter = "alpha(opacity=10)";
				h.opacity = "0.1";
				var g = j.getAttribute("groups");
				if (g == "false" || !g) {
					a[c] = j;
					f[c++] = e.contextMap.get(i);
					e.contextMap.remove(i);
					e.pathBody.removeChild(j)
				}
			});
			if (a.length > 0) {
				var d = new com.xjwgraph.UndoRedoEvent(function() {
					var h = a.length;
					for ( var g = h; g--;) {
						var j = a[g];
						e.contextMap.put(j.id, f[g]);
						e.pathBody.appendChild(j)
					}
				}, PathGlobal.clearContext);
				d.setRedo(function() {
					var h = a.length;
					for ( var g = h; g--;) {
						var j = a[g];
						if (j && j.id && $id(j.id)) {
							e.contextMap.remove(j.id);
							e.pathBody.removeChild(j)
						}
					}
				})
			}
		},
		clear : function() {
			PathGlobal.rightMenu = false;
			var k = this, d = k.areaDiv.style, i = parseInt(d.top), c = parseInt(d.left), m = parseInt(d.width), l = parseInt(d.height);
			if (d.visibility != "visible") {
				return
			}
			var r = document.createElement("div"), t = r.style;
			t.position = "absolute";
			t.fontSize = "0px";
			t.borderWidth = "1px";
			t.borderStyle = "solid";
			t.borderColor = PathGlobal.defaultColor;
			t.visibility = "visible";
			t.top = 0 + "px";
			t.left = 0 + "px";
			t.width = 0 + "px";
			t.height = 0 + "px";
			t.backgroundColor = PathGlobal.selectColor;
			t.filter = "alpha(opacity=20)";
			t.opacity = "0.2";
			var p = com.xjwgraph.Global, h = p.modeTool, o = p.lineTool, n = h
					.getNextIndex();
			t.zIndex = n;
			r.setAttribute("id", "contextDiv" + n);
			var b = new Map(), a = new Map(), u = 0, q = 0, e = com.xjwgraph.Global.baseTool;
			o.clear();
			o
					.forEach(function(w) {
						var F = $id(w), E = o.getPath(F), D = o.getPathArray(E), C = D.length, B = true, A = 0, z = 0;
						for ( var v = C; v--;) {
							var y = true;
							if (v % 2 == 1) {
								A = i;
								z = i + l
							} else {
								A = c;
								z = c + m;
								y = false
							}
							if (!(D[v] >= A && D[v] <= z)) {
								B = false;
								break
							}
							if (y
									&& (parseInt(t.top) > D[v] || parseInt(t.top) == 0)) {
								t.top = D[v] - 2 + "px"
							}
							if (!y
									&& (parseInt(t.left) > D[v] || parseInt(t.left) == 0)) {
								t.left = D[v] - 2 + "px"
							}
							if (y && (parseInt(q) < parseInt(D[v]))) {
								q = D[v]
							}
							if (!y && (parseInt(u) < parseInt(D[v]))) {
								u = D[v]
							}
						}
						if (B) {
							var x = true;
							e
									.forEach(
											e.contextMap,
											function(G) {
												var H = p.baseTool.contextMap
														.get(G).contextLineMap, I = $id(G), J = I
														.getAttribute("groups");
												e.forEach(H, function(K) {
													if (K == F.id
															&& (J == "true" || J)) {
														x = false
													}
												})
											});
							stopEvent = true;
							if (x) {
								o.show(F);
								a.put(w, F)
							}
						}
					});
			h
					.forEach(function(C) {
						var z = $id(C), x = z.style, v = $id(C.replace("module",
								"backImg")), D = v.style, E = parseInt(x.top), w = parseInt(x.left), y = parseInt(D.width), B = parseInt(D.height), A = true;
						e
								.forEach(
										e.contextMap,
										function(F) {
											var H = p.baseTool.contextMap.get(F).contextModeMap, G = $id(F), I = G
													.getAttribute("groups");
											e
													.forEach(
															H,
															function(J) {
																if (J == z.id
																		&& (I == "true" || I)) {
																	A = false
																}
															})
										});
						if (A && E > i && w > c && w + y < c + m && E + B < i + l) {
							if (parseInt(t.top) > E || parseInt(t.top) == 0) {
								t.top = E + "px"
							}
							if (parseInt(t.left) > w || parseInt(t.left) == 0) {
								t.left = w + "px"
							}
							if (u < y + w) {
								u = y + w
							}
							if (q < B + E) {
								q = B + E
							}
							stopEvent = true;
							p.modeTool.showPointer(z);
							b.put(z.id, z)
						} else {
							p.modeTool.hiddPointer(z)
						}
					});
			k.clearContext();
			function g(w, v) {
				this.contextModeMap = w;
				this.contextLineMap = v
			}
			if ((b.size() + a.size()) > 1) {
				k.pathBody.appendChild(r);
				var g = new g(b, a);
				k.contextMap.put(r.id, g);
				k.tempContextId = r.id;
				t.width = (u - parseInt(t.left)) + "px";
				t.height = (q - parseInt(t.top)) + "px";
				var e = p.baseTool;
				var s = new com.xjwgraph.UndoRedoEvent(function() {
					if (r && r.id && $id(r.id)) {
						e.pathBody.removeChild(r);
						e.contextMap.remove(r.id)
					}
				}, PathGlobal.baseClear);
				s.setRedo(function() {
					e.pathBody.appendChild(r);
					e.contextMap.put(r.id, g)
				});
				k.contextDivDrag(r, g)
			}
			d.top = 1 + "px";
			d.left = 1 + "px";
			d.width = 1 + "px";
			d.height = 1 + "px";
			d.visibility = "hidden"
		},
		contextDivDrag : function(d, b) {
//			var i = d.style, c = com.xjwgraph.Global, j = c.smallTool, h = c.baseTool, e = c.modeTool, f = c.lineTool, a = b.contextModeMap, g = b.contextLineMap;
//			d.onclick = function() {
//				i.borderColor = PathGlobal.defaultColor;
//				i.filter = "alpha(opacity=30)";
//				i.opacity = "0.3";
//				h.forEach(h.contextMap, function(k) {
//					if (k != d.id) {
//						var m = $id(k);
//						var l = m.style;
//						l.borderColor = PathGlobal.clearBoderColor;
//						l.filter = "alpha(opacity=10)";
//						l.opacity = "0.1"
//					}
//				})
//			};
//			d.oncontextmenu = function(k) {
//				PathGlobal.rightMenu = true;
//				h.showMenu(k, this.id);
//				return false
//			};
//			d.ondragstart = function() {
//				return false
//			};
//			d.onmousedown = function(m) {				
//				m = m || window.event;
//				c.modeTool.clear();
//				h.contextMoveAbale = true;
//				i.borderColor = PathGlobal.defaultColor;
//				i.filter = "alpha(opacity=20)";
//				i.opacity = "0.2";
//				i.visibility = "visible";
//				h.forEach(h.contextMap, function(q) {
//					if (q != d.id) {
//						var s = $id(q), r = s.style;
//						r.borderColor = PathGlobal.clearBoderColor;
//						r.filter = "alpha(opacity=10)";
//						r.opacity = "0.1"
//					}
//				});
//				var l = parseInt(i.top), p = parseInt(i.left);
//				if (d.setCapture) {
//					d.setCapture()
//				} else {
//					if (window.captureEvents) {
//						window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
//					}
//				}
//				var k = m.layerX && m.layerX >= 0 ? m.layerX : m.offsetX, o = m.layerY
//						&& m.layerX >= 0 ? m.layerY : m.offsetY, l = parseInt(i.top), p = parseInt(i.left), n = document;
//				n.onmousemove = function(u) {
//					h.contextMoveAttempt = true;
//					u = u || window.event;
//					if (!u.pageX) {
//						u.pageX = u.clientX
//					}
//					if (!u.pageY) {
//						u.pageY = u.clientY
//					}
//					var r = u.pageX - k, q = u.pageY - o, t = com.xjwgraph.Global, s = t.lineTool.pathBody, v = t.baseTool
//							.sumLeftTop(s);
//					r = r - parseInt(v[0]) + parseInt(s.scrollLeft);
//					q = q - parseInt(v[1]) + parseInt(s.scrollTop);
//					i.left = r + "px";
//					i.top = q + "px"
//				};
//				n.onmouseup = function(C) {
//					C = C || window.event;
//					if (!C.pageX) {
//						C.pageX = C.clientX
//					}
//					if (!C.pageY) {
//						C.pageY = C.clientY
//					}
//					var N = C.pageX - k, L = C.pageY - o, A = com.xjwgraph.Global, r = A.lineTool.pathBody, y = A.baseTool
//							.sumLeftTop(r);
//					N = N - parseInt(y[0]) + parseInt(r.scrollLeft);
//					L = L - parseInt(y[1]) + parseInt(r.scrollTop);
//					if (d.releaseCapture) {
//						d.releaseCapture()
//					} else {
//						if (window.releaseEvents) {
//							window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
//						}
//					}
//					n.onmousemove = null;
//					n.onmouseup = null;
//					if (h.contextMoveAttempt) {
//						var v = a.getKeys(), w = v.length, z = g.getKeys(), J = z.length, F = new Map(), P = new Map();
//						for ( var G = J; G--;) {
//							var O = z[G];
//							line = $id(O), newLineLeft = N - p, newLineTop = L - l,
//									path = f.getPath(line), paths = f
//											.getPathArray(path),
//									pathLength = paths.length, lineMode = A.lineMap
//											.get(line.id),
//									xBaseMode = lineMode.xBaseMode,
//									wBaseMode = lineMode.wBaseMode;
//							F.put(O, path);
//							for ( var B = pathLength; B--;) {
//								if (B % 2 == 1) {
//									paths[B] = parseInt(paths[B])
//											+ parseInt(newLineTop)
//								} else {
//									paths[B] = parseInt(paths[B])
//											+ parseInt(newLineLeft)
//								}
//							}
//							var q = f.arrayToPath(paths);
//							P.put(O, q);
//							f.setPath(line, q);
//							f.setDragPoint(line);
//							if (wBaseMode && $id(wBaseMode.id)
//									&& !a.containsKey(wBaseMode.id)) {
//								f.removeAllLineAndMode(line, $id(wBaseMode.id))
//							}
//							if (xBaseMode && $id(xBaseMode.id)
//									&& !a.containsKey(xBaseMode.id)) {
//								f.removeAllLineAndMode(line, $id(xBaseMode.id))
//							}
//							j.drawLine(line);
//							f.clearLine(O)
//						}
//						function H(S, R) {
//							var Q = this;
//							Q.modeTop = S;
//							Q.modeLeft = R
//						}
//						var M = new Map(), E = new Map();
//						for ( var G = w; G--;) {
//							var x = $id(v[G]), D = x.style, u = N - p, I = L - l;
//							M.put(x.id, new H(parseInt(D.top), parseInt(D.left)));
//							D.left = parseInt(D.left) + u + "px";
//							D.top = parseInt(D.top) + I + "px";
//							E.put(x.id, new H(parseInt(D.top), parseInt(D.left)));
//							e.changeBaseModeAndLine(x, true);
//							j.drawMode(x)
//						}
//						var K = new com.xjwgraph.UndoRedoEvent(function() {
//							i.top = l + "px";
//							i.left = p + "px";
//							for ( var S = w; S--;) {
//								var U = $id(v[S]), R = U.style, T = M.get(U.id);
//								R.left = T.modeLeft + "px";
//								R.top = T.modeTop + "px";
//								e.showPointer(U);
//								e.changeBaseModeAndLine(U, true);
//								j.drawMode(U)
//							}
//							for ( var S = J; S--;) {
//								var Q = z[S];
//								line = $id(Q), oldLine = F.get(Q);
//								f.setPath(line, oldLine);
//								f.show(line);
//								j.drawLine(line)
//							}
//						}, PathGlobal.contextDivDrag);
//						var t = parseInt(i.top), s = parseInt(i.left);
//						K.setRedo(function() {
//							i.top = t + "px";
//							i.left = s + "px";
//							for ( var S = w; S--;) {
//								var U = $id(v[S]), R = U.style, T = E.get(U.id);
//								R.left = T.modeLeft + "px";
//								R.top = T.modeTop + "px";
//								e.showPointer(U);
//								e.changeBaseModeAndLine(U, true);
//								j.drawMode(U)
//							}
//							for ( var S = J; S--;) {
//								var Q = z[S];
//								line = $id(Q), q = P.get(Q);
//								f.setPath(line, q);
//								f.show(line);
//								j.drawLine(line)
//							}
//						})
//					}
//					h.contextMoveAttempt = false;
//					h.contextMoveAbale = false
//				}
//			}
		},
		initPathBody : function(b) {
			var b = $id(b.id), a = this, e = a.areaDiv, c = e.style;
			c.position = "absolute";
			c.width = 1 + "px";
			c.height = 1 + "px";
			c.fontSize = "0px";
			c.borderWidth = "1px";
			c.borderStyle = "solid";
			c.visibility = "hidden";
			c.backgroundColor = PathGlobal.selectColor;
			c.filter = "alpha(opacity=30)";
			c.opacity = "0.3";
			b.appendChild(e);
			b.ondragstart = function() {
				return false
			};
			var d = com.xjwgraph.Global;
			b.onclick = function() {
				d.baseTool.clear()
			};
			b.ondblclick = function() {
				d.baseTool.clear()
			};
			
			
		},
		findBackImgSrc : function(b) {			
			for ( var a = b.firstChild; a != null; a = a.nextSibling) {
				if (a.id && a.id.indexOf("backGroundImg") >= 0) {
					return a.src
				}
			}
		},
		drag : function(b, c, a) {			
			var b = b, c = c, d = this.findBackImgSrc(b), e = com.xjwgraph.Global;
			b.ondragstart = function() {
				return false
			};
			//选中图元区图标事件
			b.onmousedown = function(i) {
				
				i = i || window.event;
				var k = $id("moveBaseModeImg");
				k.src = d;
				var h = $id("moveBaseMode"), g = h.style;
				g.visibility = "visible";
				if (h.setCapture) {
					h.setCapture()
				} else {
					if (window.captureEvents) {
						document.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
					}
				}
				var f = i.clientX ? i.clientX : i.offsetX, l = i.clientY ? i.clientY
						: i.offsetY;
				g.left = f + "px";
				g.top = l + "px";
				var j = document;
				j.onmousemove = function(o) {
					o = o || window.event;
					var n = o.clientX, m = o.clientY;
					g.left = n + "px";
					g.top = m + "px"
				};
				j.onmouseup = function(q) {
					q = q || window.event;
					if (h.releaseCapture) {
						h.releaseCapture()
					} else {
						if (window.releaseEvents) {
							document.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
						}
					}
					j.onmousemove = null;
					j.onmouseup = null;
					g.visibility = "hidden";
					if (!q.pageX) {
						q.pageX = q.clientX
					}
					if (!q.pageY) {
						q.pageY = q.clientY
					}
					var o = q.pageX, m = q.pageY, p = e.lineTool.pathBody, r = e.baseTool
							.sumLeftTop(p);
					o = o - parseInt(r[0]) + parseInt(p.scrollLeft);
					m = m - parseInt(r[1]) + parseInt(p.scrollTop);
					var n = o >= 0 && m >= 0;
					if (n) {
						if (c) {      //判断是生成line还是其他图标，o表示x坐标，m表示y坐标，a表示线的类型
							e.lineTool.create(o, m, a);
						} else {
							e.modeTool.create(m, o, d,b);
						}
					}
				}
			}
		},
		initEndDiv : function(f, b) {
			var d = this;
			d.endDiv = document.createElement("div");
			var e = d.endDiv, g = e.style;
			g.left = f + "px";
			g.top = b + "px";
			g.width = "10px";
			g.height = "10px";
			g.fontSize = "10px";
			g.position = "absolute";
			d.pathBody.appendChild(e);
			var a = $id("topCross"), c = $id("leftCross");
			a.style.width = f + "px";
			c.style.height = b + "px"
		},
		isSVG : function() {
			return this.VGType() == "SVG"
		},
		VGType : function() {
			return window.SVGAngle
					|| document.implementation.hasFeature(
							"http://www.w3.org/TR/SVG11/feature#BasicStructure",
							"1.1") ? "SVG" : "VML"
		},
		isVML : function() {
			return this.VGType() == "VML"
		},
		
		//如果点击绘图区task图标，则输入框显示
		showtaskTemplate : function(taskType){		
			if(taskType===undefined){
				 return;
			}
	
			//如果点击绘图区task的图标，显示taskTemplate的tr
			if(taskType=="serviceTask"){
				$("[name='taskTemplate']").show();				
				$("[name='inputTitle']").show();
				$("#blockFlag").show();
				$("#Variable").hide();
				$("#countersign").hide();
				$('#isSequential').hide();
				$("#subProcessButton").hide();
			}
			else if(taskType=="userTask"){
				$("[name='taskTemplate']").show();
				$("[name='inputTitle']").show();
				$("#Variable").hide();
				$("#countersign").show();
				$("#subProcessButton").hide();
				$("#blockFlag").hide();
			}
			else if(taskType=="businessRuleTask"){
				$("[name='taskTemplate']").show();
				$("[name='inputTitle']").show();
				$("#Variable").show();
				$("#countersign").hide();
				$('#isSequential').hide();
				$("#subProcessButton").hide();
				$("#blockFlag").hide();
			}
			else if(taskType=="scriptTask"){				
				$("[name='taskTemplate']").hide();
				$("[name='inputTitle']").show();
				$("#Variable").hide();
				$("#countersign").hide();
				$('#isSequential').hide();
				$("#subProcessButton").hide();
				$("#blockFlag").hide();
			}
			else if(taskType=="esbTask"){
				$("[name='taskTemplate']").hide();
				$("[name='inputTitle']").show();
				$("#Variable").hide();
				$("#countersign").hide();
				$('#isSequential').hide();
				$("#subProcessButton").hide();
				$("#blockFlag").hide();
			}
			else if(taskType=="subProcess"){
				$("[name='inputTitle']").show();
				$("[name='taskTemplate']").hide();
				$("#countersign").hide();
				$("#Variable").hide();
				$("#subProcessButton").show();
				$("#blockFlag").hide();
			}
			else{
				$("[name='taskTemplate']").hide();
				$("[name='inputTitle']").hide();
				$("#Variable").hide();
				$("#countersign").hide();
				$('#isSequential').hide();
				$("#subProcessButton").hide();
				$("#blockFlag").hide();
			}
		}
	};