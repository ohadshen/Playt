package com.example.playt.models;

import android.graphics.Bitmap;

import java.util.Date;

public class PostModel {
    String _id;
    String title;
    ImageBuffer image;
    String carPlate;
    int points;
    Date date;

    public PostModel(String _id, String title, ImageBuffer image, String carNumber, int points, Date date) {
        this._id = _id;
        this.title = title;
        this.image = image;
        this.carPlate = carNumber;
        this.points = points;
        this.date = date;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
