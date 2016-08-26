<%@ page language="java"  import="java.util.ArrayList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<html>
  <%
  	String Language = "en_US"; 
  	String context=request.getContextPath();
  %>
<head>
	<title>Upload</title>	
		<style type="text/css">
body{ font-size:14px;}
input{ vertical-align:middle; margin:0; padding:0}
.file-box{ position:relative;width:340px}
.txt{ height:20px; border:1px solid #cdcdcd; width:180px;}
.btn{ background-color:#FFF; border:1px solid #CDCDCD;height:20px; width:70px;}
.file{ position:absolute; top:0; right:80px; height:20px; filter:alpha(opacity:0);opacity: 0;width:260px }
</style>
	<script language="javascript">
	
	
	
		//上传文件  如果页面文件上传控件宽度与默认值不同需要设定
		var g_GlobalInfo = new Object();
		function uploadFile(widthPx,userPath)
		{
			var form = $("#frmFile");
			var filePath = $("#textfield").val();
			if(filePath == ""){
				alert("请指定附件!");
				return;
			}
			if(filePath.indexOf(" ") >= 0){
				alert("附件名称不能含有空格!");
				$("#textfield").val("");
				return;
			}
			var nextLink = "/public/script/ui/ztesoft/uploadFile/up.jsp";
			
			var actionurl = "";
			
			if(widthPx!=null){
				actionurl = "<%=context%>/UploadFlowServlet?_username="+_USER_NAME +"&nextlink="+nextLink+";widthPx=width:"+widthPx;
				//form.attr("action","<%=context%>/UploadFlowServlet?nextlink="+nextLink+";widthPx=width:"+widthPx);
			}
			else{
				actionurl = "<%=context%>/UploadFlowServlet?_username="+_USER_NAME +"&nextlink="+nextLink;
				//form.attr("action", "<%=context%>/UploadFlowServlet?nextlink="+nextLink);
			}
			
			if(userPath != null && userPath != ""){
				actionurl += ("&userPath=" + userPath);
			}
			
			form.attr("action", actionurl);
			
			form.submit();
		}
		//重新加载  widthPx为 500px or 300px 等  ，如果页面文件上传控件宽度与默认值不同需要设定

		
		function uploadFileStoreDB(widthPx)
		{
			var form = $("#frmFile");
			var nextLink = "/bss/pub/popwin/UploadFile.jsp";
			
			if(widthPx!=null){
				form.attr("action", g_GlobalInfo.WebRoot +"/UploadFlowServlet?_username="+_USER_NAME +"&nextlink="+nextLink+";widthPx=width:"+widthPx+"&storeDB=1");
			}
			else{
				form.attr("action", g_GlobalInfo.WebRoot +"/UploadFlowServlet?_username="+_USER_NAME +"&nextlink="+nextLink+"&storeDB=1");
			}
			form.submit();
		}

		function reload(widthPx)
		{
			var form = $("#frmFile");
			var nextLink = "/bss/pub/popwin/UploadFile.jsp";
			if(widthPx!=null){
				form.attr("action", g_GlobalInfo.WebRoot +"/UploadFlowServlet?_username="+_USER_NAME +"&nextlink="+nextLink+";widthPx=width:"+widthPx+";reload");
			}
			else{
				form.attr("action", g_GlobalInfo.WebRoot +"/UploadFlowServlet?_username="+_USER_NAME +"&nextlink="+nextLink+";"+";reload");
			}

			form.submit();
		}
		
		function refreshParent()
		{
			if($("#hidNewFileName").val()!="" || (g_UploadResultObj != null && g_UploadResultObj.labelUpResult != null))
			{
				window.parent.afterDoPostBack();
			}
		}
	</script>
  </head>
<body  style='overflow:hidden;margin:0px' bgcolor="#E1F1FD" onload="refreshParent();">
	<form id="frmFile" method="post"  target="_self" style="margin:0px" enctype="multipart/form-data">	
		<input type="hidden" id="hidUpResult"  name="hidUpResult">			
		<input type="hidden" id="hidNewFileName"  name="hidNewFileName">
		<input type="hidden" id="hidNextLink"  name="hidNextLink">
		<input type="button" id="hidBtnUpload" name="hidBtnUpload"  style="display:none">				
		<input type="hidden" id="hidOldFileName"  name="hidOldFileName">
		<input type="hidden" id="hidFileSize"  name="hidFileSize">
		<input type="hidden" id="hidFileType"  name="hidFileType">
		<table cellpadding=0 cellspacing=0 border=0>
			<tr>
				<td>
				<%String widthPx = request.getParameter("widthPx");%>
				<%if (widthPx==null || widthPx.equalsIgnoreCase("")) widthPx="width:230px";%>
				<div class="file-box">
 <input type='text' name='textfield' id='textfield' class='text' style='width:180px' readonly="readonly"/>  
 <input type='button' class='button' value='请选择 '  style="height:25px;"/>
    <input type="file" name="fileField" class="file" id="fileField" style='width:180px' onchange="document.getElementById('textfield').value=this.value" />
</div>
		
					
				</td>				
			</tr>
		</table>
	</form>	
  </body>  
  <script language="javascript">

  		
  		<%			
			if(request.getAttribute("filePath")!=null)
			{
		%>			
				$("#hidNewFileName").val("<%=request.getAttribute("filePath")%>");
		<%
			}		
		%>
  </script>
  <script language="javascript">
 		 var g_UploadResultObj = {};
  
		<%
			String reqFileName = "";
			String reqFileSrc = "";
			String reqFileSize = "";
			String reqFilePath = "";
			String reqFileOldName = "";
			String reqFileType = "";
			String regFileId = "";
			
			if(request.getAttribute("fileName")!=null)
			{	
				reqFileName = (String)request.getAttribute("fileName");
			}
			if(request.getAttribute("fileId")!=null)
			{	
				regFileId = (String)request.getAttribute("fileId");
			}
			if(request.getAttribute("fileSrc")!=null)
			{	
				reqFileSrc = ((String)request.getAttribute("fileSrc")).replaceAll("\\\\","\\\\\\\\");	
			}
			if(request.getAttribute("fileSize")!=null)
			{	
				reqFileSize = (String)request.getAttribute("fileSize");		
			}
			if(request.getAttribute("filePath")!=null)
			{	
				reqFilePath = (String)request.getAttribute("filePath");				
			}
			if(request.getAttribute("fileNameOld")!=null)
			{	
			    reqFileOldName = (String)request.getAttribute("fileNameOld");				
			}
			if(request.getAttribute("fileType")!=null)
			{	
			    reqFileType = (String)request.getAttribute("fileType");			
			}
			
			ArrayList alMsg = null;
			if(request.getAttribute("ValiMessage")!=null){
				alMsg = (ArrayList)request.getAttribute("ValiMessage");	
				if(alMsg.size()>0 && alMsg.get(0)!=null){
		%>	
					//document.getElementById("labelUpResult").innerText = "<=alMsg.get(0)%>";
					g_UploadResultObj.labelUpResult = "<%=alMsg.get(0)%>";
					$("hidUpResult").val("<%=alMsg.get(0)%>");
		<%		}
			}
		%>	
		
		g_UploadResultObj.labelFileNameNew = "<%=reqFileName%>";
		g_UploadResultObj.labelFileName = "<%=reqFileSrc%>";
		g_UploadResultObj.labelFileSize = "<%=reqFileSize%>";
		$("#hidFileSize").val("<%=reqFileSize%>");
		g_UploadResultObj.labelFilePath = "<%=reqFilePath%>";
		g_UploadResultObj.labelFileNameOld = "<%=reqFileOldName%>";
		$("#hidOldFileName").val("<%=reqFileOldName%>");
		g_UploadResultObj.labelFileType = "<%=reqFileType%>";
		g_UploadResultObj.labelFileId = "<%=regFileId%>";
		$("#hidUpResult").val("<%=reqFileType%>");
			
		function getUploadResultObj()
		{
			return g_UploadResultObj;				
		}
</script>
</html>
