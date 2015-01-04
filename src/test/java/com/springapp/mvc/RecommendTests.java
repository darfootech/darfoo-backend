package com.springapp.mvc;

/**
 * Created by zjh on 15-1-4.
 */

import com.darfoo.backend.model.Video;
import com.darfoo.backend.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class RecommendTests {

    @Test
    public void addRecommendList(){
        String[] images = {"image4", "image5", "image6"};
        String[] videos = {"video4", "video5", "video6"};
        String imagefilename = "recommendimage.data";
        String videofilename = "recommendvideo.data";
        FileUtils.createFile(imagefilename);
        FileUtils.createFile(videofilename);

        File imagefile = new File(imagefilename);
        File videofile = new File(videofilename);

        try {
            FileOutputStream imageos = new FileOutputStream(imagefile);
            FileOutputStream videoos = new FileOutputStream(videofile);
            StringBuilder imagesb = new StringBuilder();
            StringBuilder videosb = new StringBuilder();
            for (int i=0; i<images.length; i++){
                imagesb.append(images[i]).append("\n");
            }

            for (int i=0; i<videos.length; i++){
                videosb.append(videos[i]).append("\n");
            }

            byte[] imageBytes = imagesb.toString().getBytes();
            byte[] videoBytes = videosb.toString().getBytes();
            imageos.write(imageBytes, 0, imageBytes.length);
            videoos.write(videoBytes, 0, videoBytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRecommendList(){
        String imagefilename = "recommendimage.data";
        String videofilename = "recommendvideo.data";

        try {
            BufferedReader imageReader = new BufferedReader(new InputStreamReader(new FileInputStream(imagefilename), "UTF-8"));
            BufferedReader videoReader = new BufferedReader(new InputStreamReader(new FileInputStream(videofilename), "UTF-8"));

            String imageline = null;
            while ((imageline = imageReader.readLine()) != null) {
                System.out.println(imageline);
            }

            String videoline = null;
            while ((videoline = videoReader.readLine()) != null) {
                System.out.println(videoline);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setRecommendList(){
        List<Integer> videoids = new ArrayList<Integer>();
        videoids.add(41);
        videoids.add(40);
        videoids.add(39);
        String flag = "video";
        String filename = "recommend" + flag + ".data";
        FileUtils.createFile(filename);

        File file = new File(filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (Integer videoid : videoids){
                stringBuilder.append(videoid.toString()).append("\n");
            }

            byte[] bytes = stringBuilder.toString().getBytes();
            outputStream.write(bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRecommendListTest(){
        String flag = "video";
        String filename = "recommend" + flag + ".data";
        FileUtils.createFile(filename);
        List<Integer> videoList = new ArrayList<Integer>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                videoList.add(Integer.parseInt(line));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateRecommendList(){
        List<Integer> videoids = new ArrayList<Integer>();
        videoids.add(40);
        String flag = "video";
        String filename = "recommend" + flag + ".data";
        List<Integer> videoList = new ArrayList<Integer>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                videoList.add(Integer.parseInt(line));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (videoList.size() == 0){
            return;
        }else{
            FileUtils.createFile(filename);

            File file = new File(filename);

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                StringBuilder stringBuilder = new StringBuilder();

                for (Integer videoid : videoList){
                    if (!videoids.contains(videoid)){
                        stringBuilder.append(videoid.toString()).append("\n");
                    }
                }

                byte[] bytes = stringBuilder.toString().getBytes();
                outputStream.write(bytes, 0, bytes.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
