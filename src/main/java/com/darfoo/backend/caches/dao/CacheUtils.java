package com.darfoo.backend.caches.dao;

import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.ServiceUtils;
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
    AuthorDao authorDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    PaginationDao paginationDao;

    public Object cacheSingleResource(String type, Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        cacheDao.insertSingleResource(resource, object, type);
        return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), String.format("%s-%d", type, id));
    }

    public List cacheIndexResources(String type) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sindex", type);

        List resources;
        if (type.equals("video")) {
            resources = commonDao.getResourcesByNewest(resource, 12);
        } else if (type.equals("author")) {
            resources = authorDao.getAuthorsOrderByVideoCountDesc();
        } else {
            System.out.println("wired");
            resources = new ArrayList();
        }

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    public List cacheResourcesByCategories(String type, String categories) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%scategory%s", type, categories);

        List resources = categoryDao.getResourcesByCategories(resource, ServiceUtils.convertList2Array(cacheDao.parseResourceCategories(resource, categories)));
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SORTEDSET);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SORTEDSET);
    }

    public List cacheResourcesByCategoriesByPage(String type, String categories, Integer page) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%scategory%spage%d", type, categories, page);

        List resources = paginationDao.getResourcesByCategoriesByPage(resource, ServiceUtils.convertList2Array(cacheDao.parseResourceCategories(resource, categories)), page);
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    public List cacheHottestResources(String type) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        int limit = commonDao.getResourceHottestLimit(resource);
        List resources = commonDao.getResourcesByHottest(resource, limit);
        String cachekey = String.format("%shottest", type);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    public List cacheResourcesBySearch(String type, String searchContent, Integer... pageArray) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        //因为这里的分页是一次性把所有查询结果都放入缓存中然后在前端用redis游标来得到分页结果所以缓存的key中不需要包含页码号不然没请求一个页码都会在redis中生成一个完全一样的缓存结果
        String cachekey = String.format("%ssearch%s", type, searchContent);

        if (type.equals("video")) {
            String[] types = {"video", "tutorial"};

            for (String innertype : types) {
                Class innerresource = TypeClassMapping.typeClassMap.get(innertype);
                List resources = cacheDao.getSearchResourcesWithAuthor(innerresource, searchContent);
                cacheDao.insertResourcesIntoCache(innerresource, resources, cachekey, innertype, CacheCollType.LIST);
            }
        } else if (type.equals("music")) {
            List resources = commonDao.getResourcesBySearch(resource, searchContent);
            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        } else {
            System.out.println("wired");
        }

        if (pageArray.length == 0) {
            return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
        } else {
            int page = pageArray[0];
            int pageSize = paginationDao.getResourcePageSize(resource);
            long start = (page - 1) * pageSize;
            long end = page * pageSize - 1;
            return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST, start, end);
        }
    }

    public List cacheSidebarResources(String type, Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getSideBarResources(resource, id);
        String cachekey = String.format("%ssidebar%d", type, id);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return cacheDao.extractResourcesFromCache(resource, cachekey, CacheCollType.LIST);
    }

    public List cacheAuthorVideos(Integer id, Integer... pageArray) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);
        //因为这里的分页是一次性把所有查询结果都放入缓存中然后在前端用redis游标来得到分页结果所以缓存的key中不需要包含页码号不然没请求一个页码都会在redis中生成一个完全一样的缓存结果
        String cachekey = String.format("authorvideos%d", id);

        String[] types = {"video", "tutorial"};

        for (String type : types) {
            Class resource = TypeClassMapping.typeClassMap.get(type);
            List resources = commonDao.getResourcesByFields(resource, conditions);
            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        }

        if (pageArray.length == 0) {
            return cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.LIST);
        } else {
            int page = pageArray[0];
            int pageSize = paginationDao.getResourcePageSize(Video.class);
            long start = (page - 1) * pageSize;
            long end = page * pageSize - 1;

            return cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.LIST, start, end);
        }
    }
}
