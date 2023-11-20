package com.example.appbandienthoai;

public class GioHang {
    Integer soLuong, idCartList;
    String idVoucher, idSanPham, idCus, hinhSanPham;
    double totalMoney, donGia;
    String tenSP;

    public GioHang(Integer soLuong, Integer idCartList, String idVoucher, String idSanPham, String idCus, long donGia, String tenSP, String hinhSanPham) {
        this.soLuong = soLuong;
        this.idCartList = idCartList;
        this.idVoucher = idVoucher;
        this.idSanPham = idSanPham;
        this.idCus = idCus;
        this.donGia = donGia;
        this.tenSP = tenSP;
        this.hinhSanPham = hinhSanPham;
    }

    public GioHang() {
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public String getHinhSanPham() {
        return hinhSanPham;
    }

    public void setHinhSanPham(String hinhSanPham) {
        this.hinhSanPham = hinhSanPham;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Integer getIdCartList() {
        return idCartList;
    }

    public void setIdCartList(Integer idCartList) {
        this.idCartList = idCartList;
    }

    public String getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getIdCus() {
        return idCus;
    }

    public void setIdCus(String idCus) {
        this.idCus = idCus;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

}
