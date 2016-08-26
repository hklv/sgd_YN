<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@include file="../../../../../../../web.inc"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>grid</title>
        
    <!-- this gmaps key generated for http://openlayers.org/dev/ -->
    <script type="text/javascript">
  	  var website = "<%=WebRoot%>";
  	  var iconDir="public/script/ui/ztesoft/gis/icons";
    </script>
      <link rel="stylesheet" type="text/css" href="skins/default/zh_CN/css/jquery/jquery-easyui/default/easyui.css">
    <script type="text/javascript" src="public/script/jquery/jquery-easyui/jquery.easyui.min.js"></script>
	<script>
		$(function(){
			$('#test').datagrid({
				height:350,
				fitColumns:true,  //True 就会自动扩大或缩小列的尺寸以适应表格的宽度并且防止水平滚动。  默认false
				autoRowHeight: false,
				striped: true,  //True 就把行条纹化。（即奇偶行使用不同背景色）默认false
				nowrap:true,  //True 就会把数据显示在一行里。 默认 true
				singleSelect:true,  // True 就会只允许选中一行。 默认false
				checkbox:true,
				loadMsg:"Processing, please wait …",
				pageSize:20,
				url:'datagrid_data.json',
				sortName: 'code',
				sortOrder: 'desc',
				remoteSort: false,
				idField:'name',  //标识字段
				columns:[[
					{field:'id',title:'',checkbox:true},
					{field:'name',title:'名称',width:120},
					{field:'addr',title:'地址',width:220,rowspan:2,sortable:true,
						sorter:function(a,b){
							return (a>b?1:-1);
						}
					},
					{field:'phone',title:'Phone',width:150,rowspan:2}
				]],
				pagination:true,  //True 就会在 datagrid 的底部显示分页栏。默认false
				rownumbers:false, //True 就会显示行号的列。 默认false
			});
			

		});
		
		function getAjaxDate(oPage){
			 var pPageIndex = oPage.pageIndex;
		     var pPageSize =  oPage.pageSize;
		     var obj = {'total':100,'rows':[{name:'hanson',addr:'休斯顿',phone:"123456"},{name:'frank',addr:'纽约皇后大道',phone:"098765432"}]};
		     return obj;
		}
		function resize(){
			$('#test').datagrid('resize', {
				width:700,
				height:400
			});
		}
		function getSelected(){
			var selected = $('#test').datagrid('getSelected');
			if (selected){
				alert("name:"+selected.name+":addr: "+selected.addr);
			}
		}
		function getSelections(){
			var ids = [];
			var rows = $('#test').datagrid('getSelections');
			if(rows == null){
				return;
			}
			
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].name);
			}
			alert(ids.join(':'));
		}
		function clearSelections(){
			$('#test').datagrid('clearSelections');
		}
		function selectRow(){
			$('#test').datagrid('selectRow',2);
		}
		function selectRecord(){
			$('#test').datagrid('selectRecord','002');
		}
		function unselectRow(){
			$('#test').datagrid('unselectRow',2);
		}
	</script>
</head>
<body>
	<h2>Complex DataGrid</h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div>Click the button to do actions with datagrid.</div>
	</div>
	
	<div style="margin:10px 0;">
		<a href="#" onclick="getSelected()">GetSelected</a>
		<a href="#" onclick="getSelections()">GetSelections</a>
		<a href="#" onclick="selectRow()">SelectRow</a>
		<a href="#" onclick="selectRecord()">SelectRecord</a>
		<a href="#" onclick="unselectRow()">UnselectRow</a>
		<a href="#" onclick="clearSelections()">ClearSelections</a>
		<a href="#" onclick="resize()">Resize</a>
		<a href="#" onclick="mergeCells()">MergeCells</a>
	</div>
	<div style="width: 70%;margin-left: 20px;">
	<table id="test"></table>
	</div>
</body>
</html>
