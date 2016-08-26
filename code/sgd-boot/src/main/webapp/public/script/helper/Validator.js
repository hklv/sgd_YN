var Validator = {
	/************************************************************
	 函数说明：校验IP地址，是否合法

	 函数名称：checkIpAddress
	 输入参数：IP地址串

	 输出参数：无
	 返 回 值：布尔值

	 ************************************************************/
	checkIpAddress : function(strIpAddress)
	{
		var scount=0; 
		var ip = strIpAddress; 
		var iplength = ip.length; 
		var Letters = "1234567890."; 
		for (i=0; i < ip.length; i++) 
  		{ 
   			var CheckChar = ip.charAt(i); 
   			if (Letters.indexOf(CheckChar) == -1) 
   			{ 
	    		return false; 
   			} 
  		} 

		for (var i = 0;i<iplength;i++) 
  			(ip.substr(i,1)==".")?scount++:scount; 
		if(scount!=3) 
		{ 
	 		return false; 
		} 

		var first = ip.indexOf("."); 
		var last = ip.lastIndexOf("."); 
		var str1 = ip.substring(0,first); 
		var subip = ip.substring(0,last); 
		var sublength = subip.length; 
		var second = subip.lastIndexOf("."); 
		var str2 = subip.substring(first+1,second); 
		var str3 = subip.substring(second+1,sublength); 
		var str4 = ip.substring(last+1,iplength); 

		if (str1=="" || str2=="" ||str3== "" ||str4 == "") 
		{
			return false; 
		} 
		
		if (str1< 0 || str1 >255) 
		{
		    return false; 
		} 
		else if (str2< 0 || str2 >255) 
		{
			return false; 
		} 
		else if (str3< 0 || str3 >255) 
		{
			return false; 
		} 
		else if (str4< 0 || str4 >255) 
		{
			return false; 
		}
		return true;
	},
	
	
	/*************************************************************************
	函数说明：校验转入的数值是否为【正整数】

	函数要求：无
	输入参数：

		val ：需要验证值

	输出参数：

		true：是正整数

		false：不是

	************************************************************************/
	checkPosInt : function(val)
	{
	    return RegExpHelper.isMatch(val,RegExpHelper.POS_INTEGER);
	},
	
	/*************************************************************************
	函数说明：校验转入的数值是否为【整数】

	函数要求：无
	输入参数：

		val ：需要验证值

	输出参数：

		true：是整数
		false：不是

	***********************************************************************/
	checkInteger : function(val)
	{
		return RegExpHelper.isMatch(val,RegExpHelper.INTEGER);
	},
	
	/************************************************************
	 函数名称：checkFloat
	 函数功能：校验输入值是否为浮点数

	 输入参数：无
	 输出参数：无
	 ************************************************************/
	checkFloat : function(str)
	{
		str = str.toString();
		
		return RegExpHelper.isMatch(str,RegExpHelper.INTEGER)||RegExpHelper.isMatch(str,RegExpHelper.FLOAT);
	},
	
	/************************************************************
	 函数名称：checkNumberAndChar
	 函数功能：校验只允许输入字母和数字

	 输入参数：无
	 输出参数：无
	 返 回 值：无

	 函数说明：

	 ************************************************************/
	checkNumberAndChar : function(str)
	{
		return RegExpHelper.isMatch(str,RegExpHelper.NUMBER_AND_CHAR);
	},
	
	/************************************************************
	 函数说明：校验Email是否合法
	 函数名称：chkemail
	 输入参数：eamil字符串

	 输出参数：无
	 返 回 值：布尔值

	 ************************************************************/
	chkemail : function chkemail(a)
	{
		var i=a.length;
		if(i==0) return true;
	
		var temp = a.indexOf('@');
		var tempd = a.indexOf('.',temp);
	
		if (temp > 0) {
			if ((i-temp) > 3){
				if(tempd>0){
					if (((tempd-temp)>1) && ((i-tempd)>1))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	},
	/************************************************************
	 函数说明：校验是否包含空格

	 函数名称：chkspc
	 输入参数：字符串
	 输出参数：无
	 返 回 值：布尔值

	 ************************************************************/
	chkspc : function chkspc(a)
	{
		var i=a.length;
		var j = 0;
		var k = 0;
		while (k<i)
		{
			if (a.charAt(k) != " ")
				j = j+1;
			k = k+1;
		}
		if (j==0)
		{
			return false;
		}
	
		if (i!=j)
		{ return 2; }
		else
		{
			return true;
		}
	},
	
	
	/*************************************************************************
	函数说明：判断输入对象是否为空

	函数要求：无
	输入参数：obj :  判断对象
	输出参数：true :　为空   false : 不为空		
	************************************************************************/
	checkTextIsEmpty : function checkTextIsEmpty(obj)
	{
		var val = obj.value;
		if(val.replace(/(^\s*)|(\s*$)/g,"") == "")
			return false;
		else
		    return true;
	},
	
	/*************************************************************************
	函数说明：判断输入是否为数字

	函数要求：无
	输入参数：str :  输入
	输出参数：true :　数字   false : 非数字

	************************************************************************/
	checkNumber : function (str)
	{
		return RegExpHelper.isMatch(str,RegExpHelper.NUMBER);
	},
	
	/*************************************************************************
	函数说明：判断字符输入是否超长

	函数要求：无
	输入参数：inputObj :  输入
	输入参数：len :  最大长度
	输入参数：alarmMessage :  提示信息
	输出参数：true :　超长   false : 不超长

	************************************************************************/
	checkMaxLength : function (inputObj, len, alarmMessage)
	{
		var obj = $("#"+inputObj);
		
		if(obj){
			
			var data = obj.val();
			
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
				
				showMessage(alarmMessage,function(){
					obj.focus();
				});
				
				rtn = false;
			}
			
			return rtn;
		}

	},
	
	/*************************************************************************
	函数说明：判断数字输入是否超过最大值

	函数要求：无
	输入参数：inputObj :  输入
	输入参数：num :  最大长度
	输入参数：alarmMessage :  提示信息
	输出参数：true :　超长   false : 不超长

	************************************************************************/
	checkMaxValue : function (inputObj, num, alarmMessage)
	{
		var obj = $("#"+inputObj);
		
		if(obj){
			
			var data = obj.val();
			
			if(Validator.checkInteger(data)){
				
				var dataNumber = parseInt(data);
				
				if(dataNumber > parseInt(num)){
					
					showMessage(alarmMessage,function(){
						obj.focus();
					});
					
					return false;
				}
				
				return true;
				
			}else{
				
				return true;
			}
			
		}
		
		return true;

	}
}