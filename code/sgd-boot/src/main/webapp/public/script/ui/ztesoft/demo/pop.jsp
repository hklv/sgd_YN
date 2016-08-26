<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../../../web.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>弹出窗demo</title>
<style type="text/css">
		
	</style>
<script language="javascript">
	var input = new Object();
	input.name='tom';
	input.age='20';
	input.city='nanjing';
	/* var callb = function(obj){
		if(obj != null){
			alert("callback name ==="+obj.name);
		}
	}; */
	function popclick(){
		showModalWindow('RSP On-boarding','public/script/ui/ztesoft/demo/pop1.jsp',800,500,input,function(obj){
			alert("close");
			if(obj != null){
				alert("popcallback name ==="+obj.name);
			}
		});
	}
	function cc(){
		alert(isScroll().scrollY);
	}
	function isScroll(el) {
	    // test targets  
	    var elems = el ? [el] : [document.documentElement, document.body];  
	    var scrollX = false, scrollY = false;  
	    for (var i = 0; i < elems.length; i++) {  
	        var o = elems[i];  
	        // test horizontal  
	        var sl = o.scrollLeft;  
	        o.scrollLeft += (sl > 0) ? -1 : 1;  
	        o.scrollLeft !== sl && (scrollX = scrollX || true);  
	        o.scrollLeft = sl;  
	        // test vertical  
	        var st = o.scrollTop;  
	        o.scrollTop += (st > 0) ? -1 : 1;  
	        o.scrollTop !== st && (scrollY = scrollY || true);  
	        o.scrollTop = st;  
	    }  
	    // ret  
	    return {  
	        scrollX: scrollX,  
	        scrollY: scrollY  
	    };  
	}
	$(function(){
		/* $(".divssss").scroll(function(event){
			event.preventDefault();
			event.returnValue = false;
			}); */
			document.attachEvent("onmousewheel", 
			        function(){
			              // alert("11");
			        }
			)
		$(window).scroll(function(e){
			return false;
			//e.preventDefault();
			//e.returnValue = false; //禁止页面滚动
		});
	});
</script>
</head>

<body style="overflow:hidden;">
<input type="button" value="弹出新窗口" onclick="popclick();" />
<input type="button" value="cc" onclick="cc();" />
<input type="button" id="baidubtn" value="baidu" onclick="showModalWindow('baidu','http://baidu.com',800,500);" />

<table style="height: 100px;" border="1"><tr><td>1111111111</td></tr></table>

</body>
</html>