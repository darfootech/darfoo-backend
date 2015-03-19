package com.darfoo.backend.service;

import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import com.darfoo.backend.service.cota.ActorSysContainer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.concurrent.Callable;

import static akka.dispatch.Futures.future;

/**
 * Created by zjh on 15-3-19.
 */

@Controller
public class AkkaController {
    final ActorSystem system = ActorSysContainer.getInstance().getSystem();
    final ExecutionContext ec = system.dispatcher();

    //=> 貌似没法直接把future中的结果response出来所以还是直接用play吧 但是可以解决一些异步上传的问题
    @RequestMapping(value = "/resources/akkaasync", method = RequestMethod.GET)
    public String akkaAsync() {
        Future<String> future = future(new Callable<String>() {
            public String call() {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "cleantha";
            }
        }, system.dispatcher());

        future.onComplete(new OnComplete<String>() {
            public void onComplete(Throwable failure, String result) {
                if (failure != null) {
                    //We got a failure, handle it here
                    System.out.println("we got a failure");
                    //return "error";
                } else {
                    // We got a result, do something with it
                    System.out.println("result is: " + result);
                }
            }
        }, ec);
        return "success";
    }
}
