package com.darfoo.backend.model.statistics;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

//记录搜索行为
public class SearchHistory {
    @ModelAttrSuper
    public Integer id;

    @CSVTitle(title = "搜索内容")
    public String searchcontent;

    @CSVTitle(title = "搜索资源类型")
    public String searchtype;

    @CSVTitle(title = "搜索时间戳")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @CSVTitle(title = "搜索日期")
    public Date date = new Date();
}
