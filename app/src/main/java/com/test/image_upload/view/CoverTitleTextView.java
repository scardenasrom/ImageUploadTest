package com.test.image_upload.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.test.image_upload.util.OnDragTouchListener;

public class CoverTitleTextView extends TextView {

    private View anchorView;
    private OnDragTouchListener.OnDraggableViewClickListener onDraggableViewClickListener;

    public CoverTitleTextView(Context context, View anchorView, OnDragTouchListener.OnDraggableViewClickListener onDraggableViewClickListener) {
        super(context);
        this.anchorView = anchorView;
        this.onDraggableViewClickListener = onDraggableViewClickListener;
        refreshTouchListener();
    }

    public void refreshTouchListener() {
        OnDragTouchListener onDragTouchListener = new OnDragTouchListener(this, anchorView);
        onDragTouchListener.setOnClickAction(onDraggableViewClickListener);
        setOnTouchListener(onDragTouchListener);
    }

    public float getCenterX() {
        return getX() + getWidth()/2;
    }

    public float getCenterY() {
        return getY() + getHeight()/2;
    }

}
