package com.darfoo.backend.dao.cota;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by zjh on 15-2-17.
 */

//just for the common database things
public class CommonDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    AccompanyDao accompanyDao;

    private boolean ifHasCategoryResource(Class resource) {
        if (resource == Video.class || resource == Tutorial.class || resource == Music.class) {
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, Integer> insertResource(Class resource, HashMap<String, String> insertcontents) {
        Set<String> categoryTitles = new HashSet<String>();

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        boolean isCategoryHasSingleChar = false;

        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria;

            Object object = resource.newInstance();

            for (String key : insertcontents.keySet()) {
                System.out.println(key);
                if (key.equals("title")) {
                    Field field = resource.getDeclaredField("title");
                    field.setAccessible(true);

                    String title = insertcontents.get(key);
                    String authorname = insertcontents.get("authorname");

                    if (resource == Video.class || resource == Tutorial.class) {
                        Author author = (Author) getResourceByTitleOrName(Author.class, authorname, "name");

                        if (author == null) {
                            System.out.println("作者还不存在");
                            resultMap.put("statuscode", 502);
                            resultMap.put("insertid", -1);
                            return resultMap;
                        }

                        int authorid = author.getId();

                        HashMap<String, Object> conditions = new HashMap<String, Object>();
                        conditions.put("title", title);
                        conditions.put("author_id", authorid);

                        Object queryVideo = getResourceByFields(resource, conditions);

                        if (queryVideo == null) {
                            System.out.println("视频名字和作者id组合不存在，可以进行插入");
                            field.set(object, title);

                            Field keyField = resource.getDeclaredField("video_key");
                            keyField.setAccessible(true);
                            keyField.set(object, title + System.currentTimeMillis());
                        } else {
                            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
                            resultMap.put("statuscode", 500);
                            resultMap.put("insertid", -1);
                            return resultMap;
                        }
                    } else if (resource == Music.class) {
                        HashMap<String, Object> conditions = new HashMap<String, Object>();
                        conditions.put("title", title);
                        conditions.put("authorname", authorname);
                        Object queryMusic = getResourceByFields(resource, conditions);

                        if (queryMusic == null) {
                            System.out.println("伴奏名字和作者名字组合不存在，可以进行插入");
                            field.set(object, title);

                            Field keyField = resource.getDeclaredField("music_key");
                            keyField.setAccessible(true);
                            keyField.set(object, title + System.currentTimeMillis());
                        } else {
                            System.out.println("伴奏名字和作者名字组合已存在，不可以进行插入了，是否需要修改");
                            resultMap.put("statuscode", 505);
                            resultMap.put("insertid", -1);
                            return resultMap;
                        }
                    } else {
                        System.out.println("wired");
                    }
                } else if (key.equals("imagekey")) {
                    String imagekey = insertcontents.get(key);

                    if (!ServiceUtils.isValidImageKey(imagekey)) {
                        resultMap.put("statuscode", 504);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    }

                    criteria = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                    if (criteria.list().size() == 1) {
                        System.out.println("相同imagekey的图片已经存在了");
                        resultMap.put("statuscode", 501);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    } else {
                        Field field = resource.getDeclaredField("image");
                        field.setAccessible(true);
                        field.set(object, new Image(imagekey));
                    }
                } else if (key.equals("authorname")) {
                    String authorname = insertcontents.get(key);
                    if (resource == Video.class || resource == Tutorial.class) {
                        criteria = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                        if (criteria.list().size() == 1) {
                            Field field = resource.getDeclaredField("author");
                            field.setAccessible(true);
                            field.set(object, criteria.uniqueResult());
                        } else {
                            System.out.println("作者还不存在");
                            resultMap.put("statuscode", 502);
                            resultMap.put("insertid", -1);
                            return resultMap;
                        }
                    } else if (resource == Music.class) {
                        Field field = resource.getDeclaredField("authorname");
                        field.setAccessible(true);
                        field.set(object, authorname);
                    } else {
                        System.out.println("authorname something is wired class -> " + resource.getName());
                    }
                } else if (key.contains("category")) {
                    String category = insertcontents.get(key);
                    if (ServiceUtils.isSingleCharacter(category)) {
                        isCategoryHasSingleChar = true;
                    }
                    categoryTitles.add(category);
                } else {
                    System.out.println("key something is wired key -> " + key);
                }
            }

            if (ifHasCategoryResource(resource)) {
                if (resource == Video.class || resource == Music.class) {
                    if (!isCategoryHasSingleChar) {
                        resultMap.put("statuscode", 503);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    }
                }

                Class categoryClass = null;
                Set categories;
                if (resource == Video.class) {
                    categoryClass = VideoCategory.class;
                }

                if (resource == Tutorial.class) {
                    categoryClass = TutorialCategory.class;
                }

                if (resource == Music.class) {
                    categoryClass = MusicCategory.class;
                }

                criteria = session.createCriteria(categoryClass).add(Restrictions.in("title", categoryTitles));
                List categoryList = criteria.list();
                categories = new HashSet(categoryList);

                Field field = resource.getDeclaredField("categories");
                field.setAccessible(true);
                field.set(object, categories);
            }

            Field timeField = resource.getDeclaredField("update_timestamp");
            timeField.setAccessible(true);
            timeField.set(object, System.currentTimeMillis());

            session.save(object);

            Field field = resource.getDeclaredField("id");
            field.setAccessible(true);
            int insertid = (Integer) field.get(object);

            if (resource == Video.class || resource == Tutorial.class) {
                HashMap<String, Object> updateMap = new HashMap<String, Object>();
                updateMap.put("video_key", insertcontents.get("title") + "-" + insertid + "." + insertcontents.get("videotype"));
                updateResourceFieldsById(resource, insertid, updateMap);

                String connectmusic = insertcontents.get("connectmusic");
                if (!connectmusic.equals("")) {
                    int mid = Integer.parseInt(connectmusic.split("-")[2]);
                    accompanyDao.updateResourceMusic(resource, insertid, mid);
                }
            }

            if (resource == Music.class) {
                HashMap<String, Object> updateMap = new HashMap<String, Object>();
                updateMap.put("music_key", insertcontents.get("title") + "-" + insertid);
                updateResourceFieldsById(resource, insertid, updateMap);
            }

            resultMap.put("statuscode", 200);
            resultMap.put("insertid", insertid);
            return resultMap;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        resultMap.put("statuscode", 404);
        resultMap.put("insertid", -1);
        return resultMap;
    }

    /**
     * 获取单个资源的信息
     *
     * @param resource
     * @param id
     * @return 抽象资源对象
     */
    public Object getResourceById(Class resource, Integer id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);
            criteria.add(Restrictions.eq("id", id));
            if (ifHasCategoryResource(resource)) {
                //设置JOIN mode，这样categories会直接加载到返回的实例中
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }
            return criteria.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
    }

    /**
     * 获得某一类的所有记录
     *
     * @param resource
     * @return
     */
    public List getAllResource(Class resource) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.addOrder(Order.desc("id"));
            criteria.setReadOnly(true);
            //如果fetch了就会出现duplicate的情况 反正需要得到类别的时候直接从单个资源那里fetch就行了
            /*if (ifHasCategoryResource(resource)) {
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }*/
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 获得除了传入id对应记录的所有类别的记录
     *
     * @param resource
     * @param id
     * @return
     */
    public List getAllResourceWithoutId(Class resource, Integer id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.add(Restrictions.not(Restrictions.eq("id", id)));
            criteria.addOrder(Order.desc("id"));
            criteria.setReadOnly(true);
            //c.setFetchMode("categories", FetchMode.JOIN);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 根据字段值来获取单个资源
     *
     * @param resource
     * @param conditions
     * @return
     */
    public Object getResourceByFields(Class resource, HashMap<String, Object> conditions) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);

            for (String key : conditions.keySet()) {
                if (key.equals("author_id") || key.equals("music_id")) {
                    criteria.add(Restrictions.eq(key.replace("_", "."), conditions.get(key)));
                } else {
                    criteria.add(Restrictions.eq(key, conditions.get(key)));
                }
            }

            if (ifHasCategoryResource(resource)) {
                //设置JOIN mode，这样categories会直接加载到返回的实例中
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }
            return criteria.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
    }

    /**
     * 根据字段值获取资源
     *
     * @param resource
     * @param conditions
     * @return
     */
    public List getResourcesByFields(Class resource, HashMap<String, Object> conditions) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);

            for (String key : conditions.keySet()) {
                if (key.equals("author_id") || key.equals("music_id")) {
                    criteria.add(Restrictions.eq(key.replace("_", "."), conditions.get(key)));
                } else {
                    criteria.add(Restrictions.eq(key, conditions.get(key)));
                }
            }

            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 获得热度排名前count个
     *
     * @param resource
     * @param count
     * @return
     */
    public List getResourcesByHottest(Class resource, Integer count) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.addOrder(Order.desc("hottest"));//安热度递减排序
            criteria.setMaxResults(count);
            criteria.setReadOnly(true);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 获得倒序更新日期前count个
     *
     * @param resource
     * @param count
     * @return
     */
    public List getResourcesByNewest(Class resource, Integer count) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria c = session.createCriteria(Video.class);
            c.addOrder(Order.desc("update_timestamp"));//按最新时间排序
            c.setMaxResults(count);
            c.setReadOnly(true);
            return c.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 根据name或者title字段的值获取资源实例
     *
     * @param resource
     * @param titlename
     * @param type
     * @return
     */
    public Object getResourceByTitleOrName(Class resource, String titlename, String type) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource);
            criteria.setReadOnly(true);
            if (type.equals("title")) {
                criteria.add(Restrictions.eq("title", titlename));
            } else if (type.equals("name")) {
                criteria.add(Restrictions.eq("name", titlename));
            }
            if (ifHasCategoryResource(resource)) {
                //设置JOIN mode，这样categories会直接加载到返回的实例中
                criteria.setFetchMode("categories", FetchMode.JOIN);
            }
            return criteria.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
    }

    /**
     * 根据内容搜索资源
     *
     * @param resource
     * @param searchContent
     * @return 最多返回50个结果
     */
    public List getResourceBySearch(Class resource, String searchContent) {
        List result = new ArrayList();
        try {
            char[] s = searchContent.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s.length; i++) {
                sb.append("%");
                sb.append(s[i]);
            }
            sb.append("%");
            System.out.println("匹配>>>>" + sb.toString());
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(resource).setProjection(Projections.property("id"));
            if (ifHasCategoryResource(resource)) {
                criteria.add(Restrictions.like("title", sb.toString(), MatchMode.ANYWHERE));
            } else if (resource == Author.class) {
                criteria.add(Restrictions.like("name", sb.toString(), MatchMode.ANYWHERE));
            } else {
                System.out.println("something is bad");
            }
            List<Integer> l_id = criteria.list();
            if (l_id.size() > 0) {
                criteria = session.createCriteria(resource).add(Restrictions.in("id", l_id));
                criteria.setMaxResults(50);
                criteria.setReadOnly(true);
                result = criteria.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取资源相邻的资源用于在客户端的播放界面的侧边选择
     * 获得播放页面右侧的相关视频和伴奏
     * 获取原则 ->
     * 视频
     * 从相同明星舞队中随机选取5个
     * 如果相同明星舞队中视频个数不足则从所有视频中随机选出对应个数来填充
     * 伴奏
     * 从相同名字的明星舞队中随机选取5个
     * 如果相同名字明星舞队中伴奏个数不足则从所有伴奏中随机选出对应个数来填充
     *
     * @param resource
     * @param id
     * @return
     */
    public List getSideBarResources(Class resource, Integer id) {
        List result = new ArrayList();
        List sameAuthorList = new ArrayList();
        try {
            if (resource == Video.class || resource == Tutorial.class) {
                Field field = resource.getDeclaredField("author");
                field.setAccessible(true);
                Object object = getResourceById(resource, id);

                Author author = (Author) field.get(object);
                int authorid = author.getId();

                HashMap<String, Object> conditions = new HashMap<String, Object>();
                conditions.put("author_id", authorid);

                sameAuthorList = getResourcesByFields(resource, conditions);
            } else if (resource == Music.class) {
                Field field = resource.getDeclaredField("authorname");
                field.setAccessible(true);
                Object object = getResourceById(resource, id);

                String authorname = field.get(object).toString();

                HashMap<String, Object> conditions = new HashMap<String, Object>();
                conditions.put("authorname", authorname);

                sameAuthorList = getResourcesByFields(resource, conditions);
            } else {
                System.out.println("something is wired");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int sameAuthorLen = sameAuthorList.size();
        if (sameAuthorLen > 5) {
            Collections.shuffle(sameAuthorList);
            for (int i = 0; i < 5; i++) {
                result.add(sameAuthorList.get(i));
            }
        } else if (sameAuthorLen == 5) {
            result = sameAuthorList;
        } else {
            List allResources = getAllResourceWithoutId(resource, id);
            Collections.shuffle(allResources);
            for (int i = 0; i < 5 - sameAuthorLen; i++) {
                sameAuthorList.add(allResources.get(i));
            }
            result = sameAuthorList;
        }

        return result;
    }

    /**
     * 根据资源id来更新对应资源的字段值
     *
     * @param resource
     * @param id
     * @param updateFieldValue
     * @return
     */
    public int updateResourceFieldsById(Class resource, Integer id, HashMap<String, Object> updateFieldValue) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);

            for (String key : updateFieldValue.keySet()) {
                Field field = resource.getDeclaredField(key);
                field.setAccessible(true);
                field.set(object, updateFieldValue.get(key));
            }

            session.saveOrUpdate(object);
            res = CRUDEvent.UPDATE_SUCCESS;
        } catch (Exception e) {
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 更新资源热度
     *
     * @param resource
     * @param id       资源id
     * @param n        热度增加的值
     * @return
     */
    public int updateResourceHottest(Class resource, Integer id, Integer n) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object object = session.get(resource, id);
            if (object == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                Field field = resource.getDeclaredField("hottest");
                field.setAccessible(true);
                Long Hottest = (Long) field.get(object);
                Hottest += n;
                field.set(object, Hottest);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 删除单个资源
     *
     * @param resource
     * @param id
     * @return
     */
    public int deleteResourceById(Class resource, Integer id) {
        int res;
        try {
            Session session = sessionFactory.getCurrentSession();
            Object target = session.get(resource, id);
            if (target == null) {
                res = CRUDEvent.DELETE_NOTFOUND;
            } else {
                session.delete(target);
                res = CRUDEvent.DELETE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = CRUDEvent.DELETE_FAIL;
        }
        return res;
    }
}
