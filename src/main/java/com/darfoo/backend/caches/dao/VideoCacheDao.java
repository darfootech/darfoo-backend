package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.awt.image.TileObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 14-12-17.
 */
public class VideoCacheDao extends AbstractBaseRedisDao<String, Video> {
    QiniuUtils qiniuUtils = new QiniuUtils();
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    JedisPool jedisPool;

    public boolean add(final Video video){
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
                String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());

                String cacheValue = video.getTitle() + "-" + video_download_url + "-" + image_download_url + "-" + video.getAuthor().getName() + "-" + video.getUpdate_timestamp();

                byte[] key = serializer.serialize("video-" + video.getId());
                byte[] value = serializer.serialize(cacheValue);
                return  redisConnection.setNX(key, value);
            }
        });
        return result;
    }

    public boolean insert(Video video){
        Integer id = video.getId();
        String key = "video-" + id;
        if (!commonRedisClient.exists(key)){
            String title = video.getTitle();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());
            String authorname = video.getAuthor().getName();
            videoMap.put("id", id.toString());
            videoMap.put("title", title);
            videoMap.put("videourl", video_download_url);
            videoMap.put("imageurl", image_download_url);
            videoMap.put("authorname", authorname);
            commonRedisClient.hmset(key, videoMap);
            return true;
        }else{
            return false;
        }
    }

    public SingleVideo getSingleVideo(Integer id){
        String key = "video-" + id;
        String title = commonRedisClient.hget(key, "title");
        String authorname = commonRedisClient.hget(key, "authorname");
        String videourl = commonRedisClient.hget(key, "videourl");
        String imageurl = commonRedisClient.hget(key, "imageurl");
        return new SingleVideo(id, title, authorname, videourl, imageurl);
    }

    public SingleVideo getSingleVideoFromPool(Integer id){
        Jedis jedis = null;
        try {
            String key = "video-" + id;
            jedis = jedisPool.getResource();
            String title = jedis.hget(key, "title");
            String authorname = jedis.hget(key, "authorname");
            String videourl = jedis.hget(key, "videourl");
            String imageurl = jedis.hget(key, "imageurl");
            //System.out.println(title + " - " + authorname + " - " + videourl + " - " + imageurl);
            return new SingleVideo(id, title, authorname, videourl, imageurl);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedisPool.returnResource(jedis);
        }
    }
}
