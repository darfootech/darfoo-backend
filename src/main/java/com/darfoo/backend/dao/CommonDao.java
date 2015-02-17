package com.darfoo.backend.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zjh on 15-2-17.
 */

//just for the common database things
public class CommonDao {
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 获取单个资源的信息
     * @param resource
     * @param vid
     * @return 抽象资源对象
     */
    public Object getResourceById(Class resource ,Integer vid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(resource);
            c.setReadOnly(true);
            c.add(Restrictions.eq("id", vid));
            return c.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
