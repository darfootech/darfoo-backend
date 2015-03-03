package com.darfoo.backend.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//底部菜单的点击热度
@Entity
@Table(name = "tabclickcount")
public class TabClickCount extends CommonClickCount implements Serializable {
    @Column(name = "tabid", nullable = false)
    public Integer tabid;

    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 1")
    public Long hottest;
}
