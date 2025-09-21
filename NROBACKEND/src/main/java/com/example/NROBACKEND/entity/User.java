package com.example.NROBACKEND.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private Long vang = 0L; // vàng
    private Long ngoc = 0L; // ngọc

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getVang() { return vang; }
    public void setVang(Long vang) { this.vang = vang; }

    public Long getNgoc() { return ngoc; }
    public void setNgoc(Long ngoc) { this.ngoc = ngoc; }
}