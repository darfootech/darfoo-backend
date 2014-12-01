package com.springapp.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.darfoo.backend.dao.AuthorDao;
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
        String title = "Strong Heart";
        String authorName = "周杰伦";
        String imagekey = "仓木麻衣.jpg";

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
		System.out.println(educationDao.deleteEducationById(5)>0?"delete success":"delete fail");
	}
}
