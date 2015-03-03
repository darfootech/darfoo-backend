package com.springapp.mvc.auth;

/**
 * Created by zjh on 15-3-3.
 */

import com.darfoo.backend.dao.auth.AuthenticateDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.auth.Bind;
import com.darfoo.backend.model.auth.Feedback;
import com.darfoo.backend.model.auth.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class AuthTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    AuthenticateDao authenticateDao;

    @Test
    public void prepareBind() {
        String mac = "macaddr333";

        if (commonDao.isResourceExistsByField(Bind.class, "mac", mac)) {
            System.out.println("mac地址已经绑定过用户,不能被绑定了");
        } else {
            System.out.println("mac地址还没有接受过绑定");
        }
    }

    @Test
    public void bindMac() {
        String mac = "macaddr333";
        String username = "username333";
        String password = "password";

        boolean flag = authenticateDao.authenticate(username, password);
        if (flag) {
            System.out.println("用户已经存在");
        } else {
            System.out.println("用户不存在");
            int userid = authenticateDao.createUser(username, password);
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("userid", userid);
            conditions.put("mac", mac);

            if (authenticateDao.createResource(Bind.class, conditions)) {
                System.out.println("绑定成功");
            } else {
                System.out.println("绑定失败");
            }
        }
    }

    @Test
    public void getFeedback() {
        String content = "feedback";
        String username = "username333";
        String password = "password";

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
            } else {
                System.out.println("提交反馈失败");
            }
        } else {
            System.out.println("用户不存在无法提交反馈");
        }
    }
}
