package com.example.firebasedemo.Fragments;

public class ProductData
{
   private String id;
    private String pName;
    private String pDes;
    private String pPrice;
    private String pImg;

    public ProductData() {
    }

    public ProductData(String id, String pName, String pDes, String pPrice,String pImg) {
        this.id = id;
        this.pName = pName;
        this.pDes = pDes;
        this.pPrice = pPrice;
        this.pImg=pImg;

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
