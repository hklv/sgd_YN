package com.ztesoft.uboss.bpm.taskcenter.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.IWorkItemManagerDAO;
import com.ztesoft.uboss.bpm.taskcenter.model.HolderQueryCondition;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class WorkItemManagerDAO extends BusiBaseDAO implements
        IWorkItemManagerDAO {
    //edit; liuhao 添加e.BIZ_KEY
    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryTaskList(DynamicDict bo,
                                         HolderQueryCondition condition, DynamicDict uboss_session)
            throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("select e.BIZ_KEY,a.task_list_id,a.task_name,a.task_type,a.template_id,a.create_date,a.state,A.DIRECTION,a.PROC_INST_ID,j.user_name owner_name,"
                + "  b.user_name,g.role_name ,h.job_name,i.org_name,c.HOLDER_ID,c.holder_no,c.holder_state,e.process_name,d.form_id"
                + " ,bform.form_id FLOW_FORM_ID,bform.form_type flow_form_type,bform.dyn_form_id flow_dyn_form_id,bform.page_url flow_form_page_url"
                + " ,form.form_id TASK_FORM_ID, form.form_type TASK_FORM_TYPE,form.dyn_form_id task_dyn_form_id,form.page_url task_form_page_url,k.code TASK_CODE " +
                " from"
                + " bpm_task_list a  left join bfm_user b on a.user_id=b.user_id left join bfm_user j on a.owner=j.user_id"
                + "  left join bfm_role g on a.role_id=g.role_id left join bfm_job h on a.job_id=h.job_id left join bfm_org i on a.org_id=i.org_id,"
                + " bpm_task_holder c ,BPM_PROCESS_TEMP_VER d left join bpm_form bform on d.form_id=bform.form_id,BPM_PROCESS_TEMP e,"
                + "  BPM_PROC_DEF_TYPE f,BPM_TASK_TEMPLATE k left JOIN bpm_form form on k.form_id=form.form_id WHERE a.task_type = 'U' and a.TEMPLATE_ID=k.TEMPLATE_ID"
                + " and a.holder_id = c.holder_id"
                + " and c.PROCESS_VER_ID = D.PROCESS_VER_ID"
                + " and d.PROC_TEMP_ID = e.PROC_TEMP_ID"
                + " and e.PROC_DEF_TYPE_ID = f.PROC_DEF_TYPE_ID"
                + " and a.state in ('I', 'A', 'H','C')"
                + " and c.holder_state <> 'T'  and c.SIMU_FLAG <> '1' ");

        // + " and (A .user_id is null or a.user_id = :USER_ID)"
        // + " and (A .owner is null or a.owner = :USER_ID)");
        // +" AND e.PROC_TEMP_ID =?"

        ParamMap pm = new ParamMap();

        List<Long> proxyUserIds = bo.getList("PROXY_USER");
        if (proxyUserIds != null && !proxyUserIds.isEmpty()) {
            StringBuilder proxyUsers = new StringBuilder();

            for (Long proxyUserId : proxyUserIds) {
                proxyUsers.append(proxyUserId);
                proxyUsers.append(",");
            }
            proxyUsers.deleteCharAt(proxyUsers.length() - 1);
            sqlStr.append(" and (A.user_id is null or a.user_id = " + proxyUsers.toString() + ") and" +
                    " (A.owner is null or a.owner = " + proxyUsers.toString() + ")");
        /*	sqlStr.append(" AND (A.USER_ID IN (");
            sqlStr.append(proxyUsers.toString());
			sqlStr.append(") OR A.OWNER IN (");
			sqlStr.append(proxyUsers.toString());
			sqlStr.append("))");*/
            //edit by  liuhao ';

            List<Long> roles = bo.getList("PROXY_USER_ROLE");
            if (roles != null && roles.size() > 0) {
                StringBuffer roleIds = new StringBuffer();
                roleIds.append("(");
                for (Long role : roles) {
                    roleIds.append(role);
                    roleIds.append(",");
                }
                if (roleIds.length() > 0) {
                    roleIds.deleteCharAt(roleIds.length() - 1);
                }
                roleIds.append(")");
                sqlStr.append(" AND ( A .role_id IS NULL OR A .role_id in "
                        + roleIds + ")");
            } else {
                sqlStr.append(" AND ( A .role_id IS NULL OR A .role_id =-1)");
            }


        } else {
            //add by hklv solve nullpointer
            if (uboss_session != null) {
                pm.set("USER_ID", uboss_session.getLong("user-id"));
            }
            pm.set("USER_ID", 1l);
            sqlStr.append(" and (A.user_id is null or a.user_id = :USER_ID) and (A.owner is null or a.owner = :USER_ID)");
            if (uboss_session != null) {
                List<DynamicDict> jobs = uboss_session.getList("jobList");
                StringBuffer jobIds = new StringBuffer();
                jobIds.append("(");
                if (jobs != null && jobs.size() > 0) {
                    for (DynamicDict job : jobs) {
                        String jobId = job.getString("JOB_ID");
                        jobIds.append(jobId);
                        jobIds.append(",");
                    }
                    jobIds.deleteCharAt(jobIds.length() - 1);
                    jobIds.append(")");
                    sqlStr.append(" and (a.job_id is null or a.job_id in " + jobIds
                            + ") ");
                } else {
                    sqlStr.append(" and (a.job_id is null or a.job_id = -1) ");
                }

                List<DynamicDict> roles = uboss_session.getList("roleList");
                if (roles != null && roles.size() > 0) {
                    StringBuffer roleIds = new StringBuffer();
                    roleIds.append("(");
                    for (DynamicDict role : roles) {
                        String roleId = role.getString("ROLE_ID");
                        roleIds.append(roleId);
                        roleIds.append(",");
                    }
                    if (roleIds.length() > 0) {
                        roleIds.deleteCharAt(roleIds.length() - 1);
                    }
                    roleIds.append(")");
                    sqlStr.append(" AND ( A .role_id IS NULL OR A .role_id in "
                            + roleIds + ")");
                } else {
                    sqlStr.append(" AND ( A .role_id IS NULL OR A .role_id =-1)");
                }

                List<DynamicDict> orgs = uboss_session.getList("orgList");
                if (orgs != null && orgs.size() > 0) {
                    StringBuffer orgIds = new StringBuffer();
                    orgIds.append("(");
                    for (DynamicDict org : orgs) {
                        String roleId = org.getString("ORG_ID");
                        orgIds.append(roleId);
                        orgIds.append(",");
                    }
                    if (orgIds.length() > 0) {
                        orgIds.deleteCharAt(orgIds.length() - 1);
                    }
                    orgIds.append(")");
                    sqlStr.append(" and (a.org_id is null or a.org_id in " + orgIds
                            + ")");
                } else {
                    sqlStr.append(" and (a.org_id is null or a.org_id = -1)");
                }
            }

        }

        if (StringUtil.isNotEmpty(bo.getString("TASK_STATE"))) {
            sqlStr.append(" AND a.STATE =:STATE");
            pm.set("STATE", bo.getString("TASK_STATE"));
        }
        if (bo.getLong("PROC_DEF_TYPE_ID") != null) {
            sqlStr.append(" AND f.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID");
            pm.set("PROC_DEF_TYPE_ID", bo.getLong("PROC_DEF_TYPE_ID"));
        }
        if (StringUtil.isNotEmpty(bo.getString("HOLDER_NO"))) {
            sqlStr.append(" AND c.HOLDER_NO=:HOLDER_NO");
            pm.set("HOLDER_NO", bo.getString("HOLDER_NO"));
        }
        // pm.set("", bo.getString("PROC_TEMP_ID"));
        if (StringUtil.isNotEmpty(bo.getString("TASK_NAME"))) {
            sqlStr.append(" AND a.TASK_NAME like :TASK_NAME");
            pm.set("TASK_NAME", "%" + bo.getString("TASK_NAME") + "%");
        }
        /**
         *修改了时间sql语句.
         */
        if (StringUtil.isNotEmpty(bo.getString("FROM_DATE"))) {
            sqlStr.append(" AND    datediff(a.create_date,:create_date)>=0 ");
            //sqlStr.append(" AND a.create_date >= :create_date ");
            pm.set("create_date", bo.getString("FROM_DATE"));
        }
        if (StringUtil.isNotEmpty(bo.getString("TO_DATE"))) {
            sqlStr.append(" AND  datediff(:to_date,a.create_date)>=0 ");
            //sqlStr.append(" AND a.create_date <= :to_date ");
            pm.set("to_date", bo.getString("TO_DATE"));
        }
        if (StringUtil.isNotEmpty(bo.getString("HOLDER_STATE"))) {
            sqlStr.append(" AND c.HOLDER_STATE =:HOLDER_STATE");
            pm.set("HOLDER_STATE", bo.getString("HOLDER_STATE"));
        }
        /***
         * directio 条件后面多了个小括号  by liuhao 8/18
         */
        if (StringUtil.isNotEmpty(bo.getString("DIRECTION"))) {
            sqlStr.append(" AND a.DIRECTION =:DIRECTION");
            pm.set("DIRECTION", bo.getString("DIRECTION"));
        }

        String countSql = new StringBuilder()
                .append("select count(*) cnt from (").append(sqlStr)
                .append(") a").toString();
        Long count = this.query(countSql, pm, null, null,
                new RowSetMapper<Long>() {
                    public Long mapRows(RowSetOperator op, ResultSet rs,
                                        int colNum, Object para) throws SQLException,
                            BaseAppException {
                        long count = 0;
                        if (rs.next()) {
                            count = op.getLong(rs, 1);
                        }
                        return count;
                    }
                });
        //edit by liuhao.任务中心显示任务时 排序,优先待办任务
        sqlStr.append(" ORDER BY a.state desc	,A.CREATE_DATE DESC");
        bo.set("BFM_USER_LIST_COUNT", count);
        if (count == 0) {
            return null;
        }
        return (List<DynamicDict>) this.query(sqlStr.toString(), pm,
                condition.getRowSetFormatter(), null, new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        List<DynamicDict> areaList = new ArrayList();
                        while (rs.next()) {
                            int flag = 1;
                            DynamicDict temp = new DynamicDict();

                            temp.set("BIZ_KEY", op.getString(rs, "BIZ_KEY"));
                            temp.set("TASK_LIST_ID",
                                    op.getString(rs, "TASK_LIST_ID"));
                            temp.set("TASK_NAME", op.getString(rs, "TASK_NAME"));
                            temp.set("TASK_TYPE", op.getString(rs, "TASK_TYPE"));
                            temp.set("TEMPLATE_ID",
                                    op.getString(rs, "TEMPLATE_ID"));
                            temp.set("CREATE_DATE",
                                    op.getString(rs, "CREATE_DATE"));
                            temp.set("STATE", op.getString(rs, "STATE"));
                            temp.set("PROC_INST_ID",
                                    op.getString(rs, "PROC_INST_ID"));
                            temp.set("OWNER_NAME",
                                    op.getString(rs, "OWNER_NAME"));
                            temp.set("USER_NAME", op.getString(rs, "USER_NAME"));
                            temp.set("ROLE_NAME", op.getString(rs, "ROLE_NAME"));
                            temp.set("JOB_NAME", op.getString(rs, "JOB_NAME"));
                            temp.set("ORG_NAME", op.getString(rs, "ORG_NAME"));
                            temp.set("HOLDER_ID", op.getString(rs, "HOLDER_ID"));
                            temp.set("HOLDER_NO", op.getString(rs, "HOLDER_NO"));
                            temp.set("PROCESS_NAME",
                                    op.getString(rs, "PROCESS_NAME"));
                            temp.set("FORM_ID", op.getString(rs, "FORM_ID"));
                            temp.set("FLOW_FORM_TYPE", op.getString(rs, "FLOW_FORM_TYPE"));
                            temp.set("FLOW_DYN_FORM_ID", op.getString(rs, "FLOW_DYN_FORM_ID"));
                            temp.set("FLOW_FORM_PAGE_URL", op.getString(rs, "FLOW_FORM_PAGE_URL"));

                            temp.set("TASK_FORM_TYPE", op.getString(rs, "TASK_FORM_TYPE"));
                            temp.set("TASK_DYN_FORM_ID", op.getString(rs, "TASK_DYN_FORM_ID"));
                            temp.set("TASK_FORM_PAGE_URL", op.getString(rs, "TASK_FORM_PAGE_URL"));
                            temp.set("TASK_FORM_ID", op.getString(rs, "TASK_FORM_ID"));
                            temp.set("HOLDER_STATE",
                                    op.getString(rs, "HOLDER_STATE"));
                            temp.set("DIRECTION",
                                    op.getString(rs, "DIRECTION"));
                            temp.set("TASK_CODE",
                                    op.getString(rs, "TASK_CODE"));
                            areaList.add(temp);
                        }
                        return areaList;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public DynamicDict selectTaskDetail(DynamicDict dict)
            throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,f.comments, u.user_name owner_name,"
                + " b.user_name,c.role_name,d.job_name,e.org_name,g.holder_no,i.process_name from"
                + " bpm_task_list a left join bfm_user u on a.user_id=u.user_id  left join bfm_user b on a.user_id=b.user_id "
                + " left join bfm_role c on a.role_id=c.role_id left join bfm_job d on a.job_id=d.job_id left join bfm_org e on a.org_id=e.org_id "
                + " left join BPM_TASK_DETAIL f on  a.task_list_id=f.wo_id ,"
                + " bpm_task_holder g,BPM_PROCESS_TEMP_VER h,BPM_PROCESS_TEMP i"
                + " where  a.holder_id=g.holder_id and g.PROCESS_VER_ID=h.PROCESS_VER_ID"
                + " and h.PROC_TEMP_ID=i.PROC_TEMP_ID and a.task_list_id =?");
        ParamArray pa = new ParamArray();
        pa.set("", dict.getString("TASK_LIST_ID"));
        return (DynamicDict) this.query(sql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        DynamicDict temp = null;
                        if (rs.next()) {
                            temp = new DynamicDict();
//							int flag = 1;
//							temp.set("TASK_LIST_ID", rs.getObject(flag++));
//							temp.set("HOLDER_ID", rs.getObject(flag++));
//							temp.set("SIGIN_TYPE", rs.getObject(flag++));
//							temp.set("TASK_TYPE", rs.getObject(flag++));
//							temp.set("TEMPLATE_ID", rs.getObject(flag++));
//							temp.set("PROC_INST_ID", rs.getObject(flag++));
//							temp.set("EXECUTION_ID", rs.getObject(flag++));
//							temp.set("TASK_ID", rs.getObject(flag++));
//							temp.set("ACT_INST_ID", rs.getObject(flag++));
//							temp.set("TASK_NAME", rs.getObject(flag++));
//							temp.set("PARENT_TASK_ID", rs.getObject(flag++));
//							temp.set("OWNER", rs.getObject(flag++));
//							temp.set("USER_ID", rs.getObject(flag++));
//
//							temp.set("ROLE_ID", rs.getObject(flag++));
//							temp.set("JOB_ID", rs.getObject(flag++));
//							temp.set("ORG_ID", rs.getObject(flag++));
//							temp.set("DESCRIPTION", rs.getObject(flag++));
//							temp.set("TASK_RESULT", rs.getObject(flag++));
//							temp.set("START_TIME", rs.getObject(flag++));
//							temp.set("END_TIME", rs.getObject(flag++));
//							temp.set("DURATION", rs.getObject(flag++));
//							temp.set("PRIORITY", rs.getObject(flag++));
//							temp.set("DUE_DATE", rs.getObject(flag++));
//							temp.set("CREATE_DATE", rs.getObject(flag++));
//							temp.set("STATE", rs.getObject(flag++));
//							temp.set("STATE_DATE", rs.getObject(flag++));
//							temp.set("STATE_REASON", rs.getObject(flag++));
//							temp.set("COMMENTS", rs.getObject(flag++));
//
//							temp.set("OWNER_NAME", rs.getObject(flag++));
//							temp.set("USER_NAME", rs.getObject(flag++));
//							temp.set("ROLE_NAME", rs.getObject(flag++));
//							temp.set("JOB_NAME", rs.getObject(flag++));
//							temp.set("ORG_NAME", rs.getObject(flag++));
//							temp.set("HOLDER_NO", rs.getObject(flag++));
//							temp.set("PROCESS_NAME", rs.getObject(flag++));

                            //int flag = 1;
                            temp.set("TASK_LIST_ID", rs.getObject("TASK_LIST_ID"));
                            temp.set("HOLDER_ID", rs.getObject("HOLDER_ID"));
                            temp.set("SIGIN_TYPE", rs.getObject("SIGIN_TYPE"));
                            temp.set("TASK_TYPE", rs.getObject("TASK_TYPE"));
                            temp.set("TEMPLATE_ID", rs.getObject("TEMPLATE_ID"));
                            temp.set("PROC_INST_ID", rs.getObject("PROC_INST_ID"));
                            temp.set("EXECUTION_ID", rs.getObject("EXECUTION_ID"));
                            temp.set("TASK_ID", rs.getObject("TASK_ID"));
                            temp.set("ACT_INST_ID", rs.getObject("ACT_INST_ID"));
                            temp.set("TASK_NAME", rs.getObject("TASK_NAME"));
                            temp.set("PARENT_TASK_ID", rs.getObject("TASK_NAME"));
                            temp.set("OWNER", rs.getObject("OWNER"));
                            temp.set("USER_ID", rs.getObject("USER_ID"));

                            temp.set("ROLE_ID", rs.getObject("ROLE_ID"));
                            temp.set("JOB_ID", rs.getObject("JOB_ID"));
                            temp.set("ORG_ID", rs.getObject("ORG_ID"));
                            temp.set("DESCRIPTION", rs.getObject("DESCRIPTION"));
                            temp.set("TASK_RESULT", rs.getObject("TASK_RESULT"));
                            temp.set("START_TIME", rs.getObject("START_TIME"));
                            temp.set("END_TIME", rs.getObject("END_TIME"));
                            temp.set("DURATION", rs.getObject("DURATION"));
                            temp.set("PRIORITY", rs.getObject("PRIORITY"));
                            temp.set("DUE_DATE", rs.getObject("DUE_DATE"));
                            temp.set("CREATE_DATE", rs.getObject("CREATE_DATE"));
                            temp.set("STATE", rs.getObject("STATE"));
                            temp.set("STATE_DATE", rs.getObject("STATE_DATE"));
                            temp.set("STATE_REASON", rs.getObject("STATE_REASON"));
                            temp.set("COMMENTS", rs.getObject("COMMENTS"));

                            temp.set("OWNER_NAME", rs.getObject("OWNER_NAME"));
                            temp.set("USER_NAME", rs.getObject("USER_NAME"));
                            temp.set("ROLE_NAME", rs.getObject("ROLE_NAME"));
                            temp.set("JOB_NAME", rs.getObject("JOB_NAME"));
                            temp.set("ORG_NAME", rs.getObject("ORG_NAME"));
                            temp.set("HOLDER_NO", rs.getObject("HOLDER_NO"));
                            temp.set("PROCESS_NAME", rs.getObject("PROCESS_NAME"));
                        }
                        return temp;
                    }
                });
    }

    public List<DynamicDict> qryAbnoramlTaskList(DynamicDict bo)
            throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("SELECT A.task_list_id,A.execution_Id, A.task_name,A.task_type,A.create_date,a.state,"
                + " c.holder_no,c.process_name,A.STATE_REASON FROM"
                + " bpm_task_list A, "
                + " bpm_task_holder c ,BPM_PROCESS_TEMP_VER d,BPM_PROCESS_TEMP e,"
                + "  BPM_PROC_DEF_TYPE f WHERE task_type = 'S'"
                + " AND A .holder_id = c.holder_id"
                + " AND C.PROCESS_VER_ID = D.PROCESS_VER_ID"
                + " AND d.PROC_TEMP_ID = e.PROC_TEMP_ID"
                + " AND e.PROC_DEF_TYPE_ID = f.PROC_DEF_TYPE_ID"
                + " AND A . STATE = ('E')  "
                // + " AND (A .user_id IS NULL OR A .user_id = ?)"
                + " AND (A .job_id IS NULL OR A .job_id = :JOB_ID) "
                // + " AND ( A .role_id IS NULL OR A .role_id = :)"
                + " AND (A .org_id IS NULL OR A .org_id = :ORG_ID)" +
                "  and   c.holder_state='A'");
        // +" AND e.PROC_TEMP_ID =?"

        ParamMap pm = new ParamMap();

        pm.set("JOB_ID", bo.getLong("JOB_ID"));
        pm.set("ORG_ID", bo.getLong("ORG_ID"));

        if (bo.getLong("PROC_DEF_TYPE_ID") != null) {
            sqlStr.append(" AND f.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID");
            pm.set("PROC_DEF_TYPE_ID", bo.getLong("PROC_DEF_TYPE_ID"));
        }
        // pm.set("", bo.getString("PROC_TEMP_ID"));
        if (StringUtil.isNotEmpty(bo.getString("TASK_NAME"))) {
            sqlStr.append(" AND a.TASK_NAME like :TASK_NAME");
            pm.set("TASK_NAME", bo.getString("TASK_NAME"));
        }
        if (StringUtil.isNotEmpty(bo.getString("START_TIME"))) {
            sqlStr.append(" AND datediff(a.START_TIME,:START_TIME)>=0");
            pm.set("START_TIME", bo.getString("START_TIME"));
        }
        if (StringUtil.isNotEmpty(bo.getString("END_TIME"))) {
            sqlStr.append(" AND datediff(:END_TIME,a.START_TIME)>=0 ");
            pm.set("END_TIME", bo.getString("END_TIME"));
        }
        if (StringUtil.isNotEmpty(bo.getString("HOLDER_NO"))) {
            sqlStr.append(" AND C.HOLDER_NO = :HOLDER_NO ");
            pm.set("HOLDER_NO", bo.getString("HOLDER_NO"));
        }

        return (List<DynamicDict>) this.query(sqlStr.toString(), pm, (RowSetFormatter) BoHelper.boToDto(bo.getBO("ROW_SET_FORMATTER"), RowSetFormatter.class),
                null, new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {
                        List<DynamicDict> areaList = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            int flag = 1;
                            DynamicDict temp = new DynamicDict();
                            temp.set("TASK_LIST_ID", op.getString(rs, "TASK_LIST_ID"));
                            temp.set("EXECUTION_ID", op.getString(rs, "EXECUTION_ID"));
                            temp.set("TASK_NAME", op.getString(rs, "TASK_NAME"));
                            temp.set("TASK_TYPE", op.getString(rs, "TASK_TYPE"));
                            temp.set("CREATE_DATE", op.getString(rs, "CREATE_DATE"));
                            temp.set("STATE", op.getString(rs, "STATE"));
                            temp.set("HOLDER_NO", op.getString(rs, "HOLDER_NO"));
                            temp.set("PROCESS_NAME", op.getString(rs, "PROCESS_NAME"));
                            temp.set("STATE_REASON", op.getString(rs, "STATE_REASON"));
                            areaList.add(temp);
                        }
                        return areaList;
                    }
                });
    }

    public List<DynamicDict> qryBlockTaskList(DynamicDict bo) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("SELECT A.task_list_id,A.execution_Id, A.task_name,A.task_type,A.create_date,a.state,"
                + " c.holder_no,c.process_name,A.STATE_REASON FROM"
                + " bpm_task_list A, "
                + " bpm_task_holder c ,BPM_PROCESS_TEMP_VER d,BPM_PROCESS_TEMP e,"
                + "  BPM_PROC_DEF_TYPE f WHERE task_type = 'S'"
                + " AND A .holder_id = c.holder_id"
                + " AND C.PROCESS_VER_ID = D.PROCESS_VER_ID"
                + " AND d.PROC_TEMP_ID = e.PROC_TEMP_ID"
                + " AND e.PROC_DEF_TYPE_ID = f.PROC_DEF_TYPE_ID"
                + " AND A . STATE = ('K')  "
                // + " AND (A .user_id IS NULL OR A .user_id = ?)"
                + " AND (A .job_id IS NULL OR A .job_id = :JOB_ID) "
                // + " AND ( A .role_id IS NULL OR A .role_id = :)"
                + " AND (A .org_id IS NULL OR A .org_id = :ORG_ID)");
        // +" AND e.PROC_TEMP_ID =?"

        ParamMap pm = new ParamMap();

        pm.set("JOB_ID", bo.getLong("JOB_ID"));
        pm.set("ORG_ID", bo.getLong("ORG_ID"));

        if (bo.getLong("PROC_DEF_TYPE_ID") != null) {
            sqlStr.append(" AND f.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID");
            pm.set("PROC_DEF_TYPE_ID", bo.getLong("PROC_DEF_TYPE_ID"));
        }
        // pm.set("", bo.getString("PROC_TEMP_ID"));
        if (StringUtil.isNotEmpty(bo.getString("TASK_NAME"))) {
            sqlStr.append(" AND a.TASK_NAME like :TASK_NAME");
            pm.set("TASK_NAME", bo.getString("TASK_NAME"));
        }

        return (List<DynamicDict>) this.query(sqlStr.toString(), pm, null,
                null, new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {
                        List<DynamicDict> areaList = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            int flag = 1;
                            DynamicDict temp = new DynamicDict();
                            temp.set("TASK_LIST_ID", op.getString(rs, flag++));
                            temp.set("EXECUTION_ID", op.getString(rs, flag++));
                            temp.set("TASK_NAME", op.getString(rs, flag++));
                            temp.set("TASK_TYPE", op.getString(rs, flag++));
                            temp.set("CREATE_DATE", op.getString(rs, flag++));
                            temp.set("STATE", op.getString(rs, flag++));
                            temp.set("HOLDER_NO", op.getString(rs, flag++));
                            temp.set("PROCESS_NAME", op.getString(rs, flag++));
                            temp.set("STATE_REASON", op.getString(rs, flag++));
                            areaList.add(temp);
                        }
                        return areaList;
                    }
                });
    }

    /**
     * 查询流程单   change by liuhao.. 2014 8 25 ..可以查询出我的流程
     *
     * @param bo
     * @return
     * @throws BaseAppException
     */
    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryHolderList(DynamicDict bo,
                                           HolderQueryCondition condition) throws BaseAppException {
        StringBuffer sqlStr1 = new StringBuffer();

        sqlStr1.append("select a.holder_no "
                + "from bpm_task_holder a ,BPM_PROCESS_TEMP_VER b,BPM_PROCESS_TEMP c,  BPM_PROC_DEF_TYPE d "
                + " where a .PROCESS_VER_ID = b.PROCESS_VER_ID and b.PROC_TEMP_ID = c.PROC_TEMP_ID "
                + " and c.PROC_DEF_TYPE_ID = d.PROC_DEF_TYPE_ID [and a.HOLDER_STATE = :HOLDER_STATE] [and a.PROCESS_NAME = :PROCESS_NAME] and b.state !='X' and c.state !='X' and d.state !='X' "
                + " [and b.PROCESS_VER_ID = :PROCESS_VER_ID] [and c.PROC_TEMP_ID=:PROC_TEMP_ID] [and d.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID] [and a.HOLDER_NO  like :HOLDER_NO]"
                + " [and a.STATE = :STATE]  [AND datediff(a.START_TIME,:START_TIME)>=0] [AND datediff(:END_TIME,a.START_TIME)>=0 ] [AND A.SIMU_FLAG = :SIMU_FLAG] [ AND  a.creator_id=:USER_ID] ");

        StringBuffer sqlStr2 = new StringBuffer();
        sqlStr2.append("select c.BIZ_KEY,a.holder_id,a.holder_no,a.PROCESS_VER_ID,a.PROCESS_NAME,a.PROC_INST_ID,a.BUSINESS_KEY,a.HOLDER_STATE,a.HOLDER_STATE_DATE,"
                + "a.START_TIME,a.END_TIME,a.DELETE_REASON,A.CREATOR_ID,U.USER_NAME CREATOR_NAME, b.VER,b.DEPLOY_ID,d.NAME as PROC_TYPE_NAME,c.OVER_TIME "
                + "from BPM_TASK_HOLDER A LEFT JOIN BFM_USER U ON A.CREATOR_ID = U.USER_ID ,BPM_PROCESS_TEMP_VER b,BPM_PROCESS_TEMP c,  BPM_PROC_DEF_TYPE d "
                + " where a .PROCESS_VER_ID = b.PROCESS_VER_ID and b.PROC_TEMP_ID = c.PROC_TEMP_ID "
                + " and c.PROC_DEF_TYPE_ID = d.PROC_DEF_TYPE_ID [and a.HOLDER_STATE = :HOLDER_STATE] [and a.PROCESS_NAME = :PROCESS_NAME] and b.state !='X' and c.state !='X' and d.state !='X' "
                + " [and b.PROCESS_VER_ID = :PROCESS_VER_ID] [and c.PROC_TEMP_ID=:PROC_TEMP_ID] [and d.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID] [and a.HOLDER_NO  like :HOLDER_NO] [and a.HOLDER_ID=:HOLDER_ID]"
                + " [and a.STATE = :STATE]  [AND datediff(a.START_TIME,:START_TIME)>=0] [AND datediff(:END_TIME,a.START_TIME)>=0 ] [AND A.SIMU_FLAG = :SIMU_FLAG][ AND  a.creator_id=:USER_ID ] ORDER BY A.START_TIME DESC");
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "STATE", bo.getString("STATE"));
        ParamMapHelper.setValue(pm, "PROC_DEF_TYPE_ID",
                bo.getLong("PROC_DEF_TYPE_ID"));
        ParamMapHelper.setValue(pm, "PROC_TEMP_ID", bo.getLong("PROC_TEMP_ID"));
        ParamMapHelper.setValue(pm, "PROCESS_VER_ID",
                bo.getLong("PROCESS_VER_ID"));
        ParamMapHelper.setValue(pm, "HOLDER_NO", bo.getString("HOLDER_NO"));
        ParamMapHelper.setValue(pm, "HOLDER_ID", bo.getString("HOLDER_ID"));
        ParamMapHelper.setValue(pm, "HOLDER_STATE",
                bo.getString("HOLDER_STATE"));
        ParamMapHelper.setValue(pm, "START_TIME", bo.getString("START_TIME"));
        ParamMapHelper.setValue(pm, "END_TIME", bo.getString("END_TIME"));

        ParamMapHelper.setValue(pm, "PROCESS_NAME", bo.getString("PROCESS_NAME"));

        if (StringUtil.isNotEmpty(bo.getString("SIMU_FLAG"))) {
            ParamMapHelper.setValue(pm, "SIMU_FLAG", bo.getString("SIMU_FLAG"));
        } else {
            ParamMapHelper.setValue(pm, "SIMU_FLAG", "0");
        }
        ParamMapHelper.setValue(pm, "USER_ID", bo.getLong("USER_ID"));
        String countSql = new StringBuilder()
                .append("select count(*) cnt from (").append(sqlStr1)
                .append(") a").toString();

        Long count = this.query(countSql, pm, null, null,
                new RowSetMapper<Long>() {
                    public Long mapRows(RowSetOperator op, ResultSet rs,
                                        int colNum, Object para) throws SQLException,
                            BaseAppException {
                        long count = 0;
                        if (rs.next()) {
                            count = op.getLong(rs, 1);
                        }
                        return count;
                    }
                });

        bo.set("BFM_USER_LIST_COUNT", count);
        if (count == 0) {
            return null;
        }

        return (List<DynamicDict>) this.query(sqlStr2.toString(), pm,
                condition.getRowSetFormatter(), null, new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        List<DynamicDict> areaList = new ArrayList();
                        while (rs.next()) {
                            int flag = 1;
                            DynamicDict temp = new DynamicDict();

                            temp.set("BIZ_KEY", op.getString(rs, "BIZ_KEY"));
                            temp.set("HOLDER_ID", op.getString(rs, "HOLDER_ID"));
                            temp.set("HOLDER_NO", op.getString(rs, "HOLDER_NO"));
                            temp.set("PROCESS_VER_ID",
                                    op.getString(rs, "PROCESS_VER_ID"));
                            temp.set("PROCESS_NAME",
                                    op.getString(rs, "PROCESS_NAME"));
                            temp.set("PROC_INST_ID",
                                    op.getString(rs, "PROC_INST_ID"));
                            temp.set("BUSINESS_KEY",
                                    op.getString(rs, "BUSINESS_KEY"));
                            temp.set("HOLDER_STATE",
                                    op.getString(rs, "HOLDER_STATE"));
                            temp.set("HOLDER_STATE_DATE",
                                    op.getString(rs, "HOLDER_STATE_DATE"));
                            temp.set("START_TIME",
                                    op.getDate(rs, "START_TIME"));
                            temp.set("END_TIME", op.getDate(rs, "END_TIME"));
                            temp.set("DELETE_REASON",
                                    op.getString(rs, "DELETE_REASON"));
                            temp.set("CREATOR_ID", op.getString(rs, "CREATOR_ID"));
                            temp.set("CREATOR_NAME", op.getString(rs, "CREATOR_NAME"));
                            temp.set("PROCESS_VER", op.getString(rs, "VER"));
                            temp.set("DEPLOY_ID", op.getString(rs, "DEPLOY_ID"));
                            temp.set("PROC_TYPE_NAME",
                                    op.getString(rs, "PROC_TYPE_NAME"));
                            temp.set("OVER_TIME", op.getString(rs, "OVER_TIME"));

                            areaList.add(temp);
                        }
                        return areaList;
                    }
                });
    }

    /**
     * 根据流程单查询工单
     *
     * @param bo
     * @return
     * @throws BaseAppException
     */
    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryTaskListByHolder(DynamicDict bo)
            throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" select a.*,b.user_name,own.user_name owner_name,c.role_name,d.job_name,e.org_name,f.holder_no, g.form_id ,h.FORM_TYPE flow_form_type,h.DYN_FORM_ID flow_dyn_form_id,h.PAGE_URL flow_form_page_url," +
                " j.form_id task_form_id,j.FORM_TYPE task_form_type,j.DYN_FORM_ID task_dyn_form_id,j.PAGE_URL task_form_page_url "
                + " from bpm_task_list a "
                + " left join bfm_user b on a.user_id=b.user_id "
                + " LEFT JOIN bfm_user own ON A .owner = own.user_id "
                + " left join bfm_role c on a.role_id=c.role_id "
                + " left join bfm_job d on a.job_id=d.job_id "
                + " left join bfm_org e on a.org_id=e.org_id"
                + " left join  BPM_TASK_TEMPLATE k on  a.TEMPLATE_ID = k.TEMPLATE_ID left join BPM_FORM j on k.form_id=j.form_id,"
                + " bpm_task_holder f,BPM_PROCESS_TEMP_VER g left join BPM_FORM h on g.form_id=h.form_id "
                + " where  a.HOLDER_ID = :HOLDER_ID and  "
                + " a.holder_id=f.holder_id "
                + " and f.PROCESS_VER_ID = g.PROCESS_VER_ID"
                + " order by a.CREATE_DATE");

        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "HOLDER_ID", bo.getString("HOLDER_ID"));

        return (List<DynamicDict>) this.query(sqlStr.toString(), pm, null,
                null, new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        List<DynamicDict> areaList = new ArrayList();
                        while (rs.next()) {
                            int flag = 1;
                            DynamicDict temp = new DynamicDict();
                            temp.set("TASK_LIST_ID",
                                    op.getString(rs, "TASK_LIST_ID"));
                            temp.set("HOLDER_ID", op.getString(rs, "HOLDER_ID"));
                            temp.set("SIGIN_TYPE",
                                    op.getString(rs, "SIGIN_TYPE"));
                            temp.set("TASK_TYPE", op.getString(rs, "TASK_TYPE"));
                            temp.set("PROC_INST_ID",
                                    op.getString(rs, "PROC_INST_ID"));
                            temp.set("EXECUTION_ID",
                                    op.getString(rs, "EXECUTION_ID"));
                            temp.set("TASK_ID", op.getString(rs, "TASK_ID"));
                            temp.set("TASK_NAME", op.getString(rs, "TASK_NAME"));
                            temp.set("PARENT_TASK_ID",
                                    op.getString(rs, "PARENT_TASK_ID"));
                            temp.set("OWNER", op.getLong(rs, "OWNER"));
                            temp.set("USER_ID", op.getLong(rs, "USER_ID"));
                            temp.set("ROLE_ID", op.getLong(rs, "ROLE_ID"));

                            temp.set("JOB_ID", op.getLong(rs, "JOB_ID"));
                            temp.set("ORG_ID", op.getLong(rs, "ORG_ID"));
                            temp.set("DESCRIPTION",
                                    op.getString(rs, "DESCRIPTION"));
                            temp.set("START_TIME",
                                    op.getString(rs, "START_TIME"));
                            temp.set("END_TIME", op.getString(rs, "END_TIME"));
                            temp.set("DURATION", op.getString(rs, "DURATION"));
                            temp.set("PRIORITY", op.getString(rs, "PRIORITY"));
                            temp.set("DUE_DATE", op.getString(rs, "DUE_DATE"));
                            temp.set("CREATE_DATE",
                                    op.getString(rs, "CREATE_DATE"));
                            temp.set("STATE", op.getString(rs, "STATE"));
                            temp.set("STATE_DATE",
                                    op.getString(rs, "STATE_DATE"));
                            temp.set("STATE_REASON",
                                    op.getString(rs, "STATE_REASON"));
                            temp.set("TEMPLATE_ID",
                                    op.getLong(rs, "TEMPLATE_ID"));
                            temp.set("USER_NAME", op.getString(rs, "USER_NAME"));
                            temp.set("OWNER_NAME", rs.getString("OWNER_NAME"));
                            temp.set("ROLE_NAME", rs.getString("ROLE_NAME"));
                            temp.set("JOB_NAME", op.getString(rs, "JOB_NAME"));
                            temp.set("ORG_NAME", op.getString(rs, "ORG_NAME"));
                            temp.set("HOLDER_NO", op.getString(rs, "HOLDER_NO"));
                            temp.set("FORM_ID", op.getString(rs, "FORM_ID"));
                            temp.set("FLOW_FORM_TYPE", op.getString(rs, "FLOW_FORM_TYPE"));
                            temp.set("FLOW_DYN_FORM_ID", op.getString(rs, "FLOW_DYN_FORM_ID"));
                            temp.set("FLOW_FORM_PAGE_URL", op.getString(rs, "FLOW_FORM_PAGE_URL"));

                            temp.set("TASK_FORM_ID", op.getString(rs, "TASK_FORM_ID"));
                            temp.set("TASK_FORM_TYPE", op.getString(rs, "TASK_FORM_TYPE"));
                            temp.set("TASK_DYN_FORM_ID", op.getString(rs, "TASK_DYN_FORM_ID"));
                            temp.set("TASK_FORM_PAGE_URL", op.getString(rs, "TASK_FORM_PAGE_URL"));

                            temp.set("DIRECTION",
                                    op.getString(rs, "DIRECTION"));
                            areaList.add(temp);
                        }
                        return areaList;
                    }
                });
    }

    // @Override
    public long qryAbnoramlTaskListCount(DynamicDict bo)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("SELECT COUNT(1) FROM"
                + " bpm_task_list A, "
                + " bpm_task_holder c ,BPM_PROCESS_TEMP_VER d,BPM_PROCESS_TEMP e,"
                + "  BPM_PROC_DEF_TYPE f WHERE task_type = 'S'"
                + " AND A .holder_id = c.holder_id"
                + " AND C.PROCESS_VER_ID = D.PROCESS_VER_ID"
                + " AND d.PROC_TEMP_ID = e.PROC_TEMP_ID"
                + " AND e.PROC_DEF_TYPE_ID = f.PROC_DEF_TYPE_ID"
                + " AND A . STATE = ('E')  "
                // + " AND (A .user_id IS NULL OR A .user_id = ?)"
                + " AND (A .job_id IS NULL OR A .job_id = :JOB_ID) "
                // + " AND ( A .role_id IS NULL OR A .role_id = :)"
                + " AND (A .org_id IS NULL OR A .org_id = :ORG_ID)" +
                //edit by liuhao
                "  and   c.holder_state='A'");
        // +" AND e.PROC_TEMP_ID =?"

        ParamMap pm = new ParamMap();

        pm.set("JOB_ID", bo.getLong("JOB_ID"));
        pm.set("ORG_ID", bo.getLong("ORG_ID"));

        if (bo.getLong("PROC_DEF_TYPE_ID") != null) {
            sqlStr.append(" AND f.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID");
            pm.set("PROC_DEF_TYPE_ID", bo.getLong("PROC_DEF_TYPE_ID"));
        }
        // pm.set("", bo.getString("PROC_TEMP_ID"));
        if (StringUtil.isNotEmpty(bo.getString("TASK_NAME"))) {
            sqlStr.append(" AND a.TASK_NAME like :TASK_NAME");
            pm.set("TASK_NAME", bo.getString("TASK_NAME"));
        }

        if (StringUtil.isNotEmpty(bo.getString("START_TIME"))) {
            sqlStr.append(" AND a.START_TIME >= to_date(:START_TIME, 'YYYY-mm-dd' )");
            pm.set("START_TIME", bo.getString("START_TIME"));
        }
        if (StringUtil.isNotEmpty(bo.getString("END_TIME"))) {
            sqlStr.append(" AND a.START_TIME <= to_date(:END_TIME, 'YYYY-mm-dd') ");
            pm.set("END_TIME", bo.getString("END_TIME"));
        }
        if (StringUtil.isNotEmpty(bo.getString("HOLDER_NO"))) {
            sqlStr.append(" AND C.HOLDER_NO = :HOLDER_NO ");
            pm.set("HOLDER_NO", bo.getString("HOLDER_NO"));
        }

        return this.selectCount(sqlStr.toString(), pm);
    }

    // @Override
    public long qryBlockTaskListCount(DynamicDict bo)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("SELECT COUNT(1) FROM"
                + " bpm_task_list A, "
                + " bpm_task_holder c ,BPM_PROCESS_TEMP_VER d,BPM_PROCESS_TEMP e,"
                + "  BPM_PROC_DEF_TYPE f WHERE task_type = 'S'"
                + " AND A .holder_id = c.holder_id"
                + " AND C.PROCESS_VER_ID = D.PROCESS_VER_ID"
                + " AND d.PROC_TEMP_ID = e.PROC_TEMP_ID"
                + " AND e.PROC_DEF_TYPE_ID = f.PROC_DEF_TYPE_ID"
                + " AND A . STATE = ('K')  "
                // + " AND (A .user_id IS NULL OR A .user_id = ?)"
                + " AND (A .job_id IS NULL OR A .job_id = :JOB_ID) "
                // + " AND ( A .role_id IS NULL OR A .role_id = :)"
                + " AND (A .org_id IS NULL OR A .org_id = :ORG_ID)");
        // +" AND e.PROC_TEMP_ID =?"

        ParamMap pm = new ParamMap();

        pm.set("JOB_ID", bo.getLong("JOB_ID"));
        pm.set("ORG_ID", bo.getLong("ORG_ID"));

        if (bo.getLong("PROC_DEF_TYPE_ID") != null) {
            sqlStr.append(" AND f.PROC_DEF_TYPE_ID=:PROC_DEF_TYPE_ID");
            pm.set("PROC_DEF_TYPE_ID", bo.getLong("PROC_DEF_TYPE_ID"));
        }
        // pm.set("", bo.getString("PROC_TEMP_ID"));
        if (StringUtil.isNotEmpty(bo.getString("TASK_NAME"))) {
            sqlStr.append(" AND a.TASK_NAME like :TASK_NAME");
            pm.set("TASK_NAME", bo.getString("TASK_NAME"));
        }

        return this.selectCount(sqlStr.toString(), pm);
    }

    public List<Long> qryProxyUser(Long userId) throws BaseAppException {
        return this.selectList(
                "SELECT USER_ID FROM BFM_USER WHERE PROXY_USER_ID = ?",
                Long.class, userId);
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryTaskListByTaskId(String taskId) throws BaseAppException {
        String sqlStr = "SELECT A.TASK_LIST_ID,A.TASK_ID,"
                + "       A.TASK_NAME,B.TEMPLATE_NAME,"
                + "       B.TASK_TYPE,  B.CODE  TASK_CODE"
                + "  FROM BPM_TASK_LIST A   JOIN BPM_TASK_TEMPLATE B"
                + "    ON A.TEMPLATE_ID = B.TEMPLATE_ID "
                + " WHERE A.TASK_ID = :TASK_ID";

        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "TASK_ID", taskId);

        return (List<DynamicDict>) this.query(sqlStr.toString(), pm, null,
                null, new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        List<DynamicDict> taskList = new ArrayList();

                        while (rs.next()) {
                            DynamicDict temp = new DynamicDict();
                            temp.set("TASK_LIST_ID",
                                    op.getString(rs, "TASK_LIST_ID"));
                            temp.set("TASK_ID", op.getString(rs, "TASK_ID"));
                            temp.set("TASK_NAME", op.getString(rs, "TASK_NAME"));
                            temp.set("TEMPLATE_NAME",
                                    op.getString(rs, "TEMPLATE_NAME"));
                            temp.set("TASK_TYPE", op.getString(rs, "TASK_TYPE"));
                            temp.set("TASK_CODE", rs.getString("TASK_CODE"));
                            taskList.add(temp);
                        }
                        return taskList;
                    }
                });
    }

    @Override
    public DynamicDict queryHolderBizKeyByHolderNo(String holderNo)
            throws BaseAppException {
        // TODO Auto-generated method stub
        String sql =
                "select c.BIZ_KEY from BPM_TASK_HOLDER A, " +
                        " BPM_PROCESS_TEMP_VER b,BPM_PROCESS_TEMP c " +
                        " where " +
                        " a .PROCESS_VER_ID = b.PROCESS_VER_ID " +
                        " and b.PROC_TEMP_ID = c.PROC_TEMP_ID " +
                        " and a.HOLDER_NO=?";
        ParamArray pa = new ParamArray();
        pa.set("", holderNo);
        return (DynamicDict) this.query(sql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        DynamicDict temp = null;
                        if (rs.next()) {
                            temp = new DynamicDict();
                            temp.set("BIZ_KEY", rs.getObject("BIZ_KEY"));
                        }
                        return temp;
                    }
                });
    }

}
