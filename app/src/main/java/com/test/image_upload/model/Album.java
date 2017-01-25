package com.test.image_upload.model;

import java.util.List;

public class Album {

    private Cover cover;
    private List<AlbumPictureDTO> pictures;

    public Album(List<AlbumPictureDTO> pictures) {
        this.pictures = pictures;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public List<AlbumPictureDTO> getPictures() {
        return pictures;
    }

    public void setPictures(List<AlbumPictureDTO> pictures) {
        this.pictures = pictures;
    }

}
