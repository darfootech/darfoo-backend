package com.darfoo.backend.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.Video;

@Component
public class SearchDao {
	@Autowired
	private SessionFactory sf;
	/**
	 * @param searchContent 搜索参数
	 * 搜索video  匹配Video的title字段
	 * 最多返回50个结果
	 * **/
	public List<Video> getVideoBySearch(String searchContent){
		List<Video> l_videos = new ArrayList<Video>();
		try{
			char[] s = searchContent.toCharArray();
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<s.length;i++){
				sb.append("%");
				sb.append(s[i]);
			}
			sb.append("%");
			System.out.println("匹配>>>>"+sb.toString());
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Video.class).setProjection(Projections.property("id"));
			c.add(Restrictions.like("title", sb.toString(), MatchMode.ANYWHERE));
			List<Integer> l_id = c.list();
			if(l_id.size() > 0){
				c = session.createCriteria(Video.class).add(Restrictions.in("id", l_id));
				c.setMaxResults(50);
				c.setReadOnly(true);
				l_videos = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_videos;
	}
	/**
	 * @param searchContent 搜索参数
	 * 搜索Education Video  匹配Video的title字段
	 * 最多返回50个结果
	 * **/
	public List<Education> getEducationBySearch(String searchContent){
		List<Education> l_videos = new ArrayList<Education>();
		try{
			char[] s = searchContent.toCharArray();
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<s.length;i++){
				sb.append("%");
				sb.append(s[i]);
			}
			sb.append("%");
			System.out.println("匹配>>>>"+sb.toString());
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Education.class).setProjection(Projections.property("id"));
			c.add(Restrictions.like("title", sb.toString(), MatchMode.ANYWHERE));
			List<Integer> l_id = c.list();
			if(l_id.size() > 0){
				c = session.createCriteria(Education.class).add(Restrictions.in("id", l_id));
				c.setMaxResults(50);
				c.setReadOnly(true);
				l_videos = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_videos;
	}
	/**
	 * @param searchContent 搜索参数
	 * 搜索music  匹配Music的title字段
	 * 最多返回50个结果
	 * **/
	public List<Music> getMusicBySearch(String searchContent){
		List<Music> l_videos = new ArrayList<Music>();
		try{
			char[] s = searchContent.toCharArray();
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<s.length;i++){
				sb.append("%");
				sb.append(s[i]);
			}
			sb.append("%");
			System.out.println("匹配>>>>"+sb.toString());
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Music.class).setProjection(Projections.property("id"));
			c.add(Restrictions.like("title", sb.toString(), MatchMode.ANYWHERE));
			List<Integer> l_id = c.list();
			if(l_id.size() > 0){
				c = session.createCriteria(Music.class).add(Restrictions.in("id", l_id));
				c.setMaxResults(50);
				c.setReadOnly(true);
				l_videos = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_videos;
	}
}
