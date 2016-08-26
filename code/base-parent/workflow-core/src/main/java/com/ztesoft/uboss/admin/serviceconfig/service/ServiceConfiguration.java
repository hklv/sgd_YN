package com.ztesoft.uboss.admin.serviceconfig.service;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.admin.serviceconfig.dao.ICatDAO;
import com.ztesoft.uboss.admin.serviceconfig.dao.IServiceDAO;
import com.ztesoft.uboss.admin.serviceconfig.dao.abstractimpl.CatDAO;
import com.ztesoft.uboss.admin.serviceconfig.dao.abstractimpl.ServiceDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import utils.UbossActionSupport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServiceConfiguration extends UbossActionSupport {
    /**
     * 反射出所有方法到前台
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int getMethodsOfService(DynamicDict dict) throws BaseAppException {
        String className = dict.getString("DEFINITION");

        if (StringUtil.isEmpty(className)) {

            String serviceName = dict.getString("SERVICE_NAME");
            IServiceDAO serviceDao = SgdDaoFactory.getDaoImpl(ServiceDAO.class);
            className = serviceDao.queryTfmServices(serviceName.substring((serviceName.indexOf("_") + 1))).getDefinition();
        }

        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            ExceptionHandler.publish("", " don't has a class named " + className, e);
        }
        List<DynamicDict> methodList = new ArrayList<DynamicDict>();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            DynamicDict temp = new DynamicDict();
            temp.set("METHOD_NAME", method.getName());
            temp.set("RETURN_TYPE", method.getReturnType());
            methodList.add(temp);
        }
        dict.set("METHOD_LIST", methodList);
        return 0;
    }

    /**
     * 查询系统所有服务
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int getSysAllServices(DynamicDict dict) throws BaseAppException {

        List<DynamicDict> all = new ArrayList<DynamicDict>();

        //先查询服务目录树
        ICatDAO catDao = SgdDaoFactory.getDaoImpl(CatDAO.class);
        List<DynamicDict> catList = catDao.qryAllCatTree();

        //再查目录树下的所有服务
        IServiceDAO serviceDao = SgdDaoFactory.getDaoImpl(ServiceDAO.class);
        List<DynamicDict> serviceList = serviceDao.qryAllServices();

        //最后查目录下服务的所有方法
        all.addAll(catList);
        all.addAll(serviceList);
        dict.set("SERVICE_LIST", all);

        return 0;
    }

}
