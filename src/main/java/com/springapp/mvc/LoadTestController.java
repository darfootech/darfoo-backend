package com.springapp.mvc;

import com.darfoo.backend.caches.dao.VideoCacheDao;
import com.darfoo.backend.dao.VideoDao;
import com.darfoo.backend.model.Video;
import com.darfoo.backend.service.responsemodel.IndexVideo;
import com.darfoo.backend.service.responsemodel.SingleVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjh on 14-12-28.
 */

@Controller
@RequestMapping("/loadtest")
public class LoadTestController {
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    VideoDao videoDao;
    @Autowired
    VideoCacheDao videoCacheDao;

    private final Queue<DeferredResult<SingleVideo>> responseQueue = new ConcurrentLinkedQueue<DeferredResult<SingleVideo>>();

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/normal/nocache/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    19.30ms   35.74ms 185.09ms   91.07%
        Req/Sec   209.38    112.15   586.00     69.38%
      8261 requests in 10.00s, 2.11MB read
    Requests/sec:    826.02
    Transfer/sec:    216.36KB
    */
    @RequestMapping(value = "/normal/nocache/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo normalnocache(@PathVariable String id) {
        Video targetVideo = videoDao.getVideoByVideoId(Integer.parseInt(id));
        int video_id = targetVideo.getId();
        String video_url = targetVideo.getVideo_key();
        String image_url = targetVideo.getImage().getImage_key();
        String video_title = targetVideo.getTitle();
        String author_name = "";
        if (targetVideo.getAuthor() != null){
            author_name = targetVideo.getAuthor().getName();
        }
        return new SingleVideo(video_id, video_title, author_name, video_url, image_url);
    }

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/index/nocache
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    22.91ms   28.02ms 177.87ms   88.25%
        Req/Sec   122.74     54.78   340.00     70.91%
      4867 requests in 10.00s, 5.41MB read
    Requests/sec:    486.65
    Transfer/sec:    554.33KB
    */
    @RequestMapping(value = "/index/nocache", method = RequestMethod.GET)
    public
    @ResponseBody
    List<IndexVideo> indexnocache() {
        List<Video> latestVideos = videoDao.getVideosByNewest(7);
        List<IndexVideo> result = new ArrayList<IndexVideo>();
        for (Video video : latestVideos) {
            int video_id = video.getId();
            String image_url = video.getImage().getImage_key();
            String video_title = video.getTitle();
            String author_name = "";
            if (video.getAuthor() != null){
                author_name = video.getAuthor().getName();
            }
            String video_url = video.getVideo_key();
            Long update_timestamp = video.getUpdate_timestamp();
            result.add(new IndexVideo(video_id, video_title, image_url, video_url, author_name, update_timestamp));
        }
        return result;
    }

    //=> 和normal差不都
    @RequestMapping(value = "/yanormal/nocache/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SingleVideo yanormalnocache(@PathVariable Integer id) {
        Video targetVideo = videoDao.getVideoByVideoId(id);
        String video_url = targetVideo.getVideo_key();
        String image_url = targetVideo.getImage().getImage_key();
        String video_title = targetVideo.getTitle();
        String author_name = "";
        if (targetVideo.getAuthor() != null){
            author_name = targetVideo.getAuthor().getName();
        }
        return new SingleVideo(id, video_title, author_name, video_url, image_url);
    }

    //=> 和normal差不多
    @RequestMapping(value = "/yanormalmap/nocache/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> yanormalmapnocache(@PathVariable Integer id) {
        Video targetVideo = videoDao.getVideoByVideoId(id);
        Map<String, Object> result = new HashMap<String, Object>();
        String video_url = targetVideo.getVideo_key();
        String image_url = targetVideo.getImage().getImage_key();
        String video_title = targetVideo.getTitle();
        String author_name = "";
        if (targetVideo.getAuthor() != null){
            author_name = targetVideo.getAuthor().getName();
        }
        result.put("id", id);
        result.put("title", video_title);
        result.put("video_url", video_url);
        result.put("image_url", image_url);
        result.put("authorname", author_name);
        return result;
    }

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/normal/cache/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency     4.62ms    1.36ms  29.80ms   85.43%
        Req/Sec   468.08     98.06   800.00     64.01%
      17859 requests in 10.00s, 4.57MB read
    Requests/sec:   1785.73
    Transfer/sec:    467.71KB

    Running 10s test @ http://localhost/darfoobackend/rest/loadtest/normal/cache/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency     6.38ms    6.25ms  58.94ms   95.60%
        Req/Sec   377.27     94.11   571.00     79.83%
      14482 requests in 10.00s, 3.95MB read
    Requests/sec:   1448.67
    Transfer/sec:    404.54KB
    */
    @RequestMapping(value = "/normal/cache/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo normalcache(@PathVariable Integer id){
        //Integer vid = Integer.parseInt(id);
        return videoCacheDao.getSingleVideo(id);
    }

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/normal/cachepool/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency     4.09ms    2.95ms  60.06ms   95.36%
        Req/Sec   553.38    145.52     1.11k    65.04%
      21088 requests in 10.00s, 5.39MB read
    Requests/sec:   2108.79
    Transfer/sec:    552.29KB
    */
    @RequestMapping(value = "/normal/cachepool/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SingleVideo normalcachepool(@PathVariable Integer id){
        //Integer vid = Integer.parseInt(id);
        return videoCacheDao.getSingleVideoFromPool(id);
    }

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/async/nocache/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    14.12ms   18.59ms 257.98ms   94.90%
        Req/Sec   156.58     46.60   328.00     70.47%
      6145 requests in 10.00s, 1.57MB read
    Requests/sec:    614.46
    Transfer/sec:    160.92KB
    */
    @RequestMapping(value = "/async/nocache/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Callable<SingleVideo> asyncnocache(@PathVariable String id) {
        final int targetid = Integer.parseInt(id);

        return new Callable<SingleVideo>() {
            @Override
            public SingleVideo call() throws Exception {
                Video targetVideo = videoDao.getVideoByVideoId(targetid);
                int video_id = targetVideo.getId();
                String video_url = targetVideo.getVideo_key();
                String image_url = targetVideo.getImage().getImage_key();
                String video_title = targetVideo.getTitle();
                String author_name = "";
                if (targetVideo.getAuthor() != null){
                    author_name = targetVideo.getAuthor().getName();
                }
                return new SingleVideo(video_id, video_title, author_name, video_url, image_url);
            }
        };
    }

    /*
   Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/async/cache/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency     7.73ms    1.73ms  19.64ms   78.09%
        Req/Sec   270.33     50.59   411.00     70.24%
      10527 requests in 10.00s, 2.69MB read
    Requests/sec:   1052.57
    Transfer/sec:    275.67KB

    Running 10s test @ http://localhost/darfoobackend/rest/loadtest/async/cache/1
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    16.06ms   25.72ms 229.39ms   95.92%
        Req/Sec   184.43     66.32   344.00     72.76%
      7263 requests in 10.00s, 1.98MB read
    Requests/sec:    726.15
    Transfer/sec:    202.78KB
    */
    @RequestMapping(value = "/async/cache/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Callable<SingleVideo> asynccache(@PathVariable String id){
        final int targetid = Integer.parseInt(id);

        return new Callable<SingleVideo>() {
            @Override
            public SingleVideo call() throws Exception {
                return videoCacheDao.getSingleVideo(targetid);
            }
        };
    }

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/deferred/nocache
      4 threads and 10 connections

      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency   966.07ms  515.84ms   1.41s    78.78%
        Req/Sec     7.20     14.47    84.00     93.25%
      373 requests in 10.00s, 97.62KB read
    Requests/sec:     37.29
    Transfer/sec:      9.76KB
    */
    @RequestMapping(value = "/deferred/nocache", method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<SingleVideo> deferrednocache() {
        DeferredResult<SingleVideo> response = new DeferredResult<SingleVideo>();
        responseQueue.add(response);
        taskExecutor.execute(new NoCacheRunnable());
        return response;
    }

    /*
    Running 10s test @ http://localhost:8080/darfoobackend/rest/loadtest/deferred/cache
      4 threads and 10 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency   209.85ms   34.71ms 292.95ms   71.29%
        Req/Sec     9.44      3.15    22.00     64.85%
      439 requests in 10.01s, 114.89KB read
    Requests/sec:     43.88
    Transfer/sec:     11.48KB
    */
    @RequestMapping(value = "/deferred/cache", method = RequestMethod.GET)
    public @ResponseBody
    DeferredResult<SingleVideo> deferredcache(){
        DeferredResult<SingleVideo> response = new DeferredResult<SingleVideo>();
        responseQueue.add(response);
        taskExecutor.execute(new CacheRunnable());
        return response;
    }

    class NoCacheRunnable implements Runnable{
        @Override
        public void run() {
            for (DeferredResult<SingleVideo> response : responseQueue) {
                int targetid = 1;
                Video targetVideo = videoDao.getVideoByVideoId(targetid);
                int video_id = targetVideo.getId();
                String video_url = targetVideo.getVideo_key();
                String image_url = targetVideo.getImage().getImage_key();
                String video_title = targetVideo.getTitle();
                String author_name = "";
                if (targetVideo.getAuthor() != null){
                    author_name = targetVideo.getAuthor().getName();
                }
                response.setResult(new SingleVideo(video_id, video_title, author_name, video_url, image_url));
                responseQueue.remove(response);
            }
        }
    }

    class CacheRunnable implements Runnable{
        @Override
        public void run() {
            for (DeferredResult<SingleVideo> response : responseQueue) {
                int targetid = 1;
                response.setResult(videoCacheDao.getSingleVideo(targetid));
                responseQueue.remove(response);
            }
        }
    }
}
