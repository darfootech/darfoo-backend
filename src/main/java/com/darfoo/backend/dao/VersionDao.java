package com.darfoo.backend.dao;

import com.darfoo.backend.model.Version;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zjh on 15-1-6.
 */

@Component
@SuppressWarnings("unchecked")
public class VersionDao {
    @Autowired
    SessionFactory sessionFactory;

    public boolean isExistVersion(String version) {
        boolean isExist = true;
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "select * from version where version=:version";
            Version v = (Version) session.createSQLQuery(sql).addEntity(Version.class).setString("version", version).uniqueResult();
            isExist = (v == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public Version getLatestVersion() {
        Version version = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "SELECT * FROM version ORDER BY id DESC LIMIT 1";
            version = (Version) session.createSQLQuery(sql).addEntity(Version.class).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public int insertVersion(Version version) {
        int res = 0;
        try {
            boolean isExist = isExistVersion(version.getVersion());
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
