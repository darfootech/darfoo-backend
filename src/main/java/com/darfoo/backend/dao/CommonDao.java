package com.darfoo.backend.dao;

import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.model.Video;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zjh on 15-2-17.
 */

//just for the common database things
public class CommonDao {
    @Autowired
    private SessionFactory sessionFactory;

    private boolean ifHasCategoryResource(Class resource) {
        if (resource == Video.class || resource == Tutorial.class || resource == Music.class) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取单个资源的信息
     * @param resource
     * @param vid
     * @return 抽象资源对象
     */
    public Object getResourceById(Class resource ,Integer vid) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);
            criteria.add(Restrictions.eq("id", vid));
            if (ifHasCategoryResource(resource)) {
                //设置JOIN mode，这样categories会直接加载到返回的实例中
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }
            return criteria.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List getAllResource(Class resource) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.addOrder(Order.desc("id"));
            criteria.setReadOnly(true);
            //如果fetch了就会出现duplicate的情况 反正需要得到类别的时候直接从单个资源那里fetch就行了
            /*if (ifHasCategoryResource(resource)) {
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }*/
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据name或者title字段的值获取资源实例
     * @param resource
     * @param titlename
     * @param type
     * @return
     */
    public Object getResourceByTitleOrName(Class resource, String titlename, String type){
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);
            if (type.equals("title")) {
                criteria.add(Restrictions.eq("title", titlename));
            } else if (type.equals("name")) {
                criteria.add(Restrictions.eq("name", titlename));
            }
            if (ifHasCategoryResource(resource)) {
                //设置JOIN mode，这样categories会直接加载到返回的实例中
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }
            return criteria.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteResourceById(Class resource, Integer id) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object target = session.get(resource, id);
            if (target == null) {
                res = CRUDEvent.DELETE_NOTFOUND;
            } else {
                session.delete(target);
                res = CRUDEvent.DELETE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = CRUDEvent.DELETE_FAIL;
        }
        return res;
    }
}
