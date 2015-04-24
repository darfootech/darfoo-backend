package com.darfoo.backend.utils;

/**
 * Created by zjh on 15-2-27.
 */

/**
 * RAW -> 未加密链接
 * SMALL -> 未加密并且缩小资源
 * ENCRYPT -> 加密链接
 */
public enum QiniuResourceEnum {
    RAWNORMAL, ENCRYPT, RAWSMALL
}
