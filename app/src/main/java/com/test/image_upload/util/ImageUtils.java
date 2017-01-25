package com.test.image_upload.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.test.image_upload.model.Resolution;

import java.io.File;

public class ImageUtils {

    public static final int REQ_WIDTH_FOR_PRINT = 1024;
    public static final int REQ_HEIGHT_FOR_PRINT = 768;

    public static Bitmap decodeSampledBitmapFromFile(File file, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        options.inSampleSize = calculateInSamplesize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    private static int calculateInSamplesize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while (((halfHeight / inSampleSize) >= reqHeight) && ((halfWidth / inSampleSize) >= reqWidth)) {
                inSampleSize *= 2;
            }

        }
        return inSampleSize;
    }

    //TODO Find a proper way to store files in private storage
    public static File getOutputMediaTemporalFile() {
        return new File(Environment.getExternalStorageDirectory().getPath() + File.separator + System.currentTimeMillis() + ".png");
    }

    public static Resolution getPictureResolution(File file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return new Resolution(options.outWidth, options.outHeight);
    }

}
