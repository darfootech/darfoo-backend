package com.darfoo.backend.Interceptors;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.MusicDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Music;
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
    VideoDao videoDao;
    @Autowired
    EducationDao educationDao;
    @Autowired
    MusicDao musicDao;

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

        if (uri.matches("(.*)/resources/video/\\d+$")){
            String videoid = getNumbers(uri);
            System.out.println("video clicked id is: " + videoid + "\n");
            int vid = Integer.parseInt(videoid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(videoDao.updateVideoHotest(vid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/video/tutorial/\\d+$")){
            String tutorialid = getNumbers(uri);
            System.out.println("tutorial clicked id is: " + tutorialid + "\n");
            int tid = Integer.parseInt(tutorialid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(educationDao.updateEducationHotest(tid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/music/\\d+$")){
            String musicid = getNumbers(uri);
            System.out.println("music clicked id is: " + musicid + "\n");
            int mid = Integer.parseInt(musicid);
            int acc = 1;
            System.out.println(CRUDEvent.getResponse(musicDao.updateMusicHotest(mid, acc)));
            return true;
        }

        if (uri.matches("(.*)/resources/video/getmusic/\\d+$")){
            int videoid = Integer.parseInt(getNumbers(uri));
            Music music = videoDao.getMusic(videoid);
            if (music != null){
                int musicid = music.getId();
                System.out.println("music clicked id is: " + musicid + "\n");
                int acc = 1;
                System.out.println(CRUDEvent.getResponse(musicDao.updateMusicHotest(musicid, acc)));
                return true;
            }else{
                System.out.println("no music connect to this video: " + videoid);
                return true;
            }
        }

        System.out.print("no resource clicked" + "\n");
        return true;
    }
}
