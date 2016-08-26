var KeyDownFactory = com.xjwgraph.KeyDownFactory = function() {
};
KeyDownFactory.prototype = {
	removeNode : function() {
		var o = com.xjwgraph.Global, m = o.lineTool, g = o.baseTool, d = m.pathBody, f = m.svgBody, k = o.smallTool, h = o.modeTool, r = [], n = [], q = 0, b = false, c = [], l = [], p = 0, t = false, a = null;
		var lineTitleArray=[];  //存放删除的连接线名称数组
		var lineConditionArray=[];//存放删除的连接线表达式数组
		h.forEach(function(w) {
			var v = $id(w);
			if (h.isActiveMode(v)) {
				n[q] = o.modeMap.get(v.id);
				r[q++] = v;
				a = o.modeMap.get(w).lineMap, baseLineKey = a.getKeys(),
						baseLineKeyLength = baseLineKey.length;
				for ( var j = baseLineKeyLength; j--;) {
					var u = a.get(baseLineKey[j]), i = $id(u.id);
					var lineTitle=$id(u.id+"Title");	//获取随mode删除的连接线名称
					var lineCondition=$id(u.id+"Condition");//获取随mode删除的连接线表达式
					if (i) {
						l[p] = com.xjwgraph.Global.lineMap.get(i.id);
						var index=[p++];
						c[index] = i;	//存放的是连接线的PATH（储存的线的基本属性信息和坐标信息）					
						lineTitleArray[index]=lineTitle;	//将删除的连接线名称存入到数组中
						lineConditionArray[index]=lineCondition;//将删除的连接线表达式存入到数组中
						o.lineTool.removeNode(u.id);
					}
				}
				o.modeTool.removeNode(w);
				b = true;				
			}
		});
		o.baseTool.clearContext();
		if (b) {
			var s = new com.xjwgraph.UndoRedoEvent(function() {
				var B = c.length, w = null;
				for ( var y = B; y--;) {
					var A = c[y], u = A.getAttribute("id");
					var lineTitle=lineTitleArray[y];	//恢复mode的时候获取恢复连接线的名称
					var lineCondition=lineConditionArray[y];//恢复mode的时候获取恢复连接线的表达式
					if (m.isVML) {
						A.setAttribute("coordsize", "100,100");
						A.setAttribute("filled", "f");
						A.setAttribute("strokeweight", PathGlobal.strokeweight
								+ "px");
						A.setAttribute("strokecolor", PathGlobal.lineColor);
						w = d
					} else {
						if (m.isSVG) {
							w = f;
						}
					}
					if (!$id(u)) {
						w.appendChild(A);
						w.appendChild(lineTitle);	
						w.appendChild(lineCondition);
						w.appendChild(m.createRect(u + "lineHead"));
						w.appendChild(m.createRect(u + "lineMiddle"));
						w.appendChild(m.createRect(u + "lineEnd"));
						m.drag(A);
						o.lineMap.put(A.id, l[y]);
						k.drawLine(A);
					}
				}
				var x = r.length;
				for ( var z = x; z--;) {
					var v = r[z];
					o.modeMap.put(v.id, n[z]);
					d.appendChild(v);
					h.showPointer(v);
					k.drawMode(v)
				}
			}, PathGlobal.removeMode);
			s.setRedo(function() {
				var x = c.length;
				for ( var w = x; w--;) {
					var v = c[w];
					m.removeNode(v.id);
				}
				var u = r.length;
				for ( var w = u; w--;) {
					var j = r[w];
					o.modeTool.removeNode(j.id)
				}
			})
		}
		//单独操作删除连接线的时候存储数据信息
		m.forEach(function(i) {
			var j = $id(i);//获取连接线path属性和坐标
			var lineTitle=$id(i+"Title");	//获取连接线的名称
			var lineCondition=$id(i+"Condition");//获取连接线的表达式
			if (m.isActiveLine(j)) {
				l[p] = com.xjwgraph.Global.lineMap.get(j.id);
				var index=[p++];
				c[index] = j;
				lineTitleArray[index]=lineTitle;
				lineConditionArray[index]=lineCondition;
				m.removeNode(j.id);
				t = true;
			}
		});
		if (t) {
			var e = new com.xjwgraph.UndoRedoEvent(function() {
				var x = c.length, u = null;
				for ( var v = x; v--;) {
					var w = c[v], i = w.getAttribute("id");
					var lineTitle=lineTitleArray[v];
					var lineCondition=lineConditionArray[v];
					if (m.isVML) {
						w.setAttribute("coordsize", "100,100");
						w.setAttribute("filled", "f");
						w.setAttribute("strokeweight", PathGlobal.strokeweight
								+ "px");
						w.setAttribute("strokecolor", PathGlobal.lineColor);
						u = d
					} else {
						if (m.isSVG) {
							u = f
						}
					}
					if (!$id(i)) {
						u.appendChild(w);
						u.appendChild(lineTitle);
						u.appendChild(lineCondition);
						u.appendChild(m.createRect(i + "lineHead"));
						u.appendChild(m.createRect(i + "lineMiddle"));
						u.appendChild(m.createRect(i + "lineEnd"));
						m.drag(w);
						o.lineMap.put(w.id, l[v]);
						k.drawLine(w);
					}
				}
			}, PathGlobal.remodeLine);
			e.setRedo(function() {
				var v = c.length;
				for ( var u = v; u--;) {
					var j = c[u];
					m.removeNode(j.id)
				}
			})
		}
	},
	copyNode : function() {
		var f = com.xjwgraph.Global, b = f.modeTool, a = [], d = 0, c = f.smallTool;
		b.forEach(function(j) {
			var i = $id(j);
			if (b.isActiveMode(i)) {
				var h = f.modeTool.copy(i);
				a[d++] = h;
				b.hiddPointer(i);
				c.drawMode(h)
			}
		});
		var g = f.lineTool;
		if (a.length > 0) {
			var e = new com.xjwgraph.UndoRedoEvent(function() {
				var j = a.length;
				for ( var k = j; k--;) {
					var h = a[k];
					if (h && h.id && $id(h.id)) {
						f.modeMap.remove(h.id);
						g.pathBody.removeChild(h);
						c.removeMode(h.id)
					}
				}
			}, PathGlobal.copyMode);
			e.setRedo(function() {
				var k = a.length;
				for ( var l = k; l--;) {
					var j = a[l];
					var h = new BaseMode();
					h.id = j.id;
					f.modeMap.put(j.id, h);
					g.pathBody.appendChild(j);
					b.showPointer(j);
					c.drawMode(j);
				}
			})
		}
	}
};
var keyDownFactory = new KeyDownFactory();
function KeyDown(a) {
	a = a || window.event;
	if (a.keyCode == 46) {
		keyDownFactory.removeNode();
	}
	if (a.ctrlKey) {
		switch (a.keyCode) {
		case 77:
			keyDownFactory.removeNode();
			break;
		case 86:
			keyDownFactory.copyNode();
			break
		}
	}
}