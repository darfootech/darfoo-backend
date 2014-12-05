package com.springapp.mvc;

import java.util.Iterator;
import java.util.List;

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
		Integer id = 2;
		String newName = "周杰伦";
		String newDesciption = null;
		int res = authorDao.updateAuthor(id,newName, newDesciption);//更新id为2的Author对象的名字
		System.out.println(CRUDEvent.getResponse(res));
	}
	
	@Test
	public void getAllAuthor(){
		List<Author> l_author = authorDao.getAllAuthor();
		for(Author a : l_author){
            System.out.println("id: " + a.getId());
			System.out.println(a.getName()+"  "+a.getDescription());
		}
	}
}
