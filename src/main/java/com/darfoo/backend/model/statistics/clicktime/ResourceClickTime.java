package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.CSVTitle;

import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//统计资源的点击时间
public class ResourceClickTime extends CommonClickTime implements Serializable {
    @CSVTitle(title = "资源类型")
    public String type;

    @CSVTitle(title = "资源标识")
    public Integer resourceid;
}
