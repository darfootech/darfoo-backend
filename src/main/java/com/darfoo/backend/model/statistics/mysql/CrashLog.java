package com.darfoo.backend.model.statistics.mysql;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

//记录launcher崩溃日志
@Entity
@Table(name = "crashlog")
public class CrashLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "log", unique = false, nullable = false)
    @CSVTitle(title = "崩溃原因")
    public String log;

    @Column(name = "timestamp")
    @CSVTitle(title = "崩溃时间戳")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    @CSVTitle(title = "崩溃日期")
    public Date date = new Date();
}
