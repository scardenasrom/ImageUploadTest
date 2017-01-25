package com.test.image_upload.util;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OnDragTouchListener implements View.OnTouchListener {

    private static final float CLICK_ACTION_THRESHOLD = 5;
    private static final long LONG_CLICK_ACTION_DURATION = 1000;

    /**
     * Callback used to indicate when the drag is finished
     */
    public interface OnDragActionListener {
        /**
         * Called when drag event is started
         *
         * @param view The view dragged
         */
        void onDragStart(View view);

        /**
         * Called when drag event is completed
         *
         * @param view The view dragged
         */
        void onDragEnd(View view);
    }

    private View mView;
    private View mParent;
    private boolean isDragging;
    private boolean isInitialized = false;

    private int width;
    private float xWhenAttached;
    private float maxLeft;
    private float maxRight;
    private float dX;

    private int height;
    private float yWhenAttached;
    private float maxTop;
    private float maxBottom;
    private float dY;

    private float startX;
    private float endX;
    private float startY;
    private float endY;
    private long startTime;
    private long endTime;

    private OnDragActionListener mOnDragActionListener;
    private OnDraggableViewClickListener onDraggableViewClickListener;

    public OnDragTouchListener(View view) {
        this(view, (View) view.getParent(), null);
    }

    public OnDragTouchListener(View view, View parent) {
        this(view, parent, null);
    }

    public OnDragTouchListener(View view, OnDragActionListener onDragActionListener) {
        this(view, (View) view.getParent(), onDragActionListener);
    }

    public OnDragTouchListener(View view, View parent, OnDragActionListener onDragActionListener) {
        initListener(view, parent);
        setOnDragActionListener(onDragActionListener);
    }

    public void setOnDragActionListener(OnDragActionListener onDragActionListener) {
        mOnDragActionListener = onDragActionListener;
    }

    public void setOnClickAction(OnDraggableViewClickListener onDraggableViewClickListener) {
        this.onDraggableViewClickListener = onDraggableViewClickListener;
    }

    public void initListener(View view, View parent) {
        mView = view;
        mParent = parent;
        isDragging = false;
        isInitialized = false;
    }

    public void updateBounds() {
        updateViewBounds();
        updateParentBounds();
        isInitialized = true;
    }

    public void updateViewBounds() {
        width = mView.getWidth();
        xWhenAttached = mView.getX();
        dX = 0;

        height = mView.getHeight();
        yWhenAttached = mView.getY();
        dY = 0;
    }

    public void updateParentBounds() {
        maxLeft = 0;
        maxRight = maxLeft + mParent.getWidth();

        maxTop = 0;
        maxBottom = maxTop + mParent.getHeight();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDragging) {
            handler.removeCallbacks(mLongPressed);
            float[] bounds = new float[4];
            // LEFT
            bounds[0] = event.getRawX() + dX;
            if (bounds[0] < maxLeft) {
                bounds[0] = maxLeft;
            }
            // RIGHT
            bounds[2] = bounds[0] + width;
            if (bounds[2] > maxRight) {
                bounds[2] = maxRight;
                bounds[0] = bounds[2] - width;
            }
            // TOP
            bounds[1] = event.getRawY() + dY;
            if (bounds[1] < maxTop) {
                bounds[1] = maxTop;
            }
            // BOTTOM
            bounds[3] = bounds[1] + height;
            if (bounds[3] > maxBottom) {
                bounds[3] = maxBottom;
                bounds[1] = bounds[3] - height;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    endX = event.getRawX();
                    endY = event.getRawY();
                    endTime = event.getEventTime();
                    handleMotionEventEnd();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mView.animate().x(bounds[0]).setDuration(0).start();
                    mView.animate().y(bounds[1]).setDuration(0).start();
                    break;
            }
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDragging = true;
                    if (!isInitialized) {
                        updateBounds();
                    }
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    startX = event.getRawX();
                    startY = event.getRawY();
                    handler.postDelayed(mLongPressed, LONG_CLICK_ACTION_DURATION);
                    Log.d("Dragging", "startX: " + startX + ", startY: " + startY);
                    if (mOnDragActionListener != null) {
                        mOnDragActionListener.onDragStart(mView);
                    }
                    return true;
            }
        }
        return false;
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            onLongClickAction();
        }
    };

    private void handleMotionEventEnd() {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > CLICK_ACTION_THRESHOLD || differenceY > CLICK_ACTION_THRESHOLD) {
            onDragFinish();
        } else {
            onClickAction();
        }
    }

    private void onDragFinish() {
        if (mOnDragActionListener != null) {
            mOnDragActionListener.onDragEnd(mView);
        }

        dX = 0;
        dY = 0;
        isDragging = false;
    }

    private void onClickAction() {
        onDraggableViewClickListener.onClick(mView);
        dX = 0;
        dY = 0;
        isDragging = false;
    }

    private void onLongClickAction() {
        onDraggableViewClickListener.onLongClick(mView);
        dX = 0;
        dY = 0;
        isDragging = false;
    }

    public interface OnDraggableViewClickListener {
        void onClick(View v);
        void onLongClick(View v);
    }

}
