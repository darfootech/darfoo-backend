package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Music;
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

    public boolean insert(Music music){
        Integer id = music.getId();
        String key = "music-" + id;
        if (!commonRedisClient.exists(key)){
            String title = music.getTitle();
            HashMap<String, String> musicMap = new HashMap<String, String>();
            String music_download_url = qiniuUtils.getQiniuResourceUrl(music.getMusic_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(music.getImage().getImage_key());
            String authorname = music.getAuthor().getName();
            musicMap.put("id", id.toString());
            musicMap.put("title", title);
            musicMap.put("musicurl", music_download_url);
            musicMap.put("imageurl", image_download_url);
            musicMap.put("authorname", authorname);
            commonRedisClient.hmset(key, musicMap);
            return true;
        }else{
            return false;
        }
    }
}
