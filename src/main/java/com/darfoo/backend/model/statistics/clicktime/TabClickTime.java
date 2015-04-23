package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.annotations.CSVTitle;

import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//底部菜单的点击时间
public class TabClickTime extends CommonClickTime implements Serializable {
    @CSVTitle(title = "底部菜单标识")
    public Integer tabid;
}
