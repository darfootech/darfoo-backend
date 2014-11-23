package com.springapp.mvc;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.DanceDao;
import com.darfoo.backend.model.DanceGroup;
import com.darfoo.backend.model.DanceGroupImage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DanceDaoTests {
	@Autowired
	private DanceDao danceDao;
	
	@Test
	public void insertSingleDanceGroup(){
		DanceGroup group = new DanceGroup();
		group.setName("四号");
		group.setDescription("小妈队");
		group.setUpdate_timestamp(System.currentTimeMillis());
		DanceGroupImage image = new DanceGroupImage();
		image.setImage_key("dg1");
		group.setImage(image);
		danceDao.insertSingleDanceGroup(group);
	}
	
	@Test
	public void getDanceGroups(){
		long start = System.currentTimeMillis();
		List<DanceGroup> l_group = danceDao.getDanceGroups();
		for(DanceGroup group : l_group){
			System.out.println(group.toString());
			System.out.println("————————————————————————————————————");
		}
		System.out.println((System.currentTimeMillis()-start)/1000f);
	}
}
