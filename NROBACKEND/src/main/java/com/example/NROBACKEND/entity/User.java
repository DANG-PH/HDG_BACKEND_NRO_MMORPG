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
    private Long sucManh = 2000L;
    private Long vangNapTuWeb = 0L; // vàng nạp từ web
    private Long ngocNapTuWeb = 0L; // ngọc nạp từ web


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

    public Long getVangNapTuWeb() { return vangNapTuWeb; }
    public void setVangNapTuWeb(Long vang) { this.vangNapTuWeb = vang; }

    public Long getNgocNapTuWeb() { return ngocNapTuWeb; }
    public void setNgocNapTuWeb(Long ngoc) { this.ngocNapTuWeb = ngoc; }

    public Long getSucManh() { return sucManh; }
    public void setSucManh(Long sm) { this.sucManh = sm; }
}