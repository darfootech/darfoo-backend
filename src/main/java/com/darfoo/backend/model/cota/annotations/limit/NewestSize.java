package com.darfoo.backend.model.cota.annotations.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zjh on 15-4-2.
 */

//用于标注出某一个资源显示最新资源个数
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NewestSize {
    int newestsize();
}
