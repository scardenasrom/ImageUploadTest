package com.test.image_upload.model;

import java.io.File;
import java.io.Serializable;

public class AlbumPictureDTO implements Serializable {

    private String base64;
    private File file;
    private String title;
    private String footer;
    private Resolution resolution;

    public AlbumPictureDTO() {
    }

    public AlbumPictureDTO(File file) {
        this.file = file;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

}
