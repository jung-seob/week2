package com.example.q.week2;

import android.graphics.Bitmap;

public class contact_item {
    private String name;
    private String number;
    private String image;
    private int hasImage;

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }

    public int getHasImage() {

        return hasImage;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public contact_item(String name, String number,String image,int hasImage){
        this.name = name;
        this.number = number;
        this.image = image;
        this.hasImage = hasImage;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
