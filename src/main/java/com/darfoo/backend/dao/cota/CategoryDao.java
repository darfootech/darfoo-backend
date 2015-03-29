package com.darfoo.backend.dao.cota;

import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
     * (较快-简单—欢快-A) -> {"较快","简单","欢快","A"}
     * 一个类别属性没有选择就表示只要考虑剩下的类别属性来筛选资源记录
     *
     * @param resource
     * @param categories
     * @return
     */
    public List getResourcesByCategories(Class resource, String[] categories) {
        List result = new ArrayList();
        try {
            List<Integer> l_interact_id = new ArrayList<Integer>(); //存符合部分条件的video id
            for (int i = 0; i < categories.length; i++) {
                List<Integer> l_id = commonDao.getCommonQueryCriteria(resource)
                        .addOrder(Order.desc("id"))
                        .setProjection(Projections.property("id"))
                        .createCriteria("categories").add(Restrictions.eq("title", categories[i]))
                        .list();

                System.out.println("满足条件 " + categories[i] + " 的video数量 -> " + l_id.size());

                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                    result = new ArrayList();
                    l_interact_id.clear();//清空，表示无交集
                    break;
                } else {
                    if (l_interact_id.size() == 0) {
                        l_interact_id = l_id;
                        continue;
                    } else {
                        l_interact_id.retainAll(l_id);
                        boolean hasItersection = l_interact_id.size() > 0 ? true : false;
                        if (!hasItersection) {
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                            result = new ArrayList();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                l_interact_id = commonDao.getCommonQueryCriteria(resource)
                        .addOrder(Order.desc("id"))
                        .setProjection(Projections.property("id"))
                        .list();
            }

            if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                return commonDao.getCommonQueryCriteria(resource)
                        .addOrder(Order.desc("id"))
                        .add(Restrictions.in("id", l_interact_id))
                        .list();
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Collections.reverse(l_video);
        return result;
    }
}
