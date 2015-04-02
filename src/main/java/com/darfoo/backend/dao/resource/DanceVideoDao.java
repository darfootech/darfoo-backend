package com.darfoo.backend.dao.resource;

import com.darfoo.backend.model.cota.enums.DanceVideoType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zjh on 15-3-31.
 */

@Component
@SuppressWarnings("unchecked")
public class DanceVideoDao {
    @Autowired
    SessionFactory sessionFactory;

    //当舞队类型改变时 其关联的舞蹈视频的类型也要相应变化
    public void updateDanceVideoTypeWithDanceGroupId(Integer danceGroupId, DanceVideoType targetType) {
        Session session = sessionFactory.getCurrentSession();
        String prepareSql = "update dancevideo set video_type = :video_type where author_id = :author_id";
        session.createSQLQuery(prepareSql).setInteger("author_id", danceGroupId).setInteger("video_type", targetType.ordinal()).executeUpdate();
    }
}
