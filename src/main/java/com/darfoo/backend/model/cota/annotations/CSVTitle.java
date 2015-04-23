package com.darfoo.backend.model.cota.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zjh on 15-3-4.
 */

//用于标注出在资源导出为csv文件的时候某一字段那一列最上面的中文标题
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CSVTitle {
    String title();
}
