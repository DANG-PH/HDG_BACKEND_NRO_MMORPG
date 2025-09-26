package com.example.NROBACKEND.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String maItem; // mã định danh item (map sang client)
    private String ten;
    private String loai;
    @Column(columnDefinition = "TEXT")
    private String moTa;

    private int soLuong;
    private String hanhTinh;
    private String setKichHoat;

    private int soSaoPhaLe;
    private int soSaoPhaLeCuongHoa;
    private int soCap;

    private float hanSuDung;
    private long sucManhYeuCau;

    private String linkTexture;

    // Lưu chỉ số dưới dạng JSON string (vd: {"atk":100,"def":50})
    @Column(columnDefinition = "json")
    private String chiso;

    // Nơi item đang nằm: hanhtrang, ruongdo, hanhtrangdetu, hanhtrangdangmac
    private String viTri;

    // Quan hệ N-1: nhiều item thuộc về 1 user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private User user;

    // Getters & Setters
    public String getMaItem() { return maItem; }
    public void setMaItem(String maItem) { this.maItem = maItem; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getHanhTinh() { return hanhTinh; }
    public void setHanhTinh(String hanhTinh) { this.hanhTinh = hanhTinh; }

    public String getSetKichHoat() { return setKichHoat; }
    public void setSetKichHoat(String setKichHoat) { this.setKichHoat = setKichHoat; }

    public String getLinkTexture() { return linkTexture; }
    public void setLinkTexture(String linkTexture) { this.linkTexture = linkTexture; }


    public int getSoSaoPhaLe() { return soSaoPhaLe; }
    public void setSoSaoPhaLe(int soSaoPhaLe) { this.soSaoPhaLe = soSaoPhaLe; }

    public int getSoSaoPhaLeCuongHoa() { return soSaoPhaLeCuongHoa; }
    public void setSoSaoPhaLeCuongHoa(int soSaoPhaLeCuongHoa) { this.soSaoPhaLeCuongHoa = soSaoPhaLeCuongHoa; }

    public int getSoCap() { return soCap; }
    public void setSoCap(int soCap) { this.soCap = soCap; }

    public float getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(float hanSuDung) { this.hanSuDung = hanSuDung; }

    public long getSucManhYeuCau() { return sucManhYeuCau; }
    public void setSucManhYeuCau(long sucManhYeuCau) { this.sucManhYeuCau = sucManhYeuCau; }

    public String getChiso() { return chiso; }
    public void setChiso(String chiso) { this.chiso = chiso; }

    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}