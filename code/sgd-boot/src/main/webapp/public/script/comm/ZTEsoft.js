if (typeof ZTEsoft == "undefined") {
    var ZTEsoft = {};
};

ZTEsoft.namespace = function() {
    var a=arguments, o=null, i, j, d;
    for (i=0; i<a.length; i=i+1) {
        d=a[i].split(".");
        o=ZTEsoft;

        for (j=(d[0] == "ZTEsoft") ? 1 : 0; j<d.length; j=j+1) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }

    return o;
};

ZTEsoft.lang = {
    /**
     * Determines whether or not the provided object is an array
     * @method isArray
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isArray: function(obj) { // frames lose type, so test constructor string
        if (obj && obj.constructor && obj.constructor.toString().indexOf('Array') > -1) {
            return true;
        } else {
            return obj && ZTEsoft.lang.isObject(obj) && obj.constructor == Array;
        }
    },

    /**
     * Determines whether or not the provided object is a boolean
     * @method isBoolean
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isBoolean: function(obj) {
        return typeof obj == 'boolean';
    },
    
    /**
     * Determines whether or not the provided object is a function
     * @method isFunction
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isFunction: function(obj) {
        return typeof obj == 'function';
    },
        
    /**
     * Determines whether or not the provided object is null
     * @method isNull
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isNull: function(obj) {
        return obj == null;
    },
        
    /**
     * Determines whether or not the provided object is a legal number
     * @method isNumber
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isNumber: function(obj) {
        return typeof obj == 'number' && isFinite(obj);
    },
      
    /**
     * Determines whether or not the provided object is of type object
     * or function
     * @method isObject
     * @param {any} obj The object being testing
     * @return Boolean
     */  
    isObject: function(obj) {
        return typeof obj == 'object' || ZTEsoft.lang.isFunction(obj);
    },
        
    /**
     * Determines whether or not the provided object is a string
     * @method isString
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isString: function(obj) {
        return typeof obj == 'string';
    },
        
    /**
     * Determines whether or not the provided object is undefined
     * @method isUndefined
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isUndefined: function(obj) {
        return typeof obj == 'undefined';
    },

    /**
     * Determines whether or not the provided object is undefined
     * @method isDate
     * @param {any} obj The object being testing
     * @return Boolean
     */
    isDate: function(obj) {
        if (obj.constructor && obj.constructor.toString().indexOf('Date') > -1) {
            return true;
        } else {
            return ZTEsoft.lang.isObject(obj) && obj.constructor == Date;
        }
    }
};


ZTEsoft.env = ZTEsoft.env || {};

ZTEsoft.env.isUseActiveX = (typeof ActiveXObject != "undefined");
ZTEsoft.env.isUseDom = document.implementation && document.implementation.createDocument;
ZTEsoft.env.isUseXmlHttp = (typeof XMLHttpRequest != "undefined");

ZTEsoft.env.ARR_XMLHTTP_VERS = ["MSXML2.XmlHttp.6.0","MSXML2.XmlHttp.3.0"];
ZTEsoft.env.ARR_DOM_VERS = ["MSXML2.DOMDocument.6.0","MSXML2.DOMDocument.3.0"];
ZTEsoft.env.XMLHTTP_VER = null;
ZTEsoft.env.DOM_VER = null;

ZTEsoft.env.createRequest = function () {
    //if it natively supports XMLHttpRequest object
    if (this.isUseXmlHttp) {
        return new XMLHttpRequest();
    } else if (this.isUseActiveX) { //IE < 7.0 = use ActiveX
  
        if (!this.XMLHTTP_VER) {
            for (var i=0; i < this.ARR_XMLHTTP_VERS.length; i++) {
                try {
                    new ActiveXObject(this.ARR_XMLHTTP_VERS[i]);
                    this.XMLHTTP_VER = this.ARR_XMLHTTP_VERS[i];
                    break;
                } catch (oError) {                
                }
            }
        }
        
        if (this.XMLHTTP_VER) {
            return new ActiveXObject(this.XMLHTTP_VER);
        } else {
            throw new Error("Could not create XML HTTP Request.");
        }
    } else {
        throw new Error("Your browser doesn't support an XML HTTP Request.");
    }

};

/**
 * Creates an XML DOM document.
 * @return An XML DOM document.
 */
ZTEsoft.env.createDocument = function() /*:XMLDocument*/{

    if (ZTEsoft.env.isUseDom) {

        var oXmlDom = document.implementation.createDocument("","",null);

        oXmlDom.parseError = {
            valueOf: function () { return this.errorCode; },
            toString: function () { return this.errorCode.toString() }
        };
                
        oXmlDom.addEventListener("load", function () {
            this.__changeReadyState__(4);
        }, false);

        return oXmlDom;        
        
    } else if (this.isUseActiveX) {
        if (!this.DOM_VER) {
            for (var i=0; i < this.ARR_DOM_VERS.length; i++) {
                try {
                    new ActiveXObject(this.ARR_DOM_VERS[i]);
                    this.DOM_VER = this.ARR_DOM_VERS[i];
                    break;
                } catch (oError) {                
                }
            }
        }
        
        if (this.DOM_VER) {
            return new ActiveXObject(this.DOM_VER);
        } else {
            throw new Error("Could not create XML DOM document.");
        }
    } else {
        throw new Error("Your browser doesn't support an XML DOM document.");
    }

};