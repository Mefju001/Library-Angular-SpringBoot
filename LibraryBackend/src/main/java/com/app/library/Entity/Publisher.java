package com.app.library.Entity;

import jakarta.persistence.*;


@Entity

@Table(name = "Publishers",uniqueConstraints = {
        @UniqueConstraint(columnNames = "Publisher_name")
})
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Publisher_id")
    private Integer id;
    @Column(name = "Publisher_name")
    private String name;

    public Publisher() {
    }

    public Publisher(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
