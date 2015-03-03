package com.darfoo.backend.dao.statistic;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.cota.ModelAttrSuper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by zjh on 15-3-2.
 */
public class StatisticsDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    public void insertBehavior(Class resource, HashMap<String, Object> conditions) {
        System.out.println("insert resource");
        try {
            Object object = resource.newInstance();

            for (Field field : resource.getFields()) {
                String fieldname = field.getName();
                if (conditions.keySet().contains(fieldname)) {
                    if (field.isAnnotationPresent(ModelAttrSuper.class)) {
                        commonDao.setResourceAttr(resource.getSuperclass(), object, fieldname, conditions.get(fieldname));
                    } else {
                        commonDao.setResourceAttr(resource, object, fieldname, conditions.get(fieldname));
                    }
                }
                if (fieldname.equals("hottest")) {
                    commonDao.setResourceAttr(resource.getSuperclass(), object, fieldname, 1L);
                }
            }
            sessionFactory.getCurrentSession().save(object);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateClickBehavior(Class resource, HashMap<String, Object> conditions) {
        Object object = commonDao.getResourceByFields(resource, conditions);
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(resource.getSuperclass(), object, "hottest")));
    }

    public void insertOrUpdateClickBehavior(Class resource, HashMap<String, Object> conditions) {
        if (commonDao.isResourceExistsByFields(resource, conditions)) {
            updateClickBehavior(resource, conditions);
        } else {
            insertBehavior(resource, conditions);
        }
    }
}
