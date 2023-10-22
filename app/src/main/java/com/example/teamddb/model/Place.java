package com.example.teamddb.model;

public class Place {
    private int imageResource;
    private String name;
    private int price;
    private String diemdi,diemden;

    public Place(int imageResource, String name, int price, String diemdi, String diemden) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.diemdi = diemdi;
        this.diemden=diemden;
    }

    public Place(int imageResource, String name, int price) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
    }

    public Place() {
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDiemdi() {
        return diemdi;
    }

    public void setDiemdi(String diemdi) {
        this.diemdi = diemdi;
    }

    public String getDiemden() {
        return diemden;
    }

    public void setDiemden(String diemden) {
        this.diemden = diemden;
    }
}
