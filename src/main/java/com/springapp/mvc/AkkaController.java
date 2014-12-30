package com.springapp.mvc;

import akka.actor.ActorSystem;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import scala.concurrent.ExecutionContext;

import scala.concurrent.duration.Duration;
import akka.japi.Function;
import java.util.concurrent.Callable;
import static akka.dispatch.Futures.future;
import static java.util.concurrent.TimeUnit.SECONDS;
import scala.concurrent.Future;
import scala.concurrent.Await;
import akka.dispatch.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.Promise;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContext$;

/**
 * Created by zjh on 14-12-30.
 */

@Controller
@RequestMapping("/akkatest")
public class AkkaController {
    final ActorSystem system = ActorSysContainer.getInstance().getSystem();
    final ExecutionContext ec = system.dispatcher();

    //=> 貌似没法直接把future中的结果response出来所以还是直接用play吧
    @RequestMapping(value = "/nocache/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String akkaNoCache(@PathVariable Integer id){
        Future<String> future = future(new Callable<String>() {
            public String call() {
                return "Hello" + "World";
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
        return "cleantha";
    }
}
