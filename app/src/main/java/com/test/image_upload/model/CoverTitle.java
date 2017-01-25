package com.test.image_upload.model;

public class CoverTitle {

    private Integer color;
    private String font;
    private String title;
    private Float xCanvasCoordinate;
    private Float yCanvasCoordinate;

    public CoverTitle() {
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getxCanvasCoordinate() {
        return xCanvasCoordinate;
    }

    public void setxCanvasCoordinate(Float xCanvasCoordinate) {
        this.xCanvasCoordinate = xCanvasCoordinate;
    }

    public Float getyCanvasCoordinate() {
        return yCanvasCoordinate;
    }

    public void setyCanvasCoordinate(Float yCanvasCoordinate) {
        this.yCanvasCoordinate = yCanvasCoordinate;
    }

}
