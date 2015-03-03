package com.darfoo.backend.model.statistics.clicktime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//底部菜单的点击时间
@Entity
@Table(name = "tabclicktime")
public class TabClickTime extends CommonClickTime implements Serializable {
    @Column(name = "tabid", nullable = false)
    public Integer tabid;
}
