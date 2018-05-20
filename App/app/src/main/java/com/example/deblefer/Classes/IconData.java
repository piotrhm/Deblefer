package com.example.deblefer.Classes;

public class IconData {
    private String description;
    private int imgId;
    private int imgId2;
    private int imgId3;
    private int imgId4;
    private int imgId5;

    public IconData(String description, int imgId, int imgId2, int imgId3, int imgId4, int imgId5) {
        this.description = description;
        this.imgId = imgId;
        this.imgId2 = imgId2;
        this.imgId3 = imgId3;
        this.imgId4 = imgId4;
        this.imgId5 = imgId5;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}