var ZTEsoftWindow = {
	openWindow : function(url,height,width)
	{
	    var itop = (window.screen.height -height)/2-40;
	    var ileft =  (window.screen.width-width)/2;
		var param = 'height='+height+', width='+width+', toolbar=no, menubar=no, top='+itop+',left='+ileft+',scrollbars=no ,scrollbars=no,status=yes';
		window.open(url,'',param);
	},
	
	showModal : function(url,dialogArgument,width,height)
	{
		var status = "dialogWidth:"+width+"px;dialogHeight:"+height+"px;status:no;help:no;resizable:no;scroll:no";
		return window.showModalDialog(url,dialogArgument,status);
	},
	
	showDlg : function(aURL,vArguments,sFeatures)
	{
		window.showModalDialog(aURL,vArguments,sFeatures);
	}	
}