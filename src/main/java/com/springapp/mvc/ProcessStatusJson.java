package com.springapp.mvc;

/**
 * Created by zjh on 14-12-26.
 */
public class ProcessStatusJson {
    private final String status;

    private final int processingTimeMs;

    public ProcessStatusJson() {
        status = "UNKNOWN";
        processingTimeMs = -1;
    }

    public ProcessStatusJson(String status, int processingTimeMs) {
        this.status = status;
        this.processingTimeMs = processingTimeMs;
    }

    public String getStatus() {
        return status;
    }

    public int getProcessingTimeMs() {
        return processingTimeMs;
    }
}
