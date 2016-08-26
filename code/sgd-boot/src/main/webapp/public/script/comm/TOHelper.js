/**
 * flag为1表示session超时
 * flag为2表示同一账号被别人再次登陆导致自己被登出
 */
function Timeout(flag) 
{
	//模态窗口

	if(!window.opener && !window.modalHelp && window == window.parent){
		if(flag == 1)
		{
			returnValue = "sessiontimeout";
		}
		else if(flag == 2)
		{
			returnValue = "forcedout";
		}
		window.close();
	}
	else
	{
		if(window.parent != null && window!=window.parent && window.parent.Timeout)
		{
			window.parent.Timeout(flag);
		}
		else
		{
			if(flag == 1)	//session超时
			{
				window.top.location = g_GlobalInfo.WebRoot + "Login.jsp?timeout=1";
			}
			else if(flag == 2)	//被挤出

			{
				window.top.location = g_GlobalInfo.WebRoot + "Login.jsp?timeout=2";
			}
		}
		
	}
	
}