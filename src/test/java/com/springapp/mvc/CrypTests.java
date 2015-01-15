package com.springapp.mvc;

/**
 * Created by zjh on 14-12-22.
 * encrypt and decrypt
 */

import com.darfoo.backend.utils.CryptUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class CrypTests {

    /**
     * 使用base64加密
     */
    @Test
    public void base64EncodeStr(){
        String plainText = "cleantha";
        byte[] b = plainText.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        System.out.println("encode result -> " + s);
    }

    /**
     * 使用base64解密
     */
    @Test
    public void base64DecodeStr(){
        String encodeStr = "Y2xlYW50aGE=";
        byte[] b = encodeStr.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s = new String(b);
        System.out.println("decode result -> " + s);
    }

    @Test
    public void encryptQiniuPlayUrl(){
        System.out.println(CryptUtils.encryptQiniuUrl("http://7qnarb.com1.z0.glb.clouddn.com/%E6%A2%81%E7%A5%9D-361.flv?e=1421892623&token=bnMvAStYBsL5AjYM3UXbpGalrectRZZF88Y6fZ-X:xAWCvZpGW5G0njaa8kulloQ83J8="));
    }

    @Test
    public void decryptQiniuPlayUrl(){
        System.out.println(CryptUtils.decryptQiniuUrl("WVVoU01HTkViM1pNZW1SNFltMUdlVmxwTldwaU1qQjRURzV2ZDB4dFpITlphVFZxWWtjNU1WcEhVblZNYlU1MllsTTRiRkpVV1d4UlZFbHNUMFJGYkZKVVkyeFJWRlZzVDFWUmRFMTZXWGhNYlZwelpHbzViRkJVUlRCTmFrVTBUMVJKTWsxcVRXMWtSemx5V2xjME9WbHROVTVrYTBaVVpFWnNRMk13ZHpGUlYzQmFWRlJPVmxkSFNuZFNNa1p6WTIxV2FtUkdTbUZYYTFrMFQwWnJNbHBzYjNSWFJIQTBVVlprUkdSc2NIZFNNV014VW5wQ2RXRnRSbWhQUjNReFlrZDRkbFZVWjNwVGFtYzU="));
    }
}
