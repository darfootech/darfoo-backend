package com.darfoo.backend.dao;

import com.darfoo.backend.model.UploadVideo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-1-11.
 */

@Component
@SuppressWarnings("unchecked")
public class UploadVideoDao {
    @Autowired
    SessionFactory sessionFactory;

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
            String sql = "select * from uploadvideo where video_key=:videokey";
            UploadVideo video = (UploadVideo) session.createSQLQuery(sql).addEntity(UploadVideo.class).setString("videokey", videokey).uniqueResult();
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
    public int insertUploadVideo(UploadVideo video) {
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
            res = CRUDEvent.CRUD_EXCETION;
            //throw new RuntimeException("rollback");
        }
        return res;
    }

    /**
     * 更新上传视频对应视频库中的真实id
     *
     * @param videoid
     * @return
     */
    public int updateRealVideoid(Integer id, Integer videoid) {
        int res = 0;
        try {
            Session session = sessionFactory.getCurrentSession();
            UploadVideo uploadVideo = (UploadVideo) session.get(UploadVideo.class, id);
            if (uploadVideo == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                uploadVideo.setVideoid(videoid);
                session.saveOrUpdate(uploadVideo);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 根据id来获取已经上传的视频
     *
     * @param id
     * @return
     */
    public UploadVideo getUploadVideoById(Integer id) {
        UploadVideo video = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(UploadVideo.class);
            c.setReadOnly(true);
            c.add(Restrictions.eq("id", id));
            video = (UploadVideo) c.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return video;
    }

    /**
     * 根据id删除上传的视频
     *
     * @param id
     * @return
     */
    public int deleteVideoById(Integer id) {
        int res = 0;
        try {
            Session session = sessionFactory.getCurrentSession();
            UploadVideo video = (UploadVideo) session.get(UploadVideo.class, id);
            if (video == null) {
                res = CRUDEvent.DELETE_NOTFOUND;
            } else {
                session.delete(video);
                res = CRUDEvent.DELETE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = CRUDEvent.DELETE_FAIL;
        }
        return res;
    }

    /**
     * 获得所有用户上传的视频
     *
     * @return
     */
    public List<UploadVideo> getAllVideo() {
        List<UploadVideo> s_videos = new ArrayList<UploadVideo>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(UploadVideo.class);
            c.addOrder(Order.desc("id"));
            c.setReadOnly(true);
            s_videos = c.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s_videos;
    }

    /**
     * 根据上传用户的用户id获得所有该用户上传的视频
     *
     * @param userid
     * @return
     */
    public List<UploadVideo> getVideosByUserId(int userid) {
        List<UploadVideo> videos = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "select * from uploadvideo where user_id=:userid and video_id > 0 order by id desc";
            videos = (List<UploadVideo>) session.createSQLQuery(sql).addEntity(UploadVideo.class).setInteger("userid", userid).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return videos;
    }
}
