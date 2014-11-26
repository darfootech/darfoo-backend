package com.springapp.mvc;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.model.Music;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class MusicDaoTests {
	@Autowired
	private MusicDao musicDao;
	
	@Test
	public void insertAllMusicCategories(){
		musicDao.insertAllMusicCategories();
	}
	
	@Test
	public void inserSingleMusic(){
		Music music = new Music();
		Author a1 = new Author();
		a1.setName("T-ara");
		a1.setDescription("韩国女团组合");
		music.setAuthor(a1);
		Image img = new Image();
		img.setImage_key("T-ara.jpg");
		music.setImage(img);
		MusicCategory c1 = new MusicCategory();	
		MusicCategory c2 = new MusicCategory();	
		MusicCategory c3 = new MusicCategory();	
		c1.setTitle("八拍");
		c2.setTitle("情歌风");
		c3.setTitle("S");
		Set<MusicCategory> s_mCategory = music.getCategories();
		s_mCategory.add(c1);
		s_mCategory.add(c2);
		s_mCategory.add(c3);
		music.setTitle("Sexy Love");
		music.setMusic_key("SexyLove");
		music.setUpdate_timestamp(System.currentTimeMillis());
		musicDao.inserSingleMusic(music);
	}
	
	@Test
	public void getMusicByMusicId(){
		long start = System.currentTimeMillis();
		Music music = musicDao.getMusicByMusicId(3);
		System.out.println(music.toString(true));
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);
	}
	
	@Test
	public void getHottestMusics(){
		long start = System.currentTimeMillis();
		List<Music> musics = musicDao.getHottestMusics(5);
		for(Music music : musics){
			System.out.println(music.toString(true));
			System.out.println("——————————————————————————————————————");
		}
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);	
	}
	
	@Test
	public void getMusicsByCategories(){
		long start = System.currentTimeMillis();
		//String[] categories = {};//无条件限制
		String[] categories = {"四拍","情歌风","D"}; //满足所有条件
		//String[] categories = {"四拍"}; //满足个别条件
		//String[] categories = {"四拍","情歌风","0"};//最后一个条件不满足
		List<Music> musics = musicDao.getMusicsByCategories(categories);
		System.out.println("最终满足的music数量>>>>>>>>>>>>>>>>>>>>>"+musics.size());
		for(Music music : musics){
			System.out.println(music.toString());
			System.out.println("——————————————————————————————————————");
		}
		System.out.println("time elapse:"+(System.currentTimeMillis()-start)/1000f);	
	}
	
}
