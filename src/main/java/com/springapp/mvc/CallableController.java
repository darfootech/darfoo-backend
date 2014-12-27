package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by zjh on 14-12-27.
 */

@Controller
@RequestMapping("/testasync")
public class CallableController {
    @RequestMapping("/responsebody")
    public @ResponseBody
    Callable<Map<String, Object>> callable() {
        return new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                //just did the block stuff and it's asynchronize
                //Thread.sleep(2000);
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", "cleantha");
                return result;
            }
        };
    }

    @RequestMapping("/normalresponse")
    public @ResponseBody
    Map<String, Object> normalresponse() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", "cleantha");
        return result;
    }
}
