package com.springapp.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.ImageDao;
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
import com.darfoo.backend.model.UpdateCheckResponse;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class VideoDaoTests {
	@Autowired
	VideoDao videoDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    VideoCates videoCates = new VideoCates();

    @Test
	public void insertAllVideoCategories(){
		videoDao.insertAllVideoCategories();
	}

	@Test
	public void insertSingleVideo(){
        String videoTitle = "clea33";
        String authorName = "滨崎步";
        String imagekey = "滨崎步333.jpg";

        Author a = authorDao.getAuthor(authorName);
        if(a != null){
            System.out.println(a.getName());
        }
        else{
            System.out.println("无该author记录");
            return;
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.inserSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

        int authorid = a.getId();
        //视频title可以重名,但是不可能出现视频title一样,作者id都一样的情况,也就是一个作者的作品中不会出现重名的情况
        Video queryVideo = videoDao.getVideoByTitleAuthorId(videoTitle, authorid);
        if (queryVideo == null){
            System.out.println("视频名字和作者id组合不存在，可以进行插入");
        }else{
            System.out.println(queryVideo.toString(true));
            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
            return;
        }

        Video video = new Video();
		video.setAuthor(a);
		video.setImage(image);
		VideoCategory c1 = new VideoCategory();	
		VideoCategory c2 = new VideoCategory();	
		VideoCategory c3 = new VideoCategory();	
		VideoCategory c4 = new VideoCategory();	
		c1.setTitle("适中");
		c2.setTitle("中等");
		c3.setTitle("情歌风");
		c4.setTitle("D");
		Set<VideoCategory> s_vCategory = video.getCategories();
		s_vCategory.add(c1);
		s_vCategory.add(c2);
		s_vCategory.add(c3);
		s_vCategory.add(c4);
		video.setTitle(videoTitle);
		video.setVideo_key(videoTitle + System.currentTimeMillis());
		video.setUpdate_timestamp(System.currentTimeMillis());
		int insertStatus = videoDao.inserSingleVideo(video);
        if (insertStatus == -1){
            System.out.println("插入视频失败");
        }else{
            System.out.println("插入视频成功，视频id是" + insertStatus);
        }

        videoDao.updateVideoKeyById(insertStatus, videoTitle + "-" + insertStatus);

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
	public void deleteVideoCascade(){
		System.out.println(CRUDEvent.getResponse(videoDao.deleteVideoById(18)));
	}
	
	/**
	 * 更新操作可以参考这个测试
	 * **/
	@Test
	public void updateVideoById(){
		Integer vid = 4;
		String authorName = "仓木麻衣";
		String imageKey = "仓木麻衣.jpg";		
		UpdateCheckResponse response = videoDao.updateVideoCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
		System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
		String videoSpeed = "适中";
		String videoDifficuty = "稍难";
		String videoStyle = "戏曲风";
		String videoLetter = "q";		
		Set<String> categoryTitles = new HashSet<String>();
		categoryTitles.add(videoSpeed);
		categoryTitles.add(videoDifficuty);
		categoryTitles.add(videoStyle);
		categoryTitles.add(videoLetter.toUpperCase());
		if(response.updateIsReady()){
			//updateIsReady为true表示可以进行更新操作
			System.out.println(CRUDEvent.getResponse(videoDao.updateVideo(vid, authorName, imageKey,categoryTitles,System.currentTimeMillis())));
		}else{
			System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
		}
	}
	
	/**
	 * 获取所有的video对象
	 * **/
	@Test
	public void getAllVideos(){
		List<Video> s_videos = new ArrayList<Video>();
		s_videos = videoDao.getAllVideo();
		for(Video video : s_videos){
			System.out.println("----------------");
            System.out.println("videois: " + video.getId());
            System.out.println(video.toString(true));
		}
        System.out.println("总共查到"+s_videos.size());
    }
}
