package com.test.image_upload.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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
import com.test.image_upload.util.ColorUtils;
import com.thebluealliance.spectrum.SpectrumPalette;

public class CoverTitleDialog extends Dialog implements View.OnClickListener, View.OnLongClickListener, SpectrumPalette.OnColorSelectedListener {

    private LinearLayout mainLayout;
    private TextInputEditText editText;
    private RelativeLayout textColorButton;
    private TextView textColorLabel;
    private View textColorBox;
    private AppCompatButton cancelButton;
    private AppCompatButton acceptButton;

    private LinearLayout colorLayout;
    private ColorPicker colorPicker;
    private AppCompatButton colorNegativeButton;
    private AppCompatButton colorPositiveButton;

    private LinearLayout paletteLayout;
    private SpectrumPalette colorPalette;
    private AppCompatButton paletteNegativeButton;
    private AppCompatButton palettePositiveButton;
    private Integer paletteSelectedColor;

    private Context context;
    private CoverTitle oldCoverTitle;
    private OnCoverTitleClickListener onCoverTitleClickListener;

    public CoverTitleDialog(Context context, int themeResId, CoverTitle oldCoverTitle, OnCoverTitleClickListener onCoverTitleClickListener) {
        super(context, themeResId);
        this.context = context;
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

        setupMainLayout();
        setupColorPickerLayout();
        setupColorPaletteLayout();
    }

    @Override
    public void onClick(View view) {
        if (view == textColorButton) {
            showPaletteLayout();
        } else if (view == colorNegativeButton) {
            hideColorLayout();
        } else if (view == colorPositiveButton) {
            selectColor();
        } else if (view == paletteNegativeButton) {
            hideColorLayout();
        } else if (view == palettePositiveButton) {
            selectColor2();
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

    @Override
    public void onColorSelected(@ColorInt int color) {
        paletteSelectedColor = color;
    }

    @Override
    public boolean onLongClick(View view) {
        if (view == textColorButton) {
            showColorLayout();
        }
        return true;
    }

    private void setupColorButton(int color) {
        textColorLabel.setTextColor(color);
        textColorBox.setBackgroundColor(color);
        if (ColorUtils.isColorDark(color)) {
            textColorButton.setBackground(context.getResources().getDrawable(R.drawable.white_button));
        } else {
            textColorButton.setBackground(context.getResources().getDrawable(R.drawable.primary_button));
        }
    }

    private void setupMainLayout() {
        mainLayout = (LinearLayout)findViewById(R.id.cover_title_dialog_main_layout);
        editText = (TextInputEditText)findViewById(R.id.cover_title_dialog_edit_text);
        editText.setText(oldCoverTitle.getTitle());
        editText.setSelection(editText.getText().toString().length());
        textColorButton = (RelativeLayout)findViewById(R.id.cover_title_dialog_text_color_button);
        textColorLabel = (TextView)findViewById(R.id.cover_title_dialog_text_color_label);
        textColorBox = findViewById(R.id.cover_title_dialog_text_color_box);
        if (oldCoverTitle.getColor() != null) {
            setupColorButton(oldCoverTitle.getColor());

        }
        cancelButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_negative_button);
        acceptButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_positive_button);
        textColorButton.setOnClickListener(this);
        textColorButton.setOnLongClickListener(this);
        cancelButton.setOnClickListener(this);
        acceptButton.setOnClickListener(this);
    }

    private void setupColorPickerLayout() {
        colorLayout = (LinearLayout)findViewById(R.id.cover_title_dialog_color_layout);
        colorPicker = (ColorPicker)findViewById(R.id.cover_title_dialog_color_picker);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.cover_title_dialog_color_saturation_bar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.cover_title_dialog_color_value_bar);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);
        colorNegativeButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_color_negative_button);
        colorPositiveButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_color_positive_button);
        colorNegativeButton.setOnClickListener(this);
        colorPositiveButton.setOnClickListener(this);
    }

    private void setupColorPaletteLayout() {
        paletteLayout = (LinearLayout)findViewById(R.id.cover_title_dialog_palette_layout);
        colorPalette = (SpectrumPalette)findViewById(R.id.cover_title_dialog_color_palette);
        paletteNegativeButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_palette_negative_button);
        palettePositiveButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_palette_positive_button);
        colorPalette.setOnColorSelectedListener(this);
        paletteNegativeButton.setOnClickListener(this);
        palettePositiveButton.setOnClickListener(this);
    }

    private void showColorLayout() {
        mainLayout.setVisibility(View.GONE);
        colorLayout.setVisibility(View.VISIBLE);
        colorPicker.setOldCenterColor(textColorLabel.getCurrentTextColor());
        colorPicker.setNewCenterColor(textColorLabel.getCurrentTextColor());
        colorPicker.setColor(textColorLabel.getCurrentTextColor());
    }

    private void showPaletteLayout() {
        mainLayout.setVisibility(View.GONE);
        paletteLayout.setVisibility(View.VISIBLE);
        colorPalette.setSelectedColor(textColorLabel.getCurrentTextColor());
    }

    private void hideColorLayout() {
        mainLayout.setVisibility(View.VISIBLE);
        colorLayout.setVisibility(View.GONE);
        paletteLayout.setVisibility(View.GONE);
    }

    private void selectColor() {
        int selectedColor = colorPicker.getColor();
        mainLayout.setVisibility(View.VISIBLE);
        colorLayout.setVisibility(View.GONE);
        setupColorButton(selectedColor);
//        textColorLabel.setTextColor(selectedColor);
//        textColorBox.setBackgroundColor(selectedColor);
    }

    private void selectColor2() {
        mainLayout.setVisibility(View.VISIBLE);
        paletteLayout.setVisibility(View.GONE);
        if (paletteSelectedColor != null) {
            setupColorButton(paletteSelectedColor);
//            textColorLabel.setTextColor(paletteSelectedColor);
//            textColorBox.setBackgroundColor(paletteSelectedColor);
        }
    }

    public interface OnCoverTitleClickListener {
        void onCancel(Dialog dialog);
        void onAccept(Dialog dialog, CoverTitle coverTitle);
    }

}
