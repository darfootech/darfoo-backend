package com.springapp.mvc;

import com.sun.management.UnixOperatingSystemMXBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zjh on 14-12-26.
 */

@Controller
@RequestMapping("/testconcurrent")
public class ProcessingController {
    private static Timer timer = new Timer();
    private static OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
    private static final AtomicLong lastRequestId = new AtomicLong(0);
    private static final AtomicLong concurrentRequests = new AtomicLong(0);
    private static long maxConcurrentRequests = 0;

    //@Value("${statistics.requestsPerLog}")
    private int STAT_REQS_PER_LOG = 5000;

    @RequestMapping("/blockprocess")
    public @ResponseBody ProcessingStatus blockingProcessing(HttpServletRequest request) {
        int minMs = Integer.parseInt(request.getParameter("minMs"));
        int maxMs = Integer.parseInt(request.getParameter("maxMs"));

        long reqId = lastRequestId.getAndIncrement();
        long concReqs = concurrentRequests.getAndIncrement();

        updateStatistics(reqId, concReqs);

        int processingTimeMs = calculateProcessingTime(minMs, maxMs);

        System.out.println(concReqs + ": Start blocking request #" + reqId + ", processing time: " + processingTimeMs + " ms.");

        try {
            Thread.sleep(processingTimeMs);
        }
        catch (InterruptedException e) {}

        finally {
            concurrentRequests.decrementAndGet();
            System.out.println(concReqs + ": Processing of blocking request #" + reqId + " is done");
        }

        return new ProcessingStatus("Ok", processingTimeMs);
    }

    @RequestMapping(value = "/nonblockprocess", method = RequestMethod.GET)
    public @ResponseBody DeferredResult<ProcessingStatus> nonBlockingProcessing(HttpServletRequest request){
        int minMs = Integer.parseInt(request.getParameter("minMs"));
        int maxMs = Integer.parseInt(request.getParameter("maxMs"));

        long reqId = lastRequestId.getAndIncrement();
        long concReqs = concurrentRequests.getAndIncrement();

        updateStatistics(reqId, concReqs);

        int processingTimeMs = calculateProcessingTime(minMs, maxMs);

        System.out.println(concReqs + ": Start non-blocking request #" + reqId + ", processing time: " + processingTimeMs + " ms.");

        // Create the deferredResult and initiate a callback object, task, with it
        DeferredResult<ProcessingStatus> deferredResult = new DeferredResult<ProcessingStatus>();
        ProcessingTask task = new ProcessingTask(reqId, concurrentRequests, processingTimeMs, deferredResult);

        // Schedule the task for asynch completion in the future
        timer.schedule(task, processingTimeMs);

        System.out.println(concReqs + ": Processing of non-blocking request #" + reqId + " leave the request thread");

        // Return to let go of the precious thread we are holding on to...
        return deferredResult;
    }

    private void updateStatistics(long reqId, long concReqs) {
        if (concReqs > maxConcurrentRequests) {
            maxConcurrentRequests = concReqs;
        }

        if (reqId % STAT_REQS_PER_LOG == 0 && reqId > 0) {
            Object openFiles = "UNKNOWN";
            if (os instanceof UnixOperatingSystemMXBean) {
                openFiles = ((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount();
            }
            System.out.println("Statistics: noOfReqs: " + reqId + ", maxConcReqs: " + maxConcurrentRequests +", openFiles: " + openFiles);
        }
    }

    private int calculateProcessingTime(int minMs, int maxMs) {
        if (maxMs < minMs) maxMs = minMs;
        int processingTimeMs = minMs + (int) (Math.random() * (maxMs - minMs));
        return processingTimeMs;
    }
}
