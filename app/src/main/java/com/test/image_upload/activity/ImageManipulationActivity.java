package com.test.image_upload.activity;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.test.image_upload.R;
import com.test.image_upload.model.AlbumPictureDTO;
import com.test.image_upload.util.ImageManipulationTouchListener;
import com.test.image_upload.util.ImageUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_image_manipulation)
public class ImageManipulationActivity extends AppCompatActivity {

    @Extra("picture") AlbumPictureDTO picture;

    @ViewById(R.id.image_manipulation_preview) ImageView imagePreview;
    @ViewById(R.id.image_manipulation_rotation_bar) SeekBar rotationBar;

    @AfterViews
    public void initialize() {
        imagePreview.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(picture.getFile(), 300, 300));
        setupImagePreview();
    }

    @Click(R.id.image_manipulation_rotation_minus)
    public void onMinusClick() {
        performPrecisionRotation(-1);
    }

    @Click(R.id.image_manipulation_rotation_plus)
    public void onPlusClick() {
        performPrecisionRotation(1);
    }

    private void performPrecisionRotation(float f) {
        Matrix rotatonMatrix = new Matrix(imagePreview.getImageMatrix());
        rotatonMatrix.postRotate(f, imagePreview.getMeasuredWidth() / 2, imagePreview.getMeasuredHeight() / 2);
        imagePreview.setScaleType(ImageView.ScaleType.MATRIX);
        imagePreview.setImageMatrix(rotatonMatrix);
    }

    private void setupImagePreview() {
        imagePreview.setImageMatrix(new Matrix());
        imagePreview.setAdjustViewBounds(true);
        imagePreview.setImageMatrix(imagePreview.getMatrix());

        ImageManipulationTouchListener touchListener = new ImageManipulationTouchListener();
        touchListener.setRotationSeekbar(rotationBar);
        imagePreview.setOnTouchListener(touchListener);

        rotationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float rotation = 0;
            Matrix matrix = new Matrix();
            Matrix savedMatrix = new Matrix();
            boolean rotationActive = true;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if (rotationActive) {
                    matrix = new Matrix(savedMatrix);
                    rotation = ((progressValue - 50) * 90) / 50f;
                    matrix.postRotate(rotation, imagePreview.getMeasuredWidth() / 2, imagePreview.getMeasuredHeight() / 2);
                    imagePreview.setImageMatrix(matrix);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rotationActive = true;
                imagePreview.setScaleType(ImageView.ScaleType.MATRIX);
                savedMatrix.set(imagePreview.getImageMatrix());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rotationActive = false;
                seekBar.setProgress(50);
            }
        });
    }

    @Click(R.id.image_manipulation_cancel_button)
    public void onCancelClick() {

    }

    @Click(R.id.image_manipulation_accept_button)
    public void onAcceptClick() {

    }

}
