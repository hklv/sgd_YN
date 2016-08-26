/**
 * 在easyui tree 基础上封装的接口 ,demo参考ZTEsoftTreeList.jsp
 */

(function($) {
	if ($) {
		/**
		 * 创建grid
		 */
		$.fn.TreeList = function(p) {
			
			if (p == null) {
				return;
			}
			var options = {
				animate:true,   //动画效果
				lines:true,		//是否显示线条
				dnd:false,		//是否可拖拽
				formatter:null,  
			};
			options = jQuery.extend(options, p);
			$(this).tree(options);
			var rootItem = $(this).tree("getRoot");
			$(this).tree("select",rootItem.target);
			$(this).tree("expand",rootItem.target);
			return $(this); // 返回一个引用
		}

		/**
		 * 加载数据
		 */
		$.fn.loadData = function(treeData) {
			if(treeData == null){
				treeData = {};
			}
			$(this).tree('loadData', treeData);
		}
		/**
		 * 返回checkbox选中的行
		 */
		$.fn.getChecked = function() {
			return $(this).tree("getChecked");
		}
		/**
		 * 返回选中的行
		 */
		$.fn.getSelected = function() {
			return $(this).tree("getSelected");
		}

		/**
		 * 查找特定节点
		 */
		$.fn.getNode = function(target) {
			return $(this).tree("getNode",target);
		}
		
		/**
		 * 查找特定节点及其子节点
		 */
		$.fn.getData = function(target) {
			return $(this).tree("getData",target);
		}
		
		/**
		 * 获取根节点
		 */
		$.fn.getRootNode = function() {
			return $(this).tree("getRoot");
		}
		
		/**
		 * 获取根节点数组
		 */
		$.fn.getRootNodes = function() {
			return $(this).tree("getRoots");
		}
		
		/**
		 * 获取节点父亲
		 */
		$.fn.getParent = function(target) {
			if(target == null){
				var node = $(this).tree("getSelected");
				target = node.target;
			}
			return $(this).tree("getParent",target);
		}
		
		/**
		 * 获取节点儿子
		 */
		$.fn.getChildren = function(target) {
			if(target == null){
				var node = $(this).tree("getSelected");
				target = node.target;
			}
			return $(this).tree("getChildren",target);
		}
		
		/**
		 * 判断是否叶子节点
		 */
		$.fn.isLeaf = function(target) {
			if(target == null){
				var node = $(this).tree("getSelected");
				target = node.target;
			}
			return $(this).tree("isLeaf",target);
		}
		
		/**
		 * 查找并选中指定节点
		 */
		$.fn.findNode = function(target) {
			return $(this).tree("find",target);
		}
		
		/**
		 * 展开节点
		 */
		$.fn.expand = function(target) {
			if(target == null){
				var node = $(this).tree("getSelected");
				target = node.target;
			}
			$(this).tree("expand",target);
		}
		
		/**
		 * 收缩节点
		 */
		$.fn.collapse = function(target) {
			if(target == null){
				var node = $(this).tree("getSelected");
				target = node.target;
			}
			$(this).tree("collapse",target);
		}
		
		/**
		 * 插入节点到选中节点之前
		 */
		$.fn.insertNode = function(data) {
			var node = $(this).tree("getSelected");
			$(this).tree("insert",{before:node.target,data:data});
		}
		
		/**
		 * 插入节点到选中节点之后
		 *  ex:  data:{id:11,text:xx}
		 */
		$.fn.appendNode = function(data) {
			var node = $(this).tree("getSelected");
			return $(this).tree("insert",{after:node.target,data:data});
		}
		
		/**
		 * 更新选中节点
		 *  ex:  data:{id:11,text:xx}
		 */
		$.fn.updateNode = function(newText) {
			var node = $(this).tree("getSelected");
			return $(this).tree("update",{target: node.target,text: newText});
		}
		
		/**
		 * 删除选中节点
		 */
		$.fn.removeNode = function(target) {
			if(target == null){
				var node = $(this).tree("getSelected");
				target = node.target;
			}
			return $(this).tree("remove",target);
		}
	}
})(jQuery);