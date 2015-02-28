package com.darfoo.backend.service;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.PaginationDao;
import com.darfoo.backend.dao.cota.CategoryDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.cota.RecommendDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.model.resource.Author;
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
    CommonRedisClient redisClient;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RecommendDao recommendDao;
    @Autowired
    PaginationDao paginationDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    VideoCates videoCates;
    @Autowired
    TutorialCates tutorialCates;
    @Autowired
    MusicCates musicCates;

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public @ResponseBody Object getSingleResourceFromCache(@PathVariable String type, @PathVariable Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        Object object = commonDao.getResourceById(resource, id);
        cacheDao.insertSingleResource(resource, object, type);
        return cacheDao.getSingleResource(TypeClassMapping.cacheResponseMap.get(type), String.format("%s-%d", type, id));
    }

    private void insertResourcesIntoCache(Class insertclass, List resources, String cachekey, String prefix, CacheCollType type) {
        for (Object object : resources) {
            int id = (Integer) commonDao.getResourceAttr(insertclass, object, "id");
            long status = 0L;
            if (type == CacheCollType.SET) {
                status = redisClient.sadd(cachekey, String.format("%s-%d", prefix, id));
            } else if (type == CacheCollType.LIST) {
                status = redisClient.lpush(cachekey, String.format("%s-%d", prefix, id));
            } else {
                System.out.println("wired");
            }
            System.out.println("insert result -> " + status);

            boolean result = cacheDao.insertSingleResource(insertclass, object, prefix);
            System.out.println("insert result -> " + result);
        }
    }

    private List extractResourcesFromCache(Class responseclass, String cachekey, CacheCollType type, Long... points) {
        Collection<String> keys;
        if (type == CacheCollType.SET) {
            keys = redisClient.smembers(cachekey);
        } else if (type == CacheCollType.LIST) {
            if (points.length == 0) {
                keys = redisClient.lrange(cachekey, 0L, -1L);
            } else {
                keys = redisClient.lrange(cachekey, points[0], points[1]);
            }
        } else {
            System.out.println("wired");
            keys = new ArrayList<String>();
        }
        List result = new ArrayList();
        for (String key : keys) {
            System.out.println("key -> " + key);
            result.add(cacheDao.getSingleResource(responseclass, key));
        }
        return result;
    }

    private List<String> parseResourceCategories(Class resource, String categories) {
        String[] requestCategories = categories.split("-");
        List<String> targetCategories = new ArrayList<String>();

        if (resource == Video.class) {
            if (!requestCategories[0].equals("0")) {
                String speedCate = videoCates.getSpeedCategory().get(requestCategories[0]);
                targetCategories.add(speedCate);
            }
            if (!requestCategories[1].equals("0")) {
                String difficultyCate = videoCates.getDifficultyCategory().get(requestCategories[1]);
                targetCategories.add(difficultyCate);
            }
            if (!requestCategories[2].equals("0")) {
                String styleCate = videoCates.getStyleCategory().get(requestCategories[2]);
                targetCategories.add(styleCate);
            }
            if (!requestCategories[3].equals("0")) {
                String letterCate = requestCategories[3];
                targetCategories.add(letterCate);
            }
        } else if (resource == Tutorial.class) {
            if (!requestCategories[0].equals("0")) {
                String speedCate = tutorialCates.getSpeedCategory().get(requestCategories[0]);
                targetCategories.add(speedCate);
            }
            if (!requestCategories[1].equals("0")) {
                String difficultyCate = tutorialCates.getDifficultyCategory().get(requestCategories[1]);
                targetCategories.add(difficultyCate);
            }
            if (!requestCategories[2].equals("0")) {
                String styleCate = tutorialCates.getStyleCategory().get(requestCategories[2]);
                targetCategories.add(styleCate);
            }
        } else if (resource == Music.class) {
            if (!requestCategories[0].equals("0")) {
                String beatCate = musicCates.getBeatCategory().get(requestCategories[0]);
                targetCategories.add(beatCate);
            }
            if (!requestCategories[1].equals("0")) {
                String styleCate = musicCates.getStyleCategory().get(requestCategories[1]);
                targetCategories.add(styleCate);
            }
            if (!requestCategories[2].equals("0")) {
                String letterCate = requestCategories[2];
                targetCategories.add(letterCate);
            }
        } else {
            System.out.println("wired");
        }

        return targetCategories;
    }

    private List getSearchResourcesWithAuthor(Class resource, String searchContent) {
        List objects = commonDao.getResourceBySearch(resource, searchContent);

        List<Author> authors = commonDao.getResourceBySearch(Author.class, searchContent);
        for (Author author : authors) {
            int aid = author.getId();
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("author_id", aid);

            List authorObjects = commonDao.getResourcesByFields(resource, conditions);
            objects.addAll(authorObjects);
        }

        return objects;
    }

    @RequestMapping(value = "/video/recommend", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheRecmmendVideos() {
        String cachekey = "recommend";
        String videoPrefix = String.format("%svideo", cachekey);
        String tutorialPrefix = String.format("%stutorial", cachekey);

        List recommendVideos = recommendDao.getRecommendResources(Video.class);
        List recommendTutorials = recommendDao.getRecommendResources(Tutorial.class);

        insertResourcesIntoCache(Video.class, recommendVideos, videoPrefix, videoPrefix, CacheCollType.SET);
        insertResourcesIntoCache(Tutorial.class, recommendTutorials, tutorialPrefix, tutorialPrefix, CacheCollType.SET);

        return extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.SET);
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

        insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    @RequestMapping(value = "/{type}/index/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheIndexResourcesByPage(@PathVariable String type, @PathVariable Integer page) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        String cachekey = String.format("%sindexpage%d", type, page);

        List resources = authorDao.getAuthorsOrderByVideoCountDesc();

        insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }


    @RequestMapping(value = "/{type}/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategories(@PathVariable String type, @PathVariable String categories) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%s", type, categories);

        List resources = categoryDao.getResourcesByCategories(resource, ServiceUtils.convertList2Array(parseResourceCategories(resource, categories)));
        insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SET);
        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SET);
    }

    @RequestMapping(value = "/{type}/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategoriesByPage(@PathVariable String type, @PathVariable String categories, @PathVariable Integer page) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%spage%d", type, categories, page);

        List resources = paginationDao.getResourcesByCategoriesByPage(resource, ServiceUtils.convertList2Array(parseResourceCategories(resource, categories)), page);
        insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    @RequestMapping("/{type}/hottest")
    public
    @ResponseBody
    List getHottestResources(@PathVariable String type) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getResourcesByHottest(resource, 5);
        String cachekey = String.format("%shottest", type);

        insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return extractResourcesFromCache(resource, cachekey, CacheCollType.LIST);
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

            insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.SET);
        }

        return extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.SET);
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

            insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        }

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        return extractResourcesFromCache(SingleVideo.class, cachekey, CacheCollType.LIST, start, end);
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
            List videos = getSearchResourcesWithAuthor(Video.class, searchContent);
            List tutorials = getSearchResourcesWithAuthor(Tutorial.class, searchContent);

            insertResourcesIntoCache(Video.class, videos, cachekey, "video", CacheCollType.LIST);
            insertResourcesIntoCache(Tutorial.class, tutorials, cachekey, "tutorial", CacheCollType.LIST);
        } else if (type.equals("music")) {
            Class resource = TypeClassMapping.typeClassMap.get(type);
            List resources = commonDao.getResourceBySearch(resource, searchContent);

            insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        } else {
            System.out.println("wired");
        }

        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
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
            List videos = getSearchResourcesWithAuthor(Video.class, searchContent);
            List tutorials = getSearchResourcesWithAuthor(Tutorial.class, searchContent);

            insertResourcesIntoCache(Video.class, videos, cachekey, "video", CacheCollType.LIST);
            insertResourcesIntoCache(Tutorial.class, tutorials, cachekey, "tutorial", CacheCollType.LIST);
        } else if (type.equals("music")) {
            List resources = commonDao.getResourceBySearch(resource, searchContent);
            insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);
        } else {
            System.out.println("wired");
        }

        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST, start, end);
    }

    @RequestMapping(value = "/{type}/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getSidebarResources(@PathVariable String type, @PathVariable Integer id) {
        Class resource = TypeClassMapping.typeClassMap.get(type);
        List resources = commonDao.getSideBarResources(resource, id);
        String cachekey = String.format("%ssidebar%d", type, id);

        insertResourcesIntoCache(resource, resources, cachekey, type, CacheCollType.LIST);

        return extractResourcesFromCache(resource, cachekey, CacheCollType.LIST);
    }
}