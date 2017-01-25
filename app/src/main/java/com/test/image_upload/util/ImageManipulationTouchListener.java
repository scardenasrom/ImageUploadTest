package com.test.image_upload.util;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class ImageManipulationTouchListener implements View.OnTouchListener {

    private static final String TAG = "ImageManipulationTouchListener";

    private static final int EVENT_NONE = 0;
    private static final int EVENT_DRAG = 1;
    private static final int EVENT_ZOOM = 2;

    private int mode = EVENT_NONE;

    private Matrix matrix = null;
    private Matrix savedMatrix = new Matrix();

    private float lastEvent[];
    private float d;

    // Zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDistance = 1f;

    private SeekBar rotationSeekbar;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageView imageView = (ImageView)view;
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

        float scale;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(imageView.getImageMatrix());
                if (matrix == null) {
                    matrix = new Matrix(imageView.getImageMatrix());
                }
                if (rotationSeekbar != null)
                    rotationSeekbar.setProgress(50);
                start.set(motionEvent.getX(), motionEvent.getY());
                mode = EVENT_DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = getEventSpace(motionEvent);
                if (oldDistance > 10f) {
                    savedMatrix.set(matrix);
                    setupMidPoint(motionEvent);
                    mode = EVENT_ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = motionEvent.getX(0);
                lastEvent[1] = motionEvent.getX(1);
                lastEvent[2] = motionEvent.getY(0);
                lastEvent[3] = motionEvent.getY(1);
                d = getEventRotationDegrees(motionEvent);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = EVENT_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == EVENT_DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate((motionEvent.getX() - start.x) * 0.8f, (motionEvent.getY() - start.y) * 0.8f);
                } else if (mode == EVENT_ZOOM && motionEvent.getPointerCount() == 2) {
                    float newDistance = getEventSpace(motionEvent);
                    matrix.set(savedMatrix);
                    if (newDistance > 2) {
                        scale = newDistance / oldDistance;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null) {
                        float newRotation = getEventRotationDegrees(motionEvent);
                        float rotation = newRotation - d;
                        matrix.postRotate(rotation, imageView.getMeasuredWidth() / 2, imageView.getMeasuredHeight() / 2);
                    }
                }
                break;
        }

        imageView.setImageMatrix(matrix);

        return true;
    }

    private float getEventSpace(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return ((Double)Math.sqrt(x * x + y * y)).floatValue() * 0.6f;
    }

    private float getEventRotationDegrees(MotionEvent motionEvent) {
        double deltaX = motionEvent.getX(0) - motionEvent.getX(1);
        double deltaY = motionEvent.getY(0) - motionEvent.getY(1);
        double radians = Math.atan2(deltaY, deltaX);
        return (float)Math.toDegrees(radians);
    }

    private void setupMidPoint(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        mid.set(x / 2, y / 2);
    }

    public void setRotationSeekbar(SeekBar rotationSeekbar) {
        this.rotationSeekbar = rotationSeekbar;
    }

}
