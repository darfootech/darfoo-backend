package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

@MappedSuperclass
public class CommonClickTime {
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

    @Column(name = "timestamp")
    @ModelAttrSuper
    @CSVTitle(title = "时间戳")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    @ModelAttrSuper
    @CSVTitle(title = "日期")
    public Date date = new Date();
}
