package com.darfoo.backend.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by zjh on 14-12-22.
 */
public class CryptUtils {
    /**
     * 使用base64加密
     */
    public String base64EncodeStr(String plainText){
        byte[] b = plainText.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        System.out.println("encode result -> " + s);
        return s;
    }

    /**
     * 使用base64解密
     */
    public String base64DecodeStr(String encodeStr){
        byte[] b = encodeStr.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s = new String(b);
        System.out.println("decode result -> " + s);
        return s;
    }
}
