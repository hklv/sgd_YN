/*************************************************************************
函数说明：组合Select下拉框内容,根据参数决定显示ID的顺序

函数要求：

输入参数：

	value：下拉框显示值

	id	：传入的下拉框的ID值

输出参数：

	无；
************************************************************************/
function BuildSelectIDText(value,id)
{ 
	if (g_GlobalInfo!=null && g_GlobalInfo.SelComponentIDAlign!=null && g_GlobalInfo.SelComponentIDAlign!="null")
	{
	    var mark = g_GlobalInfo.SelComponentIDAlign;
	    //放在后方(1),前面(2),还是不显示(0)设定 -->
	    if(mark=="1")
	    {
	        return value+" ["+id+"]";
	    }
	    else if(mark=="2")
	    {
	        return "["+id+"] "+value;
	    }else if(mark=="0")
	    {
	        return value;
	    }else
	    {
	    	return value;
	    }
	}else
	{
		return value+" ["+id+"]";
	}
}

/************************************************************
 函数名称：removeNodeSelfAndChild
 函数功能：删除树的节点，包括子节点

 输入参数：无
 输出参数：无
 ************************************************************/
 function removeNodeSelfAndChild(nodeSrc)
 {
 	if (nodeSrc == null)
	{
		return;
	}
	nodeSrc.parent.removeNode(nodeSrc);
	
 	/*
	if (nodeSrc == null)
	{
		return;
	}
	var nodeChildren = nodeSrc.getChildren();

	if (nodeChildren == null)
	{
		nodeSrc.remove();
	}
	else
	{
		for (var i=nodeChildren.length-1; i>=0; i--)
		{
			removeNodeSelfAndChild(nodeChildren[i]);
		}
		nodeSrc.remove();
	}
	*/
 }

/**************************************************************************
 函数名称：nextCursor
 函数功能：界面控件输入回车后，能自动聚焦到下一个对象控件

 输入参数：

         eve   :  触发对象
         nextobj : 回车后聚焦对象

 输出参数：无
 **************************************************************************/
function nextCursor(eve,nextobj)
{
    if (eve.keyCode==13)
	{
		document.all.item(nextobj).focus();
		eve.keyCode=0;
	}
	return;
}

/**************************************************************************
 函数名称：focusTextEnd
 函数功能：光标,停在[文本编辑框/文本输入框]文字的最后,方便用户输入
 输入参数：

 输出参数：无
 **************************************************************************/
function focusTextEnd()
{
	var e = event.srcElement;
	var r =e.createTextRange();
	r.moveStart('character',e.value.length);
	r.collapse(true);
	r.select();
}

/**************************************************************************
 函数名称：selectText
 函数功能：将[文本编辑框/文本输入框]文字都选中,方便用户输入
 输入参数：

 输出参数：无
 **************************************************************************/
function selectText(txtId)
{
	txtId.focus();
	var rng = document.selection.createRange();
	txtId.select();
}

/**************************************************************************
 函数名称：gotoQuerySubmit
 函数功能：界面控件输入回车后，自动执行"Query"处理
 输入参数：

           eve   :  触发对象
 输出参数：无
 **************************************************************************/
function gotoQuerySubmit(eve)
{
    if (eve.keyCode==13)
	{
		queryOnClick();
	}
	return;
}

/************************************************************
 函数名称：setControlStatusByCheckbox
 函数功能：checkbox的onclick事件触发的同时，控制相应控件的可用性

 输入参数：

	chkId : checkbox对象ID
	controlId : checkbox控制的对象ID或者示例名
 输出参数：无
 ************************************************************/
function setControlStatusByCheckbox(chkId,controlId)
{
	if ( (chkId == null)||(controlId == null) )
	{
		return;
	}
	var status = !chkId.checked;
	var tagName = controlId.tagName.toUpperCase();
	switch (tagName)
	{
		case "INPUT":
		case "SELECT":
		{
				controlId.disabled = status;
				break;
		}
		case "DATETIMEPICKER":
			{
				controlId.setBtnDisable(status);
				break;
			}
		case "POPEDIT":
			{
				controlId.setBtnDisable(status);
				break;
			}
		case "ZTESOFTSELECT":
			{
				controlId.setDisable(status);
				break;
			}
		default:
			break;
	}
	try
	{
		controlId.focus();
	}
	catch (e)
	{
	}	
}

function Select_Init(drpId,arrObj,colarr,addBlankRow)
{
	if(arrObj==null) return;
	if (drpId.tagName!="SELECT")
		return false;
	
	drpId.length=0;
	if(! arrObj instanceof Array)
	{
		arrObj = [arrObj];
	}
	if(arrObj.length  <= 1)
	{
		drpId.disabled = true;
	}
	else
	{
		drpId.disabled = false;
	}
	
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	
	addBlankRow = (arrObj.length <= 1 && addBlankRow) || (arrObj.length > 1);
	if(addBlankRow){
		var option = document.createElement("OPTION");
		option._text = "";
		option.text = SELECT_PLEASE_SELECT.replace(/-/g,"");
		option.value = "";
		drpId.add(option);
	}
	
	for (i = 0; i < arrObj.length; i++)
	{
		var option = document.createElement("OPTION");
		option._text = eval("arrObj[i]."+colarr[0]);
		option.text = BuildSelectIDText(eval("arrObj[i]."+colarr[0]),eval("arrObj[i]."+colarr[1]));
		option.value = eval("arrObj[i]."+colarr[1]);
		if ( (option.value == "")||(option.value == null) )
		{
			option.text	= " ";
		}
		//option.title = option.text;
		drpId.add(option);
	}
}

function Select_Init_No_Id(drpId,arrObj,colarr,addBlankRow)
{
	if(arrObj==null) return;
	if (drpId.tagName!="SELECT")
		return false;
	drpId.length=0;
	if(! arrObj instanceof Array)
	{
		arrObj = [arrObj];
	}
	if(arrObj.length  <= 1)
	{
		drpId.disabled = true;
	}
	else
	{
		drpId.disabled = false;
	}
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	addBlankRow = (arrObj.length <= 1 && addBlankRow) || (arrObj.length > 1);
	if(addBlankRow){
		var option = document.createElement("OPTION");
		option.text = SELECT_PLEASE_SELECT.replace(/-/g,"");
		option.value = "";
		drpId.add(option);
	}

	for (i = 0; i < arrObj.length; i++)
	{
		var option = document.createElement("OPTION");
		option._data=eval("arrObj[i]");
		option._text = eval("arrObj[i]."+colarr[0]);
		option.text = eval("arrObj[i]."+colarr[0]);
		option.value = eval("arrObj[i]."+colarr[1]);
		if ( (option.value == "")||(option.value == null) )
		{
			option.text	= " ";
		}
		//option.title = option.text;
		drpId.add(option);
	}
}

function Select_Init_No_Id_By_Condition(drpId,arrObj,colarr,colName,colValue)
{
	if(arrObj==null) return;
	if (drpId.tagName!="SELECT")
		return false;
	drpId.length=0;
	if(! arrObj instanceof Array)
	{
		arrObj = [arrObj];
	}
	if(arrObj.length  <= 1)
	{
		drpId.disabled = true;
	}
	else
	{
		drpId.disabled = false;
	}
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	addBlankRow = (arrObj.length <= 1 && addBlankRow) || (arrObj.length > 1);
	for (i = 0; i < arrObj.length; i++)
	{
		if(eval("arrObj[i]."+colName) == colValue){
			var option = document.createElement("OPTION");
			option._text = eval("arrObj[i]."+colarr[0]);
			option.text = eval("arrObj[i]."+colarr[0]);
			option.value = eval("arrObj[i]."+colarr[1]);
			if ( (option.value == "")||(option.value == null) )
			{
				option.text	= " ";
			}
			//option.title = option.text;
			drpId.add(option);
		}
	}
}

function Select_Init_No_Id_With_Label(drpId,arrObj,colarr,pText,pValue)
{
	if(arrObj==null) return;
	
	var label = 0;
	if (drpId.tagName!="SELECT")
		return false;
	drpId.length=0;
	if(! arrObj instanceof Array)
	{
		arrObj = [arrObj];
	}
	if(arrObj.length  <= 1)
	{
		drpId.disabled = true;
	}
	else
	{
		drpId.disabled = false;
	}
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	for (i = 0; i < arrObj.length; i++)
	{
		var option = document.createElement("OPTION");			
		option._text = eval("arrObj[i]."+colarr[0]);		
		option.text = eval("arrObj[i]."+colarr[0]);
		option.value = eval("arrObj[i]."+colarr[1]);
		if(option.text == pText || option.value == pValue)
		{	
			option.text = eval("arrObj[i]."+colarr[0]);

			label = i;
		}	
		if ( (option.value == "")||(option.value == null) )
		{
			option.text	= " ";
		}
		//option.title = option.text;
		drpId.add(option);		
	}
	drpId.selectedIndex = label;
}


function Select_Init_Comments(drpId,arrObj,colarr,addBlanckRow)
{
	if(arrObj==null) return;
	
	if (drpId.tagName!="SELECT")
		return false;
	drpId.length=0;
	if(! arrObj instanceof Array)
	{
		arrObj = [arrObj];
	}
	if(arrObj.length  <= 1)
	{
		drpId.disabled = true;
	}
	else
	{
		drpId.disabled = false;
	}
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	addBlankRow = (arrObj.length <= 1 && addBlankRow) || (arrObj.length > 1);
	if(addBlanckRow) {
		var option = document.createElement("OPTION");
		option._text = "";
		option.text = SELECT_PLEASE_SELECT.replace(/-/g,"");
		option.value = "";
		option.comments = "";
		drpId.add(option);
	}
	
	for (i = 0; i < arrObj.length; i++)
	{
		var option = document.createElement("OPTION");
		option._text = eval("arrObj[i]."+colarr[0]);

		option.text = BuildSelectIDText(eval("arrObj[i]."+colarr[0]),eval("arrObj[i]."+colarr[1]));

		option.value = eval("arrObj[i]."+colarr[1]);
		option.comments = eval("arrObj[i]."+colarr[2]);
		if ( (option.value == "")||(option.value == null) )
		{
			option.text	= " ";
		}
		//option.title = option.text;
		drpId.add(option);
	}
}
function Select_Init_With_Value(drpId,arrObj,colarr,addBlankRow)
{
	if(arrObj==null) return;
	
	if (drpId.tagName!="SELECT")
		return false;
	drpId.length=0;
	if(! arrObj instanceof Array)
	{
		arrObj = [arrObj];
	}
	if(arrObj.length  <= 1)
	{
		drpId.disabled = true;
	}
	else
	{
		drpId.disabled = false;
	}
	if(arrObj.length <= 1 && addBlankRow)	//如果强制要加Please Select但是数据又小于等于一条，不灰化

	{
		drpId.disabled = false;
	}
	addBlankRow = (arrObj.length <= 1 && addBlankRow) || (arrObj.length > 1);
	if(addBlankRow){
		var option = document.createElement("OPTION");
		option.text = SELECT_PLEASE_SELECT.replace(/-/g,"");
		option.value = "";
		drpId.add(option);
	}

	for (i = 0; i < arrObj.length; i++)
	{
		var option = document.createElement("OPTION");
		option._text = eval("arrObj[i]."+colarr[0]);
		option._data=eval("arrObj[i]");
		option.text = eval("arrObj[i]."+colarr[0]);
		option.value = eval("arrObj[i]."+colarr[1]);
		if ( (option.value == "")||(option.value == null) )
		{
			option.text	= " ";
			option._data=null;
		}
		//option.title = option.text;
		drpId.add(option);
	}
}