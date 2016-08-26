package com.ztesoft.uboss.bpm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * WorkDayUtil
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/5/11
 */
public class WorkDayUtil {
    /**
     * 查询两个日期之间的工作日天数
     *
     * @param dateFrom
     * @param dateTo
     */
    public static int getWorkDays(Date dateFrom, Date dateTo) {
        // 这里要判断第二个参数日期要比第一个参数日期大先继续运行
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat holidaysdf = new SimpleDateFormat("MM-dd");
        // 工作日
        int workDay = 0;
        try {
            gc.setTime(dateFrom);
            long time = dateTo.getTime() - dateFrom.getTime();
            long day = time / 3600000 / 24 + 1;
            for (int i = 0; i < day; i++) {
                if (gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY && gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {
                    // System.out.println(holidaysdf.format(gc.getTime()));
                    if (!holidayList(holidaysdf.format(gc.getTime())) && !holidayOfCN(sdf.format(gc.getTime())))
                        workDay++;
                }
                // 天数加1
                gc.add(gc.DATE, 1);
            }
            // gc.add(gc.DATE,1);
            // System.out.println(sdf.format(gc.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//		System.out.println(workDay);
        return workDay;
    }

    // 春节放假三天，定义到2020年
    public static boolean holidayOfCN(String year) {
        List<String> ls = new ArrayList<String>();
        ls.add("2013-02-10");
        ls.add("2013-02-11");
        ls.add("2013-02-12");
        ls.add("2014-01-31");
        ls.add("2014-02-01");
        ls.add("2014-02-02");
        ls.add("2015-02-19");
        ls.add("2015-02-20");
        ls.add("2015-02-21");
        ls.add("2006-02-08");
        ls.add("2006-02-09");
        ls.add("2006-02-10");
        ls.add("2017-01-28");
        ls.add("2017-01-29");
        ls.add("2017-01-30");
        ls.add("2018-02-16");
        ls.add("2018-02-17");
        ls.add("2018-02-18");
        ls.add("2019-02-05");
        ls.add("2019-02-06");
        ls.add("2019-02-07");
        ls.add("2020-01-25");
        ls.add("2020-01-26");
        ls.add("2020-01-27");
        if (ls.contains(year))
            return true;
        return false;
    }

    // 法定假日，五一和国庆
    public static boolean holidayList(String findDate) {
        List<String> ls = new ArrayList<String>();
        ls.add("05-01");
        ls.add("05-02");
        ls.add("05-03");
        ls.add("10-01");
        ls.add("10-02");
        ls.add("10-03");
        ls.add("10-04");
        ls.add("10-05");
        ls.add("10-06");
        if (ls.contains(findDate))
            return true;
        return false;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse("2013-01-21");
        int workDay = WorkDayUtil.getWorkDays(fromDate, new Date());
        System.out.println(workDay);
    }
}
