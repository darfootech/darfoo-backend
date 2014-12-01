package com.springapp.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.service.responsemodel.VideoCates;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.model.VideoCategory;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class VideoDaoTests {
	@Autowired
	VideoDao videoDao;
    @Autowired
    AuthorDao authorDao;
    VideoCates videoCates = new VideoCates();

    @Test
	public void insertAllVideoCategories(){
		videoDao.insertAllVideoCategories();
	}
	
	@Test
	public void inserSingleVideo(){
        String videoTitle = "cleantha";
        String authorName = "滨崎步";

        Author a = authorDao.getAuthor(authorName);
        if(a != null){
            System.out.println(a.getName());
        }
        else{
            System.out.println("无该author记录");
            return;
        }

        Video queryVideo = videoDao.getVideoByVideoTitle(videoTitle);
        if (queryVideo == null){
            System.out.println("对象不存在，可以进行插入");
        }else{
            System.out.println(queryVideo.toString(true));
            System.out.println("对象已存在，不可以进行插入了，是否需要修改");
            return;
        }

        Video video = new Video();
		Author a1 = new Author();
		a1.setName(authorName);
		a1.setDescription("日本女歌手");
		video.setAuthor(a1);
		Image img = new Image();
		img.setImage_key("滨崎步.jpg");
		video.setImage(img);
		VideoCategory c1 = new VideoCategory();	
		VideoCategory c2 = new VideoCategory();	
		VideoCategory c3 = new VideoCategory();	
		VideoCategory c4 = new VideoCategory();	
		c1.setTitle("较快");
		c2.setTitle("普通");
		c3.setTitle("情歌风");
		c4.setTitle("D");
		Set<VideoCategory> s_vCategory = video.getCategories();
		s_vCategory.add(c1);
		s_vCategory.add(c2);
		s_vCategory.add(c3);
		s_vCategory.add(c4);
		video.setTitle(videoTitle);
		video.setVideo_key(videoTitle);
		video.setUpdate_timestamp(System.currentTimeMillis());
		videoDao.inserSingleVideo(video);
	}
	
	@Test
	public void getVideoByVideoId(){
		long start = System.currentTimeMillis();
		Video video = videoDao.getVideoByVideoId(2);
		System.out.println(video.toString(true));
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);
	}

    @Test
    public void getVideoByVideoTitle(){
        long start = System.currentTimeMillis();
        Video video = videoDao.getVideoByVideoTitle("ccc");
        if (video == null){
            System.out.println("对象不存在，可以进行插入");
        }else{
            System.out.println(video.toString(true));
            System.out.println("对象已存在，不可以进行插入了，是否需要修改");
        }
        System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);
    }

	@Test
	public void getRecommendVideos(){
		long start = System.currentTimeMillis();
		List<Video> videos = videoDao.getRecommendVideos(7);
		for(Video video : videos){
			System.out.println(video.toString(true));
			System.out.println("——————————————————————————————————————");
		}
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);		
	}
	@Test
	public void getLastestVideos(){
		long start = System.currentTimeMillis();
		List<Video> videos = videoDao.getLatestVideos(7);
		for(Video video : videos){
			System.out.println(video.toString(true));
			System.out.println("——————————————————————————————————————");
		}
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);		
	}
	@Test
	public void getVideosByCategories(){
		long start = System.currentTimeMillis();
		//String[] categories = {};//无条件限制
		//String[] categories = {"较快","稍难","情歌风","S"}; //满足所有条件
		//String[] categories = {"较快","普通","优美","0"}; //有一个条件不满足
		String[] categories = {"较快"};//满足单个条件
		List<Video> videos = videoDao.getVideosByCategories(categories);
		System.out.println("最终满足的video数量>>>>>>>>>>>>>>>>>>>>>"+videos.size());
		for(Video video : videos){
			System.out.println(video.toString());
			System.out.println("——————————————————————————————————————");
		}
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);	
	}
	
	@Test
    public void requestVideosByCategories(){
        String categories = "0-1-4-3";
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        if (!requestCategories[0].equals("0")){
            String speedCate = videoCates.getSpeedCategory().get(requestCategories[0]);
            targetCategories.add(speedCate);
        }
        if (!requestCategories[1].equals("0")){
            String difficultyCate = videoCates.getDifficultyCategory().get(requestCategories[1]);
            targetCategories.add(difficultyCate);
            System.out.println("!!!!speedcate!!!!" + difficultyCate);
        }
        if (!requestCategories[2].equals("0")){
            String styleCate = videoCates.getStyleCategory().get(requestCategories[2]);
            targetCategories.add(styleCate);
        }
        if (!requestCategories[3].equals("0")){
            String letterCate = requestCategories[3];
            targetCategories.add(letterCate);
        }

        System.out.println(targetCategories.toString());
        System.out.println(requestCategories[0]);
        System.out.println(requestCategories[0].equals("0"));
    }
	
	@Test
	public void deleteVideoById(){
		System.out.println(videoDao.deleteVideoById(3)>0?"delete success":"delete fail");
	}
	
	@Test
	public void deleteVideoCascade(){
		videoDao.deleteVideoCascade(7);
	}
}
