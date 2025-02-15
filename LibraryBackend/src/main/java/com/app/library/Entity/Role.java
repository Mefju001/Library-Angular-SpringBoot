package com.app.library.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Size(max = 20)
    @Column(name = "name")
    private ERole name;

    public Role() {
    }

    public Role(String name) {
        this.name = ERole.valueOf(name);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
