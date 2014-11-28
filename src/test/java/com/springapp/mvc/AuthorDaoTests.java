package com.springapp.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.model.Author;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class AuthorDaoTests {
	@Autowired
	AuthorDao authorDao;
	
	@Test
	public void getAuthorByName(){
		String name = "T-ara";
		Author a = authorDao.getAuthor(name);
		if(a != null)
			System.out.println(a.getName());
		else
			System.out.println("无该author记录");
	}
	
	@Test
	public void isExistAuthor(){
		String name = "T-ara";
		if(authorDao.isExistAuthor(name)){
			System.out.println("已存在");
		}else{
			System.out.println("无该author记录");
		}
	}
	
	@Test
	public void insertAuthor(){
		Author author = new Author();
		author.setName("Fir");
		author.setDescription("飞儿乐团");
		int res = authorDao.insertAuthor(author);
		System.out.println(CRUDEvent.getResponse(res));
	}
}
