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

    @RequestMapping(value = "/video/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheIndexVideos() {
        List latestVideos = commonDao.getResourcesByNewest(Video.class, 12);
        String cachekey = "videoindex";
        String prefix = "video";

        insertResourcesIntoCache(Video.class, latestVideos, cachekey, prefix, CacheCollType.SET);

        return extractResourcesFromCache(Video.class, cachekey, CacheCollType.SET);
    }

    @RequestMapping(value = "/{type}/category/{categories}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getVideosByCategories(@PathVariable String type, @PathVariable String categories) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%s", type, categories);
        String prefix = type;

        List resources = categoryDao.getResourcesByCategories(resource, ServiceUtils.convertList2Array(parseResourceCategories(resource, categories)));
        insertResourcesIntoCache(resource, resources, cachekey, prefix, CacheCollType.SET);
        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.SET);
    }

    @RequestMapping(value = "/{type}/category/{categories}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getVideosByCategoriesByPage(@PathVariable String type, @PathVariable String categories, @PathVariable Integer page) {
        Class resource = TypeClassMapping.typeClassMap.get(type);

        String cachekey = String.format("%scategory%spage%d", type, categories, page);
        String prefix = type;

        List resources = paginationDao.getResourcesByCategoriesByPage(resource, ServiceUtils.convertList2Array(parseResourceCategories(resource, categories)), page);
        insertResourcesIntoCache(resource, resources, cachekey, prefix, CacheCollType.LIST);
        return extractResourcesFromCache(TypeClassMapping.cacheResponseMap.get(type), cachekey, CacheCollType.LIST);
    }

    /*@RequestMapping("/music/hottest")
    public
    @ResponseBody
    List<SingleMusic> getHottestMusics() {
        List<Music> musics = commonDao.getResourcesByHottest(Music.class, 5);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.sadd("musichottest", "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        Set<String> hottestMusicKeys = redisClient.smembers("musichottest");
        for (String vkey : hottestMusicKeys) {
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }

    @RequestMapping(value = "/video/getmusic/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleMusic getMusicByVideoId(@PathVariable Integer id) {
        Music targetMusic = ((Video) commonDao.getResourceById(Video.class, id)).getMusic();
        if (targetMusic != null) {
            int music_id = targetMusic.getId();
            videoCacheDao.insertMusic(id, music_id);
            Music music = (Music) commonDao.getResourceById(Music.class, music_id);
            System.out.println(musicCacheDao.insertSingleMusic(music));
            return musicCacheDao.getSingleMusic(music_id);
        } else {
            return new SingleMusic(-1, "", "", "", 0L);
        }
    }

    @RequestMapping(value = "/author/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleAuthor cacheSingleAuthor(@PathVariable Integer id) {
        Author author = (Author) commonDao.getResourceById(Author.class, id);
        System.out.println(authorCacheDao.insertSingleAuthor(author));
        SingleAuthor result = authorCacheDao.getSingleAuthor(id);
        return result;
    }

    @RequestMapping(value = "/author/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleAuthor> cacheIndexAuthors() {
        List<Object[]> authorIdAndCnt = authorDao.getAuthorOrderByVideoCountDesc();
        for (Object[] rows : authorIdAndCnt) {
            int authorid = (Integer) rows[1];
            System.out.println(authorid + " -> " + ((BigInteger) rows[0]).intValue());
            Author author = (Author) commonDao.getResourceById(Author.class, authorid);
            long result = redisClient.lpush("authorindex", "author-" + authorid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        List<String> indexAuthorKeys = redisClient.lrange("authorindex", 0L, -1L);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys) {
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }
        return result;
    }

    @RequestMapping(value = "/author/index/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleAuthor> cacheIndexAuthorsByPage(@PathVariable Integer page) {
        List<Object[]> authorIdAndCnt = authorDao.getAuthorOrderByVideoCountDescByPage(page);
        for (Object[] rows : authorIdAndCnt) {
            int authorid = (Integer) rows[1];
            System.out.println(authorid + " -> " + ((BigInteger) rows[0]).intValue());
            Author author = (Author) commonDao.getResourceById(Author.class, authorid);
            long result = redisClient.lpush("authorindexpage" + page, "author-" + authorid);
            authorCacheDao.insertSingleAuthor(author);
            System.out.println("insert result -> " + result);
        }

        List<String> indexAuthorKeys = redisClient.lrange("authorindexpage" + page, 0L, -1L);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (String key : indexAuthorKeys) {
            System.out.println("key -> " + key);
            int id = Integer.parseInt(key.split("-")[1]);
            SingleAuthor author = authorCacheDao.getSingleAuthor(id);
            System.out.println("name -> " + author.getName());
            result.add(author);
        }
        return result;
    }

    @RequestMapping(value = "/author/videos/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<SingleVideo> getVideoListForAuthor(@PathVariable Integer id) {
        List<SingleVideo> result = new ArrayList<SingleVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        List<Tutorial> tutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);

        for (Video video : videos) {
            int vid = video.getId();
            long status = redisClient.sadd("authorvideos" + id, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Tutorial tutorial : tutorials) {
            int tid = tutorial.getId();
            long status = redisClient.sadd("authorvideos" + id, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        Set<String> authorVideoKeys = redisClient.smembers("authorvideos" + id);
        for (String key : authorVideoKeys) {
            System.out.println("key -> " + key);
            int vtid = Integer.parseInt(key.split("-")[1]);
            String vtflag = key.split("-")[0];
            if (vtflag.equals("video")) {
                SingleVideo video = videoCacheDao.getSingleVideo(vtid);
                result.add(video);
            } else if (vtflag.equals("tutorial")) {
                SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(vtid);
                result.add(tutorial);
            } else {
                System.out.println("something is wrong");
            }
        }

        return result;
    }

    @RequestMapping(value = "/author/videos/{id}/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public List<SingleVideo> getVideoListForAuthorByPage(@PathVariable Integer id, @PathVariable Integer page) {
        List<SingleVideo> result = new ArrayList<SingleVideo>();

        HashMap<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("author_id", id);

        List<Video> videos = commonDao.getResourcesByFields(Video.class, conditions);
        List<Tutorial> tutorials = commonDao.getResourcesByFields(Tutorial.class, conditions);

        int pageSize = 12;
        String rediskey = "authorvideos" + id + "page";

        for (Video video : videos) {
            int vid = video.getId();
            long status = redisClient.lpush(rediskey, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + status);
        }

        for (Tutorial tutorial : tutorials) {
            int tid = tutorial.getId();
            long status = redisClient.lpush(rediskey, "tutorial-" + tid);
            tutorialCacheDao.insertSingleTutorial(tutorial);
            System.out.println("insert result -> " + status);
        }

        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;

        List<String> authorVideoKeys = redisClient.lrange(rediskey, start, end);
        for (String key : authorVideoKeys) {
            System.out.println("key -> " + key);
            int vtid = Integer.parseInt(key.split("-")[1]);
            String vtflag = key.split("-")[0];
            if (vtflag.equals("video")) {
                SingleVideo video = videoCacheDao.getSingleVideo(vtid);
                result.add(video);
            } else if (vtflag.equals("tutorial")) {
                SingleVideo tutorial = tutorialCacheDao.getSingleTutorial(vtid);
                result.add(tutorial);
            } else {
                System.out.println("something is wrong");
            }
        }

        System.out.println("videolist size -> " + result.size());

        return result;
    }*/

    private List getSearchResourcesWithAuthor(Class resource, String searchContent) {
        List objects = commonDao.getResourceBySearch(resource, searchContent);

        List<Author> authors = commonDao.getResourceBySearch(Author.class, searchContent);
        for (Author author : authors) {
            int aid = author.getId();
            HashMap<String, Object> conditions = new HashMap<String, Object>();
            conditions.put("author_id", aid);

            List authorObjects = commonDao.getResourcesByFields(Video.class, conditions);
            objects.addAll(authorObjects);
        }

        return objects;
    }

    //http://localhost:8080/darfoobackend/rest/cache/{type}/search?search=s
    @RequestMapping(value = "/{type}/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List searchVideo(@PathVariable String type, HttpServletRequest request) {
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
    List searchVideoByPage(@PathVariable String type, @PathVariable Integer page, HttpServletRequest request) {
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

    /*@RequestMapping(value = "/video/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getSidebarVideos(@PathVariable Integer id) {
        List<Video> sidebarVideos = commonDao.getSideBarResources(Video.class, id);
        String rediskey = "videosidebar" + id;
        for (Video video : sidebarVideos) {
            int vid = video.getId();
            long result = redisClient.lpush(rediskey, "video-" + vid);
            videoCacheDao.insertSingleVideo(video);
            System.out.println("insert result -> " + result);
        }
        List<String> sideBarVideoKeys = redisClient.lrange(rediskey, 0L, -1L);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : sideBarVideoKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = videoCacheDao.getSingleVideo(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/tutorial/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleVideo> getSidebarTutorials(@PathVariable Integer id) {
        List<Tutorial> sidebarTutorials = commonDao.getSideBarResources(Tutorial.class, id);
        String rediskey = "tutorialsidebar" + id;
        for (Tutorial video : sidebarTutorials) {
            int vid = video.getId();
            long result = redisClient.lpush(rediskey, "tutorial-" + vid);
            tutorialCacheDao.insertSingleTutorial(video);
            System.out.println("insert result -> " + result);
        }

        List<String> sidebarTutorialKeys = redisClient.lrange(rediskey, 0L, -1L);
        List<SingleVideo> result = new ArrayList<SingleVideo>();
        for (String vkey : sidebarTutorialKeys) {
            System.out.println("vkey -> " + vkey);
            int vid = Integer.parseInt(vkey.split("-")[1]);
            SingleVideo video = tutorialCacheDao.getSingleTutorial(vid);
            System.out.println("title -> " + video.getTitle());
            result.add(video);
        }

        return result;
    }

    @RequestMapping(value = "/music/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SingleMusic> getSidebarMusics(@PathVariable Integer id) {
        List<Music> musics = commonDao.getSideBarResources(Music.class, id);
        List<SingleMusic> result = new ArrayList<SingleMusic>();
        String rediskey = "musicsidebar" + id;
        for (Music music : musics) {
            int mid = music.getId();
            long status = redisClient.lpush(rediskey, "music-" + mid);
            musicCacheDao.insertSingleMusic(music);
            System.out.println("insert result -> " + status);
        }

        List<String> sidebarMusicKeys = redisClient.lrange(rediskey, 0L, -1L);
        for (String vkey : sidebarMusicKeys) {
            System.out.println("vkey -> " + vkey);
            int mid = Integer.parseInt(vkey.split("-")[1]);
            SingleMusic music = musicCacheDao.getSingleMusic(mid);
            System.out.println("title -> " + music.getTitle());
            result.add(music);
        }

        return result;
    }*/
}
