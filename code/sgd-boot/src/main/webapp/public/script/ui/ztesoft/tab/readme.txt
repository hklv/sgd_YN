ZTEsoftTabs使用说明

现在版本的Tabs同时支持IE和FireFox，使用事项如下：

1.功能引入
要使用ZTEsoftTabs功能，界面上必须引入以下两个文件，分别是样式和实现代码：
<link rel="stylesheet" href="ZTEsoftTabs.css"/>
<script language="JavaScript" src="ZTEsoftTabs.js"></script>

注：样式一般会在头文件中固定引入


2.界面定义
界面代码中的定义，类似如下写法：
<div id="TestTabs">
	<ul>
		<li id="tabId1"><span>AbnormalCDRQry</span></li>
		<li id="tabId2"><span>ErrorCDRQry</span></li>				
	</ul>
	<div style="height:500px;">
		<div>对应AbnormalCDRQry的内容</div>
		<div>对应ErrorCDRQry的内容</div>
	</div>
</div>

注：也可以只有	<div id="TestTabs"></div>，而在js中生成相应的tab页面。


3.JavaScript定义
代码如下，其中，ZTEsoftTabs构造函数的第一个参数是界面定义表格的id，第二个参数是表示Tab页面是否可以被关闭，true表示可以，如果第二个参数不传默认为不可以。

var tab = new ZTEsoftTabs("TestTabs",true);

4.插入新Tab页面
使用insertTab方法可以插入新的页面,其中第一个参数为Tab页名称，第二个参数为Tab页高度,可以不传,第三个参数为Tab页的id：

tab.insertTab("tabName",500,"tabId3");

5.关闭Tab页面
点击Tab页面上的关闭按钮可以关闭Tab页，当自定义事件返回值为false时不触发。


6.根据标签页id关闭标签页

其中参数为标签页id：

tab.deleteTabById("tab3Id");

7.关闭所有标签页
强制关闭所有标签页，不管关闭自定义事件的返回值为什么：
tab.deleteAllTabs();

8.获得当前选中标签页的id
tab.getSeletedTabId();

9.修改标签页标题

tab.modifyTabName(index,newName);按照数组的方式变换第index+1个标签页的标题

tab.modifyTabNameById(id,newName);根据标签页的Id变换标签页的标题

10.给tab标签页关联对应的内容：使用与appendChild的情况，即对应的content为元素，相当于给<div>对应AbnormalCDRQry的内容</div>添加子节点

tab.insertTabContent(id,content);根据标签页的Id关联标签页的内容
tab.insertTabContent(index,content);按照数组的方式关联第index+1个标签页的内容


10.添加自定义事件处理方法

ZTEsoftTabs目前支持的自定义事件暂时只有onTabClick，onTabDelete后续有需要可以再添加。

onTabClick，点击Tab页面时触发的事件，通过setOnTabClick方法可以设定事件触发的方法。

onTabDelete，关闭Tab页面时触发的事件，通过setOnTabDelete方法可以设定事件触发的方法。

例如：

function testTab1()
{
	alert(" first clicked event of tab1 happened. ");
}
function testTab2()
{
	alert(" second clicked event of tab1 happened. ");
}
function testTab3()
{
	alert(" clicked event of tab2 happened. ");
}
function deleteTab1()
{
	return false;
}
tab.tabsArray[0].setOnTabClick(testTab1);
tab.tabsArray[0].setOnTabClick(testTab2);
tab.tabsArray[1].setOnTabClick(testTab3);
tab.tabsArray[1].setOnTabDelete(deleteTab1);

每一个事件可以添加多个事件处理方法，按照添加的先后顺序执行，即队列顺序，先加的先执行。

上面的例子在点击第一个Tab页时先后触发testTab1和testTab2这两个方法，同理点击第二个标签页时触发testTab3方法。


11.动态定义Tab页面的内容

Tab页面的内容既可以直接在页面里写好了，像例子中的AbnormalCDRQry和ErrorCDRQry这两个Tab，也可以动态定义，像新建的new Tab。

例如：

tab.setTabContent(index,"对应的HTML");
tab.setTabContentById(id,"对应的HTML");

通过HTML定义其内容，这里对应的HTML可以是包括Frame在内的任意HTML元素。

12、通过代码设置选中的tab
有两个方法：setTabSelected(index)和setTabSelectedById(id)
13、设置 tab页的显示模式
当tab也比较多的时候，一行显示不下，有两种方式处理剩下的tab
0:自然换行（默认）
1:不换行,在Tab上显示左右箭头，通过左右箭头切换限制的tab页面
两种模式的切换方法：setTabPagesMode
详细见：例子页面

