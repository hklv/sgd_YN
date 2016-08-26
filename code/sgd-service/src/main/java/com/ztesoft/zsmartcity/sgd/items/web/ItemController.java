package com.ztesoft.zsmartcity.sgd.items.web;

import com.ztesoft.sgd.base.web.ActionSupport;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmartcity.sgd.items.domain.ItemMaterials;
import com.ztesoft.zsmartcity.sgd.items.service.ItemService;

/**
 * Created by liuhao 2016-5-25.
 * sgd
 */
public class ItemController extends ActionSupport {
    private ItemService serv = null;

    public ItemController() throws BaseAppException {
        this.serv = new ItemService();
    }

    @Override
    public int perform(DynamicDict dict) throws BaseAppException {
        String serviceName = dict.serviceName;
        if ("QryAllItems".equalsIgnoreCase(serviceName)) {
            return qryItemsByParams(dict);
        } else if ("AddItem".equalsIgnoreCase(serviceName)) {
            return addItem(dict);
        } else if ("UpdateItem".equalsIgnoreCase(serviceName)) {
            return updateItem(dict);
        } else if ("DeleteItem".equalsIgnoreCase(serviceName)) {
            return deleteItem(dict);
        } else if ("QryAllItemsMaterials".equalsIgnoreCase(serviceName)) {
            return qryAllItemsMaterials(dict);
        } else if ("AddItemMaterials".equalsIgnoreCase(serviceName)) {
            return addItemMaterials(dict);
        } else if ("UpdateItemMaterials".equalsIgnoreCase(serviceName)) {
            return updateItemMaterials(dict);
        } else if ("DeleteItemMaterials".equalsIgnoreCase(serviceName)) {
            return deleteItemMaterials(dict);
        } else if ("QryAllLaws".equalsIgnoreCase(serviceName)) {
            return qryAllLaws(dict);
        } else if ("AddLaws".equalsIgnoreCase(serviceName)) {
            return addLaws(dict);
        } else if ("UpdateLaws".equalsIgnoreCase(serviceName)) {
            return updateLaws(dict);
        } else if ("DeleteLaws".equalsIgnoreCase(serviceName)) {
            return deleteLaws(dict);
        } else if ("QryMaterialsByItemId".equalsIgnoreCase(serviceName)) {
            return qryMaterialsByItemId(dict);
        } else {
            String msg = StringUtil.stringFormat("BO.serviceName = [{0}] can not match method.", dict.getServiceName(),
                    serviceName);
            throw new BaseAppException("S-ACT-00203", msg, BaseAppException.INNER_ERROR);
        }

    }


    private int qryItemsByParams(DynamicDict dict) throws BaseAppException {
        serv.qryItemsByParams(dict);
        return 0;
    }

    private int addItem(DynamicDict dict) throws BaseAppException {
        return serv.addItem(dict);
    }

    private int updateItem(DynamicDict dict) throws BaseAppException {
        return serv.updateItem(dict);
    }

    private int deleteItem(DynamicDict dict) throws BaseAppException {
        return serv.deleteItem(dict);
    }


    private int qryAllItemsMaterials(DynamicDict dict) throws BaseAppException {
        BoHelper.listDtoToBO("z_d_r", serv.qryAllItemsMaterials(dict), ItemMaterials.class, dict);
        return 0;
    }

    private int addItemMaterials(DynamicDict dict) throws BaseAppException {
        return serv.addItemMaterials(dict);
    }

    private int updateItemMaterials(DynamicDict dict) throws BaseAppException {
        return serv.updateItemMaterials(dict);
    }

    private int deleteItemMaterials(DynamicDict dict) throws BaseAppException {
        return serv.deleteItemMaterials(dict);
    }

    private int qryMaterialsByItemId(DynamicDict dict) throws BaseAppException {
        BoHelper.listDtoToBO("z_d_r", serv.qryMaterialsByItemId(dict), ItemMaterials.class, dict);
        return 0;
    }


    private int qryAllLaws(DynamicDict dict) throws BaseAppException {
        serv.qryAllLaws(dict);
        return 0;
    }

    private int addLaws(DynamicDict dict) throws BaseAppException {
        return serv.addOrUpdateLaws(dict);
    }

    private int updateLaws(DynamicDict dict) throws BaseAppException {
        return serv.addOrUpdateLaws(dict);
    }

    private int deleteLaws(DynamicDict dict) throws BaseAppException {
        return serv.deleteLaws(dict);
    }
}
