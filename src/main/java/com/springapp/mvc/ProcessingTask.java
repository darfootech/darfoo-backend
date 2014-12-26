package com.springapp.mvc;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by zjh on 14-12-26.
 */
public class ProcessingTask extends TimerTask {

    private long reqId;
    private AtomicLong concurrentRequests;
    private DeferredResult<ProcessingStatus> deferredResult;
    private int processingTimeMs;

    public ProcessingTask(long reqId, AtomicLong concurrentRequests, int processingTimeMs, DeferredResult<ProcessingStatus> deferredResult) {
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
            boolean deferredStatus = deferredResult.setResult(new ProcessingStatus("Ok", processingTimeMs));
            System.out.println(concReqs + ": Processing of non-blocking request #" + reqId + " done, deferredStatus = " + deferredStatus);
        }
    }
}
