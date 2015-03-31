package com.darfoo.backend.Interceptors;

import com.darfoo.backend.dao.DashboardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zjh on 14-12-3.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    DashboardDao dashboardDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获得请求路径的uri
        String uri = request.getRequestURI();
        System.out.println(request.getRemoteUser() + " - " + request.getRemoteHost() + " - " + request.getRemoteAddr());

        // 判断路径是登出还是登录验证，是这两者之一的话执行Controller中定义的方法
        // 客户端的restfulapi的url不能拦截
        if (uri.endsWith("/login/auth") || uri.endsWith("/login/out") || (uri.contains("/resources/") && !uri.contains("new") && !uri.contains("create"))) {
            System.out.println("在请求登陆登出和restfulapi");
            return true;
        } else if (uri.endsWith("/login/") || uri.endsWith("/login")) {
            // 进入登录页面，判断session中是否有key，有的话重定向到首页，否则进入登录界面
            if (dashboardDao.isDashboardOpen()) {
                System.out.println("在请求登陆页面");
                if (request.getSession() != null && request.getSession().getAttribute("loginUser") != null) {
                    response.sendRedirect(request.getContextPath() + "/rest/resources/new/dancevideo");
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else if (uri.contains("/resources/") && (uri.contains("new") || uri.contains("create"))) {
            //上传资源需要一个低权限的用户
            System.out.println("在新建资源");
            if (request.getSession() != null && request.getSession().getAttribute("loginUser") != null && (request.getSession().getAttribute("loginUser").equals("upload") || request.getSession().getAttribute("loginUser").equals("cleantha"))) {
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/rest/login");
                return false;
            }
        } else if (uri.contains("cache")) {
            System.out.println("在请求缓存资源");
            return true;
        } else if (uri.contains("uploadresource")) {
            System.out.println("客户端在上传用户拍摄视频");
            return true;
        } else if (uri.contains("test")) {
            System.out.println("测试");
            return true;
        } else if (uri.contains("error") || uri.contains("jsp")) {
            System.out.println("出错啦");
            return true;
        } else if (uri.contains("statistics")) {
            System.out.println("行为统计");
            return true;
        } else if (uri.contains("admin")) {
            // 其他情况判断session中是否有key，有的话继续用户的操作
            if (request.getSession() != null && request.getSession().getAttribute("loginUser") != null && request.getSession().getAttribute("loginUser").equals("cleantha")) {
                System.out.println("在管理资源");
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/rest/login");
                return false;
            }
        } else if (uri.contains("auth")) {
            System.out.println("用户操作");
            return true;
        } else {
            // 最后的情况就是进入登录页面
            System.out.println("需要登陆");
            response.sendRedirect(request.getContextPath() + "/rest/login");
            return false;
        }

        System.out.println("something is going wrong");
        return false;
    }
}
