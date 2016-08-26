<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>

<link href="<%=WebRoot%>public/script/ui/ztesoft/treetable/css/jquery.treeTable.css" rel="stylesheet" type="text/css" />
<script src="<%=WebRoot%>public/script/ui/ztesoft/treetable/js/jquery.treeTable.js" type="text/javascript"></script>
<style>
td,th {
	border: 0px solid;
}
</style>
<script>
	var webroot = "<%=WebRoot%>";
	//用于构建动态表格的数据集
	var datastore=[{name:'节点1',id:'node1',parent:null,commons:"demo"},
	               {name:'节点8',id:'node8',parent:null,commons:"demo"},
		            {name:'节点2',id:'node2',parent:'node1',commons:"demo"},
		            {name:'节点11',id:'node11',parent:'node8',commons:"demo"},
		            {name:'节点3',id:'node3',parent:'node2',commons:"demo"},
		            {name:'节点4',id:'node4',parent:'node3',commons:"demo"},
		            {name:'节点5',id:'node5',parent:'node4',commons:"demo"},
		            {name:'节点6',id:'node6',parent:'node5',commons:"demo"},
		            {name:'节点7',id:'node7',parent:'node3',commons:"demo"},
		            {name:'节点9',id:'node9',parent:'node1',commons:"demo"},
		            {name:'节点10',id:'node10',parent:'node9',commons:"demo"}
		            ];
	$(function() {
		/* $("#tree_table").treeTable({
			expandable : true,
			clickableNodeNames : true,
			childPrefix : 'child-of-',
			treeColumn : 1
		}); *///treeColumn：0表示表格第二列显示树形结构。但是会影响此表单之后的树形表单显示
		for ( var i = 0; i < datastore.length; i++) {
			filltr($("#tree_table2"), datastore[i]);
		}
		$("#tree_table2").treeTable({
			reveal:false,
			clickableNodeNames:false
			});
		$("table#tree_table2 tbody tr").mousedown(function() {
			  $("tr.selected").removeClass("selected"); // Deselect currently selected rows
			  $(this).addClass("selected");
			  //clearUserDetail();
			 // selectedOrgID = this.id;
			 // getStaffByOrg(this.id);
		});
	});
	function filltr(apendtotable, dataitem) {
		//console.log("level=="+dataitem.level);
	if (dataitem.parent == null) {
		$("tbody", apendtotable).append(
				//<tr id='node--2' class='parent '><td><span class='folder'>CHUD</span></td><td>Folder</td><td>--</td></tr>
				"<tr id='"+dataitem.id+"' class='parent expanded'><td width='65%' class='treetd'>"
				+'<SPAN  class=folder>'
						+ dataitem.name + "</SPAN></td><td>commons</td></tr>");
	} else {
		$("#" + dataitem.parent, apendtotable).addClass("parent");
		$("#" + dataitem.parent, apendtotable).find(".treetd").find(".file").removeClass("file").addClass("folder");
		$("#" + dataitem.parent, apendtotable).after(
				"<tr id='"+dataitem.id+"' class='child-of-"+dataitem.parent+"'><td class='treetd'>"
				+'<SPAN  class=file>'+dataitem.name + "</span>" + "</td><td>"+dataitem.commons+"</td></tr>");
	}
}
	
</script>
</head>
<body style="margin-left: 0px">
	<br/>
	<div style="margin-left: 30px;">
	<table id="tree_table2" style="width: 300px;margin-left: 0px;" border="0">
		<tbody></tbody>
	</table> 
	</div>
	<br/>
	<!--  <table id="tree_table" width="300px">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<th>名称</th>
				<th width="50px">操作</th>
			</tr>
		</thead>
		<tbody>
			<tr id="node-1">
				<td><input type="radio" name="tt">
				</td>
				<td>第一级1</td>
				<td>&nbsp;&nbsp;操作</td>
			</tr>
			<tr id="node-2" class="child-of-node-1">
				<td><input type="radio" name="tt">
				</td>
				<td>第二级</td>
				<td>&nbsp;&nbsp;操作</td>
			</tr>
			<tr id="node-3" class="child-of-node-2">
				<td><input type="radio" name="tt">
				</td>
				<td>第三级</td>
				<td>&nbsp;&nbsp;操作</td>
			</tr>
			<tr id="node-4" class="child-of-node-3">
				<td><input type="radio" name="tt">
				</td>
				<td>第四级</td>
				<td>&nbsp;&nbsp;操作</td>
			</tr>
			<tr id="node-5" class="child-of-node-4">
				<td><input type="radio" name="tt">
				</td>
				<td>第五级</td>
				<td>&nbsp;&nbsp;操作</td>
			</tr>
			<tr id="node-6">
				<td><input type="radio" name="tt">
				</td>
				<td>第一级2</td>
				<td>&nbsp;&nbsp;操作</td>
			</tr>
		</tbody>
		<tfoot>
		</tfoot>
	</table>-->
	<hr>
	
</body>
</html>
