package com.darfoo.backend.model.statistics;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by zjh on 15-3-2.
 */

public class CommonClickCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "mac", unique = false, nullable = false)
    String mac;

    @Column(name = "hostip", unique = false, nullable = false)
    String hostip;

    @Column(name = "uuid", unique = false, nullable = false)
    String uuid;

    @Column(name = "clickcount", unique = false, columnDefinition = "bigint(64) default 1")
    Long clickcount;

    public void updateClickcount() {
        this.clickcount += 1;
    }
}
