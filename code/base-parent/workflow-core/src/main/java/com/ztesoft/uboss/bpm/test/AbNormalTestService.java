package com.ztesoft.uboss.bpm.test;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.IAction;

import java.util.Iterator;
import java.util.Map;


public class AbNormalTestService implements IAction {

    public int dosucess(DynamicDict dict) throws BaseAppException {

        System.out.println("===============BpmTestService.dosucess begin=====================");

        Map map = dict.valueMap;

        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        System.out.println("===============BpmTestService.dosucess end=====================");

        return 0;
    }

    public int dosucess2(DynamicDict dict) throws BaseAppException {

        System.out.println("===============BpmTestService.dosucess2 begin=====================");

        Map map = dict.valueMap;

        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        System.out.println("===============BpmTestService.dosucess2 end=====================");

        return 0;
    }

    public int dosucess3(DynamicDict dict) throws BaseAppException {

        System.out.println("===============BpmTestService.dosucess3 begin=====================");

        Map map = dict.valueMap;

        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        System.out.println("===============BpmTestService.dosucess3 end=====================");

        return 0;
    }

    public int doException(DynamicDict dict) throws BaseAppException {

        System.out.println("===============BpmTestService.doException begin=====================");

        ExceptionHandler.publish("BpmTestService has exception");

        return 0;
    }

    public int assignTaskProcessing(DynamicDict dict) throws BaseAppException {

        System.out.println("===============BpmTestService.assignTaskProcessing begin=====================");

        dict.set("bpm_user_id", 4);

        System.out.println("===============BpmTestService.assignTaskProcessing end=====================");

        return 0;
    }

    public int assignTaskMulProcessing(DynamicDict dict) throws BaseAppException {

        System.out.println("===============BpmTestService.assignTaskProcessing begin=====================");

        dict.set("bpm_role_id", 2);

        System.out.println("===============BpmTestService.assignTaskProcessing end=====================");

        return 0;
    }

    @Override
    public int perform(DynamicDict dict) throws BaseAppException {
        String method = (String) dict.get("method");
        if (method != null) {
            if (method.equals("dosucess")) {
                dosucess(dict);
            } else if (method.equals("dosucess2")) {
                dosucess2(dict);
            } else if (method.equals("dosucess3")) {
                dosucess3(dict);
            } else if (method.equals("doException")) {
                doException(dict);
            } else if (method.equals("assignTaskProcessing")) {
                assignTaskProcessing(dict);
            } else if (method.equals("assignTaskMulProcessing")) {
                assignTaskMulProcessing(dict);
            }
        }
        return 0;
    }
}
