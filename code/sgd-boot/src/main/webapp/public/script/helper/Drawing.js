/*************************************************************************
1)	Copyright by OSS R&D Dept. of ZTEsoft,2004-04-27
	File Name:Drawing.js
	Create Date:2005-11-10
	Author:fu.jianjun
	create Version:0.0.0.1
	Version:0.0.0.1
	Create Version Date:2005-11-10
	Description: 绘制图形函数
	Function Lists:	
*************************************************************************/
/*****************************************
函数名称：VMLImage
函数说明：定义VMLImage类

传入参数：VMLImage的位置参数

		left	:左边距

		top		:右边距

		width	:宽度
		height	:高度
		xDesc	:X轴的描述信息
		yDesc	:Y轴的描述信息
		zeroBased:坐标数据是否从0算起
		outputObject:VML图形输出到的Object
返回：新定义的VMLImage对象
*****************************************/
function VMLImage(left,top,width,height,xDesc,yDesc,zeroBased,outputObject)
{
	this.left = left;
	this.top = top;
	this.width = width;
	this.height = height;
	this.xDesc = xDesc;
	this.yDesc = yDesc;
	this.position = "bottom"; //控制显示描述信息的位置

	this.zeroBased = zeroBased==null?"auto":zeroBased.toString().toLowerCase();
	this.outputObject = outputObject;
	return this;
}
/**********************************************************
函数名称：drawBars
函数说明：绘制柱状图
传入参数：

		vmlImage	:VMLImage对象参数，包含了VML图形的宽度，高度，边距等参数信息
		columnWidth	:柱状图中每个柱子的宽度

		dataDesc	:数组，描述每个数据代表的意义描述信息
		dataArray	:绘制图形的数据值

返回：无
***********************************************************/
function drawBars(vmlImage,columnWidth,dataDesc,dataArray)
{	
	if(dataArray==null||dataArray.length==0) return;
	
	offsetLeft = vmlImage.left;
	offsetTop = vmlImage.top;
	canvasWidth = vmlImage.width;
	canvasHeight = vmlImage.height;
	xDesc = vmlImage.xDesc;
	yDesc = vmlImage.yDesc;
	if(null != vmlImage.outputObject)
	{
		if(typeof(vmlImage.outputObject)=="string")
			vmlImage.outputObject = document.all(vmlImage.outputObject);
		if(vmlImage.outputObject.tagName != null && vmlImage.outputObject.tagName != "DIV")
			vmlImage.outputObject = null;
	}
	parseNumber(dataArray);
	var spaceSpan = 15; //距离坐标轴两端的空间
	//只有一组数据的时候居中显示

	if(dataArray.length==1)
		spaceSpan = spaceSpan = (canvasWidth - dataDesc.length * columnWidth )/2;
	var yCoordCount = 10; //对Y轴进行均分的数目
	
	var maxValue=getMaxValue(dataArray);
	maxValue = maxValue==0?1:maxValue;
	var minValue =getMinValue(dataArray);
	var baseValue=0;
	if(vmlImage.zeroBased=="auto")
	{
		if(minValue != 0 && (maxValue - minValue)/minValue<0.1)
			vmlImage.zeroBased = "false";
	}
	if(vmlImage.zeroBased=="false")
	{
		baseValue = minValue-((maxValue - minValue)/(yCoordCount-1));
		if(baseValue==maxValue)
			baseValue=0;
	}
	
	var divImage = document.createElement("<div id='divImage' style='z-index:100;' width:"+eval(canvasWidth-50)+"px;height:"+eval(canvasHeight+50)+"px></div>");
	var eleRect = null;
	
	//画背景图
	eleRect = document.createElement("<v:rect style='position:absolute;left:"+offsetLeft+"px;top:"+eval(offsetTop-10)+"px;width:"+canvasWidth+"px;height:"+eval(canvasHeight+10)+"px;z-index:100' fillcolor='#9cf' stroked='f'></v:rect>");
	eleRect.appendChild(document.createElement("<v:fill rotate='t' angle='-45' focus='100%' type='gradient'/>"));
	divImage.insertBefore(eleRect);
	
	//The plus 20px used for end arrow
	var originalPoint = new Point(offsetLeft,canvasHeight+offsetTop);
	var xEndPoint = new Point(offsetLeft + canvasWidth + 20,originalPoint.y);
	var yEndPoint = new Point(offsetLeft,offsetTop-20);
	
	var eleShape = null;
	var eleTextBox = null;
	//画横坐标轴 X轴

	var eleLine = document.createElement("<v:line  alt='' style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+originalPoint.x+"px,"+originalPoint.y+"px' to='"+xEndPoint.x+"px,"+xEndPoint.y+"px'></v:line>");
	eleLine.appendChild(document.createElement("<v:stroke endarrow='classic' weight='1px'/>"));
	divImage.appendChild(eleLine);
	//标注X轴的描述
	eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft+canvasWidth) + "px;top:" + eval(offsetTop+canvasHeight+10) + "px;width:" + 60 + "px;height:18px;z-index:101'></v:shape>");
	eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
	eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + xDesc + "</td></tr></table>";
	eleShape.appendChild(eleTextBox);
	divImage.appendChild(eleShape);
	
	//画纵坐标轴 Y轴

	eleLine = document.createElement("<v:line alt='' style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100'  from='"+originalPoint.x+","+originalPoint.y+"px' to='"+yEndPoint.x+"px,"+yEndPoint.y+"px'></v:line>");
	eleLine.appendChild(document.createElement("<v:stroke startarrow='classic' weight='1px'/>"));
	divImage.appendChild(eleLine);
	//标注Y轴的描述
	eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft+10) + "px;top:" + eval(offsetTop-40) + "px;width:" + eval(canvasWidth-20) + "px;height:18px;z-index:101'></v:shape>");
	eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
	eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + yDesc + "</td></tr></table>";
	eleShape.appendChild(eleTextBox);
	divImage.appendChild(eleShape);
	
	//画三维背景 cornerPoint.x=280 cornerPoint.y=240
	var cornerPoint = new Point(offsetLeft + 10, canvasHeight + offsetTop - 10);
	
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+cornerPoint.x+"px,"+eval(offsetTop-10)+"px' to='"+cornerPoint.x+"px,"+cornerPoint.y+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' to='"+offsetLeft+"px,"+cornerPoint.y+"px' from='"+cornerPoint.x+"px,"+eval(offsetTop+canvasHeight)+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+cornerPoint.x+"px,"+cornerPoint.y+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+cornerPoint.y+"px' strokecolor='#69f'/>"));
	
	//画纵坐标 Y轴，均分为10等份
	var stepHeight = canvasHeight / yCoordCount;
	
	var leftEnd = offsetLeft -8;//坐标刻度的长度

	
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+eval(offsetTop-10)+"px' to='"+eval(offsetLeft+10)+"px,"+eval(offsetTop)+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+eval(offsetLeft+10)+"px,"+eval(offsetTop-10)+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+eval(offsetTop-10)+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+offsetTop+"px' to='"+leftEnd+"px,"+eval(offsetTop)+"px'/>"));
	for(var i=1;i< yCoordCount ;i++)
	{
		var spanHeight = offsetTop + canvasHeight - stepHeight*i -10 ;
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+spanHeight+"px' to='"+eval(offsetLeft+10)+"px,"+eval(spanHeight+10)+"px' strokecolor='#69f'/>"));
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+eval(offsetLeft+10)+"px,"+spanHeight+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+spanHeight+"px' strokecolor='#69f'/>"));
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+eval(spanHeight+10)+"px' to='"+leftEnd+"px,"+eval(spanHeight+10)+"px'/>"));
	}
	
	/******************************
		  画柱状图的主体部分

	******************************/
	//获得数据的缩放比率

	if(baseValue>0)
		maxValue -= baseValue;
	var ratio = canvasHeight/ maxValue;
	var newData = resetValue(ratio,baseValue,dataArray);
	
	/**********************************************
				标示数据在坐标轴
	**********************************************/
	var stepValue = maxValue / yCoordCount;
	for(var i=0;i<=yCoordCount;i++)
	{
		var spanHeight = offsetTop + canvasHeight - stepHeight*i -10 ;
		var coordValue = maxValue>=10?Math.round(stepValue*i+baseValue):formatShowData(stepValue*i+baseValue);
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft-80) + "px;top:" + spanHeight + "px;width:" + 70 + "px;height:18px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='right'>" + coordValue + "</td></tr></table>";
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
	}
	//柱子的数目

	var columnCount = dataArray.length;
	//获得柱子之间的间距

	var columnSpan=null;
	if(columnCount==1)
		columnSpan=spaceSpan;
	else
		columnSpan = (canvasWidth - spaceSpan*2 - columnCount * columnWidth)/(columnCount-1);
	//起画点坐标

	var leftStart = offsetLeft + spaceSpan;
	for(var i=0;i<columnCount;i++)
	{
		var topVal = canvasHeight + offsetTop - newData[i];
		var heightVal = newData[i];
		var columnColor = getColor(i);
		var eleRect = document.createElement("<v:rect style='position:absolute;left:"+leftStart+"px;top:"+topVal+"px;width:"+columnWidth+";height:"+heightVal+"px;z-index:101' fillcolor='"+columnColor.color1+"'></v:rect>");
		eleRect.appendChild(document.createElement("<v:fill color2='"+columnColor.color2+"' rotate='t' type='gradient'/>"));
		eleRect.appendChild(document.createElement("<o:extrusion v:ext='view' backdepth='20pt' color='"+columnColor.color1+"' on='t'/>"));
			
		
		//画柱状图上面的数据

		eleShape = document.createElement("<v:shape style='position:absolute;left:"+leftStart+"px;top:" +eval(topVal-25)+ "px;width:" + (columnWidth+15) + "px;height:18px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML = "<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='center'>" + dataArray[i] + "</td></tr></table>";
		eleShape.appendChild(eleTextBox);

		divImage.appendChild(eleRect);
		divImage.appendChild(eleShape);  	
				
  		//画柱状图下面标示的名称

  		eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(leftStart - columnWidth/2) + "px;top:" + eval(canvasHeight + offsetTop) + "px;width:" + eval(columnWidth+columnSpan) + "px;height:50px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML= "<table cellspacing='3' cellpadding='0'><tr><td align='left'>" + dataDesc[i] + "</td></tr></table>"
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
		
		leftStart += columnWidth+columnSpan;
	}
	if(null != vmlImage.outputObject)
		vmlImage.outputObject.appendChild(divImage);
	else
		document.body.appendChild(divImage);
}
/**********************************************************
函数名称：drawGroupBars
函数说明：绘制分组柱状图
传入参数：

		vmlImage	:VMLImage对象，包含了宽度，高度，边距等参数

		columnWidth	:柱状图中每个柱子的宽度

		dataDesc	:数据信息描述
		groupDesc	:分组信息描述
		dataArray	:二维数组，内容为分组数据的数组

返回：无
***********************************************************/
function drawGroupBars(vmlImage,columnWidth,dataDesc,groupDesc,dataArray)
{
	if(dataArray==null || dataArray.length==0) return;
	offsetLeft = vmlImage.left;
	offsetTop = vmlImage.top;
	canvasWidth = vmlImage.width;
	canvasHeight = vmlImage.height;
	xDesc = vmlImage.xDesc;
	yDesc = vmlImage.yDesc;
	if(null != vmlImage.outputObject)
	{
		if(typeof(vmlImage.outputObject)=="string")
			vmlImage.outputObject = document.all(vmlImage.outputObject);
		if(vmlImage.outputObject.tagName != null && vmlImage.outputObject.tagName != "DIV")
			vmlImage.outputObject = null;
	}
	
	var spaceSpan = 15; //距离坐标轴两端的空间
	//只有一组数据的时候居中显示

	if(dataArray.length==1)
		spaceSpan = spaceSpan = (canvasWidth - dataDesc.length * columnWidth )/2;
	
	var yCoordCount = 10; //对Y轴进行均分的数目
	var maxValue=0;
	for(var i=0;i<dataArray.length;i++)
	{
		parseNumber(dataArray[i]);
		var tmpMaxValue = getMaxValue(dataArray[i]);
		if(tmpMaxValue > maxValue) maxValue = tmpMaxValue;
	}
	maxValue = maxValue==0?1:maxValue;
	var minValue =dataArray[0][0];
	for(var i=0;i<dataArray.length;i++)
	{
		var tmpMinValue = getMinValue(dataArray[i]);
		if(tmpMinValue < minValue) minValue = tmpMinValue;
	}
	//判断是否从0开始算坐标
	var baseValue=0;
	if(vmlImage.zeroBased=="auto")
	{
		if(minValue != 0 && (maxValue - minValue)/minValue<0.1)
			vmlImage.zeroBased = "false";
	}
	if(vmlImage.zeroBased=="false")
	{
		baseValue = minValue-((maxValue - minValue)/(yCoordCount-1));
		if(baseValue==maxValue)
			baseValue=0;
	}
	
	var divImage = document.createElement("<div id='divImage' style='z-index:100;' width:"+eval(canvasWidth-50)+"px;height:"+eval(canvasHeight+50)+"px></div>");
	
	//画背景图
	var eleBackRect = document.createElement("<v:rect style='position:absolute;left:"+offsetLeft+"px;top:"+eval(offsetTop-10)+"px;width:"+canvasWidth+"px;height:"+eval(canvasHeight+10)+"px;z-index:100' fillcolor='#9cf' stroked='f'></v:rect>");
	
	eleBackRect.appendChild(document.createElement("<v:fill rotate='t' angle='-45' focus='100%' type='gradient'/>"));
	divImage.insertBefore(eleBackRect);
	
	//The plus 20px used for end arrow
	var originalPoint = new Point(offsetLeft,canvasHeight+offsetTop);
	var xEndPoint = new Point(offsetLeft + canvasWidth + 20,originalPoint.y);
	var yEndPoint = new Point(offsetLeft,offsetTop-20);
	
	var eleShape=null;
	var eleTextBox = null;
	//画横坐标轴 X轴

	var eleLine = document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+originalPoint.x+"px,"+originalPoint.y+"px' to='"+xEndPoint.x+"px,"+xEndPoint.y+"px'></v:line>");
	eleLine.appendChild(document.createElement("<v:stroke endarrow='classic' weight='1px'/>"));
	divImage.appendChild(eleLine);
	//标注X轴的描述
	eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft+canvasWidth) + "px;top:" + eval(offsetTop+canvasHeight+10) + "px;width:" + 60 + "px;height:18px;z-index:100'></v:shape>");
	eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
	eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + xDesc + "</td></tr></table>";
	eleShape.appendChild(eleTextBox);
	divImage.appendChild(eleShape);

	//画纵坐标轴 Y轴

	eleLine = document.createElement("<v:line alt='' style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100'  from='"+originalPoint.x+","+originalPoint.y+"px' to='"+yEndPoint.x+"px,"+yEndPoint.y+"px'></v:line>");
	eleLine.appendChild(document.createElement("<v:stroke startarrow='classic' weight='1px'/>"));
	divImage.appendChild(eleLine);
	//标注Y轴的描述
	eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft+10) + "px;top:" + eval(offsetTop-40) + "px;width:" + eval(canvasWidth-20) + "px;height:18px;z-index:100'></v:shape>");
	eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
	eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + yDesc + "</td></tr></table>";
	eleShape.appendChild(eleTextBox);
	divImage.appendChild(eleShape);
	
	//画三维背景 cornerPoint.x=280 cornerPoint.y=240
	var cornerPoint = new Point(offsetLeft + 10, canvasHeight + offsetTop - 10);
	
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+cornerPoint.x+"px,"+eval(offsetTop-10)+"px' to='"+cornerPoint.x+"px,"+cornerPoint.y+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' to='"+offsetLeft+"px,"+cornerPoint.y+"px' from='"+cornerPoint.x+"px,"+eval(offsetTop+canvasHeight)+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+cornerPoint.x+"px,"+cornerPoint.y+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+cornerPoint.y+"px' strokecolor='#69f'/>"));
	
	//画纵坐标 Y轴，均分为10等份
	var stepHeight = canvasHeight / yCoordCount;
	
	var leftEnd = offsetLeft -8;//坐标刻度的长度

	
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+eval(offsetTop-10)+"px' to='"+eval(offsetLeft+10)+"px,"+eval(offsetTop)+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+eval(offsetLeft+10)+"px,"+eval(offsetTop-10)+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+eval(offsetTop-10)+"px' strokecolor='#69f'/>"));
	divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+offsetTop+"px' to='"+leftEnd+"px,"+eval(offsetTop)+"px'/>"));
	for(var i=1;i< yCoordCount ;i++)
	{
		var spanHeight = offsetTop + canvasHeight - stepHeight*i -10 ;
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+spanHeight+"px' to='"+eval(offsetLeft+10)+"px,"+eval(spanHeight+10)+"px' strokecolor='#69f'/>"));
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+eval(offsetLeft+10)+"px,"+spanHeight+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+spanHeight+"px' strokecolor='#69f'/>"));
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+eval(spanHeight+10)+"px' to='"+leftEnd+"px,"+eval(spanHeight+10)+"px'/>"));
	}
	/******************************
		画柱状图的主体部分

	******************************/
	//获得数据的缩放比率

	if(baseValue > 0)
		maxValue -= baseValue;
	var ratio = canvasHeight/ maxValue;
	
	/**********************************************
				标示数据在坐标轴
	**********************************************/
	var stepValue = maxValue / yCoordCount;
	for(var i=0;i<=yCoordCount;i++)
	{
		var spanHeight = offsetTop + canvasHeight - stepHeight*i -10 ;
		var coordValue = maxValue>=10?Math.round(stepValue*i+baseValue):formatShowData(stepValue*i+baseValue);
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft-80) + "px;top:" + spanHeight + "px;width:" + 70 + "px;height:18px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='right'>" + coordValue + "</td></tr></table>";
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
	}
	
	//绘制柱状图的组数目

	var groupCount = groupDesc.length;
	//每组中包含的柱状图的数目，必须保证每组数据中的数据个数一致！！！
	var columnCount = dataDesc.length;
	//获得柱子之间的间距

	if(groupCount==1)
		columnSpan = spaceSpan;
	else
		columnSpan = (canvasWidth - spaceSpan*2 - columnCount * columnWidth*groupCount)/(groupCount-1);
	//起画点坐标

	var leftStart = offsetLeft + spaceSpan;
	for(var i=0;i<dataArray.length;i++)
	{
		var oldData = dataArray[i];
		var newData = resetValue(ratio,baseValue,oldData);
		for(var j=0;j<columnCount;j++)
		{
			var topVal = canvasHeight + offsetTop - newData[j];
			var heightVal = newData[j];
			var columnColor = getColor(j);
			var eleRect = document.createElement("<v:rect style='position:absolute;left:"+leftStart+"px;top:"+topVal+"px;width:"+columnWidth+";height:"+heightVal+"px;z-index:101' fillcolor='"+columnColor.color1+"'></v:rect>");
			eleRect.appendChild(document.createElement("<v:fill color2='"+columnColor.color2+"' rotate='t' type='gradient'/>"));
			eleRect.appendChild(document.createElement("<o:extrusion v:ext='view' backdepth='20pt' color='"+columnColor.color1+"' on='t'/>"));
			
			//画柱状图上面的数据

			eleShape = document.createElement("<v:shape style='position:absolute;left:"+leftStart+"px;top:" +eval(topVal-25)+ "px;width:" + (columnWidth+15) + "px;height:18px;z-index:101'></v:shape>");
			eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
			eleTextBox.innerHTML = "<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='center'>" + oldData[j] + "</td></tr></table>";
			eleShape.appendChild(eleTextBox);
			
			divImage.appendChild(eleRect);
			divImage.appendChild(eleShape);
			leftStart += columnWidth;
		}
		
		//画柱状图下面标示的名称

		eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(leftStart - columnWidth*columnCount) + "px;top:" + eval(canvasHeight + offsetTop) + "px;width:" + eval(columnWidth*columnCount +columnSpan) + "px;height:50px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML= "<table cellspacing='3' cellpadding='0'><tr><td align='left'>" + groupDesc[i] + "</td></tr></table>"
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
		
		leftStart += columnSpan;
	}
	//显示数据描述信息
	var rectWidth = 120; //信息框的长度
	if(vmlImage.position.toLowerCase()=="right")
	{
		var x1= offsetLeft + canvasWidth;
		var y1= offsetTop + 30;
		for(var i=0;i<columnCount;i++)
		{   
			var color = getColor(i);
			eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(x1+10)+ "px;top:" + (y1+ i*30) + "px;width:80px;height:" + 30 + "px;z-index:101'></v:shape>");
			eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
			eleTextBox.innerHTML = "<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + dataDesc[i] + "</td></tr></table>";
				eleShape.appendChild(eleTextBox);
				divImage.appendChild(eleShape);
			divImage.appendChild(document.createElement("<v:rect style='position:absolute;left:" + eval(x1+ rectWidth) + "px;top:" + eval(y1+ i * 30 +3) + "px;width:30px;height:20px;z-index:101' fillcolor='" +color.color1 + "'></v:rect>"));
		}
	}
	else
	{
		var x1= offsetLeft ;
		var y1= offsetTop+canvasHeight+50;
		var rowNum = 1;
		for(var i=0;i<columnCount;i++)
		{   
		var color = getColor(i);
		
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + x1+ "px;top:" + y1 + "px;width:"+eval(rectWidth+40)+"px;height:" + 30 + "px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML = "<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + dataDesc[i] + "</td></tr></table>";
			eleShape.appendChild(eleTextBox);
			divImage.appendChild(eleShape);
		divImage.appendChild(document.createElement("<v:rect style='position:absolute;left:" + eval(x1+rectWidth) + "px;top:" + eval(y1+5) + "px;width:30px;height:20px;z-index:101' fillcolor='" +color.color1 + "'></v:rect>"));
		if(rectWidth * (i+1) +50 >=canvasWidth)
		{
			x1 = offsetLeft;
			rowNum ++;
			y1 += rowNum * 30;
		}
		else
			x1 += rectWidth + 50;
		}
	}
	
	if(null != vmlImage.outputObject)
		vmlImage.outputObject.appendChild(divImage);
	else
		document.body.appendChild(divImage);
}
/**********************************************************
函数名称：drawLines
函数说明：画折线图

传入参数：

		vmlImage	:VMLImage对象，包含图形的宽度，高度，边距参数
		dataDesc	:数据信息描述
		groupDesc	:分组信息描述
		dataArray	:二维数组，内容保存点数据的数组

返回：无
***********************************************************/
function drawLines(vmlImage,dataDesc,groupDesc,dataArray)
{
	if(dataArray==null || dataArray.length==0) return;
	offsetLeft = vmlImage.left;
	offsetTop = vmlImage.top;
	canvasWidth = vmlImage.width;
	canvasHeight = vmlImage.height;
	xDesc = vmlImage.xDesc;
	yDesc = vmlImage.yDesc;
	if(null != vmlImage.outputObject)
	{
		if(typeof(vmlImage.outputObject)=="string")
			vmlImage.outputObject = document.all(vmlImage.outputObject);
		if(vmlImage.outputObject.tagName != null && vmlImage.outputObject.tagName != "DIV")
			vmlImage.outputObject = null;
	}
	var yCoordCount = 10; //对Y轴进行均分的数目
	var spaceSpan = 15; //距离坐标轴右端的空间
	var maxValue=0;
	for(var i=0;i<dataArray.length;i++)
	{
		parseNumber(dataArray[i]);
		var tmpMaxValue = getMaxValue(dataArray[i]);
		if(tmpMaxValue > maxValue) maxValue = tmpMaxValue;
	}
	maxValue = maxValue==0?1:maxValue;
	var minValue =dataArray[0][0];
	for(var i=0;i<dataArray.length;i++)
	{
		var tmpMinValue = getMinValue(dataArray[i]);
		if(tmpMinValue < minValue) minValue = tmpMinValue;
	}
	//判断是否从0开始算坐标
	var baseValue=0;
	if(vmlImage.zeroBased=="auto")
	{
		if(minValue != 0 && (maxValue - minValue)/minValue<0.1)
			vmlImage.zeroBased = "false";
	}
	if(vmlImage.zeroBased=="false")
	{
		baseValue = minValue-((maxValue - minValue)/(yCoordCount-1));
		if(baseValue==maxValue)
			baseValue=0;
	}
	
	var divImage = document.createElement("<div id='divImage' style='z-index:100;' width:"+eval(canvasWidth-50)+"px;height:"+eval(canvasHeight+50)+"px></div>");
	var eleRect = null;
	//画背景图
	eleRect = document.createElement("<v:rect style='position:absolute;left:"+offsetLeft+"px;top:"+offsetTop+"px;width:"+canvasWidth+"px;height:"+canvasHeight+"px;z-index:100' fillcolor='#9cf' stroked='f'></v:rect>");
	eleRect.appendChild(document.createElement("<v:fill rotate='t' angle='-45' focus='100%' type='gradient'/>"));
	divImage.appendChild(eleRect);
	
	//The plus 20px used for end arrow
	var originalPoint = new Point(offsetLeft,canvasHeight+offsetTop);
	var xEndPoint = new Point(offsetLeft + canvasWidth + 20,originalPoint.y);
	var yEndPoint = new Point(offsetLeft,offsetTop-20);
	
	var eleShape=null;
	var eleTextBox = null;
	//画横坐标轴 X轴

	var eleLine = document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+originalPoint.x+"px,"+originalPoint.y+"px' to='"+xEndPoint.x+"px,"+xEndPoint.y+"px'></v:line>");
	eleLine.appendChild(document.createElement("<v:stroke endarrow='classic' weight='1px'/>"));
	divImage.appendChild(eleLine);
	//标注X轴的描述
	eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft+canvasWidth) + "px;top:" + eval(offsetTop+canvasHeight+10) + "px;width:" + 60 + "px;height:18px;z-index:101'></v:shape>");
	eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
	eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + xDesc + "</td></tr></table>";
	eleShape.appendChild(eleTextBox);
	divImage.appendChild(eleShape);
	
	//画纵坐标轴 Y轴

	eleLine = document.createElement("<v:line alt='' style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100'  from='"+originalPoint.x+","+originalPoint.y+"px' to='"+yEndPoint.x+"px,"+yEndPoint.y+"px'></v:line>");
	eleLine.appendChild(document.createElement("<v:stroke startarrow='classic' weight='1px'/>"));
	divImage.appendChild(eleLine);
	//标注Y轴的描述
	eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft+10) + "px;top:" + eval(offsetTop-40) + "px;width:" + eval(canvasWidth-20) + "px;height:18px;z-index:101'></v:shape>");
	eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
	eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + yDesc + "</td></tr></table>";
	eleShape.appendChild(eleTextBox);
	divImage.appendChild(eleShape);
				
	//画纵坐标 Y轴，均分为10等份
	var stepHeight = canvasHeight / yCoordCount;
	
	var leftEnd = offsetLeft -8;//坐标刻度的长度

	
	for(var i=1;i<= yCoordCount ;i++)
	{
		var spanHeight = offsetTop + canvasHeight - stepHeight*i;
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+spanHeight+"px' to='"+eval(canvasWidth+offsetLeft)+"px,"+spanHeight+"px' strokecolor='#69f'/>"));
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;flip:y;z-index:100' from='"+offsetLeft+"px,"+spanHeight+"px' to='"+leftEnd+"px,"+spanHeight+"px'/>"));
	}
	/******************************
			画折线图的主体部分

	******************************/
	/**********************************************
				标示数据在纵坐标轴

	**********************************************/
	//if(baseValue>0)
		maxValue -= baseValue;
	//获得数据的缩放比率

	var ratio = canvasHeight/ maxValue;
	var stepValue = maxValue / yCoordCount;
	for(var i=0;i<=yCoordCount;i++)
	{
		var spanHeight = offsetTop + canvasHeight - stepHeight*i-10 ;
		var coordValue = maxValue>=10?Math.round(stepValue*i+baseValue):formatShowData(stepValue*i+baseValue);
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(offsetLeft-80) + "px;top:" + spanHeight + "px;width:" + 70 + "px;height:18px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML ="<table cellspacing='3' border='0' cellpadding='0' width='100%' height='100%'><tr><td align='right'>" + coordValue + "</td></tr></table>";
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
	}
	
	//坐标点个数，必须确保所有传入的数组长度一致！！！
	var dotCount = dataDesc.length; 
	//横坐标之间的坐标点宽度

	var stepWidth = (canvasWidth - spaceSpan)/(dotCount-1);
	//开始画折线
	for(var i=0;i<dataArray.length;i++)
	{
		var newData = resetValue(ratio,baseValue,dataArray[i]);
		var color = getColor(i);
		for(var j=0;j<dotCount;j++)
		{
			var x1 = offsetLeft+j*stepWidth;
			var y1 = offsetTop+canvasHeight - newData[j];
	  	
	  	var x2 = offsetLeft+(j+1)*stepWidth;
	  	var y2 = offsetTop+canvasHeight - newData[j+1];
			if(j<dotCount-1)
			{
				divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;z-index:101' from='" + x1 + "px," + y1 + "px' to='" + x2 + "px," + y2 + "px' coordsize='21600,21600' strokecolor='" + color.color1 + "' strokeweight='2px'></v:line>"));
			}
			//在线上打实心点

			divImage.appendChild(document.createElement("<v:oval style='position:absolute;left:" + (x1 - 2) + "px;top:" + (y1 - 2) + "px;width:4px;height:4px;z-index:101' fillcolor='" + color.color1 + "' strokecolor='" + color.color1 + "'/>"));
			
			//实心点旁写文字标识

			var tempTextbox=document.createElement("<v:TextBox style='position:absolute;left:" + (x1 +5) + "px;top:" + (y1 - 2) + "px;WIDTH: 59.874pt; HEIGHT: 29.562pt;z-index:999999'></v:TextBox>");
	
			var temptext=document.createTextNode(dataArray[i][j]);
			tempTextbox.appendChild(temptext);
			divImage.appendChild(tempTextbox);
		}
	}
	//在坐标轴下画刻度，标数据
	for(var j=0;j<dotCount;j++)
	{
		var x1 = offsetLeft+j*stepWidth;
		var y1 = offsetTop+canvasHeight;
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;z-index:101' from='" + x1 + "px," + y1 + "px' to='" + x1 + "px," + eval(y1+8) + "px'  strokecolor='#000000'></v:line>"));
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(x1- stepWidth/2)+ "px;top:" + eval(y1+8) + "px;width:" + stepWidth + "px;height:50px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:shape>");
		eleTextBox.innerHTML ="<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + dataDesc[j] + "</td></tr></table>";
		
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
	}
	
	//显示描述信息
	var rectWidth = 120; //信息框的长度
	if(vmlImage.position.toLowerCase()=="right")
	{
		var x1= offsetLeft + canvasWidth;
		var y1= offsetTop + 30;
		for(var i=0;i<groupDesc.length;i++)
		{   
			var color = getColor(i);
			eleShape = document.createElement("<v:shape style='position:absolute;left:" + eval(x1+10)+ "px;top:" + (y1+ i*30) + "px;width:80px;height:" + 30 + "px;z-index:101'></v:shape>");
			eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
			eleTextBox.innerHTML = "<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + groupDesc[i] + "</td></tr></table>";
				eleShape.appendChild(eleTextBox);
				divImage.appendChild(eleShape);
			divImage.appendChild(document.createElement("<v:rect style='position:absolute;left:" + eval(x1+ rectWidth) + "px;top:" + eval(y1+ i * 30 +3) + "px;width:30px;height:20px;z-index:101' fillcolor='" +color.color1 + "'></v:rect>"));
		}
	}
	else
	{
		var x1= offsetLeft ;
		var y1= offsetTop+canvasHeight+50;
		var rowNum = 1;
		for(var i=0;i<groupDesc.length;i++)
		{   
		var color = getColor(i);
		
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + x1+ "px;top:" + y1 + "px;width:"+eval(rectWidth+40)+"px;height:" + 30 + "px;z-index:101'></v:shape>");
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		eleTextBox.innerHTML = "<table cellspacing='3' cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + groupDesc[i] + "</td></tr></table>";
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
		divImage.appendChild(document.createElement("<v:rect style='position:absolute;left:" + eval(x1+rectWidth) + "px;top:" + eval(y1+5) + "px;width:30px;height:20px;z-index:101' fillcolor='" +color.color1 + "'></v:rect>"));
		if(rectWidth * (i+1) +50 >=canvasWidth)
		{
			x1 = offsetLeft;
			rowNum ++;
			y1 += rowNum * 30;
		}
		else
			x1 += rectWidth + 50;
		}
	}
	
	if(null != vmlImage.outputObject)
		vmlImage.outputObject.appendChild(divImage);
	else
		document.body.appendChild(divImage);
}
/**********************************************************
函数名称：drawPie
函数说明：画饼状图

传入参数：

		vmlImage	:VMLImage对象，包含图形的宽度，高度和边距参数
		dataDesc	:数组，描述每个数据代表的意义
		dataArray	:绘制图形的数据值

返回：无
***********************************************************/
function drawPie(vmlImage,dataDesc,dataArray)
{
	offsetLeft = vmlImage.left;
	offsetTop = vmlImage.top;
	canvasWidth = vmlImage.width;
	canvasHeight = vmlImage.height;
	if(null != vmlImage.outputObject)
	{
		if(typeof(vmlImage.outputObject)=="string")
			vmlImage.outputObject = document.all(vmlImage.outputObject);
		if(vmlImage.outputObject.tagName != null && vmlImage.outputObject.tagName != "DIV")
			vmlImage.outputObject = null;
	}
	var dataCount = dataDesc.length;
	parseNumber(dataArray);
	var totalValue = sumArray(dataArray);
	
	var divImage = document.createElement("<div id='divImage' style='z-index:100;' width:"+eval(canvasWidth-50)+"px;height:"+eval(canvasHeight+50)+"px></div>");
	var PreAngle = 0;
	var eleShape = null;
	var eleTextBox = null;
	for(var i=0;i<dataCount;i++)
	{ 
		var color = getColor(i);
		eleShape = document.createElement("<v:shape style='position:absolute;left:" + offsetLeft + "px;top:" + offsetTop + "px;width:" + canvasWidth + "px;height:" + canvasHeight + "px;z-index:101' coordsize='1500,1400' o:spt='100' adj='0,,0' path='m750,700ae750,700,750,700," + parseInt(23592960*PreAngle) + "," + parseInt(23592960*dataArray[i]/totalValue) + "xe' fillcolor='" + color.color1 + "' strokecolor='#FFFFFF'></v:shape>");
		eleShape.appendChild(document.createElement("<v:fill color2='" + color.color2 + "' rotate='t' focus='100%' type='gradient'/>"));
		eleShape.appendChild(document.createElement("<v:stroke joinstyle='round'/>"));
		eleShape.appendChild(document.createElement("<v:formulas/>"));
		eleShape.appendChild(document.createElement("<v:path o:connecttype='segments'/>"));
		eleShape.appendChild(document.createElement("<v:extrusion type='view' on='t' backdepth='20' brightness='0.2' rotationangle='0,0' />"));
		divImage.appendChild(eleShape);
		
		PreAngle += dataArray[i] / totalValue;
	}

	var pie = Math.PI;
	var TempPie = 0;
	var startX = offsetLeft + canvasWidth/2;
	var startY = offsetTop + canvasHeight/2;
	
	for(var i=0;i<dataCount;i++)
	{
		var TempAngle = pie * 2 * (dataArray[i] / (totalValue * 2) + TempPie);
		var x1 = startX + Math.cos(TempAngle) * canvasWidth * 3/8;
		var y1 = startY - Math.sin(TempAngle) * canvasHeight * 3/8;
		var x2 = startX + Math.cos(TempAngle) * canvasWidth * 3/4;
		var y2 = startY - Math.sin(TempAngle) * canvasHeight * 3/4;
	
		if(x2>offsetLeft + canvasWidth/2)
		{
			x3 = x2 + 20;
			x4 = x3;
		}
		else
		{
			x3 = x2 - 20;
			x4 = x3 - 100;
		}
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;z-index:101' from='" + x1 + "px," + y1 + "px' to='" + x2 + "px," + y2 + "px' coordsize='21600,21600' strokecolor='#111111' strokeweight='1px'></v:line>"));
		divImage.appendChild(document.createElement("<v:line style='position:absolute;left:0;text-align:left;top:0;z-index:101' from='" + x2 + "px," + y2 + "px' to='" + x3 + "px," + y2 + "px' coordsize='21600,21600' strokecolor='#111111' strokeweight='1px'></v:line>"));
		var txtValue= dataDesc[i] + " " + eval(Math.round(parseFloat(dataArray[i] * 100/ totalValue)*100)/100)+"%";
		eleShape = document.createElement("<v:shape title=\""+txtValue+"\" style='position:absolute;left:" + x4 + "px;top:" + (y2 - 20) + "px;width:150px;height:30px;z-index:101'></v:shape>")
		eleTextBox = document.createElement("<v:textbox inset='0px,0px,0px,0px'></v:textbox>");
		//eleTextBox.innerHTML = "<table cellspacing='3' border=0 cellpadding='0' width='100%' height='100%'><tr><td align='left'>" + dataDesc[i] + " " + Math.round(parseFloat(dataArray[i] * 100/ totalValue)*100)/100 + "%</td></tr></table>";
		eleTextBox.innerHTML = "<input type='text' style='border:0;background-color:transparent;font-family: Tahoma;font-size:8pt;' readonly title=\""+txtValue+"\" value='"+txtValue+"'>";
		eleShape.appendChild(eleTextBox);
		divImage.appendChild(eleShape);
		TempPie += dataArray[i]/totalValue;  
	}
	if(null != vmlImage.outputObject)
		vmlImage.outputObject.appendChild(divImage);
	else
		document.body.appendChild(divImage);
}
/**********************************
函数名称：Point
函数说明：定义点坐标类

传入参数：

返回：

**********************************/
function Point(x,y)
{
	this.x=x;
	this.y=y;
	return this;
}
/**********************************
函数名称：Color
函数说明：定义颜色类
传入参数：

返回：

**********************************/
function Color(color1,color2)
{
	this.color1 = color1;
	this.color2 = color2;
	return this;
}

/**********************************
函数名称：getMaxValue
函数说明：获得数组中的最大值

传入参数：

	dataArray:查找最大值的数组
返回：数组中的最大值

**********************************/
function getMaxValue(dataArray)
{
	if(null==dataArray || dataArray.length==0) return 0;
	var maxValue = 0;
	for(var i=0;i<dataArray.length;i++)
	{
		if(dataArray[i]>maxValue)
			maxValue = dataArray[i];
	}
	return maxValue;
}
/**********************************
函数名称：getMinValue
函数说明：获得数组中的最小值

传入参数：

	dataArray:查找最小值的数组
返回：数组中的最小值

**********************************/
function getMinValue(dataArray)
{
	if(null==dataArray || dataArray.length==0) return null;
	var minValue = dataArray[0];
	for(var i=0;i<dataArray.length;i++)
	{
		if(dataArray[i]<minValue)
			minValue = dataArray[i];
	}
	return minValue;
}
/**********************************
函数名称：resetValue
函数说明：对数组中的值按比率缩放
传入参数：

		ratio:比率值

		baseValue:要减去的值

		dataArray:进行缩放数据的数组

返回：缩放后的新数组
**********************************/
function resetValue(ratio,baseValue,dataArray)
{
	if(null==dataArray || dataArray.length==0) return;
	var newArray = new Array();
	for(var i=0;i<dataArray.length;i++)
	{
		newArray[i] = (dataArray[i]-baseValue)*ratio;
	}
	return newArray;
}

/*****************************************
函数名称：getColor
函数说明：获取指定位置的颜色值

传入参数：颜色值索引

返回：颜色变量对象

*****************************************/
function getColor(index)
{
	index = parseInt(index)%8;
	var color = new Color();
	switch(index)
	{
		case 0:
			{
				color.color1 = "#00ff00";
				color.color2 = "#d1ffd1";
			}
			break;
		case 1:
			{
				color.color1 = "#ff0000";
				color.color2 = "#ffbbbb";
			}
			break;
		case 2:
			{
				color.color1 = "#33cccc";
				color.color2 = "#cff4f3";
			}
			break;
			return "#ffe3bb";
		case 3:
			{
				color.color1 = "#ff9900";
				color.color2 = "#ffbbbb";
			}
			break;
		case 4:
			{
				color.color1 = "#666699";
				color.color2 = "#d9d9e5";
			}
			break;
		case 5:
			{
				color.color1 = "#993300";
				color.color2 = "#ffc7ab";
			}
			break;
		case 6:
			{
				color.color1 = "#99cc00";
				color.color2 = "#ecffb7";
			}
			break;
		case 7:
			{
				color.color1 = "#ff0000";
				color.color2 = "#FF0000";
			}
			break;
		default:
			return "";
	}
	return color;
}
/*****************************************
函数名称：sumArray
函数说明：对数组中的值进行求和

传入参数：进行求和的数组
返回：求和后的值

*****************************************/
function sumArray(dataArray)
{
	var sumValue = 0;
	for(var i=0;i<dataArray.length;i++)
		sumValue = eval(sumValue+dataArray[i]);
	return sumValue;
}
/*****************************************
函数名称：parseNumber
函数说明：对数组中的值转换为数值型
传入参数：进行转换数值的数组
返回：转换后的值

*****************************************/
function parseNumber(dataArray)
{
	if(dataArray ==null || dataArray.length==0) return;
	for(var i=0;i<dataArray.length;i++)
		dataArray[i] = dataArray[i] *1;
}
/*****************************************   
函数名称：formatShowData   
函数说明：截取小数点后两位数字，统一数据，如33.333 -> 33.33      
传入参数：进行转换的数值  
返回：转换后的值   
*****************************************/   
function formatShowData(str)
{	
	var strCurrent = str.toString();
    var pos = strCurrent.indexOf(".");
    if(pos>0)
    {
		if(strCurrent.length-pos<=2)
			strCurrent=strCurrent+"0";
		else
			strCurrent=strCurrent.substr(0,pos+3);		
    }
    else
		strCurrent = strCurrent+".00";
    if(strCurrent==".00")
		strCurrent="0.00";
	return strCurrent;
}