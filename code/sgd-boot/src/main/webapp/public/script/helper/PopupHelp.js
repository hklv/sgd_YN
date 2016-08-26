var winPopup = window.createPopup();
var winstr="<table style=\" border: 1 solid  #000000\" border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\"  background=\"\" >";
	winstr+="<tr><td align=\"center\"><table width=\"100%\" height=\"100%\" border=\"0\" bgColor=\"#FFFFE1\"cellpadding=\"0\" cellspacing=\"0\">";
	winstr+="<tr><td nowrap id=\"tdMsg\" valign=\"center\" style=\"font-size:12px; color: black; face: Tahoma\"></td></tr></table></td></tr></table>";
winPopup.document.body.innerHTML = winstr;
var eventElement = null;
var xPos = 0;
var yPos = 0;
var offsetYPos=0;
var timer = null;


/*************************************************************************
 Function Name:showPopupHelp
 Description:Popup a window that shows the content of text or select
 Input Parameter:None
 Output :None
 Return:None
 ***********************************************************************/
function showPopupHelp(helpMsg)
{
	initPopupWindow(helpMsg);
	eventElement = event.srcElement;
	xPos= window.event.screenX ;
	yPos= window.event.screenY + window.event.offsetY + 12;
	timer = setTimeout("showPopupWindow()",1000);
}
/*************************************************************************
 Function Name:hidePopupHelp
 Description:Invisible the popup window
 Input Parameter:None
 Output :None
 Return:None
 ***********************************************************************/
function hidePopupHelp()
{
	clearTimeout(timer);
	winPopup.hide();
}
/*************************************************************************
 Function Name:initPopupWindow
 Description:Initilize the popup window,draw the frame
 Input Parameter:String to show
 Output :None
 Return:None
 ***********************************************************************/
function initPopupWindow(msgstr)
{
	winPopup.document.all('tdMsg').innerText = msgstr;
}
/*************************************************************************
 Function Name:showPopupWindow
 Description:Visible the popup window
 Input Parameter:None
 Output :None
 Return:None
 ***********************************************************************/
function showPopupWindow()
{
	winPopup.document.all("tdMsg").noWrap = true;
	winPopup.show(0,0,10,10);
	var h = winPopup.document.body.scrollHeight;
	var w = winPopup.document.body.scrollWidth;
	winPopup.hide();
	if(w>300)
	{
		w= 300;
		winPopup.document.all("tdMsg").noWrap = false;
		winPopup.show(0,0,w,10);
		h = winPopup.document.body.scrollHeight;
		winPopup.hide();
	}
	winPopup.show(xPos,yPos,w,h);
	delete eventElement;
}
/*************************************************************************
 Function Name:changeMousePosition
 Description:Change the up-to-show window position
 Input Parameter:None
 Output :None
 Return:None
 ***********************************************************************/
function changeMousePosition()
{
	xPos = window.event.screenX;
	yPos = window.event.screenY + window.event.offsetY + 12;
}
