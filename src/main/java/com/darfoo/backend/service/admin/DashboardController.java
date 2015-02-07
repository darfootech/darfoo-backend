package com.darfoo.backend.service.admin;

import com.darfoo.backend.utils.DashboardUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 15-2-7.
 */

@Controller
public class DashboardController {
    @RequestMapping(value = "/resources/od", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> openDashboard(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        String username = request.getParameter("dXNlcm5hbWU=");
        String password = request.getParameter("cGFzc3dvcmQ=");

        if (username.equals("Y2NjMzMz") && password.equals("cHBwcHBwcHA=")) {
            DashboardUtils.openDashboard();
            result.put("status", "ok");
            return result;
        } else {
            result.put("status", "error");
            return result;
        }
    }

    @RequestMapping(value = "/resources/cd", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> closeDashboard(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        String username = request.getParameter("dXNlcm5hbWU=");
        String password = request.getParameter("cGFzc3dvcmQ=");

        if (username.equals("Y2NjMzMz") && password.equals("cHBwcHBwcHA=")) {
            DashboardUtils.closeDashboard();
            result.put("status", "ok");
            return result;
        } else {
            result.put("status", "error");
            return result;
        }
    }
}
