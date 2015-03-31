package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.*;
import com.darfoo.backend.dao.resource.InsertDao;
import com.darfoo.backend.dao.resource.UpdateDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.responsemodel.DanceVideoCates;
import com.darfoo.backend.utils.ModelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DanceVideoDaoTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    AccompanyDao accompanyDao;
    @Autowired
    InsertDao insertDao;
    @Autowired
    UpdateDao updateDao;

    DanceVideoCates videoCates = new DanceVideoCates();

    @Test
    public void insertVideoResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String videoTitle = "videotitle-" + System.currentTimeMillis();
        String authorName = "王广成广场舞";
        String imagekey = String.format("dancevideo-imagekey-%s.%s", System.currentTimeMillis(), ".jpg");

        insertcontents.put("imagekey", imagekey);
        insertcontents.put("title", videoTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("imagetype", "jpg");
        /*insertcontents.put("category1", "正面教学");
        insertcontents.put("category2", "口令分解");
        insertcontents.put("category3", "背面教学");
        insertcontents.put("category4", "队形教学");*/
        insertcontents.put("category", "");
        insertcontents.put("type", "tutorial");
        insertcontents.put("videotype", "mp4");
        insertcontents.put("connectmusic", "ccccc-memeda-33");

        HashMap<String, Integer> insertresult = commonDao.insertResource(DanceVideo.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    /**
     * 更新操作可以参考这个测试
     * *
     */
    @Test
    public void updateVideoById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String videoTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "王广成广场舞";

        Integer id = 1055;

        updatecontents.put("title", videoTitle);
        updatecontents.put("authorname", authorName);
        //updatecontents.put("category1", "正面教学");
        //updatecontents.put("category2", "口令分解");
        //updatecontents.put("category3", "背面教学");
        //updatecontents.put("category4", "队形教学");
        updatecontents.put("type", "normal");
        updatecontents.put("category", "");
        updatecontents.put("connectmusic", "");

        HashMap<String, Integer> insertresult = commonDao.updateResource(DanceVideo.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void updateVideokeyById() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", "cleanthacleantha.mp4");
        commonDao.updateResourceFieldsById(DanceVideo.class, 3, updateMap);
    }

    @Test
    public void getVideoByVideoId() {
        long start = System.currentTimeMillis();
        DanceVideo video = (DanceVideo) commonDao.getResourceById(DanceVideo.class, 59);
        System.out.println(video.toString(true));
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    /**
     * video的标题可以重复但是同一个明星舞队下面的video标题不可以重复
     */
    @Test
    public void getVideoByTitleAndAuthorId() {
        long start = System.currentTimeMillis();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", "ccc");
        conditions.put("author_id", 3);

        DanceVideo video = (DanceVideo) commonDao.getResourceByFields(DanceVideo.class, conditions);
        if (video == null) {
            System.out.println("对象不存在，可以进行插入");
        } else {
            System.out.println(video.toString(true));
            System.out.println("对象已存在，不可以进行插入了，是否需要修改");
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void getVideosByCategories() {
        long start = System.currentTimeMillis();
        //String[] categories = {};//无条件限制
        //String[] categories = {"较快","稍难","情歌风","S"}; //满足所有条件
        //String[] categories = {"较快","普通","优美","0"}; //有一个条件不满足
        String[] categories = {"较快"};//满足单个条件
        List<DanceVideo> videos = categoryDao.getResourcesByCategories(DanceVideo.class, categories);
        System.out.println("最终满足的video数量 -> " + videos.size());
        for (DanceVideo video : videos) {
            System.out.println(video.getId());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void requestVideosByCategories() {
        String categories = "0-1-4-3";
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();
        /*if (!requestCategories[0].equals("0")) {
            String speedCate = videoCates.getSpeedCategory().get(requestCategories[0]);
            targetCategories.add(speedCate);
        }
        if (!requestCategories[1].equals("0")) {
            String difficultyCate = videoCates.getDifficultyCategory().get(requestCategories[1]);
            targetCategories.add(difficultyCate);
            System.out.println("!!!!speedcate!!!!" + difficultyCate);
        }
        if (!requestCategories[2].equals("0")) {
            String styleCate = videoCates.getStyleCategory().get(requestCategories[2]);
            targetCategories.add(styleCate);
        }
        if (!requestCategories[3].equals("0")) {
            String letterCate = requestCategories[3];
            targetCategories.add(letterCate);
        }*/

        System.out.println(targetCategories.toString());
        System.out.println(requestCategories[0]);
        System.out.println(requestCategories[0].equals("0"));
    }


    @Test
    public void deleteVideoCascade() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(DanceVideo.class, 91)));
    }

    /**
     * 获取所有的video对象
     * *
     */
    @Test
    public void getAllVideos() {
        List<DanceVideo> s_videos = commonDao.getAllResource(DanceVideo.class);
        for (DanceVideo video : s_videos) {
            System.out.println("----------------");
            System.out.println("videois: " + video.getId());
            //System.out.println(video.toString(true));
        }
        System.out.println("总共查到" + s_videos.size());
    }

    /**
     * video中 插入/更新 music
     * *
     */
    @Test
    public void insertOrUpdateMusic() {
        Integer vId = 1;
        Integer mId = 6;
        System.out.println(CRUDEvent.getResponse(accompanyDao.updateResourceMusic(DanceVideo.class, vId, mId)));
    }

    /**
     * 从video中查询对应的music(只能查到唯一一个)
     * *
     */
    @Test
    public void getMusicFromVideo() {
        Integer id = 79;
        DanceMusic music = ((DanceVideo) commonDao.getResourceById(DanceVideo.class, id)).getMusic();
        if (music != null) {
            System.out.println(music.toString(false));
        } else {
            System.out.println("id为" + id + "对应的video还未包含相应的music");
        }
    }

    /**
     * 删除video中的music(将MUSIC_ID设为空)
     * *
     */
    @Test
    public void disconnectVideoMusic() {
        Integer vId = 81;
        System.out.println(CRUDEvent.getResponse(accompanyDao.deleteMusicFromResource(DanceVideo.class, vId)));
    }

    /**
     * 更新点击量
     * *
     */
    @Test
    public void updateVideoHottest() {
        Integer id = 81;
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(DanceVideo.class, id, "hottest")));
    }

    /**
     * 按照热度排序从大到小，获取前number个video
     * *
     */
    @Test
    public void getVideosByHottest() {
        int number = 20;
        List<DanceVideo> videos = commonDao.getResourcesByHottest(DanceVideo.class, number);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (DanceVideo v : videos) {
            System.out.println("热度值---->" + v.getHottest());
            System.out.println(v.toString(false));
            System.out.println("---------------------------");
        }
    }

    /**
     * 获取最新的number个video
     * *
     */
    @Test
    public void getVideosByNewest() {
        int number = 12;
        List<DanceVideo> videos = commonDao.getResourcesByNewest(DanceVideo.class, number);
        for (DanceVideo v : videos) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
        System.out.println("---------返回" + videos.size() + "个视频---------");
    }

    /**
     * 根据musicid获得所有与之关联的video
     */
    @Test
    public void getVideosByMusicId() {
        Integer mId = 33;

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("music_id", mId);

        List<DanceVideo> videos = commonDao.getResourcesByFields(DanceVideo.class, conditions);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (DanceVideo v : videos) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.getTitle());
            System.out.println("---------------------------");
        }
    }

    /**
     * 根据musicid获得所有不与之关联的video
     */
    @Test
    public void getVideosWithoutMusicId() {
        Integer mId = 33;

        List<DanceVideo> videos = accompanyDao.getResourcesWithoutMusicId(DanceVideo.class, mId);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (DanceVideo v : videos) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.getTitle());
            System.out.println("---------------------------");
        }
    }

    /**
     * 根据authorid获得所有与之关联的video
     */
    @Test
    public void getVideosByAuthorId() {
        Integer aId = 1;

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", aId);

        List<DanceVideo> videos = commonDao.getResourcesByFields(DanceVideo.class, conditions);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (DanceVideo v : videos) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.getTitle());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getPageCount() {
        System.out.println("pagecount -> " + paginationDao.getResourcePageCount(DanceVideo.class));
    }

    @Test
    public void getVideosByPage() {
        List<DanceVideo> result = paginationDao.getResourcesByPage(DanceVideo.class, 1);
        System.out.println(result.size());
    }

    @Test
    public void getPageCountByCategories() {
        String[] categories = {};//无条件限制
        //String[] categories = {"较快"};//满足单个条件
        System.out.println("pagecount -> " + paginationDao.getResourcePageCountByCategories(DanceVideo.class, categories));
    }

    @Test
    public void getVideosByCategoriesByPage() {
        long start = System.currentTimeMillis();
        //String[] categories = {};//无条件限制
        //String[] categories = {"较快","简单", "欢快"}; //满足所有条件
        //String[] categories = {"较快","简单", "欢快", "A"}; //满足所有条件
        String[] categories = {"适中"};//满足单个条件
        List<DanceVideo> videos = paginationDao.getResourcesByCategoriesByPage(DanceVideo.class, categories, 1);
        for (DanceVideo video : videos) {
            System.out.println(video.getId());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("最终满足的video数量 -> " + videos.size());
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void isDuplicateWithPageQuery() {
        String[] categories = {};//满足单个条件
        int pagecount = (int) paginationDao.getResourcePageCountByCategories(DanceVideo.class, categories);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagecount; i++) {
            List<DanceVideo> videos = paginationDao.getResourcesByCategoriesByPage(DanceVideo.class, categories, i + 1);
            for (DanceVideo video : videos) {
                System.out.println(video.getId());
                idSet.add(video.getId());
            }
        }

        System.out.println("video count -> " + commonDao.getAllResource(DanceVideo.class).size());
        System.out.println("video count -> " + idSet.size());
    }

    @Test
    public void getAllVideosWithoutId() {
        int vid = 1;
        List<DanceVideo> allvideos = commonDao.getAllResourceWithoutId(DanceVideo.class, vid);
        for (DanceVideo video : allvideos) {
            System.out.println(video.getId());
        }
    }

    @Test
    public void getSideBarVideos() {
        List<DanceVideo> result = commonDao.getSideBarResources(DanceVideo.class, 79);
        System.out.println(result.size());
        for (DanceVideo video : result) {
            System.out.println(video.getTitle() + "-" + video.getId());
        }
    }

    @Test
    public void doRecommendVideo() {
        int[] vids = {89, 84, 83};
        for (Integer id : vids) {
            recommendDao.doRecommendResource(DanceVideo.class, id);
        }
    }

    @Test
    public void unRecommendVideo() {
        recommendDao.unRecommendResource(DanceVideo.class, 1);
    }

    @Test
    public void getUnRecommendVideos() {
        List<DanceVideo> videos = recommendDao.getUnRecommendResources(DanceVideo.class);
        for (DanceVideo video : videos) {
            System.out.println(video.getTitle());
        }
    }

    @Test
    public void getRecommendVideos() {
        List<DanceVideo> videos = recommendDao.getRecommendResources(DanceVideo.class);
        for (DanceVideo video : videos) {
            System.out.println(video.getTitle());
        }
    }
}
