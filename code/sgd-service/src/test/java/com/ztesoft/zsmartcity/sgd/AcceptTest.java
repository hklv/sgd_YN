package com.ztesoft.zsmartcity.sgd;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmartcity.sgd.accept.service.AcceptService;
import org.junit.Test;

/**
 * Created by ZhangLu on 2016/6/22.
 */
public class AcceptTest {
    @Test
    public void testQueryTree() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        AcceptService service = new AcceptService();
        service.qryItemTree(dict);
        Object object = dict.get("z_d_r");
        System.out.println("=============="+dict.get("z_d_r"));
    }

    @Test
    public void testQueryApplyInfo() throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        AcceptService service = new AcceptService();
        dict.set("HOLDER_NO","20160712140540895");
        service.queryApplyInfo(dict);
        Object object1 = dict.get("APPLY");
        Object object2 = dict.get("AGENT");
        Object object3 = dict.get("ITEM_MATERIALS");
    }

    @Test
    public void testQryPriN() throws BaseAppException{
        DynamicDict dict = new DynamicDict();
        AcceptService service = new AcceptService();
        service.qryPrivateNetWork(dict);
        Object object = dict.get("MENU_LIST");
        System.out.println("=============="+dict.get("MENU_LIST"));
    }
}
