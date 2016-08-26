package com.ztesoft.sgd.base.helper;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.springframework.util.Assert;

/**
 * 对时间操作的工具类<br>
 *
 * @author sunhao<br>
 * @version 1.0 2015年6月2日 上午11:18:02<br>
 */
public abstract class DateHelper {
    /**
     * 获取间隔给定天数的日期 <br>
     *
     * @param date 给定的日期(eg:2015-06-02 12:00)
     * @param days 间隔的天数，正数是给定时间往后，负数是给定时间往前(eg:-1 or 1)
     * @return 计算出来的日期(eg:2015-06-01 12:00 or 2015-06-03 12:00)<br>
     */
    public static Date intervalSomeDays(Date date, int days) {
        if (date == null) {
            return date;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + days);

        return cal.getTime();
    }

    /**
     * 获取间隔给定月数的日期 <br>
     *
     * @param date   给定的日期(eg:2015-06-02 12:00)
     * @param months 间隔的月数，正数是给定时间往后，负数是给定时间往前(eg:-1 or 1)
     * @return 计算出来的日期(eg:2015-05-02 12:00 or 2015-07-02 12:00)<br>
     */
    public static Date intervalSomeMonths(Date date, int months) {
        if (date == null) {
            return date;
        }


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + months);

        return cal.getTime();
    }

    /**
     * 获取间隔给定月数的日期 <br>
     *
     * @param date  给定的日期(eg:2015-06-02 12:00)
     * @param years 间隔的月数，正数是给定时间往后，负数是给定时间往前(eg:-1 or 1)
     * @return 计算出来的日期(eg:2014-06-02 12:00 or 2016-06-02 12:00)<br>
     */
    public static Date intervalSomeYears(Date date, int years) {
        if (date == null) {
            return date;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + years);

        return cal.getTime();
    }

    /**
     * timestamp转成java.util.Date
     *
     * @param timestamp timestamp时间
     * @return
     */
    public static Date toDate(Timestamp timestamp) {
        Assert.notNull(timestamp);

        return new Date(timestamp.getTime());
    }
}
