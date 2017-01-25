package com.test.image_upload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.test.image_upload.activity.AlbumCreationActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar) Toolbar toolbar;

    private static final int SMALL_ALBUM_NUMBER_OF_PICS = 4;
    private static final int MEDIUM_ALBUM_NUMBER_OF_PICS = 6;
    private static final int LARGE_ALBUM_NUMBER_OF_PICS = 8;

    @AfterViews
    public void initialize() {
        setupToolbar("Prueba Creación de Álbum");
        checkForPermissions();
    }

    @Click(R.id.main_small_album_button)
    public void onSmallAlbumButtonClick() {
        AlbumCreationActivity_.intent(MainActivity.this).totalNumberOfPics(SMALL_ALBUM_NUMBER_OF_PICS).start();
    }

    @Click(R.id.main_medium_album_button)
    public void onMediumAlbumButtonClick() {
        AlbumCreationActivity_.intent(MainActivity.this).totalNumberOfPics(MEDIUM_ALBUM_NUMBER_OF_PICS).start();
    }

    @Click(R.id.main_large_album_button)
    public void onLargeAlbumButtonClick() {
        AlbumCreationActivity_.intent(MainActivity.this).totalNumberOfPics(LARGE_ALBUM_NUMBER_OF_PICS).start();
    }

    private void checkForPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA }, 0);
    }

}
