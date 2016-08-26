//Map类
var Map = com.xjwgraph.Map = function() {
	var a = this;
	a.map = new Object();
	a.length = 0
};
Map.prototype = {
		size : function() {
			return this.length
		},
		clear : function() {
			var a = this;
			a.map = new Object();
			a.length = 0
		},
		put : function(b, c) {
			var a = this;
			if (!a.map["_" + b]) {
				++a.length
			}
			a.map["_" + b] = c
		},
		putAll : function(a) {
			var f = a.getKeys(), d = f.length, c = this;
			for ( var e = d; e--;) {
				var b = f[e];
				c.put(b, a.get(b))
			}
		},
		remove : function(b) {
			var a = this;
			if (a.map["_" + b]) {
				--a.length;
				return delete a.map["_" + b]
			} else {
				return false
			}
		},
		containsKey : function(a) {
			return this.map["_" + a] ? true : false
		},
		get : function(b) {
			var a = this;
			return a.map["_" + b] ? a.map["_" + b] : null
		},
		inspect : function() {
			var c = "", a = this;
			for ( var b in a.map) {
				c += "\n" + b + "  Value:" + a.map[b]
			}
			return c
		},
		getKeys : function() {
			var d = new Array(), b = 0, a = this;
			for ( var c in a.map) {
				d[b++] = c.replace("_", "");
			}
			return d;
		}
	};