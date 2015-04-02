package com.darfoo.backend.dao.cota;

import com.darfoo.backend.model.cota.annotations.HotSize;
import com.darfoo.backend.model.cota.annotations.PageSize;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 15-2-20.
 */

//for the pagination stuff 分页加载机制
public class PaginationDao {
    @Autowired
    CommonDao commonDao;

    /**
     * 获得某一类别资源的每一页要显示的记录个数
     *
     * @param resource
     * @return
     */
    public int getResourcePageSize(Class resource) {
        return ((PageSize) resource.getAnnotation(PageSize.class)).pagesize();
    }

    /**
     * 获得某一类别资源的热门资源的个数
     * @param resource
     * @return
     */
    public int getResourceHotSize(Class resource) {
        return ((HotSize) resource.getAnnotation(HotSize.class)).hotsize();
    }

    /**
     * 获得某一类资源的总页数
     *
     * @param resource
     * @return
     */
    public long getResourcePageCount(Class resource) {
        long result = 0;
        int pageSize = getResourcePageSize(resource);
        try {
            Criteria criteria = commonDao.getCommonQueryCriteria(resource);
            result = (Long) criteria.setProjection(
                    Projections.rowCount()).uniqueResult();
            return (result / pageSize) + 1;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 根据页码号来获取资源
     *
     * @param pageNo
     * @return
     */
    public List getResourcesByPage(Class resource, Integer pageNo) {
        List result = new ArrayList();
        int pageSize = getResourcePageSize(resource);

        if (pageNo > getResourcePageCount(resource)) {
            return result;
        }

        try {
            return commonDao.getCommonQueryCriteria(resource)
                    .setFirstResult((pageNo - 1) * pageSize)
                    .setMaxResults(pageSize).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return result;
        }
    }
}
