package com.springapp.mvc.resource;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.cota.AuthorType;
import com.darfoo.backend.model.resource.Author;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.QiniuResourceEnum;
import com.darfoo.backend.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class AuthorDaoTests {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    PaginationDao paginationDao;

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
        String name = "cleantha";
        if (commonDao.isResourceExistsByField(Author.class, "name", name)) {
            System.out.println("已存在");
        } else {
            System.out.println("无该author记录");
        }
    }

    @Test
    public void insertAuthorResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String authorName = "周杰伦" + System.currentTimeMillis();
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        insertcontents.put("name", authorName);
        insertcontents.put("imagekey", imagekey);
        insertcontents.put("description", "台湾人气偶像组合");

        HashMap<String, Integer> insertresult = commonDao.insertResource(Author.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void updateAuthorById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String authorName = "滨崎步" + System.currentTimeMillis();
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        Integer id = 39;

        updatecontents.put("name", authorName);
        updatecontents.put("imagekey", imagekey);
        updatecontents.put("description", "日本女歌手");

        HashMap<String, Integer> insertresult = commonDao.updateResource(Author.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
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
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key(), QiniuResourceEnum.RAW);
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key(), QiniuResourceEnum.RAW);
            String title = video.getTitle();
            long timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(vid, title, authorname, video_download_url, image_download_url, 1, timestamp));
        }

        for (Tutorial tutorial : tutorials) {
            int tid = tutorial.getId();
            String tutorial_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key(), QiniuResourceEnum.RAW);
            String image_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getImage().getImage_key(), QiniuResourceEnum.RAW);
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
        List<Author> result = authorDao.getAuthorsOrderByVideoCountDesc();
        for (Author author : result) {
            System.out.println(String.format("%d -> %s", author.getId(), author.getName()));
        }
        System.out.println("result size -> " + result.size());
    }

    @Test
    public void getAuthorOrderByVideoCountDescByPage() {
        List<Author> result = authorDao.getAuthorsOrderByVideoCountDescByPage(2);
        for (Author author : result) {
            System.out.println(String.format("%d -> %s", author.getId(), author.getName()));
        }
        System.out.println("result size -> " + result.size());
    }

    @Test
    public void getAllPages() {
        System.out.println("pagecount -> " + paginationDao.getResourcePageCount(Author.class));
    }

    @Test
    public void isDuplicateWithPageQuery() {
        int pagesize = (int) paginationDao.getResourcePageCount(Author.class);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagesize; i++) {
            List<Author> result = authorDao.getAuthorsOrderByVideoCountDescByPage(i + 1);
            for (Author author : result) {
                System.out.println(String.format("%d -> %s", author.getId(), author.getName()));
                idSet.add(author.getId());
            }
            System.out.println("result size -> " + result.size());
        }

        System.out.println("author count -> " + authorDao.getAuthorsOrderByVideoCountDesc().size());
        System.out.println("author count -> " + idSet.size());
    }

    @Test
    public void updateAuthorHottest() {
        Integer id = 1;
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(Author.class, id, "hottest")));
    }

    @Test
    public void getAuthorsByHottest() {
        int number = 20;
        List<Author> authors = commonDao.getResourcesByHottest(Author.class, number);
        System.out.println("---------返回" + authors.size() + "个视频---------");
        for (Author a : authors) {
            System.out.println(a.getName());
            System.out.println("热度值---->" + a.getHottest());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void changeAuthorType() {
        Class resource = Author.class;
        commonDao.saveResource(commonDao.setResourceAttr(resource, commonDao.getResourceById(resource, 3), "type", AuthorType.NORMAL));
    }
}
