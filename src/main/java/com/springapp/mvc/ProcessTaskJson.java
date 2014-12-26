package com.springapp.mvc;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zjh on 14-12-26.
 */
public class ProcessTaskJson extends TimerTask {
    private long reqId;
    private AtomicLong concurrentRequests;
    private DeferredResult<ProcessStatusJson> deferredResult;
    private int processingTimeMs;

    public ProcessTaskJson(long reqId, AtomicLong concurrentRequests, int processingTimeMs, DeferredResult<ProcessStatusJson> deferredResult) {
        this.reqId = reqId;
        this.concurrentRequests = concurrentRequests;
        this.processingTimeMs = processingTimeMs;
        this.deferredResult = deferredResult;
    }

    @Override
    public void run() {

        long concReqs = concurrentRequests.getAndDecrement();

        if (deferredResult.isSetOrExpired()) {
            System.out.println(concReqs + ": Processing of non-blocking request #" + reqId + " already expired");
        } else {
            boolean deferredStatus = deferredResult.setResult(new ProcessStatusJson("Ok", processingTimeMs));
            System.out.println(concReqs + ": Processing of non-blocking request #" + reqId + " done, deferredStatus = " + deferredStatus);
        }
    }
}
