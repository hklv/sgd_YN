package filter;


import com.ztesoft.zsmart.web.resource.Common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理错误页面
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/7/18
 */
public class ErrorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestStr = httpRequest.getRequestURL().toString();
        String strWebRoot = Common.getWebRoot(httpRequest);
        if (requestStr.indexOf("Error.jsp") > 0) {
            httpResponse.sendRedirect(strWebRoot);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
