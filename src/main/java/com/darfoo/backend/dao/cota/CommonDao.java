package com.darfoo.backend.dao.cota;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.hibernate.Criteria;
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
    @Autowired
    AuthorDao authorDao;

    public Criteria getCommonQueryCriteria(Class resource) {
        Criteria criteria = sessionFactory
                .getCurrentSession()
                .createCriteria(resource)
                .setReadOnly(true);

        if (ifHasCategoryResource(resource)) {
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
        return criteria;
    }

    public boolean ifHasCategoryResource(Class resource) {
        if (resource == Video.class || resource == Tutorial.class || resource == Music.class) {
            return true;
        } else {
            return false;
        }
    }

    public boolean ifHasHottestResource(Class resource) {
        if (ifHasCategoryResource(resource) || resource == Author.class) {
            return true;
        } else {
            return false;
        }
    }

    public void setResourceAttr(Class resource, Object object, String fieldname, Object value) {
        try {
            Field field = resource.getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getResourceAttr(Class resource, Object object, String fieldname) {
        try {
            Field field = resource.getDeclaredField(fieldname);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入新的资源
     *
     * @param resource
     * @param insertcontents
     * @return
     */
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
                            setResourceAttr(resource, object, key, title);
                            setResourceAttr(resource, object, "video_key", title + System.currentTimeMillis());
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
                            setResourceAttr(resource, object, key, title);
                            setResourceAttr(resource, object, "music_key", title + System.currentTimeMillis());
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
                        setResourceAttr(resource, object, "image", new Image(imagekey));
                    }
                } else if (key.equals("authorname")) {
                    String authorname = insertcontents.get(key);
                    if (resource == Video.class || resource == Tutorial.class) {
                        criteria = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                        if (criteria.list().size() == 1) {
                            setResourceAttr(resource, object, "author", criteria.uniqueResult());
                        } else {
                            System.out.println("作者还不存在");
                            resultMap.put("statuscode", 502);
                            resultMap.put("insertid", -1);
                            return resultMap;
                        }
                    } else if (resource == Music.class) {
                        setResourceAttr(resource, object, key, insertcontents.get(key));
                    } else {
                        System.out.println("authorname something is wired class -> " + resource.getName());
                    }
                } else if (key.contains("category")) {
                    String category = insertcontents.get(key);
                    if (ServiceUtils.isSingleCharacter(category)) {
                        isCategoryHasSingleChar = true;
                    }
                    categoryTitles.add(category);
                } else if (key.equals("name")) {
                    String name = insertcontents.get(key);
                    if (authorDao.isExistAuthor(name)) {
                        System.out.println("相同名字明星舞队已存在，不能创建新明星舞队");
                        resultMap.put("statuscode", 506);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    } else {
                        System.out.println("可以创建新明星舞队");
                        setResourceAttr(resource, object, key, insertcontents.get(key));
                    }
                } else if (key.equals("description")) {
                    setResourceAttr(resource, object, key, insertcontents.get(key));
                } else {
                    System.out.println("key something is wired key -> " + key);
                }
            }

            if (ifHasCategoryResource(resource)) {
                setResourceAttr(resource, object, "update_timestamp", System.currentTimeMillis());

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

                setResourceAttr(resource, object, "categories", categories);
            }

            if (ifHasHottestResource(resource)) {
                setResourceAttr(resource, object, "hottest", 0L);
            }

            if (resource == Video.class || resource == Tutorial.class) {
                setResourceAttr(resource, object, "recommend", 0);
            }

            session.save(object);

            int insertid = (Integer) getResourceAttr(resource, object, "id");

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
        }
        resultMap.put("statuscode", 404);
        resultMap.put("insertid", -1);
        return resultMap;
    }

    /**
     * 更新已有资源
     *
     * @param resource
     * @param updatecontents
     * @return image_key这个字段创建了就不需要更新的 更新这个字段没有意义
     */
    public HashMap<String, Integer> updateResource(Class resource, Integer id, HashMap<String, String> updatecontents) {
        Set<String> categoryTitles = new HashSet<String>();

        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        boolean isCategoryHasSingleChar = false;

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Object object = session.get(resource, id);

        if (object == null) {
            System.out.println("需要更新的资源不存在");
            resultMap.put("statuscode", 500);
            return resultMap;
        } else {
            for (String key : updatecontents.keySet()) {
                if (key.equals("authorname")) {
                    String authorname = updatecontents.get(key);
                    if (resource == Video.class || resource == Tutorial.class) {
                        String oldAuthorname = ((Author) getResourceAttr(resource, object, "author")).getName();
                        if (!authorname.equals(oldAuthorname)) {
                            criteria = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
                            criteria.setReadOnly(true);
                            Author author = (Author) criteria.uniqueResult();
                            if (author == null) {
                                System.out.println("要更新的资源的的明星舞队不存在，请先完成明星舞队信息的创建");
                                resultMap.put("statuscode", 501);
                                return resultMap;
                            } else {
                                setResourceAttr(resource, object, "author", author);
                            }
                        }
                    } else if (resource == Music.class) {
                        String oldAuthorname = getResourceAttr(resource, object, "authorname").toString();
                        if (!authorname.equals(oldAuthorname)) {
                            setResourceAttr(resource, object, "authorname", authorname);
                        }
                    } else {
                        System.out.println("wired");
                    }
                } else if (key.contains("category")) {
                    String category = updatecontents.get(key);
                    if (ServiceUtils.isSingleCharacter(category)) {
                        isCategoryHasSingleChar = true;
                    }
                    categoryTitles.add(category);
                } else if (key.equals("title")) {
                    String title = updatecontents.get(key);
                    String oldTitle = getResourceAttr(resource, object, "title").toString();
                    if (title != null && !title.equals(oldTitle)) {
                        setResourceAttr(resource, object, "title", title);
                    }
                }
            }

            if (ifHasCategoryResource(resource)) {
                setResourceAttr(resource, object, "update_timestamp", System.currentTimeMillis());

                if (resource == Video.class || resource == Music.class) {
                    if (!isCategoryHasSingleChar) {
                        resultMap.put("statuscode", 503);
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

                setResourceAttr(resource, object, "categories", categories);
            }

            session.saveOrUpdate(object);
            resultMap.put("statuscode", 200);
            return resultMap;
        }
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
            return getCommonQueryCriteria(resource)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
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
            return getCommonQueryCriteria(resource)
                    .addOrder(Order.desc("id"))
                    .list();
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
            return getCommonQueryCriteria(resource)
                    .add(Restrictions.not(Restrictions.eq("id", id)))
                    .addOrder(Order.desc("id"))
                    .list();
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
            Criteria criteria = getCommonQueryCriteria(resource);
            for (String key : conditions.keySet()) {
                if (key.equals("author_id") || key.equals("music_id")) {
                    criteria.add(Restrictions.eq(key.replace("_", "."), conditions.get(key)));
                } else {
                    criteria.add(Restrictions.eq(key, conditions.get(key)));
                }
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
            Criteria criteria = getCommonQueryCriteria(resource)
                    .addOrder(Order.desc("id"));
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
            return getCommonQueryCriteria(resource)
                    .addOrder(Order.desc("hottest"))
                    .setMaxResults(count)
                    .list();
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
            return getCommonQueryCriteria(resource)
                    .addOrder(Order.desc("update_timestamp"))
                    .setMaxResults(count)
                    .list();
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
            Criteria criteria = getCommonQueryCriteria(resource);
            if (type.equals("title")) {
                criteria.add(Restrictions.eq("title", titlename));
            } else if (type.equals("name")) {
                criteria.add(Restrictions.eq("name", titlename));
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
            Criteria criteria = getCommonQueryCriteria(resource)
                    .setProjection(Projections.property("id"));
            if (ifHasCategoryResource(resource)) {
                criteria.add(Restrictions.like("title", sb.toString(), MatchMode.ANYWHERE));
            } else if (resource == Author.class) {
                criteria.add(Restrictions.like("name", sb.toString(), MatchMode.ANYWHERE));
            } else {
                System.out.println("something is bad");
            }
            List<Integer> l_id = criteria.list();
            if (l_id.size() > 0) {
                criteria = getCommonQueryCriteria(resource)
                        .add(Restrictions.in("id", l_id));
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
