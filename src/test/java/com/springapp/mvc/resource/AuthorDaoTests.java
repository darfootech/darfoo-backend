package com.springapp.mvc.resource;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.resource.ImageDao;
import com.darfoo.backend.dao.resource.TutorialDao;
import com.darfoo.backend.dao.resource.VideoDao;
import com.darfoo.backend.model.*;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Image;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class AuthorDaoTests {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    ImageDao imageDao;
    @Autowired
    TutorialDao tutorialDao;
    @Autowired
    VideoDao videoDao;
    @Autowired
    CommonDao commonDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    @Test
    public void getAuthorByName() {
        String name = "T-ara";
        Author a = (Author) commonDao.getResourceByTitleOrName(Author.class, name, "name");
        if (a != null)
            System.out.println(a.getName());
        else
            System.out.println("无该author记录");
    }

    @Test
    public void isExistAuthor() {
        String name = "T-ara";
        if (authorDao.isExistAuthor(name)) {
            System.out.println("已存在");
        } else {
            System.out.println("无该author记录");
        }
    }

    @Test
    public void insertAuthor() {
        String authorName = System.currentTimeMillis() + "";
        String imagekey = System.currentTimeMillis() + "dg111";

        if (authorDao.isExistAuthor(authorName)) {
            System.out.println("已存在，不能创建新作者");
            return;
        } else {
            System.out.println("无该author记录，可以创建新作者");
        }

        HashMap<String, Object> imageConditions = new HashMap<String, Object>();
        imageConditions.put("image_key", imagekey);

        Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);
        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(imagekey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

        Author author = new Author();
        author.setName(authorName);
        author.setDescription("台湾人气偶像组合");
        author.setImage(image);
        int res = authorDao.insertAuthor(author);
        System.out.println(CRUDEvent.getResponse(res));
    }

    @Test
    public void batchInsertAuthor() {
        for (int i = 0; i < 18; i++) {
            String authorName = System.currentTimeMillis() + "";
            String imagekey = System.currentTimeMillis() + "dg111";

            if (authorDao.isExistAuthor(authorName)) {
                System.out.println("已存在，不能创建新作者");
                return;
            } else {
                System.out.println("无该author记录，可以创建新作者");
            }

            HashMap<String, Object> imageConditions = new HashMap<String, Object>();
            imageConditions.put("image_key", imagekey);

            Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);

            if (image == null) {
                System.out.println("图片不存在，可以进行插入");
                image = new Image();
                image.setImage_key(imagekey);
                imageDao.insertSingleImage(image);
            } else {
                System.out.println("图片已存在，不可以进行插入了，是否需要修改");
                return;
            }

            Author author = new Author();
            author.setName(authorName);
            author.setDescription("台湾人气偶像组合");
            author.setImage(image);
            int res = authorDao.insertAuthor(author);
            System.out.println(CRUDEvent.getResponse(res));
        }
    }

    /**
     * 更新作者，先对image进行是否存在的检测，再更新
     * *
     */
    @Test
    public void updateAuthor() {
        Integer id = 4;
        String newName = "滨崎步";
        String newDesciption = "日本女歌手";
        String newimageKey = "滨崎步3113.jpg";

        if (authorDao.isExistAuthor(newName)) {
            System.out.println("已存在，不能修改作者名字");
            return;
        } else {
            System.out.println("作者名字不存在，可以进行修改");
        }

        HashMap<String, Object> imageConditions = new HashMap<String, Object>();
        imageConditions.put("image_key", newimageKey);

        Image image = (Image) commonDao.getResourceByFields(Image.class, imageConditions);

        if (image == null) {
            System.out.println("图片不存在，可以进行插入");
            image = new Image();
            image.setImage_key(newimageKey);
            imageDao.insertSingleImage(image);
        } else {
            System.out.println("图片已存在，不可以进行插入了，是否需要修改");
            return;
        }

        UpdateCheckResponse response = authorDao.updateAuthorCheck(id, newimageKey);
        if (response.updateIsReady()) {
            int res = authorDao.updateAuthor(id, newName, newDesciption, newimageKey);//更新id为2的Author对象的名字
            System.out.println(CRUDEvent.getResponse(res));
        } else {
            System.out.println("请根据reponse中的成员变量值来设计具体逻辑");
        }

    }

    @Test
    public void getAllAuthor() {
        List<Author> l_author = commonDao.getAllResource(Author.class);
        for (Author a : l_author) {
            System.out.println(a.toString());
        }
    }

    @Test
    public void deleteAuthor() {
        Integer id = 6;
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(Author.class, id)));
    }

    @Test
    public void getVideoResourceByAuthorid() {
        int aid = 2;

        Author author = (Author) commonDao.getResourceById(Author.class, aid);
        String authorname = author.getName();

        List<SingleVideo> result = new ArrayList<SingleVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", aid);

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        List<Tutorial> tutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);

        for (Video video : videos) {
            int vid = video.getId();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());
            String title = video.getTitle();
            long timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(vid, title, authorname, video_download_url, image_download_url, 1, timestamp));
        }

        for (Tutorial tutorial : tutorials) {
            int tid = tutorial.getId();
            String tutorial_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getImage().getImage_key());
            String title = tutorial.getTitle();
            long timestamp = tutorial.getUpdate_timestamp();
            result.add(new SingleVideo(tid, title, authorname, tutorial_download_url, image_download_url, 0, timestamp));
        }

        for (SingleVideo video : result) {
            System.out.println(video.getTitle());
        }

        System.out.println(result.size());
    }

    @Test
    public void getAuthorOrderByVideoCountDesc() {
        List<Object[]> result = authorDao.getAuthorOrderByVideoCountDesc();
        for (Object[] rows : result) {
            System.out.println((Integer) rows[1] + " -> " + ((BigInteger) rows[0]).intValue());
            Author author = (Author) commonDao.getResourceById(Author.class, (Integer) rows[1]);
            System.out.println(author.getName());
        }
    }

    @Test
    public void getAuthorOrderByVideoCountDescByPage() {
        List<Object[]> result = authorDao.getAuthorOrderByVideoCountDescByPage(2);
        for (Object[] rows : result) {
            System.out.println((Integer) rows[1] + " -> " + ((BigInteger) rows[0]).intValue());
            Author author = (Author) commonDao.getResourceById(Author.class, (Integer) rows[1]);
            System.out.println(author.getName());
        }
        System.out.println("result size -> " + result.size());
    }

    @Test
    public void getAllPages() {
        System.out.println("pagecount -> " + authorDao.getPageCount());
    }

    @Test
    public void sublistTest() {
        List<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);

        List<Integer> subtest = test.subList(0, 1);
        for (Integer id : subtest) {
            System.out.println(id);
        }
    }

    @Test
    public void isDuplicateWithPageQuery() {
        int pagesize = (int) authorDao.getPageCount();
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagesize; i++) {
            List<Object[]> result = authorDao.getAuthorOrderByVideoCountDescByPage(i + 1);
            for (Object[] rows : result) {
                System.out.println((Integer) rows[1] + " -> " + ((BigInteger) rows[0]).intValue());
                Author author = (Author) commonDao.getResourceById(Author.class, (Integer) rows[1]);
                System.out.println(author.getName());
                idSet.add((Integer) rows[1]);
            }
            System.out.println("result size -> " + result.size());
        }

        System.out.println("author count -> " + authorDao.getAuthorOrderByVideoCountDesc().size());
        System.out.println("author count -> " + idSet.size());
    }

    @Test
    public void updateVideoHottest() {
        Integer id = 1;
        int n = 1;
        System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Author.class, id, n)));
    }

    @Test
    public void getVideosByHottest() {
        int number = 20;
        List<Author> authors = commonDao.getResourcesByHottest(Author.class, number);
        System.out.println("---------返回" + authors.size() + "个视频---------");
        for (Author a : authors) {
            System.out.println(a.getName());
            System.out.println("热度值---->" + a.getHottest());
            System.out.println("---------------------------");
        }
    }
}