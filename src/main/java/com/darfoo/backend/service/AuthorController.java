package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import com.darfoo.backend.model.Author;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/resources/author")
public class AuthorController {
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    Author getSingleAuthor(@PathVariable String id){
        Author targetAuthor = new Author();
        return targetAuthor;
    }

}
