package com.darfoo.backend.dao;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Version;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-1-6.
 */

@Component
@SuppressWarnings("unchecked")
public class VersionDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    public Version getLatestVersion(Class resource, String type) {
        Version version = null;
        try {
            List result = commonDao.getCommonQueryCriteria(resource)
                    .add(Restrictions.eq("type", type))
                    .addOrder(Order.desc("id"))
                    .list();
            if (result.size() > 0) {
                version = (Version) result.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public int insertVersion(Version version) {
        int res;
        try {
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("version", version.getVersion());
            conditions.put("type", version.getType());
            boolean isExist = commonDao.isResourceExistsByFields(Version.class, conditions);
            if (isExist) {
                res = CRUDEvent.INSERT_REPEAT;
            } else {
                Session session = sessionFactory.getCurrentSession();
                Integer id = (Integer) (session.save(version));
                res = (id > 0) ? CRUDEvent.INSERT_SUCCESS : CRUDEvent.INSERT_FAIL;
            }
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
        }
        return res;
    }
}
