package com.darfoo.backend.model.statistics;

import com.darfoo.backend.model.cota.ModelAttrDefault;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-3-2.
 */

@Entity
@Table(name = "resource_test")
public class ResourceClickCount extends CommonClickCount implements Serializable {
    @Column(name = "type", nullable = false)
    public String type;

    @Column(name = "resourceid", nullable = false)
    public Integer resourceid;

    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 1")
    public Long hottest;

    public ResourceClickCount() {
    }

    public Long getHottest() {
        return hottest;
    }

    public void setHottest(Long hottest) {
        this.hottest = hottest;
    }
}
