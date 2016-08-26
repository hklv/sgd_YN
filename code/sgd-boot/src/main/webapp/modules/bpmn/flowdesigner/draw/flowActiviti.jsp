<![if !IE]>    <!--判断是否是IE浏览器，如果不是在加入<!DOCTYPE html>声明 ，用来解决非IE浏览器下任务库弹出框无法关闭的问题 -->
<!DOCTYPE html>
<![endif]>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ztesoft.uboss.bpm.utils.Common"%>
<%@page import="com.ztesoft.zsmart.core.configuation.ConfigurationMgr"%>

<%!
String WebRoot = "";
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

%>

<%
    WebRoot = getWebRoot(request);    
%>


<script type="text/javascript">
var g_GlobalInfo = new Object();
g_GlobalInfo.WebRoot="<%=WebRoot%>";
</script>
<link href="<%=WebRoot%>skins/default/zh_CN/css/pagecss/bpmn/flowtemplat.css" rel="stylesheet" type="text/css">
<script src="<%=WebRoot%>public/script/comm/ZTEsoft.js" type="text/javascript"></script>
<script src="<%=WebRoot%>public/script/comm/RemoteHandler.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/popup/windows.js"></script>
<%@include file="../../../../public/DisplayPage.inc"%>

<html xmlns:v>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>uBOSS流程设计器静态流程图</title>
	
	<link rel="stylesheet" type="text/css" href="../themes/default/easyui.css">
	<link href="css/flowPath.css" type="text/css" rel="stylesheet" />
<STYLE>
v\:* {
	behavior: url(#default#VML);
}
</STYLE>
	

</head>

<body  class="easyui-layout bodySelectNone" id="body" onselectstart="return false" >
	
	<div
		style="width: 100%; position: relative; z-index: 9999; top: 0; right: 20px;">
		<table style="float: right;">
			<tr>
				<td><img id="backGroundImg" src="images/A.png"
					style="height: 10px; width: 20px;" />正在处理</td>
				<td><img id="backGroundImg1" src="images/B.png"
					style="height: 10px; width: 20px;" />回退</td>
				<td><img id="backGroundImg2" src="images/C.png"
					style="height: 10px; width: 20px;" />完成</td>
				<td><img id="backGroundImg3" src="images/E.png"
					style="height: 10px; width: 20px;" />异常</td>
				<td><img id="backGroundImg4" src="images/H.png"
					style="height: 10px; width: 20px;" />挂起</td>
				<td><img id="backGroundImg5" src="images/I.png"
					style="height: 10px; width: 20px;" />待处理</td>
				<td><img id="backGroundImg6" src="images/J.png"
					style="height: 10px; width: 20px;" />跳转</td>
				<td><img id="backGroundImg7" src="images/X.png"
					style="height: 10px; width: 20px;" />已删除</td>
			</tr>
		</table>
	</div>
	
	

	<div region="center" id="contextBody" style="overflow:auto;position:relative;" >

			<!-- Line右键菜单 -->
			<div id="lineRightMenu" >
				
			</div>
					
			<!-- Mode右键菜单 -->
			<div id="rightMenu" >
				
			</div>
			
					
		 	<div id="topCross"></div>
      <div id="leftCross"></div>
	
	
</div>
<div    style="display: none;" >
		<!-- 小地图 -->	
		<div id="smallMap"></div> 
		
		<div id="mainControl" >
			
			<div id="tab" class="control">
	
				
					<div id="tab1" class="htabup">

					<table id="modelAttr" cellpadding="0" cellspacing="0">
						<tr>
							<td name="inputTitle" style="text-align: center; width: 80px; height: 25px;">
								任务名称</td>
							<td name="inputTitle"><input type="text" id="inputTitle" class="inputComm"
								style="width: 150px;" />
								<input type="button" id="subProcessButton" value="..."
								style="width: 25px; height: 20px; padding: 1px 4px; margin-left: -5px;display: none;"
								onclick="subProcessClick();" /></td>
							<td name="taskTemplate"
								style="text-align: center; height: 25px; width: 80px; padding-left: 30px; display: none;">
								任务模板</td>
							<td name="taskTemplate" style="display: none;"><input
								type="text" id="taskTemplateName" class="inputComm"
								style="width: 150px;" readonly="readonly" /> <input type="text"
								id="taskTemplateId" class="inputComm" style="display: none;" />
								<input type="text" id="index" value="" style="display: none;" />
								<input type="button" value="..."
								style="width: 25px; height: 20px; padding: 1px 4px; margin-left: -5px;"
								onclick="taskTemplateClick();" /></td>
						</tr>
						<tr id="Variable" style="display: none;" data-role="none">
							<td style="text-align: center; height: 25px; width: 80px;">
								结果变量</td>
							<td><input type="text" id="resultVariable" class="inputComm"
								style="width: 150px;" /></td>
						</tr>
						<tr id="countersign" style="display: none;" data-role="none">
							<td style="text-align: center; height: 25px; width: 80px;">
								是否会签</td>
							<td><input type="radio" name="countersign" value="true" />是
								<input type="radio" name="countersign" value="false"
								checked="checked" />否</td>
						</tr>
						<tr id="isSequential" style="display: none;">
							<td style="text-align: center; height: 25px; width: 80px;">
								签入方式</td>
							<td><input type="radio" name="sequential" value="true" />并行签入
								<input type="radio" name="sequential" value="false"
								checked="checked" />串行签入</td>
						</tr>
						<tr style="display: none;">
							<td style="width: 80px; text-align: center;">内容</td>
							<td><textarea rows="3" id="modeContent" class="contextArea"></textarea>
							</td>
						</tr>
						
						

					</table>					

				</div>
				
		
			

			</div>
			
		</div>
	
	
	</div>
	
	
	
	<!-- 移动时的图象 -->
	<div id="moveBaseMode" class="moveBaseMode">
		<img id="moveBaseModeImg"  src="images/Favourite.png" class="nodeStyle"/>
	</div>
	
	<div id="prop" style="visibility: hidden;">
				Dialog Content.  
	</div>


	
</body>
	
	<script src="js/jquery-1.6.1.js" type="text/javascript"></script>
	<script src="js/jquery.easyui.min.js" type="text/javascript"></script>
	<script src="js/uuid.js" type="text/javascript"></script>
	<script src="js/ActivitiBaseTool.js" type="text/javascript" language="javascript"></script>
	<script src="js/flowActiviti.js" type="text/javascript" language="javascript"></script>
	<script src="js/ControlTool.js" type="text/javascript" language="javascript"></script>
	<script src="js/Entity.js" type="text/javascript" language="javascript"></script>
	<script src="js/Global.js" type="text/javascript" language="javascript"></script>
	<script src="js/KeyEvent.js" type="text/javascript" language="javascript"></script>
	<script src="js/ActivitiLineTool.js" type="text/javascript" language="javascript"></script>
	<script src="js/Map.js" type="text/javascript" language="javascript"></script>
	<script src="js/ActivitiModeTool.js" type="text/javascript" language="javascript"></script>
	<script src="js/SmallMapTool.js" type="text/javascript" language="javascript"></script>
	<script src="js/UndoRedoEventFactory.js" type="text/javascript" language="javascript"></script>
	<script src="js/Utils.js" type="text/javascript" language="javascript"></script>

	<%
		
		String process_instance_id=request.getParameter("process_instance_id");
		if("".equals(process_instance_id)){
			process_instance_id=null;
		}
		String taskHolderId=request.getParameter("taskHolderId");
		if("".equals(taskHolderId)){
			taskHolderId=null;
		}
		String taskholderNo=request.getParameter("taskholderNo");
		if("".equals(taskholderNo)){
			taskholderNo=null;
		}
	%>
	
	<script type="text/javascript" language="javascript">
		
	
	
		var mainControl = $id("mainControl");
		mainControl.style.width = (document.body.offsetWidth - 212) + "px";
			
  		var bgImg = "url(images/bg.gif)";
  		var backColor = "#e0ecff";
  		
  		
  	
  		//var process_instance_id="55317";
  		var process_instance_id=<%=process_instance_id%>;
  		var taskHolderId='<%=taskHolderId%>';
  		var taskholderNo='<%=taskholderNo%>';
  		
			jQuery(document).ready(function () {
				var global = com.xjwgraph.Global;
				
				graphUtils = com.xjwgraph.Utils.create({
					
					contextBody : 'contextBody',
					width : document.body.offsetWidth,
					height : document.body.offsetHeight,
					smallMap : 'smallMap',
					mainControl : 'mainControl',
					prop : 'prop'
				
				});
				
				
				document.body.onclick = function () {
					if (!stopEvent) {
						global.modeTool.clear();
					} 
					
				}

				if(process_instance_id!=null){
					/*如果有deployId则说明数据库中存在流程图，查询数据库流程xml开始  */
		  			var inParam = {};
		  			inParam.method= "qryProcessTrack";
		  			inParam.PROCESS_INSTANCE_ID = process_instance_id;
		  			try{
		  			var result = callRemoteFunction("BpmClientService", inParam); 
		  			/* if(BeanXML.prototype.brower()!="IE"){				//使用SVG画线的时候如果超出视窗界面会被截取，通过任务LIST的长度重新计算SVG视窗的宽度，指定一个宽度线就不会被截取
		  			var width=180*(result.TASK_LIST.length);
		  			$("#svgContext").attr("width",width);
		  			} */
		  			if(result.TASK_LIST===undefined){
		  				showMessage(qryWebRes("BPMN_NO_DATA"));
		  			}else{
		  			BeanXML.prototype.toHTML(result.TASK_LIST);
		  				}
		  			}
		  			catch(e) {
		  			showError("Error_INITPAGE",e);
		  			}  	
		  			/*如果有deployId则说明数据库中存在流程图，查询数据库流程xml结束  */
		  			
		  		}
					else if(taskHolderId!='null'){
					
		  			var inParam = {};
		  			inParam.method= "qryProcessTrack";
		  			inParam.HOLDER_ID = taskHolderId;
		  			try {
		  			var result = callRemoteFunction("BpmClientService", inParam);
		  			if(result.TASK_LIST==undefined){
		  				showMessage(qryWebRes("BPMN_NO_DATA"));
		  			}else{
		  				BeanXML.prototype.toHTML(result.TASK_LIST);	
		  			   }
		  			}catch (e) 
		  				{
		  				
			  			showError("Error_INITPAGE", e);		 				
			  				
		  				}
		  			
		  			
		  		}
				else if(taskholderNo!='null'){
					
		  			var inParam = {};
		  			inParam.method= "qryProcessTrack";
		  			inParam.HOLDER_NO = taskholderNo;
		  			try {
		  			var result = callRemoteFunction("BpmClientService", inParam);
		  			if(result.TASK_LIST==undefined){
		  				showMessage(qryWebRes("BPMN_NO_DATA"));
		  			}else{
		  				BeanXML.prototype.toHTML(result.TASK_LIST);	
		  			   }
		  			}catch (e) 
		  				{
		  				
			  			showError("Error_INITPAGE", e);		 				
			  				
		  				}
		  			
		  			
		  		}
				
			});
			
	</script>

</html>