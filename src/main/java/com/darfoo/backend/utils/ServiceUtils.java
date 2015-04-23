package com.darfoo.backend.utils;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.darfoo.backend.model.cota.enums.ModelUploadEnum;
import com.darfoo.backend.service.cota.ActorSysContainer;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import static akka.dispatch.Futures.future;

/**
 * Created by zjh on 14-11-26.
 */
public class ServiceUtils {
    static QiniuUtils qiniuUtils = new QiniuUtils();

    public enum QiniuOperationType {
        UPLOAD, REUPLOAD
    }

    final static ActorSystem system = ActorSysContainer.getInstance().getSystem();
    final static ExecutionContext ec = system.dispatcher();

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

        if (type == ModelUploadEnum.LARGE) {
            FileUtils.delete(path);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("运行时间：" + String.valueOf(endTime - startTime) + "ms");

        return statusCode;
    }

    public static String reUploadQiniuResource(CommonsMultipartFile file, String fileName, ModelUploadEnum type) {
        deleteResource(fileName);
        return uploadQiniuResource(file, fileName, type);
    }

    public static void operateQiniuResourceAsync(CommonsMultipartFile file, String key, ModelUploadEnum type, QiniuOperationType operation) {
        final CommonsMultipartFile innerfile = file;
        final String innerkey = key;
        final ModelUploadEnum innertype = type;

        //删除七牛资源的操作如果放在异步任务里会出现akka的空指针错误 暂时还无法定位问题原因只能暂时先移植外面
        if (operation == QiniuOperationType.REUPLOAD) {
            deleteResource(innerkey);
        }

        Future<String> future = future(new Callable<String>() {
            public String call() {
                return uploadQiniuResource(innerfile, innerkey, innertype);
            }
        }, system.dispatcher());

        future.onComplete(new OnComplete<String>() {
            public void onComplete(Throwable failure, String status) {
                if (failure != null && !status.equals("200")) {
                    System.out.println("upload file failed");
                } else {
                    System.out.println("upload file success");
                }
            }
        }, ec);
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
