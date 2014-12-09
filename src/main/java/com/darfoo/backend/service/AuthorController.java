package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.dao.AuthorDao;
import com.darfoo.backend.model.Author;
import com.darfoo.backend.service.responsemodel.IndexAuthor;
import com.darfoo.backend.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resources/author")
public class AuthorController {
    @Autowired
    AuthorDao authorDao;

    QiniuUtils qiniuUtils = new QiniuUtils();

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    Author getSingleAuthor(@PathVariable String id){
        Author targetAuthor = authorDao.getAuthor(Integer.parseInt(id));
        return targetAuthor;
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
}
