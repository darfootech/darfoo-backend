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
		author.setName("哈哈哈");
		author.setDescription("台湾人气偶像组合");
		int res = authorDao.insertAuthor(author);
		System.out.println(CRUDEvent.getResponse(res));
	}
	
	@Test
	public void updateAuthor(){
		Author author = new Author();
		author.setName("五月天");
		author.setDescription("菜狗组合");
		String oldName = "五月天";
		int res = authorDao.updateAuthor(author, oldName);
		System.out.println(CRUDEvent.getResponse(res));
	}
}
