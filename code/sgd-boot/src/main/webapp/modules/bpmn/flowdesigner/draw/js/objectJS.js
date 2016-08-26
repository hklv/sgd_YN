/**
 * FastMap用来处理类似于java中的Map的脚本对象，这里是FastMap的构造函数

 */
var FastMap = function() {
	this.version = '1.0'; // 版本标识
	this.buf = new Object(); // 缓存Key和Value的对象

}

/**
 * FastMap的类方法，提供了类似Map的add、get、remove，另外提供了cut方法等

 */
FastMap.prototype = {
	/**
	 * 添加键值对
	 * 
	 * @param {String}
	 *            sKey 键

	 * @param {Object}
	 *            oValue 值

	 */
	add : function(sKey, oValue) {
		this.buf[sKey] = oValue;
	},

	/**
	 * 通过键取得值

	 * 
	 * @param {Object}
	 *            sKey 键

	 */
	get : function(sKey) {
		return this.buf[sKey];
	},

	/**
	 * 移除sKey所对应的属性

	 * 
	 * @param {String}
	 *            sKey
	 */
	remove : function(sKey) {
		delete(this.buf[sKey]);
	},

	/**
	 * 返回sKey对应的对象后移除sKey属性

	 * 
	 * @param {Object}
	 *            sKey
	 */
	cut : function(sKey) {
		var buf = this.buf;
		var result = buf[sKey];
		delete buf[sKey];
		return result;
	},

	/**
	 * 返回Map中的缓存对象， 得到这个对象后用for(var ele in buf){ someCode
	 * }去循环所有属性，在其中有_hashCode属性，要注意！
	 */
	getBuf : function() {
		return this.buf;
	},

	/**
	 * 得到buf中的对象数目
	 */
	size : function() {
		var buf = this.buf;
		var i = -1; // 有_hashCode属性，所以从-1开始

		for (var ele in buf) {
			i++;
		}
		return i;
	},

	/**
	 * toString方法
	 */
	toString : function() {
		var b = this.buf;
		var buf = [];
		for (var ele in b) {
			buf.push('Key: ');
			buf.push(ele);
			buf.push('  Value: ');
			buf.push(b[ele]);
			buf.push('\n');
		}
		return buf.join('');
	}
}