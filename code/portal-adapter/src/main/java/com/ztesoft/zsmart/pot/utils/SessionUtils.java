package com.ztesoft.zsmart.pot.utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.log.LogThreadLocalUtil;
import com.ztesoft.zsmart.core.service.DynamicDict;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * session工具类
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/8/26
 */
public class SessionUtils {
    public static DynamicDict getSessionBO(HttpServletRequest request, HttpServletResponse response) throws BaseAppException {
        DynamicDict sessionBO = new DynamicDict();
        HttpSession session = request.getSession();
        sessionBO.set("SESSION_ID", PatternUtil.sansPostfix(session.getId()));

        Long userId = (Long) request.getSession().getAttribute("USER_ID");
        Long spId = (Long) request.getSession().getAttribute("SP_ID");
        String loginIp = (String) request.getSession().getAttribute("LOGIN_IP");
        String loginDate = (String) request.getSession().getAttribute("LOGIN_DATE");
        Long appId = (Long) request.getSession().getAttribute("APP_ID");

        if (userId != null) {
            sessionBO.set("remote-ip", loginIp);
            sessionBO.set("login-ip", loginIp);
            sessionBO.set("login-date", loginDate);
            sessionBO.set("user-id", userId);
            sessionBO.set("user_id", userId);
            sessionBO.set("sp-id", spId);
            sessionBO.set("app-id", appId);
        }

        if (session.getAttribute("CURRENT_MENU_ID") != null) {
            Long currentMenuId = Long.parseLong(session.getAttribute("CURRENT_MENU_ID").toString());
            // sessionBO.set("CURRENT_MENU_ID", currentMenuId);
            // 存在手动调用日志api取不到menuId的问题
            LogThreadLocalUtil.setCurrentMenuId(currentMenuId);
        }
        return sessionBO;
    }
}
