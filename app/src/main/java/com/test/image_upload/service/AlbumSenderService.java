package com.test.image_upload.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.test.image_upload.R;
import com.test.image_upload.activity.AlbumCreationActivity_;
import com.test.image_upload.model.Album;
import com.test.image_upload.model.AlbumPictureDTO;

import java.util.List;

public class AlbumSenderService extends IntentService {

    public static final int PROGRESS_NOTIFICATION_ID = 100;
    public static final int SUCCESS_NOTIFICATION_ID = 200;

    public static final String ACTION_PROGRESS = "intentActionProgress";
    public static final String ACTION_END = "intentActionEnd";

    public static final String ALBUM_EXTRA = "albumExtra";
    public static final String PROGRESS_EXTRA = "progressExtra";

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private int progress = 0;

    private Album album;
    private List<AlbumPictureDTO> pictures;

    public AlbumSenderService() {
        super(AlbumSenderService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(AlbumSenderService.this);
        String albumString = intent.getStringExtra(ALBUM_EXTRA);
        if (albumString != null && !albumString.isEmpty()) {
            album = (new Gson()).fromJson(albumString, Album.class);
        }

        if (album != null) {
            pictures = album.getPictures();
        }

        if (pictures != null && !pictures.isEmpty()) {
            sendPictures();
        }
    }

    private void sendPictures() {
        notifyUploadStart();
        for (AlbumPictureDTO albumPictureDTO : pictures) {
            fakeWork();
            int numOfPictures = pictures.size();
            progress = progress + (100 / numOfPictures);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ACTION_PROGRESS);
            broadcastIntent.putExtra(PROGRESS_EXTRA, progress);
            sendBroadcast(broadcastIntent);
            notifyProgress(pictures.indexOf(albumPictureDTO)+1);
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_END);
        sendBroadcast(broadcastIntent);
        cancelProgressNotification();
        notifySuccess();
    }

    //region Custom Layout Notification
    private void notifyUploadStart() {
        Intent resultIntent = new Intent(this, AlbumCreationActivity_.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(AlbumSenderService.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("Subiendo Álbum")
                .setContentText("Subiendo fotografías");
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.view_notification_progress);
        notificationView.setImageViewResource(R.id.view_notification_progress_icon, R.drawable.ic_upload);
        notificationView.setTextViewText(R.id.view_notification_progress_title, "Subiendo Álbum");
        notificationView.setTextViewText(R.id.view_notification_progress_text, "Fotos subidas: 0/" + pictures.size());
        notificationView.setProgressBar(R.id.view_notification_progress_bar, 100, 0, true);
        builder.setContent(notificationView)
                .setGroup("Images")
                .setOngoing(true)
                .setContentIntent(contentIntent);
        Notification builtNotification = builder.build();
        notificationManager.notify(PROGRESS_NOTIFICATION_ID, builtNotification);
    }

    private void notifyProgress(int numOfPics) {
        Intent resultIntent = new Intent(this, AlbumCreationActivity_.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(AlbumSenderService.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("Subiendo Álbum")
                .setContentText("Subiendo fotografías");
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.view_notification_progress);
        notificationView.setImageViewResource(R.id.view_notification_progress_icon, R.drawable.ic_upload);
        notificationView.setTextViewText(R.id.view_notification_progress_title, "Subiendo Álbum");
        notificationView.setTextViewText(R.id.view_notification_progress_text, "Fotos subidas: " + numOfPics + "/" + pictures.size());
        notificationView.setProgressBar(R.id.view_notification_progress_bar, 100, progress, false);
        builder.setContent(notificationView)
                .setGroup("Images")
                .setOngoing(true)
                .setContentIntent(contentIntent);
        Notification builtNotification = builder.build();
        notificationManager.notify(PROGRESS_NOTIFICATION_ID, builtNotification);
    }

    private void notifySuccess() {
        Intent resultIntent = new Intent(this, AlbumCreationActivity_.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(AlbumSenderService.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.ic_complete)
                .setContentTitle("Álbum subido")
                .setContentText("Fotografías subidas con éxito");
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.view_notification_success);
        notificationView.setImageViewResource(R.id.view_notification_progress_icon, R.drawable.ic_complete);
        notificationView.setTextViewText(R.id.view_notification_progress_title, "Álbum subido");
        notificationView.setTextViewText(R.id.view_notification_progress_text, "Fotografías subidas con éxito");
        builder.setContent(notificationView)
                .setGroup("Images")
                .setOngoing(false)
                .setContentIntent(contentIntent);
        Notification builtNotification = builder.build();
        notificationManager.notify(SUCCESS_NOTIFICATION_ID, builtNotification);
    }
    //endregion

    //region Android Notification
//    private void notifyUploadStart() {
//        Intent resultIntent = new Intent(this, AlbumCreationActivity_.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(AlbumSenderService.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentTitle("Subiendo álbum")
//                .setContentText("Subiendo fotografías")
//                .setContentIntent(resultPendingIntent)
//                .setSmallIcon(R.drawable.ic_upload)
//                .setGroup("Images")
//                .setProgress(100, 0, false)
//                .setOngoing(true);
//        Notification builtNotification = builder.build();
//        notificationManager.notify(PROGRESS_NOTIFICATION_ID, builtNotification);
//    }

//    private void notifyProgress() {
//        Intent resultIntent = new Intent(this, AlbumCreationActivity_.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentTitle("Subiendo álbum")
//                .setContentText("Subiendo fotografías")
//                .setContentIntent(resultPendingIntent)
//                .setSmallIcon(R.drawable.ic_upload)
//                .setGroup("Images")
//                .setProgress(100, progress, false)
//                .setAutoCancel(false)
//                .setOngoing(true);
//        Notification builtNotification = builder.build();
//        notificationManager.notify(PROGRESS_NOTIFICATION_ID, builtNotification);
//    }

//    private void notifySuccess() {
//        Intent resultIntent = new Intent(this, MainActivity_.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentTitle("Álbum subido")
//                .setContentText("Álbum subido correctamente")
//                .setContentIntent(resultPendingIntent)
//                .setSmallIcon(R.drawable.ic_complete)
//                .setProgress(0, 0, false)
//                .setOngoing(false);
//        Notification builtNotification = builder.build();
//        notificationManager.notify(SUCCESS_NOTIFICATION_ID, builtNotification);
//    }
    //endregion

    private void cancelProgressNotification() {
        notificationManager.cancel(PROGRESS_NOTIFICATION_ID);
    }

    private void fakeWork() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            Log.e("AlbumSenderService", exception.getMessage(), exception);
        }
    }

}
