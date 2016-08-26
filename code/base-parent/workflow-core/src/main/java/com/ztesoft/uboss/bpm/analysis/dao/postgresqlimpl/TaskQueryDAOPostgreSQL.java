package com.ztesoft.uboss.bpm.analysis.dao.postgresqlimpl;

import com.ztesoft.uboss.bpm.analysis.dao.abstractimpl.TaskQueryDAO;
import com.ztesoft.uboss.bpm.analysis.model.UserDelayedTasks;
import com.ztesoft.uboss.bpm.analysis.model.UserTaskQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.UserTotalTasks;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;

import java.sql.Date;
import java.util.List;

public class TaskQueryDAOPostgreSQL extends TaskQueryDAO {
	
	@Override
	public List<UserTotalTasks> selectTotalTasks(
			UserTaskQryCondition userTaskQryCondition) throws BaseAppException {
		
		String selSql = "SELECT COUNT(1) TOTAL, DATE_TRUNC('day', A.CREATE_DATE) ANALYSE_DAY, 'F' TASK_TYPE FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B"
			+ " WHERE A.STATE = 'C' AND A.TEMPLATE_ID = B.TEMPLATE_ID AND A.CREATE_DATE >= :START_MONTH  AND A.CREATE_DATE < :END_MONTH"
			+ " [ AND A.USER_ID = :USER_ID] [ AND A.ORG_ID = :ORG_ID] [ AND A.ROLE_ID = :ROLE_ID] [ AND A.JOB_ID = :JOB_ID] [ AND B.TEMPLATE_ID = :TEMPLATE_ID] [ AND B.TEMPLATE_TYPE = :TEMPLATE_TYPE]"
			+ " GROUP BY DATE_TRUNC('day', A.CREATE_DATE)"
			+ " UNION ALL"
			+ " SELECT COUNT(1) TOTAL, DATE_TRUNC('day', A.CREATE_DATE) ANALYSE_DAY, 'U' TASK_TYPE FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B"
			+ " WHERE A.STATE IN ('I', 'A', 'H') AND A.TEMPLATE_ID = B.TEMPLATE_ID AND A.CREATE_DATE >= :START_MONTH  AND A.CREATE_DATE < :END_MONTH"
			+ " [ AND A.USER_ID = :USER_ID] [ AND A.ORG_ID = :ORG_ID] [ AND A.ROLE_ID = :ROLE_ID] [ AND A.JOB_ID = :JOB_ID] [ AND B.TEMPLATE_ID = :TEMPLATE_ID] [ AND B.TEMPLATE_TYPE = :TEMPLATE_TYPE]"
			+ " GROUP BY DATE_TRUNC('day', A.CREATE_DATE)";
		
		Date month = userTaskQryCondition.getMonth();
		userTaskQryCondition.setStartMonth(month);
		userTaskQryCondition.setEndMonth(DateUtil.offsetMonth(month, 1));
		
		return this.selectList(selSql, UserTotalTasks.class, userTaskQryCondition);
	}
	
	@Override
	public List<UserDelayedTasks> selectDelayedTasks(
			UserTaskQryCondition userTaskQryCondition) throws BaseAppException {
		
		String sql = "SELECT COUNT(1) TOTAL, DATE_TRUNC('day', A.CREATE_DATE) ANALYSE_DAY, 'W' DELAYED_TYPE FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B WHERE A.TEMPLATE_ID = B.TEMPLATE_ID"
			+ " [ AND A.USER_ID = :USER_ID] [ AND A.ORG_ID = :ORG_ID] [ AND A.ROLE_ID = :ROLE_ID] [ AND A.JOB_ID = :JOB_ID] [ AND B.TEMPLATE_ID = :TEMPLATE_ID] [ AND B.TEMPLATE_TYPE = :TEMPLATE_TYPE]"
			+ " AND B.TASK_TYPE= 'U' AND (A.CREATE_DATE + B.WARN_LIMIT * INTERVAL '1 day') < NOW() AND (A.CREATE_DATE + B.ALARM_LIMIT * INTERVAL '1 day') > NOW()"
			+ " AND A.CREATE_DATE >= :START_MONTH AND A.CREATE_DATE < :END_MONTH AND B.WARN_LIMIT IS NOT NULL"
			+ " GROUP BY DATE_TRUNC('day', A.CREATE_DATE)"
			+ " UNION ALL "
			+ " SELECT COUNT(1), DATE_TRUNC('day', A.CREATE_DATE) ANALYSE_DAY, 'A' DELAYED_TYPE FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B WHERE A.TEMPLATE_ID = B.TEMPLATE_ID"
			+ " [ AND A.USER_ID = :USER_ID] [ AND A.ORG_ID = :ORG_ID] [ AND A.ROLE_ID = :ROLE_ID] [ AND A.JOB_ID = :JOB_ID] [ AND B.TEMPLATE_ID = :TEMPLATE_ID] [ AND B.TEMPLATE_TYPE = :TEMPLATE_TYPE]"
			+ " AND B.TASK_TYPE= 'U' AND (A.CREATE_DATE + B.ALARM_LIMIT * INTERVAL '1 day') < NOW()"
			+ " AND A.CREATE_DATE >= :START_MONTH AND A.CREATE_DATE < :END_MONTH AND B.ALARM_LIMIT IS NOT NULL"
			+ " GROUP BY DATE_TRUNC('day', A.CREATE_DATE)";
		
		Date month = userTaskQryCondition.getMonth();
		userTaskQryCondition.setStartMonth(month);
		userTaskQryCondition.setEndMonth(DateUtil.offsetMonth(month, 1));
		
		return this.selectList(sql, UserDelayedTasks.class, userTaskQryCondition);
	}
}
