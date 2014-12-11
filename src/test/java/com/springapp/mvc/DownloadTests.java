package com.springapp.mvc;

/**
 * Created by zjh on 14-12-11.
 */

import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.model.VideoCategory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DownloadTests {
    @Autowired
    VideoDao videoDao;

    @Test
    public void writeVideosToCSV(){
        List<Video> videos = videoDao.getAllVideo();
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter("video.csv");
            printer = new CSVPrinter(out, format.withDelimiter(','));
            System.out.println("********");
            printer.printRecord("视频标题", "明星舞队名称", "舞蹈速度", "舞蹈难度", "舞蹈风格", "首字母");
            for(Video video : videos){
                HashMap<String, String> styleMap = new HashMap<String, String>();
                Set<VideoCategory> categories = video.getCategories();
                for (VideoCategory category : categories){
                    int categoryid = category.getId();
                    String categorytitle = category.getTitle();
                    System.out.println(categoryid);
                    System.out.println(categorytitle);
                    if (categoryid >= 1 && categoryid <= 3){
                        styleMap.put("speed", category.getTitle());
                    }else if(categoryid >= 4 && categoryid <= 6){
                        styleMap.put("difficult", category.getTitle());
                    }else if(categoryid >= 7 && categoryid <= 17){
                        styleMap.put("style", category.getTitle());
                    }else if(categoryid >= 18 && categoryid <= 43){
                        styleMap.put("letter", category.getTitle());
                    }else{
                        System.out.println("something is wrong with the category");
                    }
                }
                List<String> videoData = new ArrayList<String>();
                videoData.add(video.getTitle());
                if (video.getAuthor() != null) {
                    videoData.add(video.getAuthor().getName());
                }else{
                    videoData.add("没有关联明星舞队");
                }
                if (styleMap.get("speed") != null) {
                    videoData.add(styleMap.get("speed"));
                }else{
                    videoData.add("没有填视频速度");
                }
                if (styleMap.get("difficult") != null) {
                    videoData.add(styleMap.get("speed"));
                }else{
                    videoData.add("没有填视频速度");
                }
                if (styleMap.get("style") != null) {
                    videoData.add(styleMap.get("speed"));
                }else{
                    videoData.add("没有填视频速度");
                }
                if (styleMap.get("letter") != null) {
                    videoData.add(styleMap.get("speed"));
                }else{
                    videoData.add("没有填视频速度");
                }
                printer.printRecord(videoData);
            }
            //close the printer
            printer.flush();
            printer.close();
            //out.flush();
            //out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
