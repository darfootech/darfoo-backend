package com.darfoo.backend.model.statistics.clickcount;

import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;

/**
 * Created by zjh on 15-3-2.
 */

@MappedSuperclass
public class CommonClickCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "mac", unique = false, nullable = false)
    @ModelAttrSuper
    public String mac;

    @Column(name = "hostip", unique = false, nullable = false)
    @ModelAttrSuper
    public String hostip;

    @Column(name = "uuid", unique = false, nullable = false)
    @ModelAttrSuper
    public String uuid;

    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 1")
    @ModelAttrSuper
    public Long hottest;
}
