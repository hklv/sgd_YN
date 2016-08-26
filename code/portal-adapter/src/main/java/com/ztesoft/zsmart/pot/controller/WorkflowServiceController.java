package com.ztesoft.zsmart.pot.controller;

import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.HttpCall;
import com.ztesoft.zsmart.core.utils.BOUtils;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.pot.resource.Common;
import com.ztesoft.zsmart.pot.utils.SessionUtils;
import com.ztesoft.zsmart.web.handler.ExecutionSerializer;
import com.ztesoft.zsmart.web.handler.WebAdapt;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作流接口适配器
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/8/17
 */
@Controller
public class WorkflowServiceController {
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(ServiceController.class);
    private static final String CONTENT_TYPE = "text/xml; charset=UTF-8";
    /**
     * 参数的数据类型-数组类型.
     */
    public static final char OBJECT_TYPE_ARRAY = 'a';

    /**
     * 参数的数据类型-String类型.
     */
    public static final char OBJECT_TYPE_STRING = 's';

    /**
     * 参数的数据类型-日期类型.
     */
    public static final char OBJECT_TYPE_DATE = 'd';

    /**
     * 参数的数据类型-Object类型.
     */
    public static final char OBJECT_TYPE_OBJECT = 'o';

    /**
     * 参数的数据类型-null类型.
     */
    public static final char OBJECT_TYPE_NULL = 'n';

    @RequestMapping(value = "doService", method = RequestMethod.POST)
    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(Common.LOCAL_CHARSET);
        request.setCharacterEncoding(Common.LOCAL_CHARSET);
        ServletOutputStream out = response.getOutputStream();
        byte[] returnValue = null;

        try {
            try {
                returnValue = execute(request, response);
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

    public byte[] execute(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        logger.debug("WebHandler deal start...");
        long start = System.currentTimeMillis();

        // step1 get XML from request
        String characterEncoding = httpServletRequest.getCharacterEncoding();
        ServletInputStream sis;
        try {
            sis = httpServletRequest.getInputStream();

            // step2 XML 2 document
            Document reqDoc = WebAdapt.getReader().readXML(sis, characterEncoding);

            // step3 execute
            Object retObj = execute(reqDoc, httpServletRequest, response);

            // step4 write back
            byte[] packResult = WebAdapt.getWriter().packageObject(retObj);

            long end = System.currentTimeMillis();
            logger.debug("WebHandler deal time : " + (end - start));
            return packResult;

        } catch (Throwable ex) {
            logger.error("Execute Error:" + ex.getMessage(), ex);
            return ExecutionSerializer.packageException(ex);
        }
    }

    public Object execute(Document reqDoc, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // step1 doc to object
        DynamicDict bo = docToBO(reqDoc);
        bo.setChannel(ActionDomain.CHANNEL_WEB);// 设置为web调用
        bo.set(ActionDomain.IS_LOGGED, new Boolean(false));

        String serviceName = bo.getServiceName();
        if (serviceName.equals(ActionDomain.SERVICE_NAME_SET_SESSION)) {
            try {
                request.getSession().setAttribute(bo.getString("key").toString(), bo.getValueByName("value", false));
            } catch (Exception e) {
                logger.error(e);
            }
        } else if (serviceName.equals(ActionDomain.SERVICE_NAME_GET_SESSION)) {
            bo.set("SESSION_VALUE", request.getSession().getAttribute(bo.getString("key")));
        } else if (bo.serviceName.equals(ActionDomain.SERVICE_NAME_GET_CONFIG)) {
            String configKey = bo.getString("KEY");
            bo.set("KEY", configKey);
            bo.set("VALUE", ConfigurationMgr.instance().getString(configKey));
            // } else if (bo.serviceName
            // .equalsIgnoreCase(ActionDomain.SERVICE_NAME_GET_STRING)) {
            // bo.set("VALUE", Common.getResourceStringLogger(Common
            // .getGlobalRes(bo.getString("key"))));
            // } else if (bo.serviceName
            // .equalsIgnoreCase(ActionDomain.SERVICE_NAME_GET_JS_STRING)) {
            // bo.set("VALUE", Common.getResourceStringLogger(Common
            // .getJScriptRes(bo.getString("key"))));
        } else {
            DynamicDict zsmart_session = SessionUtils.getSessionBO(request, response);
            bo.set("zsmart_session", zsmart_session);
            boolean hasAppId = true;
            boolean hasSpId = true;
            boolean hasStaffJobId = true;
            if (bo.get("APP_ID") == null)
                hasAppId = false;
            bo.set("APP_ID", zsmart_session.getLong("app-id"));
            if (bo.get("SP_ID") == null)
                hasSpId = false;
            bo.set("SP_ID", zsmart_session.getLong("sp-id"));
            if (bo.get("") == null)
                hasStaffJobId = false;
            bo.set("STAFF_JOB_ID", zsmart_session.getLong("STAFF_JOB_ID"));
            // step2 call service
            new HttpCall().httpCallWithDbRtEvn(bo);
            bo.remove("zsmart_session");
            bo.set("zsmart_referer_url", request.getHeader("referer"));
            if (!hasAppId)
                bo.remove("APP_ID");
            if (!hasSpId)
                bo.remove("SP_ID");
            if (!hasStaffJobId)
                bo.remove("STAFF_JOB_ID");
        }
        bo.remove(ActionDomain.IS_LOGGED);

        return bo;
    }

    DynamicDict docToBO(Document document) throws Exception {
        DynamicDict bo = new DynamicDict();
        Element root = document.getRootElement();
        if (XMLDom4jUtils.child(root, "Type") != null
                && XMLDom4jUtils.child(root, "Type").getText().compareTo("1") == 0) {
            // Type 属性是作为兼容来考虑以及后来的扩展 ,type=1 为标准模式

            bo = BOUtils.xml2bo(document, null);
            bo.setType("1");
        } else {
            Element serviceElm = XMLDom4jUtils.child(root, "ServiceName");

            bo.setServiceName(serviceElm.getText());
            Element dataElm = XMLDom4jUtils.child(root, "Data");

            List<Element> paramElements = dataElm.elements();

            for (Element element : paramElements) {
                String tagName = element.getQName().getName();
                char childType = tagName.charAt(0);
                String key = tagName.substring(1);

                if (childType == OBJECT_TYPE_STRING) {
                    bo.set(key, element.getText());
                } else if (childType == OBJECT_TYPE_OBJECT) {
                    bo.set(key, getBOFromElement(element));
                } else if (childType == OBJECT_TYPE_ARRAY && element.elements().size() > 0) {
                    List<Element> listElm = element.elements();
                    final char arrayChildType = getArrayType(element.elements());
                    ArrayList<Object> alArray = new ArrayList<Object>();

                    switch (arrayChildType) {
                        case OBJECT_TYPE_STRING:
                            String[] strArray = getStringArrayFromNodes(element.elements());
                            for (String str : strArray) {
                                alArray.add(str);
                            }

                            bo.set(key, alArray);
                            break;
                        default:
                            for (Element ele : listElm) {
                                DynamicDict boTmp = getBOFromElement(ele);
                                alArray.add(boTmp);
                            }
                            bo.set(key, alArray);
                            break;
                    }
                }
            }
        }
        return bo;
    }

    private DynamicDict getBOFromElement(Element rootElm) throws Exception {
        DynamicDict bo = new DynamicDict();

        List paramElements = rootElm.elements();
        int paramCount = paramElements.size();
        for (int i = 0; i < paramCount; i++) {
            Element element = (Element) paramElements.get(i);

            String tagName = element.getQName().getName();
            char childType = tagName.charAt(0);
            String key = tagName.substring(1);

            if (childType == OBJECT_TYPE_STRING) {
                bo.set(key, element.getText());
            } else if (childType == OBJECT_TYPE_OBJECT) {
                bo.set(key, getBOFromElement(element));
            } else if (childType == OBJECT_TYPE_ARRAY) {
                List listElm = element.elements();
                if (element.elements().size() > 0) {
                    ArrayList<Object> alArray = new ArrayList<Object>();
                    final char arrayChildType = getArrayType(element.elements());

                    switch (arrayChildType) {
                        case OBJECT_TYPE_STRING:
                            String[] strArray = getStringArrayFromNodes(element.elements());
                            for (String str : strArray) {
                                alArray.add(str);
                            }

                            bo.set(key, alArray);
                            break;
                        default:
                            int sizeElm = listElm.size();
                            for (int j = 0; j < sizeElm; j++) {
                                DynamicDict boTmp = getBOFromElement((Element) listElm.get(j));
                                alArray.add(boTmp);

                            }
                            bo.set(key, alArray);
                            break;
                    }
                }
            }
        }

        return bo;
    }

    /**
     * 得到以数组做为输入参数时数组的数据类型.
     *
     * @param nodes List 数组
     * @return char 数据类型
     */
    @SuppressWarnings("rawtypes")
    private static char getArrayType(List nodes) {
        Element child = (Element) nodes.get(0);
        String tagName = child.getQName().getName();
        char childType = tagName.charAt(0);
        return childType;
    }

    @SuppressWarnings("rawtypes")
    private static String[] getStringArrayFromNodes(List nodes) throws Exception {
        int size = nodes.size();
        String[] strArr = new String[size];

        for (int i = 0; i < size; i++) {
            Element child = (Element) nodes.get(i);
            String tagName = child.getQName().getName();

            String key = tagName.substring(1);
            if (key.equals("I")) {
                strArr[i] = child.getText();
            }
        }
        return strArr;
    }


}
