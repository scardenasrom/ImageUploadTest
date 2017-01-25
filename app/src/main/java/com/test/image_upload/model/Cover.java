package com.test.image_upload.model;

public class Cover {

    private AlbumPictureDTO picture;
    private int backgroundColor;
    private String text;

    public AlbumPictureDTO getPicture() {
        return picture;
    }

    public void setPicture(AlbumPictureDTO picture) {
        this.picture = picture;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
