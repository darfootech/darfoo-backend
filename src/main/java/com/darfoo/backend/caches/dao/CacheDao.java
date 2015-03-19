package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.cota.CacheProtocol;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.responsemodel.MusicCates;
import com.darfoo.backend.service.responsemodel.TutorialCates;
import com.darfoo.backend.service.responsemodel.VideoCates;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-2-27.
 */
public class CacheDao {
    @Autowired
    CacheProtocol cacheProtocol;
    @Autowired
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;
    @Autowired
    VideoCates videoCates;
    @Autowired
    TutorialCates tutorialCates;
    @Autowired
    MusicCates musicCates;

    public boolean insertSingleResource(Class resource, Object object, String prefix) {
        return cacheProtocol.insertResourceIntoCache(resource, object, prefix);
    }

    public Object getSingleResource(Class resource, String cachekey) {
        return cacheProtocol.extractResourceFromCache(resource, cachekey);
    }

    public void insertResourcesIntoCache(Class insertclass, List resources, String cachekey, String prefix, CacheCollType type) {
        for (Object object : resources) {
            int id = (Integer) commonDao.getResourceAttr(insertclass, object, "id");
            long status = 0L;
            if (type == CacheCollType.SET) {
                status = redisClient.sadd(cachekey, String.format("%s-%d", prefix, id));
            } else if (type == CacheCollType.LIST) {
                //因为redis的list数据类型可以让内部元素有重复所以需要手动做到anti duplicate
                String resourcekey = String.format("%s-%d", prefix, id);
                if (!redisClient.exists(resourcekey)) {
                    status = redisClient.rpush(cachekey, resourcekey);
                }
            } else if (type == CacheCollType.SORTEDSET) {
                redisClient.zadd(cachekey, String.format("%s-%d", prefix, id), (double) id);
            } else {
                System.out.println("wired");
            }
            System.out.println("insert result -> " + status);

            boolean result = insertSingleResource(insertclass, object, prefix);
            System.out.println("insert result -> " + result);
        }
    }

    public List extractResourcesFromCache(Class responseclass, String cachekey, CacheCollType type, Long... points) {
        Collection<String> keys;
        if (type == CacheCollType.SET) {
            keys = redisClient.smembers(cachekey);
        } else if (type == CacheCollType.LIST) {
            if (points.length == 0) {
                keys = redisClient.lrange(cachekey, 0L, -1L);
            } else {
                keys = redisClient.lrange(cachekey, points[0], points[1]);
            }
        } else if (type == CacheCollType.SORTEDSET) {
            if (points.length == 0) {
                keys = redisClient.zrange(cachekey, 0L, -1L, false);
            } else {
                keys = redisClient.zrange(cachekey, points[0], points[1], false);
            }
        } else {
            System.out.println("wired");
            keys = new ArrayList<String>();
        }
        List result = new ArrayList();
        for (String key : keys) {
            System.out.println("key -> " + key);
            result.add(getSingleResource(responseclass, key));
        }
        return result;
    }

    public List<String> parseResourceCategories(Class resource, String categories) {
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();

        if (resource == Video.class) {
            if (!requestCategories[0].equals("0")) {
                String speedCate = videoCates.getSpeedCategory().get(requestCategories[0]);
                targetCategories.add(speedCate);
            }
            if (!requestCategories[1].equals("0")) {
                String difficultyCate = videoCates.getDifficultyCategory().get(requestCategories[1]);
                targetCategories.add(difficultyCate);
            }
            if (!requestCategories[2].equals("0")) {
                String styleCate = videoCates.getStyleCategory().get(requestCategories[2]);
                targetCategories.add(styleCate);
            }
            if (!requestCategories[3].equals("0")) {
                String letterCate = requestCategories[3];
                targetCategories.add(letterCate);
            }
        } else if (resource == Tutorial.class) {
            if (!requestCategories[0].equals("0")) {
                String speedCate = tutorialCates.getSpeedCategory().get(requestCategories[0]);
                targetCategories.add(speedCate);
            }
            if (!requestCategories[1].equals("0")) {
                String difficultyCate = tutorialCates.getDifficultyCategory().get(requestCategories[1]);
                targetCategories.add(difficultyCate);
            }
            if (!requestCategories[2].equals("0")) {
                String styleCate = tutorialCates.getStyleCategory().get(requestCategories[2]);
                targetCategories.add(styleCate);
            }
        } else if (resource == Music.class) {
            if (!requestCategories[0].equals("0")) {
                String beatCate = musicCates.getBeatCategory().get(requestCategories[0]);
                targetCategories.add(beatCate);
            }
            if (!requestCategories[1].equals("0")) {
                String styleCate = musicCates.getStyleCategory().get(requestCategories[1]);
                targetCategories.add(styleCate);
            }
            if (!requestCategories[2].equals("0")) {
                String letterCate = requestCategories[2];
                targetCategories.add(letterCate);
            }
        } else {
            System.out.println("wired");
        }

        return targetCategories;
    }

    public List getSearchResourcesWithAuthor(Class resource, String searchContent) {
        List objects = commonDao.getResourcesBySearch(resource, searchContent);

        List<Author> authors = commonDao.getResourcesBySearch(Author.class, searchContent);
        for (Author author : authors) {
            int aid = author.getId();
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("author_id", aid);

            List authorObjects = commonDao.getResourcesByFields(resource, conditions);
            objects.addAll(authorObjects);
        }

        return objects;
    }
}
