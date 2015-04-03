package com.darfoo.backend.utils;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.category.DanceMusicCategory;
import com.darfoo.backend.model.category.DanceVideoCategory;
import com.darfoo.backend.model.cota.annotations.CSVTitle;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.category.DanceMusicCates;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;

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
 * Created by zjh on 15-4-3.
 */
public class DownloadUtils {
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

            List<String> headers = new ArrayList<String>();
            List<String> attrs = new ArrayList<String>();

            for (Field field : resource.getDeclaredFields()) {
                System.out.println("2333333333");
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
                    Object value = "";
                    if (attr.equals("author")) {
                        DanceGroup danceGroup = (DanceGroup) commonDao.getResourceAttr(resource, object, attr);
                        if (danceGroup != null) {
                            value = danceGroup.getName();
                        } else {
                            value = "未关联舞队";
                        }
                    } else if (attr.equals("type")) {
                        value = TypeClassMapping.typeNameMap.get(commonDao.getResourceAttr(resource, object, attr));
                    } else {
                        value = commonDao.getResourceAttr(resource, object, attr);
                    }
                    System.out.println(value);
                    itemData.add(value);
                }
                printer.printRecord(itemData);
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
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
                    Object value = object.get(attr);
                    System.out.println(value);
                    itemData.add(value);
                }
                printer.printRecord(itemData);
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
