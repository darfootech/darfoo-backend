package com.darfoo.backend.service;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.*;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zjh on 14-12-18.
 */

@Controller
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    CacheDao cacheDao;
    @Autowired
    AuthorDao authorDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CategoryDao categoryDao;

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public @ResponseBody Object getSingleResourceFromCache(@PathVariable String type, @PathVariable Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        cacheDao.insertSingleResource(resource, object, type);
        return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), String.format("%s-%d", type, id));
    }

    @RequestMapping(value = "/video/recommend", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheRecmmendVideos() {
        String cachekey = "recommend";

        String[] types = {"video", "tutorial"};

        for (String type : types) {
            Class resource = TypeClassMapping.typeClassMap.get(type);

            List recommendResources = recommendDao.getRecommendResources(resource);
            cacheDao.insertResourcesIntoCache(resource, recommendResources, cachekey, type, CacheCollType.SET);
        }

        return cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.SET);
    }

    @RequestMapping(value = "/{type}/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheIndexResources(@PathVariable String type) {
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

    @RequestMapping(value = "/{type}/index/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheIndexResourcesByPage(@PathVariable String type, @PathVariable Integer page) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sindexpage%d", type, page);

        List resources = authorDao.getAuthorsOrderByVideoCountDesc();

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }


    @RequestMapping(value = "/{type}/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategories(@PathVariable String type, @PathVariable String categories) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%s", type, categories);

        List resources = categoryDao.getResourcesByCategories(resource, ServiceUtils.convertList2Array(cacheDao.parseResourceCategories(resource, categories)));
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SET);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SET);
    }

    @RequestMapping(value = "/{type}/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategoriesByPage(@PathVariable String type, @PathVariable String categories, @PathVariable Integer page) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%spage%d", type, categories, page);

        List resources = paginationDao.getResourcesByCategoriesByPage(resource, ServiceUtils.convertList2Array(cacheDao.parseResourceCategories(resource, categories)), page);
        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    @RequestMapping("/{type}/hottest")
    public
    @ResponseBody
    List getHottestResources(@PathVariable String type) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getResourcesByHottest(resource, 5);
        String cachekey = String.format("%shottest", type);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return cacheDao.extractResourcesFromCache(resource, cachekey, CacheCollType.LIST);
    }

    @RequestMapping(value = "/video/getmusic/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getMusicByVideoId(@PathVariable Integer id) {
        String type = "music";
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Music targetMusic = ((Video) commonDao.getResourceById(Video.class, id)).getMusic();
        if (targetMusic != null) {
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Object object = commonDao.getResourceById(resource, music_id);
            cacheDao.insertSingleResource(resource, object, type);
            return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), type);
        } else {
            return new SingleMusic(-1, "", "", "", 0L);
        }
    }

    @RequestMapping(value = "/author/videos/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List getVideoListForAuthor(@PathVariable Integer id) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);
        String cachekey = String.format("authorvideos%d", id);

        String[] types = {"video", "tutorial"};

        for (String type : types) {
            Class resource = TypeClassMapping.typeClassMap.get(type);
            List resources = commonDao.getResourcesByFields(resource, conditions);

            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SET);
        }

        return cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.SET);
    }

    @RequestMapping(value = "/author/videos/{id}/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public List getVideoListForAuthorByPage(@PathVariable Integer id, @PathVariable Integer page) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        int pageSize = paginationDao.getResourcePageSize(Video.class);
        String cachekey = String.format("authorvideos%dpage%d", id, page);

        String[] types = {"video", "tutorial"};

        for (String type : types) {
            Class resource = TypeClassMapping.typeClassMap.get(type);
            List resources = commonDao.getResourcesByFields(resource, conditions);

            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        }

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        return cacheDao.extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.LIST, start, end);
    }

    //http://localhost:8080/darfoobackend/rest/cache/{type}/search?search=s
    @RequestMapping(value = "/{type}/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List searchResource(@PathVariable String type, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);

        String cachekey = String.format("%ssearch%s", type, searchContent);

        if (type.equals("video")) {
            String[] types = {"video", "tutorial"};

            for (String innertype : types) {
                Class resource = TypeClassMapping.typeClassMap.get(innertype);
                List resources = cacheDao.getSearchResourcesWithAuthor(resource, searchContent);

                cacheDao.insertResourcesIntoCache(resource, resources, cachekey, innertype, CacheCollType.LIST);
            }
        } else if (type.equals("music")) {
            Class resource = TypeClassMapping.typeClassMap.get(type);
            List resources = commonDao.getResourceBySearch(resource, searchContent);

            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        } else {
            System.out.println("wired");
        }

        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    @RequestMapping(value = "/{type}/search/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List searchResourceByPage(@PathVariable String type, @PathVariable Integer page, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        Class resource = TypeClassMapping.typeClassMap.get(type);

        int pageSize = paginationDao.getResourcePageSize(resource);
        String cachekey = String.format("%ssearch%spage%d", type, searchContent, page);

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        if (type.equals("video")) {
            String[] types = {"video", "tutorial"};

            for (String innertype : types) {
                Class innerresource = TypeClassMapping.typeClassMap.get(innertype);
                List resources = cacheDao.getSearchResourcesWithAuthor(innerresource, searchContent);

                cacheDao.insertResourcesIntoCache(innerresource, resources, cachekey, innertype, CacheCollType.LIST);
            }
        } else if (type.equals("music")) {
            List resources = commonDao.getResourceBySearch(resource, searchContent);
            cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        } else {
            System.out.println("wired");
        }

        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST, start, end);
    }

    @RequestMapping(value = "/{type}/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getSidebarResources(@PathVariable String type, @PathVariable Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getSideBarResources(resource, id);
        String cachekey = String.format("%ssidebar%d", type, id);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return cacheDao.extractResourcesFromCache(resource, cachekey, CacheCollType.LIST);
    }
}