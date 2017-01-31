package com.test.image_upload.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.image_upload.BaseActivity;
import com.test.image_upload.R;
import com.test.image_upload.model.AlbumPictureDTO;
import com.test.image_upload.util.ImageUtils;
import com.test.image_upload.view.AlbumPageView;
import com.test.image_upload.view.ProgressView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.activity_page_creation)
public class PageCreationActivity extends BaseActivity {

    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int IMAGE_CROPPING_REQUEST = 2;

    @Extra("numOfPics") int numOfPics;

    @ViewById(R.id.page_creation_page_layout) RelativeLayout pageLayout;
    @ViewById(R.id.page_creation_progress_view) ProgressView progressView;

    private TextView picsCountView;
    private String currentHandlingPicTag;

    private int currentNumberOfPics;
    private File temporalPhotoFile;

    @AfterViews
    public void initialize() {
        setupToolbar("Añade tus fotos");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configurePage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_album_creation, menu);

        MenuItem menuItem = menu.findItem(R.id.pics_count_item);
        MenuItemCompat.setActionView(menuItem, R.layout.view_pics_count);
        RelativeLayout rootView = (RelativeLayout)MenuItemCompat.getActionView(menuItem);
        picsCountView = (TextView)rootView.findViewById(R.id.view_pics_count);
        updatePicsCount();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                ImageCroppingActivity_.intent(PageCreationActivity.this).pictureUri(Uri.parse("file://" + temporalPhotoFile.getPath())).startForResult(IMAGE_CROPPING_REQUEST);
                break;
            case GALLERY_REQUEST:
                break;
            case IMAGE_CROPPING_REQUEST:
                String pathToCroppedFile = data.getStringExtra(ImageCroppingActivity.CROPPED_FILE_PATH);
                if (currentHandlingPicTag != null) {
                    ImageView picImageView = (ImageView)pageLayout.findViewWithTag(currentHandlingPicTag);
                    if (picImageView != null) {
                        picImageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(new File(pathToCroppedFile), 150, 150));
                        if (currentNumberOfPics < numOfPics)
                            currentNumberOfPics++;
                        updatePicsCount();
                    }
                }
                break;
        }
    }

    private void updatePicsCount() {
        if (picsCountView != null)
            picsCountView.setText(currentNumberOfPics + " / " + numOfPics);
    }

    private void configurePage() {
        AlbumPageView albumPageView = new AlbumPageView(PageCreationActivity.this, numOfPics, new AlbumPageView.OnPagePictureClickListener() {
            @Override
            public void onClick(ImageView imageView) {
                currentHandlingPicTag = (String)imageView.getTag();
                showAddPictureDialog();
            }

            @Override
            public void onLongClick(ImageView imageView) {

            }
        });
        pageLayout.addView(albumPageView);
    }

    @Click(R.id.page_creation_save_button)
    public void onSavePageButtonClick() {

    }

    @Click(R.id.page_creation_send_button)
    public void onSendPageButtonClick() {

    }

    private void showAddPictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PageCreationActivity.this);
        builder.setTitle("Añadir fotografía");
        builder.setMessage("Seleccione de dónde quiere añadir la fotografía.");
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Galería", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO Extract pictures from gallery
            }
        });
        builder.setPositiveButton("Cámara", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startCamera();
            }
        });
        builder.create().show();
    }

    private void startCamera() {
        temporalPhotoFile = ImageUtils.getOutputMediaTemporalFile();
        if (!temporalPhotoFile.getAbsolutePath().isEmpty()) {
            Uri outputUri = Uri.fromFile(temporalPhotoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

}
