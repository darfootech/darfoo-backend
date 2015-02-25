package com.darfoo.backend.dao.cota;

import com.darfoo.backend.dao.CRUDEvent;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
    @Autowired
    CommonDao commonDao;

    public int doRecommendResource(Class resource, Integer id) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);
            if (object == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                commonDao.setResourceAttr(resource, object, "recommend", 1);
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
                commonDao.setResourceAttr(resource, object, "recommend", 0);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    public List getUnRecommendResources(Class resource) {
        try {
            return commonDao.getCommonQueryCriteria(resource)
                    .add(Restrictions.or(
                            Restrictions.eq("recommend", 0),
                            Restrictions.isNull("recommend")))
                    .addOrder(Order.desc("id"))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public List getRecommendResources(Class resource) {
        try {
            return commonDao.getCommonQueryCriteria(resource)
                    .add(Restrictions.eq("recommend", 1))
                    .addOrder(Order.desc("id"))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }
}
