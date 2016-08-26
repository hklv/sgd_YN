/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 * <p/>
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * <p/>
 * $Tracker List
 * <p/>
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5
 */
package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.JdbcTemplate;
import com.ztesoft.zsmart.core.jdbc.JdbcUtil;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.utils.ValidateUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title: ZSMART
 * </p>
 * <p/>
 * <p>
 * Description:
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p/>
 * <p>
 * Company: ztesoft
 * </p>
 *
 * @author chen.weizheng 鏀硅繘鑷猂97
 * @version 0.1
 */
public final class DateUtil {
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(DateUtil.class);

    public static Date MAX_VALUE = offsetYear(getNowDate(), 100);

    // 鏃ユ湡绫诲瀷

    public final static String DATE_FORMAT_1 = "yyyy-MM-dd";

    public final static String DATE_FORMAT_2 = "yyyyMMdd";

    public final static String DATE_FORMAT_4 = "yyyy-MM";

    public final static String DATE_FORMAT_5 = "yyyyMM";

    public final static String DATE_FORMAT_6 = "yyyy";

    public final static String DATETIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public final static String DATETIME_FORMAT_3 = "yyyy-MM-dd HH-mm-ss";

    public final static String DATETIME_FORMAT_2 = "yyyyMMddHHmmss";

    public final static String DATETIME_FORMAT_4 = "yyyy/MM/dd HH:mm:ss";

    public final static String DATE_FORMAT_3 = "yyyy年MM月dd日";
    public final static String DATE_FORMAT_7 = "yyyy年MM月dd日HH时mm分ss秒";

    public final static int DIFFER_IN_SECOND = 0;

    public final static int DIFFER_IN_MINUTE = 1;

    public final static int DIFFER_IN_HOUR = 2;

    public final static int DIFFER_IN_DAYS = 3;

    public static String DEFAULT_DATE_FORMAT = DATE_FORMAT_1;

    public static String DEFAULT_TIME_FORMAT = DATETIME_FORMAT_1;

    public static String NAME_FILE_DATE_FORMAT = "yyyyMMdd_HHmmss";

    public final static String[] DATE_FORMAT_SUPPORT = {DATETIME_FORMAT_1,
            DATETIME_FORMAT_2, DATETIME_FORMAT_3, DATETIME_FORMAT_4,
            DATE_FORMAT_1, DATE_FORMAT_2, DATE_FORMAT_3, DATE_FORMAT_4, DATE_FORMAT_5, DATE_FORMAT_6};

    public static SimpleDateFormat getDateFormat(String format) {
        /*
         * SimpleDateFormat sdf = (SimpleDateFormat)
		 * aDateFormateMap.get(format); if (sdf == null) { sdf = new
		 * SimpleDateFormat(format); aDateFormateMap.put(format, sdf); }
		 */
        return new SimpleDateFormat(format);
    }

    public static String date2String(Date date, String format) {
        if (date == null) {
            return "";
            // 如果为空，填入当前时间的代码取消。框架中不应含有这种带业务逻辑的代码。 zhang.nanyu commented the
            // code 2008-12-12
            // date = new Date();
        }
        SimpleDateFormat sdf = getDateFormat(format);
        return sdf.format(date);
    }

    public static String date2String(Date date) {
        return date2String(date, DEFAULT_DATE_FORMAT);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        return date2String(date, DEFAULT_DATE_FORMAT);
    }

    public static String getNameFileCurrentDate() {
        Date date = new Date();
        return date2String(date, NAME_FILE_DATE_FORMAT);
    }

    /**
     * Date to SqlDate
     *
     * @param date Date
     * @return Date
     */
    public static java.sql.Date dateToSqlDate(Date date) {
        if (date == null) {
            return null;
        } else if (date instanceof java.sql.Date) {
            return (java.sql.Date) date;
        } else {
            return new java.sql.Date(date.getTime());
        }
    }

    public static java.sql.Date string2SQLDate(String date) {
        java.sql.Date ret = null;

        if (date == null || date.length() == 0) {
            return ret;
        }
        if (date.length() > 11) {
            if (date.indexOf('-') > 0) {

                if (date.indexOf(':') > 0) {
                    ret = string2SQLDate(date, DATETIME_FORMAT_1);
                } else {
                    ret = string2SQLDate(date, DATETIME_FORMAT_3);
                }
            } else if (date.indexOf('/') > 0) {
                ret = string2SQLDate(date, DATETIME_FORMAT_4);
            } else {
                ret = string2SQLDate(date, DATETIME_FORMAT_2);
            }
        } else {
            if (date.indexOf('-') > 0) {
                if (date.length() == 7) {
                    ret = string2SQLDate(date, DATE_FORMAT_4);
                } else {
                    ret = string2SQLDate(date, DATE_FORMAT_1);
                }
            } else if (date.length() == 4) {
                ret = string2SQLDate(date, DATE_FORMAT_6);
            } else if (date.length() == 6) {
                ret = string2SQLDate(date, DATE_FORMAT_5);
            } else if (date.length() == 8) {
                ret = string2SQLDate(date, DATE_FORMAT_2);
            } else {
                ret = string2SQLDate(date, DATE_FORMAT_3);
            }
        }
        return ret;
    }

    public synchronized static java.sql.Date string2SQLDate(String date,
                                                            String format) {
        boolean isSucc = true;
        Exception operateException = null;
        if (!ValidateUtil.validateNotEmpty(format)) {
            isSucc = false;
            operateException = new IllegalArgumentException(
                    "the date format string is null!");
        }
        SimpleDateFormat sdf = getDateFormat(format);
        if (!ValidateUtil.validateNotNull(sdf)) {
            isSucc = false;
            operateException = new IllegalArgumentException(
                    "the date format string is not matching available format object");
        }
        Date tmpDate = null;
        try {
            if (isSucc) {
                tmpDate = sdf.parse(date);
                String tmpDateStr = sdf.format(tmpDate);
                if (!tmpDateStr.equals(date)) {
                    isSucc = false;
                    operateException = new IllegalArgumentException(
                            "the date string " + date
                                    + " is not matching format: " + format);
                }
            }
        } catch (Exception e) {
            isSucc = false;
            operateException = e;
        }

        if (!isSucc) {
            logger.error("the date string " + date
                    + " is not matching format: " + format, operateException);
            if (operateException instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) operateException;
            } else {
                throw new IllegalArgumentException("the date string " + date
                        + " is not matching format: " + format
                        + ".\n cause by :" + operateException.toString());
            }
        } else {
            return new java.sql.Date(tmpDate.getTime());
        }

    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getStandardNowTime() {
        SimpleDateFormat sdf = getDateFormat(DEFAULT_TIME_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * yyyy-MM-dd
     *
     * @return java.sql.Date 锟斤拷前锟斤拷锟斤拷锟斤拷时锟斤拷
     */
    public static java.sql.Date getNowDate() {
        return new java.sql.Date(new Date().getTime());
    }

    /**
     * @param date    Date 锟斤拷始时锟斤拷
     * @param seconds long
     * @return Date
     */
    public static java.sql.Date offsetSecond(java.sql.Date date, long seconds) {
        if (date == null) {
            return null;
        }

        long time = date.getTime();
        time = time + (seconds * 1000);
        return new java.sql.Date(time);
    }

    /**
     * @param date    Date 锟斤拷始时锟斤拷
     * @param minutes long
     * @return Date
     */
    public static java.sql.Date offsetMinute(java.sql.Date date, long minutes) {
        return offsetSecond(date, 60 * minutes);
    }

    /**
     * @param date  Date 锟斤拷始时锟斤拷
     * @param hours long
     * @return Date
     */
    public static java.sql.Date offsetHour(java.sql.Date date, long hours) {
        return offsetMinute(date, 60 * hours);
    }

    /**
     * @param date Date 锟斤拷始时锟斤拷
     * @param days long
     * @return Date
     */
    public static java.sql.Date offsetDay(java.sql.Date date, int days) {
        return offsetHour(date, 24 * days);
    }

    /**
     * @param date  Date 锟斤拷始时锟斤拷
     * @param weeks long
     * @return Date
     */
    public static java.sql.Date offsetWeek(java.sql.Date date, int weeks) {
        return offsetDay(date, 7 * weeks);
    }

    /**
     * @param date Date
     * @return Date
     */
    public static java.sql.Date getLastDayOfMonth(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    /**
     * 锟矫碉拷锟斤拷锟铰的匡拷始时锟斤拷
     *
     * @param date Date
     * @return Date
     */
    public static java.sql.Date getBeginDayOfMonth(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    /**
     * @param date1
     * @param months
     * @return
     */
    public static java.sql.Date offsetMonth(java.sql.Date date1, int months) {
        if (date1 == null) {
            return null;
        }

        java.sql.Date date = new java.sql.Date(date1.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, months);

        int newMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (curDay == maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);

        } else {
            if (curDay > newMaxDay) {
                calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, curDay);
            }
        }

        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    /**
     * @param date  origrinal date
     * @param years offset years
     * @return offset date
     */
    public static java.sql.Date offsetYear(java.sql.Date date, int years) {
        if (date == null) {
            return null;
        }

        java.sql.Date newdate = (java.sql.Date) date.clone();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newdate);
        calendar.add(Calendar.YEAR, years);
        newdate.setTime(calendar.getTimeInMillis());
        return newdate;
    }

    /**
     * 杩斿洖涓や釜鏃ユ湡闂寸殑宸�
     *
     * @param beginDate
     * @param endDate
     * @param returnType 两个时间直接的间隔，返回类型 可以为时、分、天等；
     * @return
     */
    public static long differDateInDays(java.sql.Date beginDate,
                                        java.sql.Date endDate, int returnType) {
        long begin = beginDate.getTime();
        long end = endDate.getTime();
        long surplus = begin - end;
        long ret = 0;
        switch (returnType) {
            case 0://秒
                ret = surplus / 1000;
                break;
            case 1://分
                ret = surplus / 1000 / 60;
                break;
            case 2:
                // 时
                ret = surplus / 1000 / 60 / 60;
                break;
            case 3://天
                ret = surplus / 1000 / 60 / 60 / 24;
                break;
            default:
                break;
        }

        return ret;
    }

    /**
     * 是否在两个时间范围内（字符串）
     *
     * @param date      String 指锟斤拷时锟斤拷
     * @param beginDate String
     * @param endDate   String
     * @return boolean 锟斤拷
     * @throws BaseAppException
     */
    public static boolean isInRange(String date, String beginDate,
                                    String endDate) throws BaseAppException {
        if (!ValidateUtil.validateNotNull(date)
                || !ValidateUtil.validateNotNull(beginDate)
                || !ValidateUtil.validateNotNull(endDate)) {
            ExceptionHandler.publish("");
        }

        int dateLen = date.length();
        int beginDateLen = date.length();
        int endDateLen = date.length();

        if (beginDateLen != dateLen || endDateLen != endDateLen) {
            ExceptionHandler.publish("");
        }

        boolean asc = isAsc(beginDate, endDate);

        if (asc) {
            if (date.compareTo(beginDate) >= 0 && date.compareTo(endDate) <= 0) {
                return true;
            }
        } else {
            if (date.compareTo(beginDate) >= 0 || date.compareTo(endDate) <= 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否在两个时间范围内（时间）
     *
     * @param date      Date
     * @param beginDate Date
     * @param endDate   Date
     * @return boolean
     */
    public static boolean isInRange(Date date, Date beginDate, Date endDate) {
        long time = date.getTime();
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();

        if (time >= beginTime && time <= endTime) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 姣旇緝鏃ユ湡澶у皬锟绞憋拷锟饺较达拷小鍓嶅悗
     *
     * @param beginDate Date 锟斤拷围锟斤拷始时锟斤拷
     * @param endDate   Date 锟斤拷围锟斤拷锟斤拷时锟斤拷
     * @return boolean
     */
    public static int compare(Date beginDate, Date endDate) {
        int ret = 1;
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();

        if (beginTime > endTime) {
            ret = 2;
        }
        if (beginTime == endTime) {
            ret = 1;
        }
        if (beginTime < endTime) {
            ret = 0;
        }
        return ret;
    }

    /**
     * @param firstStr  String
     * @param secondStr String
     * @return boolean true-锟斤拷锟斤拷, false-锟斤拷锟斤拷
     */
    private static boolean isAsc(String firstStr, String secondStr) {
        return (firstStr.compareTo(secondStr) < 0);
    }

    public static java.sql.Date GetDBDateTime() throws BaseAppException {
        return getDBCurrentTime();
    }

    /**
     * @return
     * @throws BaseAppException
     */
    private static java.sql.Date getDBCurrentTime() throws BaseAppException {
        return JdbcTemplate.query(JdbcUtil.getDbBackService(),
                JdbcUtil.getDbBackService().getDialect().getCurrentDateSql(), null,
                null, new RowSetMapper<java.sql.Date>() {
                    public java.sql.Date mapRows(RowSetOperator op, ResultSet rs,
                                                 int colNum, Object para) throws SQLException,
                            BaseAppException {
                        java.sql.Date currDate = null;
                        if (rs.next()) {
                            return op.getDate(rs, 1);
                        }
                        return currDate;
                    }
                });
    }

    /**
     * 返回某个时间的日期 zhang.nanyu added 2008-06-17
     *
     * @param input
     * @return
     */
    public static java.sql.Date getFullDate(java.sql.Date input) {
        // 每天的毫秒数。(这种算法有时区的问题。)
        // final long dayMilSeconds = 1000*60*60*24;
        //
        // long milSeconds = input.getTime();
        //
        // long dateInMilSeconds = (milSeconds/dayMilSeconds)*dayMilSeconds;
        // System.out.println(dateInMilSeconds);
        // return new java.sql.Date(dateInMilSeconds);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // System.out.println(calendar.getTimeInMillis());

        return new java.sql.Date(calendar.getTimeInMillis());

    }

    /**
     * 返回正确的时间字符串
     */
    public static String getConfigTime(String time, String defaultTime) {
        String htime = time.split(":")[0].substring(0, 1);
        Pattern pattern = Pattern.compile("[0-2]");
        Matcher matcher = pattern.matcher(htime);
        if (matcher.matches()) {
            if (htime.equals("2")) {
                Pattern pattern1 = Pattern.compile("[0-2][0-3]:[0-5][0-9]");
                Matcher matcher1 = pattern1.matcher(time);
                if (matcher1.matches()) {
                    return time;
                } else {
                    return defaultTime;
                }
            } else {
                Pattern pattern2 = Pattern.compile("[0-2][0-9]:[0-5][0-9]");
                Matcher matcher2 = pattern2.matcher(time);
                if (matcher2.matches()) {
                    return time;
                } else {
                    return defaultTime;
                }
            }
        } else {
            return defaultTime;
        }

    }

    public static void main(String[] args) throws BaseAppException {
        System.out.println(DateUtil.differDateInDays(DateUtil.string2SQLDate("2014-02-01"), DateUtil.string2SQLDate("2014-02-02"), 3));
    }
}
