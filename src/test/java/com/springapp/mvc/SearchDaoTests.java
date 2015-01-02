package com.springapp.mvc;

import java.util.List;

import com.darfoo.backend.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.DanceDao;
import com.darfoo.backend.dao.SearchDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class SearchDaoTests {
	@Autowired
	private SearchDao searchDao;
	
	@Test
	public void searchVideo(){
		String searchContent = "七里香";
		List<Video> l_video = searchDao.getVideoBySearch(searchContent);
		for(Video v : l_video){
			System.out.println(v.getTitle());
			System.out.println("————————————————————————————————");
		}
	}

	@Test
	public void searchMusic(){
		String searchContent = "dear";
		List<Music> l = searchDao.getMusicBySearch(searchContent);
		for(Music v : l){
			System.out.println(v.toString());
			System.out.println("————————————————————————————————");
		}
	}
	
	@Test
	public void searchEducation(){
		String searchContent = "heart";
		List<Education> l = searchDao.getEducationBySearch(searchContent);
		for(Education v : l){
			System.out.println(v.toString());
			System.out.println("————————————————————————————————");
		}
	}

    @Test
    public void searchAuthor(){
        String searchContent = "周";
        List<Author> l = searchDao.getAuthorBySearch(searchContent);
        for(Author v : l){
            System.out.println(v.toString());
            System.out.println("————————————————————————————————");
        }
    }
}
