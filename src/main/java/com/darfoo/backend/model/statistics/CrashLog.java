package com.darfoo.backend.model.statistics;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

//记录launcher崩溃日志
public class CrashLog {
    @ModelAttrSuper
    public Integer id;

    @CSVTitle(title = "崩溃原因")
    public String log;

    @CSVTitle(title = "崩溃时间戳")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @CSVTitle(title = "崩溃日期")
    public Date date = new Date();
}
