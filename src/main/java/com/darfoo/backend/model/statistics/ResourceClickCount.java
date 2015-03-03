package com.darfoo.backend.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-2.
 */

//统计资源的点击热度
@Entity
@Table(name = "resourceclickcount")
public class ResourceClickCount extends CommonClickCount implements Serializable {
    @Column(name = "type", nullable = false)
    public String type;

    @Column(name = "resourceid", nullable = false)
    public Integer resourceid;

    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 1")
    public Long hottest;
}
