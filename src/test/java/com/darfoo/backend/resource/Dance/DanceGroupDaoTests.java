package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.model.cota.DanceGroupHot;
import com.darfoo.backend.model.cota.DanceGroupType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceVideo;
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
public class DanceGroupDaoTests {
    @Autowired
    DanceGroupDao authorDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    PaginationDao paginationDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    @Test
    public void insertDanceGroupResource() {
        HashMap<String, String> insertcontents = new HashMap<String, String>();
        String authorName = "周杰伦" + System.currentTimeMillis();
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        insertcontents.put("imagekey", imagekey);
        insertcontents.put("name", authorName);
        insertcontents.put("imagetype", "png");
        insertcontents.put("description", "台湾人气偶像组合");
        insertcontents.put("type", "normal");

        HashMap<String, Integer> insertresult = commonDao.insertResource(DanceGroup.class, insertcontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
        System.out.println("insertid -> " + insertresult.get("insertid"));
    }

    @Test
    public void updateDanceGroupById() {
        HashMap<String, String> updatecontents = new HashMap<String, String>();
        String authorName = "滨崎步" + System.currentTimeMillis();
        String imagekey = "imagekey-" + System.currentTimeMillis() + ".jpg";

        Integer id = 110;

        updatecontents.put("name", authorName);
        updatecontents.put("imagekey", imagekey);
        updatecontents.put("description", "日本女歌手");

        HashMap<String, Integer> insertresult = commonDao.updateResource(DanceGroup.class, id, updatecontents);
        System.out.println("statuscode -> " + insertresult.get("statuscode"));
    }

    @Test
    public void getDanceGroupsByType() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", DanceGroupType.STAR);
        List<DanceGroup> danceGroups = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        for (DanceGroup danceGroup : danceGroups) {
            System.out.println(danceGroup);
            System.out.println("--------------");
        }
    }

    @Test
    public void getAuthorByName() {
        String name = "T-ara";
        DanceGroup a = (DanceGroup) commonDao.getResourceByTitleOrName(DanceGroup.class, name, "name");
        if (a != null)
            System.out.println(a.getName());
        else
            System.out.println("无该author记录");
    }

    @Test
    public void isExistAuthor() {
        String name = "cleantha";
        if (commonDao.isResourceExistsByField(DanceGroup.class, "name", name)) {
            System.out.println("已存在");
        } else {
            System.out.println("无该author记录");
        }
    }

    @Test
    public void getAllAuthor() {
        List<DanceGroup> l_author = commonDao.getAllResource(DanceGroup.class);
        for (DanceGroup a : l_author) {
            System.out.println(a.toString());
        }
    }

    @Test
    public void deleteAuthor() {
        Integer id = 6;
        System.out.println(CRUDEvent.getResponse(commonDao.deleteResourceById(DanceGroup.class, id)));
    }

    @Test
    public void getVideoResourceByAuthorid() {
        int aid = 2;

        DanceGroup author = (DanceGroup) commonDao.getResourceById(DanceGroup.class, aid);
        String authorname = author.getName();

        List<SingleVideo> result = new ArrayList<SingleVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", aid);

        List<DanceVideo> videos = commonDao.getResourcesByFields(DanceVideo.class, conditions);

        for (DanceVideo video : videos) {
            int tid = video.getId();
            String tutorial_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key(), QiniuResourceEnum.RAW);
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key(), QiniuResourceEnum.RAW);
            String title = video.getTitle();
            long timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(tid, title, authorname, tutorial_download_url, image_download_url, 0, timestamp));
        }

        for (SingleVideo video : result) {
            System.out.println(video.getTitle());
        }

        System.out.println(result.size());
    }

    @Test
    public void getAuthorOrderByVideoCountDesc() {
        List<DanceGroup> result = authorDao.getAuthorsOrderByVideoCountDesc();
        for (DanceGroup author : result) {
            System.out.println(String.format("%d -> %s", author.getId(), author.getName()));
        }
        System.out.println("result size -> " + result.size());
    }

    @Test
    public void getAuthorOrderByVideoCountDescByPage() {
        List<DanceGroup> result = authorDao.getAuthorsOrderByVideoCountDescByPage(2);
        for (DanceGroup author : result) {
            System.out.println(String.format("%d -> %s", author.getId(), author.getName()));
        }
        System.out.println("result size -> " + result.size());
    }

    @Test
    public void getAllPages() {
        System.out.println("pagecount -> " + paginationDao.getResourcePageCount(DanceGroup.class));
    }

    @Test
    public void isDuplicateWithPageQuery() {
        int pagesize = (int) paginationDao.getResourcePageCount(DanceGroup.class);
        Set<Integer> idSet = new HashSet<Integer>();
        for (int i = 0; i < pagesize; i++) {
            List<DanceGroup> result = authorDao.getAuthorsOrderByVideoCountDescByPage(i + 1);
            for (DanceGroup author : result) {
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
        System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(DanceGroup.class, id, "hottest")));
    }

    @Test
    public void getAuthorsByHottest() {
        int number = 20;
        List<DanceGroup> authors = commonDao.getResourcesByHottest(DanceGroup.class, number);
        System.out.println("---------返回" + authors.size() + "个视频---------");
        for (DanceGroup a : authors) {
            System.out.println(a.getName());
            System.out.println("热度值---->" + a.getHottest());
            System.out.println("---------------------------");
        }
    }

    @Test
    public void getAuthorsByType() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("type", DanceGroupType.NORMAL);
        List authors = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        for (Object author : authors) {
            System.out.println(commonDao.getResourceAttr(DanceGroup.class, author, "id"));
        }
    }

    @Test
    public void changeAuthorTypeNotWork() {
        authorDao.updateAuthorType(3, DanceGroupType.STAR);
    }

    @Test
    public void changeAuthorType() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("type", DanceGroupType.NORMAL);
        commonDao.updateResourceFieldsById(DanceGroup.class, 7, updateMap);
    }

    @Test
    public void getHotDanceGroups() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("hot", DanceGroupHot.ISHOT);
        List authors = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        for (Object author : authors) {
            System.out.println(commonDao.getResourceAttr(DanceGroup.class, author, "id"));
        }
    }

    @Test
    public void makeAuthorHot() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("hot", DanceGroupHot.ISHOT);
        commonDao.updateResourceFieldsById(DanceGroup.class, 7, updateMap);
    }
}
