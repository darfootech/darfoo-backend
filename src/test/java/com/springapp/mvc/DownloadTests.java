package com.springapp.mvc;

/**
 * Created by zjh on 14-12-11.
 */

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.MusicCategory;
import com.darfoo.backend.model.category.TutorialCategory;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.MusicCates;
import com.darfoo.backend.service.responsemodel.TutorialCates;
import com.darfoo.backend.service.responsemodel.VideoCates;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DownloadTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    VideoCates videoCates;
    @Autowired
    TutorialCates tutorialCates;
    @Autowired
    MusicCates musicCates;

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
            Writer out = new FileWriter(String.format("%s.csv", resource.getSimpleName().toLowerCase()));
            printer = new CSVPrinter(out, format.withDelimiter(','));
            HashMap<String, String> styleMap = new HashMap<String, String>();

            if (resource == Video.class) {
                printer.printRecord("视频标题", "明星舞队名称", "舞蹈速度", "舞蹈难度", "舞蹈风格", "首字母");
                for (Object object : resources) {
                    Video video = (Video) object;
                    Set<VideoCategory> categories = video.getCategories();
                    for (VideoCategory category : categories) {
                        String categorytitle = category.getTitle();
                        if (videoCates.getSpeedCategory().containsValue(categorytitle)) {
                            styleMap.put("speed", categorytitle);
                        } else if (videoCates.getDifficultyCategory().containsValue(categorytitle)) {
                            styleMap.put("difficult", categorytitle);
                        } else if (videoCates.getStyleCategory().containsValue(categorytitle)) {
                            styleMap.put("style", categorytitle);
                        } else {
                            styleMap.put("letter", categorytitle);
                        }
                    }
                    List<String> itemData = new ArrayList<String>();
                    itemData.add(video.getTitle());
                    if (video.getAuthor() != null) {
                        itemData.add(video.getAuthor().getName());
                    } else {
                        itemData.add("没有关联明星舞队");
                    }
                    if (styleMap.get("speed") != null) {
                        itemData.add(styleMap.get("speed"));
                    } else {
                        itemData.add("没有填视频速度");
                    }
                    if (styleMap.get("difficult") != null) {
                        itemData.add(styleMap.get("difficult"));
                    } else {
                        itemData.add("没有填视频难度");
                    }
                    if (styleMap.get("style") != null) {
                        itemData.add(styleMap.get("style"));
                    } else {
                        itemData.add("没有填视频风格");
                    }
                    if (styleMap.get("letter") != null) {
                        itemData.add(styleMap.get("letter"));
                    } else {
                        itemData.add("没有填首字母");
                    }

                    itemData.add(timestampTodatetime(video.getUpdate_timestamp()));
                    printer.printRecord(itemData);
                }
            } else if (resource == Tutorial.class) {
                printer.printRecord("教程标题", "明星舞队名称", "舞蹈速度", "舞蹈难度", "舞蹈风格");
                for (Object object : resources) {
                    Tutorial tutorial = (Tutorial) object;
                    Set<TutorialCategory> categories = tutorial.getCategories();
                    for (TutorialCategory category : categories) {
                        String categorytitle = category.getTitle();
                        if (tutorialCates.getSpeedCategory().containsValue(categorytitle)) {
                            styleMap.put("speed", categorytitle);
                        } else if (tutorialCates.getDifficultyCategory().containsValue(categorytitle)) {
                            styleMap.put("difficult", categorytitle);
                        } else if (tutorialCates.getStyleCategory().containsValue(categorytitle)) {
                            styleMap.put("style", categorytitle);
                        } else {
                            System.out.println("something is wrong with the category");
                        }
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
            } else if (resource == Music.class) {
                printer.printRecord("伴奏标题", "舞蹈节奏", "舞蹈风格", "首字母");
                for (Object object : resources) {
                    Music music = (Music) object;
                    Set<MusicCategory> categories = music.getCategories();
                    for (MusicCategory category : categories) {
                        String categorytitle = category.getTitle();
                        if (musicCates.getBeatCategory().containsValue(categorytitle)) {
                            styleMap.put("beat", categorytitle);
                        } else if (musicCates.getStyleCategory().containsValue(categorytitle)) {
                            styleMap.put("style", categorytitle);
                        } else {
                            styleMap.put("letter", categorytitle);
                        }
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
                System.out.println("wired");
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeVideosToCSV() {
        writeResourcesToCSV(Video.class);
    }

    @Test
    public void writeTutorialsToCSV() {
        writeResourcesToCSV(Tutorial.class);
    }

    @Test
    public void writeMusicsToCSV() {
        writeResourcesToCSV(Music.class);
    }
}
