package com.darfoo.backend.model.auth;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by zjh on 15-3-4.
 */

@MappedSuperclass
public class CommonAuth {
    @Column(name = "timestamp")
    @ModelAttrSuper
    @CSVTitle(title = "时间戳")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    @ModelAttrSuper
    @CSVTitle(title = "日期")
    public Date date = new Date();
}
