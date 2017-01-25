package com.test.image_upload.model;

import java.io.Serializable;

public class Resolution implements Serializable {

    private int width;
    private int height;

    public Resolution() {
    }

    public Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
