package com.springapp.mvc.auth;

/**
 * Created by zjh on 15-3-3.
 */

import com.darfoo.backend.dao.auth.AuthenticateDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.auth.Bind;
import com.darfoo.backend.model.auth.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class AuthTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    AuthenticateDao authenticateDao;

    @Test
    public void prepareBind() {
        String mac = "macaddr";

        if (commonDao.isResourceExistsByField(Bind.class, "mac", mac)) {
            System.out.println("mac地址已经绑定过用户,不能被绑定了");
        } else {
            System.out.println("mac地址还没有接受过绑定");
        }
    }

    @Test
    public void bindMac() {
        String mac = "macaddr";
        String username = "username";
        String password = "password";

        boolean flag = authenticateDao.authenticate(username, password);
        if (flag) {
            System.out.println("用户已经存在");
        } else {
            System.out.println("用户不存在");
            int userid = authenticateDao.createUser(username, password);
            if (authenticateDao.createBind(userid, mac)) {
                System.out.println("绑定成功");
            } else {
                System.out.println("绑定失败");
            }
        }
    }
}
