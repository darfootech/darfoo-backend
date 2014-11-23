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
import com.darfoo.backend.model.Video;
import com.darfoo.backend.model.VideoCategory;

@Component
public class VideoDao {
	@Autowired
	private SessionFactory sf;
	
	//插入所有video(视频)的类型
	public void insertAllVideoCategories(){
		String[] categories = {"较快","适中","较慢",    //按速度
				"简单","普通","稍难",					//按难度  (将"适中"改为"普通"，否则会出现unique的错误org.hibernate.exception.ConstraintViolationException， com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException)
				"欢快","活泼","优美","情歌风","红歌风","草原风","戏曲风","印巴风","江南风","民歌风","儿歌风",  //按风格
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		try{
			Session session = sf.getCurrentSession();
			for(String category : categories){
				VideoCategory vCategory = new VideoCategory();
				vCategory.setTitle(category);
				vCategory.setDescription("待定");
				session.save(vCategory);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//插入单个视频
	@SuppressWarnings("unchecked")
	public void inserSingleVideo(Video video){
		Set<VideoCategory> vCategories = video.getCategories();
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
			for(VideoCategory vCategory : vCategories){
				s_title.add(vCategory.getTitle());
			}
			c = session.createCriteria(VideoCategory.class).add(Restrictions.in("title", s_title));
			List<VideoCategory> l_vCategory = c.list();   
			vCategories = new HashSet<VideoCategory>(l_vCategory);
			video.setCategories(vCategories);
			session.save(video);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取单个video的信息
	 * 根据video的id来获得video对象
	 * @return video 返回一个video的实例对象(包含关联表中的数据)，详细请看Video.java类
	 * **/
	public Video getVideoByVideoId(Integer vid){
		Video video = null;
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Video.class);
			c.setReadOnly(true);
			c.add(Restrictions.eq("id", vid));
			//设置JOIN mode，这样categories会直接加载到video类中
			c.setFetchMode("categories", FetchMode.JOIN);  
			video = (Video)c.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return video;
	}
	/**
	 * 获取首页推荐视频信息
	 * @param number 推荐视频数量(暂时定为3个,选择video_id位于中间的3个视频作为推荐视频)
	 * @return List<Video> l_video 返回一个包含多个video对象的List
	 * @return video中的categories可以获取
	 **/
	public List<Video> getRecommendVideos(int number){
		number = 3;
		List<Video> l_video = new ArrayList<Video>();
		try{
			Session session = sf.getCurrentSession();
			//投影查询获得所有video的id
			Criteria c = session.createCriteria(Video.class).setProjection(Projections.property("id"));
			c.setReadOnly(true);
			List<Integer> l_vid = c.list();
			int count = l_vid.size();
			if(count >= number){	//要求库里至少有3部视频
				Set<Integer> s_vid = new HashSet<Integer>();
				s_vid.add(l_vid.get(count/2));
				s_vid.add(l_vid.get(count/2-1));
				s_vid.add(l_vid.get(count/2+1));
				c = session.createCriteria(Video.class).add(Restrictions.in("id", s_vid));
				c.setReadOnly(true);
				//c.setFetchMode("categories", FetchMode.JOIN);//同时加载目录(会导致查询结果不值number个，而是number*4个)
				l_video = c.list(); 
				for(Video v : l_video){
					v.trigLazyLoad();   //强制触发延迟加载,避免Session关闭后再加载出现错误
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_video;
	}
	/**
	 * 获取首页最新视频 信息(暂时定5个)
	 * @return List<Video> l_video 返回一个包含多个video对象的List
	 * @return video中的categories可以获取
	 * **/
	public List<Video>  getLastestVideos(int number){
		number = 5;
		List<Video> l_video = new ArrayList<Video>();
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Video.class).setProjection(Projections.property("update_timestamp")).addOrder(Order.desc("update_timestamp"));
			c.setReadOnly(true);
			c.setMaxResults(number);
			List<Long> l_timestamp = c.list();
			if(l_timestamp.size() > 0){
				c = session.createCriteria(Video.class).add(Restrictions.in("update_timestamp", l_timestamp)).addOrder(Order.desc("update_timestamp"));
				c.setReadOnly(true);
				//c.setFetchMode("categories", FetchMode.JOIN);  
				l_video = c.list();
				for(Video v : l_video){
					v.trigLazyLoad();   //强制触发延迟加载,避免Session关闭后再加载出现错误
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_video;
	}
	
	/**
	 * 根据类别获取视频列表(我要上网—视频页面)
	 * $categories  (较快-简单—欢快-A) 如果用户没有选择某个类别，那么就去掉该字符串
	 * @param 例如 categories = {"较快","简单","欢快","A"}  例如 categories = {"较快","欢快","A"} 表示有一个类别用户没有选择
	 * **/
	public List<Video> getVideosByCategories(String[] categories){
		List<Video> l_video = new ArrayList<Video>();
		try{
			Session session = sf.getCurrentSession();
			List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
			Criteria c;
			for(int i=0;i<categories.length;i++){
				c = session.createCriteria(Video.class).setProjection(Projections.property("id"));
				c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
				c.setReadOnly(true);
				List<Integer> l_id = c.list();	
				System.out.println("满足条件 "+categories[i]+" 的video数量》》》"+l_id.size() );
				for(Integer j : l_id){
					System.out.print(j+"#");					
				}
				System.out.println();
				if(l_id.size() == 0){
					//只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
					l_video = new ArrayList<Video>();
					l_interact_id.clear();//清空，表示无交集
					break;
				}else{
					if(l_interact_id.size() == 0){
						l_interact_id = l_id;
						continue;
					}else{
						l_interact_id.retainAll(l_id);
						boolean hasItersection = l_interact_id.size()>0?true:false;
						if(!hasItersection){
							//之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Video>对象,长度为0
							l_video = new ArrayList<Video>();
							break;
						}
					}
				}
			}	
			if(categories.length==0){
				//categories长度为0，即没有筛选条件,返回所有视频
				c = session.createCriteria(Video.class);
				c.setReadOnly(true);
				l_video = c.list();
			}else if(l_interact_id.size() > 0){
				//交集内的id数量大于0个
				c = session.createCriteria(Video.class).add(Restrictions.in("id", l_interact_id));
				c.setReadOnly(true);
				l_video = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_video;
	}
}
