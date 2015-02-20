package com.springapp.mvc;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.model.*;
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
public class TutorialDaoTests {
    @Autowired
    TutorialDao educationDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;

    @Test
    public void insertSingleEducationVideo() {
        String title = "Strong Heart321";
        String authorName = "周杰伦";
        String imagekey = "仓木麻衣3321.jpg";

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
        Tutorial queryVideo = educationDao.getEducationByTitleAuthorId(title, authorid);
        if (queryVideo == null) {
            System.out.println("教程和作者id组合不存在，可以进行插入");
        } else {
            System.out.println(queryVideo.getId());
            System.out.println(queryVideo.getAuthor().getName());
            System.out.println("教程和作者id组合已存在，不可以进行插入了，是否需要修改");
            return;
        }

        Tutorial video = new Tutorial();
        video.setAuthor(a);
        video.setImage(image);
        TutorialCategory c1 = new TutorialCategory();
        TutorialCategory c2 = new TutorialCategory();
        TutorialCategory c3 = new TutorialCategory();
        c1.setTitle("快");
        c2.setTitle("适中");
        c3.setTitle("分解教学");
        Set<TutorialCategory> s_eCategory = video.getCategories();
        s_eCategory.add(c1);
        s_eCategory.add(c2);
        s_eCategory.add(c3);
        video.setTitle(title);
        video.setVideo_key(title);
        video.setUpdate_timestamp(System.currentTimeMillis());
        int insertStatus = educationDao.insertSingleEducationVideo(video);
        if (insertStatus == -1) {
            System.out.println("插入教程失败");
        } else {
            System.out.println("插入教程成功，教程id是" + insertStatus);
        }

        educationDao.updateVideoKeyById(insertStatus, title + "-" + insertStatus);
    }

    @Test
    public void getEducationVideoById() {
        Tutorial video = (Tutorial) commonDao.getResourceById(Tutorial.class, 1);
        if (video != null) {
            System.out.println(video.toString(true));
        } else {
            System.out.println("无此ID视频信息");
        }
    }

    @Test
    public void getEducationVideosByCategories() {
        long start = System.currentTimeMillis();
        String[] categories = {};//无条件限制
        //String[] categories = {"较快","稍难","情歌风","S"}; //满条件限制
        //String[] categories = {"快","分解教学"};
        List<Tutorial> videos = educationDao.getEducationVideosByCategories(categories);
        System.out.println("最终满足的video数量>>>>>>>>>>>>>>>>>>>>>" + videos.size());
        for (Tutorial video : videos) {
            System.out.println(video.toString());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void deleteEducationById() {
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Tutorial.class, 24)));
    }


    /**
     * 更新操作可以参考这个测试
     * *
     */
    @Test
    public void updateEducationById() {
        Integer vid = 7;
        String tutorialTitle = "呵呵";
        String authorName = "滨崎步";
        String imageKey = "滨崎步.jpg";
        UpdateCheckResponse response = educationDao.updateEducationCheck(vid, authorName, imageKey); //先检查图片和作者姓名是否已经存在
        System.out.println(response.updateIsReady()); //若response.updateIsReady()为false,可以根据response成员变量具体的值来获悉是哪个值需要先插入数据库
        String videoSpeed = "快";  //"快","中","慢"//按速度
        String videoDifficuty = "稍难";  //"简单","适中","稍难"//按难度
        String videoStyle = "队形表演";    //"队形表演","背面教学","分解教学"//按教学类型
        Set<String> categoryTitles = new HashSet<String>();
        categoryTitles.add(videoSpeed);
        categoryTitles.add(videoDifficuty);
        categoryTitles.add(videoStyle);
        if (response.updateIsReady()) {
            //updateIsReady为true表示可以进行更新操作
            System.out.println(CRUDEvent.getResponse(educationDao.updateEducation(vid, tutorialTitle, authorName, imageKey, categoryTitles, System.currentTimeMillis())));
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
        }
    }

    /**
     * 获取所有的education对象
     * *
     */
    @Test
    public void getAllEducations() {
        List<Tutorial> s_educations = commonDao.getAllResource(Tutorial.class);
        System.out.println("总共查到" + s_educations.size());
        for (Tutorial video : s_educations) {
            System.out.println("----------------");
            System.out.println("id: " + video.getId());
            System.out.println(video.toString(true));

        }
    }

    /**
     * delete Education
     * *
     */
    @Test
    public void deleteEducation() {
        int res = commonDao.deleteResourceById(Tutorial.class, 5);
        System.out.println(CRUDEvent.getResponse(res));
    }

    /**
     * education中 插入/更新 music
     * *
     */
    @Test
    public void insertorUpdateMusic() {
        Integer vId = 1;
        Integer mId = 1;
        System.out.println(CRUDEvent.getResponse(educationDao.insertOrUpdateMusic(vId, mId)));
    }

    /**
     * 从education中查询对应的music(只能查到唯一一个)
     * *
     */
    @Test
    public void getMusicFromEducation() {
        Integer vId = 1;
        Integer mId = 3;
        //先为education的id为vId的记录添加一个music
        System.out.println(CRUDEvent.getResponse(educationDao.insertOrUpdateMusic(vId, mId)) + " 往id为" + vId + "的education记录中插入id为" + mId + "的music");
        //查询
        Music music = educationDao.getMusic(vId);
        if (music != null) {
            System.out.println(music.toString(true));
        } else {
            System.out.println("id为" + vId + "对应的education还未包含相应的music");
        }
    }

    /**
     * 删除video中的music(将MUSIC_ID设为空)
     * *
     */
    @Test
    public void deleteMusicFromVideo() {
        Integer vId = 1;
        Integer mId = 3;
        //先插入或更新一个music到education中
        System.out.println(CRUDEvent.getResponse(educationDao.insertOrUpdateMusic(vId, mId)));
        //删除刚插入的那个education中的music
        System.out.println(CRUDEvent.getResponse(educationDao.deleteMusicFromEducation(vId)));
    }

    /**
     * 更新点击量
     * *
     */
    @Test
    public void updateEducationHottest() {
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
    public void getEducationsByHottest() {
        int number = 20;
        List<Tutorial> educations = educationDao.getEducationsByHottest(number);
        System.out.println("---------返回" + educations.size() + "个视频---------");
        for (Tutorial v : educations) {
            System.out.println("热度值---->" + v.getHottest());
            System.out.println(v.toString(true));
            System.out.println("---------------------------");
        }
    }

    /**
     * 获取最新的number个education
     * *
     */
    @Test
    public void getEducationsByNewest() {
        int number = 20;
        List<Tutorial> educations = educationDao.getEducationsByNewest(number);
        System.out.println("---------返回" + educations.size() + "个视频---------");
        for (Tutorial v : educations) {
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
        List<Tutorial> tutorials = educationDao.getTutorialsByAuthorId(aId);
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
        List<Tutorial> videos = educationDao.getTutorialsByCategoriesByPage(categories, 1);
        for (Tutorial video : videos) {
            System.out.println(video.getId());
            System.out.println("——————————————————————————————————————");
        }
        System.out.println("最终满足的video数量>>>>>>>>>>>>>>>>>>>>>" + videos.size());
        System.out.println("time elapse:" + (System.currentTimeMillis() - start) / 1000f);
    }

    @Test
    public void isDuplicateWithPageQuery() {
        String[] categories = {};//无条件限制
        int pagecout = (int) educationDao.getPageCountByCategories(categories);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagecout; i++) {
            List<Tutorial> videos = educationDao.getTutorialsByCategoriesByPage(categories, i + 1);
            for (Tutorial video : videos) {
                System.out.println(video.getId());
                idSet.add(video.getId());
                System.out.println("——————————————————————————————————————");
            }
        }
        System.out.println("education count -> " + commonDao.getAllResource(Tutorial.class).size());
        System.out.println("education count -> " + idSet.size());
    }

    @Test
    public void getAllTutorialsWithoutId() {
        int vid = 1;
        List<Tutorial> allvideos = educationDao.getAllTutorialsWithoutId(vid);
        for (Tutorial video : allvideos) {
            System.out.println(video.getId());
        }
    }

    @Test
    public void getSideBarTutorials() {
        List<Tutorial> result = educationDao.getSideBarTutorials(1);
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
