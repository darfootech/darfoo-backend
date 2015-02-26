package com.springapp.mvc.resource;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.*;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.resource.*;
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
public class TutorialDaoTests {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    AccompanyDao accompanyDao;

    @Test
    public void insertTutorialResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String tutorialTitle = "tutorialtitle-" + System.currentTimeMillis();
        String authorName = "周杰伦";
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        insertcontents.put("title", tutorialTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("imagekey", imagekey);
        insertcontents.put("category1", "快");
        insertcontents.put("category2", "适中");
        insertcontents.put("category3", "分解教学");
        insertcontents.put("videotype", "mp3");
        insertcontents.put("connectmusic", "ccccc-memeda-33");

        HashMap<String, Integer> insertresult = commonDao.insertResource(Tutorial.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));

    }

    @Test
    public void getTutorialById() {
        Tutorial video = (Tutorial) commonDao.getResourceById(Tutorial.class, 1);
        if (video != null) {
            System.out.println(video.toString(true));
        } else {
            System.out.println("无此ID视频信息");
        }
    }

    @Test
    public void getTutorialsByCategories() {
        long start = System.currentTimeMillis();
        String[] categories = {};//无条件限制
        //String[] categories = {"较快","稍难","情歌风","S"}; //满条件限制
        //String[] categories = {"快","分解教学"};
        List<Tutorial> videos = categoryDao.getResourcesByCategories(Tutorial.class, categories);
        System.out.println("最终满足的video数量 -> " + videos.size());
        for (Tutorial video : videos) {
            System.out.println(video.toString());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void deleteTutorialById() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Tutorial.class, 24)));
    }


    /**
     * 更新操作可以参考这个测试
     * *
     */
    @Test
    public void updateTutorialById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String tutorialTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "滨崎步";

        Integer id = 37;

        updatecontents.put("title", tutorialTitle);
        updatecontents.put("authorname", authorName);
        updatecontents.put("category1", "快");
        updatecontents.put("category2", "稍难");
        updatecontents.put("category3", "队形表演");

        HashMap<String, Integer> insertresult = commonDao.updateResource(Tutorial.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    /**
     * 获取所有的tutorial对象
     * *
     */
    @Test
    public void getAllTutorials() {
        List<Tutorial> s_tutorials = commonDao.getAllResource(Tutorial.class);
        System.out.println("总共查到" + s_tutorials.size());
        for (Tutorial video : s_tutorials) {
            System.out.println("----------------");
            System.out.println("id: " + video.getId());
            System.out.println(video.toString(true));

        }
    }

    /**
     * delete Tutorial
     * *
     */
    @Test
    public void deleteTutorial() {
        int res = commonDao.deleteResourceById(Tutorial.class, 5);
        System.out.println(CRUDEvent.getResponse(res));
    }

    /**
     * tutorial中 插入/更新 music
     * *
     */
    @Test
    public void insertorUpdateMusic() {
        Integer vId = 1;
        Integer mId = 1;
        System.out.println(CRUDEvent.getResponse(accompanyDao.updateResourceMusic(Tutorial.class, vId, mId)));
    }

    /**
     * 从tutorial中查询对应的music(只能查到唯一一个)
     * *
     */
    @Test
    public void getMusicFromTutorial() {
        Integer vId = 1;
        Integer mId = 3;
        //先为tutorial的id为vId的记录添加一个music
        System.out.println(CRUDEvent.getResponse(accompanyDao.updateResourceMusic(Tutorial.class, vId, mId)) + " 往id为" + vId + "的tutorial记录中插入id为" + mId + "的music");
        //查询
        Music music = ((Video) commonDao.getResourceById(Video.class, vId)).getMusic();
        if (music != null) {
            System.out.println(music.toString(true));
        } else {
            System.out.println("id为" + vId + "对应的tutorial还未包含相应的music");
        }
    }

    /**
     * 删除Tutorial中的music(将MUSIC_ID设为空)
     * *
     */
    @Test
    public void disconnectMusicFromTutorial() {
        Integer vId = 1;
        accompanyDao.deleteMusicFromResource(Tutorial.class, vId);
    }

    /**
     * 更新点击量
     * *
     */
    @Test
    public void updateTutorialHottest() {
        Integer id = 1;
        int n = 1;
        //int n = -5;
        System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Tutorial.class, id, n)));
    }

    /**
     * 按照热度排序从大到小，获取前number个video
     * *
     */
    @Test
    public void getTutorialsByHottest() {
        int number = 20;
        List<Tutorial> tutorials = commonDao.getResourcesByHottest(Tutorial.class, number);
        System.out.println("---------返回" + tutorials.size() + "个视频---------");
        for (Tutorial v : tutorials) {
            System.out.println("热度值---->" + v.getHottest());
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    /**
     * 获取最新的number个tutorial
     * *
     */
    @Test
    public void getTutorialsByNewest() {
        int number = 20;
        List<Tutorial> tutorials = commonDao.getResourcesByNewest(Tutorial.class, number);
        System.out.println("---------返回" + tutorials.size() + "个视频---------");
        for (Tutorial v : tutorials) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    /**
     * 根据authorid获得所有与之关联的video
     */
    @Test
    public void getTutorialsByAuthorId() {
        Integer aId = 2;

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", aId);

        List<Tutorial> tutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);
        System.out.println("---------返回" + tutorials.size() + "个视频---------");
        for (Tutorial v : tutorials) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.getTitle());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getTutorialsByCategoriesByPage() {
        long start = System.currentTimeMillis();
        String[] categories = {};//无条件限制
        //String[] categories = {"较快","稍难","情歌风","S"}; //满条件限制
        //String[] categories = {"快", "分解教学"};
        List<Tutorial> videos = paginationDao.getResourcesByCategoriesByPage(Tutorial.class, categories, 1);
        for (Tutorial video : videos) {
            System.out.println(video.getId());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("最终满足的video数量 -> " + videos.size());
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void isDuplicateWithPageQuery() {
        String[] categories = {};//无条件限制
        int pagecout = (int) paginationDao.getResourcePageCountByCategories(Tutorial.class, categories);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagecout; i++) {
            List<Tutorial> videos = paginationDao.getResourcesByCategoriesByPage(Tutorial.class, categories, i + 1);
            for (Tutorial video : videos) {
                System.out.println(video.getId());
                idSet.add(video.getId());
                System.out.println("——————————————————————————————————————");
            }
        }
        System.out.println("tutorial count -> " + commonDao.getAllResource(Tutorial.class).size());
        System.out.println("tutorial count -> " + idSet.size());
    }

    @Test
    public void getAllTutorialsWithoutId() {
        int vid = 1;
        List<Tutorial> allvideos = commonDao.getAllResourceWithoutId(Tutorial.class, vid);
        for (Tutorial video : allvideos) {
            System.out.println(video.getId());
        }
    }

    @Test
    public void getSideBarTutorials() {
        List<Tutorial> result = commonDao.getSideBarResources(Tutorial.class, 1);
        System.out.println(result.size());
        for (Tutorial video : result) {
            System.out.println(video.getTitle() + "-" + video.getId());
        }
    }

    @Test
    public void doRecommendTutorial() {
        recommendDao.doRecommendResource(Tutorial.class, 1);
    }

    @Test
    public void unRecommendTutorial() {
        recommendDao.unRecommendResource(Tutorial.class, 1);
    }

    @Test
    public void yaGetRecommendTutorials() {
        List<Tutorial> tutorials = recommendDao.getRecommendResources(Tutorial.class);
        for (Tutorial tutorial : tutorials) {
            System.out.println(tutorial.getTitle());
        }
    }
}
