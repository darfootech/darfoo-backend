package com.darfoo.backend.caches.cota;

/**
 * Created by zjh on 15-2-14.
 */

//普通字段直接插入缓存层 资源字段需要获得七牛链接之后再插入缓存层
public enum CacheInsertEnum {
    NORMAL, RESOURCE
}
