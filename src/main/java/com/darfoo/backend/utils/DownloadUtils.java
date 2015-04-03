package com.darfoo.backend.utils;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.cota.annotations.CSVTitle;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.category.DanceMusicCates;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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


    public ResponseEntity<byte[]> downloadCSVFiles(String filename) throws IOException {
        String path = String.format("%s%s.csv", DiskFileDirConfig.csvdir, filename);
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String(String.format("%s.csv", filename).getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] fileBytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
        byte[] bomHead = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] download = mergeByteArray(bomHead, fileBytes);
        return new ResponseEntity<byte[]>(download,
                headers, HttpStatus.CREATED);
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

    /**
     * id指向的舞队所关联的所有舞蹈视频
     *
     * @param id
     */
    public void writeVideosOfDanceGroupToCSV(Integer id) {
        String dancegroupName = ((DanceGroup) commonDao.getResourceById(DanceGroup.class, id)).getName();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        Class videoClass = DanceVideo.class;
        List videos = commonDao.getResourcesByFields(videoClass, conditions);

        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter(String.format("%s%s.csv", DiskFileDirConfig.csvdir, String.format("dancegroupvideos-%d", id)));
            printer = new CSVPrinter(out, format.withDelimiter(','));

            printer.printRecord(dancegroupName);

            for (Object video : videos) {
                printer.printRecord(commonDao.getResourceAttr(videoClass, video, "title"));
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
