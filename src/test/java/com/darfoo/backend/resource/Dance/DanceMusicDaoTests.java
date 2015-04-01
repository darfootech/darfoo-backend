package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.resource.InsertDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
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
public class DanceMusicDaoTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    InsertDao insertDao;

    @Test
    public void insertMusicResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String musicTitle = "musictitle-" + System.currentTimeMillis();
        String authorName = "吉卉";

        insertcontents.put("title", musicTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("category", "D");

        HashMap<String, Integer> insertresult = commonDao.insertResource(DanceMusic.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void updateMusicById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String musicTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "仓木麻衣";

        Integer id = 430;

        updatecontents.put("title", musicTitle);
        updatecontents.put("authorname", authorName);
        updatecontents.put("category", "q".toUpperCase());

        HashMap<String, Integer> insertresult = commonDao.updateResource(DanceMusic.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void getMusicById() {
        long start = System.currentTimeMillis();
        DanceMusic music = (DanceMusic) commonDao.getResourceById(DanceMusic.class, 30);
        System.out.println(music.toString());
        String authorname = music.getAuthorname();
        if (authorname.equals("")) {
            System.out.println("authorname is empty please fill it");
        } else {
            System.out.println("authorname -> " + music.getAuthorname());
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void getMusicsByCategory() {
        String letterCategory = "A";
        List<DanceMusic> musics = categoryDao.getResourcesByCategory(DanceMusic.class, letterCategory);
        for (DanceMusic music : musics) {
            System.out.println(music.toString());
            System.out.println("——————————————————————————————————————");
        }
    }

    @Test
    public void deleteMusicById() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(DanceMusic.class, 2)));   //--->DELETE_SUCCESS
    }

    @Test
    public void getAllMusics() {
        List<DanceMusic> s_musics = commonDao.getAllResource(DanceMusic.class);
        Set<Integer> musicids = new HashSet<Integer>();
        for (DanceMusic music : s_musics) {
            System.out.println("----------------");
            System.out.println("id: " + music.getId());
            musicids.add(music.getId());
            System.out.println(music.toString());
        }
        System.out.println("总共查到" + s_musics.size());
        System.out.println("distinct music id -> " + musicids.size());
    }

    @Test
    public void updateMusicHottest() {
        Integer id = 1;
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(DanceMusic.class, id, "hottest")));
    }

    @Test
    public void getMusicsByHottest() {
        int number = 10;
        List<DanceMusic> musics = commonDao.getResourcesByHottest(DanceMusic.class, number);
        for (DanceMusic v : musics) {
            System.out.println("热度值---->" + v.getHottest());
            System.out.println(v.toString());
            System.out.println("---------------------------");
        }
        System.out.println("---------返回" + musics.size() + "个视频---------");
    }

    @Test
    public void getMusicsByNewest() {
        int number = 20;
        List<DanceMusic> musics = commonDao.getResourcesByNewest(DanceMusic.class, number);
        System.out.println("---------返回" + musics.size() + "个视频---------");
        for (DanceMusic v : musics) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.toString());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getAllMusicsWithoutId() {
        int vid = 1;
        List<DanceMusic> allmusics = commonDao.getAllResourceWithoutId(DanceMusic.class, vid);
        for (DanceMusic music : allmusics) {
            System.out.println(music.getId());
        }
    }

    @Test
    public void getSideBarMusics() {
        List<DanceMusic> result = commonDao.getSideBarResources(DanceMusic.class, 2);
        System.out.println(result.size());
        for (DanceMusic music : result) {
            System.out.println(music.getTitle() + "-" + music.getId());
        }
    }
}
