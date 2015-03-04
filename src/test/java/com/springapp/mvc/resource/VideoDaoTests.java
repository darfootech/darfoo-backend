package com.springapp.mvc.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.*;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.VideoCates;
import com.darfoo.backend.utils.ModelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class VideoDaoTests {
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

    VideoCates videoCates = new VideoCates();

    @Test
    public void insertVideoResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String videoTitle = "videotitle-" + System.currentTimeMillis();
        String authorName = "周杰伦";
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        insertcontents.put("title", videoTitle);
        insertcontents.put("authorname", authorName);
        insertcontents.put("imagekey", imagekey);
        insertcontents.put("category1", "适中");
        insertcontents.put("category2", "中等");
        insertcontents.put("category3", "情歌风");
        insertcontents.put("category4", "D");
        insertcontents.put("videotype", "mp3");
        insertcontents.put("connectmusic", "ccccc-memeda-33");

        HashMap<String, Integer> insertresult = commonDao.insertResource(Video.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void updateVideokeyById() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", "cleanthacleantha.mp4");
        commonDao.updateResourceFieldsById(Video.class, 3, updateMap);
    }

    @Test
    public void getVideoByVideoId() {
        long start = System.currentTimeMillis();
        Video video = (Video) commonDao.getResourceById(Video.class, 59);
        System.out.println(video.toString(true));
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void getVideoByVideoTitle() {
        long start = System.currentTimeMillis();
        Video video = (Video) commonDao.getResourceByTitleOrName(Video.class, "ccc", "title");
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
        List<Video> videos = categoryDao.getResourcesByCategories(Video.class, categories);
        System.out.println("最终满足的video数量 -> " + videos.size());
        for (Video video : videos) {
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
        if (!requestCategories[0].equals("0")) {
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
        }

        System.out.println(targetCategories.toString());
        System.out.println(requestCategories[0]);
        System.out.println(requestCategories[0].equals("0"));
    }


    @Test
    public void deleteVideoCascade() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Video.class, 85)));
    }

    /**
     * 更新操作可以参考这个测试
     * *
     */
    @Test
    public void updateVideoById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String videoTitle = "呵呵-" + System.currentTimeMillis();
        String authorName = "仓木麻衣";

        Integer id = 81;

        updatecontents.put("title", "");
        updatecontents.put("authorname", authorName);
        updatecontents.put("category1", "适中");
        updatecontents.put("category2", "稍难");
        updatecontents.put("category3", "戏曲风");
        updatecontents.put("category4", "q".toUpperCase());
        updatecontents.put("connectmusic", "");

        HashMap<String, Integer> insertresult = commonDao.updateResource(Video.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    /**
     * 获取所有的video对象
     * *
     */
    @Test
    public void getAllVideos() {
        List<Video> s_videos = commonDao.getAllResource(Video.class);
        for (Video video : s_videos) {
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
        System.out.println(CRUDEvent.getResponse(accompanyDao.updateResourceMusic(Video.class, vId, mId)));
    }

    /**
     * 从video中查询对应的music(只能查到唯一一个)
     * *
     */
    @Test
    public void getMusicFromVideo() {
        Integer vId = 1;
        //Integer mId = 6;
        //先插入或更新一个music到video中
        //System.out.println(CRUDEvent.getResponse(videoDao.insertOrUpdateMusic(vId, mId))+" 往Id为"+vId+"的video记录中插入music_id为"+mId);
        Music music = ((Video) commonDao.getResourceById(Video.class, vId)).getMusic();
        if (music != null) {
            System.out.println(music.toString(false));
        } else {
            System.out.println("id为" + vId + "对应的video还未包含相应的music");
        }
    }

    /**
     * 删除video中的music(将MUSIC_ID设为空)
     * *
     */
    @Test
    public void disconnectVideoMusic() {
        Integer vId = 81;
        System.out.println(CRUDEvent.getResponse(accompanyDao.deleteMusicFromResource(Video.class, vId)));
    }

    /**
     * 更新点击量
     * *
     */
    @Test
    public void updateVideoHottest() {
        Integer id = 88;
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(Video.class, id, "hottest")));
    }

    /**
     * 按照热度排序从大到小，获取前number个video
     * *
     */
    @Test
    public void getVideosByHottest() {
        int number = 20;
        List<Video> videos = commonDao.getResourcesByHottest(Video.class, number);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (Video v : videos) {
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
        int number = 20;
        List<Video> videos = commonDao.getResourcesByNewest(Video.class, number);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (Video v : videos) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    /**
     * 根据musicid获得所有与之关联的video
     */
    @Test
    public void getVideosByMusicId() {
        Integer mId = 33;

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("music_id", mId);

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (Video v : videos) {
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

        List<Video> videos = accompanyDao.getResourcesWithoutMusicId(Video.class, mId);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (Video v : videos) {
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

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        System.out.println("---------返回" + videos.size() + "个视频---------");
        for (Video v : videos) {
            System.out.println("更新时间---->" + ModelUtils.dateFormat(v.getUpdate_timestamp(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(v.getTitle());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getPageCount() {
        System.out.println("pagecount -> " + paginationDao.getResourcePageCount(Video.class));
    }

    @Test
    public void getVideosByPage() {
        List<Video> result = paginationDao.getResourcesByPage(Video.class, 1);
        System.out.println(result.size());
    }

    @Test
    public void getPageCountByCategories() {
        String[] categories = {};//无条件限制
        //String[] categories = {"较快"};//满足单个条件
        System.out.println("pagecount -> " + paginationDao.getResourcePageCountByCategories(Video.class, categories));
    }

    @Test
    public void getVideosByCategoriesByPage() {
        long start = System.currentTimeMillis();
        //String[] categories = {};//无条件限制
        //String[] categories = {"较快","简单", "欢快"}; //满足所有条件
        //String[] categories = {"较快","简单", "欢快", "A"}; //满足所有条件
        String[] categories = {"适中"};//满足单个条件
        List<Video> videos = paginationDao.getResourcesByCategoriesByPage(Video.class, categories, 1);
        for (Video video : videos) {
            System.out.println(video.getId());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("最终满足的video数量 -> " + videos.size());
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void isDuplicateWithPageQuery() {
        String[] categories = {};//满足单个条件
        int pagecount = (int) paginationDao.getResourcePageCountByCategories(Video.class, categories);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagecount; i++) {
            List<Video> videos = paginationDao.getResourcesByCategoriesByPage(Video.class, categories, i + 1);
            for (Video video : videos) {
                System.out.println(video.getId());
                idSet.add(video.getId());
            }
        }

        System.out.println("video count -> " + commonDao.getAllResource(Video.class).size());
        System.out.println("video count -> " + idSet.size());
    }

    @Test
    public void getAllVideosWithoutId() {
        int vid = 1;
        List<Video> allvideos = commonDao.getAllResourceWithoutId(Video.class, vid);
        for (Video video : allvideos) {
            System.out.println(video.getId());
        }
    }

    @Test
    public void getSideBarVideos() {
        List<Video> result = commonDao.getSideBarResources(Video.class, 1);
        System.out.println(result.size());
        for (Video video : result) {
            System.out.println(video.getTitle() + "-" + video.getId());
        }
    }

    @Test
    public void doRecommendVideo() {
        int[] vids = {89, 84, 83};
        for (Integer id : vids) {
            recommendDao.doRecommendResource(Video.class, id);
        }
    }

    @Test
    public void unRecommendVideo() {
        recommendDao.unRecommendResource(Video.class, 1);
    }

    @Test
    public void getUnRecommendVideos() {
        List<Video> videos = recommendDao.getUnRecommendResources(Video.class);
        for (Video video : videos) {
            System.out.println(video.getTitle());
        }
    }

    @Test
    public void getRecommendVideos() {
        List<Video> videos = recommendDao.getRecommendResources(Video.class);
        for (Video video : videos) {
            System.out.println(video.getTitle());
        }
    }
}
