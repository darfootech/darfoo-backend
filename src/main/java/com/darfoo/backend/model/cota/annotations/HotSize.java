package com.darfoo.backend.model.cota.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zjh on 15-4-2.
 */

//用于标注出某一类资源的热门资源显示个数
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HotSize {
    int hotsize();
}
