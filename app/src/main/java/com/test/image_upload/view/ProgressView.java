package com.test.image_upload.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.test.image_upload.R;

public class ProgressView extends RelativeLayout {

    private ProgressBar progressBar;
    private ImageView progressCheck;

    public ProgressView(Context context) {
        super(context);
        initialize(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_progress, this, true);

        progressBar = (ProgressBar)getChildAt(0).findViewById(R.id.view_progress_bar);
        progressCheck = (ImageView)getChildAt(0).findViewById(R.id.view_progress_check);
    }

    public void setProgress(int progress) {
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progress);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        if (progress == 100) {
            progressCheck.setVisibility(View.VISIBLE);
        }
    }

}
