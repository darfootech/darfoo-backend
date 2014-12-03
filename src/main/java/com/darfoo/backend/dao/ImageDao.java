package com.darfoo.backend.dao;

import java.util.ArrayList;
import java.util.List;

import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Image;

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
public class ImageDao {
    @Autowired
    private SessionFactory sf;

    //插入单个图片
    @SuppressWarnings("unchecked")
    public void inserSingleImage(Image image){
        try{
            Session session = sf.getCurrentSession();
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
    public Image getImageByName(String name){
        Image image = null;
        try{
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(Image.class);
            c.setReadOnly(true);
            c.add(Restrictions.eq("image_key", name));
            //设置JOIN mode，这样categories会直接加载到video类中
//            c.setFetchMode("categories", FetchMode.JOIN);
            image = (Image)c.uniqueResult();
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }
    
    /**
     * 获取所有的image
     * @return List<Image>
     * **/
	public List<Image> getAllImage(){
		List<Image> l_image = new ArrayList<Image>();
		try{
			Session session = sf.getCurrentSession();
			String sql = "select * from image";
			l_image = session.createSQLQuery(sql).addEntity(Image.class).list();
		}catch(Exception e){			
			e.printStackTrace();
		}
		return l_image;
		
	}
	
	/**
	 * 更新Image
	 * (更新imageKey意义不大，不如直接插入一个新的)
	 * **/
//	public void updateImage(Integer id,String imageKey){
//		
//	}
	
}
