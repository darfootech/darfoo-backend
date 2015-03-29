package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.model.cota.DanceGroupType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class DanceGroupDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CommonDao commonDao;

    private List extractAndReturnAuthorList(List<Object[]> idCntList) {
        List result = new ArrayList();
        for (Object[] rows : idCntList) {
            int authorid = (Integer) rows[1];
            System.out.println(authorid + " -> " + ((BigInteger) rows[0]).intValue());
            Object object = commonDao.getResourceById(DanceGroup.class, authorid);
            result.add(object);
        }
        return result;
    }

    public void updateAuthorType(Integer id, DanceGroupType type) {
        Class resource = DanceGroup.class;
        commonDao.saveResource(commonDao.setResourceAttr(resource, commonDao.getResourceById(resource, id), "type", type));
    }

    public List getAuthorsOrderByVideoCountDesc() {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt asc";
            result = (List<Object[]>) session.createSQLQuery(sql).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(result);
        return extractAndReturnAuthorList(result);
    }

    public List getAuthorsOrderByVideoCountDescByPage(int pageNo) {
        List<Object[]> result = new ArrayList<Object[]>();
        int pageSize = paginationDao.getResourcePageSize(DanceGroup.class);
        long pageCount = paginationDao.getResourcePageCount(DanceGroup.class);
        try {
            Session session = sessionFactory.getCurrentSession();
            //String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt desc limit " + pageSize + " offset " + (pageNo - 1) * pageSize;
            String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt desc";
            SQLQuery query = session.createSQLQuery(sql);
            //query.setFirstResult((pageNo - 1) * pageSize);
            //query.setMaxResults(pageSize);
            if (pageNo < pageCount) {
                result = ((List<Object[]>) query.list()).subList((pageNo - 1) * pageSize, pageNo * pageSize);
            } else if (pageNo == pageCount) {
                result = ((List<Object[]>) query.list()).subList((pageNo - 1) * pageSize, query.list().size());
            } else {
                System.out.println("out of bound");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractAndReturnAuthorList(result);
    }
}
