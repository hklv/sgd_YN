var RegExpHelper = {
	POS_INTEGER : /^[1-9]\d*$/,
	INTEGER : /^(-|\+)?\d+$/,
	FLOAT : /^(-?\d+)(\.\d+)?$/,
	NUMBER_AND_CHAR : /^[A-Za-z0-9]+$/,
	EMAIL : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	IP_ADDRESS : /(\d+)\.(\d+)\.(\d+)\.(\d+)/,
	NUMBER : /^[0-9]\d*$/,
	
	isMatch : function(str,regexp) {
		if(!str||!regexp){
			return false;
		}else {
			return regexp.test(str);
		}
	}
}
