package com.darfoo.backend.service;

import com.darfoo.backend.caches.dao.CacheDao;
import com.darfoo.backend.caches.dao.CacheUtils;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zjh on 14-12-18.
 */

@Controller
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    CacheDao cacheDao;
    @Autowired
    DanceGroupDao danceGroupDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    CacheUtils cacheUtils;

    /**
     * 根据id获取某一类资源的单个记录
     *
     * @param type
     * @param id
     * @return
     */
    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getSingleResourceFromCache(@PathVariable String type, @PathVariable Integer id) {
        return cacheUtils.cacheSingleResource(type, id);
    }

    /**
     * 获取首页推荐的舞蹈视频
     *
     * @return
     */
    @RequestMapping(value = "/{type}/recommend", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheRecommendVideos(@PathVariable String type) {
        return cacheUtils.cacheRecommendResources(type);
    }

    /**
     * 获取首页最新的舞蹈视频
     * 获取所有的舞队(与老版本的launcher兼容)
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/{type}/index", method = RequestMethod.GET)
    public
    @ResponseBody
    List cacheIndexResources(@PathVariable String type) {
        return cacheUtils.cacheIndexResources(type);
    }

    @RequestMapping(value = "{type}/newest", method = RequestMethod.GET)
    public @ResponseBody List cacheNewestResources(@PathVariable String type){
        return cacheUtils.cacheNewestResources(type);
    }

    /**
     * 根据类别获取资源
     *
     * @param type
     * @param category
     * @return
     */
    @RequestMapping(value = "/{type}/category/{category}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategories(@PathVariable String type, @PathVariable String category) {
        return cacheUtils.cacheResourcesByCategory(type, category);
    }

    /**
     * 根据类别获取资源分页
     *
     * @param type
     * @param category
     * @param page
     * @return
     */
    @RequestMapping(value = "/{type}/category/{category}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByCategoriesByPage(@PathVariable String type, @PathVariable String category, @PathVariable Integer page) {
        return cacheUtils.cacheResourcesByCategory(type, category, page);
    }

    /**
     * 根据子类型获取某一类资源
     *
     * @param type
     * @param innertype
     * @return
     */
    @RequestMapping(value = "{type}/innertype/{innertype}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByInnertype(@PathVariable String type, @PathVariable String innertype) {
        return cacheUtils.cacheResourcesByInnertype(type, innertype);
    }

    /**
     * 根据子类型获取某一类资源分页
     *
     * @param type
     * @param innertype
     * @return
     */
    @RequestMapping(value = "{type}/innertype/{innertype}/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getResourcesByInnertypeByPage(@PathVariable String type, @PathVariable String innertype, Integer page) {
        return cacheUtils.cacheResourcesByInnertype(type, innertype, page);
    }

    /**
     * 获取热门资源 暂时只有热门舞队
     *
     * @param type
     * @return
     */
    @RequestMapping("/{type}/hot")
    public
    @ResponseBody
    List getHottestResources(@PathVariable String type) {
        return cacheUtils.cacheHotResources(type);
    }

    /**
     * 获取热门资源 分页 暂时只有热门舞队
     *
     * @param type
     * @return
     */
    @RequestMapping("/{type}/hot/page/{page}")
    public
    @ResponseBody
    List getHottestResourcesByPage(@PathVariable String type, @PathVariable Integer page) {
        return cacheUtils.cacheHotResources(type, page);
    }

    /**
     * 根据舞蹈视频获取对应的舞队伴奏
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/dancevideo/getmusic/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getMusicByVideoId(@PathVariable Integer id) {
        return cacheUtils.cacheDanceMusicForDanceVideo(id);
    }

    /**
     * 获取某一个舞队下的所有舞蹈视频
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/dancegroup/videos/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List getVideoListForAuthor(@PathVariable Integer id) {
        return cacheUtils.cacheDanceGroupVideos(id);
    }

    /**
     * 获取某一个舞队下的所有舞蹈视频分页
     *
     * @param id
     * @param page
     * @return
     */
    @RequestMapping(value = "/dancegroup/videos/{id}/page/{page}", method = RequestMethod.GET)
    @ResponseBody
    public List getVideoListForAuthorByPage(@PathVariable Integer id, @PathVariable Integer page) {
        return cacheUtils.cacheDanceGroupVideos(id, page);
    }


    /**
     * 搜索内容
     *
     * @param type
     * @param request
     * @return
     * @example http://localhost:8080/darfoobackend/rest/cache/{type}/search?search=s
     */
    @RequestMapping(value = "/{type}/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List searchResource(@PathVariable String type, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);

        return cacheUtils.cacheResourcesBySearch(type, searchContent);
    }

    /**
     * 搜索内容分页
     *
     * @param type
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/{type}/search/page/{page}", method = RequestMethod.GET)
    public
    @ResponseBody
    List searchResourceByPage(@PathVariable String type, @PathVariable Integer page, HttpServletRequest request) {
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);

        return cacheUtils.cacheResourcesBySearch(type, searchContent, page);
    }

    /**
     * 获取播放界面侧边资源列表
     *
     * @param type
     * @param id
     * @return
     */
    @RequestMapping(value = "/{type}/sidebar/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List getSidebarResources(@PathVariable String type, @PathVariable Integer id) {
        return cacheUtils.cacheSidebarResources(type, id);
    }
}