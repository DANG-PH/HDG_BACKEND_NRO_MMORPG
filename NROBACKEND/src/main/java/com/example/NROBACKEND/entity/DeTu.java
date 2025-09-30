package com.example.NROBACKEND.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "detu")
public class DeTu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)

    private Long sucManh = 2000L;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private User user;

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSucManh() { return sucManh; }
    public void setSucManh(Long sm) { this.sucManh = sm; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}