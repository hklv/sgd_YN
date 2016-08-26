package com.ztesoft.uboss.admin.serviceconfig.dao;

import com.ztesoft.uboss.admin.serviceconfig.model.ServiceScopeDefinition;
import com.ztesoft.uboss.admin.serviceconfig.model.dto.TfmServicesDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.ArrayList;
import java.util.List;


/**
 * Modified by ZEN 2012-4-27
 */
public interface IServiceDAO {
    // 服务管理

    /**
     * 获取服务目录中的服务
     */
    List selectTfmServicesByCat(String catCode) throws BaseAppException;

    /**
     * 获取服务信息
     */
    TfmServicesDto queryTfmServices(String servName)
            throws BaseAppException;

    /**
     * 设置服务状态
     */
    int updateTfmServState(String ServName, String state)
            throws BaseAppException;

    /**
     * 创建一个服务
     */
    int insertTfmServices(TfmServicesDto tfmServicesDto)
            throws BaseAppException;

    /**
     * 更新服务描述blob字段
     */
//	public int updateServDefXml(String servName, BLOB blob)
//			throws BaseAppException;

    /**
     * 获取服务描述blob字段
     */
    String getServDefXml(String servName) throws BaseAppException;

    /**
     * 删除服务基本信息
     */
    int deleteTfmService(String servName) throws BaseAppException;

    /**
     * 判断服务是否被组合服务引用
     */
    int getServRefCount(String servName) throws BaseAppException;

    /**
     * 获取未放入目录的所有服务
     */
    ArrayList selectAloneServices() throws BaseAppException;

    /**
     * 更新服务
     */
    int updateTfmServiceAll(TfmServicesDto tfmServicesDto)
            throws BaseAppException;

    /**
     * 更新服务名
     */
    int updateServiceName(String oldServName, TfmServicesDto newServDto)
            throws BaseAppException;

    List<ServiceScopeDefinition> loadServiceScopeDefinition(
            String svcName) throws BaseAppException;

    List<DynamicDict> qryAllServices() throws BaseAppException;

    List<DynamicDict> qryServiceParam(String serviceName) throws BaseAppException;

    int addServiceParam(DynamicDict serviceParam) throws BaseAppException;

    int editServiceParam(DynamicDict serviceParam) throws BaseAppException;

    int delServiceParam(DynamicDict serviceParam) throws BaseAppException;

    List<DynamicDict> qryServiceListByCat(String catCode, String serviceName, String definition) throws BaseAppException;

    List<DynamicDict> qryMethodListByService(String catCode) throws BaseAppException;

    int modeServiceCat(String serviceName, String catCode) throws BaseAppException;

    int delServiceMethodByServiceName(String serviceName) throws BaseAppException;

    int[] addServiceMethod(DynamicDict serviceMethods) throws BaseAppException;

    int insertServiceCat(String serviceName, String catCode) throws BaseAppException;

    List<DynamicDict> qryServiceListNotInCat(String catCode) throws BaseAppException;

    List<DynamicDict> qryAllServicesMethods() throws BaseAppException;

    List<String> selCatServiceName(String catCode) throws BaseAppException;

    boolean isServiceMethod(String serviceName, String method) throws BaseAppException;

    int[] insertTfmServicesBatch(List<TfmServicesDto> dtos) throws BaseAppException;

    int[] updateTfmServicesBatch(List<TfmServicesDto> dtos) throws BaseAppException;

    int[] insertServiceCatBatch(List<TfmServicesDto> dtos, String catCode) throws BaseAppException;
}
