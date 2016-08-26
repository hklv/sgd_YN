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
			closeModalWindow(data);
			/* showMessage("Success to edit the role!",function(){
				
			});  */ 
		});
		
		var input = new Object();
		input.name='tom';
		input.age='20';
		input.city='nanjing';
		var callb = function(obj){
			if(obj != null){
				alert("callback name ==="+obj.name);
			}
			
		};
		$("#open").click(function(){
			showModalWindow('RSP manage','web/frm/demo/pop3.jsp',450,250,input,callb);
		});
		
		
	});
	
</script>

</head>
<body  leftmargin="30px" style="text-align: center">
<br/>
	<h2>popWin Demos</h2>
	<table style="text-align: center;margin: 0 auto;" height="1000px" width="1500px">
		<tr><td>name:</td><td><input type="text" id="name"/></td></tr>
		<tr><td>age:</td><td><input type="text" id="age"/></td></tr>
		<tr><td style="">city:</td><td><input type="text" id="city"/></td></tr>
		<tr><td colspan="1"><button id="submit">submit</button></td>
		<td><button id="close">close</button>&nbsp;<button id="open">open</button></td></tr>
	</table>


</body>
</html>