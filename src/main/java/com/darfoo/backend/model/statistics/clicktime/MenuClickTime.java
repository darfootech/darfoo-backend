package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.CSVTitle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//统计菜单的点击时间
public class MenuClickTime extends CommonClickTime implements Serializable {
    @CSVTitle(title = "菜单编号")
    public Integer menuid;
}
