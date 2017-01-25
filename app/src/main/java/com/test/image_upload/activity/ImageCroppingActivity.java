package com.test.image_upload.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.test.image_upload.BaseActivity;
import com.test.image_upload.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@EActivity(R.layout.activity_image_cropping)
public class ImageCroppingActivity extends BaseActivity {

    public final static String CROPPED_FILE_PATH = "croppedFilePath";

    @Extra("pictureUri") Uri pictureUri;

    @ViewById(R.id.image_cropping_crop_image_view) CropImageView cropImageView;

    @AfterViews
    public void initialize() {
        setupImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_cropping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.image_cropping_menu_crop:
                cropImage();
                break;
        }
        return true;
    }

    private void setupImage() {
        cropImageView.setImageUriAsync(pictureUri);
    }

    private void cropImage() {
        Bitmap bmp = cropImageView.getCroppedImage();
        String filePath = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(filePath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, System.currentTimeMillis() + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(CROPPED_FILE_PATH, file.getPath());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
