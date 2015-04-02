package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.*;
import com.darfoo.backend.dao.resource.DanceVideoDao;
import com.darfoo.backend.model.cota.enums.DanceVideoType;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.service.cota.TypeClassMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DanceVideoDaoTests {
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    LimitDao limitDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    AccompanyDao accompanyDao;
    @Autowired
    DanceVideoDao danceVideoDao;

    public void logDanceVideos(List<DanceVideo> danceVideos) {
        for (DanceVideo danceVideo : danceVideos) {
            System.out.println(danceVideo.toString(true));
            System.out.println("---------");
        }
        System.out.println("dancevideo数量 -> " + danceVideos.size());
    }

    @Test
    public void updateVideokeyById() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", "cleanthacleantha.mp4");
        commonDao.updateResourceFieldsById(DanceVideo.class, 3, updateMap);
    }

    @Test
    public void getVideoByVideoId() {
        DanceVideo video = (DanceVideo) commonDao.getResourceById(DanceVideo.class, 1075);
        System.out.println(video.toString(true));
    }

    /**
     * video的标题可以重复但是同一个明星舞队下面的video标题不可以重复
     */
    @Test
    public void getVideoByTitleAndDanceGroupId() {
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
    public void getVideosByCategory() {
        String request = "0";
        String category = DanceVideoCates.danceVideoCategoryMap.get(request);
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", DanceVideoType.TUTORIAL);
        List<DanceVideo> videos = categoryDao.getResourcesByCategory(DanceVideo.class, category);
        logDanceVideos(videos);
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
        List<DanceVideo> danceVideos = commonDao.getAllResource(DanceVideo.class);
        logDanceVideos(danceVideos);
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
            System.out.println(music.toString());
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
        logDanceVideos(videos);
    }

    /**
     * 获取最新的number个video
     * *
     */
    @Test
    public void getVideosByNewest() {
        int number = 12;
        List<DanceVideo> videos = commonDao.getResourcesByNewest(DanceVideo.class, number);
        logDanceVideos(videos);
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
        logDanceVideos(videos);
    }

    /**
     * 根据musicid获得所有不与之关联的video
     */
    @Test
    public void getVideosWithoutMusicId() {
        Integer mId = 33;

        List<DanceVideo> videos = accompanyDao.getResourcesWithoutMusicId(DanceVideo.class, mId);
        logDanceVideos(videos);
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
        logDanceVideos(videos);
    }

    @Test
    public void getAllVideosWithoutId() {
        int vid = 1;
        List<DanceVideo> allvideos = commonDao.getAllResourceWithoutId(DanceVideo.class, vid);
        logDanceVideos(allvideos);
    }

    @Test
    public void getSideBarVideos() {
        List<DanceVideo> result = commonDao.getSideBarResources(DanceVideo.class, 79);
        logDanceVideos(result);
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
        logDanceVideos(videos);
    }

    @Test
    public void getRecommendVideos() {
        List<DanceVideo> videos = recommendDao.getRecommendResources(DanceVideo.class);
        logDanceVideos(videos);
    }

    @Test
    public void changeVideoTypeWithGroupId() {
        Integer danceGroupId = 109;
        DanceVideoType targetType = DanceVideoType.NORMAL;
        danceVideoDao.updateDanceVideoTypeWithDanceGroupId(danceGroupId, targetType);
    }

    @Test
    public void getDanceVideosByType() {
        String type = "dancevideo";
        //String innertype = "TUTORIAL";
        String innertype = "NORMAL";
        Class resource = TypeClassMapping.typeClassMap.get(type);
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", Enum.valueOf(TypeClassMapping.innerTypeClassMap.get(type), innertype));

        List<DanceVideo> danceVideos = commonDao.getResourcesByFields(resource, conditions);
        logDanceVideos(danceVideos);
    }
}
