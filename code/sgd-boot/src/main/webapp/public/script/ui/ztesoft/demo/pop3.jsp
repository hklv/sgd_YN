<%@include file="../../../../../web.inc"%>
<html>
<head>
<title>window</title>
	<style type="text/css">
		body {
			font-size: 62.5%;
		}
	</style>
<script type="text/javascript">
	$(function(){
		var intObj = getModalInputObj();
		if(intObj != null){
			$("#name").val(intObj.name);
			$("#age").val(intObj.age);
			$("#city").val(intObj.city);
		}
		
		$("#close").click(function(){
			closeModalWindow();
		});
		$("#submit").click(function(){
			var data = new Object();
			data.name = $("#name").val();
			data.age = $("#age").val();
			data.city = $("#city").val();
			showMessage("Success to edit the role!",function(){
				closeModalWindow(data);
			});  
		});
		
	});
</script>

</head>
<body leftmargin="30px" style="text-align: center">
<br/>
	<h2>popWin Demos</h2>
	<table style="text-align: center;margin: 0 auto;" height="1000px" width="1500px">
		<tr><td>name:</td><td><input type="text" id="name"/></td></tr>
		<tr><td>age:</td><td><input type="text" id="age"/></td></tr>
		<tr><td style="">city:</td><td><input type="text" id="city"/></td></tr>
		<tr><td colspan="1"><button id="submit">submit</button></td>
		<td><button id="close">close</button></td></tr>
	</table>


</body>
</html>