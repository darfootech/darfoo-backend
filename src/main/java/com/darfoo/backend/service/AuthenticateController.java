package com.darfoo.backend.service;

import com.darfoo.backend.dao.auth.AuthenticateDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.auth.Bind;
import com.darfoo.backend.model.auth.Feedback;
import com.darfoo.backend.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by zjh on 15-3-4.
 */

@Controller
@RequestMapping("/auth")
public class AuthenticateController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    AuthenticateDao authenticateDao;

    @RequestMapping(value = "prepare/{mac}")
    public
    @ResponseBody
    String prepareBindMac(@PathVariable String mac) {
        if (commonDao.isResourceExistsByField(Bind.class, "mac", mac)) {
            return "error";
        } else {
            return "ok";
        }
    }

    @RequestMapping(value = "bind/m/{mac}/u/{username}/p/{password}")
    public
    @ResponseBody
    Integer bindMac(@PathVariable String mac, @PathVariable String username, @PathVariable String password) {
        int result;

        boolean flag = authenticateDao.authenticate(username, password);
        if (flag) {
            System.out.println("用户已经存在");
            result = 500;
        } else {
            System.out.println("用户不存在");
            int userid = authenticateDao.createUser(username, password);
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("userid", userid);
            conditions.put("mac", mac);

            if (authenticateDao.createResource(Bind.class, conditions)) {
                System.out.println("绑定成功");
                result = 200;
            } else {
                System.out.println("绑定失败");
                result = 501;
            }
        }

        return result;
    }

    @RequestMapping(value = "feedback/u/{username}/p/{password}")
    public
    @ResponseBody
    String feedback(@PathVariable String username, @PathVariable String password, HttpServletRequest request) {
        String content = request.getParameter("content");

        boolean flag = authenticateDao.authenticate(username, password);
        if (flag) {
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("username", username);

            Object user = commonDao.getResourceByFields(User.class, conditions);
            int userid = (Integer) commonDao.getResourceAttr(User.class, user, "id");

            conditions.put("userid", userid);
            conditions.put("feedback", content);

            if (authenticateDao.createResource(Feedback.class, conditions)) {
                System.out.println("提交反馈成功");
                return "ok";
            } else {
                System.out.println("提交反馈失败");
                return "error";
            }
        } else {
            System.out.println("用户不存在无法提交反馈");
            return "error";
        }
    }
}
