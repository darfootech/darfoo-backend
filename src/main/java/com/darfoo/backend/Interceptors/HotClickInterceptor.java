package com.darfoo.backend.Interceptors;

import com.darfoo.backend.dao.CRUDEvent;
import com.darfoo.backend.dao.cota.CommonDao;
import com.darfoo.backend.dao.resource.DanceGroupDao;
import com.darfoo.backend.model.resource.dance.DanceMusic;
import com.darfoo.backend.model.resource.dance.DanceVideo;
import com.darfoo.backend.service.cota.TypeClassMapping;
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
    DanceGroupDao authorDao;
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

    String hottestField = "hottest";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println("current uri is: " + uri + "\n");

        String[] types = {"video", "tutorial", "music", "author"};

        for (String type : types) {
            if (uri.matches(String.format("(.*)/resources/%s/\\d+$", type))) {
                Integer id = Integer.parseInt(getNumbers(uri));
                System.out.println(String.format("%s clicked id is: %d\n", type, id));
                System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(TypeClassMapping.typeClassMap.get(type), id, hottestField)));
                return true;
            }
        }

        if (uri.matches("(.*)/resources/video/getmusic/\\d+$")) {
            int videoid = Integer.parseInt(getNumbers(uri));
            DanceMusic music = ((DanceVideo) commonDao.getResourceById(DanceVideo.class, videoid)).getMusic();
            if (music != null) {
                int musicid = music.getId();
                System.out.println("music clicked id is: " + musicid + "\n");
                System.out.println(CRUDEvent.getResponse(commonDao.incResourceField(DanceMusic.class, musicid, hottestField)));
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
