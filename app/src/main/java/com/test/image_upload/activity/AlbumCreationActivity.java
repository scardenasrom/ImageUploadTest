package com.test.image_upload.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.image_upload.R;
import com.test.image_upload.adapter.AlbumPicturesAdapter;
import com.test.image_upload.model.Album;
import com.test.image_upload.model.AlbumPictureDTO;
import com.test.image_upload.service.AlbumSenderService;
import com.test.image_upload.util.ImageUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_album_creation)
public class AlbumCreationActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int COVER_REQUEST = 2;
    private static final int IMAGE_CROPPING_REQUEST = 3;

    @ViewById(R.id.toolbar) Toolbar toolbar;
    @ViewById(R.id.album_creation_recycler_view) RecyclerView picturesRecyclerView;
    @ViewById(R.id.album_creation_progress_layout) RelativeLayout progressLayout;
    @ViewById(R.id.album_creation_progress_bar) ProgressBar progressBar;
    @ViewById(R.id.album_creation_progress_check) ImageView progressCheck;
    @ViewById(R.id.album_creation_add_picture_button) AppCompatButton addPictureButton;

    @Extra("totalNumberOfPics") int totalNumberOfPics = 0;
    private int currentNumberOfPics = 0;

    private boolean exiting = false;

    private TextView picsCountTextView;

    private File temporalPhotoFile;

    private AlbumPicturesAdapter albumPicturesAdapter;
    private List<AlbumPictureDTO> albumPictureDTOs;

    AlbumSenderProgressReceiver receiver;

    @AfterViews
    public void initialize() {
        setSupportActionBar(toolbar);
        setupRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {

                    ImageCroppingActivity_.intent(AlbumCreationActivity.this).pictureUri(Uri.parse("file://" + temporalPhotoFile.getPath())).startForResult(IMAGE_CROPPING_REQUEST);

//                    CropImage.activity(Uri.parse("file://" + temporalPhotoFile.getPath()))
//                            .setGuidelines(CropImageView.Guidelines.ON)
//                            .start(AlbumCreationActivity.this);

//                    AlbumPictureDTO newAlbumPictureDTO = new AlbumPictureDTO(temporalPhotoFile);
//                    newAlbumPictureDTO.setResolution(ImageUtils.getPictureResolution(newAlbumPictureDTO.getFile()));
//                    albumPictureDTOs.add(newAlbumPictureDTO);
//                    albumPicturesAdapter.setAlbumPictureDTOs(albumPictureDTOs);
//                    currentNumberOfPics++;
//                    updatePicsCount();

//                    ImageManipulationActivity_.intent(AlbumCreationActivity.this).picture(newAlbumPictureDTO).start();

                }
                break;
            case COVER_REQUEST:
                break;
            case IMAGE_CROPPING_REQUEST:
                if (resultCode == RESULT_OK) {
                    String pathToCroppedFile = data.getStringExtra(ImageCroppingActivity.CROPPED_FILE_PATH);
                    File croppedFile = new File(pathToCroppedFile);
                    AlbumPictureDTO newAlbumPictureDTO = new AlbumPictureDTO(croppedFile);
                    newAlbumPictureDTO.setResolution(ImageUtils.getPictureResolution(croppedFile));
                    albumPictureDTOs.add(newAlbumPictureDTO);
                    albumPicturesAdapter.setAlbumPictureDTOs(albumPictureDTOs);
                    currentNumberOfPics++;
                    updatePicsCount();
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    temporalPhotoFile = new File(resultUri.getPath());
                    AlbumPictureDTO newAlbumPictureDTO = new AlbumPictureDTO(temporalPhotoFile);
                    newAlbumPictureDTO.setResolution(ImageUtils.getPictureResolution(temporalPhotoFile));
                    albumPictureDTOs.add(newAlbumPictureDTO);
                    albumPicturesAdapter.setAlbumPictureDTOs(albumPictureDTOs);
                    currentNumberOfPics++;
                    updatePicsCount();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_album_creation, menu);

        MenuItem menuItem = menu.findItem(R.id.pics_count_item);
        MenuItemCompat.setActionView(menuItem, R.layout.view_pics_count);
        RelativeLayout rootView = (RelativeLayout)MenuItemCompat.getActionView(menuItem);
        picsCountTextView = (TextView)rootView.findViewById(R.id.view_pics_count);
        updatePicsCount();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (currentNumberOfPics > 0) {
            if (exiting) {
                super.onBackPressed();
            } else {
                exiting = true;
                Snackbar confirmExitSnackbar = Snackbar.make(picturesRecyclerView, "Pulse atrás de nuevo para salir y descartar el álbum", 3000);
                confirmExitSnackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        exiting = false;
                    }
                });
                confirmExitSnackbar.show();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerAlbumReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null)
            unregisterReceiver(receiver);
    }

    private void registerAlbumReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AlbumSenderService.ACTION_PROGRESS);
        filter.addAction(AlbumSenderService.ACTION_END);
        receiver = new AlbumSenderProgressReceiver();
        registerReceiver(receiver, filter);
    }

    private void setupRecyclerView() {
        albumPictureDTOs = new ArrayList<>();
        picturesRecyclerView.setHasFixedSize(true);
        picturesRecyclerView.setLayoutManager(new LinearLayoutManager(AlbumCreationActivity.this));
        picturesRecyclerView.addItemDecoration(new DividerItemDecoration(AlbumCreationActivity.this, DividerItemDecoration.HORIZONTAL));
        picturesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        albumPicturesAdapter = new AlbumPicturesAdapter(albumPictureDTOs, new AlbumPicturesAdapter.OnPictureClickListener() {
            @Override
            public void onEditClick(AlbumPictureDTO albumPictureDTO) {

            }

            @Override
            public void onDeleteClick(AlbumPictureDTO albumPictureDTO) {
                if (albumPictureDTOs.contains(albumPictureDTO)) {
                    int position = albumPictureDTOs.indexOf(albumPictureDTO);
                    albumPictureDTOs.remove(position);
                    albumPicturesAdapter.notifyItemRemoved(position);
                    currentNumberOfPics--;
                    updatePicsCount();
                }
            }
        });
        picturesRecyclerView.setAdapter(albumPicturesAdapter);
    }

    @Click(R.id.album_creation_add_picture_button)
    public void onAddPictureButtonClick() {
        showAddPictureDialog();
    }

    @Click(R.id.album_creation_send_album_button)
    public void onSendAlbumButtonClick() {
        sendAlbum();
    }

    private void showAddPictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AlbumCreationActivity.this);
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

    private void updatePicsCount() {
        if (picsCountTextView != null) {
            picsCountTextView.setText(currentNumberOfPics + " / " + totalNumberOfPics + " fotografías");
        }
        if (currentNumberOfPics == totalNumberOfPics) {
            addPictureButton.setEnabled(false);
        } else if (currentNumberOfPics < totalNumberOfPics) {
            addPictureButton.setEnabled(true);
        }
    }

    private void sendAlbum() {
        Album album = new Album(albumPictureDTOs);
        String albumString = (new Gson()).toJson(album, Album.class);
        Intent sendAlbumIntent = new Intent(AlbumCreationActivity.this, AlbumSenderService.class);
        sendAlbumIntent.putExtra(AlbumSenderService.ALBUM_EXTRA, albumString);
        startService(sendAlbumIntent);
        progressLayout.setVisibility(View.VISIBLE);
    }

    public class AlbumSenderProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AlbumSenderService.ACTION_PROGRESS)) {
                int progress = intent.getIntExtra(AlbumSenderService.PROGRESS_EXTRA, 0);
                progressLayout.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progress);
                animator.setDuration(200);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            } else if (intent.getAction().equalsIgnoreCase(AlbumSenderService.ACTION_END)) {
                ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", 100);
                animator.setDuration(200);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
                progressCheck.setVisibility(View.VISIBLE);
                exiting = true;
            }
        }

    }

    @Click(R.id.album_creation_cover_button)
    public void onCoverButtonClick() {
        CoverEditorActivity_.intent(AlbumCreationActivity.this).startForResult(COVER_REQUEST);
    }

}
