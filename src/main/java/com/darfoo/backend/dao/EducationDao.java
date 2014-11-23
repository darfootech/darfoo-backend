package com.darfoo.backend.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.EducationCategory;

@Component
public class EducationDao {
	@Autowired
	private SessionFactory sf;
	
	//插入所有education(视频)的类型  (暂时将名师教学这个选项去掉)
	public void insertAllEducationCategories(){
		String[] categories = {"快","中","慢",    //按速度
				"简单","适中","稍难",					//按难度  
				"队形表演","背面教学","分解教学"};  //按教学类型

		try{
			Session session = sf.getCurrentSession();
			for(String category : categories){
				EducationCategory eCategory = new EducationCategory();
				eCategory.setTitle(category);
				eCategory.setDescription("待定");
				session.save(eCategory);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//插入单个education视频
	@SuppressWarnings("unchecked")
	public void inserSingleEducationVideo(Education video){
		Set<EducationCategory> eCategories = video.getCategories();
		Author author = video.getAuthor();
		Image image = video.getImage();
		try{
			Session session = sf.getCurrentSession();
			//先查询image表中是否包含此video的图片信息
			Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", image.getImage_key()));//image的image_key字段需为unique
			List<Image> l_img = c.list();
			if(l_img.size() > 0){
				//image表包含该video的图片信息,用持久化的实体代替原来的值
				video.setImage(l_img.get(0)); 
			}
			//查询author表中是否包含此video的作者信息
			c = session.createCriteria(Author.class).add(Restrictions.eq("name", author.getName())); //author的name字段需为unique
			List<Author> l_author = c.list();
			if(l_author.size() > 0){
				//author表包含该video的作者信息,用持久化的实体代替原来的值
				video.setAuthor(l_author.get(0));
			}
			//对于VideoCategory，插入时默认认为全都属于VideoCategory表，所以只需找到对应种类的实体即可
			Set<String> s_title = new HashSet<String>();  //videoCategory的title字段为unique
			for(EducationCategory eCategory : eCategories){
				s_title.add(eCategory.getTitle());
			}
			c = session.createCriteria(EducationCategory.class).add(Restrictions.in("title", s_title));
			List<EducationCategory> l_vCategory = c.list();   
			eCategories = new HashSet<EducationCategory>(l_vCategory);
			video.setCategories(eCategories);
			session.saveOrUpdate(video);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取单个enducation video的信息
	 * 根据video的id来获得video对象
	 * @return video 返回一个video的实例对象(包含关联表中的数据),详细看Education类
	 * **/
	public Education getEducationVideoById(Integer vid){
		Education video = null;
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Education.class);
			c.setReadOnly(true);
			c.add(Restrictions.eq("id", vid));
			//设置JOIN mode，这样categories会直接加载到video类中
			c.setFetchMode("categories", FetchMode.JOIN);  
			video = (Education)c.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return video;
	}
	/**
	 * 根据类别获取视频列表(我要上网—视频页面) 暂时去掉名师那个种类
	 * $categories  (快-简单—队形表演) 如果用户没有选择某个类别，那么就去掉该字符串
	 * @param 例如 categories = {"快","简单","队形表演"}  例如 categories = {"快","简单"} 
	 * **/
	public List<Education> getEducationVideosByCategories(String[] categories){
		List<Education> l_video = new ArrayList<Education>();
		try{
			Session session = sf.getCurrentSession();
			List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
			Criteria c;
			for(int i=0;i<categories.length;i++){
				c = session.createCriteria(Education.class).setProjection(Projections.property("id"));
				c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
				c.setReadOnly(true);
				List<Integer> l_id = c.list();	
				System.out.println("满足条件 "+categories[i]+" 的enducation video数量》》》"+l_id.size() );
				for(Integer j : l_id){
					System.out.print(j+"#");					
				}
				System.out.println();
				if(l_id.size() == 0){
					//只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
					l_video = new ArrayList<Education>();
					l_interact_id.clear();//清空
					break;
				}else{
					if(l_interact_id.size() == 0){
						l_interact_id = l_id;
						continue;
					}else{
						l_interact_id.retainAll(l_id);
						boolean hasItersection = l_interact_id.size()>0?true:false;
						if(!hasItersection){
							//之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
							l_video = new ArrayList<Education>();
							break;
						}
					}
				}
			}	
			if(categories.length==0){
				//categories长度为0，即没有筛选条件,返回所有视频
				c = session.createCriteria(Education.class);
				c.setReadOnly(true);
				l_video = c.list();
			}else if(l_interact_id.size() > 0){
				//交集内的id数量大于0个
				c = session.createCriteria(Education.class).add(Restrictions.in("id", l_interact_id));
				c.setReadOnly(true);
				l_video = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_video;
	}
}
