package com.test.image_upload.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.test.image_upload.R;
import com.test.image_upload.model.CoverTitle;

public class CoverTitleDialog extends Dialog implements View.OnClickListener {

    private LinearLayout mainLayout;
    private TextInputEditText editText;
    private RelativeLayout textColorButton;
    private TextView textColorLabel;
    private View textColorBox;
    private AppCompatButton cancelButton;
    private AppCompatButton acceptButton;

    private LinearLayout colorLayout;
    private ColorPicker colorPicker;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private AppCompatButton colorNegativeButton;
    private AppCompatButton colorPositiveButton;

    private CoverTitle oldCoverTitle;
    private OnCoverTitleClickListener onCoverTitleClickListener;

    public CoverTitleDialog(Context context, int themeResId, CoverTitle oldCoverTitle, OnCoverTitleClickListener onCoverTitleClickListener) {
        super(context, themeResId);
        this.oldCoverTitle = oldCoverTitle;
        this.onCoverTitleClickListener = onCoverTitleClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_cover_title);

        mainLayout = (LinearLayout)findViewById(R.id.cover_title_dialog_main_layout);
        editText = (TextInputEditText)findViewById(R.id.cover_title_dialog_edit_text);
        editText.setText(oldCoverTitle.getTitle());
        editText.setSelection(editText.getText().toString().length());
        textColorButton = (RelativeLayout)findViewById(R.id.cover_title_dialog_text_color_button);
        textColorLabel = (TextView)findViewById(R.id.cover_title_dialog_text_color_label);
        textColorBox = findViewById(R.id.cover_title_dialog_text_color_box);
        if (oldCoverTitle.getColor() != null) {
            textColorLabel.setTextColor(oldCoverTitle.getColor());
            textColorBox.setBackgroundColor(oldCoverTitle.getColor());
        }
        cancelButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_negative_button);
        acceptButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_positive_button);
        textColorButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        acceptButton.setOnClickListener(this);

        colorLayout = (LinearLayout)findViewById(R.id.cover_title_dialog_color_layout);
        colorPicker = (ColorPicker)findViewById(R.id.cover_title_dialog_color_picker);
        saturationBar = (SaturationBar)findViewById(R.id.cover_title_dialog_color_saturation_bar);
        valueBar = (ValueBar)findViewById(R.id.cover_title_dialog_color_value_bar);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);
        colorNegativeButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_color_negative_button);
        colorPositiveButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_color_positive_button);
        colorNegativeButton.setOnClickListener(this);
        colorPositiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == textColorButton) {
            showColorLayout();
        } else if (view == colorNegativeButton) {
            hideColorLayout();
        } else if (view == colorPositiveButton) {
            selectColor();
        } else if (view == cancelButton) {
            onCoverTitleClickListener.onCancel(this);
        } else if (view == acceptButton) {
            String title = editText.getText().toString();
            if (!TextUtils.isEmpty(title.trim())) {
                CoverTitle coverTitle = new CoverTitle();
                coverTitle.setTitle(title.trim());
                coverTitle.setColor(textColorLabel.getCurrentTextColor());
                onCoverTitleClickListener.onAccept(this, coverTitle);
            }
        }
    }

    private void showColorLayout() {
        mainLayout.setVisibility(View.GONE);
        colorLayout.setVisibility(View.VISIBLE);

        if (oldCoverTitle.getColor() != null) {
            colorPicker.setOldCenterColor(oldCoverTitle.getColor());
            colorPicker.setNewCenterColor(oldCoverTitle.getColor());
            colorPicker.setColor(oldCoverTitle.getColor());
        } else {
            colorPicker.setOldCenterColor(textColorLabel.getCurrentTextColor());
            colorPicker.setNewCenterColor(textColorLabel.getCurrentTextColor());
            colorPicker.setColor(textColorLabel.getCurrentTextColor());
        }
    }

    private void hideColorLayout() {
        mainLayout.setVisibility(View.VISIBLE);
        colorLayout.setVisibility(View.GONE);
    }

    private void selectColor() {
        int selectedColor = colorPicker.getColor();
        mainLayout.setVisibility(View.VISIBLE);
        colorLayout.setVisibility(View.GONE);
        textColorLabel.setTextColor(selectedColor);
        textColorBox.setBackgroundColor(selectedColor);
    }

    public interface OnCoverTitleClickListener {
        void onCancel(Dialog dialog);
        void onAccept(Dialog dialog, CoverTitle coverTitle);
    }

}
