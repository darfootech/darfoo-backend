package com.darfoo.backend.model.statistics.clicktime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-3.
 */

//统计资源的点击时间
@Entity
@Table(name = "resourceclicktime")
public class ResourceClickTime extends CommonClickTime implements Serializable {
    @Column(name = "type", nullable = false)
    public String type;

    @Column(name = "resourceid", nullable = false)
    public Integer resourceid;
}
