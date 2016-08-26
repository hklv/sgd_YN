/**
 * constructor
 */
function Format(){
    this.jsjava_class = "jsjava.text.Format";
}

/**  The DateFormat class references to java.text.SimpleDateFormat of J2SE1.4 */

/**
 * constructor
 */
function DateFormat(){
    this.jsjava_class = "jsjava.text.DateFormat";
}

DateFormat.prototype = new Format();
DateFormat.prototype.constructor = DateFormat;

DateFormat.zh_cn_month2 = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
DateFormat.zh_cn_month3 = ["\u4e00\u6708", "\u4e8c\u6708", "\u4e09\u6708", "\u56db\u6708", "\u4e94\u6708", "\u516d\u6708", "\u4e03\u6708", "\u516b\u6708", "\u4e5d\u6708", "\u5341\u6708", "\u5341\u4e00\u6708", "\u5341\u4e8c\u6708"];
DateFormat.zh_cn_month4 = ["\u4e00\u6708", "\u4e8c\u6708", "\u4e09\u6708", "\u56db\u6708", "\u4e94\u6708", "\u516d\u6708", "\u4e03\u6708", "\u516b\u6708", "\u4e5d\u6708", "\u5341\u6708", "\u5341\u4e00\u6708", "\u5341\u4e8c\u6708"];
DateFormat.us_en_month4 = ["Janu", "Febr", "Marc", "Apri", "May", "June", "July", "Augu", "Sept", "Octo", "Nove", "Dece"];
DateFormat.us_en_month3 = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
DateFormat.us_en_month2 = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
DateFormat.zh_cn_week = ["\u661f\u671f\u65e5", "\u661f\u671f\u4e00", "\u661f\u671f\u4e8c", "\u661f\u671f\u4e09", "\u661f\u671f\u56db", "\u661f\u671f\u4e94", "\u661f\u671f\u516d"];
DateFormat.zh_cn_am = "\u4e0b\u5348";
DateFormat.zh_cn_pm = "\u4e0a\u5348";
DateFormat.us_en_week = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
DateFormat.us_en_am = "AM";
DateFormat.us_en_pm = "PM";

/*
 * format a date by specified pattern
 * @param date
 */
DateFormat.prototype.format = function(date){
    var year4 = date.getFullYear();
    var year2 = year4.toString().substring(2);
    var pattern = this.pattern;
    pattern = pattern.replace(/yyyy/, year4);
    pattern = pattern.replace(/yy/, year2);
    var month = date.getMonth();
    pattern = pattern.replace(/MMMM/, eval("DateFormat." + DateFormat.language + "_month4[month]"));
    pattern = pattern.replace(/MMM/, eval("DateFormat." + DateFormat.language + "_month3[month]"));
    pattern = pattern.replace(/MM/, eval("DateFormat." + DateFormat.language + "_month2[month]"));
    var dayOfMonth = date.getDate();
    var dayOfMonth2 = dayOfMonth;
    var dayOfMonthLength = dayOfMonth.toString().length;
    if (dayOfMonthLength == 1) {
        dayOfMonth2 = "0" + dayOfMonth;
    }
    pattern = pattern.replace(/dd/, dayOfMonth2);
    pattern = pattern.replace(/d/, dayOfMonth);
    var hours = date.getHours();
    var hours2 = hours;
    var hoursLength = hours.toString().length;
    if (hoursLength == 1) {
        hours2 = "0" + hours;
    }
    pattern = pattern.replace(/HH/, hours2);
    pattern = pattern.replace(/H/, hours);
    var minutes = date.getMinutes();
    var minutes2 = minutes;
    var minutesLength = minutes.toString().length;
    if (minutesLength == 1) {
        minutes2 = "0" + minutes;
    }
    pattern = pattern.replace(/mm/, minutes2);
    pattern = pattern.replace(/m/, minutes);
    var seconds = date.getSeconds();
    var seconds2 = seconds;
    var secondsLength = seconds.toString().length;
    if (secondsLength == 1) {
        seconds2 = "0" + seconds;
    }
    pattern = pattern.replace(/ss/, seconds2);
    pattern = pattern.replace(/s/, seconds);
    var milliSeconds = date.getMilliseconds();
    pattern = pattern.replace(/S+(?!e)/, milliSeconds);
    var day = date.getDay();
    pattern = pattern.replace(/E+/, eval("DateFormat." + DateFormat.language + "_week[day]"));
    if (hours > 12) {
        pattern = pattern.replace(/a+(?!n)((?!y))(?!r)/, eval("DateFormat." + DateFormat.language + "_am"));
    }
    else {
        pattern = pattern.replace(/a+(?!n)(?!y)(?!r)/, eval("DateFormat." + DateFormat.language + "_pm"));
    }
    var kHours = hours;
    if (kHours == 0) {
        kHours = 24;
    }
    var kHours2 = kHours;
    var kHoursLength = kHours.toString().length;
    if (kHoursLength == 1) {
        kHours2 = "0" + kHours;
    }
    pattern = pattern.replace(/kk/, kHours2);
    pattern = pattern.replace(/k/, kHours);
    var KHours = hours;
    if (hours > 11) {
        KHours = hours - 12;
    }
    var KHours2 = KHours;
    var KHoursLength = KHours.toString().length;
    if (KHoursLength == 1) {
        KHours2 = "0" + KHours;
    }
    pattern = pattern.replace(/KK/, KHours2);
    pattern = pattern.replace(/K/, KHours);
    var hHours = KHours;
    if (hHours == 0) {
        hHours = 12;
    }
    var hHours2 = hHours;
    var hHoursLength = hHours.toString().length;
    if (KHoursLength == 1) {
        hHours2 = "0" + hHours;
    }
    pattern = pattern.replace(/hh/, hHours2);
    pattern = pattern.replace(/h/, hHours);
    return pattern;
};

/**
 * constructor
 */
function SimpleDateFormat(){
    this.jsjava_class = "jsjava.text.SimpleDateFormat";
}

SimpleDateFormat.prototype = new DateFormat();
SimpleDateFormat.prototype.constructor = SimpleDateFormat;

/**
 * apply the pattern
 * @param pattern
 */
SimpleDateFormat.prototype.applyPattern = function(pattern){
    this.pattern = pattern;
};

var dateHelper = {
	/*************************************************************************
	函数名称：formatDateStr
	函数说明：将传入的时间类型变量转换成指定的字符串形式,返回的格式为：YYYY-MM-DD HH:MI:SS
	输入参数：

		date：传入的时间类型变量
	输出参数：

		str：传出的字符串类型变量

	************************************************************************/
	formatDateStr : function(date)
	{
		try
		{
			var str="";
		
			str=date.getFullYear()+"-";
			str+=(((date.getMonth()+1)>=10)? (date.getMonth()+1):"0"+(date.getMonth()+1))+"-";
			str+=(date.getDate()>=10)? date.getDate():"0"+date.getDate();
			str+=" ";
			str+=((date.getHours()>=10)? date.getHours():"0"+date.getHours())+":";
			str+=((date.getMinutes()>=10)? date.getMinutes():"0"+date.getMinutes())+":";
			str+=(date.getSeconds()>=10)? date.getSeconds():"0"+date.getSeconds();
			return str;
		}
		catch(e)
		{
			throw e;
		}
	},
	
	/**************************************************************************
	 函数名称：getDateStrFromDateTimeStr
	 函数功能：从后台返回的标准时间格式(YYYY-MM-DD HH:MI:SS)中，取日期YYYY-MM-DD
	 输入参数：字符串
	 输出参数：无
	 **************************************************************************/
	getDateStrFromDateTimeStr : function(strDateTime)
	{
		var strDate = "";
		if ( (strDateTime != null)&&(strDateTime != "") )
		{
			strDate = strDateTime.substr(0,10);
		}
		return strDate;
	},
	
	/***************************************************
	 函数说明：将str类型的转换成Date类型
	 输入参数：

	 返 回 值：
	 ***************************************************/
	parseStrToDate : function(strdate)
	{
		var strdate1=strdate.trim();
		if(strdate1.length==0)
		{
			return new Date(1970,1,1,0,0,0);
		}
		var arr=strdate1.split('-');
		if(arr.length==1)
		{
			var arrtime=strdate1.split(':');
			return new Date(1970,1,1,arrtime[0],arrtime[1],arrtime[2]);
		}
		else
		{
			var arr2=arr[2].split(':');
			if(arr2.length==1)
			{
				return new Date(arr[0],arr[1]-1,arr[2],0,0,0);
			}
			else
			{
				var arr3=arr[2].split(' ');
				var arrtime=arr3[1].split(':');
				return new Date(arr[0],arr[1]-1,arr3[0],arrtime[0],arrtime[1],arrtime[2]);
			}
		}
	},
	
	toDispalyFormat : function (value,dateFormat)
	{
		if(dateFormat == "yyyy-MM-dd HH:mm:ss"||value==null)
		{
			return value;
		}
		if(value.length!=10&&value.length!=19&&value.length!=8)
		{
			return "";
		}
		
		var yearValue=0;
		var monthValue=0;
		var dayValue=0;
		
		var hourValue = 0;
		var minValue = 0;
		var ssValue = 0;
		
		if(value.length==10)
		{
			dateFormat = dateFormat.replace(/H|k|m|s/g,"");
			dateFormat = dateFormat.replace(/::/g,"").trim();
			yearValue=value.substring(0,4);
			monthValue=value.substring(5,7);
			dayValue=value.substring(8,10);
		}
		if(value.length==8)
		{
			dateFormat = dateFormat.replace(/y|M|W|d|E/g,"");
			dateFormat = dateFormat.replace(/--|\/\//g,"").trim();
			hourValue = value.substring(0,2);	  
			minValue = value.substring(3,5);
			ssValue = value.substring(6,8);
		}
		
		var df=new SimpleDateFormat();	
	    df.applyPattern(dateFormat);
	    	
		if(value.length==19)
		{
			yearValue=value.substring(0,4);
			monthValue=value.substring(5,7);
			dayValue=value.substring(8,10);
			hourValue = value.substring(11,13);	  
			minValue = value.substring(14,16);
			ssValue = value.substring(17,19);
		}
		var date  = new Date(yearValue,monthValue-1,dayValue,hourValue,minValue,ssValue);
	
	    var str=df.format(date);
	
		return str;
	},
	
	
	/************************************************************
	 函数名称：getDateStrFromDateTimeStr
	 函数功能：从后台返回的标准时间格式(YYYY-MM-DD HH:MI:SS)中，取日期YYYY-MM-DD
	 输入参数：字符串
	 输出参数：无 
	 返 回 值：字符串 
	 函数说明： 
	 ************************************************************/
	getDateStrFromDateTimeStr:function(strDateTime)
	{	
		var strDate = "";
		if ( (strDateTime != null)&&(strDateTime != "") )
		{
			strDate = strDateTime.substr(0,10);
		}
		return strDate;
	},


	/************************************************************
	 函数名称：increaseDayToDate
	 函数功能：增加日期的偏移量

	 输入参数：dateStr-日期字符串，offset-增加的偏移量（可选）
	 输出参数：无 
	 返 回 值：处理后的日期
	 函数说明：增加日期的天数
	 ************************************************************/ 
	increaseDayToDate:function(dateStr, offset)
	{
		if(dateStr==null || dateStr.trim()=="")
		{
			return dateStr;
		}
		
		//获取偏移量：优先使用方法调用指定的偏移量,其次使用系统配置的偏移量,若两者都没有则默认为1
		if(offset==null)
		{
			offset = this.qrySysExpiryDateOffset("custContact.expiryDateOffset.input");
		}
		
		//把偏移量转换成毫秒数
		var times = this.zoomOffset(dateStr, offset);
		
		var dateObj = this.parseStrToDate(dateStr);
		dateObj.setTime(dateObj.getTime() + times);
		
		if(dateStr.trim().length == 10)
		{
			return this.getDateStrFromDateTimeStr(this.formatDateStr(dateObj));
		}
		else
		{
			return this.formatDateStr(dateObj);
		}
	},


	/************************************************************
	 函数名称：decreaseDayToDate
	 函数功能：减少日期的偏移量

	 输入参数：dateStr-日期字符串，offset-减少的偏移量（可选）
	 输出参数：无 
	 返 回 值：处理后的日期
	 函数说明：减少日期的天数
	 ************************************************************/ 
	decreaseDayToDate:function (dateStr, offset)
	{
		if(dateStr==null || dateStr.trim()=="")
		{
			return dateStr;
		}
		
		//获取偏移量：优先使用方法调用指定的偏移量,其次使用系统配置的偏移量,若两者都没有则默认为1
		if(offset==null)
		{
			offset = this.qrySysExpiryDateOffset("custContact.expiryDateOffset.output");
		}
		
		//把偏移量转换成毫秒数
		var times = this.zoomOffset(dateStr, offset);
		
		var dateObj = this.parseStrToDate(dateStr);
		dateObj.setTime(dateObj.getTime() - times);
		
		if(dateStr.trim().length == 10)
		{
			return this.getDateStrFromDateTimeStr(this.formatDateStr(dateObj));
		}
		else
		{
			return this.formatDateStr(dateObj);
		}
	},
	
	/**********************************************************************************
	查询系统配置的偏移量
	***********************************************************************************/
	qrySysExpiryDateOffset:function (systemPath)
	{
		try
		{
			var offset = null;
			
			var param = new Object();
			param.SYSTEM_PATH = systemPath;
			param.DEFAULT_VALUE = "1";
			var sysObj = callRemoteFunction("QrySystemConfig",param);
			if(sysObj!=null && sysObj.SYSTEM_VALUE!=null)
			{
				offset = parseInt(sysObj.SYSTEM_VALUE, 10);
			}
			
			if(offset==null || isNaN(offset))
			{
				offset = 1;
			}
			
			return offset;
		}
		catch(e)
		{
			//默认不处理

			return 1;
		}
	},
	
	/**********************************************************************************
	把偏移量转换成毫秒数
	偏移量的单位取决于日期时间

	若日期时间精确到日期（天），则偏移量单位是天；若日期时间精确到时间（秒），则偏移量单位是秒

	***********************************************************************************/
	zoomOffset:function (dateStr, offset)
	{
		if(dateStr.trim().length == 10)//精确到日期

		{
			return offset * 24 * 60 * 60 * 1000;
		}
		else//精确到时间

		{
			return offset * 1000;
		}
	}
}

function qrySysDate()
{
    var date = callRemoteQueryFunction("QrySysdate",null,null);
    if (date!=null) 
    {
        return date[0].SYS_DATE;
    }
	
	return null;
}
