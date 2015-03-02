package com.darfoo.backend.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zjh on 15-3-2.
 */

@Entity
@Table(name = "resource")
public class ResourceClickCount extends CommonClickCount {
    @Column(name = "type", unique = false, nullable = false)
    String type;

    @Column(name = "resourceid", unique = false, nullable = false)
    Integer resourceid;
}
