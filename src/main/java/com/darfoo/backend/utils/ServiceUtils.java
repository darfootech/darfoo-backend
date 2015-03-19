package com.darfoo.backend.utils;

import com.darfoo.backend.model.cota.ModelUploadEnum;
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
    public static String uploadQiniuResource(CommonsMultipartFile file, String fileName, ModelUploadEnum type) {
        String dirName = DiskFileDirConfig.uploaddir + "uploadresources/";

        if (type == ModelUploadEnum.SMALL) {
            dirName = DiskFileDirConfig.uploaddir + "uploadresources/" + new Date().getTime() + file.getOriginalFilename() + "/";
        }

        long startTime = System.currentTimeMillis();

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

        String statusCode = qiniuUtils.uploadResourceStream(path, fileName);

        if (type == ModelUploadEnum.SMALL) {
            //删除目录
            FileUtils.delete(dirName);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("运行时间：" + String.valueOf(endTime - startTime) + "ms");

        return statusCode;
    }

    public static String reUploadSmallResource(CommonsMultipartFile file, String fileName) {
        deleteResource(fileName);
        return uploadQiniuResource(file, fileName, ModelUploadEnum.SMALL);
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
