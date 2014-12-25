package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Education;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.IndexAuthor;
import com.darfoo.backend.service.responsemodel.SingleAuthor;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resources/author")
public class AuthorController {
    @Autowired
    AuthorDao authorDao;
    @Autowired
    VideoDao videoDao;
    @Autowired
    EducationDao educationDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleAuthor getSingleAuthor(@PathVariable String id){
        Author targetAuthor = authorDao.getAuthor(Integer.parseInt(id));
        String name = targetAuthor.getName();
        String description = targetAuthor.getDescription();
        String image_url = "";
        if (targetAuthor.getImage() != null){
            image_url = targetAuthor.getImage().getImage_key();
        }
        String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
        return new SingleAuthor(Integer.parseInt(id), image_download_url, name, description);
    }

    @RequestMapping("/index")
    public
    @ResponseBody
    List<IndexAuthor> getIndexAuthor(){
        List<Author> authors = authorDao.getAllAuthor();

        /*int returnCount = 7;
        List<Author> returnAuthors = new ArrayList<Author>();
        for (int i=0; i<returnCount; i++){
            returnAuthors.add(authors.get(i));
        }*/

        List<IndexAuthor> result = new ArrayList<IndexAuthor>();
        for (Author author : authors){
            int id = author.getId();
            String image_url = "";
            if (author.getImage() != null){
                image_url = author.getImage().getImage_key();
            }
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String title = author.getName();
            String description = author.getDescription();
            result.add(new IndexAuthor(id, image_download_url, title, description));
        }
        return result;
    }

    @RequestMapping(value = "/videos/{id}", method = RequestMethod.GET)
    @ResponseBody public List<SingleVideo> getVideoListForAuthor(@PathVariable String id){
        int aid = Integer.parseInt(id);

        Author author = authorDao.getAuthor(aid);
        String authorname = author.getName();

        List<SingleVideo> result = new ArrayList<SingleVideo>();
        List<Video> videos = videoDao.getVideosByAuthorId(aid);
        List<Education> tutorials = educationDao.getTutorialsByAuthorId(aid);

        for (Video video : videos){
            int vid = video.getId();
            String video_download_url = qiniuUtils.getQiniuResourceUrl(video.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(video.getImage().getImage_key());
            String title = video.getTitle();
            result.add(new SingleVideo(vid, title, authorname, video_download_url, image_download_url));
        }

        for (Education tutorial : tutorials){
            int tid = tutorial.getId();
            String tutorial_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getImage().getImage_key());
            String title = tutorial.getTitle();
            result.add(new SingleVideo(tid, title, authorname, tutorial_download_url, image_download_url));
        }

        return result;
    }
}
