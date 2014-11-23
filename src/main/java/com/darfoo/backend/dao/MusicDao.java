package com.darfoo.backend.dao;

import java.io.Serializable;
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
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.VideoCategory;

@Component
public class MusicDao {
	@Autowired
	private SessionFactory sf;
	
	//插入所有music(categories)的类型
	public void insertAllMusicCategories(){
		String[] categories = {"四拍","八拍","十六拍","三十二拍",    //按节拍  
				"情歌风","红歌风","草原风","戏曲风","印巴风","江南风","民歌风","儿歌风",  //按风格
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};//按字母
		try{
			Session session = sf.getCurrentSession();
			for(String category : categories){
				MusicCategory mCategory = new MusicCategory();
				mCategory.setTitle(category);
				mCategory.setDescription("待定");
				session.save(mCategory);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//插入单个音频
	@SuppressWarnings("unchecked")
	public void inserSingleMusic(Music music){
		Set<MusicCategory> mCategories = music.getCategories();
		Author author = music.getAuthor();
		Image image = music.getImage();
		try{
			Session session = sf.getCurrentSession();
			//先查询image表中是否包含此music的图片信息
			Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", image.getImage_key()));//image的image_key字段需为unique
			List<Image> l_img = c.list();
			if(l_img.size() > 0){
				//image表包含该video的图片信息,用持久化的实体代替原来的值
				music.setImage(l_img.get(0)); 
			}
			//查询author表中是否包含此music的作者信息
			c = session.createCriteria(Author.class).add(Restrictions.eq("name", author.getName())); //author的name字段需为unique
			List<Author> l_author = c.list();
			if(l_author.size() > 0){
				//author表包含该video的作者信息,用持久化的实体代替原来的值
				music.setAuthor(l_author.get(0));
			}
			//对于MusicCategory，插入时默认认为全都属于MusicCategory表，所以只需找到对应种类的实体即可
			Set<String> s_title = new HashSet<String>();  //videoCategory的title字段为unique
			for(MusicCategory mCategory : mCategories){
				s_title.add(mCategory.getTitle());
			}
			c = session.createCriteria(MusicCategory.class).add(Restrictions.in("title", s_title));
			List<MusicCategory> l_mCategory = c.list();   
			mCategories = new HashSet<MusicCategory>(l_mCategory);
			music.setCategories(mCategories);
			session.save(music);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取单个music的信息
	 * 根据music的id来获得music对象
	 * @return music 返回一个Music的实例对象(包含关联表中的数据)，详细请看Music.java类
	 * **/
	public Music getMusicByMusicId(Integer vid){
		Music music = null;
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Music.class);
			c.setReadOnly(true);
			c.add(Restrictions.eq("id", vid));
			//设置JOIN mode，这样categories会直接加载到music类中
			c.setFetchMode("categories", FetchMode.JOIN);  
			music = (Music)c.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return music;
	}
	/**
	 * 获取热门歌曲排行榜(暂时不排)
	 * @param number 推荐歌曲数量(暂时定为3个)
	 * @return List<Music> l_music 返回一个包含多个music对象的List
	 **/
	public List<Music> getHotestMusics(int number){
		number = 3;
		List<Music> l_music = new ArrayList<Music>();
		try{
			Session session = sf.getCurrentSession();
			//投影查询获得所有music的id
			Criteria c = session.createCriteria(Music.class).setProjection(Projections.property("id"));
			c.setReadOnly(true);
			List<Integer> l_mid = c.list();
			int count = l_mid.size();
			System.out.println("count>>>>"+count);
			if(count >= number){	//要求库里至少有3端歌曲
				Set<Integer> s_vid = new HashSet<Integer>();
				s_vid.add(l_mid.get(count/2));
				s_vid.add(l_mid.get(count/2-1));
				s_vid.add(l_mid.get(count/2+1));
				c = session.createCriteria(Music.class).add(Restrictions.in("id", s_vid));
				c.setReadOnly(true);
				//c.setFetchMode("categories", FetchMode.JOIN);//同时加载目录(会导致查询结果不值number个，而是number*4个)
				l_music = c.list(); 
				for(Music m : l_music){
					m.trigLazyLoad();   //强制触发延迟加载,避免Session关闭后再加载出现错误
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_music;
	}
	
	/**
	 * 根据类别获取歌曲列表(我要上网—音乐页面中)
	 * $categories  (四拍-情歌风-A) 如果用户没有选择某个类别，那么就去掉该字符串
	 * @param 例如 categories = {"四拍","情歌风","A"}  例如 categories = {"四拍","A"} 表示有一个类别用户没有选择
	 * **/
	public List<Music> getMusicsByCategories(String[] categories){
		List<Music> l_music = new ArrayList<Music>();
		try{
			Session session = sf.getCurrentSession();
			List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的music id
			Criteria c;
			for(int i=0;i<categories.length;i++){
				c = session.createCriteria(Music.class).setProjection(Projections.property("id"));
				c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
				c.setReadOnly(true);
				List<Integer> l_id = c.list();	
				System.out.println("满足条件 "+categories[i]+" 的music数量》》》"+l_id.size() );
				for(Integer j : l_id){
					System.out.print(j+"#");					
				}
				System.out.println();
				if(l_id.size() == 0){
					//只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Music>对象,长度为0
					l_music = new ArrayList<Music>();
					l_interact_id.clear();//清空
					break;
				}else{
					if(l_interact_id.size()==0){
						l_interact_id = l_id;
						continue;
					}else{
						l_interact_id.retainAll(l_id);
						boolean hasItersection = l_interact_id.size()>0?true:false;
						if(!hasItersection){
							//之前查询的结果与当前的无交集，说明歌曲表无法满足该种类组合，返回一个空的List<Music>对象,长度为0
							l_music = new ArrayList<Music>();
							break;
						}
					}
				}
			}	
			if(categories.length==0){
				//categories长度为0，即没有筛选条件,返回所有视频
				c = session.createCriteria(Music.class);
				c.setReadOnly(true);
				l_music = c.list();
			}else if(l_interact_id.size() > 0){
				//交集内的id数量大于0个
				c = session.createCriteria(Music.class).add(Restrictions.in("id", l_interact_id));
				c.setReadOnly(true);
				l_music = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_music;
	}
	
}
