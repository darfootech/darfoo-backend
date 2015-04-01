package com.darfoo.backend.dao.cota;

import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-2-20.
 */

//数据库初始化的时候需要运行这些东西来插入资源的类别
public class CategoryDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    public void insertResourceCategories(Class resource, String[] categories) {
        try {
            Session session = sessionFactory.getCurrentSession();
            for (String category : categories) {
                Object object = resource.newInstance();
                Field titleField = resource.getDeclaredField("title");
                Field descField = resource.getDeclaredField("description");
                titleField.setAccessible(true);
                descField.setAccessible(true);

                titleField.set(object, category);
                descField.set(object, "待定");

                session.save(object);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    //插入所有欣赏视频的类型
    public void insertAllDanceVideoCategories() {
        String[] categories = {"正面教学", "口令分解", "背面教学", "队形教学"};

        insertResourceCategories(DanceVideoCategory.class, categories);
    }

    //插入所有舞蹈伴奏的类型
    public void insertAllDanceMusicCategories() {
        String[] categories = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        insertResourceCategories(DanceMusicCategory.class, categories);
    }

    /**
     * 根据类别返回满足类别筛选条件的所有资源记录
     *
     * @param resource
     * @param category
     * @return
     */
    public List getResourcesByCategory(Class resource, String category) {
        List resultids = commonDao.getCommonQueryCriteria(resource)
                .addOrder(Order.desc("id"))
                        //如果不映射一下就会查询超时
                .setProjection(Projections.property("id"))
                .createCriteria("categories").add(Restrictions.eq("title", category))
                .list();

        return commonDao.getCommonQueryCriteria(resource)
                .addOrder(Order.desc("id"))
                .add(Restrictions.in("id", resultids))
                .list();
    }

    public List getResourcesByCategoryWithFields(Class resource, String category, HashMap<String, Object> conditions) {
        Criteria criteria = commonDao.getCommonQueryCriteria(resource)
                .addOrder(Order.desc("id"));

        for (String key : conditions.keySet()) {
            criteria.add(Restrictions.eq(key, conditions.get(key)));
        }

        //如果不映射一下就会查询超时
        List resultids = criteria.setProjection(Projections.property("id"))
                .createCriteria("categories").add(Restrictions.eq("title", category))
                .list();

        return commonDao.getCommonQueryCriteria(resource)
                .addOrder(Order.desc("id"))
                .add(Restrictions.in("id", resultids))
                .list();
    }
}
