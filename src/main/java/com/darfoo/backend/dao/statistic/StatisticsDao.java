package com.darfoo.backend.dao.statistic;

import com.darfoo.backend.dao.cota.CommonDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by zjh on 15-3-2.
 */
public class StatisticsDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    private void insertClickBehavior(Class resource, HashMap<String, Object> conditions) {
        try {
            Object object = resource.newInstance();
            for (String key : conditions.keySet()) {
                commonDao.setResourceAttr(resource, object, key, conditions.get(key));
            }

            sessionFactory.getCurrentSession().save(object);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void updateClickBehavior(Class resource, HashMap<String, Object> conditions) {
        Object object = commonDao.getResourceByFields(resource, conditions);
        try {
            Method method = resource.getDeclaredMethod("updateClickcount");
            method.invoke(object);
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void insertOrUpdateClickBehavior(Class resource, HashMap<String, Object> conditions) {
        if (commonDao.isResourceExistsByFields(resource, conditions)) {
            updateClickBehavior(resource, conditions);
        } else {
            insertClickBehavior(resource, conditions);
        }
    }
}
