package com.darfoo.backend.model.auth;

import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;

/**
 * Created by zjh on 15-3-3.
 */

//普通用户
@Entity
@Table(name = "user")
public class User extends CommonAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "username", unique = true, nullable = false)
    public String username;

    @Column(name = "password", unique = false, nullable = false)
    public String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
