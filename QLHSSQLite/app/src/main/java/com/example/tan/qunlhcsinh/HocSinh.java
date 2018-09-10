package com.example.tan.qunlhcsinh;


import java.io.Serializable;

public class HocSinh implements Serializable {
    private int id;
    private String hoTen;
    private String lop;
    private String namSinh;
    private float latitude;
    private float longitude;
    private boolean gioiTinh;

    public HocSinh(int id, String hoTen, String lop, String namSinh, boolean gioiTinh, float longitude, float latitude) {
        this.id=id;
        this.hoTen = hoTen;
        this.lop = lop;
        this.namSinh = namSinh;
        this.gioiTinh = gioiTinh;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
}



