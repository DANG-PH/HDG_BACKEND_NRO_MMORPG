package com.example.NROBACKEND.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private boolean biBan = false;
    private String role = "USER";

    private String password;

    private Long vang = 0L; // vàng
    private Long ngoc = 0L; // ngọc
    private Long sucManh = 2000L;
    private Long vangNapTuWeb = 0L; // vàng nạp từ web
    private Long ngocNapTuWeb = 0L; // ngọc nạp từ web

    private Float x = 100f;
    private Float y = 175f;

    private String mapHienTai = "Nhà Gôhan";

    private boolean daVaoTaiKhoanLanDau = false;

    private boolean coDeTu = false;

//    // de tu
//    private Long sucManhDeTu = 2000L;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Item> items = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private DeTu deTu; // null nếu chưa có đệ

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

//    public Long getSucManhDeTu() { return sucManhDeTu; }
//    public void setSucManhDeTu(Long sm) { this.sucManhDeTu = sm; }


    public Float getX() { return x; }
    public void setX(Float x) { this.x = x; }

    public Float getY() { return y; }
    public void setY(Float y) { this.y = y; }

    public String getMapHienTai() { return mapHienTai; }
    public void setMapHienTai(String mapHienTai) { this.mapHienTai = mapHienTai; }

    public boolean isDaVaoTaiKhoanLanDau() {
        return daVaoTaiKhoanLanDau;
    }
    public void setDaVaoTaiKhoanLanDau(boolean daVaoTaiKhoanLanDau) {
        this.daVaoTaiKhoanLanDau = daVaoTaiKhoanLanDau;
    }

    public boolean isCoDeTu() {
        return coDeTu;
    }
    public void setCoDeTu(boolean coDeTu) {
        this.coDeTu = coDeTu;
    }

    public DeTu getDeTu() {return deTu;}
    public void setDeTu(DeTu deTu) {this.deTu = deTu;}

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public boolean isBiBan() {
        return biBan;
    }
    public void setBiBan(boolean biBan) {
        this.biBan = biBan;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}