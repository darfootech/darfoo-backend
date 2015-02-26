package com.darfoo.backend.Interceptors;

import com.darfoo.backend.dao.*;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.AuthorDao;
import com.darfoo.backend.dao.resource.MusicDao;
import com.darfoo.backend.dao.resource.TutorialDao;
import com.darfoo.backend.model.resource.Author;
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
 * Created by zjh on 14-12-13.
 */

/*用来拦截请求单个视频，伴奏，教程资源的点击，用来累计每一个资源记录的点击量，从而综合更新日期和点击量做热门推荐*/
public class HotClickInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    TutorialDao tutorialDao;
    @Autowired
    MusicDao musicDao;
    @Autowired
    AuthorDao authorDao;
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
        System.out.println("current uri is: " + uri + "\n");

        if (uri.matches("(.*)/resources/video/\\d+$")) {
            String videoid = getNumbers(uri);
            System.out.println("video clicked id is: " + videoid + "\n");
            int vid = Integer.parseInt(videoid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Video.class, vid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/video/tutorial/\\d+$")) {
            String tutorialid = getNumbers(uri);
            System.out.println("tutorial clicked id is: " + tutorialid + "\n");
            int tid = Integer.parseInt(tutorialid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Tutorial.class, tid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/music/\\d+$")) {
            String musicid = getNumbers(uri);
            System.out.println("music clicked id is: " + musicid + "\n");
            int mid = Integer.parseInt(musicid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Music.class, mid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/author/\\d+$")) {
            String authorid = getNumbers(uri);
            System.out.println("author clicked id is: " + authorid + "\n");
            int aid = Integer.parseInt(authorid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Author.class, aid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/video/getmusic/\\d+$")) {
            int videoid = Integer.parseInt(getNumbers(uri));
            Music music = ((Video) commonDao.getResourceById(Video.class, videoid)).getMusic();
            if (music != null) {
                int musicid = music.getId();
                System.out.println("music clicked id is: " + musicid + "\n");
                int acc = 1;
                System.out.println(CRUDEvent.getResponse(commonDao.updateResourceHottest(Music.class, musicid, acc)));
                return true;
            } else {
                System.out.println("no music connect to this video: " + videoid);
                return true;
            }
        }

        System.out.print("no resource clicked" + "\n");
        return true;
    }
}
