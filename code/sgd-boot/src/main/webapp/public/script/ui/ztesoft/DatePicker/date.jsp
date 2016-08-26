<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="public/script/ui/ztesoft/DatePicker/js/WdatePicker.js"></script>
<script type="text/javascript">
			$(function(){
				$("#date1").focus(function(){
					new WdatePicker();
				})

			    $("#startTime1").click(function(){
			 		var myDate=new Date();
			        var min=(myDate.getFullYear()-1)+"-"+(myDate.getMonth()+7)+"-"+1;
			 		WdatePicker({
			 		    dateFmt:'yyyy-MM-dd',
			 		    minDate:min,
			 		    onpicked:function(){
			  				myDate.setFullYear($dp.cal.getP('y','yyyy'),$dp.cal.getP('M','MM'),1);
			  				var max= myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+(myDate.getDate()-1);
			  				$("#endTime1").val('').unbind('click').click(function(){
			  					WdatePicker({
			  						dateFmt:'yyyy-MM-dd',
			  						minDate:'#F{$dp.$D(\'startTime1\')}',
			  						maxDate:max
			  					});
			  				});
			   		 	} 
			   		});
			   	});	
			});

			
</script>

</head>
<body>
	<br/>
	
	<br/>
<p>
		简单写法1 <input type="text" id="date1" value='2011-02-21 14:12:20'
			style="width: 200x;" readonly />
	</p>
	<p>
		简单写法2 <input type="text" id="date2" class="Wdate"
			onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});"
			value='2011-02-21 14:12:20' style="width: 200x;" readonly />
	</p>
	<p>
		选择半年以内的一个月时间 <input type="text" id="startTime1" style="width: 200x;"
			readonly /> <input type="text" id="endTime1" style="width: 200x;"
			readonly />
	</p>
	<p>
		选择30天以内的时间 <input type="text" id="startTime"
			onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{d:-30})}'});"
			style="width: 200x;" readonly /> <input type="text" id="endTime"
			onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'#F{$dp.$D(\'startTime\',{d:30})}'});"
			style="width: 200x;" readonly />
	</p>
	-----------------------
	<table>
		<tr id="tr28ExpDate" style="display: block">
			<td id="td1" class="fieldNameCol1st" style="display: none">EFFECT_LENGTH><span
				class="mark">*<span></td>
			<td id="td2" class="fieldNameCol1st" style="display: none"><input
				type="text" class="text" id="txtQuantity" /></td>
			<td id="td3" class="fieldNameCol1st">RESERVATION_DATE<span
				class="mark">*<span></td>
			<td id='tdExpiryDate'><input id="tdExpiryDateR" type="text" /> <input
				type="image" src="jQuery/my97DatePicker/skin/datePicker.gif"
				width="16" height="22" align="absmiddle"
				onclick="new WdatePicker({el:'tdExpiryDateR'});" /></td>
		</tr>
		<tr id="trReactivate" style="display: block">
			<td>Action</td>
			<td><select id="selReactivate" onchange="chgReactivate();">
					<OPTION value="A">Reactivate</OPTION>
					<OPTION value="B">CHANGE_DEBTMANAGEMENT_BLOCKINGDATE</OPTION>
			</select></td>
		</tr>
	</table>



</body>
</html>