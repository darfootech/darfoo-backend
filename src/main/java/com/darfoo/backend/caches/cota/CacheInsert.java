package com.darfoo.backend.caches.cota;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zjh on 15-2-14.
 */

//需要插入到缓存层中的字段
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CacheInsert {
    CacheInsertEnum type() default CacheInsertEnum.NORMAL;
}
