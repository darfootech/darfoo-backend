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

import java.security.Key;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 15-3-29.
 */

@Component
@SuppressWarnings("unchecked")
public class UpdateDao {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    CommonDao commonDao;
    @Autowired
    AccompanyDao accompanyDao;

    public HashMap<String, Integer> updateDanceVideo(Integer id, HashMap<String, String> updatecontents) {
        Set<String> categoryTitles = new HashSet<String>();
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = DanceVideo.class;
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
                        String oldAuthorname = ((DanceGroup) commonDao.getResourceAttr(resource, object, "author")).getName();
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
                                conditions.put("title", commonDao.getResourceAttr(resource, object, "title"));
                                conditions.put("author_id", authorid);

                                Object queryOldResource = commonDao.getResourceByFields(resource, conditions);

                                conditions.put("title", updatecontents.get("title"));

                                Object queryResource = commonDao.getResourceByFields(resource, conditions);

                                if (queryOldResource == null && queryResource == null) {
                                    System.out.println("资源名字和舞队id组合不存在，可以进行插入");
                                    commonDao.setResourceAttr(resource, object, "author", author);
                                } else {
                                    System.out.println("资源名字和舞队id组合已存在，不可以进行插入了，是否需要修改");
                                    resultMap.put("statuscode", 502);
                                    return resultMap;
                                }
                            }
                        }
                    }
                } else if (key.contains("category")) {
                    String category = updatecontents.get(key);
                    /*if (category != null && !category.equals("")) {
                        categoryTitles.add(category);
                    }*/
                    if (category != null) {
                        categoryTitles.add(category);
                    }
                } else if (key.equals("title")) {
                    String title = updatecontents.get(key);
                    String oldTitle = commonDao.getResourceAttr(resource, object, key).toString();

                    if (!title.equals("") && title != null && !title.equals(oldTitle)) {
                        int oldAuthorid = ((DanceGroup) commonDao.getResourceAttr(resource, object, "author")).getId();
                        HashMap<String, Object> conditions = new HashMap<String, Object>();
                        conditions.put("title", title);
                        conditions.put("author_id", oldAuthorid);

                        Object queryResource = commonDao.getResourceByFields(resource, conditions);

                        if (queryResource == null) {
                            System.out.println("资源名字和舞队id组合不存在，可以进行插入");
                            commonDao.setResourceAttr(resource, object, key, title);
                        } else {
                            System.out.println("资源名字和舞队id组合已存在，不可以进行插入了，是否需要修改");
                            resultMap.put("statuscode", 502);
                            return resultMap;
                        }
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
                                commonDao.setResourceAttr(resource, object, "image", new Image(imagekey));
                            }
                        }
                    }
                } else {
                    commonDao.setResourceAttr(resource, object, key, updatecontents.get(key));
                }
            }

            String connectmusic = updatecontents.get("connectmusic");
            if (connectmusic != null && !connectmusic.equals("")) {
                int mid = Integer.parseInt(connectmusic.split("-")[2]);
                accompanyDao.updateResourceMusic(resource, id, mid);
            }

            commonDao.setResourceAttr(resource, object, "update_timestamp", System.currentTimeMillis());

            criteria = session.createCriteria(DanceVideoCategory.class).add(Restrictions.in("title", categoryTitles));
            List categoryList = criteria.list();
            Set categories = new HashSet(categoryList);

            commonDao.setResourceAttr(resource, object, "categories", categories);

            session.saveOrUpdate(object);
            resultMap.put("statuscode", 200);
            return resultMap;
        }
    }
}
