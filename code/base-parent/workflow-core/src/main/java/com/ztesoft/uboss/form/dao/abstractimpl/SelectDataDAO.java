package com.ztesoft.uboss.form.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.uboss.form.dao.ISelectDataDAO;
import com.ztesoft.uboss.form.model.DynSelectDataDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.SeqUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectDataDAO extends BusiBaseDAO implements ISelectDataDAO {

	public void addSelectData(DynamicDict dict) throws BaseAppException {
		// TODO Auto-generated method stub
		DynSelectDataDto dynSelectDataDto = new DynSelectDataDto();
		long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_SELECT_DATA", "ID");
		dynSelectDataDto.setId(id);
		dynSelectDataDto.setCreateDate(DateUtil.GetDBDateTime());
		dynSelectDataDto.setState("A");
		dynSelectDataDto.setGlsm(dict.getString("GLSM"));
		dynSelectDataDto.setGlbz(dict.getString("GLBZ"));
		dynSelectDataDto.setGlbm(dict.getString("GLBM"));
		if(dict.getString("GLCONTENT")!=null){
		  dynSelectDataDto.setGlcontentStr(dict.getString("GLCONTENT"));
		}
		dynSelectDataDto.setUserId(dict.getLong("USER_ID"));
		try {
			if (dynSelectDataDto.getGlcontent() == null) {
				dynSelectDataDto.setGlcontent((new String("")).getBytes("UTF-8"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("INSERT INTO BFM_DYN_SELECT_DATA(");
		sqlbuilder
				.append("         ID, GLSM, GLBZ, GLBM, GLCONTENT, STATE, CREATE_DATE, USER_ID)");
		sqlbuilder
				.append(" VALUES (:ID,:GLSM,:GLBZ,:GLBM,:GLCONTENT,:STATE,:CREATE_DATE,:USER_ID)");

		Session ses = null;
		try {
			ses = SessionContext.newSession();
			ses.beginTrans();// 启动事务
			this.updateObject(sqlbuilder.toString(), dynSelectDataDto);
			ses.commitTrans();// 提交事务
		} finally {

			ses.releaseTrans();// 释放事务
		}
	}

	public void addSelectData(DynSelectDataDto dynSelectDataDto)
			throws BaseAppException {
		// TODO Auto-generated method stub
		if(dynSelectDataDto==null)return;
		long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_SELECT_DATA", "ID");
		dynSelectDataDto.setId(id);
		dynSelectDataDto.setCreateDate(DateUtil.GetDBDateTime());
		dynSelectDataDto.setState("A");
		
		try {
			if (dynSelectDataDto.getGlcontent() == null) {
				dynSelectDataDto.setGlcontent((new String("")).getBytes("UTF-8"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("INSERT INTO BFM_DYN_SELECT_DATA(");
		sqlbuilder
				.append("         ID, GLSM, GLBZ, GLBM, GLCONTENT, STATE, CREATE_DATE, USER_ID)");
		sqlbuilder
				.append(" VALUES (:ID,:GLSM,:GLBZ,:GLBM,:GLCONTENT,:STATE,:CREATE_DATE,:USER_ID)");

		Session ses = null;
		try {
			ses = SessionContext.newSession();
			ses.beginTrans();// 启动事务
			this.updateObject(sqlbuilder.toString(), dynSelectDataDto);
			ses.commitTrans();// 提交事务
		} finally {

			ses.releaseTrans();// 释放事务
		}
	}

	public void updateSelectData(DynamicDict dict) throws BaseAppException {
		// TODO Auto-generated method stub
		DynSelectDataDto dynSelectDataDto = new DynSelectDataDto();
		dynSelectDataDto.setId(dict.getLong("ID"));
		dynSelectDataDto.setState("A");
		dynSelectDataDto.setGlsm(dict.getString("GLSM"));
		dynSelectDataDto.setGlbz(dict.getString("GLBZ"));
		dynSelectDataDto.setGlbm(dict.getString("GLBM"));
		if(dict.getString("GLCONTENT")!=null){
		  	
		  dynSelectDataDto.setGlcontentStr(dict.getString("GLCONTENT"));
		}
		
		try {
			if (dynSelectDataDto.getGlcontent() == null) {
				dynSelectDataDto.setGlcontent((new String("")).getBytes("UTF-8"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("UPDATE  BFM_DYN_SELECT_DATA SET ");
		sqlbuilder
				.append("  GLSM=:GLSM, GLBZ=:GLBZ, GLBM=:GLBM, GLCONTENT=:GLCONTENT, STATE=:STATE ");
		sqlbuilder
				.append(" WHERE ID =:ID ");

		Session ses = null;
		try {
			ses = SessionContext.newSession();
			ses.beginTrans();// 启动事务
			this.updateObject(sqlbuilder.toString(), dynSelectDataDto);
			ses.commitTrans();// 提交事务
		} finally {

			ses.releaseTrans();// 释放事务
		}
	}

	public void updateSelectData(DynSelectDataDto dynSelectDataDto)
			throws BaseAppException {
		// TODO Auto-generated method stub
		if(dynSelectDataDto==null)return;
		
		try {
			if (dynSelectDataDto.getGlcontent() == null) {
				dynSelectDataDto.setGlcontent((new String("")).getBytes("UTF-8"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("UPDATE  BFM_DYN_SELECT_DATA SET ");
		sqlbuilder
				.append("  GLSM=:GLSM, GLBZ=:GLBZ, GLBM=:GLBM, GLCONTENT=:GLCONTENT, STATE=:STATE ");
		sqlbuilder
				.append(" WHERE ID =:ID ");

		Session ses = null;
		try {
			ses = SessionContext.newSession();
			ses.beginTrans();// 启动事务
			this.updateObject(sqlbuilder.toString(), dynSelectDataDto);
			ses.commitTrans();// 提交事务
		} finally {

			ses.releaseTrans();// 释放事务
		}

	}

	public void updateSelectDataState(DynamicDict dict) throws BaseAppException {
		// TODO Auto-generated method stub
		DynSelectDataDto dynSelectDataDto = new DynSelectDataDto();
		
		dynSelectDataDto.setState(dict.getString("STATE"));
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("UPDATE  BFM_DYN_SELECT_DATA SET ");
		sqlbuilder
				.append("   STATE=:STATE ");
		sqlbuilder
				.append(" WHERE 1=1 ");

		if(dict.getLong("ID")!=null){
			  sqlbuilder.append(" AND ID=:ID ");
			  dynSelectDataDto.setId(dict.getLong("ID"));
			}
		if(dict.getString("GLSM")!=null && dict.getString("GLSM").trim().length()>0)
			{
				sqlbuilder.append(" AND lower(GLSM) = '"+dict.getString("GLSM").toLowerCase().trim()+"' ");
			}
	    if(dict.getString("GLBZ")!=null && dict.getString("GLBZ").trim().length()>0)
			{
	        	sqlbuilder.append(" AND lower(GLBZ) = '"+dict.getString("GLBZ").toLowerCase().trim()+"' ");
			}
	    if(dict.getString("GLBM")!=null && dict.getString("GLBM").trim().length()>0)
			{
	        	sqlbuilder.append(" AND lower(GLBM) = '"+dict.getString("GLBM").toLowerCase().trim()+"' ");
			}
		
		Session ses = null;
		try {
			ses = SessionContext.newSession();
			ses.beginTrans();// 启动事务
			this.updateObject(sqlbuilder.toString(), dynSelectDataDto);
			ses.commitTrans();// 提交事务
		} finally {

			ses.releaseTrans();// 释放事务
		}

	}

	public DynSelectDataDto querySelectDataDetail(DynamicDict dict) throws BaseAppException {
		// TODO Auto-generated method stub
		ParamMap pm = new ParamMap();
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder
				.append(" SELECT   ID, GLSM, GLBZ, GLBM, GLCONTENT, STATE, CREATE_DATE, USER_ID ");
		sqlbuilder.append(" FROM BFM_DYN_SELECT_DATA ");
		sqlbuilder.append(" WHERE  1=1 ");
		if(dict.getLong("ID")!=null){
		  sqlbuilder.append(" AND ID=:ID ");
		  pm.set("ID", dict.getLong("ID"));
		}
		if(dict.getString("GLSM")!=null && dict.getString("GLSM").trim().length()>0)
		{
			sqlbuilder.append(" AND lower(GLSM) = '"+dict.getString("GLSM").toLowerCase().trim()+"' ");
		}
        if(dict.getString("GLBZ")!=null && dict.getString("GLBZ").trim().length()>0)
		{
        	sqlbuilder.append(" AND lower(GLBZ) = '"+dict.getString("GLBZ").toLowerCase().trim()+"' ");
		}
        if(dict.getString("GLBM")!=null && dict.getString("GLBM").trim().length()>0)
		{
        	sqlbuilder.append(" AND lower(GLBM) = '"+dict.getString("GLBM").toLowerCase().trim()+"' ");
		}
		
		sqlbuilder.append("  ORDER BY ID DESC");
		return this
				.selectObject(sqlbuilder.toString(), DynSelectDataDto.class, pm);
	}

	public List<DynamicDict> querySelectDataList(DynamicDict dict)
			throws BaseAppException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ID, GLSM, GLBZ, GLBM, STATE, CREATE_DATE, USER_ID ")
           .append(" FROM BFM_DYN_SELECT_DATA  ")
           .append(" WHERE 1=1  ");
        ParamArray pa = new ParamArray();
        if(dict.getString("STATE")!=null){
            	sql.append(" AND STATE = ? ");
            	pa.set("", dict.getString("STATE"));
            }
        if(dict.getLong("USER_ID")!=null){
        	sql.append(" AND USER_ID = ? ");
        	pa.set("", dict.getLong("USER_ID"));
        }
        if(dict.getLong("ID")!=null){
        	sql.append(" AND ID = ? ");
        	pa.set("", dict.getLong("ID"));
        }
        if(dict.getString("GLSM")!=null && dict.getString("GLSM").trim().length()>0)
		{
        	sql.append(" AND lower(GLSM) like '%"+dict.getString("GLSM").toLowerCase().trim()+"%' ");
		}
        if(dict.getString("GLBZ")!=null && dict.getString("GLBZ").trim().length()>0)
		{
        	sql.append(" AND lower(GLBZ) like '%"+dict.getString("GLBZ").toLowerCase().trim()+"%' ");
		}
        if(dict.getString("GLBM")!=null && dict.getString("GLBM").trim().length()>0)
		{
        	sql.append(" AND lower(GLBM) like '%"+dict.getString("GLBM").toLowerCase().trim()+"%' ");
		}
         
        sql.append(" ORDER BY ID DESC ");
 
        return (ArrayList<DynamicDict>) this.query(sql.toString(), pa, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
								  Object para) throws SQLException, BaseAppException {
                ArrayList<DynamicDict> list = new ArrayList<DynamicDict>();
                DynamicDict temp = null;
                while(rs.next()) {
                    temp = new DynamicDict();
                    int flag = 1;
                    temp.set("ID", rs.getObject(flag++));
                    temp.set("GLSM", rs.getObject(flag++));
                    temp.set("GLBZ", rs.getObject(flag++));
                    temp.set("GLBM", rs.getObject(flag++));
                    temp.set("STATE", rs.getObject(flag++));
                    temp.set("CREATE_DATE", rs.getObject(flag++));
                    temp.set("USER_ID", rs.getObject(flag++));
                    list.add(temp);
                }
                return list;
            }
        });
	}

	public List<DynSelectDataDto> querySelectDataAllList(DynamicDict dict)
			throws BaseAppException {
		// TODO Auto-generated method stub
		ParamMap pm = new ParamMap();
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder
				.append(" SELECT   ID, GLSM, GLBZ, GLBM, GLCONTENT, STATE, CREATE_DATE, USER_ID ");
		sqlbuilder.append(" FROM BFM_DYN_SELECT_DATA ");
		sqlbuilder.append(" WHERE  1=1 ");
		if(dict.getLong("ID")!=null){
		  sqlbuilder.append(" AND ID=:ID ");
		  pm.set("ID", dict.getLong("ID"));
		}
		if(dict.getString("GLSM")!=null && dict.getString("GLSM").trim().length()>0)
		{
			sqlbuilder.append(" AND lower(GLSM) = '"+dict.getString("GLSM").toLowerCase().trim()+"' ");
		}
        if(dict.getString("GLBZ")!=null && dict.getString("GLBZ").trim().length()>0)
		{
        	sqlbuilder.append(" AND lower(GLBZ) = '"+dict.getString("GLBZ").toLowerCase().trim()+"' ");
		}
        if(dict.getString("GLBM")!=null && dict.getString("GLBM").trim().length()>0)
		{
        	sqlbuilder.append(" AND lower(GLBM) = '"+dict.getString("GLBM").toLowerCase().trim()+"' ");
		}
		
		sqlbuilder.append("  ORDER BY ID DESC");
		return this.selectList(sqlbuilder.toString(), DynSelectDataDto.class, pm);
	}

	public boolean isHasSelectData(DynamicDict dict) throws BaseAppException {
		// TODO Auto-generated method stub
		ParamMap pm = new ParamMap();
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder
				.append(" select count(*) cnt  ");
		sqlbuilder.append(" from BFM_DYN_SELECT_DATA  ");
		sqlbuilder.append(" where 1=1 ");
		if(dict.getString("GLSM")!=null && dict.getString("GLSM").trim().length()>0)
		{
			sqlbuilder.append(" AND lower(GLSM) = '"+dict.getString("GLSM").toLowerCase().trim()+"' ");
		}
        if(dict.getString("GLBZ")!=null && dict.getString("GLBZ").trim().length()>0)
		{
        	sqlbuilder.append(" AND lower(GLBZ) = '"+dict.getString("GLBZ").toLowerCase().trim()+"' ");
		}
        if(dict.getString("GLBM")!=null && dict.getString("GLBM").trim().length()>0)
		{
        	sqlbuilder.append(" AND lower(GLBM) = '"+dict.getString("GLBM").toLowerCase().trim()+"' ");
		}
		
		return (boolean) this.query(sqlbuilder.toString(), pm, null, null, new RowSetMapper<Boolean>() {
            public Boolean mapRows(RowSetOperator op, ResultSet rs, int colNum,
                    Object para) throws SQLException, BaseAppException {
            	
            	long count = 0;
                
                if (rs.next()) 
                {
                	count = op.getLong(rs, 1);
                }
                if(count>0)return true;
                else return false;
            }
        });
		
	}

}
