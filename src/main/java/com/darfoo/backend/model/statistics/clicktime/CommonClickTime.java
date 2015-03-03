package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import javax.swing.*;
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
    public String mac;

    @Column(name = "hostip", unique = false, nullable = false)
    @ModelAttrSuper
    public String hostip;

    @Column(name = "uuid", unique = false, nullable = false)
    @ModelAttrSuper
    public String uuid;

    @Column(name = "timestamp")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    public Date date = new Date();
}
