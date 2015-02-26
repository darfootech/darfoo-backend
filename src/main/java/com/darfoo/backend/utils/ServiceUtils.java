package com.darfoo.backend.utils;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zjh on 14-11-26.
 */
public class ServiceUtils {
    static QiniuUtils qiniuUtils = new QiniuUtils();

    public static String[] convertList2Array(List<String> vidoes) {
        String[] stockArr = new String[vidoes.size()];
        stockArr = vidoes.toArray(stockArr);
        return stockArr;
    }

    //=> 大文件传得比较慢，所以就先放服务器上用七牛的命令行工具统一传
    public static String uploadLargeResource(CommonsMultipartFile file, String fileName) {
        String statusCode = "200";
        String dirName = "uploadresources/";

        long startTime = System.currentTimeMillis();
        //System.out.println("fileName："+file.getOriginalFilename());

        //创建目录
        FileUtils.createDir(dirName);
        String path = dirName + fileName;

        File newFile = new File(path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            statusCode = "500";
        }

        long endTime = System.currentTimeMillis();
        System.out.println("方法二的运行时间：" + String.valueOf(endTime - startTime) + "ms");

        return statusCode;
    }

    //=> 小文件直接上传七牛服务器
    public static String uploadSmallResource(CommonsMultipartFile file, String fileName) {
        String dirName = "uploadresources/" + new Date().getTime() + file.getOriginalFilename() + "/";

        long startTime = System.currentTimeMillis();
        //System.out.println("fileName："+file.getOriginalFilename());

        //创建目录
        FileUtils.createDir(dirName);
        String path = dirName + fileName;

        File newFile = new File(path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "500";
        }

        //String statusCode = qiniuUtils.uploadResource(path, file.getOriginalFilename());
        String statusCode = qiniuUtils.uploadResourceStream(path, fileName);
        System.out.println("status code: " + statusCode);

        //删除目录
        FileUtils.delete(dirName);

        long endTime = System.currentTimeMillis();
        System.out.println("方法二的运行时间：" + String.valueOf(endTime - startTime) + "ms");

        return statusCode;
    }

    public static String reUploadSmallResource(CommonsMultipartFile file, String fileName) {
        deleteResource(fileName);
        return uploadSmallResource(file, fileName);
    }

    public static void deleteResource(String key) {
        qiniuUtils.deleteResource(key);
    }

    public static boolean isSingleCharacter(String letter) {
        Pattern pattern = Pattern.compile("[A-Z]");
        return pattern.matcher(letter).matches();
    }

    public static boolean isValidImageKey(String imagekey) {
        Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");
        return pattern.matcher(imagekey).matches();
    }
}
