package com.example.q.week2;

public class Recipe {
    private String name;
    private String photo;
    private String ingredient;
    private String howToCook;
    private String time;
    private String user;

    public Recipe(String name, String photo, String ingredient, String howToCook,String time,String user){
        this.name = name;
        this.photo = photo;
        this.ingredient = ingredient;
        this.howToCook = howToCook;
        this.time = time;
        this.user=user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getHowToCook() {
        return howToCook;
    }

    public void setHowToCook(String howToCook) {
        this.howToCook = howToCook;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
