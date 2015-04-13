package com.darfoo.backend.dao.resource;

import com.darfoo.backend.dao.cota.AccompanyDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.enums.DanceVideoType;
import com.darfoo.backend.model.cota.enums.OperaVideoType;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
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

                                if (queryOldResource == null) {
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
                } else if (key.equals("type")) {
                    DanceVideoType type = TypeClassMapping.danceVideoTypeMap.get(updatecontents.get(key));
                    commonDao.setResourceAttr(resource, object, key, type);
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

    public HashMap<String, Integer> updateDanceMusic(Integer id, HashMap<String, String> updatecontents) {
        Set<String> categoryTitles = new HashSet<String>();
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
        boolean isCategoryHasSingleChar = false;

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = DanceMusic.class;
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
                        String oldAuthorname = commonDao.getResourceAttr(resource, object, "authorname").toString();
                        if (!authorname.equals(oldAuthorname)) {
                            HashMap<String, Object> conditions = new HashMap<String, Object>();
                            conditions.put("title", commonDao.getResourceAttr(resource, object, "title"));
                            conditions.put("authorname", authorname);

                            Object queryOldResource = commonDao.getResourceByFields(resource, conditions);

                            conditions.put("title", updatecontents.get("title"));

                            Object queryResource = commonDao.getResourceByFields(resource, conditions);

                            if (queryOldResource == null && queryResource == null) {
                                System.out.println("伴奏名字和舞队名字组合不存在，可以进行插入");
                                commonDao.setResourceAttr(resource, object, key, authorname);
                            } else {
                                System.out.println("伴奏名字和舞队名字组合已存在，不可以进行插入了，是否需要修改");
                                resultMap.put("statuscode", 505);
                                return resultMap;
                            }
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
                    String oldTitle = commonDao.getResourceAttr(resource, object, key).toString();

                    if (!title.equals("") && title != null && !title.equals(oldTitle)) {
                        HashMap<String, Object> conditions = new HashMap<String, Object>();
                        conditions.put("title", title);
                        conditions.put("authorname", updatecontents.get("authorname"));

                        Object queryResource = commonDao.getResourceByFields(resource, conditions);
                        if (queryResource == null) {
                            System.out.println("伴奏名字和舞队名字组合不存在，可以进行插入");
                            commonDao.setResourceAttr(resource, object, key, title);
                        } else {
                            System.out.println("伴奏名字和舞队名字组合已存在，不可以进行插入了，是否需要修改");
                            resultMap.put("statuscode", 505);
                            return resultMap;
                        }
                    }
                }
            }

            commonDao.setResourceAttr(resource, object, "update_timestamp", System.currentTimeMillis());

            if (!isCategoryHasSingleChar) {
                resultMap.put("statuscode", 503);
                return resultMap;
            }

            criteria = session.createCriteria(DanceMusicCategory.class).add(Restrictions.in("title", categoryTitles));
            List categoryList = criteria.list();
            Set categories = new HashSet(categoryList);

            commonDao.setResourceAttr(resource, object, "categories", categories);

            session.saveOrUpdate(object);
            resultMap.put("statuscode", 200);
            return resultMap;
        }
    }

    public HashMap<String, Integer> updateDanceGroup(Integer id, HashMap<String, String> updatecontents) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = DanceGroup.class;
        Object object = session.get(resource, id);

        if (object == null) {
            System.out.println("需要更新的资源不存在");
            resultMap.put("statuscode", 500);
            return resultMap;
        } else {
            for (String key : updatecontents.keySet()) {
                if (key.equals("name")) {
                    String name = updatecontents.get(key);
                    String oldName = commonDao.getResourceAttr(resource, object, key).toString();

                    if (!name.equals("") && name != null && !name.equals(oldName)) {
                        Object queryResource = commonDao.getResourceByTitleOrName(resource, name, key);
                        if (queryResource == null) {
                            commonDao.setResourceAttr(resource, object, key, name);
                        } else {
                            resultMap.put("statuscode", 504);
                            return resultMap;
                        }
                    }
                } else if (key.equals("description")) {
                    String value = updatecontents.get(key);
                    String oldValue = commonDao.getResourceAttr(resource, object, key).toString();
                    if (!value.equals("") && value != null && !value.equals(oldValue)) {
                        commonDao.setResourceAttr(resource, object, key, value);
                    }
                } else if (key.equals("imagekey")) {
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
            }
            session.saveOrUpdate(object);
            resultMap.put("statuscode", 200);
            return resultMap;
        }
    }

    public HashMap<String, Integer> updateOperaVideo(Integer id, HashMap<String, String> updatecontents) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria;

        Class resource = OperaVideo.class;
        Object object = session.get(resource, id);

        OperaVideoType type = TypeClassMapping.operaVideoTypeMap.get(updatecontents.get("type").toLowerCase());

        if (object == null) {
            System.out.println("需要更新的资源不存在");
            resultMap.put("statuscode", 500);
            return resultMap;
        } else {
            for (String key : updatecontents.keySet()) {
                if (key.equals("seriesname")) {
                    if (type == OperaVideoType.SERIES) {
                        String seriesname = updatecontents.get(key);
                        if (!seriesname.equals("") && seriesname != null) {
                            String oldSeriesname = ((OperaSeries) commonDao.getResourceAttr(resource, object, "series")).getTitle();
                            if (!seriesname.equals(oldSeriesname)) {
                                criteria = session.createCriteria(OperaSeries.class).add(Restrictions.eq("title", seriesname));
                                criteria.setReadOnly(true);
                                OperaSeries series = (OperaSeries) criteria.uniqueResult();
                                if (series == null) {
                                    System.out.println("要更新的资源的的越剧连续剧不存在，请先完成越剧连续剧信息的创建");
                                    resultMap.put("statuscode", 508);
                                    return resultMap;
                                } else {
                                    int seriesid = series.getId();

                                    HashMap<String, Object> conditions = new HashMap<String, Object>();
                                    conditions.put("title", commonDao.getResourceAttr(resource, object, "title"));
                                    conditions.put("series_id", seriesid);

                                    Object queryOldResource = commonDao.getResourceByFields(resource, conditions);

                                    if (queryOldResource == null) {
                                        System.out.println("资源名字和越剧连续剧id组合不存在，可以进行插入");
                                        commonDao.setResourceAttr(resource, object, "series", series);
                                    } else {
                                        System.out.println("资源名字和越剧连续剧id组合已存在，不可以进行插入了，是否需要修改");
                                        resultMap.put("statuscode", 509);
                                        return resultMap;
                                    }
                                }
                            }
                        }
                    }
                } else if (key.equals("title")) {
                    String title = updatecontents.get(key);
                    String oldTitle = commonDao.getResourceAttr(resource, object, key).toString();

                    if (!title.equals("") && title != null && !title.equals(oldTitle)) {
                        if (type == OperaVideoType.SERIES) {
                            int oldSeriesid = ((OperaSeries) commonDao.getResourceAttr(resource, object, "series")).getId();
                            HashMap<String, Object> conditions = new HashMap<String, Object>();
                            conditions.put("title", title);
                            conditions.put("series_id", oldSeriesid);

                            Object queryResource = commonDao.getResourceByFields(resource, conditions);

                            if (queryResource == null) {
                                System.out.println("资源名字和越剧连续剧id组合不存在，可以进行插入");
                                commonDao.setResourceAttr(resource, object, key, title);
                            } else {
                                System.out.println("资源名字和越剧连续剧id组合已存在，不可以进行插入了，是否需要修改");
                                resultMap.put("statuscode", 509);
                                return resultMap;
                            }
                        } else {
                            HashMap<String, Object> conditions = new HashMap<String, Object>();
                            conditions.put("title", title);
                            conditions.put("type", OperaVideoType.SINGLE);
                            Object queryVideo = commonDao.getResourceByFields(resource, conditions);
                            if (queryVideo == null) {
                                System.out.println("不存在同名的越剧电影,可以进行插入");
                                commonDao.setResourceAttr(resource, object, key, title);
                            } else {
                                System.out.println("已经存在同名的越剧电影,不可以进行插入");
                                resultMap.put("statuscode", 510);
                                resultMap.put("insertid", -1);
                                return resultMap;
                            }
                        }
                    }
                } else if (key.equals("type")) {
                    //DanceVideoType type = TypeClassMapping.danceVideoTypeMap.get(updatecontents.get(key));
                    commonDao.setResourceAttr(resource, object, key, type);
                }
            }

            commonDao.setResourceAttr(resource, object, "update_timestamp", System.currentTimeMillis());

            session.saveOrUpdate(object);
            resultMap.put("statuscode", 200);
            return resultMap;
        }
    }

    public HashMap<String, Integer> updateOperaSeries(Integer id, HashMap<String, String> updatecontents) {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        Session session = sessionFactory.getCurrentSession();

        Class resource = OperaSeries.class;
        Object object = session.get(resource, id);

        if (object == null) {
            System.out.println("需要更新的资源不存在");
            resultMap.put("statuscode", 500);
            return resultMap;
        } else {
            for (String key : updatecontents.keySet()) {
                if (key.equals("title")) {
                    String name = updatecontents.get(key);
                    String oldName = commonDao.getResourceAttr(resource, object, key).toString();

                    if (!name.equals("") && name != null && !name.equals(oldName)) {
                        Object queryResource = commonDao.getResourceByTitleOrName(resource, name, key);
                        if (queryResource == null) {
                            commonDao.setResourceAttr(resource, object, key, name);
                        } else {
                            System.out.println("相同名字的越剧连续剧已经存在了");
                            resultMap.put("statuscode", 510);
                            return resultMap;
                        }
                    }
                }
            }
            session.saveOrUpdate(object);
            resultMap.put("statuscode", 200);
            return resultMap;
        }
    }
}
