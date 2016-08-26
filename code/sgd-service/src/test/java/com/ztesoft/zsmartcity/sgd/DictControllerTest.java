package com.ztesoft.zsmartcity.sgd;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import org.junit.Test;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-20 10:15
 */
public class DictControllerTest {
    @Test
    public void test() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.add("NAME", "法克");
        dict.add("CODE", "fuck");
        dict.serviceName = "addDict";
        ServiceFlow.callService(dict);

        System.out.println("add over!");
        test2();
        System.out.println("query over!");
    }

    @Test
    public void test2() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "qryDictList";
        ServiceFlow.callService(dict);

        System.out.println(dict.toString());
    }

    @Test
    public void test3() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "addDictData";
        dict.add("VALUE_CODE", "法克");
        dict.add("VALUE_NAME", "fuck");
        dict.add("DICTIONARY_ID", "1");
        ServiceFlow.callService(dict);

        System.out.println(dict.toString());
    }

    @Test
    public void test4() throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        dict.serviceName = "QrySingleDicData";
        dict.add("ID", "145");
        ServiceFlow.callService(dict);

        System.out.println(dict.toString());
    }
}
