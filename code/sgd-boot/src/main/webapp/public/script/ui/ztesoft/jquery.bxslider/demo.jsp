<%@include file="../../../../../web.inc"%>
<html>
<head>
<title>window</title>
<script src="public/script/ui/ztesoft/jquery.bxslider/jquery.bxslider.min.js"></script>
<!-- bxSlider CSS file -->
<link href="public/script/ui/ztesoft/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
<script type="text/javascript">
	$(function(){
		$(document).ready(function(){
			  $('.bxslider').bxSlider({
				  auto:true
			  });
			});
	});
	//window.event.cancelBubble = true;
</script>

</head>
<body  leftmargin="30px" style="text-align: center">
<br/>
		<ul class="bxslider">
		  <li><img src="public/script/ui/ztesoft/jquery.bxslider/images/1.jpg" /></li>
		  <li><img src="public/script/ui/ztesoft/jquery.bxslider/images/2.jpg" /></li>
		  <li><img src="public/script/ui/ztesoft/jquery.bxslider/images/3.jpg" /></li>
		</ul>


</body>
</html>