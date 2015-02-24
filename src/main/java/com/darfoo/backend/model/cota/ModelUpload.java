package com.darfoo.backend.model.cota;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zjh on 15-2-24.
 */

//标识出资源中需要上传到七牛上的字段名称
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelUpload {
    ModelUploadEnum type() default ModelUploadEnum.SMALL;
}
