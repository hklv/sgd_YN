<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../web.inc"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Highcharts Example</title>
		<script src="public/script/report/js/highcharts.js"></script>
		<script src="public/script/report/js/modules/exporting.js"></script>
		<script src="public/script/comm/uReport.js"></script>
		<script type="text/javascript">
    $(function(){
		var option = {};
    	option.title = "浏览器市场份额";
		option.type = "pie";  //饼图
		option.series =  [{
            name: '占比',  
            data: [['Firefox',  45.0],
                   ['IE', 26.8],
                   {
                       name: 'Chrome',
                       y: 12.8,
                       sliced: true,
                       selected: true
                   },
                   ['Safari',    8.5],
                   ['Opera',     6.2],
                   ['其他',   0.7]]
        }];
		$("#reportdiv").uReport(option);
  
	});
		</script>
	</head>
	<body>
	<br/><br/><br/>
<form name="frm" method="post">
 <label for="" id="label_name" style="text-decoration:underline;"></label>
</form>
<div id="reportdiv" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

	</body>
</html>
