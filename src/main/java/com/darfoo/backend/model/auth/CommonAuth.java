package com.darfoo.backend.model.auth;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by zjh on 15-3-4.
 */

@MappedSuperclass
public class CommonAuth {
    @Column(name = "timestamp")
    public Long timestamp = System.currentTimeMillis() / 1000;

    @Column(name = "date")
    public Date date = new Date();
}
