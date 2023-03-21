package com.example.playt.models;

import android.graphics.Bitmap;


public class UserModel {
    String _id;
    String username;
    String nickname;
    ImageBuffer image;
    PostModel[] posts;

    public UserModel(String _id, String username, String nickname, ImageBuffer image, PostModel[] posts) {
        this._id = _id;
        this.username = username;
        this.nickname = nickname;
        this.image = image;
        this.posts = posts;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public ImageBuffer getImage() {
        return image;
    }

    public PostModel[] getPosts() {
        return posts;
    }
}
