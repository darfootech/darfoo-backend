package com.darfoo.backend.service;

/**
 * Created by zjh on 14-11-16.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/resources/image")
public class ImageController {
    @RequestMapping(value = "/showurl/{key}", method = RequestMethod.GET)
    public @ResponseBody
    String getSingleVideo(@PathVariable String key){
        return "http://imageurl";
    }
}
