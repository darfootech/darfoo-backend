package com.darfoo.backend.model.auth;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;

/**
 * Created by zjh on 15-3-4.
 */

//用户反馈
@Entity
@Table(name = "feedback")
public class Feedback extends CommonAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "userid", unique = false, nullable = false)
    public Integer userid;

    @Column(name = "username", unique = false, nullable = false)
    public String username;

    @Column(name = "feedback", unique = false, nullable = false)
    public String feedback;
}
