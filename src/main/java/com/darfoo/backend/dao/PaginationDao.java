package com.darfoo.backend.dao;

import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-2-20.
 */

//for the pagination stuff 分页加载机制
public class PaginationDao {
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 获得某一类别资源的每一页要显示的记录个数
     * @param resource
     * @return
     */
    private int getResourcePageSize(Class resource) {
        if (resource == Video.class || resource == Tutorial.class) {
            return 12;
        } else if (resource == Music.class) {
            return 22;
        } else if (resource == Author.class) {
            return 15;
        } else {
            System.out.println("something is wired");
            return 1;
        }
    }

    /**
     * 获得某一类资源的总页数
     * @param resource
     * @return
     */
    public long getResourcePageCount(Class resource) {
        long result = 0;
        int pageSize = getResourcePageSize(resource);
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);

            // 获取根据条件分页查询的总行数
            result = (Long) criteria.setProjection(
                    Projections.rowCount()).uniqueResult();
            criteria.setProjection(null);

            return (result / pageSize) + 1;
        } catch (RuntimeException re) {
            re.printStackTrace();
            return result;
        }
    }

    /**
     * 根据页码号来获取资源
     * @param pageNo
     * @return
     */
    public List getResourcesByPage(Class resource, Integer pageNo) {
        List result = new ArrayList();
        int pageSize = getResourcePageSize(resource);

        if (pageNo > getResourcePageCount(resource)) {
            return result;
        }

        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);

            criteria.setFirstResult((pageNo - 1) * pageSize);
            criteria.setMaxResults(pageSize);

            result = criteria.list();
            return result;
        } catch (RuntimeException re) {
            //re.printStackTrace();
            return result;
        }
    }
}
