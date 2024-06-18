package com.example.merisuraksha.Domain;

public class IconDomain {
    private String name;
    private String imgpath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public IconDomain(String name, String imgpath) {
        this.name = name;
        this.imgpath = imgpath;
    }
}
