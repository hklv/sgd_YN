var Base64 = {
	base64EncodeChars : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",
	base64DecodeChars : [
　　-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
　　-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
　　-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
　　52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
　　-1,　0,　1,　2,　3,  4,　5,　6,　7,　8,　9, 10, 11, 12, 13, 14,
　　15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
　　-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
　　41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1],

	base64encode : function(str)
	{
	　　var out, i, len;
	　　var c1, c2, c3;
	　　len = str.length;
	　　i = 0;
	　　out = "";
	　　while(i < len)
	　　{
			c1 = str.charCodeAt(i++) & 0xff;
			if(i == len)
			{
	　　		out += this.base64EncodeChars.charAt(c1 >> 2);
	　　		out += this.base64EncodeChars.charAt((c1 & 0x3) << 4);
	　　		out += "==";
	　　		break;
			}
			c2 = str.charCodeAt(i++);
			if(i == len)
			{
	　　		out += this.base64EncodeChars.charAt(c1 >> 2);
	　　		out += this.base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
	　　		out += this.base64EncodeChars.charAt((c2 & 0xF) << 2);
	　　		out += "=";
	　　		break;
			}
			c3 = str.charCodeAt(i++);
			out += this.base64EncodeChars.charAt(c1 >> 2);
			out += this.base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
			out += this.base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));
			out += this.base64EncodeChars.charAt(c3 & 0x3F);
	　　}
	　　return out;
	},
	
	base64decode : function(str)
	{
	　　var c1, c2, c3, c4;
	　　var i, len, out;
	　　len = str.length;
	　　i = 0;
	　　out = "";
	　　while(i < len) {
		/* c1 */
		do
		{
	　　		c1 = this.base64DecodeChars[str.charCodeAt(i++) & 0xff];
		}while(i < len && c1 == -1);

		if(c1 == -1)
	　　		break;
		/* c2 */
		do
		{
		　　	c2 = this.base64DecodeChars[str.charCodeAt(i++) & 0xff];
		} while(i < len && c2 == -1);
		if(c2 == -1)
		　　	break;
		out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
		/* c3 */
		do {
	　　 	c3 = str.charCodeAt(i++) & 0xff;
	　　 	if(c3 == 61)
	　			return out;
		　　 c3 = this.base64DecodeChars[c3];
		 } while(i < len && c3 == -1);
		if(c3 == -1)
	　　 	break;
		out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
		 /* c4 */
		 do {
		　　 c4 = str.charCodeAt(i++) & 0xff;
		　　 if(c4 == 61)
			　return out;
		　　 c4 = this.base64DecodeChars[c4];
		 } while(i < len && c4 == -1);
		 if(c4 == -1)
		　　 break;
		 out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
	　　}
	　　return out;
	},
	
	utf16to8 : function(str) 
	{
	　　var out, i, len, c;
	　　out = "";
	　　len = str.length;
	　　for(i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
			　　 out += str.charAt(i);
	 		} else if (c > 0x07FF) {
			　　 out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
			　　 out += String.fromCharCode(0x80 | ((c >>　6) & 0x3F));
			　　 out += String.fromCharCode(0x80 | ((c >>　0) & 0x3F));
			} else {
			　　 out += String.fromCharCode(0xC0 | ((c >>　6) & 0x1F));
			　　 out += String.fromCharCode(0x80 | ((c >>　0) & 0x3F));
			}
	　　}
	
	　　return out;
	},
	
	utf8to16 : function(str) 
	{
	　　var out, i, len, c;
	　　var char2, char3;
	　　out = "";
	　　len = str.length;
	　　i = 0;
	　　while(i < len) {
		 c = str.charCodeAt(i++);
		 switch(c >> 4)
		 {
		　 case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
		　　 out += str.charAt(i-1);
		　　 break;
		　 case 12: case 13:
		　　 char2 = str.charCodeAt(i++);
		　　 out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
		　　 break;
		　 case 14:
		　　 char2 = str.charCodeAt(i++);
		　　 char3 = str.charCodeAt(i++);
		　　 out += String.fromCharCode(((c & 0x0F) << 12) |
		　　　　((char2 & 0x3F) << 6) |
		　　　　((char3 & 0x3F) << 0));
		　　 break;
		 }
	　　}
	　　return out;
	}	

};

var MD5 = {
	hex_chr : "0123456789abcdef",
	
	rhex : function(num)
	{
	  str = "";
	  for(j = 0; j <= 3; j++)
	    str += this.hex_chr.charAt((num >> (j * 8 + 4)) & 0x0F) +
	           this.hex_chr.charAt((num >> (j * 8)) & 0x0F);
	  return str;
	},
	
	/*
	 * Convert a string to a sequence of 16-word blocks, stored as an array.
	 * Append padding bits and the length, as described in the MD5 standard.
	 */
	str2blks_MD5 : function(str)
	{
	  nblk = ((str.length + 8) >> 6) + 1;
	  blks = new Array(nblk * 16);
	  for(i = 0; i < nblk * 16; i++) blks[i] = 0;
	  for(i = 0; i < str.length; i++)
	    blks[i >> 2] |= str.charCodeAt(i) << ((i % 4) * 8);
	  blks[i >> 2] |= 0x80 << ((i % 4) * 8);
	  blks[nblk * 16 - 2] = str.length * 8;
	  return blks;
	},
	
	/*
	 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
	 * to work around bugs in some JS interpreters.
	 */
	add : function(x, y)
	{
	  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
	  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
	  return (msw << 16) | (lsw & 0xFFFF);
	},
	
	/*
	 * Bitwise rotate a 32-bit number to the left
	 */
	rol : function(num, cnt)
	{
	  return (num << cnt) | (num >>> (32 - cnt));
	},
	
	/*
	 * These functions implement the basic operation for each round of the
	 * algorithm.
	 */
	cmn : function(q, a, b, x, s, t)
	{
	  return this.add(this.rol(this.add(this.add(a, q), this.add(x, t)), s), b);
	},
	
	ff : function (a, b, c, d, x, s, t)
	{
	  return this.cmn((b & c) | ((~b) & d), a, b, x, s, t);
	},
	
	gg : function (a, b, c, d, x, s, t)
	{
	  return this.cmn((b & d) | (c & (~d)), a, b, x, s, t);
	},
	
	hh : function(a, b, c, d, x, s, t)
	{
	  return this.cmn(b ^ c ^ d, a, b, x, s, t);
	},
	
	ii : function(a, b, c, d, x, s, t)
	{
	  return this.cmn(c ^ (b | (~d)), a, b, x, s, t);
	},
	
	/*
	 * Take a string and return the hex representation of its MD5.
	 */
	toMD5 : function(str)
	{
	  x = this.str2blks_MD5(str);
	  var a =  1732584193;
	  var b = -271733879;
	  var c = -1732584194;
	  var d =  271733878;
	
	  for(i = 0; i < x.length; i += 16)
	  {
	    var olda = a;
	    var oldb = b;
	    var oldc = c;
	    var oldd = d;
	
	    a = this.ff(a, b, c, d, x[i+ 0], 7 , -680876936);
	    d = this.ff(d, a, b, c, x[i+ 1], 12, -389564586);
	    c = this.ff(c, d, a, b, x[i+ 2], 17,  606105819);
	    b = this.ff(b, c, d, a, x[i+ 3], 22, -1044525330);
	    a = this.ff(a, b, c, d, x[i+ 4], 7 , -176418897);
	    d = this.ff(d, a, b, c, x[i+ 5], 12,  1200080426);
	    c = this.ff(c, d, a, b, x[i+ 6], 17, -1473231341);
	    b = this.ff(b, c, d, a, x[i+ 7], 22, -45705983);
	    a = this.ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);
	    d = this.ff(d, a, b, c, x[i+ 9], 12, -1958414417);
	    c = this.ff(c, d, a, b, x[i+10], 17, -42063);
	    b = this.ff(b, c, d, a, x[i+11], 22, -1990404162);
	    a = this.ff(a, b, c, d, x[i+12], 7 ,  1804603682);
	    d = this.ff(d, a, b, c, x[i+13], 12, -40341101);
	    c = this.ff(c, d, a, b, x[i+14], 17, -1502002290);
	    b = this.ff(b, c, d, a, x[i+15], 22,  1236535329);
	
	    a = this.gg(a, b, c, d, x[i+ 1], 5 , -165796510);
	    d = this.gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
	    c = this.gg(c, d, a, b, x[i+11], 14,  643717713);
	    b = this.gg(b, c, d, a, x[i+ 0], 20, -373897302);
	    a = this.gg(a, b, c, d, x[i+ 5], 5 , -701558691);
	    d = this.gg(d, a, b, c, x[i+10], 9 ,  38016083);
	    c = this.gg(c, d, a, b, x[i+15], 14, -660478335);
	    b = this.gg(b, c, d, a, x[i+ 4], 20, -405537848);
	    a = this.gg(a, b, c, d, x[i+ 9], 5 ,  568446438);
	    d = this.gg(d, a, b, c, x[i+14], 9 , -1019803690);
	    c = this.gg(c, d, a, b, x[i+ 3], 14, -187363961);
	    b = this.gg(b, c, d, a, x[i+ 8], 20,  1163531501);
	    a = this.gg(a, b, c, d, x[i+13], 5 , -1444681467);
	    d = this.gg(d, a, b, c, x[i+ 2], 9 , -51403784);
	    c = this.gg(c, d, a, b, x[i+ 7], 14,  1735328473);
	    b = this.gg(b, c, d, a, x[i+12], 20, -1926607734);
	
	    a = this.hh(a, b, c, d, x[i+ 5], 4 , -378558);
	    d = this.hh(d, a, b, c, x[i+ 8], 11, -2022574463);
	    c = this.hh(c, d, a, b, x[i+11], 16,  1839030562);
	    b = this.hh(b, c, d, a, x[i+14], 23, -35309556);
	    a = this.hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
	    d = this.hh(d, a, b, c, x[i+ 4], 11,  1272893353);
	    c = this.hh(c, d, a, b, x[i+ 7], 16, -155497632);
	    b = this.hh(b, c, d, a, x[i+10], 23, -1094730640);
	    a = this.hh(a, b, c, d, x[i+13], 4 ,  681279174);
	    d = this.hh(d, a, b, c, x[i+ 0], 11, -358537222);
	    c = this.hh(c, d, a, b, x[i+ 3], 16, -722521979);
	    b = this.hh(b, c, d, a, x[i+ 6], 23,  76029189);
	    a = this.hh(a, b, c, d, x[i+ 9], 4 , -640364487);
	    d = this.hh(d, a, b, c, x[i+12], 11, -421815835);
	    c = this.hh(c, d, a, b, x[i+15], 16,  530742520);
	    b = this.hh(b, c, d, a, x[i+ 2], 23, -995338651);
	
	    a = this.ii(a, b, c, d, x[i+ 0], 6 , -198630844);
	    d = this.ii(d, a, b, c, x[i+ 7], 10,  1126891415);
	    c = this.ii(c, d, a, b, x[i+14], 15, -1416354905);
	    b = this.ii(b, c, d, a, x[i+ 5], 21, -57434055);
	    a = this.ii(a, b, c, d, x[i+12], 6 ,  1700485571);
	    d = this.ii(d, a, b, c, x[i+ 3], 10, -1894986606);
	    c = this.ii(c, d, a, b, x[i+10], 15, -1051523);
	    b = this.ii(b, c, d, a, x[i+ 1], 21, -2054922799);
	    a = this.ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);
	    d = this.ii(d, a, b, c, x[i+15], 10, -30611744);
	    c = this.ii(c, d, a, b, x[i+ 6], 15, -1560198380);
	    b = this.ii(b, c, d, a, x[i+13], 21,  1309151649);
	    a = this.ii(a, b, c, d, x[i+ 4], 6 , -145523070);
	    d = this.ii(d, a, b, c, x[i+11], 10, -1120210379);
	    c = this.ii(c, d, a, b, x[i+ 2], 15,  718787259);
	    b = this.ii(b, c, d, a, x[i+ 9], 21, -343485551);
	
	    a = this.add(a, olda);
	    b = this.add(b, oldb);
	    c = this.add(c, oldc);
	    d = this.add(d, oldd);
	  }
	  return this.rhex(a) + this.rhex(b) + this.rhex(c) + this.rhex(d);
	}
}
