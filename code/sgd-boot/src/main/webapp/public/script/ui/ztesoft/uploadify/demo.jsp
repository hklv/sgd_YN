<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>Uploadify上传</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" href="uploadify.css" type="text/css"></link>
		<script type="text/javascript" src="jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="jquery.uploadify-3.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path%>/skins/default/zh_CN/css/jquery/jquery-easyui/default/easyui.css">
    <script type="text/javascript" src="<%=path%>/public/script/jquery/jquery-easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript">
			  
			$(function() {
			    $("#file_upload").uploadify({
			    	'height'        : 27, 
			    	'width'         : 80,  
			    	'buttonText'    : '浏 览',
			        'swf'           : 'uploadify.swf',
			        'uploader'      : '<%=path%>/UploadServlet',
			        'auto'          : false,
			        'fileTypeExts'  : '*',
			        'formData'      : {'userName':'','qq':''},  //提交给后台的附加信息
			        'onUploadStart' : function(file) {
			        	$("#file_upload").uploadify("settings", "formData");
			        	//$("#file_upload").uploadify("settings", "qq", );
			        },
			        'onUploadSuccess':function(file, data, response){
			        	 $.messager.show({  
			                 title:'上传成功',  
			                 msg:'图片上传成功.',  
			                 showType:'show'  
			             });
			        	 imgtodb(data);
			        }
			    });
			});
			
			function imgtodb(imgpath){
				//debugger;
				
			}
		
	</script>
	</head>

	<body>

		<br>
		<input type="file" name="uploadify" id="file_upload" />
		<hr>
		<a href="javascript:$('#file_upload').uploadify('upload','*')">开始上传</a>&nbsp;   
        <a href="javascript:$('#file_upload').uploadify('cancel', '*')">取消所有上传</a> 
	</body>
</html>
