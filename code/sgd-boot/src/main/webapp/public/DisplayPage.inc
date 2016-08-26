<%!
public String getString(String id)
{
  return Common.getResourceStringLogger(Common.getGlobalRes(id));
}
public String GetJScriptString(String id)
{
  return Common.getResourceStringLogger(Common.getJScriptRes(id));
}

public String escape(String str)
{
	return Common.Escape(str);
}

public String unescape(String str)
{
	return Common.Unescape(str);
}
%>

<script language="javascript" defer>
var LoadWait = {
		
		_delNodes : function() {
			//window.top.jQuery(".popBackDiv").remove();
			//window.top.jQuery(".loading_mask_msg").remove();
			
			$(document.body).focus();
				if(jQuery(".popBackDiv").length > 0){
					jQuery(".popBackDiv").remove();
					jQuery(".loading_mask_msg").remove();
				}else{
					var $$ = window.top.jQuery;
					$$(".popBackDiv").remove();
					$$(".loading_mask_msg").remove();
				}
		},
	
		_create : function(text) {
			if (window["__maskCount"]++ > 0) {
				return;
			}
			//try{
				var topjQuery = window.top.jQuery;
				var localjQuery = null;
				var localWindow = null;
				if(topjQuery(".popBackDiv").length > 0){
					localjQuery = jQuery;
					localWindow = window;
				}else{
					localjQuery = topjQuery;
					localWindow = window.top;
				}
				var iWidth = localWindow.document.documentElement.clientWidth; 
				var iHeight = localWindow.document.documentElement.clientHeight; 
				var bgDivHeight = Math.max(localWindow.document.body.clientHeight, iHeight);
				
				var bgDiv = $("<div/>");
				bgDiv.css({
					width:iWidth+"px",
					height:bgDivHeight+"px"
				});
				bgDiv.addClass("popBackDiv");
				if("undefined" == typeof text || "" == text) text = "Loading...";
				var loadingDiv = $("<div/>");
				loadingDiv.addClass("loading_mask_msg");
				var textDiv = $("<div/>");
				textDiv.html(text);
				var divLeft = (localWindow.document.body.scrollLeft + localWindow.document.documentElement.clientWidth - 52)/2;
				var divTop = (localWindow.document.body.scrollTop + localWindow.document.documentElement.clientHeight - 16)/2;
				textDiv.css({
					top:divTop+"px",
					left:divLeft+"px",
					//width:"52px",
					height:"16px"
				});
				loadingDiv.append(textDiv);
				bgDiv.append(loadingDiv); 
				$(localWindow.document.body).append(bgDiv); 
			//} finally {
				
			//}
	
		},
	
		_free : function() {
			if (--window["__maskCount"] > 0) {
				return;
			}
	
			this._delNodes();
		}
	
	};
	
	window["__maskCount"] = 0;
	window.wait = function(proc, text) {
		LoadWait._create(text);
	
		window["__waitCall"] = function() {
			if (proc instanceof Function) {
				proc();
			} else {
				eval(proc);
			}
			LoadWait._free();
		}
	
		window.setTimeout(window["__waitCall"],100);
	}
	
	window.showMask = function(text) {
		LoadWait._create(text);
	}
	
	window.hideMask = function() {
		LoadWait._free();
	}
	
var ubosstoken = '<%=request.getSession(false).getAttribute("ubosscsrftoken")%>';
</script>
