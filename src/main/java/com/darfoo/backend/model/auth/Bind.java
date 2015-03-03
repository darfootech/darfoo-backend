package com.darfoo.backend.model.auth;

import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;

/**
 * Created by zjh on 15-3-3.
 */

//userid和mac地址的绑定关系 一个mac地址只能绑定一个用户
@Entity
@Table(name = "bind")
public class Bind extends CommonAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "userid", unique = false, nullable = false)
    public Integer userid;

    @Column(name = "mac", unique = false, nullable = false)
    public String mac;
}
