<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>初始化select</title>
<style type="text/css">
		
	</style>
<script language="javascript">
	$(function(){
		 var param = [];
 		addParam("ONLY_CONTENT_FIRST_PATH", 1, param);
		var ret = callRemoteQueryFunction("QryAgentList", param, null);
		$.initSelect("rsp",ret,["ORG_NAME","ORG_ID"]);
		
		$("#rsp").change(function(){
			var obj = $("#rsp").find("option:selected").data("data");
			alert(obj.CUST_ID);
		}); 
	});
</script>
</head>

<body>

<h2>--------------------</h2>
<select id="rsp" style="width:200px;"></select>
</body>
</html>