var tagSelect = "";
var tag=['active','view'];
/*******************************************************************************
 * 函数名称：initPage() 函数功能：初始化页面 输入参数：无 输出参数：无 返 回 值：无 函数说明：
 ******************************************************************************/
$(function() {
	try{
		$("#HOLDER_NO").val(holderNo);
		$("#PROCESS_NAME").val(processName);
		$("#PROC_INST_ID").val(procInstId);
		console.info("procInstId = "+procInstId+"   hahah");
		var holdeStateVal;
		switch(state){
			case "A":
				holdeStateVal="正在处理";
				break;
			case "C":
				holdeStateVal="完成";
				break;
			case "K":
				holdeStateVal="回退";
				break;
			case "F":
				holdeStateVal="撤回";
				break;
			case "B":
				holdeStateVal="挂起";
				break;
			case "T":
				holdeStateVal="废弃";
				break;
		}
		$("#HOLDER_STATE").val(holdeStateVal);
		/*$("#START_TIME").val(holderItem.START_TIME);
		$("#END_TIME").val(holderItem.END_TIME);*/
		var initTabType = getUrlParam('initTabType');
		if(initTabType&&initTabType=='view'){
			tab_select("view");
		}else{
			tab_select("active");
		}
	
	 } catch(e) { showError("Error_INITPAGE",e); return; }
});

function  tab_select(tagName){
	tagSelect = tagName;
	var strURL = "";
	if(tagName == "active"){
		strURL = "modules/bpmn/flowdesigner/draw/flowActiviti.jsp?process_instance_id="+procInstId;
	}else if(tagName == "view"){
		strURL = "modules/bpmn/flowdesigner/draw/showView.jsp";
		if(holderId&&holderId.length>0){
			
			strURL+="?taskHolderId="+holderId;
		}
	
	}
	for(var i=0;i<tag.length;i++){
		if(tag[i]==tagName){
			if($("#frame"+tagName).attr("src") != strURL){
				$("#frame"+tagName).attr("src",strURL);
			}
			$("#frame"+tagName).attr("load",false);
			$("#frame"+tagName).attr("isview",true);
			$("#tab_li"+tagName).addClass("hover");//增加样式
			$("#tag"+tagName).show();//显示选中的层
		}else{
			$("#tab_li"+tag[i]).removeClass("hover");
			$("#tag"+tag[i]).hide();
		}
	}
}

function setViewWinHeightFirstPage(obj)
{
	var bb = obj.contentWindow.document.documentElement;
	//var iframeHeight=jQuery(bb).outerHeight(true)+8;
	var iframeHeight = bb.scrollHeight;
	if(parseInt(iframeHeight)!=0){
			obj.style.height = (iframeHeight+"px");
		}
}