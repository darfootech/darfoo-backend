package com.springapp.mvc;

import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zjh on 14-12-30.
 */

@Controller
@RequestMapping("/akkatest")
public class AkkaController {

    /*@RequestMapping(value = "/nocache/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo akkaNoCache(@PathVariable Integer id){
    }*/
}
