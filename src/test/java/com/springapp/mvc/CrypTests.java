package com.springapp.mvc;

/**
 * Created by zjh on 14-12-22.
 * encrypt and decrypt
 */

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
}
