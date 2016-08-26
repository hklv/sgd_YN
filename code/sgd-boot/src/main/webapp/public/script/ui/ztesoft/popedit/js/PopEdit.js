/**
 * 在easyui tree 基础上封装的接口 ,demo参考ZTEsoftTreeList.jsp
 */

(function($) {
	if ($) {
		/**
		 * 创建grid
		 */
		$.fn.PopEdit = function(options) {
			if (options == null) {
				alert("请设置初始化参数!");
				return;
			}
			var table = $("<table border=0 cellpadding=0 cellspacing=0 class='PopEdit'></table>");
			var tr = $("<tr></tr>");
			var td1 = $("<td></td>");
			var td2 = $("<td></td>");
			var td3 = $("<td></td>");
			var text = $("<input type='text' class='textBox_popedit' readonly='readonly' />");
			var hiddenid = $("<input type='hidden' class='id_popedit' />");
			var openButton = $("<input type='button' style='' class='button_popedit_open'/>");
			var clearButton = $("<input type='button' style='' class='button_popedit_clear'/>");
			td1.append(text);
			td1.append(hiddenid);
			td2.append(openButton);
			td3.append(clearButton);
			tr.append(td1);
			tr.append(td2);
			tr.append(td3);
			table.append(tr);
			$(this).append(table);
			this.text = text;
			this.openButton = openButton;
			this.clearButton = clearButton;
			if($.trim(options.url) == ""){
				alert("请设置打开窗口url!");
				return;
			}
			this.initDialogObj = null ;
			var width = options.windowWidth?options.windowWidth:500;
			var height = options.windowHeight?options.windowHeight:200;
			
			$(openButton).click(function(){
				var initObj = null;
				if(options.getInitObj){
					initObj = options.getInitObj();
				}else{
					initObj = {};
					initObj.idVal = hiddenid.val();
					initObj.textVal = text.val();
				}
				
				showModalWindow(options.windowName,options.url,width,height,initObj,function(returnObj){
					if(returnObj && returnObj.textVal){
						text.val(returnObj.textVal);
					}
					if(returnObj && returnObj.idVal){
						hiddenid.val(returnObj.idVal);
					}
					if(options.onClose){
						options.onClose(returnObj);
					}
				});
			});
			
			$(clearButton).click(function(){
				text.val("");
				hiddenid.val("");
				if(options.onClear){
					options.onClear();
				}
			});
			$(text).width($(this).width()-45);
			return $(this); // 返回一个引用
		}

		/**
		 * 禁用
		 */
		$.fn.setDisable = function() {
			$(this).find('.button_popedit_open').attr("disabled",true).attr("class","button_popedit_open_gray");
			$(this).find('.button_popedit_clear').attr("disabled",true).attr("class","button_popedit_clear_gray");;
			$(this).find('.textBox_popedit').attr("disabled",true);
		}
		
		/**
		 * 启用
		 */
		$.fn.setEnable = function() {
			$(this).find('.button_popedit_open_gray').attr("disabled",false).attr("class","button_popedit_open");
			$(this).find('.button_popedit_clear_gray').attr("disabled",false).attr("class","button_popedit_clear");;
			$(this).find('.textBox_popedit').attr("disabled",false);
		}
		/**
		 * 取值,返回绑定的id和text
		 */
		$.fn.getVal = function (){
			var item = {};
			item.idVal = $(this).find('.id_popedit').val();
			item.textVal = $(this).find('.textBox_popedit').val();
			return item;
		}
		/**
		 * 赋值,需要传入id和text
		 */
		$.fn.setVal = function (item){
			if(item == null){
				return;
			}
			$(this).find('.id_popedit').val(item.idVal);
			$(this).find('.textBox_popedit').val(item.textVal);
		}
		
		/**
		 * 清除赋值
		 */
		$.fn.clearVal = function (){
			$(this).find('.id_popedit').val("");
			$(this).find('.textBox_popedit').val("");
		}
		
	}
})(jQuery);