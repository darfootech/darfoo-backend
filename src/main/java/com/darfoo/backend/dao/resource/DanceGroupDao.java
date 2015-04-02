package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.cota.enums.DanceGroupType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
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
            int dancegroupid = (Integer) rows[1];
            System.out.println(dancegroupid + " -> " + ((BigInteger) rows[0]).intValue());
            Object object = commonDao.getResourceById(DanceGroup.class, dancegroupid);
            result.add(object);
        }
        return result;
    }

    /**
     * excludeids指名了返回的舞队列表中不能包含有这个set中包含的id指向的舞队资源
     * @param idCntList
     * @param excludeids
     * @return
     */
    private List extractAndReturnDanceGroupList(List<Object[]> idCntList, HashSet<Integer> excludeids) {
        List result = new ArrayList();
        for (Object[] rows : idCntList) {
            int dancegroupid = (Integer) rows[1];
            if (!excludeids.contains(dancegroupid)) {
                System.out.println(dancegroupid + " -> " + ((BigInteger) rows[0]).intValue());
                Object object = commonDao.getResourceById(DanceGroup.class, dancegroupid);
                result.add(object);
            }
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
            String sql = "select IFNULL(v.cnt, 0) as count, dancegroup.id as id from dancegroup left outer join (select count(*) as cnt, author_id as gid from dancevideo group by author_id)v on dancegroup.id = v.gid order by v.cnt desc";
            result = (List<Object[]>) session.createSQLQuery(sql).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Collections.reverse(result);
        return extractAndReturnDanceGroupList(result);
    }

    /**
     * 根据舞队关联的舞蹈视频的数量来进行倒排序获得所有舞队 不包括excludeids中包含的id指向的舞队资源
     * @param excludeids
     * @return
     */
    public List getDanceGroupsOrderByVideoCountDesc(DanceGroupType type, HashSet<Integer> excludeids) {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = String.format("select IFNULL(v.cnt, 0) as count, dancegroup.id as id from dancegroup left outer join (select count(*) as cnt, author_id as gid from dancevideo group by author_id)v on dancegroup.id = v.gid where dancegroup.type = %d order by v.cnt desc", type.ordinal());
            result = (List<Object[]>) session.createSQLQuery(sql).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractAndReturnDanceGroupList(result, excludeids);
    }
}
