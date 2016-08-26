package com.ztesoft.uboss.bpm.analysis.services;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.analysis.dao.ITaskQueryDAO;
import com.ztesoft.uboss.bpm.analysis.dao.dmimpl.FlowQueryDAODm;
import com.ztesoft.uboss.bpm.analysis.model.UserDelayedTasks;
import com.ztesoft.uboss.bpm.analysis.model.UserTaskQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.UserTotalTasks;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskAnalysis {

    /**
     * 统计流程任务总数
     */
    public int analyseTotalTask(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        //统计月份
        Date month = dict.getDate("MONTH");

        //当前时间
        Date now = DateUtil.GetDBDateTime();

        //当前月份
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_4);
        Date nowMon = DateUtil.string2SQLDate(sdf.format(now), DateUtil.DATE_FORMAT_4);

        //默认统计当前月份
        if (month == null) {

            month = nowMon;
            dict.set("MONTH", nowMon);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(month);
        long days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        //统计月份天数
        dict.set("DAYS", days);

        if (month.compareTo(nowMon) <= 0) {

            long finsishTotal = 0;
            long unFinsishTotal = 0;

            ITaskQueryDAO dao = (ITaskQueryDAO) SgdDaoFactory.getDaoImpl(FlowQueryDAODm.class);
            List<UserTotalTasks> userTotalTasks = dao.selectTotalTasks((UserTaskQryCondition) BoHelper.boToDto(dict, UserTaskQryCondition.class));

            Map<Integer, Long> finishedTask = new HashMap<Integer, Long>();
            Map<Integer, Long> unFinishedTask = new HashMap<Integer, Long>();

            for (UserTotalTasks userTotalTask : userTotalTasks) {

                if ("F".equals(userTotalTask.getTaskType())) {

                    finishedTask.put(Integer.valueOf(DateUtil.date2String(userTotalTask.getAnalyseDay(), "dd")), userTotalTask.getTotal());
                    finsishTotal += userTotalTask.getTotal();
                } else if ("U".equals(userTotalTask.getTaskType())) {

                    unFinishedTask.put(Integer.valueOf(DateUtil.date2String(userTotalTask.getAnalyseDay(), "dd")), userTotalTask.getTotal());
                    unFinsishTotal += userTotalTask.getTotal();
                }
            }

            //统计总个数
            dict.set("FINISH_TASK_TOTAL", finsishTotal);
            dict.set("UNFINISH_TASK_TOTAL", unFinsishTotal);

            //统计当前月份
            if (month.compareTo(nowMon) == 0) {

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(now);
                days = cal2.get(Calendar.DAY_OF_MONTH);
            }

            List<DynamicDict> finishedTaskDict = new ArrayList<DynamicDict>();
            for (int i = 1; i <= days; i++) {

                DynamicDict dict2 = new DynamicDict();
                if (finishedTask.get(Integer.valueOf(i)) == null) {

                    dict2.set("TOTAL", 0);
                } else {

                    dict2.set("TOTAL", finishedTask.get(Integer.valueOf(i)));
                }

                finishedTaskDict.add(dict2);
            }

            dict.set("FINISHED_TASK_TOTAL", finishedTaskDict);

            List<DynamicDict> unFinishedTaskDict = new ArrayList<DynamicDict>();
            for (int i = 1; i <= days; i++) {

                DynamicDict dict2 = new DynamicDict();
                if (unFinishedTask.get(Integer.valueOf(i)) == null) {

                    dict2.set("TOTAL", 0);
                } else {

                    dict2.set("TOTAL", unFinishedTask.get(Integer.valueOf(i)));
                }

                unFinishedTaskDict.add(dict2);
            }

            dict.set("UNFINISHED_TASK_TOTAL", unFinishedTaskDict);
        }

        return 0;
    }

    public int analyseDelayedTask(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        //统计月份
        Date month = dict.getDate("MONTH");

        //当前时间
        Date now = DateUtil.GetDBDateTime();

        //当前月份
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT_4);
        Date nowMon = DateUtil.string2SQLDate(sdf.format(now), DateUtil.DATE_FORMAT_4);

        //默认统计当前月份
        if (month == null) {

            month = nowMon;
            dict.set("MONTH", nowMon);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(month);
        long days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        //统计月份天数
        dict.set("DAYS", days);

        if (month.compareTo(nowMon) <= 0) {

            long warnTotal = 0;
            long alarmTotal = 0;

            ITaskQueryDAO dao = (ITaskQueryDAO) SgdDaoFactory.getDaoImpl(FlowQueryDAODm.class);
            List<UserDelayedTasks> userDelayedTasks = dao.selectDelayedTasks((UserTaskQryCondition) BoHelper.boToDto(dict, UserTaskQryCondition.class));

            Map<Integer, Long> warnTask = new HashMap<Integer, Long>();
            Map<Integer, Long> alarmTask = new HashMap<Integer, Long>();
            //Map<Integer, Long> totalDelayedTask = new HashMap<Integer, Long>();

            for (UserDelayedTasks userDelayedTask : userDelayedTasks) {

                if ("W".equals(userDelayedTask.getDelayedType())) {

                    warnTask.put(Integer.valueOf(DateUtil.date2String(userDelayedTask.getAnalyseDay(), "dd")), userDelayedTask.getTotal());
                    warnTotal += userDelayedTask.getTotal();
                } else if ("A".equals(userDelayedTask.getDelayedType())) {

                    alarmTask.put(Integer.valueOf(DateUtil.date2String(userDelayedTask.getAnalyseDay(), "dd")), userDelayedTask.getTotal());
                    alarmTotal += userDelayedTask.getTotal();
                }
            }

            //统计总个数
            dict.set("WARN_TASK_TOTAL", warnTotal);
            dict.set("ALARM_TASK_TOTAL", alarmTotal);
            dict.set("DELAY_TASK_TOTAL", warnTotal + alarmTotal);

            //统计当前月份
            if (month.compareTo(nowMon) == 0) {

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(now);
                days = cal2.get(Calendar.DAY_OF_MONTH);
            }

            List<DynamicDict> warnTaskDict = new ArrayList<DynamicDict>();
            for (int i = 1; i <= days; i++) {

                DynamicDict dict2 = new DynamicDict();
                if (warnTask.get(Integer.valueOf(i)) == null) {

                    dict2.set("TOTAL", 0);
                } else {

                    dict2.set("TOTAL", warnTask.get(Integer.valueOf(i)));
                }

                warnTaskDict.add(dict2);
            }

            dict.set("WARNED_TASK_TOTAL", warnTaskDict);

            List<DynamicDict> alarmTaskDict = new ArrayList<DynamicDict>();
            for (int i = 1; i <= days; i++) {

                DynamicDict dict2 = new DynamicDict();
                if (alarmTask.get(Integer.valueOf(i)) == null) {

                    dict2.set("TOTAL", 0);
                } else {

                    dict2.set("TOTAL", alarmTask.get(Integer.valueOf(i)));
                }

                alarmTaskDict.add(dict2);
            }

            dict.set("ALARMED_TASK_TOTAL", alarmTaskDict);

            List<DynamicDict> delayedTaskDict = new ArrayList<DynamicDict>();
            for (int i = 1; i <= days; i++) {

                DynamicDict dict2 = new DynamicDict();
                long warnNum = 0;
                long alarmNum = 0;
                if (warnTask.get(Integer.valueOf(i)) != null) {

                    warnNum = warnTask.get(Integer.valueOf(i));
                }

                if (alarmTask.get(Integer.valueOf(i)) != null) {

                    alarmNum = alarmTask.get(Integer.valueOf(i));
                }

                dict2.set("TOTAL", warnNum + alarmNum);

                delayedTaskDict.add(dict2);
            }

            dict.set("DEPLAYED_TASK_TOTAL", delayedTaskDict);
        }

        return 0;
    }
}
