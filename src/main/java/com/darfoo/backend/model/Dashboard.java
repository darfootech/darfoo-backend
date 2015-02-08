package com.darfoo.backend.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjh on 15-2-8.
 */

@Entity
@Table(name = "dashboard")
public class Dashboard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "status", nullable = false, updatable = true, columnDefinition = "int default 0")
    Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
