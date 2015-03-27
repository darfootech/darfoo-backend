package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.CSVTitle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//底部菜单的点击时间
public class TabClickTime extends CommonClickTime implements Serializable {
    @CSVTitle(title = "底部菜单标识")
    public Integer tabid;
}
