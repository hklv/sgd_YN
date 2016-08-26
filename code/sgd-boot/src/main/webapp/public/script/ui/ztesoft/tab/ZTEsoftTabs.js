function ZTEsoftTabs(tab,showClose)
{
	this.tabs = tab;
	this.eventHandlers = new Object();
	this.tabsArray = new Array();
	this.tabHeaders = null;
	this.tabContents = null;
	this.selectedIndex = null;
	this.selectedTab = null;
	this.index = 0;
	this.showClose = showClose;	
	if(this.tabs==null)
	{
		alert("There is no element ,please check for it.");
		return;
	}
	if(this.tabs.childNodes.length==0)
	{
		this.tabHeaders = document.createElement("ul");
		this.tabHeaders.className="TabHeaders";
		this.tabContents = document.createElement("div");
		this.tabs.appendChild(this.tabHeaders);
		this.tabs.appendChild(this.tabContents);
	}
	else
	{
		this.tabHeaders = this.tabs.childNodes[0];
		this.tabHeaders.className="TabHeaders";
		this.tabContents = this.tabs.childNodes[1];
		if(this.tabHeaders.childNodes.length!=this.tabContents.childNodes.length)
		{
			alert("The count of tabHeader is not equal to the count of tabContent,please check for it.");
		}
		this.synTab();
		if(this.tabHeaders.childNodes.length>0)
		{
			this.setTabSelected(0);
		}	
	}
	this.tabPagesMode = 0;
	this.pageControlLi = document.createElement("li");
	this.pageControlLi.className = "PageControlLi";
	this.pageControlDiv = document.createElement("div");
	
	this.leftSpan = document.createElement("span");
	this.leftSpan.className = "LeftSpan";
	this.leftSpan.onclick = new Delegate(this, this.onLeftSpanClick);
	
	this.pageControlDiv.appendChild(this.leftSpan);
	
	this.rightSpan = document.createElement("span");
	this.rightSpan.className = "RightSpan";
	this.rightSpan.onclick = new Delegate(this, this.onRightSpanClick);
	this.pageControlDiv.appendChild(this.rightSpan);
	
	this.pageControlLi.appendChild(this.pageControlDiv);
	this.pageControlLi.style.display="none";
	this.tabHeaders.appendChild(this.pageControlLi);
	this.displayTabIndexBegin = 0;
		
	this.setTabPages();
}
/**
 * 改变tab页显示模式


 * @param {Object} mode
 */
ZTEsoftTabs.prototype.setTabPagesMode=function(mode)
{
	this.tabPagesMode = mode ;
	
	if(this.tabPagesMode==0)
	{		
		var nodes = this.tabHeaders.childNodes;
		for(var i=0;i<nodes.length-1;i++)
		{
			nodes[i].style.display = "";
		}
		this.pageControlLi.style.display="none";
		return;
	}	
	this.setTabPages();
}
/**
 * 点击向右按钮
 */
ZTEsoftTabs.prototype.onRightSpanClick=function()
{
	this.displayTabIndexBegin = this.displayTabIndexBegin+1;
	this.setTabPages();
}
/**
 * 点击向左按钮
 */
ZTEsoftTabs.prototype.onLeftSpanClick=function()
{
	this.displayTabIndexBegin = this.displayTabIndexBegin-1;
	this.setTabPages();
}
/**
 * 显示模式1下,设置tab页显示


 */
ZTEsoftTabs.prototype.setTabPages=function()
{
	if(this.tabPagesMode==0)
	{
		return ;
	}
	var nodes = this.tabHeaders.childNodes;
	var totalTabsWidth= 0;
	var displayTabIndexEnd = -1; 
	//writeLog("Ul width="+this.tabHeaders.offsetWidth);
	this.pageControlLi.style.display = "none";
	for(var i=0;i<nodes.length-1;i++)
	{		
		if(i<this.displayTabIndexBegin)
		{
			nodes[i].style.display = "none";
		}
		if(displayTabIndexEnd>-1)
		{
			nodes[i].style.display = "none";
		}
		else
		{		
			if(i>=this.displayTabIndexBegin)
			{
				nodes[i].style.display = "";
				totalTabsWidth = totalTabsWidth+nodes[i].offsetWidth+1;
			}
			//writeLog("totalTabsWidth="+totalTabsWidth);
			if(totalTabsWidth+20>=this.tabHeaders.offsetWidth)
			{				
				this.pageControlLi.style.display = "";
				nodes[i].style.display = "none";
				displayTabIndexEnd = i;				
			}
		}
	}	
	//writeLog("displayTabIndexEnd="+displayTabIndexEnd);
	if(displayTabIndexEnd==-1&&this.displayTabIndexBegin>=0)
	{
		for(var i=this.displayTabIndexBegin-1;i>-1;i--)
		{
			nodes[i].style.display = "";
			totalTabsWidth = totalTabsWidth+nodes[i].offsetWidth+1;
			//writeLog("totalTabsWidth="+totalTabsWidth);
			if(totalTabsWidth+20>=this.tabHeaders.offsetWidth)
			{				
				this.pageControlLi.style.display = "";
				nodes[i].style.display = "none";
				displayTabIndexEnd = i;
				break;
			}
		}
		this.rightSpan.disabled = true;
		this.rightSpan.className = "RightSpanGray";
	}
	else
	{
		this.rightSpan.disabled = false;
		this.rightSpan.style.filter = "RightSpan";
	}
	if(this.displayTabIndexBegin==0)
	{
		this.leftSpan.disabled = true;
		this.leftSpan.className = "LeftSpanGray";
	}
	else
	{
		this.leftSpan.disabled = false;
		this.leftSpan.className = "LeftSpan";		
	}
}
/**
 * 增加Tab
 * @param {Object} tabName
 * @param {Object} height
 * @param {Object} id
 */
ZTEsoftTabs.prototype.insertTab= function(tabName,height,id)
{
	var tab = new Tab(this, tabName,height,true,this.index,id);
	this.tabsArray.push(tab);
	this.index += 1;
	this.selectedIndex = this.index-1;
	this.selectedTab = this.tabsArray[this.selectedIndex];
	this.setTabPages();
	return tab;
}
/**
 * 设置Tab页的内容
 * @param {Object} tabName
 * @param {Object} height
 * @param {Object} id
 */
ZTEsoftTabs.prototype.setTabContentById= function(id,innerHTML)
{
	for(var i=0;i<this.tabsArray.length;i++)
	{
		if(this.tabsArray[i].id == id)
		{
			this.tabsArray[i].setTabContent(innerHTML);
			break;	
		}
	}
}
/**
 * 设置Tab页的内容
 * @param {Object} tabName
 * @param {Object} height
 * @param {Object} id
 */
ZTEsoftTabs.prototype.setTabContent= function(index,innerHTML)
{
	if(this.tabsArray[index]!=null)
	{
		this.tabsArray[index].setTabContent(innerHTML);
	}
}
ZTEsoftTabs.prototype.getTab= function(index)
{
	return this.tabsArray[index];
}
ZTEsoftTabs.prototype.getTabById= function(id)
{
	for(var i=0;i<this.tabsArray.length;i++)
	{
		if(this.tabsArray[i].id == id)
		{
			return this.tabsArray[i];			
		}
	}	
}
/**
 * 同步tab,不要在外面调用


 */
ZTEsoftTabs.prototype.synTab= function()
{
	for(var i=0;i<this.tabHeaders.childNodes.length;i++)
	{
		var tab = new Tab(this,"","",false,i,this.tabHeaders.childNodes[i].id);
		this.tabsArray.push(tab);
		this.tabContents.childNodes[i].style.display="none";
	}	
}
/**
 * 删除tab
 * @param {Object} closeIndex
 */
ZTEsoftTabs.prototype.deleteTab= function(closeIndex)
{
	if(closeIndex==null){
		closeIndex = event.srcElement.parentElement.parentElement.id.substr(9);
	}
	if(this.tabsArray[closeIndex].fireEventReturn("onTabDelete")==false)
	{
		return;
	}
	var tab = this.tabHeaders.childNodes[closeIndex];
	tab.focus();
	var content = this.tabContents.childNodes[closeIndex];

	if(tab!=null)
	{
		this.tabHeaders.removeChild(tab);
	}
	if(content!=null)
	{
		 this.tabContents.removeChild(content);
		 content.removeNode(false);
	}
	this.deleteNodes(tab);
	this.deleteNodes(content);
	this.tabsArray.splice(closeIndex,1);
	if(this.tabHeaders.childNodes.length>0)
	{
		for(var i = closeIndex;i<this.tabHeaders.childNodes.length;i++)
		{
			this.tabHeaders.childNodes[i].id = "tabHeader"+i;
		}
		if(closeIndex>this.selectedIndex)
		{
			this.setTabPages();
			this.index -= 1;
			return;
		}
		else
		{
			var selectedIndex =(this.selectedIndex==0)?0:this.selectedIndex-1;			
		}
		this.index -= 1;
		this.setTabSelected(selectedIndex);
	}
	else
	{
		this.selectedIndex = 0;
		this.index = 0;
		this.selectedTab = null;		
	}
	this.setTabPages();
}
/**
 * 根据Id删除Tab
 * @param {Object} id
 */
ZTEsoftTabs.prototype.deleteTabById= function(id)
{
	for(var i=0;i<this.tabsArray.length;i++)
	{
		if(this.tabsArray[i].id == id)
		{
			this.deleteTab(i);
			break;	
		}
	}		
}
/**
 * 清除Node
 */
ZTEsoftTabs.prototype.deleteNodes=function(node)
{
	if(node==null)  return ;
	for(var i = node.childNodes.length - 1; i >= 0; i--) { 
		var childNode = node.childNodes[i]; 
		arguments.callee(childNode); 
		childNode = null; 
	} 
	node.removeNode(false); 
	node = null; 
}
/**
 * 删除全部tab
 */
ZTEsoftTabs.prototype.deleteAllTabs= function()
{
	while(this.tabsArray.length>0)
	{
		var tab = this.tabHeaders.childNodes[0];
		var content = this.tabContents.childNodes[0];
		this.tabHeaders.removeChild(tab);
		this.tabContents.removeChild(content);
		this.deleteNodes(tab);
		this.deleteNodes(content);
		this.tabsArray.splice(0,1);
	}	
	this.selectedIndex = 0;
	this.selectedTab = this.tabsArray[this.selectedIndex];
	this.index = 0;
	this.setTabPages();
}
/**
 * 获取当期选中的tab的id属性


 */
ZTEsoftTabs.prototype.getSeletedTabId= function()
{
	if(this.tabsArray.length==0)
		return null;
	else
		return this.tabsArray[this.selectedIndex].id;
}
/**
 * 根据Id设置tab选中
 * @param {Object} Id
 */
ZTEsoftTabs.prototype.setTabSelectedById= function(id)
{
	for(var i=0;i<this.tabsArray.length;i++)
	{
		if(this.tabsArray[i].id == id)
		{
			this.tabsArray[i].tabHeader.style.display = "block";
			this.setTabSelected(i);
		}
		else
		{
			if(this.tabsArray[i].titleDisplay == false)
			{
				this.tabsArray[i].tabHeader.style.display = "none";
			}
		}
	}		
}
/**
 * 根据index设置tab选中
 * @param {Object} index
 */
ZTEsoftTabs.prototype.setTabSelected=function(index)
{
	if(index==this.selectedIndex)
	{
		return;
	}
	else
	{
		if(this.selectedTab!=null)
		{			
			this.selectedTab.tabHeader.className="";
			this.selectedTab.tabContent.style.display="none";
		}		
		this.tabsArray[index].tabHeader.className="TabHeaderSelected";
		this.tabsArray[index].tabContent.style.display="block";
		this.selectedIndex = index;	
		this.selectedTab = this.tabsArray[this.selectedIndex];	
		this.fireEvent("onSelectIndexChange");
	}	
}
/**
 * 用代码的方式点击某个Tab,尽量不要在外面调用


 * @param {Object} newTabIndex
 * @param {Object} oldTabIndex
 */
ZTEsoftTabs.prototype.clickTab= function(newTabIndex)
{
	this.setTabSelected(newTabIndex);	
	this.tabsArray[newTabIndex].fireEvent("onTabClick");	
}
/**
 * 根据ID修改tab头的内容
 * @param {Object} id
 * @param {Object} newName
 */
ZTEsoftTabs.prototype.modifyTabNameById = function(id,newName)
{
	for(var i=0;i<this.tabsArray.length;i++)
	{
		if(this.tabsArray[i].id == id)
		{
			this.modifyTabName(i,newName);
			break;	
		}
	}	
	
}
/**
 * 修改tab头的内容
 * @param {Object} index
 * @param {Object} newName
 */
ZTEsoftTabs.prototype.modifyTabName = function(index,newName)
{
	this.tabsArray[index].modifyTabName(newName);
	this.setTabPages();
}

ZTEsoftTabs.prototype.fireEvent= function(eventType)
{	
	if (this.eventHandlers[eventType] != null)
	{
		for (var i=0; i<this.eventHandlers[eventType].length; i++)
		{
			this.eventHandlers[eventType][i]();
		}
	}
}

ZTEsoftTabs.prototype.setOnSelectIndexChange= function(onTabClick)
{
	if (this.eventHandlers["onSelectIndexChange"] == null)
	{
		this.eventHandlers["onSelectIndexChange"] = new Array();
	}

	this.eventHandlers["onSelectIndexChange"].push(onTabClick);
}

/**
 * 单独的tab对象
 * @param {Object} tabs
 * @param {Object} tabName
 * @param {Object} height
 * @param {Object} isNew
 * @param {Object} index
 * @param {Object} id
 */
function Tab(tabs,tabName,height,isNew,index,id)
{
	this.eventHandlers = new Object();
	this.id = id;
	this.parent = tabs;
	this.tabHeader = null;
	this.tabContent = null;
	this.closeDiv = null;
	this.enabled = true;
	this.titleDisplay = true;
	this.index = index;
	if(isNew==true)
	{
		var tabLi = document.createElement("li");
		if(this.parent.tabsArray.length>0)
		{
			this.parent.tabHeaders.childNodes[this.parent.selectedIndex].className="";
		}
		tabLi.id="tabHeader"+index;
		tabLi.className="TabHeaderSelected";	
		
		var tabLiA = document.createElement("span");
		tabLiA.innerText = tabName;
		tabLiA.onclick = new Delegate(this, this.onTabClick);
		tabLi.appendChild(tabLiA);
		var closeDiv = document.createElement("div");
		closeDiv.onclick = new Delegate(this.parent,this.parent.deleteTab);
		if(this.parent.showClose!=true)
		{
			closeDiv.style.width = "0px";			
			closeDiv.style.height = "0px";
		}	
		tabLiA.appendChild(closeDiv);		
		this.closeDiv = closeDiv;		
		this.parent.tabHeaders.insertBefore(tabLi,this.parent.pageControlLi);
		this.tabHeader = tabLi;
	
		var tabContent = document.createElement("div");
		if(height!=null)
		{
			tabContent.style.height=height;
		}
		if(this.parent.tabsArray.length>0)
		{
			this.parent.tabContents.childNodes[this.parent.selectedIndex].style.display="none";
		}
		this.tabContent = tabContent;
		this.parent.tabContents.appendChild(tabContent);
		
	}
	else
	{
		this.parent.tabHeaders.childNodes[index].id="tabHeader"+index;
		this.parent.index += 1;
		
		var tabHeader = this.parent.tabHeaders.childNodes[index];
		var headerText = tabHeader.childNodes[0].innerText;

		tabHeader.innerHTML = "";
		
		var tabSpan = document.createElement("span");
		tabSpan.innerText = headerText;
		tabSpan.onclick = new Delegate(this, this.onTabClick);
		tabHeader.appendChild(tabSpan);
	
		
		var closeDiv = document.createElement("div");
		closeDiv.onclick = new Delegate(this.parent,this.parent.deleteTab);

		if(this.parent.showClose!=true)
		{
			closeDiv.style.width = "0px";			
			closeDiv.style.height = "0px";
		}	
		tabHeader.childNodes[0].appendChild(closeDiv);
		this.closeDiv = closeDiv;
		this.parent.tabHeaders.childNodes[index].childNodes[0].onclick = new Delegate(this, this.onTabClick);					
		this.tabHeader = this.parent.tabHeaders.childNodes[index];
		this.tabContent  = this.parent.tabContents.childNodes[index];
	}
}
/**
 * 设置tab是否可用
 * @param {Object} flag
 */
Tab.prototype.setEnabled = function(flag)
{
	this.enabled = flag;
	if(flag)
	{
		this.tabHeader.style.filter = "";	
		this.closeDiv.onclick = new Delegate(this.parent,this.parent.deleteTab);	
	}
	else
	{
		//this.tabHeader.style.filter = "Gray()";
		this.closeDiv.onclick = null;
	}
}
/**
 * 设置tab标题是否可见
 * @param {Object} flag
 */
Tab.prototype.setTitleDisplay = function(flag)
{
	this.titleDisplay = flag;
}

/**
 * 设置tab的内容

 */
Tab.prototype.setTabContent = function(innerHTML)
{
	this.tabContent.innerHTML = innerHTML;
}
Tab.prototype.modifyTabName = function(newName)
{
	var divClose = this.tabHeader.firstChild.children[0];
	this.tabHeader.firstChild.innerText = newName; 
	if(divClose!=null)
	{
		this.tabHeader.firstChild.appendChild(divClose);	
	}	
}
/*
 * tab单击处理处理
 */
Tab.prototype.onTabClick = function()
{
	if(this.enabled)
	{
		var newTabIndex =  event.srcElement.parentElement.id.substr(9);
		this.parent.clickTab(newTabIndex);	 
	}
}
/**
 * 设置tab的单击处理函数

 * @param {Object} onTabClick
 */
Tab.prototype.setOnTabClick= function(onTabClick)
{
	if (this.eventHandlers["onTabClick"] == null)
	{
		this.eventHandlers["onTabClick"] = new Array();
	}

	this.eventHandlers["onTabClick"].push(onTabClick);
}
Tab.prototype.setOnTabDelete= function(onTabDelete)
{
	if (this.eventHandlers["onTabDelete"] == null)
	{
		this.eventHandlers["onTabDelete"] = new Array();
	}

	this.eventHandlers["onTabDelete"].push(onTabDelete);
}

Tab.prototype.fireEvent= function(eventType)
{	
	if (this.eventHandlers[eventType] != null)
	{
		for (var i=0; i<this.eventHandlers[eventType].length; i++)
		{
			this.eventHandlers[eventType][i]();
		}
	}
}

Tab.prototype.fireEventReturn= function(eventType)
{	
	var isFire = true;
	if (this.eventHandlers[eventType] != null)
	{
		for (var i=0; i<this.eventHandlers[eventType].length; i++)
		{
			isFire = this.eventHandlers[eventType][i]();
			if(isFire==false)
			{
				isFire = false;
				break;
			}
		}
	}
	return isFire;
}
/**
 * 一个模仿.net中delegate的东西，主要是替换了事件主体
 * @param {Object} context 事件上下文，通常用以取代默认的发生事件的元素
 * @param {Function} fun 事件处理函数
 */
function Delegate(context, fun)
{
	var args = Array.prototype.slice.call(arguments).slice(2);
	return function()
	{
		return fun.apply(context, Array.prototype.slice.call(arguments).concat(args));
	};
}