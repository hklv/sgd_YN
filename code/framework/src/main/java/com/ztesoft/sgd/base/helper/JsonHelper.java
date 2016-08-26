package com.ztesoft.sgd.base.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * json和对象的转换，使用Jackson<br>
 *
 * @author sunhao<br>
 * @version 1.0 2015年6月11日 下午1:24:39<br>
 */
public final class JsonHelper {

    /**
     * ObjectMapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * 构造方法 
     */
    private JsonHelper() {
    }

    /**
     * 对象转成json <br>
     * 
     * @author sunhao<br>
     * @taskId <br>
     * @param obj Object
     * @return String
     */
    public static String obj2json(Object obj) {
        try {
            StringWriter sw = new StringWriter();
            objectMapper.writeValue(sw, obj);
            return sw.toString();
        }
        catch (Exception e) {
            throw new RuntimeException("对象序列化json发生异常", e);
        }
    }

    /**
     * json转对象 <br>
     * 
     * @author sunhao<br>
     * @taskId <br>
     * @param jsonStr json串
     * @param clazz 类
     * @param <T> <br>
     * @return <br>
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        }
        catch (Exception e) {
            throw new RuntimeException("json反序列化发生异常", e);
        }
    }

    /**
     * json转map <br>
     * 
     * @author sunhao<br>
     * @taskId <br>
     * @param jsonStr json串
     * @param <T> <br>
     * @return Map<String, Object>
     * @throws Exception <br>
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> json2map(String jsonStr) throws Exception {
        return objectMapper.readValue(jsonStr, Map.class);
    }

    /**
     * json转map <br>
     * 
     * @author sunhao<br>
     * @taskId <br>
     * @param jsonStr json串
     * @param clazz 类 
     * @param <T> <br>
     * @return Map<String, T>
     * @throws Exception <br>
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) throws Exception {
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {
        });
        Map<String, T> result = new HashMap<String, T>();
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * json转list <br>
     * 
     * @author sunhao<br>
     * @taskId <br>
     * @param jsonArrayStr JSON串
     * @param clazz 类 
     * @param <T> <br>
     * @return <br>
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        List<Map<String, Object>> list;
        try {
            list = objectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
            });
        }
        catch (Exception e) {
            throw new RuntimeException("json反序列化发生异常", e);
        }

        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * json转pojo <br>
     * 
     * @author sunhao<br>
     * @taskId <br>
     * @param map  映射 
     * @param clazz 类
     * @param <T> <br>
     * @return <br>
     */
    public static <T> T map2pojo(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}
