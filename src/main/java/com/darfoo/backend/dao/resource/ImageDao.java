package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.UpdateCheckResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public void insertSingleImage(Image image) {
        try {
            Session session = sf.getCurrentSession();
            session.save(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单个image的信息
     * 根据image的name来获得image对象
     *
     * @return image 返回一个image的实例对象(包含关联表中的数据)，详细请看Image.java类
     * *
     */
    public Image getImageByName(String name) {
        Image image = null;
        try {
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(Image.class);
            c.setReadOnly(true);
            c.add(Restrictions.eq("image_key", name));
            //设置JOIN mode，这样categories会直接加载到video类中
//            c.setFetchMode("categories", FetchMode.JOIN);
            image = (Image) c.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 更新之前需要先执行下面的方法，根据response来确定下一步操作
     * *
     */
    public UpdateCheckResponse updateImageCheck(Integer id, String newImageKey) {
        UpdateCheckResponse response = new UpdateCheckResponse();
        try {
            Session session = sf.getCurrentSession();
            Image image = (Image) session.get(Image.class, id);
            if (image == null) {
                System.out.println("没有要更新的image");
                response.setImageUpdate(1);
            } else {
                if (newImageKey.equals(image.getImage_key())) {
                    //重复插入
                    System.out.println("重复插入相同的imagekey,不需要进行更新");
                    response.setImageUpdate(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 更新Image（需要先完成check）
     * *
     */
    public int updateImage(Integer id, String newImageKey) {
        int res;
        try {
            Session session = sf.getCurrentSession();
            String sql = "update image set IMAGE_KEY=:key where id=:id";
            if (session.createSQLQuery(sql).setString("key", newImageKey).setInteger("id", id).executeUpdate() > 0) {
                res = CRUDEvent.UPDATE_SUCCESS;
            } else {
                res = CRUDEvent.UPDATE_IMAGE_NOTFOUND;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 删除Image
     * (先解除与video music tutorial author对应image_id的约束，将image_id设为null)
     * *
     */
    public int deleteImageById(Integer id) {
        int res;
        try {
            Session session = sf.getCurrentSession();
            Image image = (Image) session.get(Image.class, id);
            if (image == null) {
                System.out.println("没有找到对应id为" + id + "的Author");
                res = CRUDEvent.DELETE_NOTFOUND;
            } else {
                String sql1 = "update video set IMAGE_ID=null where IMAGE_ID=:image_id";
                String sql2 = "update tutorial set IMAGE_ID=null where IMAGE_ID=:image_id";
                String sql3 = "update music set IMAGE_ID=null where IMAGE_ID=:image_id";
                String sql4 = "update author set IMAGE_ID=null where IMAGE_ID=:image_id";
                System.out.println("video受影响的行数:" + session.createSQLQuery(sql1).setInteger("image_id", id).executeUpdate());
                System.out.println("tutorial受影响的行数:" + session.createSQLQuery(sql2).setInteger("image_id", id).executeUpdate());
                System.out.println("music受影响的行数:" + session.createSQLQuery(sql3).setInteger("image_id", id).executeUpdate());
                System.out.println("author受影响的行数:" + session.createSQLQuery(sql4).setInteger("image_id", id).executeUpdate());
                session.delete(image);
                res = CRUDEvent.DELETE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.DELETE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

}
