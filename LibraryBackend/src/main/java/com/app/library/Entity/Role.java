package com.app.library.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 20)
    @Column(name = "name")
    private String rola;

    public Role() {
    }

    public Role(String rola) {
        this.rola = rola;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }
}
