package com.springapp.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.darfoo.backend.model.Video;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class EducationDaoTests {
	@Autowired
	EducationDao educationDao;

	@Test
	public void insertAllEducationCategories(){
		educationDao.insertAllEducationCategories();
	}
	
	@Test
	public void insertSingleEducationVideo(){
		Education video = new Education();
		Author a1 = new Author();
		a1.setName("周杰伦");
		a1.setDescription("日本女歌手");
		video.setAuthor(a1);
		Image img = new Image();
		img.setImage_key("仓木麻衣.jpg");
		video.setImage(img);
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
		video.setTitle("Strong Heart");
		video.setVideo_key("StrongHeart");
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
}
