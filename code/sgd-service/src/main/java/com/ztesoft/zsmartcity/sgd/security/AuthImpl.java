package com.ztesoft.zsmartcity.sgd.security;

import com.ztesoft.sgd.base.helper.LoginUserHolder;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import lodsve.security.core.Account;
import lodsve.security.service.Authz;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/7/4 下午4:09
 */
@Component
public class AuthImpl implements Authz {
    @Override
    public boolean isLogin(HttpServletRequest request) {
        Long userId = getUserId(request);
        LoginUserHolder.set(userId);
        return userId != null;
    }

    @Override
    public boolean authz(Account account, String... roles) {
        return true;
    }

    @Override
    public Account getCurrentUser(HttpServletRequest request) {
        Long userId = getUserId(request);

        return new Account(userId, "Test");
    }

    private Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        DynamicDict dict = (DynamicDict) session.getAttribute("SYS_LOGIN_INFO");

        if (dict == null) {
            return null;
        }

        try {
            return dict.getLong("USER_ID");
        } catch (BaseAppException e) {
            return null;
        }
    }
}
