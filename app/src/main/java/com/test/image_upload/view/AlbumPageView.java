package com.test.image_upload.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.test.image_upload.R;

public class AlbumPageView extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {

    private static final String PIC_TAG = "picture";

    private final Context context;
    private OnPagePictureClickListener onPagePictureClickListener;

    public AlbumPageView(Context context, int numOfPics, OnPagePictureClickListener onPagePictureClickListener) {
        super(context);
        this.context = context;
        this.onPagePictureClickListener = onPagePictureClickListener;
        if (numOfPics == 1) {
            initializeOne();
        } else {
            initializeTwo();
        }
    }

    public AlbumPageView(Context context, int numOfPics, AttributeSet attrs, Context context1, OnPagePictureClickListener onPagePictureClickListener) {
        super(context, attrs);
        this.context = context1;
        this.onPagePictureClickListener = onPagePictureClickListener;
        if (numOfPics == 1) {
            initializeOne();
        } else {
            initializeTwo();
        }
    }

    public AlbumPageView(Context context, int numOfPics, AttributeSet attrs, int defStyleAttr, Context context1, OnPagePictureClickListener onPagePictureClickListener) {
        super(context, attrs, defStyleAttr);
        this.context = context1;
        this.onPagePictureClickListener = onPagePictureClickListener;
        if (numOfPics == 1) {
            initializeOne();
        } else {
            initializeTwo();
        }
    }

    private void initializeOne() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_page_one, this, true);

        ImageView pageOnePicture = (ImageView) getChildAt(0).findViewById(R.id.view_page_one_picture);
        pageOnePicture.setTag(PIC_TAG + 0);
        pageOnePicture.setOnClickListener(this);
        pageOnePicture.setOnLongClickListener(this);
    }

    private void initializeTwo() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_page_two, this, true);

        ImageView pageTwoPictureOne = (ImageView) getChildAt(0).findViewById(R.id.view_page_two_picture_one);
        ImageView pageTwoPictureTwo = (ImageView) getChildAt(0).findViewById(R.id.view_page_two_picture_two);
        pageTwoPictureOne.setTag(PIC_TAG + 0);
        pageTwoPictureTwo.setTag(PIC_TAG + 1);
        pageTwoPictureOne.setOnClickListener(this);
        pageTwoPictureOne.setOnLongClickListener(this);
        pageTwoPictureTwo.setOnClickListener(this);
        pageTwoPictureTwo.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ImageView clickedImage = (ImageView)view;
        onPagePictureClickListener.onClick(clickedImage);
    }

    @Override
    public boolean onLongClick(View view) {
        ImageView clickedImage = (ImageView)view;
        onPagePictureClickListener.onLongClick(clickedImage);
        return true;
    }

    public interface OnPagePictureClickListener {
        void onClick(ImageView imageView);
        void onLongClick(ImageView imageView);
    }

}
