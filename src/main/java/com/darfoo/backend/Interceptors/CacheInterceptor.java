package com.darfoo.backend.Interceptors;

import com.darfoo.backend.caches.client.CommonRedisClient;
import com.darfoo.backend.caches.dao.MusicCacheDao;
import com.darfoo.backend.caches.dao.TutorialCacheDao;
import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.MusicDao;
import com.darfoo.backend.dao.resource.TutorialDao;
import com.darfoo.backend.dao.resource.VideoDao;
import com.darfoo.backend.model.resource.Music;
import com.darfoo.backend.model.resource.Tutorial;
import com.darfoo.backend.model.resource.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjh on 14-12-18.
 */
public class CacheInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    VideoDao videoDao;
    @Autowired
    TutorialDao tutorialDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    VideoCacheDao videoCacheDao;
    @Autowired
    TutorialCacheDao tutorialCacheDao;
    @Autowired
    MusicCacheDao musicCacheDao;
    @Autowired
    CommonRedisClient commonRedisClient;
    @Autowired
    CommonDao commonDao;

    //截取数字
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println("current cache uri is: " + uri + "\n");

        /*将单一资源的请求结果放入缓存*/
        if (uri.matches("(.*)/resources/video/\\d+$")) {
            String videoid = getNumbers(uri);
            System.out.println("video cache id is: " + videoid + "\n");
            int vid = Integer.parseInt(videoid);
            String key = "video-" + vid;
            boolean isExists = commonRedisClient.exists(key);
            if (isExists) {
                System.out.println("resource already in cache");
                response.sendRedirect(request.getContextPath() + "/rest/cache/video/" + vid);
            } else {
                System.out.println("resource not in cache");
                Video video = (Video) commonDao.getResourceById(Video.class, vid);
                videoCacheDao.insertSingleVideo(video);
            }
            return true;
        }

        if (uri.matches("(.*)/resources/video/tutorial/\\d+$")) {
            String tutorialid = getNumbers(uri);
            System.out.println("tutorial cache id is: " + tutorialid + "\n");
            int tid = Integer.parseInt(tutorialid);
            String key = "tutorial-" + tid;
            boolean isExists = commonRedisClient.exists(key);
            if (isExists) {
                System.out.println("resource already in cache");
                response.sendRedirect(request.getContextPath() + "/rest/cache/tutorial/" + tid);
            } else {
                System.out.println("resource not in cache");
                Tutorial tutorial = (Tutorial) commonDao.getResourceById(Tutorial.class, tid);
                tutorialCacheDao.insertSingleTutorial(tutorial);
            }
            return true;
        }

        if (uri.matches("(.*)/resources/music/\\d+$")) {
            String musicid = getNumbers(uri);
            System.out.println("music cache id is: " + musicid + "\n");
            int mid = Integer.parseInt(musicid);
            String key = "music-" + mid;
            boolean isExists = commonRedisClient.exists(key);
            if (isExists) {
                System.out.println("resource already in cache");
                response.sendRedirect(request.getContextPath() + "/rest/cache/music/" + mid);
            } else {
                System.out.println("resource not in cache");
                Music music = (Music) commonDao.getResourceById(Music.class, mid);
                musicCacheDao.insertSingleMusic(music);
            }
            return true;
        }
        /*end of single resource cache*/

        System.out.print("no cache resource clicked" + "\n");
        /*这里如果不是true就会无法显示页面*/
        return true;
    }
}
