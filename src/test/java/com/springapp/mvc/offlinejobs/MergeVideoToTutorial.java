package com.springapp.mvc.offlinejobs;

import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.VideoCates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zjh on 15-3-11.
 */

//将欣赏视频合并到教学视频中
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class MergeVideoToTutorial {
    @Autowired
    CommonDao commonDao;
    @Autowired
    VideoCates videoCates;

    //因为之前只是在video和tutorial中各自保重了同一个authorid下面不存在duplicate的title 但是合并在一起就不一定了所以要先把这些无法插入的video找出来
    //此外由于相同imagekey的图片资源不能重复插入所以还不能单纯地使用CommonDao#insertResource函数来讲video资源插入到tutorial资源表中
    /*@Test
    public void getUnValidVideos() {
        List<Video> videos = commonDao.getAllResource(Video.class);
        String content = "";

        for (Video video : videos) {
            //System.out.println(video.toString(true));
            System.out.println("current process video -> " + video.getId());
            String title = video.getTitle();
            Integer authorid = video.getAuthor().getId();

            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("title", title + "-欣赏");
            conditions.put("author_id", authorid);

            Object query = commonDao.getResourceByFields(Tutorial.class, conditions);

            if (query != null) {
                System.out.println(String.format("视频名字和作者id组合已存在不可以进行插入了 视频标题为 -> %d 视频标题为 -> %s", video.getId(), title));
                content += String.format("id -> %d 标题 -> %s\n", video.getId(), title);
            }
        }

        try {
            File file = new File("duplicatevideos.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    //由于之前的疏忽 video和tutorial中的video_key这个字段还会出现重复
    /*@Test
    public void getUnValidVideosAgain() {
        List<Video> videos = commonDao.getAllResource(Video.class);
        String content = "";

        for (Video video : videos) {
            //System.out.println(video.toString(true));
            System.out.println("current process video -> " + video.getId());
            String videokey = video.getVideo_key();

            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("video_key", videokey);

            Object query = commonDao.getResourceByFields(Tutorial.class, conditions);

            if (query != null) {
                System.out.println(String.format("相同videokey的资源已经存在了 视频标题为 -> %d", video.getId()));
                content += String.format("id -> %d\n", video.getId());
            }
        }

        try {
            File file = new File("duplicatevideos.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    //mvn -Dtest=MergeVideoToTutorial test
    //should be 924 video resource there
    @Test
    public void insertVideoToTutorial() {
        List<Video> videos = commonDao.getAllResource(Video.class);

        for (Video video : videos) {
            System.out.println("current process video -> " + video.getId());

            Tutorial tutorial = new Tutorial();
            tutorial.setAuthor(video.getAuthor());
            tutorial.setImage(video.getImage());
            tutorial.setMusic(video.getMusic());
            tutorial.setTitle(video.getTitle() + "-欣赏");
            tutorial.setVideo_key(video.getVideo_key());
            tutorial.setUpdate_timestamp(video.getUpdate_timestamp());
            tutorial.setHottest(video.getHottest());
            tutorial.setRecommend(video.getRecommend());

            Set<String> categoryTitles = new HashSet<String>();
            for (VideoCategory category : video.getCategories()) {
                String categorytitle = category.getTitle();
                if (videoCates.getSpeedCategory().containsValue(categorytitle) || videoCates.getDifficultyCategory().containsValue(categorytitle)) {
                    categoryTitles.add(videoCates.categoryMap.get(categorytitle));
                }
            }
            categoryTitles.add("队形表演");

            commonDao.saveResourceWithCategory(Tutorial.class, tutorial, categoryTitles);
        }
    }
}
