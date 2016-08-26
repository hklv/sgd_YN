package com.ztesoft.uboss.form.sz600.tool;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SzConvert {
  public SzConvert() {
  }

  //复制数组的值
  public static boolean copyArrayValue(String source_str[], String target_str[]) {
    if (source_str == null || target_str == null) {
      return false;
    }
    int iLen = source_str.length;
    if (iLen != target_str.length) {
      return false;
    }
    for (int i = 0; i<iLen; i++) {
      target_str[i] = source_str[i];
    }
    return true;
  }

  //将数组转换为字符串，用逗号分割
  public static String arrayToStr(String a_str[]) {
    String str = "";
    str = SzConvert.arrayToStr(a_str, ",");
    return str;
  }

  //将数组转换为字符串，用标签分割。如果标签sTag为空，则默认用,隔开
  public static String arrayToStr(String a_str[], String sTag) {
    if (a_str == null) {
      return "";
    }
    if (sTag == null || sTag.equals("")) {
      sTag = ",";
    }
    int iLen = a_str.length;
    String str = "";
    for (int i=0; i<iLen; i++) {
      str += sTag + a_str[i];
    }
    if (str.length() > 1) {
      str = str.substring(1);
    }
    return str;
  }
  
  public static String arrayToStrExcept(String a_str[], String sTag, int iIndexExcept) {
    if (a_str == null) {
      return "";
    }
    if (sTag == null || sTag.equals("")) {
      sTag = ",";
    }
    int iLen = a_str.length;
    String str = "";
    for (int i=0; i<iLen; i++) {
    	if (i == iIndexExcept) {
    		continue;
    	}
      str += sTag + a_str[i];
    }
    if (str.length() > 1) {
      str = str.substring(1);
    }
    return str;
  }  

  //将数组转换为字符串，用标签分割。如果标签sTag为空，则默认用,隔开
  public static String arrayToWriteStr(String a_str[], String sTag) {

    if (a_str == null) {
      return "";
    }
    if (sTag == null || sTag.equals("")) {
      sTag = ",";
    }
    int iLen = a_str.length;
    String str = "";
    for (int i=0; i<iLen; i++) {
      str += sTag + SzConvert.strWriteData(a_str[i]);
    }

    if (str.length() > 1) {
      str = str.substring(1);
    }

    return str;
  }

  public static String arrayUploadToWriteStr(String a_str[], String sTag) {

    if (a_str == null) {
      return "";
    }
    if (sTag == null || sTag.equals("")) {
      sTag = ",";
    }
    int iLen = a_str.length;
    String str = "";
    for (int i=0; i<iLen; i++) {
      //str += sTag + SzConvert.strWriteData(a_str[i]);
    	str += sTag + SzConvert.uploadWriteData(a_str[i]);
    }

    if (str.length() > 1) {
      str = str.substring(1);
    }

    return str;
  }  

  /**
   * 去掉一个以某种字符分割的字符串中重复的数据
   * @ param tokenStr  待去掉重复数据的字符串.
   * @ param sepChar   数据的分割符号.
   * */
  public static Collection removeRepeatTokenColl(String tokenStr, String sepChar){
    if(tokenStr == null || sepChar == null)
      return null;
    String str = null;
    HashMap tokenMap = new HashMap();
    StringTokenizer st = null;
    st = new StringTokenizer(tokenStr,sepChar);
    while(st.hasMoreTokens()){
      String key = st.nextToken();
      tokenMap.put(key,key);
    }
    Collection tokenItems = tokenMap.values();

    tokenMap = null;
    return tokenItems;
  }

  /**
   * 去掉一个以某种字符分割的字符串中重复的数据
   * @ param tokenStr  待去掉重复数据的字符串.
   * @ param sepChar   数据的分割符号.
   * */
  public static String removeRepeatTokenStr(String tokenStr, String sepChar){
    Collection coll = SzConvert.removeRepeatTokenColl(tokenStr, sepChar);
    Iterator iterator = coll.iterator();
    StringBuffer sb = new StringBuffer("");
    boolean flag = false;
    while(iterator.hasNext()){
      if(!flag) {
        sb.append((String)iterator.next());
      } else {    
        sb.append(sepChar);
        sb.append((String)iterator.next());
      }
      flag = true;
    }
    String str = sb.toString();
    sb = null;
    return str;
  }


  //将str转成GBK,将字符串写进数据库前调用
  public static String strWriteData(String s) {
    if (s == null) {
      return "";
    }
    try {
      s = new String(s.getBytes("ISO-8859-1"), "UTF-8");
      s = SzConvert.toSafeString(s);
    }catch(Exception e) {
    }
    return s;
  }
  
  public static String strWriteDataHTML(String s) {
    if (s == null) {
      return "";
    }
    try {
      s = new String(s.getBytes("ISO-8859-1"), "UTF-8");
      //s = SzConvert.toSafeString(s);
    }catch(Exception e) {
    }
    return s;
  }  

  //当使用组件smartupload.getRequest()时，用这个转码
  public static String uploadWriteData(String s) {
    if (s == null) {
      return "";
    }
    s = SzConvert.toSafeString(s);
    return s;
  }

  //GBK转成str，从数据库读取数据库后调用
  public static String strReadData(String s) {
    if (s == null) {
      s = "";
    }
    //转为html
    s = SzConvert.toHTMLString(s);

    return s;
  }
  
  //专门用来显示html
  public static String strReadDataHTML(String s) {
    if (s == null) {
      s = "";
    }
    //转为html
    s = SzConvert.toSimpleHtml(s);
    return s;
  }  

  public static int toInt(String s) {
    if (s == null || s.equals("")) {
      s = "0";
    }
    int i = 0;
    try {
    	i = Integer.parseInt(s);
    }catch(Exception e) {    	
    }
    
    return i;
  }
  
  public static double toDouble(String s) {
    if (s == null || s.equals("")) {
      s = "0";
    }
    double i = 0;
    try {
    	i = Double.parseDouble(s);
    }catch(Exception e) {    	
    }
    
    return i;
  }
  
  public static String toZeroStr(Object s) {
    if (s == null ) {
      s = "0";
    }

    return s.toString();
  }  

  public static String toZeroStr(String s) {
    if (s == null || s.equals("")) {
      s = "0";
    }

    return s;
  }

  /**
   * 作用是null的字符串换成""以免计算的时候出错。
   * @param s
   * @return
   */
  public static String toSpaceStr(String s) {
    if (s == null) {
      s = "";
    }
    return s;
  }

  public static String toSpaceStr(Object s) {
    if (s == null) {
      s = "";
    }
    return s.toString();
  }


  /**
   * 获取足够长的编号，长度小于I_NUMBER_LEN，前面补0，长度大于I_NUMBER_LEN的不处理
   * @return
   */
  public static String getEnoughLenNumber(String sBH, int iLenNumber) {
    if (sBH == null) {
      return "";
    }
    int iLen = sBH.length();
    for (int i=0; i<iLenNumber - iLen; i++) {
      sBH = "0" + sBH;
    }
    return sBH;
  }

  /**
   * 传入一个字符串，当长度不够的时候，在后面用&nbsp;补充。
   * @param s
   * @param iLenNumber
   * @return
   */
  public static String getEnoughSpace(String s, int iLenNumber) {
    int iLen = s.length();
    for (int i=0; i<iLenNumber - iLen; i++) {
      s = s + "&nbsp;&nbsp;";
    }
    return s;
  }
  
  public static String getEnoughSpace2(String s, int iLenNumber) {
    int iLen = s.length();
    for (int i=0; i<iLenNumber - iLen; i++) {
      s = s + " ";
    }
    return s;
  }  

  public static String toSafeString(String s) {
  	/*
    //s = s.replace(',','，');
    s = s.replace('\'','‘');
    //如果第一个字符是",就转换为“
    if (s.length()>0 && s.substring(0,1).equals("\"")) {
        s = "“" + s.substring(1,s.length());
    }
    s = s.replaceAll("&", "&amp;");
    s = s.replaceAll("<", "&lt;");
    s = s.replaceAll(">", "&gt;");
		*/
  	//2012-01-09
  	s = s.replaceAll("\\\\", "\\\\\\\\");
  	s = s.replaceAll("\'", "\\\\\'");
  	s = s.replaceAll("\"", "\\\\\""); 	
    return s;
  }

  public static String convertFormFunc(String s) {
  	s = s.replaceAll("‘", "'");
  	return s;
  }
  
  public static String toHTMLString(String s) {
	  
  	s = s.replaceAll("\'", "\\\'");
		s = s.replaceAll("&", "&amp;");
  	s = s.replaceAll("\"", "&quot;");		
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");	
		
		//s = s.replaceAll("\"", "\\\"");
  	//s = s.replaceAll("\"", "\\\\\"");
  	/*
		 * s = s.replaceAll("&amp;", "&"); s = s.replaceAll("&lt;", "<"); s =
		 * s.replaceAll("&gt;", ">"); s = s.replaceAll("‘", "'");
		 */
    return s;
  }
  
  public static String toSimpleHtml(String s) {
		s = s.replaceAll("\'", "\\\'");
		s = s.replaceAll("\"", "\\\""); 
		/*
    s = s.replaceAll("\n", "<br>");
    s = s.replaceAll(" ", "&nbsp;");
    */ 
    return s;
  }


  /**
   * 字母、数字等为1个长度，中文为2个长度
   * @param s
   * @return
   */
  public static int getLength(String s) {
    if (s == null) {
        return 0;
    }
    String temp = s;
    int j = 0;  //其他字符
    int k = 0;  //汉字
    int i = 0;
    for (i=0; i<s.length(); i++) {
        if (temp.charAt(i) >=33 && temp.charAt(i)<=128) {
            j++;
        }else {
            k++;
        }
    }
    return 2 * k + j;
  }
  
  //cd中文日期，cy中文年，cm中文月，ct中文时间, 
  public static String format(String s, String sFormat) {
  	int iLen = 0;
  	if (sFormat == null || sFormat.equals("0") || sFormat.equals("")) {  	
  		iLen = 0;
  	} else if (sFormat.equals("cd")) {
  		s = SzDateHelp.strToChina(s);
  		iLen = 11;
  	} else if (sFormat.equals("cy")) {
  		s = SzDateHelp.strToChina(s);
  		iLen = 5;
  	} else if (sFormat.equals("cm")) {
  		s = SzDateHelp.strToChina(s);
  		iLen = 8;   		
  	} else if (sFormat.equals("ct")) {
  		s = SzDateHelp.strToChina(s);
  		iLen = 0;   		
  	} else {
  		//截取长度
  		iLen = SzConvert.toInt(sFormat);
  		if (iLen > 0) {
  			s = SzConvert.truncTitle(s, iLen);
  		}
  		iLen = 0;   		
  	}
  	if (iLen > 0) {
  		s = SzConvert.truncate(s, iLen);
  	}
  	
  	return s;
  }

  /**
   * 截取字符串，为了显示方便，中文算一个字，其他算半个字。
   * @param str
   * @param count
   * @return
   */
  public static String trunc(String s, int count) {
      if (s == null) {
          return "";
      }
      String temp = s;
      int j = 0;  //其他字符
      int k = 0;  //汉字
      int i = 0;
      for (i=0; i<s.length(); i++) {
          if (temp.charAt(i) >=33 && temp.charAt(i)<=128) {
              j++;
          }else {
              k++;
          }
          if (k + j/2 +j%2 > count) {
              break;
          }
      }
      return temp.substring(0,i);
  }

  /**
   * 截取字符串,作为标题显示用.中文算一个,其他算半个.当有截取的时候,后面增加"..."
   * @param s
   * @param count
   * @return
   */
  public static String truncTitle(String s, int count) {
  	String s2 = SzConvert.trunc(s, count);
  	if (s.length() > count) {
  		s2 = s2 + "...";
  	}
  	return s2;
  }
  
  /**
   * 日期的格式必须是:2009-08-10 09:52
   * @param strDate
   * @return
   */
  public static String truncDate(String strDate) {
  	if (strDate == null || strDate.length() < 10) {
  		return "";
  	}
  	strDate = strDate.substring(5,16);
  	strDate = strDate.replaceFirst("-", "月");
  	strDate = strDate.replaceFirst(" ", "号");
  	return strDate;  	
  }
  
  public static String truncMonth(String strDate) {
  	if (strDate == null || strDate.length() < 10) {
  		return "";
  	}
  	strDate = strDate.substring(0,7);
  	strDate = strDate.replaceFirst("-", "年") + "月";
  	
  	return strDate;  	
  }
  
  /**
   * 截取字符串
   * @param str
   * @param count
   * @return
   */
  public static String truncate(String s, int count) {
      if (s == null) {
          return "";
      }
      if (count < s.length()) {
      	s = s.substring(0, count);
      }
      return s;
  }  
  /**
   * 当一个字符串放到javascript变量的时候，如果遇到/n就会出错，把/n转成
   * @param s
   * @return
   */
  public static String toJsStr(String s) {
  	if (s == null) {
  		s = "";
  	}
  	/*
  	s = s.replaceAll("\"", "\\\\\"");
    s = s.replaceAll("\n", "<br>");
    s = s.replaceAll("\r", "");  
    */
		s = s.replaceAll("\\\\", "\\\\\\\\");
		
		s = s.replaceAll("\'", "\\\\\'");
		s = s.replaceAll("\"", "\\\\\""); 	
	    s = s.replaceAll("\n", "<br>");
	    s = s.replaceAll("\r", "");	    
    return s;
  }
  
  public static float formatFloat3(float f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000");  
		String s = df.format(f);
		f = new Float(s);
		return f;
  }
  
  public static float formatFloat2(float f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  
		String s = df.format(f);
		f = new Float(s);
		return f;
  }  
  
  public static String formatFloat3Str(float f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.000");  
		String s = df.format(f);
		return s;
  }
  
  public static String formatFloat2Str(float f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  
		String s = df.format(f);
		return s;
  }    
  
  public static String formatFloat2Str(double f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  
		String s = df.format(f);
		return s;
  }  
  
  public static String formatFloat1Str(double f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0");  
		String s = df.format(f);
		return s;
  }   

  public static String formatFloatStr(double f) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#");  
		String s = df.format(f);
		return s;
  }    
  
  public static double dayToHour(double f) {
  	int DayHours = 7;
  	return f * DayHours;
  }
  
  public static String hToDay(double f) {
  	String s = "";
  	int DayHours = 7;
  	int iDays = (int)f/7;
  	int iHours = (int)f%7;
  	StringBuffer sb = new StringBuffer("");
  	if (iDays > 0) {
  		sb.append(iDays);
  		sb.append("天");
  	}
  	if (iHours > 0) {
  		sb.append(iHours);
  		sb.append("小时");  		
  	}
  	if (f == 0) {
  		sb.append("0天");
  	}
  	s = sb.toString();
  	
  	return s;
  }
  
  public static double strDayToH(String str) {
  	double h = 0;
  	//是否有天
  	int iIndex1 = str.indexOf("天");
  	int iIndex2 = str.indexOf("小时");
  	int d = 0;
  	if (iIndex1>0) {
  		d = SzConvert.toInt(str.substring(0, iIndex1));
  		iIndex1 = iIndex1 + 1;
  	}
  	if (iIndex2 > iIndex1) {
  		h = SzConvert.toInt(str.substring(iIndex1, iIndex2));
  	}
  	//是否有小时
  	h = SzConvert.dayToHour(d) + h;
  	return h;
  }
  
  public static void initialArray(String s[]) {
  	SzConvert.initialArray(s, "");
  }
 
  public static void initialArray(String s[], String sValue) {
  	int iLen = 0;
  	if (s != null) {
  		iLen = s.length;
  	}
  	
  	for (int i=0; i<iLen; i++) {
  		s[i] = sValue;
  	}  	
  }
  
  
  //============XML 2008-10-16增加，
	public static String getText(Attribute a) {
		if (a != null) {
			return a.getText();
		} else {
			return "";
		}
	}	
	
	public static String getText(Element ele, String name) {
		if (ele == null) {
			return "";
		}
		Attribute a = ele.attribute(name);
		return SzConvert.getText(a);
	}
	
  public static void setText(Element ele, String name, String value) {
    //如果attribute不存在，就创建新的
    Attribute attribute = ele.attribute(name);
    if (attribute == null) {
      ele.addAttribute(name, value);
    } else {
      attribute.setValue(value);
    }
  }  
  
  public static void updateXML(String sPath, Document document) {
    try {
      OutputFormat format = OutputFormat.createPrettyPrint();
      format.setEncoding("GB2312");
      XMLWriter xMLWriter = new XMLWriter(format);
      Writer writer = new BufferedWriter(new FileWriter(sPath));
      xMLWriter.setWriter(writer);
      xMLWriter.write(document);
      xMLWriter.flush();
      xMLWriter.close();
    } catch(Exception e) {
      System.out.println("更新xml文件异常！");
    }
  }	     

  public static String encryptStr(String temp) {
		String result = "";
		byte[] result_byte = null;
		byte[] data = null;
		data = temp.getBytes();
		result = new String(org.apache.commons.codec.binary.Base64.encodeBase64(data));
//		result = new sun.misc.BASE64Encoder().encode(data);
		
		return result;
	}

	public static String decryptStr(String temp) {
		String result = "";
		try {

			result = temp;
		} catch (Exception e) {
			return result;
		}
		return result;
	} 
	
	//判断是否为可显示的图片
	public static boolean isPicture(String sFileName) {
		boolean flag = false;
		//将sFileName转化为小写
		sFileName = sFileName.toLowerCase();
		if (sFileName.endsWith(".gif") 
				|| sFileName.endsWith(".jpg") 
				|| sFileName.endsWith(".bmp")) {
			flag = true;
		}
		return flag;
	}
  
	/**
   * 判断是否是中文字符
   * @param s
   * @return
   */
  public static boolean isCnChar(String s) {
    if (s == null) {
      return true;
    }
    int iIndex = s.indexOf("??");
    //System.out.println(iIndex);
    if (iIndex >=0) {
    	//存在??，不是中文
    	return false;
    }
    return true;
  }	
	
	//将随机数进行转换，应用于：登陆助手的验证。返回0的话就是错误
	public static long checkserver(String r) {
		if (r == null || r.equals("")) {
			return -1;
		}
		long myResult = Long.parseLong(r);
		if (myResult > 99999999 || myResult < 10000000) { 
			return -1;
		}
	  //myResult = gRanKey Xor 74062
		myResult = myResult ^ 74062;	
		//System.out.println("^==" + myResult);
    myResult = myResult + 36519;
    //System.out.println("+==" + myResult);
    myResult = (long)myResult / 3;
    //System.out.println("/==" + myResult);
    myResult = myResult - 11821;
    //System.out.println("-==" + myResult);
    myResult = myResult * 7;
    //System.out.println("*==" + myResult);
    myResult = myResult + 30246;
    //System.out.println("+==" + myResult);
    return myResult;
	}
	

  public static String getJspName(String sServletPath) {
  	int iLen = 0;
  	int iIndex = sServletPath.lastIndexOf("/");
  	String s = "";
  	if (iIndex >=0) {
  		s = sServletPath.substring(iIndex + 1);
  	}
  	return s;
  }	
	
  /**
   * 开头和结尾有'包围的话,就去掉
   * @param s
   * @return
   */
  public static String cutComma(String s) {
  	if (s.startsWith("'") && s.endsWith("'") && s.length()>=2) {
  		s = s.substring(1, s.length() - 1);
  	}
  	return s;
  }
  
  public static boolean hasComma(String s) {
  	if (s.startsWith("'") && s.endsWith("'") && s.length()>=2) {
  		return true;
  	}  	
  	return false;
  }
  
  public static boolean contains(String sLong, String sShort) {
  	return SzConvert.contains(sLong, sShort, ",");
  }
  
  public static boolean contains(String sLong, String sShort, String tag) {
  	sLong = sLong.replaceAll(";", tag);
  	sLong = sLong.replaceAll(",", tag);
  	
  	sShort = sShort.replaceAll(";", tag);
  	sShort = sShort.replaceAll(",", tag);
  	
  	sLong = tag + sLong + tag;	
  	sShort = tag + sShort + tag;
  	
  	boolean flag = false;
  	if (sLong.indexOf(sShort) >=0 ) {
  		flag = true;
  	} 
  	return flag;
  }
	
  


	public static String getClob(java.sql.Clob clob) {
		try {
			java.io.InputStream input = clob.getAsciiStream();
			int len = (int) clob.length();
			byte[] by = new byte[len];
			int ii;//= input.read(by,0,len);
			while (-1 != (ii = input.read(by, 0, by.length))) {
				input.read(by, 0, ii);
			}
			return new String(by);
		} catch (Exception e) {
			System.out.println("getClob" + e);
			return "";
		}
	}

	
    
}
