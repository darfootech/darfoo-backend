package com.darfoo.backend.dao;

import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.Tutorial;
import com.darfoo.backend.model.Video;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
     *
     * @param resource
     * @param vid
     * @return 抽象资源对象
     */
    public Object getResourceById(Class resource, Integer vid) {
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
            return new Object();
        }
    }

    /**
     * 获得某一类的所有记录
     * @param resource
     * @return
     */
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
            return new ArrayList();
        }
    }

    /**
     * 获得除了传入id对应记录的所有类别的记录
     * @param resource
     * @param id
     * @return
     */
    public List getAllResourceWithoutId(Class resource, Integer id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.add(Restrictions.not(Restrictions.eq("id", id)));
            criteria.addOrder(Order.desc("id"));
            criteria.setReadOnly(true);
            //c.setFetchMode("categories", FetchMode.JOIN);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 获得热度排名前count个
     * @param resource
     * @param count
     * @return
     */
    public List getResourcesByHottest(Class resource, Integer count) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.addOrder(Order.desc("hottest"));//安热度递减排序
            criteria.setMaxResults(count);
            criteria.setReadOnly(true);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 根据name或者title字段的值获取资源实例
     *
     * @param resource
     * @param titlename
     * @param type
     * @return
     */
    public Object getResourceByTitleOrName(Class resource, String titlename, String type) {
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
            return new Object();
        }
    }

    /**
     * 删除单个资源
     * @param resource
     * @param id
     * @return
     */
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

    /**
     * 根据内容搜索资源
     *
     * @param resource
     * @param searchContent
     * @return 最多返回50个结果
     */
    public List getResourceBySearch(Class resource, String searchContent) {
        List result = new ArrayList();
        try {
            char[] s = searchContent.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s.length; i++) {
                sb.append("%");
                sb.append(s[i]);
            }
            sb.append("%");
            System.out.println("匹配>>>>" + sb.toString());
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource).setProjection(Projections.property("id"));
            if (ifHasCategoryResource(resource)) {
                criteria.add(Restrictions.like("title", sb.toString(), MatchMode.ANYWHERE));
            } else if (resource == Author.class) {
                criteria.add(Restrictions.like("name", sb.toString(), MatchMode.ANYWHERE));
            } else {
                System.out.println("something is bad");
            }
            List<Integer> l_id = criteria.list();
            if (l_id.size() > 0) {
                criteria = session.createCriteria(resource).add(Restrictions.in("id", l_id));
                criteria.setMaxResults(50);
                criteria.setReadOnly(true);
                result = criteria.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
