package com.darfoo.backend.caches.cota;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by zjh on 15-2-14.
 */

public class CacheProtocol {
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    QiniuUtils qiniuUtils;

    public boolean insertResourceIntoCache(Class model, Object object, String prefix) {
        try {
            Field idField = model.getDeclaredField("id");
            idField.setAccessible(true);
            String id = idField.get(object).toString();

            String cachekey = prefix + "-" + id;

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
                            } else if (field.getName().toLowerCase().equals("authorname")) {
                                cacheInsertMap.put("authorname", field.get(object).toString());
                                System.out.println(field.getName() + " -> " + field.get(object));
                            } else if (field.getName().equals("update_timestamp")) {
                                cacheInsertMap.put(field.getName(), ((Long) field.get(object) / 1000) + "");
                            } else {
                                cacheInsertMap.put(field.getName(), field.get(object).toString());
                                System.out.println(field.getName() + " -> " + field.get(object));
                            }
                        } else if (cacheInsert.type() == CacheInsertEnum.RESOURCE) {
                            if (field.getName().equals("image")) {
                                Image image = (Image) field.get(object);
                                String image_download_url = "";
                                if (image != null) {
                                    if (prefix.contains("recommend")) {
                                        image_download_url = qiniuUtils.getQiniuResourceUrlByType(image.getImage_key() + "@@recommend" + model.getName().toLowerCase() + ".png", "image");
                                    } else {
                                        image_download_url = qiniuUtils.getQiniuResourceUrlByType(image.getImage_key(), "image");
                                    }
                                }
                                cacheInsertMap.put("image_url", image_download_url);
                                System.out.println("image_url -> " + image_download_url);
                            } else if (field.getName().equals("video_key")) {
                                cacheInsertMap.put("video_url", qiniuUtils.getQiniuResourceUrlByType(field.get(object).toString(), "video"));
                                System.out.println("video_url -> " + field.get(object).toString());
                            } else {
                                cacheInsertMap.put("music_url", qiniuUtils.getQiniuResourceUrlByType(field.get(object).toString() + ".mp3", "music"));
                                System.out.println("music_url -> " + field.get(object).toString());
                            }
                        } else {
                            System.out.println("something is wired");
                        }
                    }
                }
                if (model == Video.class) {
                    cacheInsertMap.put("type", 1 + "");
                }
                if (model == Tutorial.class) {
                    cacheInsertMap.put("type", 0 + "");
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

    public Object extractResourceFromCache(Class response, Integer id, String prefix) {
        try {

            String cachekey = prefix + "-" + id;

            if (commonRedisClient.exists(cachekey)) {
                HashMap<String, String> result = (HashMap<String, String>) commonRedisClient.hgetAll(cachekey);
                Object obj = response.newInstance();

                for (String key : result.keySet()) {
                    Field field = response.getDeclaredField(key);
                    field.setAccessible(true);
                    if (field.getType().toString().equals("class java.lang.Integer")) {
                        field.set(obj, Integer.parseInt(result.get(key)));
                    } else if (field.getType().toString().equals("class java.lang.Long")) {
                        field.set(obj, Long.parseLong(result.get(key)));
                    } else {
                        field.set(obj, result.get(key));
                    }
                }

                return obj;
            }
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }
}
