package com.ztesoft.zsmartcity.sgd.accept.web;

import com.ztesoft.sgd.base.web.ActionSupport;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmartcity.sgd.accept.service.AcceptService;

/**
 * 事项受理
 * <p/>
 * Created by ZhangLu on 2016/6/15.
 */
public class AcceptController extends ActionSupport {
    private AcceptService serv = null;
    private static ZSmartLogger logger = ZSmartLogger.getLogger(AcceptController.class);
    public AcceptController() throws BaseAppException {
        if (serv == null) {
            serv = new AcceptService();
        }
    }

    @Override
    public int perform(DynamicDict dict) throws BaseAppException {
        String serviceName = dict.serviceName;
        if ("QryItemTree".equalsIgnoreCase(serviceName)) {
            return qryItemTree(dict);
        } else if ("SaveAcception".equalsIgnoreCase(serviceName)) {
            return saveAcception(dict);
        } else if ("QueryApplyInfo".equalsIgnoreCase(serviceName)) {
            return queryApplyInfo(dict);
        } else if("QryPrivateNetWork".equalsIgnoreCase(serviceName)){
            return qryPrivateNetWork(dict);
        }else {
            String msg = StringUtil.stringFormat("BO.serviceName = [{0}] can not match method.", dict.getServiceName(),
                    serviceName);
            throw new BaseAppException("S-ACT-00203", msg, BaseAppException.INNER_ERROR);
        }
    }

    /**
     * 查询事项目录树
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    private int qryItemTree(DynamicDict dict) throws BaseAppException {
        logger.debug("call controller qryItemTree..........");
        return serv.qryItemTree(dict);
    }

    /**
     * 事项受理
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    private int saveAcception(DynamicDict dict) throws BaseAppException {
        logger.debug("call controller saveAcception..........");
        return serv.acceptItems(dict);
    }

    /**
     * 查询申请信息详情
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    private int queryApplyInfo(DynamicDict dict) throws BaseAppException {
        logger.debug("call controller queryApplyInfo..........");
        return serv.queryApplyInfo(dict);
    }

    /**
     * 查询专线
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int qryPrivateNetWork(DynamicDict dict) throws BaseAppException {
        logger.debug("call controller qryPrivateNetWork..........");
        return serv.qryPrivateNetWork(dict);
    }
}
