package com.darfoo.backend.caches.dao;

import com.darfoo.backend.caches.cota.CacheProtocol;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zjh on 15-2-27.
 */
public class CacheDao {
    @Autowired
    CacheProtocol cacheProtocol;

    public boolean insertSingleResource(Class resource, Object object, String prefix) {
        return cacheProtocol.insertResourceIntoCache(resource, object, prefix);
    }

    public Object getSingleResource(Class resource, String cachekey) {
        return cacheProtocol.extractResourceFromCache(resource, cachekey);
    }
}
