package com.springapp.mvc.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.utils.ModelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class MusicDaoTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CategoryDao categoryDao;

    @Test
    public void insertMusicResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String musicTitle = "musictitle-" + System.currentTimeMillis();
        String authorName = "吉卉";

        insertcontents.put("title", musicTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("category1", "八拍");
        insertcontents.put("category2", "情歌风");
        insertcontents.put("category4", "D");

        HashMap<String, Integer> insertresult = commonDao.insertResource(Music.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void getMusicById() {
        long start = System.currentTimeMillis();
        Music music = (Music) commonDao.getResourceById(Music.class, 30);
        System.out.println(music.toString(true));
        String authorname = music.getAuthorname();
        if (authorname.equals("")) {
            System.out.println("authorname is empty please fill it");
        } else {
            System.out.println("authorname -> " + music.getAuthorname());
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void getMusicsByCategories() {
        long start = System.currentTimeMillis();
        //String[] categories = {};//无条件限制
        String[] categories = {"四拍", "情歌风", "D"}; //满足所有条件
        //String[] categories = {"四拍"}; //满足个别条件
        //String[] categories = {"四拍","情歌风","0"};//最后一个条件不满足
        List<Music> musics = categoryDao.getResourcesByCategories(Music.class, categories);
        System.out.println("最终满足的music数量 -> " + musics.size());
        for (Music music : musics) {
            System.out.println(music.toString());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void deleteMusicById() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Music.class, 2)));   //--->DELETE_SUCCESS
    }

    @Test
    public void updateMusicById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String musicTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "仓木麻衣";

        Integer id = 39;

        updatecontents.put("title", musicTitle);
        updatecontents.put("authorname", authorName);
        updatecontents.put("category1", "八拍");
        updatecontents.put("category2", "戏曲风");
        updatecontents.put("category3", "q".toUpperCase());

        HashMap<String, Integer> insertresult = commonDao.updateResource(Music.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void getAllMusics() {
        List<Music> s_musics = commonDao.getAllResource(Music.class);
        System.out.println("总共查到" + s_musics.size());
        for (Music video : s_musics) {
            System.out.println("----------------");
            System.out.println("id: " + video.getId());
            System.out.println(video.toString(true));

        }
    }

    @Test
    public void updateMusicHottest() {
        Integer id = 1;
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(Music.class, id, "hottest")));
    }

    @Test
    public void getMusicsByHottest() {
        int number = 5;
        List<Music> musics = commonDao.getResourcesByHottest(Music.class, number);
        System.out.println("---------返回" + musics.size() + "个视频---------");
        for (Music v : musics) {
            System.out.println("热度值---->" + v.getHottest());
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getMusicsByNewest() {
        int number = 20;
        List<Music> musics = commonDao.getResourcesByNewest(Music.class, number);
        System.out.println("---------返回" + musics.size() + "个视频---------");
        for (Music v : musics) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getMusicsByCategoriesByPage() {
        long start = System.currentTimeMillis();
        String[] categories = {};//无条件限制
        //String[] categories = {"四拍","情歌风","D"}; //满足所有条件
        //String[] categories = {"四拍"}; //满足个别条件
        //String[] categories = {"四拍","情歌风","0"};//最后一个条件不满足
        List<Music> musics = paginationDao.getResourcesByCategoriesByPage(Music.class, categories, 1);
        for (Music music : musics) {
            System.out.println(music.getId());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("最终满足的music数量>>>>>>>>>>>>>>>>>>>>>" + musics.size());
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void isDuplicateWithPageQuery() {
        String[] categories = {};//无条件限制
        int pagecount = (int) paginationDao.getResourcePageCountByCategories(Music.class, categories);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagecount; i++) {
            List<Music> musics = paginationDao.getResourcesByCategoriesByPage(Music.class, categories, i + 1);
            for (Music music : musics) {
                System.out.println(music.getId());
                idSet.add(music.getId());
                System.out.println("——————————————————————————————————————");
            }
            System.out.println("最终满足的music数量>>>>>>>>>>>>>>>>>>>>>" + musics.size());
        }

        System.out.println("music count -> " + commonDao.getAllResource(Music.class).size());
        System.out.println("music count -> " + idSet.size());
    }

    @Test
    public void getAllMusicsWithoutId() {
        int vid = 1;
        List<Music> allmusics = commonDao.getAllResourceWithoutId(Music.class, vid);
        for (Music music : allmusics) {
            System.out.println(music.getId());
        }
    }

    @Test
    public void getSideBarMusics() {
        List<Music> result = commonDao.getSideBarResources(Music.class, 27);
        System.out.println(result.size());
        for (Music music : result) {
            System.out.println(music.getTitle() + "-" + music.getId());
        }
    }
}
