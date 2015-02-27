package com.darfoo.backend.dao.upload;

/**
 * Created by zjh on 15-1-17.
 */

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.upload.UploadNoAuthVideo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class UploadNoAuthVideoDao {
    @Autowired
    SessionFactory sessionFactory;

    /*因为上传的video-key中带有了时间戳所以不会发生key重名*/

    /**
     * 查看是否已经有相同的标识的视频被上传了
     *
     * @param videokey
     * @return
     */
    public boolean isExistVideo(String videokey) {
        boolean isExist = true;
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(UploadNoAuthVideo.class);
            criteria.setReadOnly(true);
            criteria.add(Restrictions.eq("video_key", videokey));
            UploadNoAuthVideo video = (UploadNoAuthVideo) criteria.uniqueResult();
            isExist = (video == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isExist;
    }

    /**
     * 插入单个上传的视频
     *
     * @param video
     * @return
     */
    public int insertUploadVideo(UploadNoAuthVideo video) {
        int res = 0;
        try {
            boolean isExist = isExistVideo(video.getVideo_key());
            if (isExist) {
                res = CRUDEvent.INSERT_REPEAT;
            } else {
                Session session = sessionFactory.getCurrentSession();
                Integer id = (Integer) (session.save(video));
                res = (id > 0) ? CRUDEvent.INSERT_SUCCESS : CRUDEvent.INSERT_FAIL;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            res = CRUDEvent.CRUD_EXCETION;
            //throw new RuntimeException("rollback");
        }
        return res;
    }
}
