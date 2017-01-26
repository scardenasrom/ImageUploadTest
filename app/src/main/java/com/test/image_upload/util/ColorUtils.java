package com.test.image_upload.util;

import android.graphics.Color;

public class ColorUtils {

    public static boolean isColorDark(int color) {
        double brightness = Color.red(color) * 0.299 +
                Color.green(color) * 0.587 +
                Color.blue(color) * 0.114;
        return brightness < 190;
    }

}
