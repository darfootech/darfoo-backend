package com.springapp.mvc;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Music;
import com.darfoo.backend.model.MusicCategory;
import com.darfoo.backend.model.UpdateCheckResponse;
import com.darfoo.backend.utils.ModelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class MusicDaoTests {
    @Autowired
    private MusicDao musicDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    CommonDao commonDao;

    @Test
    public void insertSingleMusic() {
        String musicTitle = "ccccc";
        //String imagekey = System.currentTimeMillis() + ".jpg";
        String authorname = "吉卉";

        //伴奏title可以重名,但是不可能出现authorname和title都一样的情况,也就是一个作者名字对应的伴奏中不会出现重名的情况
        Music queryMusic = musicDao.getMusicByTitleAuthorName(musicTitle, authorname);
        if (queryMusic == null) {
            System.out.println("伴奏名字和作者名字组合不存在，可以进行插入");
        } else {
            System.out.println(queryMusic.toString());
            System.out.println("伴奏名字和作者名字组合已存在，不可以进行插入了，是否需要修改");
            return;
        }

        Music music = new Music();
        List<Author> authorList = commonDao.getAllResource(Author.class);
        if (authorList.size() == 0) {
            System.out.println("无法找到默认作者，不可以创建伴奏");
            return;
        } else {
            System.out.println("可以找到默认作者，可以创建伴奏");
            music.setAuthor(authorList.get(0));
        }

        if (imageDao.getAllImage().size() == 0) {
            System.out.println("无法找到默认图片，不可以创建伴奏");
            return;
        } else {
            System.out.println("可以找到默认图片，可以创建伴奏");
            music.setImage(imageDao.getAllImage().get(0));
        }
        //music.setImage(image);
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
        music.setTitle(musicTitle);
        music.setMusic_key(musicTitle);
        music.setUpdate_timestamp(System.currentTimeMillis());
        music.setAuthorName(authorname);
        int insertStatus = musicDao.insertSingleMusic(music);
        if (insertStatus == -1) {
            System.out.println("插入伴奏失败");
        } else {
            System.out.println("插入伴奏成功，视频id是" + insertStatus);
        }

        musicDao.updateMusicKeyById(insertStatus, musicTitle + "-" + insertStatus);
    }

    @Test
    public void getMusicByMusicId() {
        long start = System.currentTimeMillis();
        Music music = (Music) commonDao.getResourceById(Music.class, 30);
        System.out.println(music.toString(true));
        String authorname = music.getAuthorName();
        if (authorname.equals("")) {
            System.out.println("authorname is empty please fill it");
        } else {
            System.out.println("authorname -> " + music.getAuthorName());
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void getHottestMusics() {
        long start = System.currentTimeMillis();
        List<Music> musics = musicDao.getHottestMusics(5);
        for (Music music : musics) {
            System.out.println(music.toString(true));
            System.out.println("——————————————————————————————————————");
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
        List<Music> musics = musicDao.getMusicsByCategories(categories);
        System.out.println("最终满足的music数量>>>>>>>>>>>>>>>>>>>>>" + musics.size());
        for (Music music : musics) {
            System.out.println(music.toString());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void deleteMusicById() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Music.class, 2)));   //--->DELETE_SUCCESS
//		System.out.println(CRUDEvent.getResponse(musicDao.deleteMusicById(200))); //--->DELETE_NOTFOUND
    }

    /**
     * 更新操作可以参考这个测试
     * *
     */
    @Test
    public void updateMusicById() {
        Integer vid = 10;
        String musicTitle = "呵呵呵";
        String authorName = "仓木麻衣";
        String imageKey = "仓木麻衣.jpg";
        UpdateCheckResponse response = musicDao.updateMusicCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        String musicBeat = "八拍";
        String musicStyle = "戏曲风";
        String musicLetter = "q";
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(musicBeat);
        categoryTitles.add(musicStyle);
        categoryTitles.add(musicLetter.toUpperCase());
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            System.out.println(CRUDEvent.getResponse(musicDao.updateMusic(vid, musicTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis())));
        }
    }

    /**
     * 获取所有的music对象
     * *
     */
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
    public void deleteVideoCascade() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Music.class, 14)));
    }

    /**
     * 更新点击量
     * *
     */
    @Test
    public void updateMusicHottest() {
        Integer id = 1;
        int n = 1;
        //int n = -5;
        System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Music.class, id, n)));
    }

    /**
     * 按照热度排序从大到小，获取前number个video
     * *
     */
    @Test
    public void getMusicsByHottest() {
        int number = 20;
        List<Music> musics = musicDao.getMusicsByHottest(number);
        System.out.println("---------返回" + musics.size() + "个视频---------");
        for (Music v : musics) {
            System.out.println("热度值---->" + v.getHottest());
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    /**
     * 获取最新的number个音频
     * *
     */
    @Test
    public void getMusicsByNewest() {
        int number = 20;
        List<Music> musics = musicDao.getMusicsByNewest(number);
        System.out.println("---------返回" + musics.size() + "个视频---------");
        for (Music v : musics) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    /**
     * 更新authorName(Music)
     * *
     */
    @Test
    public void updateAuthorName() {
        //Integer id = 10;  //UPDATE_MUSIC_NOTFOUND
        Integer id = 30;
        String newAuthorName = "吉卉";
        System.out.println(CRUDEvent.getResponse(musicDao.updateAuthorName(id, newAuthorName)));
    }

    @Test
    public void getMusicsByCategoriesByPage() {
        long start = System.currentTimeMillis();
        String[] categories = {};//无条件限制
        //String[] categories = {"四拍","情歌风","D"}; //满足所有条件
        //String[] categories = {"四拍"}; //满足个别条件
        //String[] categories = {"四拍","情歌风","0"};//最后一个条件不满足
        List<Music> musics = musicDao.getMusicsByCategoriesByPage(categories, 1);
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
        int pagecount = (int) musicDao.getPageCountByCategories(categories);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagecount; i++) {
            List<Music> musics = musicDao.getMusicsByCategoriesByPage(categories, i + 1);
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
