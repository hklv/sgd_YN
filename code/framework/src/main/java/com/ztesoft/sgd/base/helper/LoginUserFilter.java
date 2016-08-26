package com.ztesoft.sgd.base.helper;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * login user id filter.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-27 11:30
 */
public class LoginUserFilter extends HttpServlet implements Filter {
    public static final ZSmartLogger logger = ZSmartLogger.getLogger(LoginUserFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpSession session = request.getSession();
        DynamicDict dict = (DynamicDict) session.getAttribute("SYS_LOGIN_INFO");

        if (dict == null) {
            logger.debug("no DynamicDict of SYS_LOGIN_INFO!");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            Long userId = dict.getLong("USER_ID");
            logger.debug("user-id is " + userId);
            LoginUserHolder.set(userId);

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BaseAppException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
