package com.darfoo.backend.model.statistics;

import com.darfoo.backend.model.cota.annotations.CSVTitle;

/**
 * Created by zjh on 15-3-3.
 */

//记录广场舞搜索行为
public class DanceSearchHistory {
    @CSVTitle(title = "搜索内容")
    public String searchcontent;

    @CSVTitle(title = "搜索资源类型")
    public String searchtype;

    @CSVTitle(title = "搜索时间戳")
    public Long timestamp;

    @CSVTitle(title = "搜索日期")
    public String date;
}
