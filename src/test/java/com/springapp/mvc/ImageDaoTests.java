package com.springapp.mvc;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.ImageDao;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.UpdateCheckResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class ImageDaoTests {
	
	@Autowired
	ImageDao imageDao;
	
	@Test
	public void insertImage(){
		Image image = new Image();
		image.setImage_key("CCCC");
		imageDao.insertSingleImage(image);
	}
	
	@Test
	public void getAllImage(){
		List<Image> l_image = imageDao.getAllImage();
		for(Image image : l_image){
			System.out.println(image.getId()+" "+image.getImage_key());
		}
	}
	
	/**
	 * 根据id更新image的image_key
	 * 先做check
	 * 再做update
	 * **/
	@Test
	public void updateImage(){
		Integer id = 2;
		String newImageKey = "aasa1";
		UpdateCheckResponse response = imageDao.updateImageCheck(id, newImageKey);
		if(response.updateIsReady()){
			System.out.println(CRUDEvent.getResponse(imageDao.updateImage(id, newImageKey)));
		}else{
			System.out.println("没有对应id的image或者插入imagekey重复");
		}
		
	}
	/**
	 * 删除image(将有外键的关系对应的字段设为null)
	 * **/
	@Test
	public void deleteImage(){
		Integer id = 2;
		int res = imageDao.deleteImageById(id);
		System.out.println(CRUDEvent.getResponse(res));
	}
}
