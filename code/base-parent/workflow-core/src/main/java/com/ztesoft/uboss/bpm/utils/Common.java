package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.i18n.MessageSource;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: 通用资源对象
 * </p>
 * <p>
 * Description: OCS Web Structure
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: ZTEsoft
 * </p>
 *
 * @author lu.zhen@zte.com.cn
 * @author Casper
 * @version 1.1 移除无用的import,修改默认语言为zh
 */
public class Common {
    /**
     * 目前支持的语言版本，以后新支持一种语言，这里需要增加
     */

    public final static ArrayList<String> SUPPORT_LANGUAGE_LIST = new ArrayList<String>();

    /**
     * 私有构造函数
     */
    private Common() {
    }

    /**
     * 日期时间格式
     */
    public static SimpleDateFormat MY_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd kk:mm:ss");

    /**
     * 版本信息
     */
    public static String VERSION = "2.0.0";

    public static final String SESSION_EMAIL_CODE = "SESSION_EMAIL_CODE";
    public static final String SESSION_EMAIL_CODE_TIME = "SESSION_EMAIL_CODE_TIME";

    /**
     *
     */
    public static final String USER_INFO_BEAN = "SYS_LOGIN_INFO";

    public static final String USER_RESOUSE = "UserResouce";

    public static final String ADD = "Add";

    public static final String NEW = "New";

    public static final String MODIFY = "Modify";

    public static final String DELETE = "Delete";

    public static final String SYSTEM_MESSAGE = "SystemMessage";

    public static final String VALI_MESSAGE = "ValiMessage";

    public static final String VALI_URL = "ValiURL";

    public static final String SYSTEM_ALARM = "SystemAlarm";

    public static final Hashtable hServiceMap = new Hashtable(10, 1);

    public static boolean bDebugDisplay = false;

    public static boolean IS_DEVELOPE_MODE = false;

    public static int ii = 0;

    /**
     * String类型的NULL标识
     */
    public static final String NULL_STRING = "";

    // ---------------------------------------------------------------
    /**
     * 语言
     */
    public static String DEFAULT_LANGUAGE = "en";

    public static Locale DEFAULT_LANGUAGE_LOCALE = Locale.US;

    public static ThreadLocal<Locale> _LANGUAGE = new ThreadLocal<Locale>();

    public static String COOKIE_LOCALE_NAME = "UBOSS_LOCALE";

    //避免敏感名字,伪装一下。
    public static String COOKIE_USER_TOKEN_NAME = "SYS_PREFERENCE";
    public static String COOKIE_PPP_TOKEN_NAME = "SYS_PREFERENCE_PPP";

    public static final String SESSION_VCODE = "SESSION_VCODE";

    /**
     * 本地字符集
     */
    public static String LOCAL_CHARSET = "UTF-8";

    /**
     * 缺省字符集
     */
    public static String DEFAULT_CHARSET = "UTF-8";

    /**
     * URL请求字符集，add by luz
     */
    public static String REQUEST_ENCODE = "ISO-8859-1";

    /**
     * 日历
     */
    public static String CALENDAR = "usa";

    public static String REAL_ROOT_PATH = "";

    public static String ResLoggerLevel = "0";

    /**
     * IP
     */
    public static String HOST_IP = "";

    /**
     * APP_IP
     */
    public static Long APP_IP = 1l;

    /**
     * 获取系统缺省皮肤
     */
    public static String SYS_DEFAULT_SKIN = "";

    static {
        // 初始化支持的语言
        SUPPORT_LANGUAGE_LIST.add(Locale.PRC.toString());
        SUPPORT_LANGUAGE_LIST.add(Locale.US.toString());

        String supportLocList = ConfigurationMgr.instance().getString("i18n.supportLocList");
        if (supportLocList != null && !supportLocList.equals("")) {
            SUPPORT_LANGUAGE_LIST.clear();
            String[] locStrArr = supportLocList.split(",");
            for (int i = 0; i < locStrArr.length; i++) {
                //增加匹配处理
                int underLineIndex = locStrArr[i].indexOf("_");
                if (underLineIndex > 0) {
                    Locale loc = new Locale(locStrArr[i].substring(0, underLineIndex), locStrArr[i].substring(underLineIndex + 1));
                    if (!SUPPORT_LANGUAGE_LIST.contains(loc.toString()))
                        SUPPORT_LANGUAGE_LIST.add(loc.toString());
                } else {
                    int dashIndex = locStrArr[i].indexOf("-");
                    if (dashIndex > 0) {
                        Locale loc = new Locale(locStrArr[i].substring(0, dashIndex), locStrArr[i].substring(dashIndex + 1));
                        if (!SUPPORT_LANGUAGE_LIST.contains(loc.toString()))
                            SUPPORT_LANGUAGE_LIST.add(loc.toString());
                    } else {
                        Locale loc = new Locale(locStrArr[i]);
                        if (!SUPPORT_LANGUAGE_LIST.contains(loc.toString()))
                            SUPPORT_LANGUAGE_LIST.add(loc.toString());
                    }
                }
            }
        }
    }

    /**
     * 转到另外的链接,change by luz
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param errCode  String
     */
    public static void gotoAnotherLink(HttpServletRequest request,
                                       HttpServletResponse response, String errCode, String pageLink) {
        if (errCode != null && errCode.length() > 0) {
            ArrayList<String> al = new ArrayList<String>(1);
            String errMessage = getGlobalRes(errCode);
            al.add(errMessage);
            al.add(errCode);
            request.getSession().setAttribute(Common.VALI_MESSAGE, al);
        }
        try {
            response.sendRedirect(pageLink);
        } catch (IOException ex1) {
        }
    }

    /**
     * 转到另外的链接 change by luz
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param errCode  String
     */
    public static void gotoAnotherLink(HttpServlet servlet,
                                       HttpServletRequest request, HttpServletResponse response,
                                       String errCode, String pageLink) {
        if (errCode != null) {
            ArrayList<String> al = new ArrayList<String>(1);
            String tail = "";
            if ("COMMON.UPLOAD_FILE_EXCEED_MAX".equals(errCode)) {
                tail = getConfig("uploadFileSizeMax") + getGlobalRes("COMMON.FULL_STOP");
            }
            errCode = getGlobalRes(errCode) + tail;
            al.add(errCode);
            request.setAttribute(Common.VALI_MESSAGE, al);
        }
        try {
            RequestDispatcher disp = servlet.getServletContext().getRequestDispatcher(pageLink);
            disp.forward(request, response);
        } catch (IOException ex1) {
            ex1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取web配置
     *
     * @param strCfg
     * @return
     */
    public static String getConfig(String strCfg) {
        String strValue = "";
        strValue = ConfigurationMgr.instance().getString(strCfg + ".value", null);
        return strValue;
    }

    public synchronized static String getCurrentLanguage() {
        Locale local = (Locale) _LANGUAGE.get();
        if (local == null) {
            if (DEFAULT_LANGUAGE.equals("zh-CN"))
                local = new Locale("zh");
            else if (DEFAULT_LANGUAGE.equals("en-US"))
                local = new Locale("en");
            else
                local = new Locale(DEFAULT_LANGUAGE);

            _LANGUAGE.set(local);
        }
        if (!SUPPORT_LANGUAGE_LIST.contains(local.toString())) {
            local = DEFAULT_LANGUAGE_LOCALE;
        }

        return local.getLanguage();
    }

    /**
     * websphere 转码 add by li.chunlei
     *
     * @param msg String
     * @return String
     */
    public static String respMsgEncoding(String msg) {
        return msg;
    }

    public static String getCalendar(String lan) {
        String cal = "";
        int lanValue = 0;
        if (lan.equals("zh_CN"))
            lanValue = 1;

        switch (lanValue) {
            case 0:
                cal = "usa";
                break;
            case 1:
                cal = "china";
                break;
            default:
                cal = "usa";
                break;
        }
        return cal;
    }

    /**
     * 获取后台资源文件
     *
     * @param key
     * @return
     */
    public static String getCommonRes(String key) {
        String globalRes = MessageSource.getText(key);
        return globalRes;

    }

    /**
     * 获取web标签资源
     *
     * @param key
     * @return
     */
    public static String getGlobalRes(String key) {
        String globalRes = MessageSource.getWebLabelRes(key);
        return globalRes;
    }

    /**
     * 获取web提示信息资源
     *
     * @param key
     * @return
     */
    public static String getJScriptRes(String key) {
        String jsRes = MessageSource.getWebJScriptRes(key);
        if (jsRes == null) {
            return null;
        }
        jsRes = jsRes.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
        return jsRes;
    }

    /**
     * 测试页面布局对资源的兼容情况
     *
     * @param str
     * @return
     */
    public static String getResourceStringLogger(String str) {
        if (ResLoggerLevel == null) {
            ResLoggerLevel = "0";
        }
        if (str == null || ResLoggerLevel.equals("0")
                || ResLoggerLevel.equals("")) {
            return str;
        }
        StringBuffer sb = new StringBuffer(str);
        if (str.length() < 10) {
            if (ResLoggerLevel.equals("2")) {
                sb = sb.append(str).append(str).append(str);
            } else {
                sb = sb.append(str).append(str);
            }
        } else if (str.length() < 20) {
            if (ResLoggerLevel.equals("2")) {
                sb = sb.append(str).append(str);
            } else {
                sb = sb.append(str);
            }
        } else {
            if (ResLoggerLevel.equals("2")) {
                sb = sb.append(str);
            } else {
                sb = sb.append(str).append(str.substring(str.length() / 2));
            }
        }
        return sb.toString();
    }

    /**
     * 获取构造的模拟session
     *
     * @return
     */
    public static DynamicDict getVirtualSessionBo() {
        DynamicDict sessionBO = new DynamicDict();
        try {
            sessionBO.set("StaffId", "1");
            sessionBO.set("staffCode", "admin");
            sessionBO.set("StaffName", "SysAdmin");
            sessionBO.set("USER_PASSWORD", "0o4rwbZKivg=");
            sessionBO.set("LOGIN_IP", "127.0.0.1");
            sessionBO.set("LOGIN_DATE", "virtual session date");
        } catch (Exception ex) {
        }
        return sessionBO;

    }

    /**
     * 取到真实的ip
     *
     * @param request
     * @return
     */

    public static String getIpAddr(HttpServletRequest request) {
        String ip = "";
        ip = request.getHeader("X-Forwarded-For");
        try {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String Escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);

        for (i = 0; i < src.length(); i++) {

            j = src.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String Unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
}
