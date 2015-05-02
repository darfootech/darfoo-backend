package com.darfoo.backend.utils;

/**
 * Created by zjh on 15-2-27.
 */

/**
 * RAW -> 未加密链接
 * SMALL -> 未加密并且缩小资源
 * ENCRYPT -> 加密链接
 * M3U8 -> 音视频切片文件
 */
public enum QiniuResourceEnum {
    RAWNORMAL, ENCRYPT, RAWSMALL, M3U8
}
