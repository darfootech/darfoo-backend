package com.darfoo.backend.model.statistics.mongo.clickcount;

import com.darfoo.backend.model.cota.CSVTitle;

import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//统计菜单的点击热度
public class MenuClickCount extends CommonClickCount implements Serializable {
    @CSVTitle(title = "菜单编号")
    public Integer menuid;
}
