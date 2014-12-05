package com.springapp.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.ImageDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.EducationCategory;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.EducationCategory;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.UpdateCheckResponse;
import com.darfoo.backend.model.Video;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class EducationDaoTests {
	@Autowired
	EducationDao educationDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;

	@Test
	public void insertAllEducationCategories(){
		educationDao.insertAllEducationCategories();
	}
	
	@Test
	public void insertSingleEducationVideo(){
        String title = "Strong Heart123";
        String authorName = "周杰伦";
        String imagekey = "仓木麻衣333.jpg";

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

        Education queryVideo = educationDao.getEducationVideoByTitle(title);
        if (queryVideo == null){
            System.out.println("教程不存在，可以进行插入");
        }else{
            System.out.println(queryVideo.toString(true));
            System.out.println("教程已存在，不可以进行插入了，是否需要修改");
            return;
        }

		Education video = new Education();
		video.setAuthor(a);
		video.setImage(image);
		EducationCategory c1 = new EducationCategory();	
		EducationCategory c2 = new EducationCategory();	
		EducationCategory c3 = new EducationCategory();	
		c1.setTitle("快");
		c2.setTitle("适中");
		c3.setTitle("分解教学");
		Set<EducationCategory> s_eCategory = video.getCategories();
		s_eCategory.add(c1);
		s_eCategory.add(c2);
		s_eCategory.add(c3);
		video.setTitle(title);
		video.setVideo_key(title);
		video.setUpdate_timestamp(System.currentTimeMillis());
		educationDao.inserSingleEducationVideo(video);
	}
	
	@Test
	public void getEducationVideoById(){
		Education video = educationDao.getEducationVideoById(1);
		if(video != null){
			System.out.println(video.toString(true));
		}else{
			System.out.println("无此ID视频信息");
		}
	}
	
	@Test
	public void getEducationVideosByCategories(){
		long start = System.currentTimeMillis();
		String[] categories = {};//无条件限制
		//String[] categories = {"较快","稍难","情歌风","S"}; //满条件限制
		//String[] categories = {"快","分解教学"};
		List<Education> videos = educationDao.getEducationVideosByCategories(categories);
		System.out.println("最终满足的video数量>>>>>>>>>>>>>>>>>>>>>"+videos.size());
		for(Education video : videos){
			System.out.println(video.toString());
			System.out.println("——————————————————————————————————————");
		}
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);
	}
	
	@Test
	public void deleteEducationById(){
		System.out.println(CRUDEvent.getResponse(educationDao.deleteEducationById(24)));
	}
	
	
	/**
	 * 更新操作可以参考这个测试
	 * **/
	@Test
	public void updateEducationById(){
		Integer vid = 5;
		String authorName = "滨崎步";
		String imageKey = "滨崎步.jpg";		
		UpdateCheckResponse response = educationDao.updateEducationCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
		System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
		String videoSpeed = "快";  //"快","中","慢"//按速度
		String videoDifficuty = "稍难";  //"简单","适中","稍难"//按难度  
		String videoStyle = "队形表演";	//"队形表演","背面教学","分解教学"//按教学类型
		Set<String> categoryTitles = new HashSet<String>();
		categoryTitles.add(videoSpeed);
		categoryTitles.add(videoDifficuty);
		categoryTitles.add(videoStyle);
		if(response.updateIsReady()){
			//updateIsReady为true表示可以进行更新操作
			System.out.println(CRUDEvent.getResponse(educationDao.updateEducation(vid, authorName, imageKey,categoryTitles,System.currentTimeMillis())));
		}else{
			System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
		}
	}
	
	/**
	 * 获取所有的education对象
	 * **/
	@Test
	public void getAllEducations(){
		List<Education> s_educations = new ArrayList<Education>();
		s_educations = educationDao.getAllEdutcaion();
		System.out.println("总共查到"+s_educations.size());
		for(Education video : s_educations){
			System.out.println("----------------");
            System.out.println("id: " + video.getId());
			System.out.println(video.toString(true));
			
		}
	}
}
