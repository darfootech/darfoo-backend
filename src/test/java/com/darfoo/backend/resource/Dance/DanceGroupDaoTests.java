package com.darfoo.backend.resource.Dance;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.LimitDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.model.cota.enums.DanceGroupType;
import com.darfoo.backend.model.cota.enums.ResourceHot;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.responsemodel.SingleDanceVideo;
import com.darfoo.backend.utils.QiniuResourceEnum;
import com.darfoo.backend.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/springmvc-hibernate.xml")
public class DanceGroupDaoTests {
    @Autowired
    DanceGroupDao authorDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    LimitDao limitDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

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

        List<SingleDanceVideo> result = new ArrayList<SingleDanceVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", aid);

        List<DanceVideo> videos = commonDao.getResourcesByFields(DanceVideo.class, conditions);

        for (DanceVideo video : videos) {
            int tid = video.getId();
            String tutorial_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key(), QiniuResourceEnum.RAW);
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key(), QiniuResourceEnum.RAW);
            String title = video.getTitle();
            long timestamp = video.getUpdate_timestamp();
            result.add(new SingleDanceVideo(tid, title, authorname, tutorial_download_url, image_download_url, 0, timestamp));
        }

        for (SingleDanceVideo video : result) {
            System.out.println(video.getTitle());
        }

        System.out.println(result.size());
    }

    @Test
    public void getDanceGroupsOrderByVideoCountDesc() {
        List<DanceGroup> result = authorDao.getDanceGroupsOrderByVideoCountDesc();
        for (DanceGroup author : result) {
            System.out.println(String.format("%d -> %s", author.getId(), author.getName()));
        }
        System.out.println("result size -> " + result.size());
    }

    @Test
    public void getAllPages() {
        System.out.println("pagecount -> " + limitDao.getResourcePageCount(DanceGroup.class));
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
    public void changeAuthorType() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("type", DanceGroupType.NORMAL);
        commonDao.updateResourceFieldsById(DanceGroup.class, 7, updateMap);
    }

    @Test
    public void getHotDanceGroups() {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("hot", ResourceHot.ISHOT);
        List authors = commonDao.getResourcesByFields(DanceGroup.class, conditions);
        for (Object author : authors) {
            System.out.println(commonDao.getResourceAttr(DanceGroup.class, author, "id"));
        }
    }

    @Test
    public void makeDanceGroupHot() {
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("hot", ResourceHot.ISHOT);
        commonDao.updateResourceFieldsById(DanceGroup.class, 7, updateMap);
    }
}
