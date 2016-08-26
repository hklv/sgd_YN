/**
 * 在easyui grid 基础上封装的接口 跟之前的grid接口api保持一致 demo可以参考grid_easyui.jsp
 */

(function($) {
	if ($) {
		/**
		 * 创建grid
		 */
		$.fn.GridCreate = function(p) {
			$(this).width($(this).parent().width());
			var gridId = this[0].id;
			var gridParent = $(this).parent()[0];
			if (window.gridArray) {
				window.gridArray[window.gridArray.length] = {
					gridId : gridId,
					gridParent : gridParent
				};
			} else {
				window.gridArray = [ {
					gridId : gridId,
					gridParent : gridParent
				} ];
			}
			if (p == null || p.colModel == null || p.colModel.length == 0) {
				return;
			}
			var params = {
				singleSelect : true, // True 就会只允许选中一行。 默认false
				striped : true, // True 就把行条纹化。（即奇偶行使用不同背景色）默认false
				// loadMsg:"Processing, please wait …",
				pageSize : 20
				//checkOnSelect : true,
				//selectOnCheck : true
			};
			if (p.singleSelect == false) {
				params.singleSelect = false;
			}
//			$(gridParent).height(p.height);
			if(p.height == "auto"){
				params.height = $(this).parent().height();
			}else{
				params.height = p.height;
			}
			if(!p.checkOnSelect){
				params.checkOnSelect = false;
			}
			if(!p.selectOnCheck){
				params.selectOnCheck = false; 
			}
		//	params.fit = true;
			params.pagination = p.usepager;
			params.sFirstRow = p.sFirstRow; // 是否默认选中第一行
			params.onSelect = p.onSelectChange;
			params.onDblClickRow = p.onDoubleClick;
			params.fitColumns = true;
			params.rowStyler = p.rowStyler;
			params.onLoadSuccess = function ss(data){
				setTimeout(function(){
					params.fillGrid(data);
				},10);
				if(p.onSuccess){
					p.onSuccess();
				}
				
			};
			params.fillGrid = function(data){
				if(data.total == 0){
					return;
				}
				//$("#"+gridParentId).find(".datagrid-btable").width("100%");
				
				//$("#"+gridParentId).find(".datagrid-btable").width("");
				//虚线变成实线
				//$("#"+gridId).datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom lines-left");
				/*var tbody = $("#"+gridParentId).find(".datagrid-btable").find("tbody");
				var tableHeight = $("#"+gridParentId).find(".datagrid-btable").height();
				var divHeight = $("#"+gridParentId).find(".datagrid-btable").parent().height();
				var less = divHeight - tableHeight;
				if(less <=20){
					return;
				}
				var nowRows = 0;
				if(data & data.rows && data.rows.length > 0){
					nowRows = data.rows.length;
				}
				var cols = p.colModel.length;
				if(p.checkbox){
					cols += 1;
				}
				var tr1 = "<tr class='datagrid-row'><td colspan='"+cols+"'></td></tr>";
				var tr2 = "<tr class='datagrid-row datagrid-row-alt'><td colspan='"+cols+"'></td></tr>"
				if(nowRows % 2 != 0){
					tr2 = "<tr class='datagrid-row'><td colspan='"+cols+"'></td></tr>";
					tr1 = "<tr class='datagrid-row datagrid-row-alt'><td colspan='"+cols+"'></td></tr>"
				}
				
				var lessRows = Math.floor(less/20);
				for(var i=0;i<lessRows;i++){
					if(i%2 == 0){
						$(tbody).append(tr1);
					}else{
						$(tbody).append(tr2);
					}
				}*/
					$(gridParent).find(".datagrid-row").each(function(i,v){
					if($(this).find(".addtd").length == 0){
						$(v).append("<td class='addtd' style='width:17px;overflow:hidden;'></td>");
					}
					
				});
			};
			if (p.usepager) { // 是否翻页
				params.pagination = p.usepager;
				params.pageSize = p.rp; // 每页记录数
				params.loader = function(pageItem, success, error) {
					var pageData = {
						rows : {},
						total : 0
					};
					if (p.localQuery) {
						pageData = p.localQuery([ {
							name : "page",
							value : pageItem.page
						}, {
							name : "rp",
							value : pageItem.rows
						},{
							gridId:gridId
						} ]);
					}
					if (pageData && pageData.rows) {
						$.each(pageData.rows, function(i, row) {
							if (row.cell) {
								pageData.rows[i] = row.cell;
							}
						});
					} else {
						pageData = {
							rows : [],
							total : 0
						};
					}
					success(pageData);
					if ($(this).datagrid('options').sFirstRow) {
						$(this).datagrid("selectRow", 0);
					}
					
				}; // grid注册的翻页方法
			}

			//var totalWidth = $(this).parent().width();
			//if (totalWidth > 100) {
				//var realColWidth = getRealColWidth(p, totalWidth);
				$.each(p.colModel, function(i, col) {
					if (col.display) {
						col.title = col.display;
					}
					if (col.name) {
						col.field = col.name;
					}
					col.hidden = col.hide;
					col.sortable = false;
					//col.width = realColWidth[i];
				});
			//}

			params.columns = [];
			if (p.checkbox) {
				params.columns[0] = {
					field : 'ID',
					title : '',
					checkbox : true
				};
			}
			params.columns = params.columns.concat(p.colModel);
			params.columns = [ params.columns ];

			if (p.singleSelect) {
				params.singleSelect = p.singleSelect;
			}

			$(this).datagrid(params);
			//$(this).datagrid("loadData",{rows : [],total : 0});
			return $(this); // 返回一个引用
		}
		
		// 自适应grid宽度跟其父容器一致
		// 自适应grid宽度跟其父容器一致	
		$(window).resize(function (){
		
			setTimeout(function(){
				if (window.gridArray) {
					var overx = $("body").css("overflow-x");
					$.each(window.gridArray, function(i, gridObj) {
						var parentWidth = $(gridObj.gridParent).width();
						//var parentHeight = $(gridObj.gridParent).height();
						if (gridObj.gridId && gridObj.gridParent) {
							if(parentWidth > 100){ //如果是隐藏的就不处理了
								$("#" + gridObj.gridId).datagrid('resize', {
									width : 100
								});
								
								$("#" + gridObj.gridId).datagrid('resize', {
									width : $(gridObj.gridParent).width()
								//	height: parentHeight
								});
							}
								
								$("body").css("overflow-x","hidden");
						}

					});
					$("body").css("overflow-x",overx);
				}
				}, 50)
			}
		);
		
		/**
		 * 加载数据
		 */
		$.fn.GridLoadData = function(gridData) {
			 
				if (gridData == null) {
				gridData = {
					rows : [],
					total : 0
				};
			}
			$.each(gridData.rows, function(i, row) {
				if (row.cell) {
					gridData.rows[i] = row.cell;
				}
			});
			
			var options = $(this).datagrid('options');
			//判断是否翻页
			if (options.pagination) { 
			  if(gridData.page!=null ){
			  	
					$(this).datagrid('loadData', gridData);
					
					$(this).datagrid('getPager').pagination({
					total:gridData.total,pageNumber:gridData.page});
				}else{
					$(this).datagrid('load', gridData);
				}

			}else{
				$(this).datagrid('loadData', gridData);
			}
			
			if ($(this).datagrid('options').sFirstRow) {
				$(this).datagrid("selectRow", 0);
			}
		}
		
		$.fn.getPageSize = function(){
			
			var options = $(this).datagrid('options');
			//判断是否翻页
			if (options.pagination) { 
				return $(this).datagrid('options').pageSize;
			}
			return 0;
		}
		/**
		 * 清空数据
		 */
		$.fn.clearData = function() {
			$(this).datagrid('loadData', {
				rows : [],
				total : 0
			});
			
		}
		
		/**
		 * 返回checkbox选中的行
		 */
		$.fn.getCheckedRows = function() {
			return $(this).datagrid("getChecked");
		}
		/**
		 * 返回选中的行
		 */
		$.fn.getSelections = function() {
			return $(this).datagrid("getSelections");
		}

		/**
		 * 返回选中的第一行
		 */
		$.fn.GridSelectedItem = function() {
			return $(this).datagrid("getSelected");
		}
		/**
		 * 删除选中行
		 */
		$.fn.delSelectedRow = function() {
			var rows = $(this).datagrid("getSelections");
			var data = $(this).datagrid("getData");
			if (rows.length > 0) {
				$.each(rows, function(i, row) {
					data.rows.splice(row.index, 1);
				});
				$(this).datagrid("loadData", data);
			}
		}
		/**
		 * 删除指定行
		 */
		$.fn.delOneRow = function(index) {
			$(this).datagrid("deleteRow", index);
		}

		/**
		 * 选中指定行，传入行号，从0开始
		 */
		$.fn.selectRow = function(index) {
			$(this).datagrid("selectRow", index);
			return $(this).datagrid("getSelected");
		}

		/**
		 * 插入一行
		 */
		$.fn.insertRow = function(rowData,index) {
			var item = {};
			if(index == null){
				index = 0;
			}
			item.index = index;
			if (rowData.cell) {
				rowData = rowData.cell;
			}
			item.row = rowData;
			$(this).datagrid("insertRow", item);
			$(this).datagrid("selectRow", index);
		}
		/**
		 * 修改一行
		 */
		$.fn.updateRow = function(rowData) {
			var row = $(this).datagrid("getSelected");
			var item = {};
			item.index = row.index;
			item.row = rowData;
			$(this).datagrid("updateRow", item);

		}

		/**
		 * checkbox选中行，传入行号数组。true选中，false不选中
		 */
		$.fn.checkRows = function(indexs, ifCheck) {
			if (indexs == null || indexs.length == 0) {
				return;
			}
			for ( var i = 0; i < indexs.length; i++) {
				if (ifCheck) {
					$(this).datagrid("checkRow", indexs[i]);
				} else {
					$(this).datagrid("uncheckRow", indexs[i]);
				}
			}
		}
		/**
		 * 屏蔽某些行的checkbox
		 */
		$.fn.disableRows = function(indexs, ifCheck) {
			$(this).parent().find("input[type='checkbox']").each(
					function(index, el) {
						if (ifContains(indexs, index - 1)) {
							$(this).attr("disabled", true);
							$(this).attr("checked", ifCheck);
						}
					});
		}
		/**
		 * 返回grid加载的数据
		 */
		$.fn.getGridData = function() {
			return $(this).datagrid("getData");
		}
		
		/**
		 * 返回grid加载的数据
		 */
		$.fn.getGridData = function() {
			return $(this).datagrid("getData");
		}

		$.fn.destroy = function() {

		}

		function ifContains(arr, v) {
			var result;
			$.each(arr, function(i, n) {
				if (n == v) {
					result = true;
					return false;
				}
			});
			return result;
		}

		function getRealColWidth(p, totalWidth) {
			totalWidth = totalWidth - p.colModel.length ;
			if (p.checkbox) {
				totalWidth -= 30;
			}
			var setWidth = 0;
			var realColWidth = [];
			for ( var i = 0; i < p.colModel.length; i++) {
				setWidth += p.colModel[i].width;
			}
			for ( var i = 0; i < p.colModel.length; i++) {
				var realw = (p.colModel[i].width / setWidth) * totalWidth;
				// realColWidth[i] = Math.round(realw);
				realColWidth[i] = parseInt(realw);
			}
			return realColWidth;
		}
	}
})(jQuery);