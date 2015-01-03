package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.dao.EducationDao;
import com.darfoo.backend.dao.SearchDao;
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

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    SearchDao searchDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleAuthor getSingleAuthor(@PathVariable Integer id){
        Author targetAuthor = authorDao.getAuthor(id);
        String name = targetAuthor.getName();
        String description = targetAuthor.getDescription();
        String image_url = "";
        if (targetAuthor.getImage() != null){
            image_url = targetAuthor.getImage().getImage_key();
        }
        String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
        return new SingleAuthor(id, name, description, image_download_url);
    }

    @RequestMapping("/index")
    public
    @ResponseBody
    List<SingleAuthor> getIndexAuthor(){
        List<Author> authors = authorDao.getAllAuthor();

        /*int returnCount = 7;
        List<Author> returnAuthors = new ArrayList<Author>();
        for (int i=0; i<returnCount; i++){
            returnAuthors.add(authors.get(i));
        }*/

        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (Author author : authors){
            int id = author.getId();
            String image_url = "";
            if (author.getImage() != null){
                image_url = author.getImage().getImage_key();
            }
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            String title = author.getName();
            String description = author.getDescription();
            result.add(new SingleAuthor(id, title, description, image_download_url));
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
            long update_timestamp = video.getUpdate_timestamp();
            result.add(new SingleVideo(vid, title, authorname, video_download_url, image_download_url, update_timestamp));
        }

        for (Education tutorial : tutorials){
            int tid = tutorial.getId();
            String tutorial_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getVideo_key());
            String image_download_url = qiniuUtils.getQiniuResourceUrl(tutorial.getImage().getImage_key());
            String title = tutorial.getTitle();
            long update_timestamp = tutorial.getUpdate_timestamp();
            result.add(new SingleVideo(tid, title, authorname, tutorial_download_url, image_download_url, update_timestamp));
        }

        return result;
    }

    //http://localhost:8080/darfoobackend/rest/resources/author/search?search=heart
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    List<SingleAuthor> searchAuthor(HttpServletRequest request){
        String searchContent = request.getParameter("search");
        System.out.println(searchContent);
        List<Author> authors = searchDao.getAuthorBySearch(searchContent);
        List<SingleAuthor> result = new ArrayList<SingleAuthor>();
        for (Author author : authors){
            int id = author.getId();
            String name = author.getName();
            String description = author.getDescription();
            String image_url = author.getImage().getImage_key();
            String image_download_url = qiniuUtils.getQiniuResourceUrl(image_url);
            result.add(new SingleAuthor(id, name, description, image_download_url));
        }
        return result;
    }
}
