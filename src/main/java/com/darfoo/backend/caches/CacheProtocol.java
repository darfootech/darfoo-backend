package com.darfoo.backend.caches;

import com.darfoo.backend.caches.CacheInsert;
import com.darfoo.backend.caches.CacheInsertEnum;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.JedisPool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjh on 15-2-14.
 */

public class CacheProtocol {
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    QiniuUtils qiniuUtils;

    public boolean insertResourceIntoCache(Class model, Object object) {
        try {
            Field idField = model.getDeclaredField("id");
            idField.setAccessible(true);
            String id = idField.get(object).toString();

            String modelName = model.getSimpleName().toLowerCase();
            String cachekey = "";
            if (modelName.equals("education")) {
                cachekey = "tutorial-" + id;
            } else {
                cachekey = modelName + "-" + id;
            }

            if (!commonRedisClient.exists(cachekey)) {
                HashMap<String, String> cacheInsertMap = new HashMap<String, String>();
                for (Field field : model.getDeclaredFields()) {
                    if (field.isAnnotationPresent(CacheInsert.class)) {
                        field.setAccessible(true);
                        Annotation annotation = field.getAnnotation(CacheInsert.class);
                        CacheInsert cacheInsert = (CacheInsert) annotation;

                        if (cacheInsert.type() == CacheInsertEnum.NORMAL) {
                            if (field.getName().equals("author")) {
                                Author author = (Author) field.get(object);
                                cacheInsertMap.put("authorname", author.getName());
                                System.out.println(field.getName() + " -> " + author.getName());
                            } else {
                                cacheInsertMap.put(field.getName(), field.get(object).toString());
                                System.out.println(field.getName() + " -> " + field.get(object));
                            }
                        } else if (cacheInsert.type() == CacheInsertEnum.RESOURCE) {
                            if (field.getName().equals("image")) {
                                Image image = (Image) field.get(object);
                                cacheInsertMap.put("image_url", qiniuUtils.getQiniuResourceUrlByType(image.getImage_key(), "image"));
                                System.out.println("image_url -> " + qiniuUtils.getQiniuResourceUrlByType(image.getImage_key(), "image"));
                            } else {
                                cacheInsertMap.put("video_url", qiniuUtils.getQiniuResourceUrlByType(field.get(object).toString(), "video"));
                                System.out.println("video_url -> " + field.get(object).toString());
                            }
                        } else {
                            System.out.println("something is wired");
                        }
                    }
                }
                if (model == Video.class) {
                    cacheInsertMap.put("type", 1 + "");
                }
                commonRedisClient.hmset(cachekey, cacheInsertMap);
            }
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, String> extractResourceFromCache(Class model, Integer id) {
        String modelName = model.getSimpleName().toLowerCase();
        String cachekey = "";
        if (modelName.equals("education")) {
            cachekey = "tutorial-" + id;
        } else {
            cachekey = modelName + "-" + id;
        }

        if (commonRedisClient.exists(cachekey)) {
            HashMap<String, String> result = (HashMap<String, String>) commonRedisClient.hgetAll(cachekey);
            return result;
        }

        return null;
    }
}
