package com.darfoo.backend.dao.resource;

import com.darfoo.backend.model.resource.Author;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class AuthorDao {
    @Autowired
    SessionFactory sf;

    private int pageSize = 15;

    /**
     * 根据name判断该author是否已经存在表里
     *
     * @param name 待判断的author的name
     * @return 表中已经存在该name对应的作者信息, 返回true;反之，返回一个false
     * *
     */
    public boolean isExistAuthor(String name) {
        boolean isExist = true;
        try {
            Session session = sf.getCurrentSession();
            Criteria criteria = session.createCriteria(Author.class);
            criteria.setReadOnly(true);
            criteria.add(Restrictions.eq("name", name));
            Author author = (Author) criteria.uniqueResult();
            isExist = (author == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public List<Object[]> getAuthorOrderByVideoCountDesc() {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            Session session = sf.getCurrentSession();
            String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt asc";
            result = (List<Object[]>) session.createSQLQuery(sql).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*分页机制*/
    public long getPageCount() {
        long result = 0;
        try {
            Session session = sf.getCurrentSession();
            Criteria criteria = session.createCriteria(Author.class);

            // 获取根据条件分页查询的总行数
            result = (Long) criteria.setProjection(
                    Projections.rowCount()).uniqueResult();
            criteria.setProjection(null);

            return (result / pageSize) + 1;
        } catch (RuntimeException re) {
            re.printStackTrace();
            return result;
        }
    }

    public List<Object[]> getAuthorOrderByVideoCountDescByPage(int pageNo) {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            Session session = sf.getCurrentSession();
            //String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt desc limit " + pageSize + " offset " + (pageNo - 1) * pageSize;
            String sql = "select vv.count + tt.count as cnt, vv.id as aid from (select IFNULL(v.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from video group by author_id)v on author.id = v.mid order by v.cnt desc)vv left outer join (select IFNULL(t.cnt, 0) as count, author.id as id from author left outer join (select count(*) as cnt, author_id as mid from tutorial group by author_id)t on author.id = t.mid order by t.cnt desc)tt on vv.id = tt.id order by cnt desc";
            SQLQuery query = session.createSQLQuery(sql);
            //query.setFirstResult((pageNo - 1) * pageSize);
            //query.setMaxResults(pageSize);
            if (pageNo < getPageCount()) {
                result = ((List<Object[]>) query.list()).subList((pageNo - 1) * pageSize, pageNo * pageSize);
            } else if (pageNo == getPageCount()) {
                result = ((List<Object[]>) query.list()).subList((pageNo - 1) * pageSize, query.list().size());
            } else {
                System.out.println("out of bound");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(result);
        return result;
    }
}
