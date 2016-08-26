/**
 * 显示对话框
 * @param exeption  异常对象
 * @param message   异常消息
 * @param type      类型
 * @param title     对话框标题
 */
var _root="";//图片路径问题
function setPopWindowRoot(_r){
	_root=_r;
}
function showException( message, exeption,func, type, title) {
	if (!exeption) {
		exeption = {type: 1, message: "Unkonw Error."};
	}
	if (!type) {
		type = exeption.type;
	}
	if (!message) {
		message = exeption.Desc;
	}
	switch (type) {
	case 1:
		showError(message, exeption, func, title);
		break;
	case 2:
		showMessage(message, func, title);
		break;
	default:
		showError(message, exeption, func, title);
	}
}

/**
 * 
 * @param message
 * @param func
 * @param title
 */
function showError(message, exeption, func, title) {
	if (!title) {
		title = "Error";
	}
	var errMsg = message;
	if(exeption != null)
	{
		if(exeption.Desc != null)
		{
			errMsg = "@@" + exeption.Desc;
		}
		else if(exeption.message != null)
		{
			errMsg = "@@" + exeption.message;	
		}
		else if(exeption.description != null)
		{
			errMsg = "@@" + exeption.description;
		}
		else if(typeof(e) === "string")
		{
			errMsg = "@@" + exeption;	
		}
	}
	messagebox('error', title, errMsg, func);
}

/**
 * 
 * @param message
 * @param func
 * @param title
 */
function showAlarm(message, exeption, func, title) {
	if (!title) {
		title = "";
	}
	var errMsg = message;
	if(exeption != null)
	{
		if(exeption.Desc != null)
		{
			errMsg = "@@" + exeption.Desc;
		}
		else if(exeption.message != null)
		{
			errMsg = "@@" + exeption.message;	
		}
		else if(exeption.description != null)
		{
			errMsg = "@@" + exeption.description;
		}
		else if(typeof(e) === "string")
		{
			errMsg = "@@" + exeption;	
		}
	}
	messagebox('alarm', title, errMsg, func);
}

/**
 * 
 * @param message
 * @param func
 * @param title
 */
function showMessage(message, func, title,type) {
	if (!title) {
		title = "";
	}
	if(type==2){
		messagebox('successfully', title, message, func);
	}else{
		messagebox('info', title, message, func);
	}
	
	
}

/**
 * 
 * @param message
 * @param func
 * @param title
 */
function showWarning(message, func, title) {
	if (!title) {
		title = "Warning";
	}
	messagebox('warning', title, message, func);
}

/**
 * 
 * @param message
 * @param okfunc
 * @param cancelfunc
 * @param title
 */
function showConfirm(message, okfunc, cancelfunc, title) {
	if (!title) {
		title = "Confirm";
	}
	messagebox('confirm', title, message, okfunc, cancelfunc);
}

function messagebox(boxtype, title, msg, okfunc, cancelfunc) {
	
	window.xifscroll = $(document.body).css("overflow-x");
	window.yifscroll = $(document.body).css("overflow-y");
	 $(document.body).css("overflow-x","hidden");
	 $(document.body).css("overflow-y","hidden");
	var jParent = window.top.jQuery;
	var imgsrc = '';
	if (boxtype == 'info') {
		imgsrc = 'skins/default/zh_CN/img/tips/messager_info.gif';
	}
	if (boxtype == 'warning') {
		imgsrc = 'skins/default/zh_CN/img/tips/messager_warning.gif';
	}
	if (boxtype == 'error') {
		imgsrc = 'skins/default/zh_CN/img/tips/messager_error.gif';
	}
	if(boxtype=='alarm'){//alarm
		imgsrc = "skins/default/zh_CN/img/tips/message_alarm.png";
	}
	if (boxtype == 'confirm') {
		imgsrc = 'skins/default/zh_CN/img/tips/messager_question.gif';
	}
	if(boxtype=='successfully'){//successfully
		imgsrc = "skins/default/zh_CN/img/tips/successfully.jpg";
	}
	
	imgsrc = _root+imgsrc;
	msg = doI18n(msg);
	
	jQuery("#dialog-confirm").remove();
	var confirmdiv = "";
	if(boxtype=='alarm'){
		confirmdiv = "<div id='dialog-confirm' title='"
			+ title
			+ "'><table border=0 cellSpacing=0 cellPadding=0 width='100%'><tbody>"
			+ "<tr style='height:115px;'><td width=5%></td><td id=imgMsg>"
			+ "<img alt='' src="
			+ imgsrc
			+ "></td><td id=txtMsg width='95%'><span style='color:red;font-size:20px;'>"
			+ msg + "</span></td><td width=20></td></tr></tbody></table></div>";
	}else{
		confirmdiv = "<div id='dialog-confirm' title='"
			+ title
			+ "'><table border=0 cellSpacing=0 cellPadding=0 width='100%'><tbody>"
			+ "<tr style='height:115px;'><td width=5%></td><td id=imgMsg>"
			+ "<img alt='' src="
			+ imgsrc
			+ "></td><td id=txtMsg width='95%' class=''>"
			+ msg + "</td><td width=20></td></tr></tbody></table></div>";
	}
	
	
	//这里需要判断本页面是否是弹出页面，不是的话需要将整个屏幕遮罩起来
/*	if(jQuery(".iframepopwinnew",window.parent.document.body).length >0 ){
		jQuery(document.body).append(confirmdiv);
		_disablePopBar();
	}else if(jQuery(".iframepopwinnew",window.parent.parent.document.body).length >0 ){//弹出框中嵌的iframe里面调用showmessage
		jQuery(window.parent.document.body).append(confirmdiv);
		_disablePopBar();
	}else{
		jQuery(window.top.document.body).append(confirmdiv);
	}

	var jqueryLocal ;
	if(jQuery(".iframepopwinnew",window.parent.document.body).length >0 ){
		jqueryLocal = jQuery;
	}else if(jQuery(".iframepopwinnew",window.parent.parent.document.body).length >0 ){  //弹出框中嵌的iframe里面调用showmessage
		jqueryLocal = window.parent.jQuery;
	}
	else{
		jqueryLocal = jParent;
	}*/
	
	if(jQuery(".iframepopwinnew",window.parent.document.body).length >0 ){
		jQuery(document.body).append(confirmdiv);
		//_disablePopBar();
	}else{
		jQuery(window.parent.document.body).append(confirmdiv);
	//	_disablePopBar();
	}

	var jqueryLocal ;
	if(jQuery(".iframepopwinnew",window.parent.document.body).length >0 ){
		jqueryLocal = jQuery;
	}else{  //弹出框中嵌的iframe里面调用showmessage
		jqueryLocal = window.parent.jQuery;
	}
	
	if (boxtype == 'confirm') {
		jqueryLocal("#dialog-confirm").dialog({
			resizable : false,
			height : 200,
			width:400,
			modal : true, // 是否锁屏
			buttons : {
				OK : function() {
					jqueryLocal(this).dialog("close");
					jqueryLocal("#dialog-confirm").remove();
					jQuery(document.body).css("overflow-x",window.xifscroll);
					jQuery(document.body).css("overflow-y",window.yifscroll);
					_enablePopBar();
					if (typeof okfunc == 'function') {
						okfunc();  
					}
					return true;
				},
				CANCEL:function() {
					jqueryLocal(this).dialog("close");
					jqueryLocal("#dialog-confirm").remove();
					jQuery(document.body).css("overflow-x",window.xifscroll);
					jQuery(document.body).css("overflow-y",window.yifscroll);
					_enablePopBar();
					if (cancelfunc) {
						cancelfunc();
					}
					return false;
				}
			},
			open:function(event,ui){
				jqueryLocal(".ui-dialog").addClass("popRadius")
				.css("border","5px solid #525252");
				
			},
			close:function(event,ui){
				jqueryLocal("#dialog-confirm").remove();
			}
		});
	} else if (boxtype == 'alarm'){
		jqueryLocal("#dialog-confirm").dialog({
			resizable : false,
			width:400,
			height : 200,
			modal : true, // 是否锁屏
			buttons : {
				Handle: function() {
					jqueryLocal(this).dialog("close");
					jqueryLocal("#dialog-confirm").remove();
					jQuery(document.body).css("overflow-x",window.xifscroll);
					jQuery(document.body).css("overflow-y",window.yifscroll);
					
					_enablePopBar();
					if (typeof okfunc == 'function') {
						okfunc();
					}
					return true;
				}
			},
			open:function(event,ui){
				jqueryLocal(".ui-dialog").addClass("popRadius");
			},
			close:function(event,ui){
				jqueryLocal("#dialog-confirm").remove();
			}
		});
	}else {
		jqueryLocal("#dialog-confirm").dialog({
			resizable : false,
			width:400,
			height : 200,
			modal : true, // 是否锁屏
			buttons : {
				OK: function() {
					jqueryLocal(this).dialog("close");
					jqueryLocal("#dialog-confirm").remove();
					jQuery(document.body).css("overflow-x",window.xifscroll);
					jQuery(document.body).css("overflow-y",window.yifscroll);
					
					_enablePopBar();
					if (typeof okfunc == 'function') {
						okfunc();
					}
					return true;
				}
			},
			open:function(event,ui){
				jqueryLocal(".ui-dialog").addClass("popRadius");
			},
			close:function(event,ui){
				jqueryLocal("#dialog-confirm").remove();
			}
		});
	}
	$(".ui-dialog").css("background-color","#FFFFFF");
}

function _disablePopBar(){
	var poptd = window.parent.jQuery(".popTitleBar");
	var popclosetd = window.parent.jQuery("#closetd");
	poptd.addClass("layout_title_black");
	poptd.find("span").addClass("layout_title_black");
	popclosetd.addClass("layout_title_black").unbind("click");
	popclosetd.find("span").addClass("layout_title_black").removeClass("colsetd-span");
}

function _enablePopBar(){
	var poptd = window.parent.jQuery(".popTitleBar");
	var popclosetd = window.parent.jQuery("#closetd");
	poptd.removeClass("layout_title_black");
	poptd.find("span").removeClass("layout_title_black");
	popclosetd.removeClass("layout_title_black").bind("click",function(){window.parent.closepopwin("x");});
	popclosetd.find("span").removeClass("layout_title_black").addClass("colsetd-span");
}


function doI18n(message)
{
	var begin = 0;
	if(message.indexOf("@") > -1)
	{
		var retMsg = message;
		while(retMsg.indexOf("@") > -1)
		{
			var begin = retMsg.indexOf("@");
			var end = retMsg.indexOf("@",begin + 1);
			if(begin < end)
			{
				var key = retMsg.substring(begin + 1,end);
				retMsg = retMsg.substring(0,begin) + qryWebRes(key) + retMsg.substring(end + 1);	//不用replace防止正则
			}
			else
			{
				break;
			}
		}
		return retMsg;
	}
	else
	{
		return qryWebRes(message);
	}
}
function qryWebRes(key)
{
	var retMsg = key;
	if(key == null || key == "")
	{
		return "";
	}
	var data = {};
	data.LANG = g_GlobalInfo.Language;
	data.RES_TYPE = "Jscript";
	data.QRY_KEY = key;
	data.IS_FUZZY = "Y";
	data.method = "qryWebRes";
	try
	{
		var ret = callRemoteFunction("WebResService",data);
		/*if(ret != null && ret.RES_LIST != null && ret.RES_LIST.length > 0)
		{
			for(var i = 0; i < ret.RES_LIST.length; i++)
			{
				if(ret.RES_LIST[i].RES_KEY == key)
				{
					retMsg = ret.RES_LIST[i].RES_VALUE;
					break;
				}	
			}
		}*/
		retMsg = ret.jsRes;
	}
	catch(e)
	{
		;
	}
	return retMsg;
}

function showModalWindow(title, iframesrc, winWidth, winHeight, objectParam, callback){
	return ModalWindow.showModalWindow(title, iframesrc, winWidth, winHeight, objectParam, callback);
}
function closeModalWindow(inputObj){
	return ModalWindow.closeModalWindow(inputObj);
}
function getModalInputObj(){
	return ModalWindow.getModalInputObj();
}
var ModalWindow = {
		  
	showModalWindow : function(title, iframesrc, winWidth, winHeight,objectParam,callback){
			var poptd = window.parent.jQuery(window.parent.document.body).find(".popTitleBar");
			var popclosetd = window.parent.jQuery("#closetd");
			var popMsgDiv = window.parent.jQuery(window.parent.document.body).find(".popMsgDiv");
			
			var needOk =objectParam==null?null: objectParam.needOk;
			if(poptd.length>0){
				//popMsgDiv.draggable( "destroy" );
				
				var closetdwidth = popclosetd.width();
				poptd.removeClass("layout_title");
				poptd.addClass("layout_title_black");
				
				poptd.find("span").removeClass("layout_title");
				poptd.find("span").addClass("layout_title_black");
				popclosetd.remove();
				var closeBtn = $("<td id='closetd'></td>");
				
				closeBtn.addClass("popCloseBtn");
				closeBtn.addClass("layout_title_black");
				closeBtn.addClass("formHead");
				closeBtn.css({
				});
				closeBtn.width(closetdwidth);
				closeBtn.html("<span class='layout_title_black span' style='font-size:15pt;width:15px;padding-right:6px;font-weight:bold; '>&nbsp;×</span>");
				closeBtn.insertAfter(poptd);
				//$(poptd).children("span").hide();
				//$(popclosetd).hide();
			}
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
			window.ifscrollx = $(localWindow.document.body).css("overflow-x");
			window.ifscrolly = $(localWindow.document.body).css("overflow-y");
			localWindow.modalCallBack = function(inputobj){
				if (typeof callback == 'function') {
					callback(inputobj);  
				}
			};
			//需要加上，遮罩才可以遮罩到滚动条
			var style=$("<style type=\"text/css\" id=\"bghtmlstyle\">"
					   +"html {"
					   +"overflow:hidden;"
					   +"width:100%;"
					   +"height:100%;"
					   +"}"
					   +"body {"
					   +"width:100%;"
					   +"height:100%;"
					   +"overflow-y:auto; "
					   +"overflow-x:auto; "
					   +"}"
					   +"</style>");
			//localjQuery(localWindow.document.body).append(style);
			var titleheight = 25; // 提示窗口标题高度 
			var iWidth = localWindow.document.documentElement.offsetWidth; 
			var iHeight = localWindow.document.documentElement.clientHeight; 
			var bgDivHeight = Math.max(localWindow.document.body.clientHeight, iHeight);
			if(winWidth >= iWidth || winHeight >= iHeight){//如果弹出窗口比父窗口大，自动将父窗口最大化
				localWindow.moveTo(0, 0);
				localWindow.resizeTo(screen.availWidth,screen.availHeight);
				iWidth = screen.availWidth;
				iHeight = screen.availHeight;
			}
			var bgDiv = $("<div id='popBackDiv'></div>");
			if($.browser.msie && $.browser.version == 8.0){
				bgDiv.css({
					//width:iWidth+"px",
					//height:bgDivHeight+"px"
					width:"100%",
					height:"99.9%"
				});
			}else{
				bgDiv.css({
					//width:iWidth+"px",
					//height:bgDivHeight+"px"
					width:"100%",
					height:"100%"
				});
			}
			
			var scrollTop = $(localWindow.document).scrollTop();
			var scrollLeft = $(localWindow.document).scrollLeft();
			if($.browser.msie){
				$(localWindow.document.body).css("overflow-y","hidden");
				$(localWindow.document.body).css("overflow-x","hidden");
			}
			
			bgDiv.addClass("popBackDiv");
			$(localWindow.document.body).append(bgDiv); 
			$(bgDiv).css({"z-index":"9999"});
			var msgDiv = localjQuery("<div/>");
			
			msgDiv.css({
				top:((iHeight-winHeight)/2+scrollTop -2)+"px",   //border=2所以需要减2
				left:((iWidth-winWidth)/2+scrollLeft-2)+"px",
				width:winWidth+"px",
				height:winHeight+"px",
				"border":"2px",
				"border-color":"#525252",
				"border-style":"solid"
			});
			msgDiv.addClass("popMsgDiv popRadius");
			localjQuery(localWindow.document.body).append(msgDiv);
			$(msgDiv).css({"z-index":"99999"});
			var table = $("<table/>");
			table.css({
				height:"100%",
				border:"0px",
				padding:"0px",
				margin:"0px"
			});
			table.attr("cellSpacing","0");
			msgDiv.append(table);
			var tr=$("<tr/>");
			var titleBar =$("<td/>");
			table.append(tr);
			tr.append(titleBar);
			titleBar.addClass("popTitleBar");
			titleBar.addClass("layout_title");
			titleBar.addClass("formHead");
			titleBar.css({
				width:winWidth-10+"px",
				"padding-bottom":"0px"
			});
			title=qryWebRes(title);
			titleBar.html("<span>"+title+"</span>");
			//msgDiv.draggable();
			
			var closeBtn = $("<td id='closetd'></td>");
			
			closeBtn.addClass("popCloseBtn");
			closeBtn.addClass("layout_title");
			closeBtn.addClass("formHead");
			closeBtn.css({
			});
			closeBtn.html("<span class='colsetd-span' style='font-size:15pt;width:15px;padding-left:2px;padding-right:2px;'>×</span>");
			
			
			localWindow.closepopwin = function(type){
				$(document.body).focus();
				var topJQuery = window.top.jQuery;
				if(jQuery(".popBackDiv").length > 0){
					jQuery(".popBackDiv").unbind();
					jQuery(".popBackDiv").remove();
					//jQuery(".iframepopwinnew").removeData2;
					/*$(jQuery(".iframepopwinnew")[0].contentWindow.document.body).find(".flexigrid").remove();
					$(jQuery(".iframepopwinnew")[0].contentWindow.document.body).find("input").each(function(){
						$(this).unbind();
						$(this)[0].parentNode.removeChild($(this)[0]);
						$(this)[0] = null;
					}); 
					if(jQuery(".iframepopwinnew")[0].contentWindow.freeIframe){
						jQuery(".iframepopwinnew")[0].contentWindow.freeIframe();
					}
					jQuery(".iframepopwinnew")[0].contentWindow.document.body.innerHTML="";
					jQuery(".iframepopwinnew")[0].contentWindow.close();  
					jQuery(".iframepopwinnew")[0].src="about:blank";
					jQuery(".iframepopwinnew").unbind();
					jQuery(".iframepopwinnew")[0].parentNode.removeChild(jQuery(".iframepopwinnew")[0]);*/
					topJQuery(".iframepopwinnew")[0]=null;
					jQuery(".iframepopwinnew").remove();
					jQuery(".popMsgDiv").unbind();
					jQuery(".popMsgDiv").remove();
					jQuery("#bghtmlstyle").unbind();
					jQuery("#bghtmlstyle").remove();
				}else if(topJQuery(".popBackDiv").length>0){
					topJQuery(".popBackDiv").remove();
					//topJQuery(".iframepopwinnew").removeData2();
					$(topJQuery(".iframepopwinnew")[0].contentWindow.document.body).find("input").each(function(){
						$(this).unbind();
						$(this)[0].parentNode.removeChild($(this)[0]);
						$(this)[0] = null;
					}); 
					$(topJQuery(".iframepopwinnew")[0].contentWindow.document.body).find(".flexigrid").remove();
					if(topJQuery(".iframepopwinnew")[0].contentWindow.freeIframe){
						topJQuery(".iframepopwinnew")[0].contentWindow.freeIframe();
					}
					topJQuery(".iframepopwinnew")[0].contentWindow.document.body.innerHTML="";
					topJQuery(".iframepopwinnew")[0].contentWindow.close();  
					topJQuery(".iframepopwinnew")[0].src="about:blank";
					topJQuery(".iframepopwinnew").unbind();
					topJQuery(".iframepopwinnew")[0].parentNode.removeChild(topJQuery(".iframepopwinnew")[0]);
					topJQuery(".iframepopwinnew")[0]=null;
					topJQuery(".iframepopwinnew").remove();
					topJQuery(".popMsgDiv").unbind();
					topJQuery(".popMsgDiv").remove();
					topJQuery("#bghtmlstyle").unbind();
					topJQuery("#bghtmlstyle").remove();
				}
				var poptd ,popclosetd;
				var tmpBackDiv=$("<div/>");
	            tmpBackDiv.css({
	                    width:"100%",
	                    height:msgBoxHeight+"px",
	                    "position":"absolute",
	                    "left":"0px",
	                    "top":"25px"
	                    });
				if(jQuery(window.parent.document).find(".popMsgDiv").length > 0){
					var poptmp = window.parent.jQuery(window.parent.document).find(".popMsgDiv");
					poptmp.draggable({scroll: false});
					poptmp.bind( "dragstart", function(event, ui) {
	                    msgBox.append(tmpBackDiv);
					});
					poptmp.bind( "dragstop", function(event, ui) {
						tmpBackDiv.unbind();
	                    tmpBackDiv.remove();
	                    var top  = ui.position.top;
	                    if(top < 0){
	                    	poptmp.css({"top":"0px"});
	                    }
					});
				}
				if(jQuery(".popMsgDiv").length > 0){
					var poptmp = jQuery(".popMsgDiv");
					poptmp.draggable();
					poptmp.bind( "dragstart", function(event, ui) {
	                    msgBox.append(tmpBackDiv);
		            });
					poptmp.bind( "dragstop", function(event, ui) {
						tmpBackDiv.unbind();
		                tmpBackDiv.remove();
		                var top  = ui.position.top;
	                    if(top < 0){
	                    	poptmp.css({"top":"0px"});
	                    }
		            });
				}
				//$(popMsgDiv).draggable();
				if(type == "x"){
					popclosetd = jQuery(window.parent.document).find(".popCloseBtn");
					poptd = jQuery(window.parent.document).find(".popTitleBar");
					popclosetd.bind("click",function(){window.parent.closepopwin("x");});
					if (typeof callback == 'function') {
						callback();  
					}
				}else{
					popclosetd = jQuery(window.parent.parent.document).find(".popCloseBtn");
					poptd = jQuery(window.parent.parent.document).find(".popTitleBar");
					popclosetd.bind("click",function(){window.parent.parent.closepopwin("x");});
				}
				if(poptd.length>0){
					$(poptd).removeClass("layout_title_black");
					$(poptd).addClass("layout_title");
					$(popclosetd).removeClass("layout_title_black");
					$(popclosetd).addClass("layout_title");
					$(popclosetd).width(closetdwidth);
					$(poptd).find("span").removeClass("layout_title_black");
					$(poptd).find("span").addClass("layout_title");
					$(popclosetd).find("span").removeClass("layout_title_black");
					$(popclosetd).find("span").addClass("layout_title");
				}
				if($.browser.msie ){
					if(!jQuery(localWindow.document.body).find("#portal_table").length>0){
						jQuery(localWindow.document.body).css("overflow-y",window.ifscrolly);
						jQuery(localWindow.document.body).css("overflow-x",window.ifscrollx);
					}
				}
				if ($.browser.msie) {  
					CollectGarbage();
	            }  
				
			};
			closeBtn.bind("click",function(){localWindow.closepopwin("x");});
			tr.append(closeBtn);
			
			var msgBox = $("<td/>");
			var msgBoxTr = $("<tr/>");
			msgBoxTr.append(msgBox);
			msgBox.attr("valign","top");
			table.append(msgBoxTr);
			msgBox.attr("colspan","2");
			
			var msgBoxHeight = "";
			if(needOk){
				msgBoxHeight = winHeight - titleheight -7-38;
				
			}else{
				msgBoxHeight = winHeight - titleheight -7;
			}
			msgBox.css({
				height:msgBoxHeight+"px"
			});
			//var timestamp=new Date().getTime();
			//var iframeid= "framepopwindiv"+timestamp;
			var framepopwindivwidth = "99.9%";
			if(!$.browser.msie){
				framepopwindivwidth = "100%";
			}
			var frame="<IFRAME ID='framepopwindiv' class='iframepopwinnew' FRAMEBORDER='0' src="+iframesrc+" SCROLL='no' width="+winWidth+"px; height="+framepopwindivwidth+"></IFRAME>";
			msgBox.html(frame);
			localjQuery(".iframepopwinnew").data("param",objectParam); 
			localjQuery(".iframepopwinnew").data("callBack",callback); 
			var tmpBackDiv=$("<div/>");
            tmpBackDiv.css({
                    width:"100%",
                    height:msgBoxHeight+"px",
                    "position":"absolute",
                    "left":"0px",
                    "top":"25px"
                    });
			msgDiv.draggable({scroll: false});
            msgDiv.bind( "dragstart", function(event, ui) {
                    msgBox.append(tmpBackDiv);
            });
            msgDiv.bind( "dragstop", function(event, ui) {
            		tmpBackDiv.unbind();
                    tmpBackDiv.remove();
                    var top  = ui.position.top;
                    if(top < 0){
                    	msgDiv.css({"top":"0px"});
                    }
            });
            //添加ok按钮 added by zhangwei UR:185074
            if(needOk){
            	var popBottomTd = $("<td/>");
            	var popBottomTr = $("<tr/>");
            	
    			popBottomTd.attr("valign","top");
    			popBottomTd.attr("align","center");
    			popBottomTd.attr("colspan","2");
    			popBottomTd.css({
    				height:"30px"
    			});
    			
    			var okBtn = $("<input id='okBtn' style='overflow: visible; padding-right: 5px; padding-left: 5px; width:45px; height:22px;' type='button' value='  OK  ' onClick='ModalWindow.closeModalWindow()'/>" );
    			
    			popBottomTd.append(okBtn);
    			
    			popBottomTr.append(popBottomTd);
    			table.append(popBottomTr);
    			
    			okBtn.focus();
    			
            }
            
            
		},
		closeModalWindow : function(inObj){
			
			if(window.parent.modalCallBack){
				window.parent.modalCallBack(inObj);
				inObj = null;
				
			}else if(window.top.modalCallBack){
				window.top.modalCallBack(inObj);
				inObj = null;
				
			}else{
				
			}
			if(window.parent.closepopwin){
				window.parent.closepopwin();
			}else if(window.top.closepopwin){
				window.top.closepopwin();
			}else{
			//定制表单里弹出页面选择特别处理	
			try {
				if(inObj!=null && inObj.idVal!=null){
           parent.location.hash = '{text:\"'+inObj.textVal+'\",value:\"'+inObj.idVal+'\"}';
        }else{
        	 parent.location.hash ='{}';
        }
        
    } catch (e) {
    	
        // ie、chrome的安全机制无法修改parent.location.hash，
        // 所以要利用一个中间的cnblogs域下的代理iframe
        var ifrproxy = document.createElement('iframe');
        ifrproxy.style.display = 'none';
        if(inObj!=null && inObj.idVal!=null){
           ifrproxy.src = '<%=request.getParameter("callbackurl")%>'+'#'+'{text:\"'+inObj.textVal+'\",value:\"'+inObj.idVal+'\"}'; 
        }else{
        	 ifrproxy.src = '<%=request.getParameter("callbackurl")%>'+'#{}'; 
        }
        
        document.body.appendChild(ifrproxy);
    }
		}
			
		},
		getModalInputObj : function(){
			try{
			$(document.body).focus();
			var IframeWhereContaineMe = window.frameElement;
			var parentJQuery = window.parent.jQuery;
			var inputObj = parentJQuery(IframeWhereContaineMe).data("param");
			return inputObj;
		}catch(e){
			return null;
		}
		}
};


