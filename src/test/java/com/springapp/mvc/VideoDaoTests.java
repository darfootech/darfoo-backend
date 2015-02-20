package com.springapp.mvc;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.PaginationDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.resource.ImageDao;
import com.darfoo.backend.dao.resource.VideoDao;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.category.VideoCategory;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
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
    VideoDao videoDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    PaginationDao paginationDao;

    VideoCates videoCates = new VideoCates();

    @Test
    public void insertSingleVideo() {
        String videoTitle = "clea333";
        String authorName = "周杰伦";
        String imagekey = "滨崎步311.jpg";

        Author a = (Author) commonDao.getResourceByTitleOrName(Author.class, authorName, "name");
        if (a != null) {
            System.out.println(a.getName());
        } else {
            System.out.println("无该author记录");
            return;
        }

        Image image = imageDao.getImageByName(imagekey);
        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

        int authorid = a.getId();
        //视频title可以重名,但是不可能出现视频title一样,作者id都一样的情况,也就是一个作者的作品中不会出现重名的情况

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("title", videoTitle);
        conditions.put("author_id", authorid);

        Video queryVideo = (Video) commonDao.getResourceByFields(Video.class, conditions);
        if (queryVideo == null) {
            System.out.println("视频名字和作者id组合不存在，可以进行插入");
        } else {
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("视频名字和作者id组合已存在，不可以进行插入了，是否需要修改");
            return;
        }

        Video video = new Video();
        video.setAuthor(a);
        video.setImage(image);
        VideoCategory c1 = new VideoCategory();
        VideoCategory c2 = new VideoCategory();
        VideoCategory c3 = new VideoCategory();
        VideoCategory c4 = new VideoCategory();
        c1.setTitle("适中");
        c2.setTitle("中等");
        c3.setTitle("情歌风");
        c4.setTitle("D");
        Set<VideoCategory> s_vCategory = video.getCategories();
        s_vCategory.add(c1);
        s_vCategory.add(c2);
        s_vCategory.add(c3);
        s_vCategory.add(c4);
        video.setTitle(videoTitle);
        video.setVideo_key(videoTitle + System.currentTimeMillis());
        video.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = videoDao.insertSingleVideo(video);
        if (insertStatus == -1) {
            System.out.println("插入视频失败");
        } else {
            System.out.println("插入视频成功，视频id是" + insertStatus);
        }

        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("video_key", videoTitle + "-" + insertStatus);
        commonDao.updateResourceFieldsById(Video.class, insertStatus, updateMap);
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
        Video video = (Video) commonDao.getResourceById(Video.class, 2);
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
        List<Video> videos = videoDao.getVideosByCategories(categories);
        System.out.println("最终满足的video数量>>>>>>>>>>>>>>>>>>>>>" + videos.size());
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
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Video.class, 40)));
    }

    /**
     * 更新操作可以参考这个测试
     * *
     */
    @Test
    public void updateVideoById() {
        Integer vid = 4;
        String videoTitle = "呵呵";
        String authorName = "仓木麻衣";
        String imageKey = "仓木麻衣.jpg";
        UpdateCheckResponse response = videoDao.updateVideoCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        String videoSpeed = "适中";
        String videoDifficuty = "稍难";
        String videoStyle = "戏曲风";
        String videoLetter = "q";
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(videoSpeed);
        categoryTitles.add(videoDifficuty);
        categoryTitles.add(videoStyle);
        categoryTitles.add(videoLetter.toUpperCase());
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            System.out.println(CRUDEvent.getResponse(videoDao.updateVideo(vid, videoTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis())));
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
        }
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
        System.out.println(CRUDEvent.getResponse(videoDao.insertOrUpdateMusic(vId, mId)));
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
        Music music = videoDao.getMusic(vId);
        if (music != null) {
            System.out.println(music.toString(true));
        } else {
            System.out.println("id为" + vId + "对应的video还未包含相应的music");
        }
    }

    /**
     * 删除video中的music(将MUSIC_ID设为空)
     * *
     */
    @Test
    public void deleteMusicFromVideo() {
        Integer vId = 1;
        Integer mId = 6;
        //先插入或更新一个music到video中
        System.out.println(CRUDEvent.getResponse(videoDao.insertOrUpdateMusic(vId, mId)));
        //删除刚插入的那个video中的music
        System.out.println(CRUDEvent.getResponse(videoDao.deleteMusicFromVideo(vId)));
    }

    @Test
    public void disconnectVideoMusic() {
        Integer vId = 1;
        Integer mId = 6;
        videoDao.disconnectVideoMusic(vId, mId);
    }

    /**
     * 更新点击量
     * *
     */
    @Test
    public void updateVideoHottest() {
        Integer id = 41;
        int n = 1;
        //int n = -5;
        System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Video.class, id, n)));
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
        Integer mId = 25;

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
        Integer mId = 1;

        List<Video> videos = videoDao.getVideosWithoutMusicId(mId);
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
        //String[] categories = {"较快","稍难","情歌风","S"}; //满足所有条件
        //String[] categories = {"较快","普通","优美","0"}; //有一个条件不满足
        String[] categories = {"较快"};//满足单个条件
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
        int[] vids = {359, 360, 358, 348, 300, 138, 128};
        for (Integer id : vids) {
            recommendDao.doRecommendResource(Video.class, id);
        }
    }

    @Test
    public void unRecommendVideo() {
        recommendDao.unRecommendResource(Video.class, 1);
    }

    @Test
    public void yaGetRecommendVideos() {
        List<Video> videos = recommendDao.getRecommendResources(Video.class);
        for (Video video : videos) {
            System.out.println(video.getTitle());
        }
    }
}
