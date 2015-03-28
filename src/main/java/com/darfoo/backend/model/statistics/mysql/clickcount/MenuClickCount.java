package com.darfoo.backend.model.statistics.mysql.clickcount;

import com.darfoo.backend.model.cota.CSVTitle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//统计菜单的点击热度
@Entity
@Table(name = "menuclickcount")
public class MenuClickCount extends CommonClickCount implements Serializable {
    @Column(name = "menuid", nullable = false)
    @CSVTitle(title = "菜单编号")
    public Integer menuid;
}
