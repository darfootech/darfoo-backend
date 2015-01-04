package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 14-12-18.
 */
public class TutorialCacheDao extends AbstractBaseRedisDao<String, Education> {
    QiniuUtils qiniuUtils = new QiniuUtils();
    @Autowired
    CommonRedisClient commonRedisClient;

    public boolean insertSingleTutorial(Education tutorial){
        Integer id = tutorial.getId();
        String key = "tutorial-" + id;
        if (!commonRedisClient.exists(key)){
            String title = tutorial.getTitle();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getImage().getImage_key());
            String authorname = tutorial.getAuthor().getName();
            Long timestamp = tutorial.getUpdate_timestamp();
            videoMap.put("id", id.toString());
            videoMap.put("title", title);
            videoMap.put("video_url", video_download_url);
            videoMap.put("image_url", image_download_url);
            videoMap.put("authorname", authorname);
            videoMap.put("update_timestamp", timestamp.toString());
            commonRedisClient.hmset(key, videoMap);
            return true;
        }else{
            return false;
        }
    }

    public boolean insertRecommendTutorial(Education tutorial){
        Integer id = tutorial.getId();
        String key = "recommendvideo-" + id;
        if (!commonRedisClient.exists(key)){
            String title = tutorial.getTitle();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key() + "@@recommendtutorial.png");
            String authorname = tutorial.getAuthor().getName();
            Long timestamp = tutorial.getUpdate_timestamp();
            videoMap.put("id", id.toString());
            videoMap.put("title", title);
            videoMap.put("video_url", video_download_url);
            videoMap.put("image_url", image_download_url);
            videoMap.put("authorname", authorname);
            videoMap.put("update_timestamp", timestamp.toString());
            commonRedisClient.hmset(key, videoMap);
            return true;
        }else{
            return false;
        }
    }

    public SingleVideo getSingleTutorial(Integer id){
        String key = "tutorial-" + id;
        String title = commonRedisClient.hget(key, "title");
        String authorname = commonRedisClient.hget(key, "authorname");
        String tutorialurl = commonRedisClient.hget(key, "video_url");
        String imageurl = commonRedisClient.hget(key, "image_url");
        long timestamp = Long.parseLong(commonRedisClient.hget(key, "update_timestamp"));
        return new SingleVideo(id, title, authorname, tutorialurl, imageurl, timestamp);
    }
}
