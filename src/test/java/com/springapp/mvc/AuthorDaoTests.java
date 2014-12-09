package com.springapp.mvc;

import java.util.Iterator;
import java.util.List;

import com.darfoo.backend.dao.ImageDao;
import com.darfoo.backend.model.Image;
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
    @Autowired
    ImageDao imageDao;
	
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
        String authorName = "四号3";
        String imagekey = "dg111";

        if(authorDao.isExistAuthor(authorName)){
            System.out.println("已存在，不能创建新作者");
            return;
        }else{
            System.out.println("无该author记录，可以创建新作者");
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

		Author author = new Author();
		author.setName(authorName);
		author.setDescription("台湾人气偶像组合");
        author.setImage(image);
		int res = authorDao.insertAuthor(author);
		System.out.println(CRUDEvent.getResponse(res));
	}
	
	/**
	 * 更新作者，先对image进行是否存在的检测，再更新
	 * **/
	@Test
	public void updateAuthor(){
		Integer id = 4;
		String newName = "滨崎步";
		String newDesciption = "日本女歌手";
		String newimageKey = "滨崎步3113.jpg";

        if(authorDao.isExistAuthor(newName)){
            System.out.println("已存在，不能修改作者名字");
            return;
        }else{
            System.out.println("作者名字不存在，可以进行修改");
        }

        Image image = imageDao.getImageByName(newimageKey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(newimageKey);
            imageDao.insertSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

		UpdateCheckResponse response = authorDao.updateAuthorCheck(id, newimageKey);
		if(response.updateIsReady()){
			int res = authorDao.updateAuthor(id, newName, newDesciption, newimageKey);//更新id为2的Author对象的名字
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
		Integer id = 6;
		System.out.println(CRUDEvent.getResponse(authorDao.deleteAuthorById(id)));
	}
}
