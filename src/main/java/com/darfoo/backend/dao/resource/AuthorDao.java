package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.model.resource.Author;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class AuthorDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CommonDao commonDao;

    private List extractAndReturnAuthorList(List<Object[]> idCntList) {
        List result = new ArrayList();
        for (Object[] rows : idCntList){
            int authorid = (Integer)rows[1];
            System.out.println(authorid + " -> " + ((BigInteger)rows[0]).intValue());
            Object object = commonDao.getResourceById(Author.class, authorid);
            result.add(object);
        }
        return result;
    }

    public List<Object[]> getAuthorsOrderByVideoCountDesc() {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt asc";
            result = (List<Object[]>) session.createSQLQuery(sql).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractAndReturnAuthorList(result);
    }

    public List<Object[]> getAuthorsOrderByVideoCountDescByPage(int pageNo) {
        List<Object[]> result = new ArrayList<Object[]>();
        int pageSize = paginationDao.getResourcePageSize(Author.class);
        long pageCount = paginationDao.getResourcePageCount(Author.class);
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
        Collections.reverse(result);
        return extractAndReturnAuthorList(result);
    }
}
