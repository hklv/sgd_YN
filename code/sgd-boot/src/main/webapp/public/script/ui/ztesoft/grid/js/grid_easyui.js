/**
 * 在easyui grid 基础上封装的接口
 * 跟之前的grid接口api保持一致
 * demo可以参考grid_easyui.jsp
 */

(function($) {
	if ($) {
		/**
		 * 创建grid
		 */
		$.fn.GridCreate = function(p) {
			$(this).width($(this).parent().width());
			if (p == null || p.colModel == null || p.colModel.length == 0) {
				return;
			}
			var params = {
				singleSelect : true, // True 就会只允许选中一行。 默认false
				striped : true, // True 就把行条纹化。（即奇偶行使用不同背景色）默认false
				// loadMsg:"Processing, please wait …",
				pageSize : 20,
				checkOnSelect : false,
				selectOnCheck : false
			};
			if (p.singleSelect == false) {
				params.singleSelect = false;
			}
			params.height = p.height;
			params.pagination = p.usepager;
			params.sFirstRow = p.sFirstRow; // 是否默认选中第一行
			params.onSelect = p.onSelectChange;
			params.onDblClickRow = p.onDoubleClick;
			// params.fitColumns = true;
			if(p.usepager){							// 是否翻页
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
						} ]);
					}
					$.each(pageData.rows, function(i, row) {
						if (row.cell) {
							pageData.rows[i] = row.cell;
						}
					});
					success(pageData);
					$(this).datagrid("selectRow", 0);
				}; // grid注册的翻页方法
			}

			var totalWidth = $(this).parent().width();
			if (totalWidth > 100) {
				var realColWidth = getRealColWidth(p, totalWidth);
				$.each(p.colModel, function(i, col) {
					if (col.display) {
						col.title = col.display;
					}
					if (col.name) {
						col.field = col.name;
					}
					col.width = realColWidth[i];
				});
			}

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
			return $(this); // 返回一个引用
		}

		/**
		 * 加载数据
		 */
		$.fn.GridLoadData = function(gridData) {
			if (gridData == null) {
				gridData = {
					rows : {},
					total : 0
				};
			}
			$.each(gridData.rows, function(i, row) {
				if (row.cell) {
					gridData.rows[i] = row.cell;
				}
			});

			$(this).datagrid('loadData', gridData);
			if ($(this).datagrid('options').sFirstRow) {
				$(this).datagrid("selectRow", 0);
			}
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
		$.fn.insertRow = function(rowData) {
			var item = {};
			item.index = 0;
			item.row = rowData;
			$(this).datagrid("insertRow", item);
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
			totalWidth = totalWidth - 25;
			if (p.checkbox) {
				totalWidth -= 25;
			}
			var setWidth = 0;
			var realColWidth = [];
			for ( var i = 0; i < p.colModel.length; i++) {
				setWidth += p.colModel[i].width;
			}
			for ( var i = 0; i < p.colModel.length; i++) {
				var realw = (p.colModel[i].width / setWidth) * totalWidth;
				realColWidth[i] = Math.round(realw);
			}
			return realColWidth;
		}
	}
})(jQuery);