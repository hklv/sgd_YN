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
		var initObj = getModalInputObj();
		if(initObj != null){
			$("#name").val(initObj.textVal);
		}
		
		$("#close").click(function(){
			closeModalWindow();
		});
		$("#submit").click(function(){
			var data = new Object();
			data.textVal = $("#name").val();
			data.idVal = 55;
			closeModalWindow(data);
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
			showModalWindow('RSP manage','public/script/ui/ztesoft/demo/pop2.jsp',550,300,input,callb);
		});
		
	});
	//window.event.cancelBubble = true;
</script>

</head>
<body  leftmargin="30px" style="text-align: center">
<br/>
	<table style="text-align: center;margin: 0 auto;" id="tabble1" >
		<tr>
			<td>name:</td>
			<td width="200px"><input type="text" id="name"/></td>
			<td width="100px" ><button id="submit">submit</button></td>
			<td width="100px"><button id="close">close</button>&nbsp;</td>
		</tr>
	</table>


</body>
</html>