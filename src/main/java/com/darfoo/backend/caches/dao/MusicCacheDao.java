package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by zjh on 14-12-18.
 */
public class MusicCacheDao extends AbstractBaseRedisDao<String, Music> {
    QiniuUtils qiniuUtils = new QiniuUtils();
    @Autowired
    CommonRedisClient commonRedisClient;

    public boolean insertSingleMusic(Music music){
        Integer id = music.getId();
        String key = "music-" + id;
        if (!commonRedisClient.exists(key)){
            String title = music.getTitle();
            HashMap<String, String> musicMap = new HashMap<String, String>();
            String music_download_url = qiniuUtils.getQiniuResourceUrl(music.getMusic_key());
            //String image_download_url = qiniuUtils.getQiniuResourceUrl(music.getImage().getImage_key());
            String authorname = music.getAuthor().getName();
            String timestamp = music.getUpdate_timestamp().toString();
            musicMap.put("id", id.toString());
            musicMap.put("title", title);
            musicMap.put("music_url", music_download_url);
            //musicMap.put("imageurl", image_download_url);
            musicMap.put("authorname", authorname);
            musicMap.put("update_timestamp", timestamp);
            commonRedisClient.hmset(key, musicMap);
            return true;
        }else{
            return false;
        }
    }

    public SingleMusic getSingleMusic(Integer id){
        String key = "music-" + id;
        String title = commonRedisClient.hget(key, "title");
        String authorname = commonRedisClient.hget(key, "authorname");
        String musicurl = commonRedisClient.hget(key, "music_url");
        long timestamp = Long.parseLong(commonRedisClient.hget(key, "update_timestamp"));
        //String imageurl = commonRedisClient.hget(key, "imageurl");
        //return new SingleMusic(id, musicurl, imageurl, authorname, title);
        return new SingleMusic(id, title, musicurl, authorname, timestamp);
    }
}
