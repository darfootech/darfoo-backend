package com.darfoo.backend.dao.cota;

import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
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
        if (resource == Video.class || resource == Tutorial.class) {
            return 12;
        } else if (resource == Music.class) {
            return 22;
        } else if (resource == Author.class) {
            return 15;
        } else {
            System.out.println("something is wired");
            return 1;
        }
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

    public long getResourcePageCountByCategories(Class resource, String[] categories) {
        List result = new ArrayList();
        int pageSize = getResourcePageSize(resource);

        try {
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
            for (int i = 0; i < categories.length; i++) {
                //利用projection投影只获得某一个字段的collection结果
                List<Integer> l_id = commonDao.getCommonQueryCriteria(resource)
                        .setProjection(Projections.property("id"))
                        .createCriteria("categories").add(Restrictions.eq("title", categories[i]))
                        .list();
                System.out.println("满足条件 " + categories[i] + " 的video数量 -> " + l_id.size());

                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                    result = new ArrayList();
                    l_interact_id.clear();//清空，表示无交集
                    break;
                } else {
                    if (l_interact_id.size() == 0) {
                        l_interact_id = l_id;
                        continue;
                    } else {
                        l_interact_id.retainAll(l_id);
                        boolean hasItersection = l_interact_id.size() > 0 ? true : false;
                        if (!hasItersection) {
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                            result = new ArrayList();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                result = commonDao.getCommonQueryCriteria(resource).list();
            } else if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                result = commonDao.getCommonQueryCriteria(resource)
                        .add(Restrictions.in("id", l_interact_id))
                        .list();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return (result.size() / pageSize) + 1;
    }

    public List getResourcesByCategoriesByPage(Class resource, String[] categories, Integer pageNo) {
        List result = new ArrayList();
        int pageSize = getResourcePageSize(resource);

        if (pageNo > getResourcePageCountByCategories(resource, categories)) {
            return result;
        }

        try {
            List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
            for (int i = 0; i < categories.length; i++) {
                List<Integer> l_id = commonDao.getCommonQueryCriteria(resource)
                        .addOrder(Order.desc("id"))
                        .setProjection(Projections.property("id"))
                        .createCriteria("categories").add(Restrictions.eq("title", categories[i]))
                        .setFirstResult((pageNo - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .list();

                System.out.println("满足条件 " + categories[i] + " 的video数量 -> " + l_id.size());

                if (l_id.size() == 0) {
                    //只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                    result = new ArrayList();
                    l_interact_id.clear();//清空，表示无交集
                    break;
                } else {
                    //第一次赋值给l_interact_id
                    if (l_interact_id.size() == 0) {
                        l_interact_id = l_id;
                        continue;
                    } else {
                        l_interact_id.retainAll(l_id);
                        boolean hasItersection = l_interact_id.size() > 0 ? true : false;
                        if (!hasItersection) {
                            //之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
                            result = new ArrayList();
                            break;
                        }
                    }
                }
            }
            if (categories.length == 0) {
                //categories长度为0，即没有筛选条件,返回所有视频
                l_interact_id = commonDao.getCommonQueryCriteria(resource)
                        .addOrder(Order.desc("id"))
                        .setProjection(Projections.property("id"))
                        .setFirstResult((pageNo - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .list();
            }

            if (l_interact_id.size() > 0) {
                //交集内的id数量大于0个
                return commonDao.getCommonQueryCriteria(resource)
                        .addOrder(Order.desc("id"))
                        .add(Restrictions.in("id", l_interact_id))
                        .list();
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
