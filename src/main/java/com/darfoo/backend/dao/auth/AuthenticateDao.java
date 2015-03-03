package com.darfoo.backend.dao.auth;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.auth.Bind;
import com.darfoo.backend.model.auth.User;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by zjh on 15-3-3.
 */
public class AuthenticateDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    public boolean createBind(Integer userid, String macaddr) {
        Bind bind = new Bind(userid, macaddr);
        sessionFactory.getCurrentSession().save(bind);
        if (bind.id > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Integer createUser(String username, String password) {
        User user = new User(username, BCrypt.hashpw(password, BCrypt.gensalt()));
        sessionFactory.getCurrentSession().save(user);
        System.out.println(user.id);
        return user.id;
    }

    public boolean authenticate(String username, String password) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("username", username);

        User user = (User) commonDao.getResourceByFields(User.class, conditions);
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return true;
        } else {
            return false;
        }
    }
}
