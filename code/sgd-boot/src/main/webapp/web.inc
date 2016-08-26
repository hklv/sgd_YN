<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.ztesoft.uboss.bpm.utils.Common"%>
<%@page import="com.ztesoft.zsmart.core.configuation.ConfigurationMgr"%>
<%@page import="com.ztesoft.zsmart.core.service.DynamicDict"%>
<%@page import="com.ztesoft.uboss.bpm.utils.MyValidateCode"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.ztesoft.zsmart.userrole.service.PortalServ"%>
<%
DynamicDict[] staffRoleDtoList = new DynamicDict[0];
DynamicDict[] staffPotalDtoList = new DynamicDict[0];
DynamicDict[] staffOrgDtoList = new DynamicDict[0];
String showPortalId = "";
Long orgId=null;
String username="";
String usercode="";
String logoutConfirmStr=getString("LOGOUT_CONFIRM");
DynamicDict sysLoginDto = null;

try
{
	sysLoginDto = (DynamicDict) session.getAttribute("SYS_LOGIN_INFO");
	username = sysLoginDto.getString("USER_NAME");
	usercode = sysLoginDto.getString("USER_CODE");
	if (sysLoginDto != null) {
	
		staffRoleDtoList = (DynamicDict[]) sysLoginDto.getList("USER_ROLE_LIST").toArray(new DynamicDict[]{});
		
	}
	
	if (sysLoginDto != null) {
		staffPotalDtoList = (DynamicDict[]) sysLoginDto.getList("USER_PORTAL_LIST").toArray(new DynamicDict[]{});
	}
	
	if (sysLoginDto != null) {
		staffOrgDtoList = (DynamicDict[]) sysLoginDto.getList("STAFF_ORG_LIST").toArray(new DynamicDict[]{});
	}
	
	if(staffOrgDtoList!=null && staffOrgDtoList.length>0){
	for(int j=0;j<staffOrgDtoList.length;j++){
		DynamicDict staffOrgDto = (DynamicDict)staffOrgDtoList[j];
		if((staffOrgDto.get("IS_PART_TIME").toString()).equals("1")){
			orgId=staffOrgDto.getLong("ORG_ID");
			break;
		}
		}
      }
	
	
	if(showPortalId!=null && showPortalId.length()>0){
		boolean isHasPortal=false;
		if(staffPotalDtoList!=null && staffPotalDtoList.length>0){
			
			for(int j=0;j<staffPotalDtoList.length;j++){
				DynamicDict staffPotalDto = (DynamicDict)staffPotalDtoList[j];
				if((staffPotalDto.get("PORTAL_ID").toString()).equals(showPortalId)){
					isHasPortal=true;
					break;
				}
	
		      }
		 }
		if(isHasPortal==false){
			response.sendRedirect("error404.jsp?CODE=FORBID_PORTAL");
		}
	}
	
	//获取当前Portal_Id
	DynamicDict defaultPortal = (DynamicDict) sysLoginDto.getBO("DEFAULT_PORTAL");
	
	if(showPortalId==null || showPortalId.length()<=0 ||Long.valueOf(showPortalId)<=0){
		if(defaultPortal!=null && defaultPortal.get("PORTAL_ID")!=null){
			showPortalId = defaultPortal.get("PORTAL_ID").toString();
		}else{
			showPortalId="0";
		}
	}
	session.setAttribute("NOW_PORTAL_ID",showPortalId);
		
}catch(Exception ex){
	ex.printStackTrace();
}

%>
<%!
String PageCss = "";
String WebRoot = "";
String Language = "zh_CN";
String Calendar = "";
String DateFormat = "";
String PleaseSelect="";
 
String FormTitleStr = "";
String FormHeadStr = "";
String FormFootStr = "";
String vCode = "";


String  _skin = "default";

public String getWebRoot(HttpServletRequest request)
{
  	String strWebRoot = "";
  
  	String ips = ConfigurationMgr.instance().getString("https.ip",null);
	String requestfrom = request.getRemoteAddr();
	if (requestfrom != null) {
		if (ips != null && ips.indexOf(requestfrom) > -1) {
			strWebRoot = "https";
		} else {
			strWebRoot = request.getScheme();
		}
	} else {
		strWebRoot = request.getScheme();
	}
	strWebRoot += "://";
	strWebRoot += request.getServerName();

	int port = request.getServerPort();
	if (port != 80) {
		strWebRoot += ":" + port;
	}
	strWebRoot += request.getContextPath() + "/";
  	return strWebRoot;
}




public String getPageCss(HttpServletRequest request)
{
	StringBuffer pageCssStrBuf = new StringBuffer();
	String requestUriStr = request.getServletPath();
	if(requestUriStr!=null&&requestUriStr!="")
	{
	
		String[] strs = requestUriStr.split("/");
		if(strs == null) return "";
		String moduleName = "";
		if(strs.length > 3 && "modules".equals(strs[1])){
			moduleName = strs[2]+"/";
		}
		if((strs.length > 3 && "modules".equals(strs[1]))
		&&(
		"portal".equals(strs[3])
		||"report".equals(strs[3])
		||"form".equals(strs[3])
		)
		){
		  moduleName = strs[3]+"/";
		}
		String cssFileName = strs[strs.length - 1].replaceAll(".jsp",".css");
	  pageCssStrBuf.append(WebRoot).append("skins/").append(_skin).append("/").append(Language).append("/css/pagecss/").append(moduleName).append(cssFileName);
	}
	return pageCssStrBuf.toString();	
}

%>

<%
    Language = Common._LANGUAGE.get()==null?"zh_CN":Common._LANGUAGE.get().toString();
    String nbsp = "&nbsp;";
    if(Language.equals("zh_CN")){
    	nbsp = "";
    }
    if(Language.startsWith("en")){
		    Language = "en_US";
    }
    Calendar=Common.getCalendar(Language);
    if(session.getAttribute("SYS_DEFAULT_SKIN")!=null){
      _skin = session.getAttribute("SYS_DEFAULT_SKIN").toString();
     }
    if(session.getAttribute("USER_SKIN")!=null){
     _skin = session.getAttribute("USER_SKIN").toString();
    }
    WebRoot = getWebRoot(request);
    PageCss = getPageCss(request);
//	DateFormat =  application.getAttribute("DateFormat").toString();
	
	if (request.getSession().getAttribute(Common.SESSION_VCODE) != null)
	{
		vCode = request.getSession().getAttribute(Common.SESSION_VCODE).toString();
	}
	else
	{
		MyValidateCode code = new MyValidateCode();
		vCode = code.getRandomCode(24);
		request.getSession().setAttribute(Common.SESSION_VCODE,vCode);
	}
		
	

response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
PleaseSelect=Common.getResourceStringLogger(Common.getGlobalRes("PLEASE_SELECT"));
%>
<script type="text/javascript">
var g_GlobalInfo = new Object();
g_GlobalInfo.Language="<%=Language%>";
g_GlobalInfo.Charset="<%=Common.LOCAL_CHARSET%>";
g_GlobalInfo.WebRoot="<%=WebRoot%>";
g_GlobalInfo.Version="<%=Common.VERSION%>";
g_GlobalInfo.Calendar="<%=Calendar%>";
g_GlobalInfo.DateFormat="<%=DateFormat%>";
g_GlobalInfo.SupportServTypeList="<%=application.getAttribute("SupportServTypeList")%>";
g_GlobalInfo.ShowDetailLogComments="<%=application.getAttribute("ShowDetailLogComments")%>";
g_GlobalInfo.CurrencyPrecision = "<%=application.getAttribute("CurrencyPrecision")%>";
g_GlobalInfo.AuditLog ="<%=application.getAttribute("AuditLog")%>";
g_GlobalInfo.SSOAuth="<%=application.getAttribute("SSOAuth")%>";
g_GlobalInfo.V_CODE="<%=vCode%>";
g_GlobalInfo.AlignRightString="<%=application.getAttribute("AlignRightString")%>";
g_GlobalInfo.ImgFilter="<%=application.getAttribute("ImgFilter")%>";
g_GlobalInfo.PosLeftString="<%=application.getAttribute("PosLeftString")%>";
g_GlobalInfo.Direction="<%=application.getAttribute("Direction")%>";
g_GlobalInfo.SelComponentIDAlign="<%=application.getAttribute("SelComponentIDAlign")%>";
var g_HideComponet = [];
var theMenuIdAboveIs = "<%=session.getAttribute("PMENU_ID")%>";
var _ShowPortalId = <%=session.getAttribute("NOW_PORTAL_ID")%>;
var _WebRoot = "<%=WebRoot%>";	
var _Language  = "<%=Language%>";
var _Skin= "<%=_skin%>";
var _USER_ID= <%=session.getAttribute("USER_ID")%>;
var _ORG_ID =<%=orgId %>;
var _USER_NAME ="<%=username %>";
var _USER_CODE ="<%=usercode %>";
var _LOGIN_IP ="<%=session.getAttribute("LOGIN_IP") %>";
var _DEST_IP ="<%=session.getAttribute("DEST_IP") %>";
</script>
<BASE HREF="<%=WebRoot%>">
<script type="text/javascript" src="<%=WebRoot%>public/script/jquery/jquery-1.7.2.min.js"></script>

<link rel="stylesheet" type="text/css" href="skins/default/zh_CN/css/jquery/jquery-easyui/default/easyui.css">
<link rel="stylesheet" type="text/css" href="skins/default/zh_CN/css/jquery/jquery-easyui/icon.css">

<link   type="text/css"	href="<%=WebRoot%>skins/<%=_skin%>/<%=Language%>/css/common.css" rel="stylesheet"/>
<link   type="text/css"	href="<%=WebRoot%>skins/<%=_skin%>/<%=Language%>/css/jquery/jquery-ui-1.8.14.css" rel="stylesheet"/>
<link   type="text/css"	href="<%=WebRoot%>skins/<%=_skin%>/<%=Language%>/css/jquery/icon.css" rel="stylesheet"/>
<link   type="text/css"	href="<%=WebRoot%>skins/<%=_skin%>/<%=Language%>/css/ztesoft/grid/grid.css" rel="stylesheet"/>
<link   type="text/css" href="<%=PageCss%>" rel="stylesheet" >

<script type="text/javascript" src="<%=WebRoot%>public/script/jquery/jquery-ui-1.8.10.custom.js"></script>
<script type="text/javascript" src="public/script/jquery/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="public/script/jquery/jquery-easyui/locale/easyui-lang-<%=Language%>.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/jquery/jquery.serialize.ext.js"></script>
 
<script src="<%=WebRoot%>public/script/comm/ZTEsoft.js" type="text/javascript"></script>
<script src="<%=WebRoot%>public/script/comm/RemoteHandler.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/popup/windows.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/comm/common.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/helper/Validator.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/helper/RegExpHelper.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/comm/TOHelper.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=WebRoot%>modules/uoa/comm/bizjs/bizcomm.js"></script>
<!-- Extjs start -->
<link href="<%=WebRoot%>public/extjs/ext-4.2.1/resources/css/ext-all.css" rel="stylesheet" type="text/css" />
<link href="<%=WebRoot%>public/extjs/icon/icon.css" rel="stylesheet" type="text/css" />
<link href="<%=WebRoot%>public/extjs/custom/custom.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=WebRoot%>public/extjs/ext-4.2.1/ext-all.js"></script>
<!--<script type="text/javascript" src="<%=WebRoot%>public/extjs/ext-4.2.1/ext-all-debug.js"></script>-->
<script type="text/javascript" src="<%=WebRoot%>public/extjs/ext-4.2.1/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
var webRoot = "<%=WebRoot%>";
var componentPath = webRoot+'./public/extjs/component';
Ext.Loader.setConfig({
	enabled: true,
	disableCaching: true,
	paths: {
		'component' : './public/extjs/component'
	}
});
</script>
<!-- Extjs end -->

<%@include file="public/DisplayPage.inc"%>
<script type="text/javascript">
	var PLEASE_SELECT= "<%=PleaseSelect%>";
	$(function(){
		if($.browser.msie && $.browser.version == 7.0){
			$(".text").css({"width":"95%"});
		}
		$(".left-inner-div").height("auto");
		$(".right-inner-div").height("auto");
		var ht = document.body.scrollHeight;
		$(".left-inner-div").height(ht);
		$(".right-inner-div").height(ht);
		
	});
</script>
<script>
//不允许脱离portal.jsp页面单独访问
var purl="<%=request.getServletPath()%>";

if((purl.indexOf("modules/admin/form")<0 && purl.indexOf("modules/admin/stafforg/org/popwin/")<0) && purl.indexOf("/modules/")>=0){
	var topurl = window.top.document.location.href;
	
	if(topurl.indexOf("<%=WebRoot%>Portal.jsp")<0 && topurl.indexOf("<%=WebRoot%>Portal2.jsp")<0 &&  topurl.indexOf("modules/admin/post/post.jsp")<0){
	 // location.href="<%=WebRoot%>error404.jsp?CODE=FORBID_OPEN_URL";
	}
}
</script>
<script>
function setdivheight(){
	if($(".outer-div-box .left-30p").length>0){
		var ht=$(".outer-div-box").height();
		
		if($(document).height()>ht){
			return;
		  	//$(document).height(ht);
		}else{
		  	ht=$(document).height();
		}
		var h=$(".outer-div-box .left-30p").height();
		$(".outer-div-box .left-30p").height(ht);
		$(".outer-div-box .left-30p .left-inner-div").height(ht);
		$(".outer-div-box .right-70p").height(ht);
		$(".outer-div-box .right-70p .right-inner-div").height(ht);
	}
}
$(window).ready(function(){
	//setdivheight();
});
$(window).resize(function(){
	setdivheight();
});
var ubosstoken = '<%=request.getSession(false).getAttribute("ubosscsrftoken")%>';
</script>