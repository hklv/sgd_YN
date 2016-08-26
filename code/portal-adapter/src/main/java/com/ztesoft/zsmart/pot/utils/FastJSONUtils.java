package com.ztesoft.zsmart.pot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceDynamicDict;
import com.ztesoft.zsmart.core.service.model.ServiceDto;
import com.ztesoft.zsmart.core.utils.DateUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class FastJSONUtils {
    /**
     * @return
     * @throws BaseAppException
     */
    public static DynamicDict jsonString2BO(String jsonStr) throws BaseAppException {
        Object json = JSON.parse(jsonStr);
        if (json instanceof JSONObject) {
            return fastJson2BO((JSONObject) json);
        }
        else {
            ExceptionHandler.publish("J-CONVERT-001");// 直接抛出了一场，后面的null是不会返回的
            return null;
        }
    }

    /**
     * BO转换为JSON字符串信息
     * 
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public static String B02JsonString(DynamicDict dict) throws BaseAppException {
        return JSON.toJSONStringWithDateFormat(bo2FastJsonObject(dict), DateUtil.DATETIME_FORMAT_1,
            SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * JSONObject转换成BO
     * 
     * @param json
     * @return
     * @throws BaseAppException
     */
    public static DynamicDict fastJson2BO(JSONObject json) throws BaseAppException {
        DynamicDict dict = new DynamicDict();
        if (json != null) {
            for (Iterator<Entry<String, Object>> i = ((JSONObject) json).entrySet().iterator(); i.hasNext();) {
                Map.Entry<String, Object> entry = i.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                dict.set(key, obj2BoInnerObj(value));
            }
            if (StringUtil.isNotEmpty(json.getString("ServiceName"))) {
                dict.setServiceName(json.getString("ServiceName"));
            }
        }
        return dict;
    }

    /**
     * BO转换为fastjson的JSONObject
     * 
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public static JSONObject bo2FastJsonObject(DynamicDict dict) throws BaseAppException {
        JSONObject jsonObj = new JSONObject();
        // date //
        if (dict != null) {
            for (Iterator<Entry<String, Object>> it = dict.valueMap.entrySet().iterator(); it.hasNext();) {
                Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                jsonObj.put(key, obj2JsonObjectInnerObj(value));
            }
            if (StringUtil.isNotEmpty(dict.getServiceName())) {
                jsonObj.put("serviceName", dict.getServiceName());
            }
            if (StringUtil.isNotEmpty(dict.getChannel())) {
                jsonObj.put("channel", dict.getChannel());
            }
            jsonObj.put("isSuccess", dict.isSuccess());
            if (StringUtil.isNotEmpty(dict.getMsgCode())) {
                jsonObj.put("MsgCode", dict.getMsgCode());
            }
            if (StringUtil.isNotEmpty(dict.getMsg())) {
                jsonObj.put("Msg", dict.getMsg());
            }
            if (dict instanceof ServiceDynamicDict) {
                ServiceDto serviceDto = ((ServiceDynamicDict) dict).getServiceDto();
                if (serviceDto != null) {
                    jsonObj.put("serviceType", serviceDto.getServiceType());
                }
            }
        }
        return jsonObj;
    }

    /**
     * Object转换为直接放置在BO内部的object，对于map类型其会转换为DynamicDict， 对于数组和Collection类型其会转换为ArrayList，对于其他类型直接返回
     * 
     * @param obj
     * @return
     * @throws BaseAppException
     */
    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    private static Object obj2BoInnerObj(Object obj) throws BaseAppException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {// 如果是map类型的
            DynamicDict dict = new DynamicDict();
            for (Iterator<Map.Entry> i = ((Map) obj).entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = i.next();
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                dict.set(key, obj2BoInnerObj(value));
            }
            return dict;
        }
        else if (obj instanceof Iterable) {// 如果是list类型的
            List list = new ArrayList();
            for (Iterator i = ((Iterable) obj).iterator(); i.hasNext();) {
                Object subObj = i.next();
                list.add(obj2BoInnerObj(subObj));
            }
            return list;
        }
        else if (obj.getClass().isArray()) {// 如果是数组
            List list = new ArrayList();
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object subobj = Array.get(obj, i);
                list.add(obj2BoInnerObj(subobj));
            }
            return list;
        }
        else {// 是其他类型，直接返回
            return obj;
        }
    }

    /**
     * Object转换为直接放置在JSON内部的object，对于map类型其会转换为JSONObject， 对于数组和Collection类型其会转换为JSONArray，对于其他类型直接返回
     * 
     * @param obj
     * @return
     * @throws BaseAppException
     */
    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    private static Object obj2JsonObjectInnerObj(Object obj) throws BaseAppException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof DynamicDict) {
            return obj2JsonObjectInnerObj(((DynamicDict) obj).valueMap);
        }
        else if (obj instanceof Map) {// 如果是Map的话
            JSONObject jsonObject = new JSONObject();
            for (Iterator<Map.Entry> i = ((Map) obj).entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = i.next();
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                jsonObject.put(key, obj2JsonObjectInnerObj(value));
            }
            return jsonObject;
        }
        else if (obj instanceof Iterable) {
            JSONArray jsonArray = new JSONArray();
            for (Iterator i = ((Iterable) obj).iterator(); i.hasNext();) {
                Object subObj = i.next();
                jsonArray.add(obj2JsonObjectInnerObj(subObj));
            }
            return jsonArray;
        }
        else if (obj.getClass().isArray()) {// 如果是数组
            JSONArray jsonArray = new JSONArray();
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object subobj = Array.get(obj, i);
                jsonArray.add(obj2BoInnerObj(subobj));
            }
            return jsonArray;
        }
        else {// 是其他类型，直接返回
            return obj;
        }
    }

    public static void main(String[] args) {
        System.setProperty("ZSMART_HOME", "C:\\DevelopEnviroment\\portal8.1\\ZSMART_HOME");
        String jsonString = "{'Name':'wanghui','Code':'blackcoal','特点':{'人品':'好人','性格':'好'},'爱好':[{'最喜欢':'玩'},{'然后喜欢':'吃'},{'最不喜欢':'上班'}]}";
        try {
            DynamicDict dict = jsonString2BO(jsonString);
            System.out.println(dict);
        }
        catch (BaseAppException e) {
            e.printStackTrace();
        }

        try {
            DynamicDict dict = new DynamicDict();
            dict.set("Name", "王慧");
            dict.set("Code", "blackcoal");

            dict.set("date1", new Date());

            DynamicDict dictTD = new DynamicDict();
            dictTD.set("人品", "好人");
            dictTD.set("性格", "好");
            dict.set("特点", dictTD);

            List<DynamicDict> list = new ArrayList<DynamicDict>();
            DynamicDict dict1 = new DynamicDict();
            dict1.set("最喜欢", "玩");
            list.add(dict1);

            DynamicDict dict2 = new DynamicDict();
            dict2.set("然后喜欢", "吃");
            list.add(dict2);

            DynamicDict dict3 = new DynamicDict();
            dict3.set("最不喜欢", "上班");
            list.add(dict3);

            dict.set("特点", list);

            JSONObject o = bo2FastJsonObject(dict);

            System.out.println(o);

            System.out.println(JSON.toJSONStringWithDateFormat(o, DateUtil.DATETIME_FORMAT_1,
                SerializerFeature.WriteDateUseDateFormat));
        }
        catch (BaseAppException e) {
            e.printStackTrace();
        }
    }
}
