package com.darfoo.backend.model.statistics.mysql.clickcount;

import com.darfoo.backend.model.cota.CSVTitle;
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
    @CSVTitle(title = "mac地址")
    public String mac;

    @Column(name = "hostip", unique = false, nullable = false)
    @ModelAttrSuper
    @CSVTitle(title = "ip地址")
    public String hostip;

    @Column(name = "uuid", unique = false, nullable = false)
    @ModelAttrSuper
    @CSVTitle(title = "唯一标识")
    public String uuid;

    @Column(name = "HOTTEST", nullable = true, updatable = true, columnDefinition = "bigint(64) default 1")
    @ModelAttrSuper
    @CSVTitle(title = "热度")
    public Long hot;
}
