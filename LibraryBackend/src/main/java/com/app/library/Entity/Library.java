package com.app.library.Entity;

import jakarta.persistence.*;

@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Location")
    private String location;
    @Column(name = "address")
    private String address;
    @Column(name = "Map")
    private String map;

    public Library() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
