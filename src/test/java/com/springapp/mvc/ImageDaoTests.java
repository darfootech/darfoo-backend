package com.springapp.mvc;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.ImageDao;
import com.darfoo.backend.model.Image;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class ImageDaoTests {
	
	@Autowired
	ImageDao imageDao;
	
	@Test
	public void insertImage(){
		Image image = new Image();
		image.setImage_key("CCCC");
		imageDao.inserSingleImage(image);
	}
	
	@Test
	public void getAllImage(){
		List<Image> l_image = imageDao.getAllImage();
		for(Image image : l_image){
			System.out.println(image.getId()+" "+image.getImage_key());
		}
	}
}
