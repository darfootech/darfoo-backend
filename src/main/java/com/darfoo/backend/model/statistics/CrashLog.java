package com.darfoo.backend.model.statistics;

import com.darfoo.backend.model.cota.CSVTitle;

/**
 * Created by zjh on 15-3-3.
 */

//记录launcher崩溃日志
public class CrashLog {
    @CSVTitle(title = "崩溃原因")
    public String log;

    @CSVTitle(title = "崩溃时间戳")
    public Long timestamp;

    @CSVTitle(title = "崩溃日期")
    public String date;
}
