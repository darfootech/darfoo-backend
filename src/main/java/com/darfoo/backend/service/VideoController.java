package com.darfoo.backend.service;

import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjh on 14-11-16.
 */

@Controller
@RequestMapping("/resources/video")
public class VideoController {
    @Autowired
    VideoDao videoDao;

    //http://localhost:8080/darfoobackend/rest/resources/video/3
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo getSingleVideo(@PathVariable String id){
        Video targetVideo = videoDao.getVideoByVideoId(Integer.parseInt(id));
        int video_id = targetVideo.getId();
        String video_url = targetVideo.getVideo_key();
        String video_title = targetVideo.getTitle();
        return new SingleVideo(video_id, video_title, video_url);
    }

    @RequestMapping("/recommend")
    public @ResponseBody
    List<IndexVideo> getRecmmendVideos(){
        List<Video> recommendVideos = videoDao.getRecommendVideos(3);
        List<IndexVideo> result = new ArrayList<IndexVideo>();
        for (Video video : recommendVideos){
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String video_title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new IndexVideo(video_id, video_title, image_url, update_timestamp));
        }
        return result;
    }

    @RequestMapping("/index")
    public @ResponseBody
    List<IndexVideo> getIndexVideos(){
        List<Video> latestVideos = videoDao.getLatestVideos(5);
        List<IndexVideo> result = new ArrayList<IndexVideo>();
        for (Video video : latestVideos){
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String video_title = video.getTitle();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new IndexVideo(video_id, video_title, image_url, update_timestamp));
        }
        return result;
    }
}
