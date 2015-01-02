package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.IndexVideo;
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

    /**
     * 为单个视频资源进行缓存
     * @param video
     * @return
     */
    public boolean insertSingleVideo(Video video){
        Integer id = video.getId();
        String key = "video-" + id;
        if (!commonRedisClient.exists(key)){
            String title = video.getTitle();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());
            String authorname = video.getAuthor().getName();
            String update_timestamp = video.getUpdate_timestamp().toString();
            videoMap.put("id", id.toString());
            videoMap.put("title", title);
            videoMap.put("video_url", video_download_url);
            videoMap.put("image_url", image_download_url);
            videoMap.put("authorname", authorname);
            videoMap.put("update_timestamp", update_timestamp);
            commonRedisClient.hmset(key, videoMap);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 为首页视频资源进行缓存
     * @param video
     * @return
     */
    public boolean insertIndex(Video video){
        Integer id = video.getId();
        String key = "vi-" + id;
        if (!commonRedisClient.exists(key)){
            String title = video.getTitle();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());
            String authorname = video.getAuthor().getName();
            Long timestamp = video.getUpdate_timestamp();
            videoMap.put("id", id.toString());
            videoMap.put("title", title);
            videoMap.put("videourl", video_download_url);
            videoMap.put("imageurl", image_download_url);
            videoMap.put("authorname", authorname);
            videoMap.put("timestamp", timestamp.toString());
            commonRedisClient.hmset(key, videoMap);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 为首页推荐视频资源进行缓存
     * @param video
     * @return
     */
    public boolean insertRecommend(Video video){
        Integer id = video.getId();
        String key = "vr-" + id;
        if (!commonRedisClient.exists(key)){
            String title = video.getTitle();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());
            String authorname = video.getAuthor().getName();
            Long timestamp = video.getUpdate_timestamp();
            videoMap.put("id", id.toString());
            videoMap.put("title", title);
            videoMap.put("videourl", video_download_url);
            videoMap.put("imageurl", image_download_url);
            videoMap.put("authorname", authorname);
            videoMap.put("timestamp", timestamp.toString());
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
        String videourl = commonRedisClient.hget(key, "video_url");
        String imageurl = commonRedisClient.hget(key, "image_url");
        String timestamp = commonRedisClient.hget(key, "update_timestamp");
        return new SingleVideo(id, title, authorname, videourl, imageurl, Long.parseLong(timestamp));
    }

    public SingleVideo getSingleVideoFromPool(Integer id){
        Jedis jedis = null;
        try {
            String key = "video-" + id;
            jedis = jedisPool.getResource();
            String title = jedis.hget(key, "title");
            String authorname = jedis.hget(key, "authorname");
            String videourl = jedis.hget(key, "video_url");
            String imageurl = jedis.hget(key, "image_url");
            //System.out.println(title + " - " + authorname + " - " + videourl + " - " + imageurl);
            String timestamp = commonRedisClient.hget(key, "update_timestamp");
            return new SingleVideo(id, title, authorname, videourl, imageurl, Long.parseLong(timestamp));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedisPool.returnResource(jedis);
        }
    }

    public IndexVideo getIndexVideo(String key){
        int id = Integer.parseInt(key.split("-")[1]);
        String title = commonRedisClient.hget(key, "title");
        String authorname = commonRedisClient.hget(key, "authorname");
        String videourl = commonRedisClient.hget(key, "videourl");
        String imageurl = commonRedisClient.hget(key, "imageurl");
        Long timestamp = Long.parseLong(commonRedisClient.hget(key, "timestamp"));
        return new IndexVideo(id, title, imageurl, videourl, authorname, timestamp);
    }
}
