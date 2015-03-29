package com.darfoo.backend.dao.resource;


import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.utils.ServiceUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public HashMap<String, Integer> insertDanceGroup(HashMap<String, String> insertcontents) {
        Set<String> categoryTitles = new HashSet<String>();
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Class resource = DanceVideo.class;

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        try {
            Object object = DanceVideo.class.newInstance();

            for (String key : insertcontents.keySet()) {
                if (key.equals("title")) {
                    String title = insertcontents.get(key);
                    String authorname = insertcontents.get("authorname");

                    DanceGroup author = (DanceGroup) commonDao.getResourceByTitleOrName(DanceGroup.class, authorname, "name");

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

                    Object queryVideo = commonDao.getResourceByFields(resource, conditions);

                    if (queryVideo == null) {
                        System.out.println("视频名字和作者id组合不存在，可以进行插入");
                        commonDao.setResourceAttr(resource, object, key, title);
                        commonDao.setResourceAttr(resource, object, "video_key", title + System.currentTimeMillis());
                    } else {
                        System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
                        resultMap.put("statuscode", 500);
                        resultMap.put("insertid", -1);
                        return resultMap;
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
                        commonDao.setResourceAttr(resource, object, "image", new Image(imagekey));
                    }
                } else if (key.equals("authorname")) {
                    String authorname = insertcontents.get(key);
                    criteria = session.createCriteria(DanceGroup.class).add(Restrictions.eq("name", authorname));
                    if (criteria.list().size() == 1) {
                        commonDao.setResourceAttr(resource, object, "author", criteria.uniqueResult());
                    } else {
                        System.out.println("作者还不存在");
                        resultMap.put("statuscode", 502);
                        resultMap.put("insertid", -1);
                        return resultMap;
                    }
                } else if (key.contains("category")) {
                    String category = insertcontents.get(key);
                    categoryTitles.add(category);
                } else {
                    commonDao.setResourceAttr(resource, object, key, insertcontents.get(key));
                }
            }

            criteria = session.createCriteria(DanceVideoCategory.class).add(Restrictions.in("title", categoryTitles));
            List categoryList = criteria.list();
            Set categories = new HashSet(categoryList);

            commonDao.setResourceAttr(resource, object, "categories", categories);

            session.save(object);

            int insertid = (Integer) commonDao.getResourceAttr(resource, object, "id");

            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("video_key", insertcontents.get("title") + "-" + resource.getSimpleName().toLowerCase() + "-" + insertid + "." + insertcontents.get("videotype"));
            commonDao.updateResourceFieldsById(resource, insertid, updateMap);

            String connectmusic = insertcontents.get("connectmusic");
            if (!connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(resource, insertid, mid);
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
}
