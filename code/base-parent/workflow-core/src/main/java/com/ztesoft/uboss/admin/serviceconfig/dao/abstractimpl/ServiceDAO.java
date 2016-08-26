package com.ztesoft.uboss.admin.serviceconfig.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.uboss.admin.serviceconfig.dao.IServiceDAO;
import com.ztesoft.uboss.admin.serviceconfig.model.ServiceScopeDefinition;
import com.ztesoft.uboss.admin.serviceconfig.model.dto.TfmServiceScopeDefineDto;
import com.ztesoft.uboss.admin.serviceconfig.model.dto.TfmServicesDto;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.*;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Modified by ZEN 2012-4-27
 */
public class ServiceDAO extends BaseDAO implements IServiceDAO {
    private static ZSmartLogger logger = ZSmartLogger
            .getLogger(ServiceDAO.class);

    /**
     * 归档更新序列
     */
    public static Long getArcNoSeq() throws BaseAppException {
        return SeqUtil.getBackServiceDBUtil().longValue("ARC_NO_SEQ");
    }

    // 服务管理

    /**
     * 查询目录下的所有服务
     */
    public List selectTfmServicesByCat(String catCode) throws BaseAppException {
        logger.debug("selectTfmServicesByCat(catCode=" + catCode + ")");
        AssertUtil.isNotNull(catCode, "catCode is null");
        StringBuffer sql = new StringBuffer(
                " SELECT B.SERVICE_NAME,B.SERVICE_TYPE,B.MODIFIER,B.MODIFY_DATE,B.DEFINITION,B.STATE,B.CACHE_FLAG,B.VERSION")
                .append("  FROM TFM_SERVICE_CAT_LIST A ,TFM_SERVICES B ")
                .append(" WHERE A.SERVICE_NAME = B.SERVICE_NAME ").append(
                        " AND A.CAT_CODE =:catCode ").append(
                        " ORDER BY B.SERVICE_TYPE,B.SERVICE_NAME");

        ParamMap pa = new ParamMap();
        pa.set("catCode", catCode);
        return (List) query(sql.toString(), pa, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
                                  Object para) throws SQLException, BaseAppException {
                List TfmServices = new ArrayList();
                while (rs.next()) {
                    TfmServicesDto dto = new TfmServicesDto();
                    dto.setServiceName(op.getString(rs, "SERVICE_NAME"));
                    dto.setServiceType(op.getString(rs, "SERVICE_TYPE"));
                    dto.setModifier(op.getString(rs, "MODIFIER"));
                    dto.setModifyDate(op.getDate(rs, "MODIFY_DATE"));
                    dto.setDefinition(op.getString(rs, "DEFINITION"));
                    dto.setState(op.getString(rs, "STATE"));
                    dto.setCacheFlag(op.getString(rs, "CACHE_FLAG"));
                    dto.setVersion(op.getString(rs, "VERSION"));
                    TfmServices.add(dto);
                }
                return TfmServices;
            }
        });
    }

    /**
     * 查询某一状态的所有服务
     *
     * @param state
     * @return
     * @throws BaseAppException
     */
    public ArrayList selectServicesByState(String state)
            throws BaseAppException {
        logger.debug("selectServicesByState(state=" + state + ")");
        AssertUtil.isNotNull(state, "catCode is null");
        String sql = " SELECT A.SERVICE_NAME,A.SERVICE_TYPE "
                + "  FROM TFM_SERVICES A " + " WHERE A.STATE = :state ";

        ParamMap pa = new ParamMap();
        pa.set("state", state);
        return (ArrayList) query(sql, pa, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
                                  Object para) throws SQLException, BaseAppException {
                ArrayList TfmServices = new ArrayList();
                while (rs.next()) {
                    TfmServicesDto dto = new TfmServicesDto();
                    dto.setServiceName(op.getString(rs, "SERVICE_NAME"));
                    dto.setServiceType(op.getString(rs, "SERVICE_TYPE"));
                    TfmServices.add(dto);
                }
                return TfmServices;
            }
        });
    }

    /**
     * 查询所有未关联目录服务
     *
     * @return
     * @throws BaseAppException
     */
    public ArrayList selectAloneServices() throws BaseAppException {
        logger.debug("selectAloneServices()");
        StringBuffer sql = new StringBuffer(
                " SELECT A.SERVICE_NAME,A.SERVICE_TYPE,A.MODIFIER,A.MODIFY_DATE ")
                .append("  FROM TFM_SERVICES A ").append(
                        " WHERE A.SERVICE_NAME NOT IN ").append(
                        " (SELECT B.SERVICE_NAME FROM TFM_SERVICE_CAT_LIST B)")
                .append(" ORDER BY A.SERVICE_TYPE,A.SERVICE_NAME");

        return (ArrayList) query(sql.toString(), null, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        ArrayList TfmServices = new ArrayList();
                        while (rs.next()) {
                            TfmServicesDto dto = new TfmServicesDto();
                            dto.setServiceName(op
                                    .getString(rs, "SERVICE_NAME"));
                            dto.setServiceType(op
                                    .getString(rs, "SERVICE_TYPE"));
                            dto.setModifier(op.getString(rs, "MODIFIER"));
                            dto.setModifyDate(op.getDate(rs, "MODIFY_DATE"));
                            TfmServices.add(dto);
                        }
                        return TfmServices;
                    }
                });
    }

    /**
     * 判断服务是否被组合服务引用
     *
     * @param servName
     */
    public int getServRefCount(String servName) throws BaseAppException {
        logger.debug("getServRefCount(servName=" + servName + ")");
        AssertUtil.isNotNull(servName, "servName is null");
        StringBuffer sql = new StringBuffer(" SELECT COUNT(*) AS COUNT ")
                .append(" FROM TFM_SERV_RELATION A ").append(
                        " WHERE A.SUB_SERVICE_NAME=:servName ").append(
                        " OR A.COND_SERVICE_NAME=:servName");

        ParamMap pa = new ParamMap();
        pa.set("servName", servName);
        Integer retVal = (Integer) query(sql.toString(), pa, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        if (rs.next()) {
                            return op.getInteger(rs, "COUNT");
                        }
                        return new Integer(0);
                    }
                });
        return retVal.intValue();
    }

    /**
     * 获取服务信息
     *
     * @param servName 服务名
     */
    public TfmServicesDto queryTfmServices(String servName)
            throws BaseAppException {
        logger.debug("queryTfmServices(servName=" + servName + ")");
        AssertUtil.isNotNull(servName, "servName is null");
        StringBuffer sql = new StringBuffer();
        sql
                .append(" SELECT A.SERVICE_NAME,A.ENV_ID,A.ENV_DESC,A.ENV_NAME,A.ENV_TYPE,A.VERSION,\r\n");
        sql
                .append("				A.SERVICE_DESC,A.SERVICE_TYPE,A.DEFINITION,A.STATE,A.CACHE_FLAG,\r\n");
        sql.append("        A.MODIFIER,A.MODIFY_DATE,A.MODULE_NAME \r\n");
        sql.append(" FROM TFM_SERVICES A WHERE A.SERVICE_NAME =:servName");

        ParamMap pa = new ParamMap();
        pa.set("servName", servName);
        return (TfmServicesDto) query(sql.toString(), pa, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        if (rs.next()) {
                            TfmServicesDto dto = new TfmServicesDto();
                            dto.setCacheFlag(op.getString(rs, "CACHE_FLAG"));
                            dto.setDefinition(op.getString(rs, "DEFINITION"));
                            dto.setEnvDesc(op.getString(rs, "ENV_DESC"));
                            dto.setEnvId(op.getString(rs, "ENV_ID"));
                            dto.setEnvName(op.getString(rs, "ENV_NAME"));
                            dto.setEnvType(op.getString(rs, "ENV_TYPE"));
                            dto.setServiceDesc(op
                                    .getString(rs, "SERVICE_DESC"));
                            dto.setServiceName(op
                                    .getString(rs, "SERVICE_NAME"));
                            dto.setServiceType(op
                                    .getString(rs, "SERVICE_TYPE"));
                            dto.setState(op.getString(rs, "STATE"));
                            dto.setVersion(op.getString(rs, "VERSION"));
                            dto.setModifier(op.getString(rs, "MODIFIER"));
                            dto.setModifyDate(op.getDate(rs, "MODIFY_DATE"));
                            dto.setModuleName(op.getString(rs, "MODULE_NAME"));
                            return dto;
                        }
                        return null;
                    }
                });
    }

    /**
     * 新增服务
     */
    public int insertTfmServices(TfmServicesDto tfmServicesDto)
            throws BaseAppException {
        logger.debug("insertTfmServices(tfmServicesDto),ServiceName="
                + tfmServicesDto.getServiceName());
        AssertUtil.isNotNull(tfmServicesDto.getServiceName(),
                "Service_name is null");
        StringBuffer sql = new StringBuffer();
        sql
                .append("INSERT INTO TFM_SERVICES (SERVICE_NAME, ENV_ID, ENV_DESC, ENV_NAME,\r\n");
        sql
                .append("			 ENV_TYPE, VERSION, SERVICE_DESC, SERVICE_TYPE, DEFINITION,\r\n");
        sql
                .append("       STATE, CACHE_FLAG, MODIFIER, MODIFY_DATE, MODULE_NAME) \r\n");
        sql
                .append("				 VALUES(:service_name,:env_id,:env_desc,:env_name,\r\n");
        sql
                .append("         		:env_type,:version,:service_desc,:service_type,:definition,\r\n");
        sql
                .append("				:state,:cache_flag,:modifier,:modify_date,:module_name)");

        ParamMap pm = new ParamMap();
        pm.set("cache_flag", tfmServicesDto.getCacheFlag());
        pm.set("definition", tfmServicesDto.getDefinition());
        pm.set("env_desc", tfmServicesDto.getEnvDesc());
        pm.set("env_id", tfmServicesDto.getEnvId());
        pm.set("env_name", tfmServicesDto.getEnvName());
        pm.set("env_type", tfmServicesDto.getEnvType());
        pm.set("service_desc", tfmServicesDto.getServiceDesc());
        pm.set("service_name", tfmServicesDto.getServiceName());
        pm.set("service_type", tfmServicesDto.getServiceType());
        pm.set("state", tfmServicesDto.getState());
        pm.set("version", tfmServicesDto.getVersion());
        pm.set("modifier", tfmServicesDto.getModifier());
        pm.set("modify_date", tfmServicesDto.getModifyDate());
        pm.set("module_name", tfmServicesDto.getModuleName());

        return executeUpdate(sql.toString(), pm);
    }

    /**
     * 设置服务状态
     *
     * @param ServName 服务名
     * @param state    服务状态
     */
    public int updateTfmServState(String ServName, String state)
            throws BaseAppException {
        logger.debug("updateTfmServState(ServName=" + ServName + ",state="
                + state + ")");
        AssertUtil.isNotNull(ServName, "ServName is null");
        AssertUtil.isNotNull(state, "state is null");
        String sql = "UPDATE TFM_SERVICES A SET A.STATE=:state "
                + " WHERE A.SERVICE_NAME=:ServName";

        ParamMap pm = new ParamMap();
        pm.set("state", state);
        pm.set("ServName", ServName);
        return executeUpdate(sql, pm);
    }

    /**
     * 删除服务
     *
     * @param servName
     * @return
     * @throws BaseAppException
     */
    public int deleteTfmService(String servName) throws BaseAppException {
        logger.debug("deleteTfmService(ServName=" + servName + ")");
        AssertUtil.isNotNull(servName, "ServName is null");
        String sql = "DELETE FROM TFM_SERVICES "
                + " WHERE SERVICE_NAME=:ServName";

        ParamMap pm = new ParamMap();
        pm.set("ServName", servName);
        return executeUpdate(sql, pm);
    }

    /**
     * 更新服务描述blob字段
     *
     * @param servName
     * @param blob
     * @return
     * @throws BaseAppException
     */
//	public int updateServDefXml(String servName, BLOB blob)
//			throws BaseAppException {
//		AssertUtil.isNotNull(servName, "servName is null");
//		AssertUtil.isNotNull(blob, "blob is null");
//		logger.debug("updateServDefXml(servName=" + servName + ")");
//		String sql = " UPDATE TFM_SERVICES A SET A.SERVICE_DEF_XML=:blob "
//				+ " WHERE A.SERVICE_NAME=:servName ";
//		ParamMap pm = new ParamMap();
//		pm.set("servName", servName);
//		pm.set("blob", blob);
//		return executeUpdate(sql, pm);
//	}

    /**
     * 获取服务描述blob字段
     *
     * @param servName
     * @return
     * @throws BaseAppException
     */
    public String getServDefXml(String servName) throws BaseAppException {
        AssertUtil.isNotNull(servName, "servName is null");
        String sql = " SELECT A.SERVICE_DEF_XML FROM TFM_SERVICES A  "
                + " WHERE A.SERVICE_NAME =:servName ";
        ParamMap pm = new ParamMap();
        pm.set("servName", servName);
        return (String) query(sql, pm, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
                                  Object para) throws SQLException, BaseAppException {
                if (rs.next()) {
                    Blob blob = (rs).getBlob("SERVICE_DEF_XML");
                    if (blob != null) {
                        byte[] bytes = blob.getBytes(1, (int) blob.length());
                        if (bytes != null)
                            try {
                                return new String(bytes, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                throw new BaseAppException("", e);
                            }
                    }
                }
                return null;
            }
        });
    }

    public int updateTfmServiceAll(TfmServicesDto tfmServicesDto)
            throws BaseAppException {
        AssertUtil.isNotNull(tfmServicesDto.getServiceName(),
                "servName is null");
        logger.debug("updateTfmServiceAll(ServName="
                + tfmServicesDto.getServiceName() + ")");
        StringBuffer sqlStr = new StringBuffer();
        sqlStr
                .append("UPDATE TFM_SERVICES T SET T.SERVICE_NAME=T.SERVICE_NAME \r\n");
        sqlStr.append(" ,T.ENV_ID=:env_id\r\n");
        sqlStr.append(" ,T.ENV_DESC=:env_desc\r\n");
        sqlStr.append(" ,T.ENV_NAME=:env_name\r\n");
        sqlStr.append(" ,T.ENV_TYPE=:env_type\r\n");
        sqlStr.append(" ,T.VERSION=:version\r\n");
        sqlStr.append(" ,T.SERVICE_DESC=:service_desc\r\n");
        sqlStr.append(" ,T.SERVICE_TYPE=:service_type\r\n");
        sqlStr.append(" ,T.DEFINITION=:definition\r\n");
        sqlStr.append(" ,T.STATE=:state\r\n");
        sqlStr.append(" ,T.CACHE_FLAG=:cache_flag\r\n");
        sqlStr.append(" ,T.MODIFIER=:modifier\r\n");
        sqlStr.append(" ,T.MODIFY_DATE=:modify_date\r\n");
        sqlStr.append(" ,T.MODULE_NAME=:module_name\r\n");
        sqlStr.append(" WHERE T.SERVICE_NAME=:service_name");
        ParamMap pm = new ParamMap();
        pm.set("env_id", tfmServicesDto.getEnvId());
        pm.set("env_desc", tfmServicesDto.getEnvDesc());
        pm.set("env_name", tfmServicesDto.getEnvName());
        pm.set("env_type", tfmServicesDto.getEnvType());
        pm.set("version", tfmServicesDto.getVersion());
        pm.set("service_desc", tfmServicesDto.getServiceDesc());
        pm.set("service_type", tfmServicesDto.getServiceType());
        pm.set("definition", tfmServicesDto.getDefinition());
        pm.set("state", tfmServicesDto.getState());
        pm.set("cache_flag", tfmServicesDto.getCacheFlag());
        pm.set("modifier", tfmServicesDto.getModifier());
        pm.set("modify_date", tfmServicesDto.getModifyDate());
        pm.set("module_name", tfmServicesDto.getModuleName());
        pm.set("service_name", tfmServicesDto.getServiceName());
        return executeUpdate(sqlStr.toString(), pm);
    }

    public int updateServiceName(String oldServName, TfmServicesDto newServDto)
            throws BaseAppException {
        logger.debug("updateServiceName(oldServName={},newServName={}", oldServName, newServDto.getServiceName());
        AssertUtil.isNotNull(oldServName,
                "oldServName is null");
        AssertUtil.isNotNull(newServDto.getServiceName(),
                "newServName is null");
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" UPDATE TFM_SERVICES T \r\n");
        sqlStr.append(" SET T.SERVICE_NAME=:newServName,T.MODIFIER=:modifier,T.MODIFY_DATE=:modifyDate\r\n");
        sqlStr.append(" WHERE T.SERVICE_NAME=:oldServName");
        ParamMap pm = new ParamMap();
        pm.set("oldServName", oldServName);
        pm.set("newServName", newServDto.getServiceName());
        pm.set("modifier", newServDto.getModifier());
        pm.set("modifyDate", newServDto.getModifyDate());
        return executeUpdate(sqlStr.toString(), pm);
    }

    /**
     * 修改服务的目录
     *
     * @param serviceName
     * @param catCode
     * @return
     * @throws BaseAppException
     */
    public int modeServiceCat(String serviceName, String catCode)
            throws BaseAppException {
        AssertUtil.isNotNull(serviceName, "ServiceName is null");
        AssertUtil.isNotNull(catCode, "catCode is null");
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" UPDATE TFM_SERVICE_CAT_LIST \r\n");
        sqlStr.append(" SET SERVICE_NAME=:SERVICE_NAME,CAT_CODE=:CAT_CODE\r\n");
        sqlStr.append(" WHERE SERVICE_NAME=:SERVICE_NAME");
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceName);
        pm.set("CAT_CODE", catCode);
        return executeUpdate(sqlStr.toString(), pm);
    }

    /**
     * 设置服务的目录
     *
     * @param serviceName
     * @param catCode
     * @return
     * @throws BaseAppException
     */
    public int insertServiceCat(String serviceName, String catCode)
            throws BaseAppException {
        AssertUtil.isNotNull(serviceName, "ServiceName is null");
        AssertUtil.isNotNull(catCode, "catCode is null");
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" INSERT INTO  TFM_SERVICE_CAT_LIST VALUES (:SERVICE_NAME,:CAT_CODE) ");
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceName);
        pm.set("CAT_CODE", catCode);
        return executeUpdate(sqlStr.toString(), pm);
    }

    @SuppressWarnings("unchecked")
    public List<ServiceScopeDefinition> loadServiceScopeDefinition(
            String svcName) throws BaseAppException {
        String sqlStr = "SELECT \r\n "
                + " ID,SERVICE_TYPE SERVICE_NAME,NAME,STATE,STATE_DATE,CRATE_DATE \r\n "
                + " FROM TFM_SERVICE_SCOPE_DEFINE a \r\n "
                + " WHERE SERVICE_TYPE=:SERVICE_NAME   \r\n ";
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", svcName);

        return (List<ServiceScopeDefinition>) query(sqlStr, pm, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        List<ServiceScopeDefinition> scopeDefList = new ArrayList<ServiceScopeDefinition>();
                        while (rs.next()) {
                            TfmServiceScopeDefineDto dto = new TfmServiceScopeDefineDto();
                            dto.setCreateDate(op.getDate(rs, "CRATE_DATE"));
                            dto.setId(op.getLong(rs, "ID"));
                            dto.setScopeName(op.getString(rs, "NAME"));
                            dto.setServiceName(op.getString(rs, "SERVICE_NAME"));
                            dto.setState(op.getString(rs, "STATE"));
                            dto.setStateDate(op.getDate(rs, "STATE_DATE"));

                            ServiceScopeDefinition bean = new ServiceScopeDefinition(
                                    dto);
                            scopeDefList.add(bean);
                        }
                        return scopeDefList;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryAllServices() throws BaseAppException {
        String sql = "select a.SERVICE_NAME,a.SERVICE_NAME,a.CAT_CODE,b.definition from TFM_SERVICE_CAT_LIST a ,TFM_SERVICES b where a.SERVICE_NAME=b.SERVICE_NAME and b.state='1'";
        return (List<DynamicDict>) query(sql, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict service = new DynamicDict();
                            service.setValueByName("id", "service_" + op.getString(rs, dbloop++));
                            service.setValueByName("name", op.getString(rs, dbloop++));
                            service.setValueByName("parent", op.getString(rs, dbloop++));
                            service.setValueByName("definition", op.getString(rs, dbloop++));
                            list.add(service);
                        }
                        return list;
                    }

                });
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryServiceListByCat(String catCode, String serviceName, String definition) throws BaseAppException {
        ParamMap pm = new ParamMap();
        String sql = "select a.SERVICE_NAME,b.CAT_CODE,a.definition,a.service_type,a.version,a.state,a.service_desc,a.cache_flag,IS_RESERVED from TFM_SERVICES a "
                + " LEFT JOIN TFM_SERVICE_CAT_LIST b on a.SERVICE_NAME=b.SERVICE_NAME "
                + "where 1=1  [and b.cat_code=:cat_code] [and lower(a.service_name) like :service_name] [and lower(a.definition) like :definition]";

        ParamMapHelper.setValue(pm, "cat_code", catCode);
        if (StringUtil.isNotEmpty(serviceName)) {
            ParamMapHelper.setValue(pm, "service_name", "%" + serviceName.toLowerCase() + "%");
        }
        if (StringUtil.isNotEmpty(definition)) {
            ParamMapHelper.setValue(pm, "definition", "%" + definition.toLowerCase() + "%");
        }
        return (List<DynamicDict>) query(sql, pm, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict service = new DynamicDict();
                            service.setValueByName("SERVICE_NAME", op.getString(rs, dbloop++));
                            service.setValueByName("CAT_CODE", op.getString(rs, dbloop++));
                            service.setValueByName("DEFINITION", op.getString(rs, dbloop++));
                            service.setValueByName("SERVICE_TYPE", op.getString(rs, dbloop++));
                            service.setValueByName("VERSION", op.getString(rs, dbloop++));
                            service.setValueByName("STATE", op.getString(rs, dbloop++));
                            service.setValueByName("SERVICE_DESC", op.getString(rs, dbloop++));
                            service.setValueByName("CACHE_FLAG", op.getString(rs, dbloop++));
                            service.setValueByName("IS_RESERVED", op.getString(rs, dbloop++));
                            list.add(service);
                        }
                        return list;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryServiceListNotInCat(String catCode) throws BaseAppException {
        ParamMap pm = new ParamMap();
        String sql = "select a.SERVICE_NAME,b.CAT_CODE,a.definition,a.service_type,a.version,a.state,a.service_desc,a.cache_flag,IS_RESERVED from TFM_SERVICES a "
                + " LEFT JOIN TFM_SERVICE_CAT_LIST b on a.SERVICE_NAME=b.SERVICE_NAME "
                + "where b.cat_code is null ";

        pm.set("cat_code", catCode);
        return (List<DynamicDict>) query(sql, pm, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {

                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict service = new DynamicDict();
                            service.setValueByName("SERVICE_NAME", op.getString(rs, dbloop++));
                            service.setValueByName("CAT_CODE", op.getString(rs, dbloop++));
                            service.setValueByName("DEFINITION", op.getString(rs, dbloop++));
                            service.setValueByName("SERVICE_TYPE", op.getString(rs, dbloop++));
                            service.setValueByName("VERSION", op.getString(rs, dbloop++));
                            service.setValueByName("STATE", op.getString(rs, dbloop++));
                            service.setValueByName("SERVICE_DESC", op.getString(rs, dbloop++));
                            service.setValueByName("CACHE_FLAG", op.getString(rs, dbloop++));
                            service.setValueByName("IS_RESERVED", op.getString(rs, dbloop++));
                            list.add(service);
                        }
                        return list;
                    }

                });
    }

    public int delServiceMethodByServiceName(String serviceName) throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("DELETE FROM TFM_SERVICES_METHOD WHERE SERVICE_NAME =  ?");
        ParamArray pa = new ParamArray();
        pa.set("", serviceName);
        return executeUpdate(sqlStr.toString(), pa);
    }

    public int[] addServiceMethod(DynamicDict serviceMethods) throws BaseAppException {
        String sqlStr = "INSERT INTO TFM_SERVICES_METHOD (SERVICE_NAME,METHOD_NAME) VALUES (:SERVICE_NAME,:METHOD_NAME)";
        ArrayList<DynamicDict> methodList = (ArrayList<DynamicDict>) serviceMethods.getList("METHOD_LIST");
        ParamObject[] paraList = ParamObject.newParamObjectList(2, methodList.size());
        for (int i = 0; i < methodList.size(); i++) {
            DynamicDict dict2 = (DynamicDict) methodList.get(i);
            paraList[0].setBatchElement("SERVICE_NAME", dict2.getString("SERVICE_NAME"), i);
            paraList[1].setBatchElement("METHOD_NAME", dict2.getString("METHOD_NAME"), i);
        }
        return executeBatch(sqlStr.toString(), ParamMap.wrap(paraList));
    }

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryMethodListByService(String catCode)
            throws BaseAppException {
        String sql = "SELECT SERVICE_NAME,METHOD_NAME,COMMENTS FROM TFM_SERVICES_METHOD WHERE SERVICE_NAME=:SERVICE_NAME";
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", catCode);
        return (List<DynamicDict>) query(sql, pm, null, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict service = new DynamicDict();
                            service.setValueByName("SERVICE_NAME", op.getString(rs, dbloop++));
                            service.setValueByName("METHOD_NAME", op.getString(rs, dbloop++));
                            service.setValueByName("COMMENTS", op.getString(rs, dbloop++));
                            list.add(service);
                        }
                        return list;
                    }

                });
    }

    @Override
    public List<DynamicDict> qryServiceParam(String serviceName)
            throws BaseAppException {

        String sql = "SELECT SERVICE_NAME,VAR_CODE,VAR_NAME,VAR_TYPE,DEFAULT_VALUE,SCOPE,COMMENTS FROM BPM_SERV_TASK_PARA WHERE SERVICE_NAME=:SERVICE_NAME";
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceName);

        return (List<DynamicDict>) query(sql, pm, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs,
                                  int colNum, Object para) throws SQLException,
                    BaseAppException {
                int dbloop = 1;
                List<DynamicDict> list = new ArrayList<DynamicDict>();
                while (rs.next()) {

                    dbloop = 1;
                    DynamicDict service = new DynamicDict();
                    service.setValueByName("SERVICE_NAME", op.getString(rs, dbloop++));
                    service.setValueByName("VAR_CODE", op.getString(rs, dbloop++));
                    service.setValueByName("VAR_NAME", op.getString(rs, dbloop++));
                    service.setValueByName("VAR_TYPE", op.getString(rs, dbloop++));
                    service.setValueByName("DEFAULT_VALUE", op.getString(rs, dbloop++));
                    service.setValueByName("SCOPE", op.getString(rs, dbloop++));
                    service.setValueByName("COMMENTS", op.getString(rs, dbloop++));
                    list.add(service);
                }
                return list;
            }

        });
    }

    @Override
    public int addServiceParam(DynamicDict serviceParam)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" INSERT INTO BPM_SERV_TASK_PARA (SERVICE_NAME, VAR_CODE, VAR_NAME, VAR_TYPE, DEFAULT_VALUE, SCOPE, COMMENTS)");
        sqlStr.append(" VALUES (:SERVICE_NAME,:VAR_CODE,:VAR_NAME,:VAR_TYPE,:DEFAULT_VALUE,:SCOPE,:COMMENTS)");

        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceParam.getString("SERVICE_NAME"));
        pm.set("VAR_CODE", serviceParam.getString("VAR_CODE"));
        pm.set("VAR_NAME", serviceParam.getString("VAR_NAME"));
        pm.set("VAR_TYPE", serviceParam.getString("VAR_TYPE"));
        pm.set("DEFAULT_VALUE", serviceParam.getString("DEFAULT_VALUE"));
        pm.set("SCOPE", serviceParam.getString("SCOPE"));
        pm.set("COMMENTS", serviceParam.getString("COMMENTS"));

        return executeUpdate(sqlStr.toString(), pm);
    }

    @Override
    public int editServiceParam(DynamicDict serviceParam)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" UPDATE BPM_SERV_TASK_PARA SET VAR_NAME = :VAR_NAME, VAR_TYPE = :VAR_TYPE, DEFAULT_VALUE = :DEFAULT_VALUE, SCOPE = :SCOPE, COMMENTS = :COMMENTS");
        sqlStr.append(" WHERE SERVICE_NAME = :SERVICE_NAME AND VAR_CODE = :VAR_CODE");

        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceParam.getString("SERVICE_NAME"));
        pm.set("VAR_CODE", serviceParam.getString("VAR_CODE"));
        pm.set("VAR_NAME", serviceParam.getString("VAR_NAME"));
        pm.set("VAR_TYPE", serviceParam.getString("VAR_TYPE"));
        pm.set("DEFAULT_VALUE", serviceParam.getString("DEFAULT_VALUE"));
        pm.set("SCOPE", serviceParam.getString("SCOPE"));
        pm.set("COMMENTS", serviceParam.getString("COMMENTS"));

        return executeUpdate(sqlStr.toString(), pm);
    }

    @Override
    public int delServiceParam(DynamicDict serviceParam)
            throws BaseAppException {

        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" DELETE FROM BPM_SERV_TASK_PARA WHERE SERVICE_NAME = :SERVICE_NAME AND VAR_CODE = :VAR_CODE");
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceParam.getString("SERVICE_NAME"));
        pm.set("VAR_CODE", serviceParam.getString("VAR_CODE"));

        return executeUpdate(sqlStr.toString(), pm);
    }

    @Override
    public List<DynamicDict> qryAllServicesMethods() throws BaseAppException {

        String sql = "SELECT SERVICE_NAME,METHOD_NAME,COMMENTS FROM TFM_SERVICES_METHOD";
        ParamMap pm = new ParamMap();
        return (List<DynamicDict>) query(sql, pm, null, null,
                new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs,
                                                     int colNum, Object para) throws SQLException,
                            BaseAppException {
                        int dbloop = 1;
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        while (rs.next()) {
                            dbloop = 1;
                            DynamicDict service = new DynamicDict();
                            service.setValueByName("SERVICE_NAME", op.getString(rs, dbloop++));
                            service.setValueByName("METHOD_NAME", op.getString(rs, dbloop++));
                            service.setValueByName("COMMENTS", op.getString(rs, dbloop++));
                            list.add(service);
                        }
                        return list;
                    }

                });
    }

    @Override
    public int[] insertTfmServicesBatch(List<TfmServicesDto> dtos)
            throws BaseAppException {

        Date now = DateUtil.GetDBDateTime();

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO TFM_SERVICES ");
        sql.append("(SERVICE_NAME,DEFINITION,MODIFY_DATE,VERSION,SERVICE_TYPE,STATE,CACHE_FLAG)");
        sql.append("VALUES ");
        sql.append("(:SERVICE_NAME,:DEFINITION,:MODIFY_DATE,:VERSION,:SERVICE_TYPE,:STATE,:CACHE_FLAG)");

        ParamObject[] paraList = ParamObject.newParamObjectList(7, dtos.size());
        TfmServicesDto tempDict = null;
        for (int i = 0, size = dtos.size(); i < size; i++) {

            tempDict = dtos.get(i);
            paraList[0].setBatchElement("SERVICE_NAME", tempDict.getServiceName(), i);
            paraList[1].setBatchElement("DEFINITION", tempDict.getDefinition(), i);
            paraList[2].setBatchElement("MODIFY_DATE", now, i);
            paraList[3].setBatchElement("VERSION", tempDict.getVersion(), i);
            paraList[4].setBatchElement("SERVICE_TYPE", tempDict.getServiceType(), i);
            paraList[5].setBatchElement("STATE", tempDict.getState(), i);
            paraList[6].setBatchElement("CACHE_FLAG", tempDict.getCacheFlag(), i);

        }

        return JdbcTemplate.executeBatch(JdbcUtil4SGD.getDefaultDbService(), sql.toString(), ParamMap.wrap(paraList));
    }

    @Override
    public int[] insertServiceCatBatch(List<TfmServicesDto> dtos, String catCode)
            throws BaseAppException {

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO  TFM_SERVICE_CAT_LIST VALUES (:SERVICE_NAME,:CAT_CODE) ");

        ParamObject[] paraList = ParamObject.newParamObjectList(2, dtos.size());
        TfmServicesDto tempDict = null;
        for (int i = 0, size = dtos.size(); i < size; i++) {

            tempDict = dtos.get(i);
            paraList[0].setBatchElement("SERVICE_NAME", tempDict.getServiceName(), i);
            paraList[1].setBatchElement("CAT_CODE", catCode, i);
        }

        return JdbcTemplate.executeBatch(JdbcUtil4SGD.getDefaultDbService(), sql.toString(), ParamMap.wrap(paraList));
    }

    @Override
    public int[] updateTfmServicesBatch(List<TfmServicesDto> dtos)
            throws BaseAppException {

        Date now = DateUtil.GetDBDateTime();

        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE TFM_SERVICES SET DEFINITION = :DEFINITION, MODIFY_DATE = :MODIFY_DATE, ");
        sql.append(" VERSION = :VERSION, STATE = :STATE, CACHE_FLAG = :CACHE_FLAG, SERVICE_TYPE = :SERVICE_TYPE ");
        sql.append(" WHERE SERVICE_NAME = :SERVICE_NAME ");

        ParamObject[] paraList = ParamObject.newParamObjectList(7, dtos.size());
        TfmServicesDto tempDict = null;
        for (int i = 0, size = dtos.size(); i < size; i++) {

            tempDict = dtos.get(i);
            paraList[0].setBatchElement("SERVICE_NAME", tempDict.getServiceName(), i);
            paraList[1].setBatchElement("DEFINITION", tempDict.getDefinition(), i);
            paraList[2].setBatchElement("MODIFY_DATE", now, i);
            paraList[3].setBatchElement("VERSION", tempDict.getVersion(), i);
            paraList[4].setBatchElement("STATE", tempDict.getState(), i);
            paraList[5].setBatchElement("CACHE_FLAG", tempDict.getCacheFlag(), i);
            paraList[6].setBatchElement("SERVICE_TYPE", tempDict.getServiceType(), i);
        }

        return JdbcTemplate.executeBatch(JdbcUtil4SGD.getDefaultDbService(), sql.toString(), ParamMap.wrap(paraList));
    }

    @Override
    public List<String> selCatServiceName(String catCode)
            throws BaseAppException {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SERVICE_NAME FROM TFM_SERVICE_CAT_LIST WHERE CAT_CODE = :CAT_CODE ");
        ParamMap pm = new ParamMap();
        pm.set("CAT_CODE", catCode);

        return query(sql.toString(), pm, null, null,
                new RowSetMapper<List<String>>() {
                    public List<String> mapRows(RowSetOperator op, ResultSet rs,
                                                int colNum, Object para) throws SQLException,
                            BaseAppException {

                        int dbloop = 1;
                        List<String> list = new ArrayList<String>();
                        while (rs.next()) {

                            dbloop = 1;
                            list.add(op.getString(rs, dbloop++));
                        }
                        return list;
                    }

                });
    }

    @Override
    public boolean isServiceMethod(String serviceName, String method)
            throws BaseAppException {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM TFM_SERVICES_METHOD WHERE SERVICE_NAME = :SERVICE_NAME AND METHOD_NAME = :METHOD_NAME ");
        ParamMap pm = new ParamMap();
        pm.set("SERVICE_NAME", serviceName);
        pm.set("METHOD_NAME", method);

        return query(sql.toString(), pm, null, null,
                new RowSetMapper<Boolean>() {
                    public Boolean mapRows(RowSetOperator op, ResultSet rs,
                                           int colNum, Object para) throws SQLException,
                            BaseAppException {

                        boolean ise = false;
                        if (rs.next()) {

                            ise = true;
                        }

                        return ise;
                    }

                });
    }

}
