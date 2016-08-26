<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../web.inc"%>
<html>
<%
	WebRoot = getWebRoot(request);
	String procInstId=request.getParameter("PROC_INST_ID");
	String holderId=request.getParameter("HOLDER_ID");
	String holderNo = request.getParameter("HOLDER_NO");
	String processName = request.getParameter("PROCESS_NAME");
	String state = request.getParameter("HOLDER_STATE");
	if("".equals(procInstId)){
		procInstId=null;
	}
%>

<script type="text/javascript">
	var g_GlobalInfo = new Object();
	g_GlobalInfo.WebRoot="<%=WebRoot%>";
	var procInstId=<%=procInstId%>;
	var holderId=<%=holderId%>;
    var holderNo = <%=holderNo%>;
	var processName = <%=processName%>;
	var state = <%=state%>;
	console.info("procInstId = "+procInstId);
</script>
	<head>
		<title></title>
		<link href="<%=WebRoot%>/modules/bpmn/flowdesigner/draw/css/ZTEsoftTabs.css" rel="stylesheet" type="text/css"/>
		<script language="javascript" src="<%=WebRoot%>/modules/bpmn/flowdesigner/draw/js/holderDetail.js" type="text/javascript"></script>
	</head>
	<body style="overflow-y:auto;overflow-x:auto">
	<div id="divTabs">
			<ul class="Menubox">
				<li id="tab_liactive" class="hover" onclick="tab_select('active')" >
					<div><span>流程执行轨迹</span></div>
				</li>
				<li  id="tab_liview" onclick="tab_select('view')" >
					<div><span>标准流程图</span></div>
				</li>
			</ul>
		</div>
		<div id="divFrameTab" >
			<div id="tagactive"  style="display: none;height: 300px;">
				<iframe id="frameactive" width="100%" 
				frameBorder=0 SCROLLING="no" name="frameactive" onLoad="" style="height: 300px;"></iframe>
			</div>
			<div id="tagview" style="display: none;height: 300px;" >
				<iframe id="frameview" width="100%"
				frameBorder=0 SCROLLING="no" name="frameview" onLoad="" style="height: 300px;"></iframe>
			</div>
		</div>
		
		  <div class="layout_title">
			<table cellpadding=0 cellspacing=0>
				<tr>
					<td class="formHead"><div><span>流程单详情</span></div></td>
				</tr>
				<tr>
					<td class="line"></td>
				</tr>
			</table>
		</div>
		<div class="layout_item">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<th width="15%"></th>
					<th width="35%"></th>
					<th width="15%"></th>
					<th width="35%"></th>
				</tr>
				<tr>		
			   		<td class="fieldNameCol1st">流程单编号</td>
					<td>
						<input type="text" id="HOLDER_NO" class="text" maxlength="20" disabled="disabled">
					</td>		
			   		<td class="fieldname">流程名称</td>
			   		<td><input type="text" id="PROCESS_NAME" class="text" maxlength="20" disabled="disabled"></td>	
				</tr>
				<tr>		
			   		<td class="fieldNameCol1st">流程实例ID</td>
					<td>
						<input type="text" id="PROC_INST_ID" class="text" maxlength="20"disabled="disabled">
					</td>		
			   		<td class="fieldname">流程单状态</td>
			   		<td><input type="text" id="HOLDER_STATE" class="text" maxlength="20" disabled="disabled"></td>		
				</tr>
				<tr>		
			   		<td class="fieldNameCol1st">开始时间</td>
					<td>
						<input type="text" id="START_TIME" class="text" maxlength="20"disabled="disabled">
					</td>		
			   		<td class="fieldname">结束时间</td>
			   		<td><input type="text" id="END_TIME" class="text" maxlength="20" disabled="disabled"></td>
				</tr>
			</table>						
		</div>
		
		
	</body>
</html>