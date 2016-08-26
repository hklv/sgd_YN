var BaseMode = com.xjwgraph.BaseMode = function() {
	var a = this;
	a.id;
	a.lineMap = new Map();
	a.prop = {
		attri1 : "2",
		attri2 : "3",
		attri3 : "4"
	}
};
var BuildLine = com.xjwgraph.BuildLine = function() {
	var a = this;
	a.id;
	a.index;
	a.xIndex;
	a.wIndex;
	a.type;
	a.xBaseMode;
	a.wBaseMode;
	a.prop = {
		attri1 : "2",
		attri2 : "3",
		attri3 : "4",
		attri4 : "5"
	}
};
var Point = com.xjwgraph.Point = function() {
	var a = this;
	a.x = 0;
	a.y = 0;
	a.index = 0
};