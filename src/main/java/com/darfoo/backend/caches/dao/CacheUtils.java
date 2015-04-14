package com.darfoo.backend.caches.dao;

import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.LimitDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.dao.statistic.StatisticsDao;
import com.darfoo.backend.model.cota.annotations.limit.HotSize;
import com.darfoo.backend.model.cota.annotations.limit.NewestSize;
import com.darfoo.backend.model.cota.annotations.limit.PageSize;
import com.darfoo.backend.model.cota.enums.DanceGroupType;
import com.darfoo.backend.model.resource.dance.DanceGroup;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.model.resource.opera.OperaVideo;
import com.darfoo.backend.service.category.DanceVideoCates;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.SingleDanceMusic;
import com.darfoo.backend.service.responsemodel.SingleDanceVideo;
import com.darfoo.backend.service.responsemodel.SingleOperaVideo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjh on 15-3-1.
 */
public class CacheUtils {
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    DanceGroupDao danceGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    LimitDao limitDao;
    @Autowired
    AccompanyCacheDao accompanyCacheDao;
    @Autowired
    StatisticsDao statisticsDao;
    @Autowired
    StatisticsCacheDao statisticsCacheDao;

    /**
     * 分页统一在cache层做
     * 先将满足条件的资源全部插入redis 然后直接在redis层进行切片来达到分页的效果
     *
     * @param resource
     * @param response
     * @param cachekey
     * @param cachetype
     * @param pageArray
     * @return
     */
    //因为这里的分页是一次性把所有查询结果都放入缓存中然后在前端用redis游标来得到分页结果所以缓存的key中不需要包含页码号不然每请求一个页码都会在redis中生成一个完全一样的缓存结果
    //一种请求方式是按照定死的每一页显示的格式与页码来请求分页信息
    //一种请求方式是根据请求的跳过的个数与要返回的个数来获取翻页信息
    public List returnWithPagination(Class resource, Class response, String cachekey, CacheCollType cachetype, Integer[] pageArray) {
        if (pageArray.length == 0) {
            return cacheDao.extractResourcesFromCache(response, cachekey, cachetype);
        } else if (pageArray.length == 1) {
            int page = pageArray[0];
            int pageSize = limitDao.getResourceLimitSize(resource, PageSize.class);
            long start = (page - 1) * pageSize;
            long end = page * pageSize - 1;
            return cacheDao.extractResourcesFromCache(response, cachekey, cachetype, start, end);
        } else if (pageArray.length == 2) {
            int skipNum = pageArray[0];
            int returnNum = pageArray[1];
            long start = skipNum;
            long end = skipNum + returnNum - 1;
            return cacheDao.extractResourcesFromCache(response, cachekey, cachetype, start, end);
        } else {
            return new ArrayList();
        }
    }

    /**
     * 将单个资源缓存进入redis
     *
     * @param type
     * @param id
     * @return
     */
    public Object cacheSingleResource(String type, Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        cacheDao.insertSingleResource(resource, object, type);
        return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), String.format("%s-%d", type, id));
    }

    public List cacheRecommendResources(String type) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%srecommend", type);

        List recommendResources = recommendDao.getRecommendResources(resource);
        cacheDao.insertResourcesIntoCache(resource, recommendResources, cachekey, cachekey, CacheCollType.SORTEDSET);

        return cacheDao.extractResourcesFromCache(SingleDanceVideo.class, cachekey, CacheCollType.SORTEDSET);
    }

    /**
     * 对于dancegroup就是获取所有资源并且根据关联的视频个数倒排序 也是为了和老api相兼容
     *
     * @param type
     * @return
     */
    public List cacheIndexResources(String type, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sindex", type);

        List resources;
        if (type.equals("dancegroup")) {
            resources = danceGroupDao.getDanceGroupsOrderByVideoCountDesc();
        } else {
            resources = commonDao.getAllResource(resource);
        }

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    /**
     * 根据最新资源个数限制根据修改日期倒排序返回资源列表
     *
     * @param type
     * @param pageArray
     * @return
     */
    public List cacheNewestResources(String type, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%snewest", type);

        int newestsize = limitDao.getResourceLimitSize(resource, NewestSize.class);
        List resources = commonDao.getResourcesByNewest(resource, newestsize);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    public List cacheAllResources(String type, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sall", type);

        List resources = commonDao.getAllResource(resource);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    /**
     * 根据类别获取相应的资源
     * 对于dancevideo 类别有 {0 -> 正面教学, 1 -> 口令分解, 2 -> 背面教学, 3 -> 队形教学}
     * 在获取类别的时候其实不需要去判断是欣赏视频还是教程 只要保证类别的标识是互相隔离的就可以了
     * 对于dancemusic 类别为 1->26 表示A->Z 26个字母
     *
     * @param type
     * @param category
     * @param pageArray
     * @return
     */
    public List cacheResourcesByCategory(String type, String category, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%scategory%s", type, category);

        List resources;
        if (type.equals("dancevideo")) {
            resources = categoryDao.getResourcesByCategory(resource, DanceVideoCates.danceVideoCategoryMap.get(category));
        } else if (type.equals("dancemusic")) {
            resources = categoryDao.getResourcesByCategory(resource, category);
        } else {
            System.out.println("wired");
            resources = new ArrayList();
        }

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    /**
     * 根据子类别获取相应的资源
     * 对于dancevideo 子类别有 {NORMAL -> 欣赏, TUTORIAL -> 教学}
     * 对于dancegroup 子类别有 {NORMAL -> 普通舞队, STAR -> 明星舞队}
     *
     * @param type
     * @param innertype
     * @param pageArray
     * @return
     */
    public List cacheResourcesByInnertype(String type, String innertype, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sinnertype%s", type, innertype);
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        Enum innertypeValue = Enum.valueOf(TypeClassMapping.innerTypeClassMap.get(type), innertype);
        conditions.put("type", innertypeValue);

        List resources;

        if (resource == DanceGroup.class && innertypeValue == DanceGroupType.STAR) {
            resources = commonDao.getResourcesWithPriority(resource);
        } else {
            resources = commonDao.getResourcesByFields(resource, conditions);
        }

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    /**
     * 获取热门资源
     *
     * @param type
     * @return
     */
    public List cacheHotResources(String type, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%shot", type);

        List resources;
        if (resource.isAnnotationPresent(HotSize.class)) {
            resources = commonDao.getResourcesWithHotPriority(resource);
        } else {
            resources = new ArrayList();
        }

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    public List cacheResourcesBySearch(String type, String searchContent, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%ssearch%s", type, searchContent);

        List resources;
        if (type.equals("dancevideo")) {
            resources = cacheDao.getSearchResourcesWithDanceGroup(resource, searchContent);
        } else if (type.equals("dancemusic") || type.equals("operavideo")) {
            resources = commonDao.getResourcesBySearch(resource, searchContent);
        } else {
            System.out.println("wired");
            resources = new ArrayList();
        }
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);

        return returnWithPagination(resource, TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    public List cacheSidebarResources(String type, Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getSideBarResources(resource, id);
        String cachekey = String.format("%ssidebar%d", type, id);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET);
    }

    public List cacheDanceGroupVideos(Integer id, Integer... pageArray) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);
        String cachekey = String.format("dancegroupvideos%d", id);

        Class resource = DanceVideo.class;
        List resources = commonDao.getResourcesByFields(resource, conditions);
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, resource.getSimpleName().toLowerCase(), CacheCollType.SORTEDSET);

        return returnWithPagination(resource, SingleDanceVideo.class, cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    public List cacheOperaSeriesVideos(Integer id, Integer... pageArray) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("series_id", id);
        String cachekey = String.format("operaseriesvideos%d", id);

        Class resource = OperaVideo.class;
        List resources = commonDao.getResourcesByFields(resource, conditions);
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, resource.getSimpleName().toLowerCase(), CacheCollType.SORTEDSET);

        return returnWithPagination(resource, SingleOperaVideo.class, cachekey, CacheCollType.SORTEDSET, pageArray);
    }

    public Object cacheDanceMusicForDanceVideo(Integer id) {
        String type = "dancemusic";
        Class resource = TypeClassMapping.typeClassMap.get(type);
        DanceMusic targetMusic = ((DanceVideo) commonDao.getResourceById(DanceVideo.class, id)).getMusic();
        if (targetMusic != null) {
            int mid = targetMusic.getId();
            accompanyCacheDao.insertAccompany(DanceVideo.class, DanceMusic.class, id, mid);
            Object object = commonDao.getResourceById(resource, mid);
            cacheDao.insertSingleResource(resource, object, type);
            return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), String.format("%s-%d", type, mid));
        } else {
            return new SingleDanceMusic(-1, "", "", "", 0L);
        }
    }

    public List cacheHotSearchKeyWords(String type) {
        statisticsCacheDao.insertHotSearchIntoCache(statisticsDao.getHotSearchKeyWordsByType(type), type);
        return statisticsCacheDao.extractHotSearchFromCache(type);
    }
}
