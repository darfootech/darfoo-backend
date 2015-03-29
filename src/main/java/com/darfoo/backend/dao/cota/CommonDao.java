package com.darfoo.backend.dao.cota;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.dao.resource.InsertDao;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.ModelOperation;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
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
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    DanceGroupDao authorDao;
    @Autowired
    InsertDao insertDao;

    public int getResourceHottestLimit(Class resource) {
        if (resource == DanceMusic.class) {
            return 5;
        } else {
            return 0;
        }
    }

    public Criteria getCommonQueryCriteria(Class resource) {
        Criteria criteria = sessionFactory
                .getCurrentSession()
                .createCriteria(resource)
                .setReadOnly(true);

        if (ifHasCategoryResource(resource)) {
            //不设置fetchmode则截取部分结果的行为会发生在去重之前
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .setFetchMode("categories", FetchMode.SELECT);
        }
        return criteria;
    }

    public boolean ifHasCategoryResource(Class resource) {
        if (resource == DanceVideo.class || resource == DanceMusic.class) {
            return true;
        } else {
            return false;
        }
    }

    public Object setResourceAttr(Class resource, Object object, String fieldname, Object value) {
        try {
            Field field = resource.getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(object, value);
            return object;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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

    public void saveResource(Object object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    /**
     * 插入新的资源
     *
     * @param resource
     * @param insertcontents
     * @return
     */
    public HashMap<String, Integer> insertResource(Class resource, HashMap<String, String> insertcontents) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        try {
            String methodName = ((ModelOperation) resource.getAnnotation(ModelOperation.class)).insertMethod();
            Method method = InsertDao.class.getDeclaredMethod(methodName, new Class[] {HashMap.class});
            return (HashMap<String, Integer>) method.invoke(insertDao, new Object[] {insertcontents});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
    //视频title可以重名,但是不可能出现视频title一样,舞队id都一样的情况,也就是一个舞队的作品中不会出现重名的情况
    //伴奏title可以重名,但是不可能出现伴奏title一样authorname也一样的情况
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
                    if (!authorname.equals("") && authorname != null) {
                        if (resource == DanceVideo.class) {
                            String oldAuthorname = ((DanceGroup) getResourceAttr(resource, object, "author")).getName();
                            if (!authorname.equals(oldAuthorname)) {
                                criteria = session.createCriteria(DanceGroup.class).add(Restrictions.eq("name", authorname));
                                criteria.setReadOnly(true);
                                DanceGroup author = (DanceGroup) criteria.uniqueResult();
                                if (author == null) {
                                    System.out.println("要更新的资源的的明星舞队不存在，请先完成明星舞队信息的创建");
                                    resultMap.put("statuscode", 501);
                                    return resultMap;
                                } else {
                                    int authorid = author.getId();

                                    HashMap<String, Object> conditions = new HashMap<String, Object>();
                                    conditions.put("title", getResourceAttr(resource, object, "title"));
                                    conditions.put("author_id", authorid);

                                    Object queryOldResource = getResourceByFields(resource, conditions);

                                    conditions.put("title", updatecontents.get("title"));

                                    Object queryResource = getResourceByFields(resource, conditions);

                                    if (queryOldResource == null && queryResource == null) {
                                        System.out.println("资源名字和舞队id组合不存在，可以进行插入");
                                        setResourceAttr(resource, object, "author", author);
                                    } else {
                                        System.out.println("资源名字和舞队id组合已存在，不可以进行插入了，是否需要修改");
                                        resultMap.put("statuscode", 502);
                                        return resultMap;
                                    }
                                }
                            }
                        } else if (resource == DanceMusic.class) {
                            String oldAuthorname = getResourceAttr(resource, object, "authorname").toString();
                            if (!authorname.equals(oldAuthorname)) {
                                HashMap<String, Object> conditions = new HashMap<String, Object>();
                                conditions.put("title", getResourceAttr(resource, object, "title"));
                                conditions.put("authorname", authorname);

                                Object queryOldResource = getResourceByFields(resource, conditions);

                                conditions.put("title", updatecontents.get("title"));

                                Object queryResource = getResourceByFields(resource, conditions);

                                if (queryOldResource == null && queryResource == null) {
                                    System.out.println("伴奏名字和舞队名字组合不存在，可以进行插入");
                                    setResourceAttr(resource, object, key, authorname);
                                } else {
                                    System.out.println("伴奏名字和舞队名字组合已存在，不可以进行插入了，是否需要修改");
                                    resultMap.put("statuscode", 505);
                                    return resultMap;
                                }
                            }
                        } else {
                            System.out.println("wired");
                        }
                    }
                } else if (key.contains("category")) {
                    String category = updatecontents.get(key);
                    if (category != null && !category.equals("")) {
                        if (ServiceUtils.isSingleCharacter(category)) {
                            isCategoryHasSingleChar = true;
                        }
                        categoryTitles.add(category);
                    }
                } else if (key.equals("title")) {
                    String title = updatecontents.get(key);
                    String oldTitle = getResourceAttr(resource, object, key).toString();

                    if (!title.equals("") && title != null && !title.equals(oldTitle)) {
                        if (resource == DanceVideo.class) {
                            int oldAuthorid = ((DanceGroup) getResourceAttr(resource, object, "author")).getId();
                            HashMap<String, Object> conditions = new HashMap<String, Object>();
                            conditions.put("title", title);
                            conditions.put("author_id", oldAuthorid);

                            Object queryResource = getResourceByFields(resource, conditions);

                            if (queryResource == null) {
                                System.out.println("资源名字和舞队id组合不存在，可以进行插入");
                                setResourceAttr(resource, object, key, title);
                            } else {
                                System.out.println("资源名字和舞队id组合已存在，不可以进行插入了，是否需要修改");
                                resultMap.put("statuscode", 502);
                                return resultMap;
                            }
                        } else if (resource == DanceMusic.class) {
                            HashMap<String, Object> conditions = new HashMap<String, Object>();
                            conditions.put("title", title);
                            conditions.put("authorname", updatecontents.get("authorname"));

                            Object queryResource = getResourceByFields(resource, conditions);
                            if (queryResource == null) {
                                System.out.println("伴奏名字和舞队名字组合不存在，可以进行插入");
                                setResourceAttr(resource, object, key, title);
                            } else {
                                System.out.println("伴奏名字和舞队名字组合已存在，不可以进行插入了，是否需要修改");
                                resultMap.put("statuscode", 505);
                                return resultMap;
                            }
                        } else {
                            System.out.println("wired");
                        }
                    }
                } else if (key.equals("name")) {
                    String name = updatecontents.get(key);
                    String oldName = getResourceAttr(resource, object, key).toString();

                    if (!name.equals("") && name != null && !name.equals(oldName)) {
                        Object queryResource = getResourceByTitleOrName(resource, name, key);
                        if (queryResource == null) {
                            setResourceAttr(resource, object, key, name);
                        } else {
                            resultMap.put("statuscode", 504);
                            return resultMap;
                        }
                    }
                } else if (key.equals("description")) {
                    String value = updatecontents.get(key);
                    String oldValue = getResourceAttr(resource, object, key).toString();
                    if (!value.equals("") && value != null && !value.equals(oldValue)) {
                        setResourceAttr(resource, object, key, value);
                    }
                } else if (key.equals("imagekey")) {
                    //为之前没有关联图片的明星舞队更新图片
                    if (resource == DanceGroup.class) {
                        String imagekey = updatecontents.get(key);
                        String oldImagekey = ((DanceGroup) object).getImage().getImage_key();

                        if (!imagekey.equals("") && !imagekey.equals(oldImagekey)) {
                            if (!ServiceUtils.isValidImageKey(imagekey)) {
                                resultMap.put("statuscode", 506);
                                resultMap.put("insertid", -1);
                                return resultMap;
                            }
                            criteria = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
                            if (criteria.list().size() == 1) {
                                System.out.println("相同imagekey的图片已经存在了");
                                resultMap.put("statuscode", 507);
                                resultMap.put("insertid", -1);
                                return resultMap;
                            } else {
                                setResourceAttr(resource, object, "image", new Image(imagekey));
                            }
                        }
                    }
                } else {
                    System.out.println("wired");
                }
            }

            if (resource == DanceVideo.class) {
                String connectmusic = updatecontents.get("connectmusic");
                if (connectmusic != null && !connectmusic.equals("")) {
                    int mid = Integer.parseInt(connectmusic.split("-")[2]);
                    accompanyDao.updateResourceMusic(resource, id, mid);
                }
            }

            if (ifHasCategoryResource(resource)) {
                setResourceAttr(resource, object, "update_timestamp", System.currentTimeMillis());

                if (resource == DanceVideo.class || resource == DanceMusic.class) {
                    if (!isCategoryHasSingleChar) {
                        resultMap.put("statuscode", 503);
                        return resultMap;
                    }
                }

                Class categoryClass = null;
                Set categories;

                if (resource == DanceVideo.class) {
                    categoryClass = DanceVideoCategory.class;
                }

                if (resource == DanceMusic.class) {
                    categoryClass = DanceMusicCategory.class;
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
     * 根据某一个字段的值来判断是否已经有资源占用了这个字段的值
     *
     * @param resource
     * @param field
     * @param value
     * @return
     */
    public boolean isResourceExistsByField(Class resource, String field, String value) {
        try {
            Criteria criteria = getCommonQueryCriteria(resource)
                    .add(Restrictions.eq(field, value));
            Object object = criteria.uniqueResult();
            return (object == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据字段的值来判断是否已经有资源占用了这个字段的值
     *
     * @param resource
     * @param conditions
     * @return
     */
    public boolean isResourceExistsByFields(Class resource, HashMap<String, Object> conditions) {
        try {
            Criteria criteria = getCommonQueryCriteria(resource);
            for (String key : conditions.keySet()) {
                criteria.add(Restrictions.eq(key, conditions.get(key)));
            }
            Object object = criteria.uniqueResult();
            return (object == null) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
     * 根据字段值获取资源 并且record的id值不能和制定id相同
     *
     * @param resource
     * @param conditions
     * @param id
     * @return
     */
    public List getResourcesByFieldsWithoutId(Class resource, HashMap<String, Object> conditions, Integer id) {
        try {
            Criteria criteria = getCommonQueryCriteria(resource)
                    .addOrder(Order.desc("id"))
                    .add(Restrictions.not(Restrictions.eq("id", id)));
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
    //.setMaxResults(count)在这里是个坑
    public List getResourcesByNewest(Class resource, Integer count) {
        try {
            return getCommonQueryCriteria(resource)
                    .addOrder(Order.desc("update_timestamp"))
                    .list().subList(0, count);
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
    public List getResourcesBySearch(Class resource, String searchContent) {
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
            } else if (resource == DanceGroup.class) {
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
            if (resource == DanceVideo.class) {
                Field field = resource.getDeclaredField("author");
                field.setAccessible(true);
                Object object = getResourceById(resource, id);

                DanceGroup author = (DanceGroup) field.get(object);
                int authorid = author.getId();

                HashMap<String, Object> conditions = new HashMap<String, Object>();
                conditions.put("author_id", authorid);

                sameAuthorList = getResourcesByFieldsWithoutId(resource, conditions, id);
            } else if (resource == DanceMusic.class) {
                Field field = resource.getDeclaredField("author_name");
                field.setAccessible(true);
                Object object = getResourceById(resource, id);

                String authorname = field.get(object).toString();

                HashMap<String, Object> conditions = new HashMap<String, Object>();
                conditions.put("author_name", authorname);

                sameAuthorList = getResourcesByFieldsWithoutId(resource, conditions, id);
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
     * @param id
     * @param fieldname
     * @return
     */
    @Transactional
    public int incResourceField(Class resource, Integer id, String fieldname) {
        int res;
        //getCurrentSession是在上下文中 openSession是在另一个线程中开启一个数据库会话
        //在统计中用getCurrentSession不起作用的原因可能是先用了getCurrentSession得到的会话来进行了查询持有了锁没有释放 所以更新失败 所以要用newSession新开启一个会话
        Session session = sessionFactory.openSession();
        try {
            Object object = session.get(resource, id);
            if (object == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                Long oldvalue = (Long) getResourceAttr(resource, object, fieldname);
                setResourceAttr(resource, object, fieldname, oldvalue + 1);
                session.update(object);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return res;
    }

    //a overload function
    @Transactional
    public int incResourceField(Class resource, Object object, String fieldname) {
        int res;
        Session session = sessionFactory.openSession();
        try {
            if (object == null) {
                res = CRUDEvent.UPDATE_VIDEO_NOTFOUND;
            } else {
                Long oldvalue = (Long) getResourceAttr(resource, object, fieldname);
                setResourceAttr(resource, object, fieldname, oldvalue + 1);
                session.update(object);
                res = CRUDEvent.UPDATE_SUCCESS;
            }
        } catch (Exception e) {
            res = CRUDEvent.UPDATE_FAIL;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
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

            if (resource == DanceGroup.class) {
                String prepareSql = "update dancevideo set AUTHOR_ID=null where AUTHOR_ID=:author_id";
                System.out.println(session.createSQLQuery(prepareSql).setInteger("author_id", id).executeUpdate());
            }

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
