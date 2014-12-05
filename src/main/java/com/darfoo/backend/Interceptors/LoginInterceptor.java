package com.darfoo.backend.Interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zjh on 14-12-3.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获得请求路径的uri
        String uri = request.getRequestURI();

        // 判断路径是登出还是登录验证，是这两者之一的话执行Controller中定义的方法
        if(uri.endsWith("/login/auth") || uri.endsWith("/login/out") || (uri.contains("/resources/") && !uri.contains("new") && !uri.contains("create"))) {
            return true;
        }

        // 进入登录页面，判断session中是否有key，有的话重定向到首页，否则进入登录界面
        if(uri.endsWith("/login/") || uri.endsWith("/login")) {
            if(request.getSession() != null && request.getSession().getAttribute("loginUser") != null) {
                response.sendRedirect(request.getContextPath() + "/rest/resources/video/new");
            } else {
                return true;
            }
        }

        // 其他情况判断session中是否有key，有的话继续用户的操作
        if(request.getSession() != null && request.getSession().getAttribute("loginUser") != null && request.getSession().getAttribute("loginUser").equals("cleantha")) {
            return true;
        }

        // 最后的情况就是进入登录页面
        response.sendRedirect(request.getContextPath() + "/rest/login");
        return false;
    }
}
