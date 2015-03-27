package com.darfoo.backend.model.statistics.clickcount;

import com.darfoo.backend.model.cota.CSVTitle;
import com.darfoo.backend.model.cota.ModelAttrSuper;

import javax.persistence.*;

/**
 * Created by zjh on 15-3-2.
 */

public class CommonClickCount {
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
    @CSVTitle(title = "热度")
    public Long hot;
}
