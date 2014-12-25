package com.springapp.mvc;

import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/test")
public class HelloController {
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hey Cleantha!");
        return "hello";
	}

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    @ResponseBody public Map<String, Object> returnJson(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", true);
        map.put("video", new SingleVideo(3, "title", "ccc", "videourl", "imageurl"));
        return map;
    }

    @RequestMapping(value = "/jsonarray", method = RequestMethod.GET)
    @ResponseBody public List<Map<String, Object>> returnJsonArray(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", true);
        map.put("video", new SingleVideo(3, "title", "ccc", "videourl", "imageurl"));
        list.add(map);
        list.add(map);
        list.add(map);
        return list;
    }
}
