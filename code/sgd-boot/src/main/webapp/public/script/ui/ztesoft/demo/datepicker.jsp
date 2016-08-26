<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<script src="<%=WebRoot%>public/script/ui/ztesoft/DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		$("#date1").focus(function() {
			new WdatePicker();
		})

		$("#startTime1")
				.click(
						function() {
							var myDate = new Date();
							var min = (myDate.getFullYear() - 1) + "-"
									+ (myDate.getMonth() + 7) + "-" + 1;
							WdatePicker({
								dateFmt : 'yyyy-MM-dd',
								minDate : min,
								onpicked : function() {
									myDate.setFullYear($dp.cal
											.getP('y', 'yyyy'), $dp.cal.getP(
											'M', 'MM'), 1);
									var max = myDate.getFullYear() + "-"
											+ (myDate.getMonth() + 1) + "-"
											+ (myDate.getDate() - 1);
									$("#endTime1")
											.val('')
											.unbind('click')
											.click(
													function() {
														WdatePicker({
															dateFmt : 'yyyy-MM-dd',
															minDate : '#F{$dp.$D(\'startTime1\')}',
															maxDate : max
														});
													});
								}
							});
						});
	});
</script>

</head>
<body>
	<br />
	<br />
	<br />
	<div style="margin-left: 20px;">
		<p>
			普通用法 :<input type="text" id="date1"  class="Wdate"
				style="width: 200x;" readonly />
		</p>
		<p>
			日期带时间:  <input type="text" id="date2" class="Wdate"
				onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"
				value='2011-02-21 14:12:20' style="width: 200x;" readonly />
		</p>
		<p>
			选择半年以内的一个月时间 : <input type="text" id="startTime1" style="width: 200x;"
				readonly /> <input type="text" id="endTime1" style="width: 200x;"
				readonly />
		</p>
		<p>
			选择30天以内的时间 : <input type="text" id="startTime"
				onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{d:-30})}'});"
				style="width: 200x;" readonly /> <input type="text" id="endTime"
				onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'#F{$dp.$D(\'startTime\',{d:30})}'});"
				style="width: 200x;" readonly />
		</p>
		
		</div>
</body>
</html>
