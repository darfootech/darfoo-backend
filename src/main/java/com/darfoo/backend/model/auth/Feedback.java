package com.darfoo.backend.model.auth;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

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
    @CSVTitle(title = "用户标识")
    public Integer userid;

    @Column(name = "username", unique = false, nullable = false)
    @CSVTitle(title = "用户名")
    public String username;

    @Column(name = "feedback", unique = false, nullable = false)
    @CSVTitle(title = "反馈内容")
    public String feedback;
}
