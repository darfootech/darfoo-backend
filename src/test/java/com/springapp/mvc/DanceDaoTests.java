package com.springapp.mvc;

import java.util.List;
import java.util.Set;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.DanceGroupImageDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.DanceDao;
import com.darfoo.backend.model.DanceGroup;
import com.darfoo.backend.model.DanceGroupImage;
import com.darfoo.backend.model.UpdateCheckResponse;

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
		System.out.println(CRUDEvent.getResponse(danceDao.deleteDanceGroupById(5)));
	}
	/**
	 * 更新舞队
	 * **/
	@Test
	public void updateDanceGroup(){
		int id = 5;
		//String imageKey = "dg2";
		String imageKey = null;
		UpdateCheckResponse response = danceDao.updateDanceGroupCheck(id, imageKey);
		String name = "年轻妈妈";
		String description = "青春无极限";
		if(response.updateIsReady()){
			System.out.println(CRUDEvent.getResponse(danceDao.updateDanceGourp(id, name, description, imageKey,System.currentTimeMillis())));
		}else{
			System.out.println("请先完成舞队图片的插入");
		}
	}
	
	/**
	 * 得到所有舞队
	 * **/
	@Test
	public void getAllDanceGroup(){
		List<DanceGroup> s_groups = danceDao.getAllDanceGourp();
		for(DanceGroup group : s_groups){
            System.out.println("id: " + group.getId());
			System.out.println(group.getName()+"  "+group.getDescription()+" "+group.getImage().getImage_key());
		}
	}
	
	/**
	 * 得到所有舞队图片
	 * **/
	@Test
	public void getAllDanceGourpImage(){
		List<DanceGroupImage> l_image = danceGroupImageDao.getAllImage();
		for(DanceGroupImage image : l_image){
			System.out.println(image.getId()+"  "+image.getImage_key());
		}
		
	}

    @Test
    public void deleteTeamById(){
        System.out.println(CRUDEvent.getResponse(danceDao.deleteDanceGroupById(14)));
    }

    @Test
    public void getTeamById(){
        int id = 3;
        DanceGroup a = danceDao.getTeamById(id);
        if(a != null)
            System.out.println(a.getName());
        else
            System.out.println("无该team记录");

    }
}
