package com.ztesoft.zsmart.pot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceDynamicDict;
import com.ztesoft.zsmart.core.utils.EqualsUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.pot.resource.Common;
import com.ztesoft.zsmart.pot.service.GenericService;
import com.ztesoft.zsmart.pot.utils.FastJSONUtils;
import com.ztesoft.zsmart.pot.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;


@Controller
public class ServiceController {

    private static final ZSmartLogger logger = ZSmartLogger.getLogger(ServiceController.class);

    private static final String CONTENT_TYPE = "text/json; charset=UTF-8";

    @Autowired
    private GenericService genericServ;

    @RequestMapping(value = "service", method = RequestMethod.POST)
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(Common.LOCAL_CHARSET);
        request.setCharacterEncoding(Common.LOCAL_CHARSET);
        ServletOutputStream out = response.getOutputStream();
        byte[] returnValue = null;

        try {
            try {
                returnValue = executeJson(request, response).getBytes(Common.LOCAL_CHARSET);
            } catch (Throwable e) {
                logger.error("This is BusiJSONFacadeServlet : ", e);
                String code = "{\"Msg\":\"" + e.getMessage() + "\"}";
                returnValue = code.getBytes(Common.LOCAL_CHARSET);
            }
            out.write(returnValue);
            out.flush();
        } finally {
            returnValue = null;
            if (out != null) {
                out.close();
            }
            if (request != null) {
                request = null;
            }
            if (response != null) {
                response = null;
            }
        }
    }

    /**
     * 执行JSON格式的交互
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String executeJson(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("WebHandler deal start...");
        try {
            long start = System.currentTimeMillis();
            JSONObject jsonObj = getInputJSONFromRequest(request);

            DynamicDict bo = FastJSONUtils.fastJson2BO(jsonObj);// JSON字符串转换为对象


            ServiceDynamicDict newBo = (ServiceDynamicDict) formatBO(bo);
            newBo.setChannel(ActionDomain.CHANNEL_WEB);// 设置为web调用
            newBo.set(ActionDomain.IS_LOGGED, new Boolean(false));
            newBo.serviceName = bo.serviceName;

            if ("A".equals(newBo.getString("PARTY_TYPE")) && "NaN".equals(newBo.getString("PARTY_CODE"))) {// task:136045
                newBo.set("PARTY_CODE", "1");
            }
            if (StringUtil.isEmpty(newBo.getString("JOB_ID")) || "NaN".equals(newBo.getString("JOB_ID"))) {
                newBo.set("JOB_ID", "1");
            }

            DynamicDict zsmart_session = SessionUtils.getSessionBO(request, response);// 设置后台缓存
            if (zsmart_session.getLong("staff-id") == null) {
                zsmart_session.set("staff-id", 0);
            }
            newBo.set("zsmart_session", zsmart_session);
            if (bo.get("APP_ID") == null)// 增加对APP_ID,SP_ID的处理
                newBo.set("APP_ID", zsmart_session.getLong("app-id"));
            if (bo.get("SP_ID") == null)
                newBo.set("SP_ID", zsmart_session.getLong("sp-id"));
            if (bo.get("STAFF_JOB_ID") == null)
                newBo.set("STAFF_JOB_ID", zsmart_session.getLong("staff-job-id"));


            ServiceDynamicDict result = genericServ.callService(newBo);

            //bo对象变化了,重新设置
            newBo.valueMap = result.valueMap;
            newBo.setServiceDto(result.getServiceDto());

            newBo.remove("zsmart_session");
            if (bo.get("APP_ID") == null)
                newBo.remove("APP_ID");
            if (bo.get("SP_ID") == null)
                newBo.remove("SP_ID");
            if (bo.get("STAFF_JOB_ID") == null)
                newBo.remove("STAFF_JOB_ID");


            newBo.setSuccess(true);

            String resultStr = FastJSONUtils.B02JsonString(newBo);
            long end = System.currentTimeMillis();
            logger.debug("WebHandler deal time : " + (end - start));
            return resultStr;
        } catch (Throwable ex) {
            logger.error("Execute Error:" + ex.getMessage(), ex);
            return parserException(ex);
        }
    }

    /**
     * 转换字符串
     *
     * @param t
     * @return
     */
    private static final String parserException(Throwable t) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isSuccess", false);
        if (t instanceof BaseAppException) {
            BaseAppException bx = (BaseAppException) t;
            String code = bx.getCode();
            jsonObject.put("MsgCode", code);
            String msg = bx.getLocaleMessage();
            if (StringUtil.isEmpty(msg)) {
                msg = bx.getDesc();
            }
            msg = (msg == null) ? "UNKNOWN ERROR." : msg;
            jsonObject.put("Msg", msg);
            if (bx.getType() == BaseAppException.BUSS_ERROR) {
                jsonObject.put("ExceptionType", BaseAppException.BUSS_ERROR);
            }
        } else {
            String desc = t.getMessage();
            if (StringUtil.isEmpty(desc)) {
                desc = t.getClass().getCanonicalName();
            }
            jsonObject.put("Msg", desc);
        }
        return jsonObject.toJSONString();
    }

    /**
     * 从Request中获取输入的JSON字符串
     *
     * @param httpServletRequest
     * @return
     * @throws Throwable
     */
    private static final JSONObject getInputJSONFromRequest(HttpServletRequest httpServletRequest) throws Throwable {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        String encoding = httpServletRequest.getCharacterEncoding();
        try {
            reader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream(), encoding));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        String jsonStr = builder.toString();
        JSONObject json = (JSONObject) JSON.parse(jsonStr);
        Object data = json.remove("Data");
        if (data != null && data instanceof JSONObject) {
            json.putAll((JSONObject) data);
        } else {
            json.put("Data", data);
        }
        return json;
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    private DynamicDict formatBO(DynamicDict bo) throws BaseAppException {
        boolean trimStr = judgeTrimInputParam(bo);

        ServiceDynamicDict result = new ServiceDynamicDict();

        Set s = bo.valueMap.entrySet();
        Map.Entry m = null;
        Object o = null;
        String key = null;

        for (Iterator iter = s.iterator(); iter.hasNext(); ) {
            m = (Entry) iter.next();
            o = m.getValue();
            key = m.getKey().toString();

            // 分别处理
            if (o instanceof HashMap) {
                DynamicDict child = new DynamicDict();

                child.valueMap = (HashMap) o;

                result.set(key, formatBO(child));
            }
            // 若是 ARRAYLIST
            else if (o instanceof ArrayList) {
                ArrayList children = new ArrayList();

                Object ele = null;
                for (Iterator it = ((ArrayList) o).iterator(); it.hasNext(); ) {
                    ele = it.next();
                    if (ele instanceof HashMap) {
                        DynamicDict child = new DynamicDict();

                        child.valueMap = (HashMap) ele;

                        children.add(formatBO(child));
                    } else {
                        children.add(ele);
                    }
                }
                result.add(key, children);
            } else if (o instanceof Object[]) {
                Object[] a = (Object[]) o;
                int length = a.length;
                ArrayList arr = new ArrayList();

                for (int i = 0; i < length; i++) {
                    Object ele = a[i];
                    if (ele instanceof HashMap) {
                        DynamicDict child = new DynamicDict();

                        child.valueMap = (HashMap) ele;

                        arr.add(formatBO(child));
                    } else {
                        arr.add(ele);
                    }
                }
                result.add(key, arr);
            } else {
                result.add(key, o, trimStr);
            }
        }// end of for
        return result;
    }

    private boolean judgeTrimInputParam(DynamicDict bo) throws BaseAppException {
        boolean trimStr = true;
        if (EqualsUtil.equals(bo.getString(ActionDomain.BO_TRIM_STRING, false), "0")) {
            trimStr = false;
        }
        return trimStr;
    }


//    public void packageObjectForJson(DynamicDict dict) throws Exception {
//        Session ses = null;
//        try {
//            ses = SessionContext.currentSession();
//            ses.beginTrans();
//            getInternationalRes(dict);
//            getServiceParamBO(dict);
//            ses.commitTrans();
//        }
//        finally {
//            ses.releaseTrans();
//        }
//    }
//    
//    private void getInternationalRes(DynamicDict dict) throws BaseAppException {
//
//        String serviceName = dict.getServiceName();
//        String zsmart_referer_url = dict.getString("zsmart_referer_url");
//        if (zsmart_referer_url == null) {
//            return;
//        }
//        if (!refererURLValidate(zsmart_referer_url, serviceName)) {
//            return;
//        }
//        ArrayList<DynamicDict> list = I18nDataCache.getPathByServiceName(serviceName);
//        Locale loc = ((Locale) Common._LANGUAGE.get());
//        if (list == null)
//            return;
//        Iterator iter = list.iterator();
//        while (iter.hasNext()) {
//            DynamicDict dt = (DynamicDict) iter.next();
//            String path = dt.getString("NAMESPACE");
//            Long resObjId = dt.getLong("RES_OBJECT_ID");
//            HashMap resMap = I18nDataCache.getGolobelResByLanAndResObjId(loc, resObjId.toString());
//            setValueByPath(dict, path, resMap);
//        }
//
//    }
//    
//    private void getServiceParamBO(DynamicDict dicParam) {
//        String serviceName = "";
//        ServiceDynamicDict dict = null;
//        dicParam.getServiceName();
//        if (dicParam instanceof ServiceDynamicDict) {
//            dict = (ServiceDynamicDict) dicParam;
//            serviceName = dict.getServiceName();
//
//            if (!METHOD_SET_SESSION.equals(serviceName)) {
//                ArrayList<String> list = new ArrayList<String>();
//
//                if (dict.getServiceDto() != null) {
//                    List outParams = dict.getServiceDto() == null ? new ArrayList() : dict.getServiceDto()
//                        .getOutParam();
//                    if (outParams.size() > 0) {
//                        for (Iterator iter = outParams.iterator(); iter.hasNext();) {
//                            ServiceArgsDto serviceArgsDto = (ServiceArgsDto) iter.next();
//                            list.add(serviceArgsDto.getName().trim());
//                        }
//                        // 加上默认出参
//                        try {
//                            String[] fieldKeeps = ConfigurationMgr.instance().getString("web.fieldKeep", null)
//                                .split(";");
//                            for (int i = 0, j = fieldKeeps.length; i < j; i++) {
//                                list.add(fieldKeeps[i]);
//                            }
//                        }
//                        catch (Exception e) {
//                        }
//                        if (list.size() > 0) {
//                            dicParam.valueMap.keySet().retainAll(list);
//                        }
//                    }
//                }
//            }
//        }
//    }
}