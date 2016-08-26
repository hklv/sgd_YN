(function($)
{
	if($)
	{
		//初始化下拉框
		$.initSelect = function(drpId,arrObj,colarr,addBlankRow)
		{
			var sel=null;
			if(null!=drpId){
				sel=$("#"+drpId);
			}
			sel.html("");
			if(!$.browser.msie){
				sel.keyup(function (){sel.blur();sel.focus();});
			}
			//addBlankRow = (arrObj.length <= 1 && addBlankRow) || (arrObj.length > 1);
			if(addBlankRow){
				//**************注释去掉流程页面select默认的"请选择 "选项*************************************************
				/*var text = "Please Select";
				if(_Language == "zh_CN"){
					text = "请选择";
				}
				$("<option/>").text(text).val("").appendTo(sel);*/
				//*****************************end*************************************************
			}
			if(null==arrObj) return;
			if(! arrObj instanceof Array)
			{
				arrObj = [arrObj];
			}
			
			$.each(arrObj,function(i,v){
				$("<option/>").text(v[colarr[0]]).val(v[colarr[1]]).appendTo(sel).data("data",v);
			});
		};
		
		//初始化下拉框
		$.initSingleSelect = function(drpId,tableName,colarr,addBlankRow,whereSql)
		{
			var inParam = {};
			inParam.method = 'qrySelectDataService';
			inParam.TABLE_NAME = tableName;
			
			var colNames = "";
			for(var i=0;i<colarr.length;i++){
				colNames += "," + colarr[i];
			}
			inParam.COL_NAMES = colNames.substring(1);
			if(whereSql!=null && whereSql.trim().length>0){
				inParam.WHERE_SQL=whereSql;
			}
			
			if(colarr[0]){
				inParam.ORDER_COL_NAMES=colarr[0];
			}
			
			//var res = callRemoteFunction("QueryService", inParam);
			var res = null;
			if(res ){
				
				$.initSelect(drpId, res.SELECT_DATA, colarr, addBlankRow);
			}
		};
		
		$.fn.data2 = function(key, value) {
			if (this.length == 0) {
				return;
			}
			if (this.length > 1) {
				alert("this function is only supported on single jQuery Object");
			}
			if (typeof value == "undefined" && !(key instanceof Object)) {
				if (!window.tfmCache || !window.tfmCache[this]) {
					return null;
				} else {
					return window.tfmCache[this][key];
				}
			}

			if (!window.tfmCache) {
				window.tfmCache = {};
			}
			if (!window.tfmCache[this]) {
				window.tfmCache[this] = {};
			}
			if (key != null && typeof value != "undefined") {
				window.tfmCache[this][key] = value;
			}
			if (typeof key === "object" && typeof value == "undefined") {
				var me = this;
				$.each(key, function(k, v) {
					window.tfmCache[me][k] = v;
				});
			}

		};

		$.fn.removeData2 = function(key) {
			if (!window.tfmCache || !window.tfmCache[this]) {
				return;
			}
			if (key == null) {
				window.tfmCache[this] = null;
			} else {
				window.tfmCache[this][key] = null;
			}
			if ($.browser.msie) {
				CollectGarbage()
			}
		};
		
	}
})(jQuery);
/**
 * 字符串去掉空格
 */
String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g, "");}
/**
 * 获取url参数
 * @param param
 * @param win
 * @returns
 */
function GetUrlParameter(param,win){
	 
	  var url = window.location.search;
	  if(win!=null){
	    var url = win.location.search;
	    
	    }
	  var pos1 = 0, pos2 = 0;
	  pos1 = url.indexOf("&"+ param +"=");
	  if(pos1<0) pos1 = url.indexOf("?"+ param +"=");
	  if(pos1>-1){
	    pos2 = url.indexOf("&",pos1+1);
	    if(pos2==-1) pos2 = url.length;
	    return unescape(url.substring(pos1+param.length+2, pos2));
	  }else return null;
	}
/**
 * 一次可查询多个key的resource，逗号分隔
 * @param key
 * @returns
 */
function getJspRes(key){
	var retMsg = key;
	if(key == null || key == "")
	{
		return "";
	}
	var data = {};
	data.LANG = g_GlobalInfo.Language;
	data.RES_TYPE = "JSP";
	data.QRY_KEY = key;
	data.method = "qryResource";
	try
	{
		var ret = callRemoteFunction("WebResService",data);
		retMsg = ret.RESOURCE;
	}
	catch(e)
	{
		;
	}
	return retMsg;
}
/**
 * 一次可查询多个key的resource，逗号分隔
 * @param key
 * @returns
 */
function getJsRes(key,resType){
	var retMsg = key;
	if(key == null || key == "")
	{
		return "";
	}
	var data = {};
	data.LANG = g_GlobalInfo.Language;
	if(resType == null){
		data.RES_TYPE = "JS";
	}else{
		data.RES_TYPE = resType;
	}
	
	data.QRY_KEY = key;
	data.method = "qryResource";
	try
	{
		var ret = callRemoteFunction("WebResService",data);
		retMsg = ret.RESOURCE;
	}
	catch(e)
	{
		;
	}
	return retMsg;
}

function parseData4GridData(data, page, total) {
	if (data.length < 1)
		return;
	var dataTemp = [];
	$.each(data, function(i, v) {
		var temp = {
			cell : v
		};
		dataTemp.push(temp);
	});
	var dataArr = {};
	dataArr.rows = dataTemp;
	dataArr.page = page;
	dataArr.total = (total == null ? data.length : total);
	return dataArr;
}

function checkLength(data, len){
	
	var rtn = true;
	var totalLen = 0;
	
	if(data == null || data == ""){
		return rtn;
	}else{
		for(var i = 0; i < data.length; i++){
			
			if(data.charCodeAt(i) > 255){
				totalLen +=2;
			}else{
				totalLen ++;
			}
		}
	}
	if(totalLen > len){
		rtn = false;
	}
	return rtn;
}	
/***********************/	
function initEasyUiTree(tree,item){
	
	if(item==null)return;
	//如果id和parent都重复就返回
	if($("li#"+item.id,tree).length>0 && $("li#"+item.id,tree).parent().parent().attr("id") == item.parent)return;
	var urls="";
	if(item.URL!=null || item.url!=null){
		urls="url='"+(item.URL||item.url)+"'";
	}
	if (item.parent == null || item.parent == "") {
		$(tree).append("<li id='"+item.id+"' data-options="+getDataOptions(item)+" title='"+(item.text||item.name)+"' "+urls+" >"
			           +"<span >"+(item.text||item.name)+"</span>"
			           +"<ul><ul>"
			           +"</li>");
	}else{
		var parentLi = $("#"+item.parent,tree);
		if(parentLi.length<=0){
			/*
			$(tree).append("<li id='"+item.id+"' title='"+(item.text||item.name)+"' "+urls+">"
			           +"<span >"+(item.text||item.name)+"</span>"
			           +"<ul><ul>"
			           +"</li>");
			           */
		}else{
			$($("ul",parentLi)[0]).append("<li id='"+item.id+"' data-options="+getDataOptions(item)+" title='"+(item.text||item.name)+"' "+urls+">"
			           +"<span >"+(item.text||item.name)+"</span>"
			           +"<ul><ul>"
			           +"</li>");
		}
			
	}
	
}

//树节点中要传递的自定义数据
function getDataOptions(item){
	if(null != item.attributes && "undefined" != item.attributes){
		var attrNode = item.attributes;
		var dataOptions="attributes:{";
		for(var attrName in attrNode){
			dataOptions+="'"+attrName+"':'"+attrNode[attrName]+',"';
		}
		return dataOptions.substring(0,dataOptions.length-2)+"'}";
	}
	return "attributes:{level:'1'}";
	
}
function initEasyUiTreeByDatas(tree,datas,clk){
    
	  clernEasyUiTree(tree);
	if(datas==null||datas.length<=0)return;
	
	for(i=0;i<datas.length;i++){
		initEasyUiTree(tree,datas[i]);
	}
   
	refreshEasyUiTree(tree,clk);
	 $("li",tree).each(function(){
	    	
	    	$(this).attr("title",$($("span.tree-title",$(this))[0]).text());
	    })
	$(">li>div>span:first-child",tree).each(function(){
		
	   if($(this).hasClass("tree-indent"))
	   $(this).addClass("tree-indent-root-firt");  
		});  
		$(tree).tree('collapseAll');
		var node = $(tree).tree('getRoot');
		$(tree).tree('expand', node.target);
		$(tree).tree('select', node.target);
		if(clk && typeof clk == 'function'){
			clk(node);
		}

}
function refreshEasyUiTree(tree,clk){
	tree.addClass("easyui-tree");
	tree.tree({lines:true,onClick: function(node){if(clk)clk(node);}});
}
function clernEasyUiTree(tree){
	tree.html("");
}
/**
 * 参数说明
 * 
 * dataList		树原始数据
 * idName		节点id对应的键值name
 * pidName		节点parent_id对应的键值name
 * titleName	节点text对应的键值name
 * leafFlag		是否对叶节点进行属性设置
 * checkFlag	是否显示复选框,'leaf'只有叶节点显示，'all'表示所有节点显示
 * expandLevel  展开层级数
 */
function format_tree_data(dataList, idName, pidName, titleName, leafFlag, checkFlag, expandLevel) {
	var pidAry = [];
	var pidDataAry = [];
	if(dataList==null){
		return;
	}
	//获得所有pid
	for(var i=0; i<dataList.length; i++) {
		if(dataList[i][pidName] == null || dataList[i][pidName] == '') {//不存在pid时默认为0
			dataList[i][pidName] = "0";
		}
		for(key in dataList[i]) {
			if(key == pidName) {
				pidAry.push(dataList[i][key]);
			}
		}		
	}
	pidAry = ary_unique(pidAry);	
	pidAry.sort(function(a,b){return a>b?1:-1});

	//构建pid为键值的数据
	for(var j=0; j<pidAry.length; j++) {
		var pid = pidAry[j];
		pidDataAry[pid] = [];
		for(var i=0; i<dataList.length; i++) {
			for(key in dataList[i]) {
				if(key == pidName && dataList[i][key] == pidAry[j]) {
					dataList[i].text = dataList[i][titleName];
					if(checkFlag == 'all') dataList[i].checked = false;
					pidDataAry[pid].push(dataList[i]);
				}
			}		
		}		
	}
	
	var tempLevel = 0;
	return format_tree_get_children(pidAry[0], pidAry, pidDataAry, idName, leafFlag, checkFlag, expandLevel, tempLevel);
}


function format_tree_get_children(pid, pidAry, pidDataAry, idName, leafFlag, checkFlag, expandLevel, tempLevel) {
	var resDataAry = pidDataAry[pid];
	if(resDataAry) {
		if(expandLevel > 0) tempLevel++;
		for(var k=0; k<resDataAry.length; k++) {
			resDataAry[k]['children'] = format_tree_get_children(resDataAry[k][idName], pidAry, pidDataAry, idName, leafFlag, checkFlag, expandLevel, tempLevel);
			if(expandLevel > 0 && tempLevel <= expandLevel) {
				resDataAry[k].expanded = true;
			}
			if(!resDataAry[k]['children']) {
				resDataAry[k]['children'] = [];
				if(leafFlag) resDataAry[k].leaf = true;
				if(checkFlag == 'leaf') resDataAry[k].checked = false;
			}
		}		
	} else {
		if(expandLevel > 0) tempLevel = 0;
	}
	return resDataAry;
}

function ary_unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}

/*
 * 把没有父子关系的列表数据放到tree中展示
 */
function initTreeByList(dataList, titleName) {
	if(dataList==null){
		return;
	}
	for(var i=0; i<dataList.length; i++) {
		dataList[i].text=dataList[i][titleName]
		dataList[i].leaf = true;
	}
	return dataList;
}

 //获取url中的参数localhost?t=code
 function getUrlParam(name) {
     var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
     var r = window.location.search.substr(1).match(reg);  //匹配目标参数
     if (r != null) return unescape(r[2]); return null; //返回参数值
 }
 
 function initComboBoxByList(dataList, idName,disPlayName) {
 	var arr = new Array();
 	if(dataList){
	for(var i=0; i<dataList.length; i++) {
		var obj = new Object();
		obj.id=dataList[i][idName];
		obj.name=dataList[i][disPlayName];
		arr[i]=obj;
	}}
	return arr;
}