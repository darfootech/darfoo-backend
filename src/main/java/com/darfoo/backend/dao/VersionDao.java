package com.darfoo.backend.dao;

import com.darfoo.backend.model.Version;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.Expression;
import java.util.List;

/**
 * Created by zjh on 15-1-6.
 */

@Component
@SuppressWarnings("unchecked")
public class VersionDao {
    @Autowired
    SessionFactory sessionFactory;

    public boolean isExistVersion(String version, String type) {
        boolean isExist = true;
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Version.class);
            criteria.add(Restrictions.eq("version", version));
            criteria.add(Restrictions.eq("type", type));
            Version v = (Version) criteria.uniqueResult();
            isExist = (v == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public Version getLatestReleaseVersion() {
        Version version = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Version.class);
            criteria.add(Restrictions.eq("type", "release"));
            criteria.addOrder(Order.desc("id"));
            List result = criteria.list();
            if (result.size() > 0){
                version = (Version) criteria.list().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public Version getLatestDebugVersion() {
        Version version = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Version.class);
            criteria.add(Restrictions.eq("type", "debug"));
            criteria.addOrder(Order.desc("id"));
            List result = criteria.list();
            if (result.size() > 0){
                version = (Version) criteria.list().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public int insertVersion(Version version) {
        int res = 0;
        try {
            boolean isExist = isExistVersion(version.getVersion(), version.getType());
            if (isExist) {
                res = CRUDEvent.INSERT_REPEAT;
            } else {
                Session session = sessionFactory.getCurrentSession();
                Integer id = (Integer) (session.save(version));
                res = (id > 0) ? CRUDEvent.INSERT_SUCCESS : CRUDEvent.INSERT_FAIL;
            }
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            //throw new RuntimeException("rollback");
        }
        return res;
    }
}
