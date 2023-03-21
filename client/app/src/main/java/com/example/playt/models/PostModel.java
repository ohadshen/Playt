package com.example.playt.models;

import android.graphics.Bitmap;

import java.util.Date;

public class PostModel {

    String title;
    ImageBuffer image;
    String carPlate;
    int points;
    Date date;

    public PostModel(String title, ImageBuffer image, String carNumber, int points, Date date) {
        this.title = title;
        this.image = image;
        this.carPlate = carNumber;
        this.points = points;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public ImageBuffer getImage() {
        return image;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public int getPoints() {
        return points;
    }

    public Date getDate() {
        return date;
    }
}
