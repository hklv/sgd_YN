<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="public/script/ui/ztesoft/popedit/style/PopEdit.css"/>
<script type="text/javascript" src="public/script/ui/ztesoft/popedit/js/PopEdit.js"></script>
<script type="text/javascript">
			$(function(){
				$("#method").PopEdit({
					url:"public/script/ui/ztesoft/popedit/poppop.jsp",   //弹出页面的url
					windowName: "Info",  //弹出窗口的标题
					windowWidth: 500,  	//弹出窗口的宽度
					windowHeight: 200,	 //弹出窗口的高度
					//getInitObj: getInitObj,  //窗口打开时调用，来查询子窗口的输入对象
					onClose: closeFun,			//弹出窗口关闭回调
					onClear: clearFun   //清空的时候回调
				});
			});

			function closeFun(returnObj){
				
			}
			
			function clearFun(returnObj){
				
			}
			
			function getInitObj(){
				return {idVal:11,textVal:"ztesoft"};
			}
			
			function disBtn(){
				$("#method").setDisable();
			}
			
			function enableBtn(){
				$("#method").setEnable();
			}
			
			function setvalue(){
				$("#method").setVal({idVal:13,textVal:"IBM"});
			}
			
			function getvalue(){
				alert($("#method").getVal().idVal+"=="+$("#method").getVal().textVal);
			}
			
			function clearInput(){
				$("#method").clearVal();
			}
</script>

</head>
<body>
	<br/>
	
	<br/>

	<div class="" id="divStaffDetail" style="width: 30%;margin: 30px;">
		<table border=0 cellpadding=0 cellspacing=0 width="400px;" id="testtb">
			<tr>
				<td class="img1" width="50px;"></td>
				<td class="bkg" width="350px;"></td>
			</tr>
			<tr id="t2" >
				<td>url:</td>
				<td id="method" ></td>
			</tr>
			<tr id="t3" >
				<td>name:</td>
				<td id="name">
				<input type="text" class="text"/>
				</td>
			</tr>
			<tr>
				<td class="img3"></td>
				<td class="bkg"></td>
			</tr>
			<tr>
				<td colspan="10">
					<button onclick="disBtn();" >disable</button>
					<button onclick="enableBtn();">enable</button>
					<button onclick="setvalue();" >setvalue</button>
					<button onclick="getvalue();" >getvalue</button>
					<button onclick="clearInput();">clear</button>
				</td>
			</tr>	
		</table>
	</div>


</body>
</html>