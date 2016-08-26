(function($)
{
	if($)
	{
		$.fn.uReport = function (option){
			var type = option.type;
			var title = option.title;
			var subtitle = option.subtitle;
			var categories = option.categories;
			var yAxis = option.yAxis;
			var series = option.series;
			var container = $(this).attr("id");
			var tooltip ;
			if(type == "pie"){
				tooltip = {
	                pointFormat: '{series.name}: <b>{point.percentage}%</b>',
	            	percentageDecimals: 1
	            };
			}else if(type == "column"){
				tooltip = {
		                formatter: function() {
		                    return ''+
		                        this.x +': '+ this.y +' mm';
		                }
		            };
			}else if(type == "line"){
				tooltip = {
						formatter: function() {
		                    return this.x +': '+ this.y +'°C';
		                }
		            };
			}
			
			// Radialize the colors
			Highcharts.getOptions().colors = $.map(Highcharts.getOptions().colors, function(color) {
			    return {
			        radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
			        stops: [
			            [0, color],
			            [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
			        ]
			    };
			});
			
			// Build the chart
	       var chart = new Highcharts.Chart({
	            chart: {
	            	type: type,
	                renderTo: container,
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: true
	            },
	            title: {
	                text: title
	            },
	            subtitle: {
	                text: subtitle
	            },
	           /* tooltip: {
	        	    pointFormat: '{series.name}: <b>{point.percentage}%</b>',
	            	percentageDecimals: 1
	            },*/
	            tooltip: tooltip,
	            plotOptions: {
	                pie: {
	                    allowPointSelect: false,  //点击有没有效果
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: true,
	                        color: '#000000',  //文字颜色
	                        connectorColor: '#000000',  //连线颜色
	                        formatter: function() {
	                            return '<b>'+ this.point.name +'</b>: '+ this.percentage +' %';
	                        }
	                    }
	                },
	                column: {
	                    pointPadding: 0.2,
	                    borderWidth: 0
	                }
	            },
	            xAxis: {
	                categories: categories
	            },
	            yAxis: yAxis,
	            legend: {
	                layout: 'vertical',
	                backgroundColor: '#FFFFFF',
	                align: 'left',
	                verticalAlign: 'top',
	                x: 100,
	                y: 70,
	                floating: true,
	                shadow: true
	            },
	            
	            series:series
	        });
	        
		}
	}
})(jQuery);