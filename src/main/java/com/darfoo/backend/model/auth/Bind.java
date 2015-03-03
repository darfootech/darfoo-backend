package com.darfoo.backend.model.auth;

import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

//userid和mac地址的绑定关系 一个mac地址只能绑定一个用户
@Entity
@Table(name = "bind")
public class Bind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "userid", unique = false, nullable = false)
    public Integer userid;

    @Column(name = "mac", unique = false, nullable = false)
    public String mac;

    @Column(name = "timestamp")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    public Date date = new Date();

    public Bind() {
    }

    public Bind(Integer userid, String mac) {
        this.userid = userid;
        this.mac = mac;
    }
}
