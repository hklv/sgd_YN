package com.ztesoft.uboss.form.sz600.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SzDateHelp {

  /**
   * 整个系统获取时间都要从这个类出发，防止以后修改时间格式！！！
   * @return
   */
  public static Date getDate() {
    return new Date();
  }

  public static String getTimeLong() {
    Date date = SzDateHelp.getDate();    
    return Long.toString(date.getTime());
  }

  public static String getTimeStr() {
    Date date = SzDateHelp.getDate();

    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String str = "";
    try {
        str = sdFormat.format(date);
    }
    catch(Exception e) {
        return "";
    }
    if (str.equals("1900-01-01 00:00")) {
        str = "";
    }
    return str;
  }

  public static String getDateStr() {
    String sFormat = "yyyy-MM-dd";
    return SzDateHelp.getDateStr(sFormat);
  }
  
  public static String getDateStr(String sFormat) {
    Date date = SzDateHelp.getDate();

    SimpleDateFormat sdFormat = new SimpleDateFormat(sFormat);
    String str = "";
    try {
        str = sdFormat.format(date);
    }
    catch(Exception e) {
        return "";
    }
    if (str.equals("1900-01-01")) {
        str = "";
    }
    return str;  	
  }
  
  public static Date stringToDate(String str) {
      String strFormat = "yyyy-MM-dd HH:mm:ss";

      int iLen = str.length();
      if (str == null) {
        return null;
      } else {
        iLen = str.length();
      }
      //当日期格式不正确的时候保证不会出错
      //KEN 2006-02-24
      if (iLen == 10) {
         strFormat = "yyyy-MM-dd";
      } else if (iLen == 16) {
         strFormat = "yyyy-MM-dd HH:mm";
      } else if (iLen == 19) {
         strFormat = "yyyy-MM-dd HH:mm:ss";
      }

      SimpleDateFormat sdFormat = new SimpleDateFormat(strFormat);
      Date date = new Date();
      try {
          date = sdFormat.parse(str);
      }
      catch(Exception e) {
        //System.out.println("Error="+e);
          return null;
      }
      return date;

  }

  public static String dateToYMD(Date dt) {
      SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
      String str = "";
      try {
          str = sdFormat.format(dt);
      }
      catch(Exception e) {
          return "";
      }
      if (str.equals("1900-01-01")) {
          str = "";
      }
      return str;
  }

  public static String dateToString(Date dt) {
      SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      String str = "";
      try {
          str = sdFormat.format(dt);
      }
      catch(Exception e) {
          return "";
      }
      if (str.equals("1900-01-01 00:00")) {
          str = "";
      }

      return str;
  }
  
  public static String strToChina(String strDate) {
  	
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
    String str = "";
    try {
        str = sdFormat.format(SzDateHelp.stringToDate(strDate));
    }
    catch(Exception e) {
        return "";
    }
    if (str.equals("1900-01-01 00:00")) {
        str = "";
    }

    return str;
}  

  public static String dateToString(Date dt, String strFormat) {
      SimpleDateFormat sdFormat = new SimpleDateFormat(strFormat);
      String str = "";
      try {
          str = sdFormat.format(dt);
      }
      catch(Exception e) {
          return "";
      }
      if (str.equals("1900-01-01 00:00")) {
          str = "";
      }

      return str;
  }


  public static String getNextDayYMD() {
    return SzDateHelp.dateToYMD(SzDateHelp.tomorow());
  }

  public static Date tomorow() {
    Date nextDay = null;
    nextDay = SzDateHelp.nextDay(SzDateHelp.getDate());
    return nextDay;
  }
  
  //2009-06-17
  public static Date yestoday() {
    Date nextDay = null;
    nextDay = SzDateHelp.dayBefore(SzDateHelp.getDate(), 1);
    return nextDay;
  }  

  public static Date nextDay(Date dt) {
      Date nextDay = null;
      nextDay = new Date(dt.getYear(),dt.getMonth(),dt.getDate()+1);
      return nextDay;
  }

  public static Date dayAfter(Date dt, int days) {
      Date nextDay = null;
      nextDay = new Date(dt.getYear(),dt.getMonth(),dt.getDate()+days);
      return nextDay;
  }
  
  public static Date dayAfterHour(Date dt, int hours) {
    Date nextDay = null;
    nextDay = new Date(dt.getYear(),dt.getMonth(),dt.getDate(),dt.getHours() + hours, dt.getMinutes(), dt.getSeconds());
    
    return nextDay;
  }  

  public static Date dayBefore(Date dt, int days) {
      Date nextDay = null;
      nextDay = new Date(dt.getYear(),dt.getMonth(),dt.getDate()-days);
      return nextDay;
  }
  
  //计算两个日期的时间差
  public static int hours(Date dtBefore, Date dtAfter) {
  	int i = 0;
  	
  	i = (int)((dtAfter.getTime() - dtBefore.getTime()) / 3600000);
  	//超过一个小时
  	/*
  	if ((dtAfter.getTime() - dtBefore.getTime())%3600000 > 0){
  		i ++;
  	}
  	*/
  	return i;
  }
  
  public static int leaveMinute(Date dtBefore, Date dtAfter) {
  	int i = 0;
  	i = (int)((dtAfter.getTime() - dtBefore.getTime())/1000/60)%60; 
  	return i;
  }
  
  
  /////////////////////////////////周第一天、最后一天，下周第一天
  //获取本周的第一天
  /**
   * 获取周的第一天
   * @param date
   * @return
   */
  public static Date getFirstDateWeek(Date date) {
    //星期几
    int iDay = date.getDay();
    date = SzDateHelp.dayBefore(date, iDay);

    return date;
  }

  /**
   * 获取本周的第一天
   * @return
   */
  public static Date getFirstDateWeek() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDateWeek(date);
    return date;
  }

  //获取本周的最后一天
  /**
   * 获取周的最后一天
   * @param date
   * @return
   */
  public static Date getLastDateWeek(Date date) {
    //星期几
    int iDay = date.getDay();
    date = SzDateHelp.dayAfter(date, 6 - iDay);

    return date;
  }

  /**
   * 获取本周的最后一天
   * @return
   */
  public static Date getLastDateWeek() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getLastDateWeek(date);
    return date;
  }


  //获取下周的第一天
  /**
   * 获取下一周的第一天
   * @param date
   * @return
   */
  public static Date getFirstDateNextWeek(Date date) {
    //星期几
    int iDay = date.getDay();
    date = SzDateHelp.dayAfter(date, 7 - iDay);

    return date;
  }

  /**
   * 获取下周的第一天
   * @return
   */
  public static Date getFirstDateNextWeek() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDateNextWeek(date);
    return date;
  }


  //获取上周的第一天
  /**
   * 获取上一周的第一天
   * @param date
   * @return
   */
  public static Date getFirstDatePrioWeek(Date date) {
    //星期几
    int iDay = date.getDay();
    date = SzDateHelp.dayBefore(date, 7 + iDay);

    return date;
  }

  /**
   * 获取上周的第一天
   * @return
   */
  public static Date getFirstDatePrioWeek() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDatePrioWeek(date);
    return date;
  }


  /////////////////////////////////////////以下是月的
  //获取本月的第一天
  public static Date getFirstDateMonth(Date date) {
    date = new Date(date.getYear(), date.getMonth(), 1);
    return date;
  }

  public static Date getFirstDateMonth() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDateMonth(date);
    return date;
  }

  //获取下月的第一天
  public static Date getFirstDateNextMonth(Date date) {

    date = new Date(date.getYear(), date.getMonth() + 1, 1);
    return date;
  }

  public static Date getFirstDateNextMonth() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDateNextMonth(date);
    return date;
  }

  //获取上月的第一天
  public static Date getFirstDatePrioMonth(Date date) {

    date = new Date(date.getYear(), date.getMonth() - 1, 1);
    return date;
  }

  //
  public static Date getDatePrioMonth(Date date) {
    date = new Date(date.getYear(), date.getMonth() - 1, date.getDate());
    return date;
  }  
  public static Date getDatePrioYear(Date date) {
    date = new Date(date.getYear() - 1, date.getMonth(), date.getDate());
    return date;
  } 
  
  public static Date getFirstDatePrioMonth() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDatePrioMonth(date);
    return date;
  }


  //获取本月的最后一天
  public static Date getLastDateMonth(Date date) {
    //先获取下个月的第一天
    date = SzDateHelp.getFirstDateNextMonth(date);
    //再取前一天
    date = SzDateHelp.dayBefore(date, 1);

    return date;
  }

  public static Date getLastDateMonth() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getLastDateMonth(date);
    return date;
  }


  /////年的
  //获取上年的第一天
  public static Date getFirstDatePrioYear(Date date) {
    date = new Date(date.getYear()-1, 0, 1);
    return date;
  }

  public static Date getFirstDatePrioYear() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDatePrioYear(date);
    return date;
  }

  //获取本年的第一天
  public static Date getFirstDateYear(Date date) {
    date = new Date(date.getYear(), 0, 1);
    return date;
  }

  public static Date getFirstDateYear() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDateYear(date);
    return date;
  }

  //获取明年的第一天
  public static Date getFirstDateNextYear(Date date) {
    date = new Date(date.getYear() + 1, 0, 1);
    return date;
  }

  public static Date getFirstDateNextYear() {
    Date date = SzDateHelp.getDate();
    date = SzDateHelp.getFirstDateNextYear(date);
    return date;
  }

  public static String getMonthStr(Date date) {  	
      String str = "";
      String sFormat = "yyyy-MM";
      str = SzDateHelp.getDateStr(sFormat);      

      if (str.equals("1900-01")) {
          str = "";
      }
      return str;	  
  }
  

  public static void main(String args[]) {
    System.out.println("==" + SzDateHelp.getDateStr("yyyyMM"));
    //System.out.println("==" + SzDateHelp.getLastDateWeek());
  }

}