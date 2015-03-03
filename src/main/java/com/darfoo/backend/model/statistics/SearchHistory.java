package com.darfoo.backend.model.statistics;

import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

//记录搜索行为
@Entity
@Table(name = "searchhistory")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ModelAttrSuper
    public Integer id;

    @Column(name = "searchcontent", unique = false, nullable = false)
    public String searchcontent;

    @Column(name = "searchtype", unique = false, nullable = false)
    public String searchtype;

    @Column(name = "timestamp")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    public Date dueDate = new Date();
}
