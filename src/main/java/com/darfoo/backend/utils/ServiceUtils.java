package com.darfoo.backend.utils;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    public static String uploadLargeResource(CommonsMultipartFile file)  throws IOException {
        String dirName = "uploadresources/";

        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());

        //创建目录
        FileUtils.createDir(dirName);
        String path = dirName + file.getOriginalFilename();

        File newFile=new File(path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);

        long  endTime=System.currentTimeMillis();
        System.out.println("方法二的运行时间："+String.valueOf(endTime-startTime)+"ms");

        String statusCode = "200";

        return statusCode;
        //return "/success";
    }

    //=> 小文件直接上传七牛服务器
    public static String uploadSmallResource(CommonsMultipartFile file)  throws IOException {
        String dirName = new Date().getTime() + file.getOriginalFilename() + "/";

        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());

        //创建目录
        FileUtils.createDir(dirName);
        String path = dirName + file.getOriginalFilename();

        File newFile=new File(path);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);

        //String statusCode = qiniuUtils.uploadResource(path, file.getOriginalFilename());
        String statusCode = qiniuUtils.uploadResourceStream(path, file.getOriginalFilename());
        System.out.println("status code: " + statusCode);

        //删除目录
        FileUtils.delete(dirName);

        long  endTime=System.currentTimeMillis();
        System.out.println("方法二的运行时间："+String.valueOf(endTime-startTime)+"ms");

        return statusCode;
        //return "/success";
    }

}
