package com.darfoo.backend.dao.cota;

import com.darfoo.backend.dao.CRUDEvent;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-2-19.
 */

//just for the recommend database things
public class RecommendDao {
    @Autowired
    private SessionFactory sessionFactory;

    public int doRecommendResource(Class resource, Integer id) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);
            if (object == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                Field field = resource.getDeclaredField("recommend");
                field.setAccessible(true);
                field.set(object, 1);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    public int unRecommendResource(Class resource, Integer id) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);
            if (object == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                Field field = resource.getDeclaredField("recommend");
                field.setAccessible(true);
                field.set(object, 0);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    public List getRecommendResources(Class resource) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);
            criteria.add(Restrictions.eq("recommend", 1));
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }
}
