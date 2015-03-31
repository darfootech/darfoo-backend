package com.darfoo.backend.dao.resource;


import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.DanceGroupType;
import com.darfoo.backend.model.cota.DanceVideoType;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.utils.ServiceUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 15-3-29.
 */

@Component
@SuppressWarnings("unchecked")
public class InsertDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    public HashMap<String, Integer> insertDanceVideo(HashMap<String, String> insertcontents) throws IllegalAccessException, InstantiationException {
        Set<String> categoryTitles = new HashSet<String>();
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = DanceVideo.class;
        Object object = resource.newInstance();

        for (String key : insertcontents.keySet()) {
            if (key.equals("title")) {
                String title = insertcontents.get(key);
                if (!title.equals("")) {
                    String authorname = insertcontents.get("authorname");

                    DanceGroup author = (DanceGroup) commonDao.getResourceByTitleOrName(DanceGroup.class, authorname, "name");

                    if (author == null) {
                        System.out.println("舞队还不存在");
                        resultMap.put("statuscode", 502);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    }

                    int authorid = author.getId();

                    HashMap<String, Object> conditions = new HashMap<String, Object>();
                    conditions.put("title", title);
                    conditions.put("author_id", authorid);

                    Object queryVideo = commonDao.getResourceByFields(resource, conditions);

                    if (queryVideo == null) {
                        System.out.println("视频名字和舞队id组合不存在，可以进行插入");
                        commonDao.setResourceAttr(resource, object, key, title);
                        commonDao.setResourceAttr(resource, object, "video_key", title + System.currentTimeMillis());
                    } else {
                        System.out.println("视频名字和舞队id组合已存在，不可以进行插入了，是否需要修改");
                        resultMap.put("statuscode", 500);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    }
                } else {
                    System.out.println("视频名字不能为空");
                    resultMap.put("statuscode", 507);
                    resultMap.put("insertid", -1);
                    return resultMap;
                }
            } else if (key.equals("authorname")) {
                String authorname = insertcontents.get(key);
                criteria = session.createCriteria(DanceGroup.class).add(Restrictions.eq("name", authorname));
                if (criteria.list().size() == 1) {
                    commonDao.setResourceAttr(resource, object, "author", criteria.uniqueResult());
                } else {
                    System.out.println("舞队还不存在");
                    resultMap.put("statuscode", 502);
                    resultMap.put("insertid", -1);
                    return resultMap;
                }
            } else if (key.contains("category")) {
                String category = insertcontents.get(key);
                categoryTitles.add(category);
            } else if (key.equals("type")) {
                DanceVideoType type = TypeClassMapping.danceVideoTypeMap.get(insertcontents.get(key));
                commonDao.setResourceAttr(resource, object, key, type);
            } else {
                System.out.println("wired");
            }
        }

        String imagekey = insertcontents.get("imagekey");
        commonDao.setResourceAttr(resource, object, "image", new Image(imagekey));

        criteria = session.createCriteria(DanceVideoCategory.class).add(Restrictions.in("title", categoryTitles));
        List categoryList = criteria.list();
        Set categories = new HashSet(categoryList);

        commonDao.setResourceAttr(resource, object, "categories", categories);

        session.save(object);

        int insertid = (Integer) commonDao.getResourceAttr(resource, object, "id");

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        String videokey = String.format("%s-%s-%d.%s", insertcontents.get("title"), resource.getSimpleName().toLowerCase() , insertid, insertcontents.get("videotype"));
        updateMap.put("video_key", videokey);
        commonDao.updateResourceFieldsById(resource, insertid, updateMap);

        String connectmusic = insertcontents.get("connectmusic");
        if (!connectmusic.equals("")) {
            int mid = Integer.parseInt(connectmusic.split("-")[2]);
            accompanyDao.updateResourceMusic(resource, insertid, mid);
        }

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertid);
        return resultMap;
    }

    public HashMap<String, Integer> insertDanceMusic(HashMap<String, String> insertcontents) throws IllegalAccessException, InstantiationException {
        Set<String> categoryTitles = new HashSet<String>();
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
        boolean isCategoryHasSingleChar = false;

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = DanceMusic.class;
        Object object = resource.newInstance();

        for (String key : insertcontents.keySet()) {
            if (key.equals("title")) {
                String title = insertcontents.get(key);
                if (!title.equals("")) {
                    String authorname = insertcontents.get("authorname");
                    HashMap<String, Object> conditions = new HashMap<String, Object>();
                    conditions.put("title", title);
                    conditions.put("authorname", authorname);
                    Object queryMusic = commonDao.getResourceByFields(resource, conditions);

                    if (queryMusic == null) {
                        System.out.println("伴奏名字和舞队名字组合不存在，可以进行插入");
                        commonDao.setResourceAttr(resource, object, key, title);
                        commonDao.setResourceAttr(resource, object, "music_key", title + System.currentTimeMillis());
                    } else {
                        System.out.println("伴奏名字和舞队名字组合已存在，不可以进行插入了，是否需要修改");
                        resultMap.put("statuscode", 505);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    }
                } else {
                    System.out.println("伴奏名字不能为空");
                    resultMap.put("statuscode", 507);
                    resultMap.put("insertid", -1);
                    return resultMap;
                }
            } else if (key.equals("authorname")) {
                String authorname = insertcontents.get("authorname");
                if (!authorname.equals("")) {
                    commonDao.setResourceAttr(resource, object, key, insertcontents.get(key));
                } else {
                    System.out.println("作者名字不能为空");
                    resultMap.put("statuscode", 508);
                    resultMap.put("insertid", -1);
                    return resultMap;
                }
            } else if (key.contains("category")) {
                String category = insertcontents.get(key);
                if (ServiceUtils.isSingleCharacter(category)) {
                    isCategoryHasSingleChar = true;
                }
                categoryTitles.add(category);
            } else {
                System.out.println("wired");
            }
        }

        if (!isCategoryHasSingleChar) {
            resultMap.put("statuscode", 503);
            resultMap.put("insertid", -1);
            return resultMap;
        }

        criteria = session.createCriteria(DanceMusicCategory.class).add(Restrictions.in("title", categoryTitles));
        List categoryList = criteria.list();
        Set categories = new HashSet(categoryList);

        commonDao.setResourceAttr(resource, object, "categories", categories);

        session.save(object);

        int insertid = (Integer) commonDao.getResourceAttr(resource, object, "id");

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        String musickey = String.format("%s-%s-%d.%s", insertcontents.get("title"), resource.getSimpleName().toLowerCase() , insertid, ".mp3");
        updateMap.put("music_key", musickey);
        commonDao.updateResourceFieldsById(resource, insertid, updateMap);

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertid);
        return resultMap;
    }

    public HashMap<String, Integer> insertDanceGroup(HashMap<String, String> insertcontents) throws IllegalAccessException, InstantiationException {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = DanceGroup.class;
        Object object = resource.newInstance();

        for (String key : insertcontents.keySet()) {
            if (key.equals("name")) {
                String name = insertcontents.get(key);
                if (!name.equals("")) {
                    if (commonDao.isResourceExistsByField(DanceGroup.class, "name", name)) {
                        System.out.println("相同名字明星舞队已存在，不能创建新明星舞队");
                        resultMap.put("statuscode", 506);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    } else {
                        System.out.println("可以创建新明星舞队");
                        commonDao.setResourceAttr(resource, object, key, insertcontents.get(key));
                    }
                } else {
                    System.out.println("舞队名字不能为空");
                    resultMap.put("statuscode", 508);
                    resultMap.put("insertid", -1);
                    return resultMap;
                }
            } else if (key.equals("type")) {
                DanceGroupType type = TypeClassMapping.danceGroupTypeMap.get(insertcontents.get(key));
                commonDao.setResourceAttr(resource, object, key, type);
            } else if (key.equals("description")) {
                String description = insertcontents.get(key);
                if (!description.equals("")) {
                    commonDao.setResourceAttr(resource, object, key, insertcontents.get(key));
                } else {
                    System.out.println("舞队简介不能为空");
                    resultMap.put("statuscode", 509);
                    resultMap.put("insertid", -1);
                    return resultMap;
                }
            } else {
                System.out.println("wired");
            }
        }

        String imagekey = String.format("dancegroup-imagekey-%s.%s", System.currentTimeMillis(), insertcontents.get("imagetype"));
        commonDao.setResourceAttr(resource, object, "image", new Image(imagekey));

        session.save(object);

        int insertid = (Integer) commonDao.getResourceAttr(resource, object, "id");

        resultMap.put("statuscode", 200);
        resultMap.put("insertid", insertid);
        return resultMap;
    }
}
