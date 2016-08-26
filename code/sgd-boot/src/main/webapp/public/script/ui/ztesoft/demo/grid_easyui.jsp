<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="skins/default/zh_CN/css/jquery/jquery-easyui/default/easyui.css">
<script type="text/javascript" src="public/script/jquery/jquery-easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=WebRoot%>public/script/ui/ztesoft/grid/js/grid.js"></script>

<script type="text/javascript">
var cols = [ {display : '地区ID', name : 'AREA_ID',	width : 80,	sortable : true, align : 'center',hide :false}, 
             {display : '父ID', name : 'PARENT_ID', width : 80, sortable : true, align : 'center'}, 
             {display : '地区名称', name : 'AREA_NAME', width : 280, sortable : true, align : 'center'}, 
             {display : '备注', name : 'COMMENTS', width : 220, sortable : true, align : 'center',hide :false} ];
	
	var grid2;
	$(function() {
		 grid2 = $("#grid2").GridCreate({
			params : [],
			colModel : cols,
			usepager : false,
			rp:10,
			checkbox: true,
			sFirstRow: true,
			singleSelect: true,  //默认true，如果需要checkbox选中多行，需要传入false
			height:350,
			onSelectChange : selectChange,
			onDoubleClick: doubleClick,
			localQuery: queryMyself,
			onError : function(XMLHttpRequest) {
				showException(XMLHttpRequest);
			}
		}); 
	});
	function changeData(data){
		for(i=0;i<data.rows.length;i++){
			data.rows[i].cell.AREA_NAME="TEST";
		}
	}
	function selectChange(item,index){
		if(item == null){
			return;
		}
		//console.log("Aread ID:" + item.AREA_ID + " : AREA_NAME:" + item.AREA_NAME);
	}
	function doubleClick(item){
		//console.log("double click == "+"Aread ID:" + item.AREA_ID);
	}
	
	var cc1="<INPUT name=\"test\" id='text1'  style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc2="<INPUT name=\"test\" id='text2' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc3="<INPUT name=\"test\" id='text3' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc4="<INPUT name=\"test\" id='text4' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc5="<INPUT name=\"test\" id='text5' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc6="<INPUT name=\"test\" id='text6' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc7="<INPUT name=\"test\" id='text7' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc8="<INPUT name=\"test\" id='text8' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	var cc9="<INPUT name=\"test\" id='text9' style=\"BORDER-BOTTOM: #939185 1px solid; BORDER-LEFT: #939185 1px solid; BORDER-TOP: #939185 1px solid; BORDER-RIGHT: #939185 1px solid\" id=txtCirBW179 class=text >";
	
	var ss="<select id='selectss' onchange='selectchange()'></select>";
	var ss1="<select id='select1' ><option val='1'>1</option><option val='22'>22</option><option val='3'>3</option></select>";
	var ss2="<select id='select2' ><option val='1'>1</option><option val='22'>22</option><option val='3'>3</option></select>";
	var ss3="<select id='select3' ><option val='1'>1</option><option val='22'>22</option><option val='3'>3</option></select>";
	
	var testData = {rows: [{AREA_ID : '1',PARENT_ID : "ss1", AREA_NAME : 'Test1', COMMENTS : "ccccccccc"},
	                       {AREA_ID : '2',PARENT_ID : "ss1", AREA_NAME : 'Test2', COMMENTS : "ccccccccc"},
	                       {AREA_ID : '3',PARENT_ID : "ss1", AREA_NAME : 'Test3', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '4',PARENT_ID : "ss1", AREA_NAME : 'Test4', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '5',PARENT_ID : "ss1", AREA_NAME : 'Test5', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '6',PARENT_ID : "ss1", AREA_NAME : 'Test6', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '7',PARENT_ID : "ss1", AREA_NAME : 'Test7', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '8',PARENT_ID : "ss1", AREA_NAME : 'Test8', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '9',PARENT_ID : "ss1", AREA_NAME : 'Test9', COMMENTS : "ccccccccc"}
                    	   ,{AREA_ID : '10',PARENT_ID : "ss1", AREA_NAME : "ccccccccc", COMMENTS : "ccccccccc"}
                    	   ], 
	                       page: 1, 
	                       total: 1900};
	var testData1 = {rows: [{AREA_ID : '11',PARENT_ID : "test", AREA_NAME : 'Test1', COMMENTS : "ccccccccc"},
	                       {AREA_ID : '12',PARENT_ID : "test", AREA_NAME : 'Test2', COMMENTS : "ccccccccc"},
	                       {AREA_ID : '13',PARENT_ID : "test", AREA_NAME : 'Test3', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '14',PARENT_ID : "test", AREA_NAME : 'Test4', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '15',PARENT_ID : "test", AREA_NAME : 'Test5', COMMENTS : "ccccccccc"},
                    	   {AREA_ID : '16',PARENT_ID : "test", AREA_NAME : 'Test6', COMMENTS : "ccccccccc"},
                    	  {AREA_ID : '17',PARENT_ID : "test", AREA_NAME : 'Test7', COMMENTS : "ccccccccc"},
                    	  {AREA_ID : '18',PARENT_ID : "test", AREA_NAME : 'Test8', COMMENTS : "ccccccccc"},
                    	  {AREA_ID : '19',PARENT_ID : "test", AREA_NAME : 'Test9', COMMENTS : "ccccccccc"}
                    	  ,{AREA_ID : "20",PARENT_ID : "ccccccccc", AREA_NAME : "ccccccccc", COMMENTS : "ccccccccc"}
                    	   ], 
	                       page: 2, 
	                       total: 19};
	var testData2 = {rows: [{AREA_ID : '1',PARENT_ID : ss1, AREA_NAME : 'Test1', COMMENTS : cc1},
		                       {AREA_ID : '3',PARENT_ID : ss3, AREA_NAME : 'Test3', COMMENTS : ss}
	                    	   ], 
		                       page: 2, 
		                       total: 3};
   
	
	function usePager_click() {
		if($("#checkbox1").attr("checked") == true){
			$("#t1").css({"display":""});
			$("#t2").css({"display":"none"});
			$("#btn_ld").css({"display":"none"});
		}else{
			$("#t1").css({"display":"none"});
			$("#t2").css({"display":""});
			$("#btn_ld").css({"display":""});
		}

	}
	
	function queryMyself(param){
		var pageSize = param[1].value;
		var pageIndex = param[0].value;
		if(pageIndex == 1){
			return testData;
		}else{
			return testData1;
		}
		
	}
	
	function getchecked(){
		//var item = $("#grid2").GridSelectedItem();
		var items = $("#grid2").getCheckedRows();
		//var items = $("#grid2").getAllRows();
		alert(items.length+" selected..");
	}
	
	function getselected(){
		var item = grid2.GridSelectedItem();
		//var items = grid2.getAllRows();
		if(item == null){
			alert("none selected..");
		}else{
			alert(item.AREA_NAME+" selected..");
		}
		
		var i = item;
	}
	
	function getSelections(){
		var items =  $("#grid2").getSelections();
		alert(items.length +"selected...");
	}
	
	function lodaData_click() {
		 var testData3 = {rows: [ {AREA_ID : 'a1',PARENT_ID : "123", AREA_NAME : 'Test1', COMMENTS : "comments"},
		                          {AREA_ID : 'a2',PARENT_ID : "456", AREA_NAME : 'Test2', COMMENTS : "comments"},
		                          {AREA_ID : 'a3',PARENT_ID : "78", AREA_NAME : 'Test3', COMMENTS : "comments"},
		                          {AREA_ID : 'a4',PARENT_ID : "99", AREA_NAME : 'Test4', COMMENTS : "comments"},
		                          {AREA_ID : 'a5',PARENT_ID : "123", AREA_NAME : 'Test1', COMMENTS : "comments"},
		                          {AREA_ID : 'a6',PARENT_ID : "456", AREA_NAME : 'Test2', COMMENTS : "comments"},
		                          {AREA_ID : 'a7',PARENT_ID : "78", AREA_NAME : 'Test3', COMMENTS : "comments"},
		                          {AREA_ID : 'a8',PARENT_ID : "99", AREA_NAME : 'Test4', COMMENTS : "comments"},
		                          {AREA_ID : 'a9',PARENT_ID : "123", AREA_NAME : 'Test1', COMMENTS : "comments"},
		                          {AREA_ID : 'a10',PARENT_ID : "456", AREA_NAME : 'Test2', COMMENTS : "comments"}
		                     	   ], 
		                         page: 1, 
		                         total: 19};
		$("#grid2").GridLoadData(testData3);
		//$(".grid2").GridLoadData(null);
		//grid2.getCheckedRows();
		//var item = grid2.GridSelectedItem();
		//alert(item);
	}
	function clearData_click() {
		$("#grid2").GridLoadData(null);
	}
	function delselected(){
		grid2.delSelectedRow();
	}
	function delOneRow(){
		grid2.delOneRow(1);
	}
	function selectrow(){
		var obj = grid2.selectRow(2);
		alert(obj.AREA_NAME);
		//getselected();
	}
	function insertrow(){
		var rowData = {AREA_ID : '11',PARENT_ID : '100', AREA_NAME : 'Test11', COMMENTS : 'Test11COMMENTS'};
		var row = grid2.insertRow(rowData);
		//grid2.selectRow(row);
	}
	function updaterow(){
		var rowData = {AREA_ID : '20',PARENT_ID : '20', AREA_NAME : 'Test2', COMMENTS : 'Test20COMMENTS'};
		grid2.updateRow(rowData);
	}
	function checkrow(){
		$("#grid2").checkRows([1,3],true);
	}
	function disableRows(){
		$("#grid2").disableRows([1,3],true);
	}

	function refresh(){
		grid2.refresh();
	}
	
	function test(com, grid) {
		if (com == 'Delete') {
			alert('Delete ' + $('.trSelected', grid).length + ' items?');
		} else if (com == 'Add') {
			alert('Add New Item');
		}
	}
	var tmp=[];
	function getinput(){
		$("#grid2").find("input:text").each(function(i){
			//$(this).val();
			tmp[tmp.length]={"id":this.id,"value":$(this).val()};
		});
	}
	function setinput(){
		$.each(tmp,function(){
			$("#"+this["id"]).val(this["value"]);
		});
		tmp=[];
	}
	function initselect1(){
		 var param = [];
	 		addParam("ONLY_CONTENT_FIRST_PATH", 1, param);
			var ret = callRemoteQueryFunction("QryAgentList", param, null);
			$.initSelect("selectss",ret,["ORG_NAME","ORG_ID"]);
			$("#selectss").attr("disabled","disabled");
			  
	}
	function selectchange(){
		var obj = $("#selectss").find("option:selected").data("data");
		alert(obj.CUST_ID);
	}
	function dealData(data){
		data.rows[0].cell["AREA_NAME"]="CCCCCCCCCCCCCCCCCC";
	}
	function getGridData(){
		debugger;
		var data = $("#grid2").getGridData();
		alert(data);
	}
	function destroy(){
		$("#grid2").destroy();
	}
</script>

</head>
<body>
	<br/>
	
	<div class="layout_detail" id="divStaffDetail" >
		<table border=0 cellpadding=0 cellspacing=0>
			
						<tr>
							<td colspan="4" align="center">
								<!-- <input type="button" onclick="search_click();" value="search" /> -->
								
								<input id="btn_ld"  type="button" onclick="lodaData_click();" value="loadLocalData" class="button"/>
								<input id="btn_clear"  type="button" onclick="clearData_click();" value="clear" class="button"/>
								<input type="button" onclick="getchecked();" value="getchecked" class="button"/>
								<input type="button" onclick="getselected();" value="getselected" class="button"/>
								<input type="button" onclick="getSelections();" value="getSelections" class="button"/>
								<input type="button" onclick="delselected();" value="delselected" class="button"/>
								<input type="button" onclick="delOneRow();" value="删除一行" class="button"/>
								<input type="button" onclick="selectrow();" value="selectrow" class="button"/>
								<input type="button" onclick="insertrow();" value="insertrow" class="button"/>
								<input type="button" onclick="updaterow();" value="updaterow" class="button"/>
								<input type="button" onclick="checkrow();" value="checkrow" class="button"/>
								<input type="button" onclick="disableRows();" value="disableRows" class="button"/>
								<!-- <input type="button" onclick="sortgrid();" value="sortgrid" class="button"/>
								<input type="button" onclick="refresh();" value="refresh" class="button"/>
								<input type="button" onclick="initselect1();" value="initselect1" class="button"/> -->
								<input type="button" onclick="getGridData();" value="getGridData" class="button"/>
								<input type="button" onclick="destroy();" value="destroy" class="button"/>
							</td>
						</tr>
					</table></td>
				<td class="bkg"></td>
			</tr>
			<tr>
				<td class="img3"></td>
				<td class="bkg"></td>
				<td class="img4"></td>
			</tr>
		</table>
	</div>

	<br/>

	<div class="" id="divStaffDetail" style="width: 90%">
		<table border=0 cellpadding=0 cellspacing=0 width="100%" id="testtb">
			<tr>
				<td class="img1"></td>
				<td class="bkg"></td>
				<td class="img2"></td>
			</tr>
			<tr>
				<td class="img1"></td>
				<td class="bkg"></td>
				<td class="img2"></td></tr>
			<tr id="t2" >
				<td class=fieldNameCol1st align="left" colspan="3" id="gridtd2" style="padding-left: 10px;">
					<div id="grid2div"  style="" valign="top">
						<table class="grid1" id="grid2"></table>
					</div>
				</td>
			</tr>
			<tr>
				<td class="img3"></td>
				<td class="bkg"></td>
				<td class="img4"></td>
			</tr>
		</table>
	</div>


</body>
</html>