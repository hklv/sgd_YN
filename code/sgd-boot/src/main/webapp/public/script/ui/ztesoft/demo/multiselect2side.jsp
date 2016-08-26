<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"	href="<%=WebRoot%>public/script/ui/ztesoft/multiselect2side/css/jquery.multiselect2side.css" />
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/multiselect2side/js/jquery.multiselect2side.js"></script>
<title>双向多选</title>
<style type="text/css">
		
	</style>
<script language="javascript">
	$(function(){
		$("#liOption").multiselect2side({
		    selectedPosition: 'right',
		    moveOptions: false,
			labelsx: '待选区',
			labeldx: '已选区'
	   });
	});
</script>
</head>

<body style="overflow:hidden;">

<div id="main">
  <h2 class="top_title"><a href="http://www.helloweba.com/view-blog-50.html">操作多选列表框</a></h2>
      <select name="liOption[]" id='liOption' multiple='multiple' size='8'  >
        <option value="PHP">PHP</option>
        <option value="MYSQL"  selected="selected">MYSQL</option>
        <option value="ASP" selected="selected">ASP.NET</option>
        <option value="XHTML">XHTML</option>
        <option value="CSS">CSS</option>
        <option value="JQUERY">JQUERY</option>
      </select>
</div>
</body>
</html>