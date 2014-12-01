package com.springapp.mvc;

import java.util.List;

import com.darfoo.backend.dao.DanceGroupImageDao;
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
    @Autowired
    DanceGroupImageDao danceGroupImageDao;
	
	@Test
	public void insertSingleDanceGroup(){
        String groupName = "四号3";
        String imagekey = "dg111";

        boolean isGroupExists = danceDao.isDanceGroupExists(groupName);
        if (isGroupExists){
            System.out.println("舞队已存在");
            return;
        }else{
            System.out.println("舞队不存在，可以新建舞队");
        }

        DanceGroupImage image = danceGroupImageDao.getImageByName(imagekey);
        if (image == null){
            System.out.println("图片不存在，可以进行插入");
            image = new DanceGroupImage();
            image.setImage_key(imagekey);
            danceGroupImageDao.inserSingleImage(image);
        }else{
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

		DanceGroup group = new DanceGroup();
		group.setName(groupName);
		group.setDescription("小妈队");
		group.setUpdate_timestamp(System.currentTimeMillis());
		group.setImage(image);
		danceDao.insertSingleDanceGroup(group);
	}
	
	@Test
	public void getDanceGroups(){
		long start = System.currentTimeMillis();
		List<DanceGroup> l_group = danceDao.getDanceGroups(7);//选2个舞队
		for(DanceGroup group : l_group){
			System.out.println(group.toString());
			System.out.println("————————————————————————————————————");
		}
		System.out.println((System.currentTimeMillis()-start)/1000f);
	}
	
	@Test
	public void deleteDanceGroupById(){
		System.out.println(danceDao.deleteDanceGroupById(5)>0?"delete success":"delete fail");
	}
}
