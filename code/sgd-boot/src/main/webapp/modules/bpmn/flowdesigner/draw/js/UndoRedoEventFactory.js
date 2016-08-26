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
		a.history();
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