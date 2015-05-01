package com.darfoo.backend.caches.cota;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.Advertise;
import com.darfoo.backend.model.ThirdPartApp;
import com.darfoo.backend.model.cota.enums.DanceVideoType;
import com.darfoo.backend.model.cota.enums.OperaVideoType;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaSeries;
import com.darfoo.backend.utils.QiniuResourceEnum;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Pipeline;

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
    @Autowired
    CommonDao commonDao;

    private String getInsertCacheKey(Class model, Object object, String prefix) {
        int id = (Integer) commonDao.getResourceAttr(model, object, "id");
        String cachekey = prefix + "-" + id;

        if (model == ThirdPartApp.class) {
            String title = (String) commonDao.getResourceAttr(model, object, "title");
            cachekey = prefix + "-" + title;
        }

        return cachekey;
    }

    private HashMap<String, String> insertSpeedupUrl(Class model, Object object, Field field, HashMap<String, String> insertMap) throws IllegalAccessException {
        String speedupkey = (String) commonDao.getResourceAttr(model, object, "speedup_key");
        if (speedupkey.equals("")) {
            insertMap.put("speedup_url", qiniuUtils.getQiniuResourceUrl(field.get(object).toString(), QiniuResourceEnum.ENCRYPT));
            System.out.println("speedup_url -> " + field.get(object).toString());
        } else {
            insertMap.put("speedup_url", qiniuUtils.getQiniuResourceUrl(speedupkey, QiniuResourceEnum.ENCRYPT));
            System.out.println("speedup_url -> " + field.get(object).toString());
        }
        return insertMap;
    }

    private HashMap<String, String> getInsertCacheMap(Class model, Object object, String prefix) throws IllegalAccessException, NoSuchFieldException {
        HashMap<String, String> cacheInsertMap = new HashMap<String, String>();
        for (Field field : model.getDeclaredFields()) {
            if (field.isAnnotationPresent(CacheInsert.class)) {
                field.setAccessible(true);
                Annotation annotation = field.getAnnotation(CacheInsert.class);
                CacheInsert cacheInsert = (CacheInsert) annotation;

                if (cacheInsert.type() == CacheInsertEnum.NORMAL) {
                    if (field.getName().equals("author")) {
                        DanceGroup author = (DanceGroup) field.get(object);
                        if (author != null) {
                            System.out.println(field.getName() + " -> " + author.getTitle());
                            cacheInsertMap.put("authorname", author.getTitle());
                        } else {
                            cacheInsertMap.put("authorname", "");
                        }
                    } else if (field.getName().equals("series")) {
                        OperaVideoType type = (OperaVideoType) commonDao.getResourceAttr(model, object, "type");
                        OperaSeries series = (OperaSeries) field.get(object);
                        if (type == OperaVideoType.SERIES && series != null) {
                            cacheInsertMap.put("seriesname", series.getTitle());
                        } else {
                            cacheInsertMap.put("seriesname", "");
                        }
                    } else if (field.getName().equals("authorname")) {
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
                                image_download_url = qiniuUtils.getQiniuResourceUrl(commonDao.getResourceAttr(model, object, "video_key") + "@@recommend" + model.getSimpleName().toLowerCase() + ".png", QiniuResourceEnum.RAWNORMAL);
                            } else {
                                if (model == Advertise.class) {
                                    image_download_url = qiniuUtils.getQiniuResourceUrl(image.getImage_key(), QiniuResourceEnum.RAWNORMAL);
                                } else {
                                    image_download_url = qiniuUtils.getQiniuResourceUrl(image.getImage_key(), QiniuResourceEnum.RAWSMALL);
                                }
                            }
                        }
                        cacheInsertMap.put("image_url", image_download_url);
                        System.out.println("image_url -> " + image_download_url);
                    } else if (field.getName().equals("video_key")) {
                        cacheInsertMap.put("video_url", qiniuUtils.getQiniuResourceUrl(field.get(object).toString(), QiniuResourceEnum.ENCRYPT));
                        System.out.println("video_url -> " + field.get(object).toString());
                        insertSpeedupUrl(model, object, field, cacheInsertMap);
                    } else if (field.getName().equals("music_key")) {
                        cacheInsertMap.put("music_url", qiniuUtils.getQiniuResourceUrl(field.get(object).toString(), QiniuResourceEnum.ENCRYPT));
                        System.out.println("music_url -> " + field.get(object).toString());
                        String speedupkey = (String) commonDao.getResourceAttr(model, object, "speedup_key");
                        insertSpeedupUrl(model, object, field, cacheInsertMap);
                    } else if (field.getName().equals("app_key")) {
                        cacheInsertMap.put("app_url", qiniuUtils.getQiniuResourceUrl(field.get(object).toString(), QiniuResourceEnum.RAWNORMAL));
                        System.out.println("app_url -> " + field.get(object).toString());
                    }
                } else {
                    System.out.println("something is wired");
                }
            }
        }
        if (model == DanceVideo.class) {
            DanceVideoType type = (DanceVideoType) commonDao.getResourceAttr(model, object, "type");
            cacheInsertMap.put("type", type.ordinal() + "");
        }
        return cacheInsertMap;
    }

    public boolean insertResourceIntoCache(Class model, Object object, String prefix) {
        String cachekey = getInsertCacheKey(model, object, prefix);
        try {
            if (!commonRedisClient.exists(cachekey)) {
                HashMap<String, String> cacheInsertMap = getInsertCacheMap(model, object, prefix);
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

    public boolean insertResourceIntoCache(Pipeline pipeline, Class model, Object object, String prefix) {
        String cachekey = getInsertCacheKey(model, object, prefix);
        try {
            if (!commonRedisClient.exists(cachekey)) {
                HashMap<String, String> cacheInsertMap = getInsertCacheMap(model, object, prefix);
                pipeline.hmset(cachekey, cacheInsertMap);
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

    public Object extractResourceFromCache(Class response, String cachekey) {
        try {
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
