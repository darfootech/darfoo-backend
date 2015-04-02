package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class DanceGroupDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;

    private List extractAndReturnDanceGroupList(List<Object[]> idCntList) {
        List result = new ArrayList();
        for (Object[] rows : idCntList) {
            int authorid = (Integer) rows[1];
            System.out.println(authorid + " -> " + ((BigInteger) rows[0]).intValue());
            Object object = commonDao.getResourceById(DanceGroup.class, authorid);
            result.add(object);
        }
        return result;
    }

    /**
     * 根据舞队关联的舞蹈视频的数量来进行倒排序获得所有舞队
     *
     * @return
     */
    public List getDanceGroupsOrderByVideoCountDesc() {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "select IFNULL(v.cnt, 0) as count, dancegroup.id as id from dancegroup left outer join (select count(*) as cnt, author_id as gid from dancevideo group by author_id)v on dancegroup.id = v.gid order by v.cnt desc;";
            result = (List<Object[]>) session.createSQLQuery(sql).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Collections.reverse(result);
        return extractAndReturnDanceGroupList(result);
    }
}
