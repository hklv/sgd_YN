<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="public/script/ui/ztesoft/tree/js/TreeList.js"></script>
<style type="text/css">
		.lockicon{
			display:inline-block;
			background:url('http://127.0.0.1:8080/uboss/skins/default/en_US/img/foldericon/locklock.png') no-repeat;
			width:16px;
			height:18px;
			vertical-align:middle;
		}
	</style>
<script type="text/javascript">
			$(function(){
				var treeData = [{  
				    "id": 1,  
				    "text": "Node 1",  
				    "state": "open",  
				    "children": [{  
				        "id": 11,  
				        "text": "Node 11"  
				    },{  
				        "id": 12,  
				        "text": "Node 12"  
				    }]  
				},{  
				    "id": 2,  
				    "text": "Node 2",  
				    "iconCls":"lockicon",
				    "state": "closed"  
				}]  ;
				
				
				$("#tree").TreeList({
					onClick:onTreeClick,
					onDblClick:onTreeDbClick,
					onLoadSuccess:onTreeLoadSuccess,
					onExpand:onExpand,
					onCheck:onTreeCheck,
					
					data: treeData
				});
			});
			
			
			function onTreeClick(node){
				
			}
			
			function onTreeDbClick(node){
				
			}
			
			function onTreeLoadSuccess(node, data){
				
			}
			
			function onTreeCheck(node, checked){
				
			}
			
			function onExpand(node){
				
			}
			
			function loadData(){
				var treeData = [{  
				    "id": 1,  
				    "text": "Node 1",  
				    "state": "open",  
				    "children": [{  
				        "id": 11,  
				        "text": "Node 11"  
				    },{  
				        "id": 12,  
				        "text": "Node 12"  
				    }]  
				},{  
				    "id": 2,  
				    "text": "Node 2",  
				    "state": "closed"  
				},{  
				    "id": 3,  
				    "text": "Node 4",  
				    "state": "closed"  
				}]  ;
				
				$("#tree").loadData(treeData);
			}
			
			function  findNode(){
				$("#tree").findNode(11);
			}
			
			function getSelected(){
				var item = $("#tree").getSelected();
				//debugger;
			}
			
			function getRootNode(){
				var node = $("#tree").getRootNode();
				alert(node.id);
			}
			
			function getRootNodes(){
				var roots = $("#tree").getRootNodes();
				alert(roots.length);
			}
			
			function getParent(){
				var node = $("#tree").getParent();
				alert(node);
			}
			
			function getChildren(){
				$("#tree").getChildren();
				alert(node);
				
			}
			
			function isLeaf(){
				var isLeaf = $("#tree").isLeaf();
				alert(isLeaf);
			}
			
			function expand(){
				$("#tree").expand();
			}
			
			function collapse(){
				$("#tree").collapse();
			}
			function insertNode(){
				$("#tree").insertNode({id:999,text:"newnode"});
			}
			function appendNode(){
				$("#tree").appendNode({id:999,text:"newnode"});
			}
			function updateNode(){
				$("#tree").updateNode("updatenode");
			}
			function removeTreeNode(){
				$("#tree").removeNode();
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
								
								<input id="btn_ld"  type="button" onclick="loadData();" value="loadLocalData" class="button"/>
								<input   type="button" onclick="getSelected();" value="getSelected" class="button"/>
								<input   type="button" onclick="getRootNode();" value="getRootNode" class="button"/>
								<input   type="button" onclick="getRootNodes();" value="getRootNodes" class="button"/>
								<input   type="button" onclick="getParent();" value="getParent" class="button"/>
								<input   type="button" onclick="isLeaf();" value="isLeaf" class="button"/>
								<input   type="button" onclick="expand();" value="expand" class="button"/>
								<input   type="button" onclick="collapse();" value="collapse" class="button"/>
								<input   type="button" onclick="insertNode();" value="insertNode" class="button"/>
								<input   type="button" onclick="appendNode();" value="appendNode" class="button"/>
								
								<input   type="button" onclick="updateNode();" value="updateNode" class="button"/>
								<input   type="button" onclick="removeTreeNode();" value="removeNode" class="button"/>
								
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

	<div class="" id="divStaffDetail" style="width: 30%">
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
					<div id="treediv"  style="" valign="top">
						<ul id="tree"></ul>  
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