package com.example.firebasedemo.Fragments;

public class ProductData
{
    String id;
    String pName;
    String pDes;
    String pPrice;
    String pImg;

    public ProductData() {
    }

    public ProductData(String id, String pName, String pDes, String pPrice,String bitmap) {
        this.id = id;
        this.pName = pName;
        this.pDes = pDes;
        this.pPrice = pPrice;
        this.pImg=bitmap;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpDes() {
        return pDes;
    }

    public void setpDes(String pDes) {
        this.pDes = pDes;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpImg() {
        return pImg;
    }

    public void setpImg(String pImg) {
        this.pImg = pImg;
    }
}
