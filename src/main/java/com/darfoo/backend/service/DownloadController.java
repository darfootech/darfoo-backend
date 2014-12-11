package com.darfoo.backend.service;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 14-12-11.
 */

@Controller
public class DownloadController {
    @Autowired
    VideoDao videoDao;
    @Autowired
    EducationDao educationDao;
    @Autowired
    MusicDao musicDao;

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
                List<String> itemData = new ArrayList<String>();
                itemData.add(video.getTitle());
                if (video.getAuthor() != null) {
                    itemData.add(video.getAuthor().getName());
                }else{
                    itemData.add("没有关联明星舞队");
                }
                if (styleMap.get("speed") != null) {
                    itemData.add(styleMap.get("speed"));
                }else{
                    itemData.add("没有填视频速度");
                }
                if (styleMap.get("difficult") != null) {
                    itemData.add(styleMap.get("difficult"));
                }else{
                    itemData.add("没有填视频难度");
                }
                if (styleMap.get("style") != null) {
                    itemData.add(styleMap.get("style"));
                }else{
                    itemData.add("没有填视频风格");
                }
                if (styleMap.get("letter") != null) {
                    itemData.add(styleMap.get("letter"));
                }else{
                    itemData.add("没有填首字母");
                }
                printer.printRecord(itemData);
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

    @RequestMapping(value = "downloadvideos", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadvideos() throws IOException {
        writeVideosToCSV();

        String path = "video.csv";
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("video.csv".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }

    public void writeTutorialsToCSV(){
        List<Education> educations = educationDao.getAllEducation();
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter("tutorial.csv");
            printer = new CSVPrinter(out, format.withDelimiter(','));
            System.out.println("********");
            printer.printRecord("教程标题", "明星舞队名称", "舞蹈速度", "舞蹈难度", "舞蹈风格");
            for(Education tutorial : educations){
                HashMap<String, String> styleMap = new HashMap<String, String>();
                Set<EducationCategory> categories = tutorial.getCategories();
                for (EducationCategory category : categories){
                    int categoryid = category.getId();
                    String categorytitle = category.getTitle();
                    System.out.println(categoryid);
                    System.out.println(categorytitle);
                    if (categorytitle.equals("快") || categorytitle.equals("中") || categorytitle.equals("慢")){
                        styleMap.put("speed", categorytitle);
                    }else if(categorytitle.equals("简单") || categorytitle.equals("适中") || categorytitle.equals("稍难")){
                        styleMap.put("difficult", categorytitle);
                    }else if(categorytitle.equals("背面教学") || categorytitle.equals("分解教学") || categorytitle.equals("队形表演")){
                        styleMap.put("style", categorytitle);
                    }else{
                        System.out.println("something is wrong with the category");
                    }
                }
                List<String> itemData = new ArrayList<String>();
                itemData.add(tutorial.getTitle());
                if (tutorial.getAuthor() != null) {
                    itemData.add(tutorial.getAuthor().getName());
                }else{
                    itemData.add("没有关联明星舞队");
                }
                if (styleMap.get("speed") != null) {
                    itemData.add(styleMap.get("speed"));
                }else{
                    itemData.add("没有填教程速度");
                }
                if (styleMap.get("difficult") != null) {
                    itemData.add(styleMap.get("difficult"));
                }else{
                    itemData.add("没有填教程难度");
                }
                if (styleMap.get("style") != null) {
                    itemData.add(styleMap.get("style"));
                }else{
                    itemData.add("没有填教程风格");
                }

                printer.printRecord(itemData);
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

    @RequestMapping(value = "downloadtutorials", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadtutorials() throws IOException {
        writeTutorialsToCSV();

        String path = "tutorial.csv";
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("tutorial.csv".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }

    public void writeMusicsToCSV(){
        List<Music> musics = musicDao.getAllMusic();
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = null;
        try {
            Writer out = new FileWriter("music.csv");
            printer = new CSVPrinter(out, format.withDelimiter(','));
            System.out.println("********");
            printer.printRecord("伴奏标题", "舞蹈节奏", "舞蹈风格", "首字母");
            for(Music music : musics){
                HashMap<String, String> styleMap = new HashMap<String, String>();
                Set<MusicCategory> categories = music.getCategories();
                for (MusicCategory category : categories){
                    int categoryid = category.getId();
                    String categorytitle = category.getTitle();
                    System.out.println(categoryid);
                    System.out.println(categorytitle);
                    if (categoryid >= 1 && categoryid <= 4){
                        styleMap.put("beat", categorytitle);
                    }else if(categoryid >= 5 && categoryid <= 12){
                        styleMap.put("style", categorytitle);
                    }else if(categoryid >= 13 && categoryid <= 38){
                        styleMap.put("letter", categorytitle);
                    }else{
                        System.out.println("something is wrong with the category");
                    }
                }
                List<String> itemData = new ArrayList<String>();
                itemData.add(music.getTitle());

                if (styleMap.get("beat") != null) {
                    itemData.add(styleMap.get("beat"));
                }else{
                    itemData.add("没有填舞蹈节奏");
                }
                if (styleMap.get("style") != null) {
                    itemData.add(styleMap.get("style"));
                }else{
                    itemData.add("没有填舞蹈风格");
                }
                if (styleMap.get("letter") != null) {
                    itemData.add(styleMap.get("letter"));
                }else{
                    itemData.add("没有填首字母");
                }
                printer.printRecord(itemData);
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

    @RequestMapping(value = "downloadmusics", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadmusics() throws IOException {
        writeMusicsToCSV();

        String path = "music.csv";
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("music.csv".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }
}
