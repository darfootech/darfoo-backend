package com.darfoo.backend.service;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Video;
import com.darfoo.backend.service.cota.CacheCollType;
import com.darfoo.backend.service.cota.TypeClassMapping;
import com.darfoo.backend.service.responsemodel.SingleMusic;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    CacheUtils cacheUtils;

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getSingleResourceFromCache(@PathVariable String type, @PathVariable Integer id) {
        return cacheUtils.cacheSingleResource(type, id);
    }

    @RequestMapping(value = "/video/recommend", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheRecommendVideos() {
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
        return cacheUtils.cacheIndexResources(type);
    }

    @RequestMapping(value = "/author/index/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheIndexResourcesByPage(@PathVariable Integer page) {
        String type = "author";
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sindexpage%d", type, page);

        List resources = authorDao.getAuthorsOrderByVideoCountDescByPage(page);

        cacheDao.insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return cacheDao.extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    @RequestMapping(value = "/{type}/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategories(@PathVariable String type, @PathVariable String categories) {
        return cacheUtils.cacheResourcesByCategories(type, categories);
    }

    @RequestMapping(value = "/{type}/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategoriesByPage(@PathVariable String type, @PathVariable String categories, @PathVariable Integer page) {
        return cacheUtils.cacheResourcesByCategoriesByPage(type, categories, page);
    }

    @RequestMapping("/{type}/hottest")
    public
    @ResponseBody
    List getHottestResources(@PathVariable String type) {
        return cacheUtils.cacheHottestResources(type);
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

        return cacheUtils.cacheResourcesBySearch(type, searchContent);
    }

    @RequestMapping(value = "/{type}/search/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List searchResourceByPage(@PathVariable String type, @PathVariable Integer page, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);

        return cacheUtils.cacheResourcesBySearch(type, searchContent, page);
    }

    @RequestMapping(value = "/{type}/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getSidebarResources(@PathVariable String type, @PathVariable Integer id) {
        return cacheUtils.cacheSidebarResources(type, id);
    }
}