<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../web.inc"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Highcharts Example</title>
		<script src="public/script/report/js/highcharts.js"></script>
		<script src="public/script/report/modules/exporting.js"></script>
		<script src="public/script/comm/uReport.js"></script>
		<script type="text/javascript">
		    $(function() {
		    	var option = {};
		    	option.title = 'Monthly Average Temperature',
		    	option.subtitle = "Source: WorldClimate.com";
				option.type = "line";  //线状图
				option.categories = [ 'Jan', 'Feb','Mar', 'Apr','May', 'Jun',  'Jul', 'Aug','Sep', 'Oct', 'Nov', 'Dec'];
				option.yAxis = {
		                min: 0,
		                title: {
		                	text: 'Temperature (°C)'
		                }
		            };
				option.series= [{
	                name: 'Tokyo',
	                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
	            }, {
	                name: 'New York',
	                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
	            }, {
	                name: 'Berlin',
	                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	            }, {
	                name: 'London',
	                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
	            }];
		    	$("#container").uReport(option);
		        
		    });
		    
				</script>
			</head>
			<body>
		<script src="http://code.highcharts.com/highcharts.js"></script>
		<script src="http://code.highcharts.com/modules/exporting.js"></script>

		<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

			</body>
		</html>

