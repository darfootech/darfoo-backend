package com.darfoo.backend.service;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.DanceGroupType;
import com.darfoo.backend.model.cota.ModelAttrSuper;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.DanceMusicCates;
import com.darfoo.backend.service.responsemodel.DanceVideoCates;
import com.darfoo.backend.utils.DiskFileDirConfig;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-11.
 */

//下载统计报表
@Controller
public class DownloadController {
    @Autowired
    CommonDao commonDao;
    @Autowired
    DanceVideoCates videoCates;
    @Autowired
    DanceMusicCates musicCates;
    @Autowired
    StatisticsDao statisticsDao;

    public String timestampTodatetime(long timestampfromdb) {
        Timestamp timestamp = new Timestamp(timestampfromdb);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(timestamp);
    }

    public byte[] mergeByteArray(byte[] bomHead, byte[] fileBytes) {
        byte[] download = new byte[bomHead.length + fileBytes.length];
        System.arraycopy(bomHead, 0, download, 0, bomHead.length);
        System.arraycopy(fileBytes, 0, download, bomHead.length, fileBytes.length);
        return download;
    }

    public void writeResourcesToCSV(Class resource) {
        List resources = commonDao.getAllResource(resource);
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter(String.format("%s%s.csv", DiskFileDirConfig.csvdir, resource.getSimpleName().toLowerCase()));
            printer = new CSVPrinter(out, format.withDelimiter(','));
            HashMap<String, String> styleMap = new HashMap<String, String>();

            if (resource == DanceVideo.class) {
                printer.printRecord("教程标题", "明星舞队名称", "舞蹈速度", "舞蹈难度", "舞蹈风格");
                for (Object object : resources) {
                    DanceVideo tutorial = (DanceVideo) object;
                    Set<DanceVideoCategory> categories = tutorial.getCategories();
                    for (DanceVideoCategory category : categories) {
                        String categorytitle = category.getTitle();
                        /*if (tutorialCates.getSpeedCategory().containsValue(categorytitle)) {
                            styleMap.put("speed", categorytitle);
                        } else if (tutorialCates.getDifficultyCategory().containsValue(categorytitle)) {
                            styleMap.put("difficult", categorytitle);
                        } else if (tutorialCates.getStyleCategory().containsValue(categorytitle)) {
                            styleMap.put("style", categorytitle);
                        } else {
                            System.out.println("something is wrong with the category");
                        }*/
                    }
                    List<String> itemData = new ArrayList<String>();
                    itemData.add(tutorial.getTitle());
                    if (tutorial.getAuthor() != null) {
                        itemData.add(tutorial.getAuthor().getName());
                    } else {
                        itemData.add("没有关联明星舞队");
                    }
                    if (styleMap.get("speed") != null) {
                        itemData.add(styleMap.get("speed"));
                    } else {
                        itemData.add("没有填教程速度");
                    }
                    if (styleMap.get("difficult") != null) {
                        itemData.add(styleMap.get("difficult"));
                    } else {
                        itemData.add("没有填教程难度");
                    }
                    if (styleMap.get("style") != null) {
                        itemData.add(styleMap.get("style"));
                    } else {
                        itemData.add("没有填教程风格");
                    }

                    itemData.add(timestampTodatetime(tutorial.getUpdate_timestamp()));
                    printer.printRecord(itemData);
                }
            } else if (resource == DanceMusic.class) {
                printer.printRecord("伴奏标题", "舞蹈节奏", "舞蹈风格", "首字母");
                for (Object object : resources) {
                    DanceMusic music = (DanceMusic) object;
                    Set<DanceMusicCategory> categories = music.getCategories();
                    for (DanceMusicCategory category : categories) {
                        String categorytitle = category.getTitle();
                        /*if (musicCates.getBeatCategory().containsValue(categorytitle)) {
                            styleMap.put("beat", categorytitle);
                        } else if (musicCates.getStyleCategory().containsValue(categorytitle)) {
                            styleMap.put("style", categorytitle);
                        } else {
                            styleMap.put("letter", categorytitle);
                        }*/
                    }
                    List<String> itemData = new ArrayList<String>();
                    itemData.add(music.getTitle());

                    if (styleMap.get("beat") != null) {
                        itemData.add(styleMap.get("beat"));
                    } else {
                        itemData.add("没有填舞蹈节奏");
                    }
                    if (styleMap.get("style") != null) {
                        itemData.add(styleMap.get("style"));
                    } else {
                        itemData.add("没有填舞蹈风格");
                    }
                    if (styleMap.get("letter") != null) {
                        itemData.add(styleMap.get("letter"));
                    } else {
                        itemData.add("没有填首字母");
                    }

                    itemData.add(timestampTodatetime(music.getUpdate_timestamp()));
                    printer.printRecord(itemData);
                }
            } else {
                List<String> headers = new ArrayList<String>();
                List<String> attrs = new ArrayList<String>();

                for (Field field : resource.getFields()) {
                    if (field.isAnnotationPresent(CSVTitle.class)) {
                        String title = field.getAnnotation(CSVTitle.class).title();
                        headers.add(title);
                        attrs.add(field.getName());
                    }
                }
                printer.printRecord(headers);

                for (Object object : resources) {
                    List itemData = new ArrayList();
                    for (String attr : attrs) {
                        Field field = resource.getField(attr);
                        if (field.isAnnotationPresent(ModelAttrSuper.class)) {
                            itemData.add(commonDao.getResourceAttr(resource.getSuperclass(), object, attr));
                        } else {
                            itemData.add(commonDao.getResourceAttr(resource, object, attr));
                        }
                    }
                    printer.printRecord(itemData);
                }
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void writeStatisticDataToCSV(Class resource) {
        DBCursor cursor = statisticsDao.getAllStatisticData(resource);
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;

        try {
            Writer out = new FileWriter(String.format("%s%s.csv", DiskFileDirConfig.csvdir, resource.getSimpleName().toLowerCase()));
            printer = new CSVPrinter(out, format.withDelimiter(','));
            HashMap<String, String> styleMap = new HashMap<String, String>();

            List<String> headers = new ArrayList<String>();
            List<String> attrs = new ArrayList<String>();

            for (Field field : resource.getFields()) {
                if (field.isAnnotationPresent(CSVTitle.class)) {
                    String title = field.getAnnotation(CSVTitle.class).title();
                    headers.add(title);
                    attrs.add(field.getName());
                }
            }
            printer.printRecord(headers);

            while (cursor.hasNext()) {
                List itemData = new ArrayList();
                DBObject object = cursor.next();
                for (String attr : attrs) {
                    Field field = resource.getField(attr);
                    Object value = object.get(field.getName().toLowerCase());
                    System.out.println(value);
                    itemData.add(value);
                }
                printer.printRecord(itemData);
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void writeAuthorVideosToCSV(Integer id) {
        String authorname = ((DanceGroup) commonDao.getResourceById(DanceGroup.class, id)).getName();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        Class videoClass = DanceVideo.class;
        List videos = commonDao.getResourcesByFields(videoClass, conditions);

        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter(String.format("%s%s.csv", DiskFileDirConfig.csvdir, String.format("author-%s", authorname)));
            printer = new CSVPrinter(out, format.withDelimiter(','));

            printer.printRecord(authorname);

            for (Object video : videos) {
                printer.printRecord(commonDao.getResourceAttr(videoClass, video, "title"));
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<byte[]> downloadCSVFiles(String filename) throws IOException {
        String path = String.format("%s%s.csv", DiskFileDirConfig.csvdir, filename);
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String(String.format("%s.csv", filename).getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        byte[] bomHead = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] download = mergeByteArray(bomHead, fileBytes);
        return new ResponseEntity<byte[]>(download,
                headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "admin/download/{type}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadResources(@PathVariable String type) throws IOException {
        writeResourcesToCSV(TypeClassMapping.typeClassMap.get(type));
        return downloadCSVFiles(type);
    }

    @RequestMapping(value = "admin/download/stat/{type}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadStatResources(@PathVariable String type) throws IOException {
        writeStatisticDataToCSV(TypeClassMapping.typeClassMap.get(type));
        return downloadCSVFiles(type);
    }

    @RequestMapping(value = "admin/download/authorvideos/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAuthorvideos(@PathVariable DanceGroupType type, @PathVariable Integer id) throws IOException {
        String authorname = ((DanceGroup) commonDao.getResourceById(DanceGroup.class, id)).getName();
        writeAuthorVideosToCSV(id);
        return downloadCSVFiles(String.format("author-%s", authorname));
    }
}