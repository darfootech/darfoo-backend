package com.darfoo.backend.dao;

/**
 * Created by zjh on 15-1-17.
 */

import com.darfoo.backend.model.UploadNoAuthVideo;
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

@Component
@SuppressWarnings("unchecked")
public class UploadNoAuthVideoDao {
    @Autowired
    SessionFactory sessionFactory;

    /*因为上传的video-key中带有了时间戳所以不会发生key重名*/

    /**
     * 插入单个上传的视频
     * @param video
     * @return
     */
    public int insertUploadVideo(UploadNoAuthVideo video){
        int res = 0;
        try{
            Session session = sessionFactory.getCurrentSession();
            Integer id = (Integer)(session.save(video));
            res = (id > 0) ? CRUDEvent.INSERT_SUCCESS : CRUDEvent.INSERT_FAIL;
        }catch(Exception e){
            //e.printStackTrace();
            res = CRUDEvent.CRUD_EXCETION;
            //throw new RuntimeException("rollback");
        }
        return res;
    }

    /**
     * 更新上传视频对应视频库中的真实id
     * @param videoid
     * @return
     */
    public int updateRealVideoid(Integer id, Integer videoid){
        int res = 0;
        try{
            Session session = sessionFactory.getCurrentSession();
            UploadNoAuthVideo uploadVideo = (UploadNoAuthVideo)session.get(UploadNoAuthVideo.class, id);
            if(uploadVideo == null){
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            }else{
                uploadVideo.setVideoid(videoid);
                session.saveOrUpdate(uploadVideo);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        }catch(Exception e){
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 根据id来获取已经上传的视频
     * @param id
     * @return
     */
    public UploadNoAuthVideo getUploadVideoById(Integer id){
        UploadNoAuthVideo video = null;
        try{
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(UploadNoAuthVideo.class);
            c.setReadOnly(true);
            c.add(Restrictions.eq("id", id));
            video = (UploadNoAuthVideo)c.uniqueResult();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return video;
    }

    /**
     * 根据id删除上传的视频
     * @param id
     * @return
     */
    public int deleteVideoById(Integer id){
        int res = 0;
        try{
            Session session = sessionFactory.getCurrentSession();
            UploadNoAuthVideo video = (UploadNoAuthVideo)session.get(UploadNoAuthVideo.class, id);
            if(video == null){
                res = CRUDEvent.DELETE_NOTFOUND;
            }else{
                session.delete(video);
                res = CRUDEvent.DELETE_SUCCESS;
            }
        }catch(Exception e){
            e.printStackTrace();
            res = CRUDEvent.DELETE_FAIL;
        }
        return res;
    }

    /**
     * 获得所有用户上传的视频
     * @return
     */
    public List<UploadNoAuthVideo> getAllVideo(){
        List<UploadNoAuthVideo> s_videos = new ArrayList<UploadNoAuthVideo>();
        try{
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(UploadNoAuthVideo.class);
            c.addOrder(Order.desc("id"));
            c.setReadOnly(true);
            s_videos = c.list();
        }catch(Exception e){
            e.printStackTrace();
        }
        return s_videos;
    }

    /**
     * 根据上传用户的mac地址获得所有该mac地址对应的视频
     * @param macaddr
     * @return
     */
    public List<UploadNoAuthVideo> getVideosByMacAddr(String macaddr){
        List<UploadNoAuthVideo> videos = null;
        try{
            Session session = sessionFactory.getCurrentSession();
            String sql = "select * from uploadnoauthvideo where mac_addr=:mac_addr and video_id > 0 order by id desc";
            videos = (List<UploadNoAuthVideo>)session.createSQLQuery(sql).addEntity(UploadNoAuthVideo.class).setString("mac_addr", macaddr).list();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return videos;
    }
}
