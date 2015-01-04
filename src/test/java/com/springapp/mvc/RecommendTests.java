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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class RecommendTests {

    @Test
    public void addRecommendList(){
        String[] images = {"image1", "image2", "image3"};
        String[] videos = {"video1", "video2", "video3"};
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
}
