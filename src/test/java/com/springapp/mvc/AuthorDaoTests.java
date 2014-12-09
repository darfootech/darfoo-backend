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
import com.darfoo.backend.model.UpdateCheckResponse;

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
	
	/**
	 * 更新作者，先对image进行是否存在的检测，再更新
	 * **/
	@Test
	public void updateAuthor(){
		Integer id = 1;
		String newName = "周杰伦";
		String newDesciption = "asdkabscla";
		String newimageKey = "仓木麻衣.jpg";
		UpdateCheckResponse response = authorDao.updateAuthorCheck(id, newimageKey);
		if(response.updateIsReady()){			
			int res = authorDao.updateAuthor(id,newName, newDesciption,newimageKey);//更新id为2的Author对象的名字
			System.out.println(CRUDEvent.getResponse(res));
		}else{
			System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
		}

	}
	
	@Test
	public void getAllAuthor(){
		List<Author> l_author = authorDao.getAllAuthor();
		for(Author a : l_author){
			System.out.println(a.toString());
		}
	}
	
	@Test
	public void deleteAuthor(){
		Integer id = 1;
		System.out.println(CRUDEvent.getResponse(authorDao.deleteAuthorById(id)));
	}
}
