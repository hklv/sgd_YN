<%@include file="../../../../../web.inc"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>notify</title>
	
<script type="text/javascript">
	$(function() {
		$("#info").click(function(){
			showMessage('Here is a message.',function(){
				alert('infomation ok...');
			});
		});
		
		$("#warning").click(function(){
			showWarning('Here is a warning!',function(){
				alert('warning ok...');
			});
		});
		
		$("#error").click(function(){
			showError('Receive a new error.',null,function(){
				alert('error ok...');
			});
		});
		$("#alarm").click(function(){
			showAlarm('Receive a new alarm.',null,function(){
				alert('error ok...');
			});
			$(".popRadius").css({"background-color":"#F0BA4D"});
			$(".ui-dialog-buttonpane").css({"background-color":"#F0BA4D"});
		});
		$("#confirm").click(function(){
			showConfirm('Are you sure to delete it?',function(){
				//alert('confirm ok...');
			},function(){
				//alert('cancel ok...');
			});
		});
	});
	
</script>
</head>
<body style="overflow-x: hidden">
	<h2>Simple Demos</h2>
	<button id="confirm">confirm</button>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

		<form style="margin: 20px 0">
			<input type="button" id="info" value="alert" />
			<input type="button" id="warning" value="warning" />
			<input type="button" id="error" value="error" />
			<input type="button" id="alarm" value="alarm" />
		</form>

</body>
</html>