package com.darfoo.backend.dao.cota;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Video;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-2-21.
 */

//一些资源需要添加或者去掉伴奏
public class AccompanyDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    /**
     * 为资源添加一个对应的伴奏music(插入操作用更新替代)
     * @param resource
     * @param id
     * @param musicid
     * @return
     */
    public int updateResourceMusic(Class resource, Integer id, Integer musicid) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);
            if (object != null) {
                Music music = (Music) session.createCriteria(Music.class).add(Restrictions.eq("id", musicid)).uniqueResult();
                if (music == null) {
                    System.out.println("被更新的music的id在music表中未发现对应记录，请先完成music的插入");
                    res = CRUDEvent.UPDATE_MUSIC_NOTFOUND;
                } else {
                    commonDao.setResourceAttr(resource, object, "music", music);
                    res = CRUDEvent.UPDATE_SUCCESS;
                }
            } else {
                System.out.println("vid对应的video未找到");
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 删除资源中的music(就是将MUSIC_ID字段设为null)
     * @param resource
     * @param id
     * @return
     */
    public int deleteMusicFromResource(Class resource, Integer id) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);
            if (object != null) {
                Object music = commonDao.getResourceAttr(resource, object, "music");
                if (music != null) {
                    commonDao.setResourceAttr(resource, object, "music", null);
                } else {
                    System.out.println("music_id 已经为null");
                }
                res = CRUDEvent.DELETE_SUCCESS;
            } else {
                res = CRUDEvent.DELETE_NOTFOUND;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = CRUDEvent.DELETE_FAIL;
        }
        return res;
    }

    /**
     * 根据musicid来获得不与之关联的所有资源记录
     * @param resource
     * @param musicid
     * @return
     */
    public List getResourcesWithoutMusicId(Class resource, int musicid) {
        List result = new ArrayList();
        try {
            return commonDao.getCommonQueryCriteria(resource)
                .addOrder(Order.desc("id"))
                .add(Restrictions.or(Restrictions.not(Restrictions.eq("music.id", musicid)), Restrictions.isNull("music.id")))
                .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}