package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.cota.CacheCollType;
import com.darfoo.backend.caches.cota.CacheProtocol;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Pipeline;

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

    public boolean insertSingleResource(Class resource, Object object, String prefix) {
        return cacheProtocol.insertResourceIntoCache(resource, object, prefix);
    }

    public boolean insertSingleResource(Pipeline pipeline, Class resource, Object object, String prefix) {
        return cacheProtocol.insertResourceIntoCache(pipeline, resource, object, prefix);
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
                String resourcekey = String.format("%s-%d", prefix, id);
                status = redisClient.rpush(cachekey, resourcekey);
            } else if (type == CacheCollType.SORTEDSET) {
                redisClient.zadd(cachekey, String.format("%s-%d", prefix, id), (double) (resources.size() - resources.indexOf(object)));
            } else {
                System.out.println("wired");
            }
            System.out.println("insert status -> " + status);

            boolean result = insertSingleResource(insertclass, object, prefix);
            System.out.println("insert result -> " + result);
        }
    }

    public void insertResourcesIntoCache(Pipeline pipeline, Class insertclass, List resources, String cachekey, String prefix, CacheCollType type) {
        for (Object object : resources) {
            int id = (Integer) commonDao.getResourceAttr(insertclass, object, "id");
            if (type == CacheCollType.SET) {
                pipeline.sadd(cachekey, String.format("%s-%d", prefix, id));
            } else if (type == CacheCollType.LIST) {
                String resourcekey = String.format("%s-%d", prefix, id);
                pipeline.rpush(cachekey, resourcekey);
            } else if (type == CacheCollType.SORTEDSET) {
                pipeline.zadd(cachekey, (double) (resources.size() - resources.indexOf(object)), String.format("%s-%d", prefix, id));
            } else {
                System.out.println("wired");
            }

            boolean result = insertSingleResource(pipeline, insertclass, object, prefix);
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
                keys = redisClient.zrevrange(cachekey, 0L, -1L, false);
            } else {
                keys = redisClient.zrevrange(cachekey, points[0], points[1], false);
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

    public List getSearchResourcesWithDanceGroup(Class resource, String searchContent) {
        List objects = commonDao.getResourcesBySearch(resource, searchContent);

        List<DanceGroup> authors = commonDao.getResourcesBySearch(DanceGroup.class, searchContent);
        for (DanceGroup author : authors) {
            int aid = author.getId();
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("author_id", aid);

            List authorObjects = commonDao.getResourcesByFields(resource, conditions);
            objects.addAll(authorObjects);
        }

        return objects;
    }
}
