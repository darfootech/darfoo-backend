package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.AbstractBaseRedisDao;
import com.darfoo.backend.caches.CommonRedisClient;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by zjh on 15-1-3.
 */
public class AuthorCacheDao  extends AbstractBaseRedisDao<String, Author> {
    QiniuUtils qiniuUtils = new QiniuUtils();
    @Autowired
    CommonRedisClient commonRedisClient;

    /**
     * 为单个作者进行缓存
     * @param author
     * @return
     */
    public boolean insertSingleAuthor(Author author){
        Integer id = author.getId();
        String key = "author-" + id;
        if (!commonRedisClient.exists(key)){
            String name = author.getName();
            HashMap<String, String> videoMap = new HashMap<String, String>();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(author.getImage().getImage_key());
            String description = author.getDescription();
            videoMap.put("id", id.toString());
            videoMap.put("name", name);
            videoMap.put("description", description);
            videoMap.put("image_url", image_download_url);
            commonRedisClient.hmset(key, videoMap);
            return true;
        }else{
            return false;
        }
    }

    public SingleAuthor getSingleAuthor(Integer id){
        String key = "author-" + id;
        String name = commonRedisClient.hget(key, "name");
        String imageurl = commonRedisClient.hget(key, "image_url");
        String description = commonRedisClient.hget(key, "description");
        return new SingleAuthor(id, name, description, imageurl);
    }
}
