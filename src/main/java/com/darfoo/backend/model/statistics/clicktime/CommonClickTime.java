package com.darfoo.backend.model.statistics.clicktime;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zjh on 15-3-3.
 */

public class CommonClickTime {
    @ModelAttrSuper
    public Integer id;

    @ModelAttrSuper
    @CSVTitle(title = "mac地址")
    public String mac;

    @ModelAttrSuper
    @CSVTitle(title = "ip地址")
    public String hostip;

    @ModelAttrSuper
    @CSVTitle(title = "唯一标识")
    public String uuid;

    @ModelAttrSuper
    @CSVTitle(title = "时间戳")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @ModelAttrSuper
    @CSVTitle(title = "日期")
    public Date date = new Date();
}
