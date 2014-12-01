package com.darfoo.backend.dao;

import com.darfoo.backend.model.DanceGroupImage;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zjh on 14-12-1.
 */

@Component
@SuppressWarnings("unchecked")
public class DanceGroupImageDao {
    @Autowired
    private SessionFactory sessionFactory;

    //插入单个图片
    @SuppressWarnings("unchecked")
    public void inserSingleImage(DanceGroupImage image){
        try{
            Session session = sessionFactory.getCurrentSession();
            session.save(image);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取单个image的信息
     * 根据image的name来获得image对象
     * @return image 返回一个image的实例对象(包含关联表中的数据)，详细请看Image.java类
     * **/
    public DanceGroupImage getImageByName(String name){
        DanceGroupImage image = null;
        try{
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(DanceGroupImage.class);
            c.setReadOnly(true);
            c.add(Restrictions.eq("image_key", name));
            //设置JOIN mode，这样categories会直接加载到video类中
            c.setFetchMode("categories", FetchMode.JOIN);
            image = (DanceGroupImage)c.uniqueResult();
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }
}
