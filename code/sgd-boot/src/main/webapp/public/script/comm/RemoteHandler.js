function getDomainName()
{
	var url = document.URL;
	var index = url.indexOf("//");
	index = url.indexOf("/",index+2);

	return url.substring(0,index);
};

var g_baseAppName = "callservice.do";
var g_baseUrlDomain = g_GlobalInfo.WebRoot + g_baseAppName;
var g_urlMap = g_baseUrlDomain;
var xmlHttpResp;
var reCallFunc;
var isAsyQuery = false;

var Protocol = {
	Version : "V1.0",
	XML_HEAD : "version=\"1.0\" encoding=\"UTF-8\"",
	XML_ROOT : "zsmart",
	XML_SERVICE_NAME : "ServiceName",
	XML_DATA_ROOT : "Data",
	XML_ERROR_ROOT : "Error",
	XML_ARRAY_ITEM : "I",
	XML_DYN_QUERY_RESULT : "DR",
	XML_TAG_ARRAYLIST : "AL"
};

function addParam(fieldName,fieldValue,inputParams)
{
	if ( inputParams == null )
	{
		inputParams = new Array();
	}
	inputParams[inputParams.length]= {};
	inputParams[inputParams.length-1][fieldName] = fieldValue;
};

var DataSetFilter = {
	addOrderField : function (fieldName,dataSetFilter)
	{
		if (dataSetFilter.OrderFields == null)
		{
			dataSetFilter.OrderFields =new Array();
		}
		dataSetFilter.OrderFields[dataSetFilter.OrderFields.length] = fieldName;
	},
	
	addShowField : function (fieldName,dataSetFilter)
	{
		if (dataSetFilter.ShowFields == null)
		{
			dataSetFilter.ShowFields =new Array();
		}
		dataSetFilter.ShowFields[dataSetFilter.ShowFields.length] = fieldName;
	}
	
};

function Packager(version){
    this.version = version;
};

Packager.prototype.getObjectType = function(obj)
{
	if(ZTEsoft.lang.isNull(obj)) return 'n';
	
	if(ZTEsoft.lang.isArray(obj))
	{
		return 'a';
	}else if(ZTEsoft.lang.isFunction(obj))
	{
		return 'm';
	}else if(ZTEsoft.lang.isObject(obj))
	{
		return 'o';
	}else
	{
	    return 's';
	}
};

var _packager = new Packager('R11');
_packager.packageObject = function (elm,type,arg)
{
	if (type == 'm' || type == 'n')
		return;

	switch (type) {
	case 'o':
		for ( var key in arg) {
			var child = arg[key];
			var subtype = this.getObjectType(child);
			if (subtype == 'm' || subtype == 'n')
				continue;

			var childElm = elm.ownerDocument.createElement(subtype + key);
			elm.appendChild(childElm);
			this.packageObject(childElm, subtype, child);
		}
		break;
	case 's':
		elm.appendChild(elm.ownerDocument.createTextNode(arg.toString()));
		break;
	case 'a':
		for ( var i = 0; i < arg.length; i++) {
			var child = arg[i];
			var subtype = this.getObjectType(child);
			if (subtype == 'm' || subtype == 'n')
				continue;

			var childElm = elm.ownerDocument.createElement(subtype
					+ Protocol.XML_ARRAY_ITEM);
			elm.appendChild(childElm);
			this.packageObject(childElm, subtype, child);
		}
		break;
	}
};

_packager.getObjectFromXml = function(elm, type){
    var obj = {};
    var nodes = elm.children();
    var len = nodes.length;
    if (len == 0) {// 如果此节点没有子节点了，说明节点到头了，则一定要返回一个text值了
        return elm.text();
    }
    else {// 有子节点则遍历此字节点
        nodes.each(function(){
            var key = this.tagName; // 取得节点的key
            if (key == Protocol.XML_TAG_ARRAYLIST &&
            (type == null || type == "2" || type == 2)) {// 特殊情况的时候（节点为AL的时候）
                var childNodes = jQuery(this).children();// childNodes为AL下面的子元素集合
                childNodes.each(function(){// 遍历AL下面的每个子元素
                    var childKey = this.tagName;// this代表每个DR，childKey表示DR
                    var childValue = _packager.getObjectFromXml(jQuery(this), type);
                    if (obj[childKey]) {
                        if (ZTEsoft.lang.isArray(obj[childKey])) {
                            obj[childKey].push(childValue);
                        }
                        else {
                            var arr = [];
                            arr.push(obj[key]);
                            arr.push(childValue);
                            obj[childKey] = arr;
                        }
                    }
                    else {
                        var arr = [];
                        arr.push(childValue);
                        obj[childKey] = arr;
                    }
                });
            }
            else {
                var value = _packager.getObjectFromXml(jQuery(this), type);
                if (obj[key]) {
                    if (ZTEsoft.lang.isArray(obj[key])) {
                        obj[key].push(value);
                    }
                    else {
                        var arr = [];
                        arr.push(obj[key]);
                        arr.push(value);
                        obj[key] = arr;
                    }
                }
                else {
                    obj[key] = value;
                }
            }
        });
    }
    return obj;
};

_packager.unpackageException = function (xmlNode) 
{
	var ex = {};
	ex.ID = jQuery("ID",xmlNode).text();
	ex.Time = jQuery("Time",xmlNode).text();
	ex.Type = jQuery("Type",xmlNode).text();
	ex.Code = jQuery("Code",xmlNode).text();
	ex.Desc = jQuery("Desc",xmlNode).text();
	ex.Trace =jQuery("Trace",xmlNode).text();
	return ex;
};

_packager.QueryResultToObject = function(result)
{
	if(!result) return null; 
	
	var arrTable = result[Protocol.XML_DYN_QUERY_RESULT];
	if(arrTable != null && ZTEsoft.lang.isArray(arrTable)){
		return arrTable;
	}
	else{
		var arr = [];
		if(arrTable){
			arr[0] = arrTable;
		}
		return arr;
	}
};

function onReadyState(obj)
{
	Remote.handleStateChange(obj);
};

function xmlDomToXmlString(xmlDom) {
	var xmlStr = xmlDom.xml;
	if (!xmlStr) {
		xmlStr = (new XMLSerializer()).serializeToString(xmlDom);
	}
	return xmlStr;
};
function callRemoteFunction(serviceName,data,filter){
	return Remote.callRemoteFunction(serviceName,data,filter);
}
function callRemoteFunctionAsync(serviceName,data,filter,callBack){
	return Remote.callRemoteFunctionAsync2(serviceName,data,filter,callBack);
}
function callRemoteAddLogFunction(partyCode,eventType,eventSrc,eventCode,comments)
{
	Remote.callRemoteAddLogFunction(partyCode,eventType,eventSrc,eventCode,comments);
}

function callRemoteQueryFunction(queryName,argObj,filter)
{
	return Remote.callRemoteQueryFunction(queryName,argObj,filter);
}
function callRemoteQueryCountFunction(queryName,argObj)
{
	return Remote.callRemoteQueryCountFunction(queryName,argObj);
}
var Remote = {
	sendXml : function (url,strXml)
	{
		var args = {"ubosscsrftoken":ubosstoken?ubosstoken:""};
		
		var returnValue=null;
		jQuery.ajax({
			processData : false,
			async : false,
			type : "POST",
			contentType : "text/xml",
			headers : args,
			url : url,
			data : strXml,
			dataType : "xml",
			success : function(msg) {
				returnValue = msg;
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if (XMLHttpRequest.status != 200) {
					throw "Network issue or remote server issue";
				} else {
					throw "Exception";
				}
			}
		});
		return returnValue;
	},
	sendXmlAsync2 : function (url,strXml,callBack)
	{
		var args = {"ubosscsrftoken":ubosstoken?ubosstoken:""};
		
		var returnValue=null;
		jQuery.ajax({
			processData : false,
			async : true,
			type : "POST",
			contentType : "text/xml",
			headers : args,
			url : url,
			data : strXml,
			dataType : "xml",
			success : function(msg) {
						
				var $retValJDOM = jQuery(msg);
				var $rtNode = jQuery("zsmart Return", $retValJDOM).first();
				var retCode = $rtNode.attr("Code");
				var $dataNode = jQuery("zsmart Data", $retValJDOM).first();
				switch (retCode) {
				case "0": // Code属性值为0表示返回成功
					var type = jQuery("zsmart Type", $retValJDOM).first().text();
					if (jQuery.trim(type) == "")
						type = null;
					var obj = _packager.getObjectFromXml($dataNode, type);
				
					if(callBack){
						callBack(obj);
					}
					
				   break;
				case "-1":// Code属性为-1表示返回不成功
					throw _packager.unpackageException($rtNode);break;
				default:// 返回一个错误
					throw "Remote server returns invalid xml";
				}
				
				
			},
			complete:function(XMLHttpRequest, textStatus){
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if (XMLHttpRequest.status != 200) {
					throw "Network issue or remote server issue";
				} else {
					throw "Exception";
				}
			}
		});
		//return returnValue;
	},	
	callRemoteFunction : function (serviceName,data,filter)
	{
		if(filter!=null)
		{
			data.uboss_query_page = new Object();
			data.uboss_query_page.page_index = filter.PageIndex;
			data.uboss_query_page.page_size = filter.PageLen;
			if(filter.OrderFields!=null)
			{
				data.uboss_query_page.order = filter.OrderFields;
			}
			if(filter.ShowFields!=null)
			{
				data.uboss_query_page.fields = filter.ShowFields;
			}			
	    }
		
		if(data == null)
	    {
	    	data = new Object();
	    	data.zsmart_referer_url = window.location.href;
	    }
	    else
	    {
	    	data.zsmart_referer_url = window.location.href;
	    }
		
		var url = g_baseUrlDomain + "?service=" + serviceName;
		var xmlDoc = jQuery.parseXML("<xml></xml>");
		var docElm = xmlDoc.createElement(Protocol.XML_ROOT);
		var serviceNameElm = xmlDoc.createElement(Protocol.XML_SERVICE_NAME);
		serviceNameElm.appendChild(xmlDoc.createTextNode(serviceName));
		docElm.appendChild(serviceNameElm);
		var dataElm = xmlDoc.createElement(Protocol.XML_DATA_ROOT);
		var type = _packager.getObjectType(data);
		if (type == 'a' && arg.length == 0) {
			dataElm.setAttribute("t", 'o');
			_packager.packageObject(dataElm, 'o', null);
		} else {
			_packager.packageObject(dataElm, type, data);
		}
		docElm.appendChild(dataElm);
		var retValDoc = this.sendXml(url,
				("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ xmlDomToXmlString(docElm) + "\r\n"));
		
		var $retValJDOM = jQuery(retValDoc);
		var $rtNode = jQuery("zsmart Return", $retValJDOM).first();
		var retCode = $rtNode.attr("Code");
		var $dataNode = jQuery("zsmart Data", $retValJDOM).first();
		switch (retCode) {
		case "0": {// Code属性值为0表示返回成功
			var type = jQuery("zsmart Type", $retValJDOM).first().text();
			if (jQuery.trim(type) == "")
				type = null;
			var obj = _packager.getObjectFromXml($dataNode, type);
			return obj;
		}
		case "-1":// Code属性为-1表示返回不成功
			throw _packager.unpackageException($rtNode);
		default:// 返回一个错误
			throw "Remote server returns invalid xml";
		}
	},
	callRemoteFunctionAsync2 : function (serviceName,data,filter,callBack)
	{
		if(filter!=null)
		{
			data.uboss_query_page = new Object();
			data.uboss_query_page.page_index = filter.PageIndex;
			data.uboss_query_page.page_size = filter.PageLen;
			if(filter.OrderFields!=null)
			{
				data.uboss_query_page.order = filter.OrderFields;
			}
			if(filter.ShowFields!=null)
			{
				data.uboss_query_page.fields = filter.ShowFields;
			}			
	    }
		
		if(data == null)
	    {
	    	data = new Object();
	    	data.zsmart_referer_url = window.location.href;
	    }
	    else
	    {
	    	data.zsmart_referer_url = window.location.href;
	    }
		
		var url = g_baseUrlDomain + "?service=" + serviceName;
		var xmlDoc = jQuery.parseXML("<xml></xml>");
		var docElm = xmlDoc.createElement(Protocol.XML_ROOT);
		var serviceNameElm = xmlDoc.createElement(Protocol.XML_SERVICE_NAME);
		serviceNameElm.appendChild(xmlDoc.createTextNode(serviceName));
		docElm.appendChild(serviceNameElm);
		var dataElm = xmlDoc.createElement(Protocol.XML_DATA_ROOT);
		var type = _packager.getObjectType(data);
		if (type == 'a' && arg.length == 0) {
			dataElm.setAttribute("t", 'o');
			_packager.packageObject(dataElm, 'o', null);
		} else {
			_packager.packageObject(dataElm, type, data);
		}
		docElm.appendChild(dataElm);
		this.sendXmlAsync2(url,
				("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ xmlDomToXmlString(docElm) + "\r\n"),callBack);
	},
	callRemoteAddLogFunction : function(partyCode,eventType,eventSrc,eventCode,comments)
	{
		if(g_GlobalInfo.AuditLog!="TRUE")
		{
			return ;
		}
		var strEventType = "";
		switch (eventType)
		{
			case "A":
				strEventType = "Add";
				break;
			case "M":
				strEventType = "Modify";
				break;
			case "D":
				strEventType = "Delete";
				break;
			default:
				strEventType = eventType;
		}

		var objEventSrcMap = null;
		try
		{
			objEventSrcMap = g_EventSrcMap_en_US;
			
			var strEventSrc = objEventSrcMap[eventSrc];
			var obj = {};
			obj.PARTY_CODE = partyCode;
			obj.PARTY_TYPE = "A";//前台
			obj.EVENT_TYPE = strEventType;
			obj.EVENT_SRC = strEventSrc;
			obj.EVENT_CODE = eventCode;
			if(comments==null ||comments=="")
				obj.COMMENTS = g_GlobalInfo.LogExtComments ||"";
			else
				obj.COMMENTS = comments+"|"+(g_GlobalInfo.LogExtComments||"");
			obj.SRC_IP = REMOTE_IP;
			obj.DEST_IP = HOST_IP;
			var retObj = this.callRemoteFunction("AddLog",obj);
			return retObj;
		}
		catch (e)
		{
			return null;
		}
	},
	callRemoteQueryFunction : function(queryName,argObj,filter)
	{
		var data = {};
		var len = 0;
		if(argObj!=null) len = argObj.length;

		for(var i=0;i<len;i++)
		{
			jQuery.extend(data,argObj[i]);
		}
		if(filter!=null)
		{
			data.uboss_query_page = new Object();
			data.uboss_query_page.page_index = filter.PageIndex;
			data.uboss_query_page.page_size = filter.PageLen;
			if(filter.OrderFields!=null)
			{
				data.uboss_query_page.order = filter.OrderFields;
			}
			if(filter.ShowFields!=null)
			{
				data.uboss_query_page.fields = filter.ShowFields;
			}			
	    }
	    var retObj = this.callRemoteFunction(queryName,data);
		return _packager.QueryResultToObject(retObj);
	},
	callRemoteQueryCountFunction : function(queryName,argObj)
	{
		var data = {};
		var len = 0;
		if(argObj!=null) len = argObj.length;

		for(var i=0;i<len;i++)
		{
			jQuery.extend(data,argObj[i]);
		}
		data.uboss_query_page = new Object();
		data.uboss_query_page.count = true;
	    var retObj = this.callRemoteFunction(queryName,data);
		var ret = _packager.QueryResultToObject(retObj);
		if(ret!=null&&ret[0]!=null)
		{
			return ret[0].CNT;
		} 
		else
		{
			return 0;
		}
	},
	//异步调用
	
	sendXmlAsync : function(url,strXml)
	{
		xmlHttpResp = ZTEsoft.env.createRequest(); 
		xmlHttpResp.open("POST",url,true);
		//var a = new Object();
		xmlHttpResp.onreadystatechange= Remote.handleStateChange;
		xmlHttpResp.send(strXml);
	},
	
	
	
	//处理返回结果
	handleStateChange : function()
	{
		try
		{
			if (xmlHttpResp.readyState == 4)
			{
			   if(xmlHttpResp.status==200)
		  	   {
					var retVal = xmlHttpResp.responseText;
					
					//unpackage return value
					var xmlDoc = ZTEsoft.env.createDocument();
					if(!xmlDoc.loadXML(retVal))
						throw "The format of remote server return is not an Xml";
			
					var docElm = xmlDoc.documentElement;
					var rtNode = docElm.selectSingleNode("//uboss/Return");
					var retCode=rtNode.getAttributeNode("Code").nodeValue;
					
					var dataNode = docElm.selectSingleNode("//uboss/Data");
					switch(retCode)
					{
					case "0":
					{
						var type = docElm.selectSingleNode("//uboss/Type").nodeValue;
						var retObj = _packager.getObjectFromXml(dataNode,type);
						if(isAsyQuery == true)
						{
							retObj = _packager.QueryResultToObject(retObj);
						}
						Remote.onComplete(retObj);
						hideMask();
						return;
					}
					case "-1":
					{
						var retObj = _packager.unpackageException(rtNode);
						Remote.onError(retObj);
						hideMask();
						return;
					}
					default:
						throw "Remote server returns invalid xml";
					}
			    }
			}
		}
		catch(e)
		{
			hideMask();
			throw e;
		}
	},
	
	onComplete : function(obj)
	{
		if(reCallFunc != null && reCallFunc.onComplete != null)
		{
			reCallFunc.onComplete(obj);
		}
	},
	
	onError : function(obj)
	{
		if(reCallFunc != null && reCallFunc.onError != null)
		{
			reCallFunc.onError(obj);
		}
	},
	
	callRemoteFunctionAsync: function(callAsync,serviceName,data,filter)
	{
		try
		{
			showMask("");
			if(filter!=null)
			{
				data.uboss_query_page = new Object();
				data.uboss_query_page.page_index = filter.PageIndex;
				data.uboss_query_page.page_size = filter.PageLen;
				if(filter.OrderFields!=null)
				{
					data.uboss_query_page.order = filter.OrderFields;
				}
				if(filter.ShowFields!=null)
				{
					data.uboss_query_page.fields = filter.ShowFields;
				}			
		    }
			isAsyQuery = false;
		    var url = g_baseUrlDomain + "?service=" + serviceName;
			reCallFunc = callAsync;
			//package arguments
			var xmlDoc = ZTEsoft.env.createDocument(); 
			var pi = xmlDoc.createProcessingInstruction("xml", Protocol.XML_HEAD);
			xmlDoc.appendChild(pi);
		
			var docElm = xmlDoc.appendChild(xmlDoc.createElement(Protocol.XML_ROOT));
			var serviceNameElm = xmlDoc.createElement(Protocol.XML_SERVICE_NAME);
			serviceNameElm.text = serviceName;
			docElm.appendChild(serviceNameElm);
			
			var dataElm = xmlDoc.createElement(Protocol.XML_DATA_ROOT);
			var type = _packager.getObjectType(data);
			
			if(type=='a'&&arg.length==0)
	        {
	            dataElm.setAttribute("t",'o');
	            _packager.packageObject(dataElm,'o',null);
	        }else
	        {
	            _packager.packageObject(dataElm,type,data);
	        }
	        docElm.appendChild(dataElm);
	
			//send to remote
		    this.sendXmlAsync(url,xmlDoc.xml);
		}
		catch(e)
		{
			hideMask();
			throw e;
		}
	},
	
	callRemoteQueryFunctionAsync : function(callAsync,serviceName,argObj,filter)
	{
		try{
			isAsyQuery = true;
			var data = {};
			var len = 0;
			if(argObj!=null) len = argObj.length;
	
			for(var i=0;i<len;i++)
			{
				jQuery.extend(data,argObj[i]);
			}
			if(filter!=null)
			{
				data.uboss_query_page = new Object();
				data.uboss_query_page.page_index = filter.PageIndex;
				data.uboss_query_page.page_size = filter.PageLen;
				if(filter.OrderFields!=null)
				{
					data.uboss_query_page.order = filter.OrderFields;
				}
				if(filter.ShowFields!=null)
				{
					data.uboss_query_page.fields = filter.ShowFields;
				}			
		    }
			var url = g_baseUrlDomain + "?service=" + serviceName;
			reCallFunc = callAsync;
			//package arguments
			var xmlDoc = ZTEsoft.env.createDocument(); 
			var pi = xmlDoc.createProcessingInstruction("xml", Protocol.XML_HEAD);
			xmlDoc.appendChild(pi);
		
			var docElm = xmlDoc.appendChild(xmlDoc.createElement(Protocol.XML_ROOT));
			var serviceNameElm = xmlDoc.createElement(Protocol.XML_SERVICE_NAME);
			serviceNameElm.text = serviceName;
			docElm.appendChild(serviceNameElm);
			
			var dataElm = xmlDoc.createElement(Protocol.XML_DATA_ROOT);
			var type = _packager.getObjectType(data);
			
			if(type=='a'&&arg.length==0)
	        {
	            dataElm.setAttribute("t",'o');
	            _packager.packageObject(dataElm,'o',null);
	        }else
	        {
	            _packager.packageObject(dataElm,type,data);
	        }
	        docElm.appendChild(dataElm);
	
			//send to remote
		    this.sendXmlAsync(url,xmlDoc.xml);
		}
		catch(e)
		{
			hideMask();
			throw e;
		}
	}
};

