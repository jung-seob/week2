package com.example.q.week2;

import android.graphics.Bitmap;

public class contact_item {
    private String name;
    private String number;
    private Bitmap photo;

    public contact_item(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
