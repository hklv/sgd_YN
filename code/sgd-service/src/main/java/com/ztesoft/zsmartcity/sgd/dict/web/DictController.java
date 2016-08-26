package com.ztesoft.zsmartcity.sgd.dict.web;

import com.ztesoft.sgd.base.web.ActionSupport;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmartcity.sgd.dict.domain.DictData;
import com.ztesoft.zsmartcity.sgd.dict.service.DictService;

/**
 * 字典控制层.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-17 14:18
 */
public class DictController extends ActionSupport {
    private DictService dictService = null;
    public DictController() throws BaseAppException {
        this.dictService = new DictService();
    }
    @Override
    public int perform(DynamicDict dict) throws BaseAppException {
        String serviceName = dict.serviceName;
        if ("QryDictList".equalsIgnoreCase(serviceName)) {
            return qryDictList(dict);
        } else if ("AddDict".equalsIgnoreCase(serviceName)) {
            return addDict(dict);
        } else if ("DeleteDict".equalsIgnoreCase(serviceName)) {
            return deleteDict(dict);
        } else if ("UpdateDict".equalsIgnoreCase(serviceName)) {
            return updateDict(dict);
        } else if ("QryDictData".equalsIgnoreCase(serviceName)) {
            return qryDictData(dict);
        } else if ("AddDictData".equalsIgnoreCase(serviceName)) {
            return addDictData(dict);
        } else if ("UpdateDictData".equalsIgnoreCase(serviceName)) {
            return updateDictData(dict);
        } else if ("DelDictData".equalsIgnoreCase(serviceName)) {
            return delDictData(dict);
        } else if("QrySingleDicData".equalsIgnoreCase(serviceName)){
            return qrySingleDicData(dict);
        }else {
            String msg = StringUtil.stringFormat("BO.serviceName = [{0}] can not match method.", dict.getServiceName(),
                    serviceName);
            throw new BaseAppException("S-ACT-00203", msg, BaseAppException.INNER_ERROR);
        }

    }

    private int qryDictList(DynamicDict dict) throws BaseAppException {
        dictService.qryDictList(dict);
        return 0;
    }

    private int addDict(DynamicDict dict) throws BaseAppException {
        return dictService.addDict(dict);
    }

    private int deleteDict(DynamicDict dict) throws BaseAppException {
        return dictService.deleteDict(dict);
    }

    private int updateDict(DynamicDict dict) throws BaseAppException {
        return dictService.updateDict(dict);
    }

    private int qryDictData(DynamicDict dict) throws BaseAppException {
        dictService.qryDictData(dict);
        BoHelper.listDtoToBO("z_d_r", dictService.qryDictData(dict), DictData.class, dict);
        return 0;
    }

    private int addDictData(DynamicDict dict) throws BaseAppException {
        return dictService.addDictData(dict);
    }

    private int updateDictData(DynamicDict dict) throws BaseAppException {
        return dictService.updateDictData(dict);
    }

    private int delDictData(DynamicDict dict) throws BaseAppException {
        return dictService.delDictData(dict);
    }

    private  int qrySingleDicData(DynamicDict dict) throws BaseAppException {
        return dictService.qrySingleDicData(dict);
    }
}
