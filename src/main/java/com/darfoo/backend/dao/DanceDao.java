package com.darfoo.backend.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darfoo.backend.model.DanceGroup;
import com.darfoo.backend.model.DanceGroupImage;

@Component
public class DanceDao {
	@Autowired
	private SessionFactory sf;
	
	public void insertSingleDanceGroup(DanceGroup group){
		DanceGroupImage image = group.getImage();
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(DanceGroupImage.class).add(Restrictions.eq("image_key", image.getImage_key()));
			c.setReadOnly(true);
			List<DanceGroupImage> l_image = c.list();
			if(l_image.size() > 0){
				//图片库中包含此图片信息，用持久化对象代替原来的image
				group.setImage(l_image.get(0));
			}
			session.save(group);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取舞队信息
	 * 默认返回全部
	 * **/
	public List<DanceGroup> getDanceGroups(){
		List<DanceGroup> l_dance = new ArrayList<DanceGroup>();;
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(DanceGroup.class);
			c.setReadOnly(true);
			l_dance = c.list();
		}catch(Exception e){			
			e.printStackTrace();
		}
		return l_dance;
	}
}
