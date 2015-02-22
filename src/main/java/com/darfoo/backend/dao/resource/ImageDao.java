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
}
