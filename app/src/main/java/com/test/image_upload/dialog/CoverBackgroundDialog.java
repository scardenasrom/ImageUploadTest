package com.test.image_upload.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.test.image_upload.R;
import com.test.image_upload.util.ColorUtils;
import com.thebluealliance.spectrum.SpectrumPalette;

public class CoverBackgroundDialog extends Dialog implements View.OnClickListener, View.OnLongClickListener, SpectrumPalette.OnColorSelectedListener {

    private LinearLayout mainLayout;
    private TextView backgroundLabel;
    private AppCompatButton mainNegativeButton;
    private AppCompatButton mainPositiveButton;

    private LinearLayout colorPickerLayout;
    private ColorPicker colorPicker;
    private AppCompatButton colorPickerNegativeButton;
    private AppCompatButton colorPickerPositiveButton;

    private LinearLayout paletteLayout;
    private SpectrumPalette palette;
    private AppCompatButton paletteNegativeButton;
    private AppCompatButton palettePositiveButton;

    private int selectedColor;

    private Context context;
    private int oldColor;
    private OnBackgroundColorClickListener onBackgroundColorClickListener;

    public CoverBackgroundDialog(Context context, int themeResId, int oldColor, OnBackgroundColorClickListener onBackgroundColorClickListener) {
        super(context, themeResId);
        this.context = context;
        this.oldColor = oldColor;
        this.selectedColor = oldColor;
        this.onBackgroundColorClickListener = onBackgroundColorClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_cover_background);

        setupMainLayout();
        setupColorPickerLayout();
        setupPaletteLayout();
    }

    @Override
    public void onClick(View view) {
        if (view == backgroundLabel) {
            showPaletteLayout();
        } else if (view == mainNegativeButton) {
            onBackgroundColorClickListener.onCancel(this);
        } else if (view == mainPositiveButton) {
            onBackgroundColorClickListener.onAccept(this, selectedColor);
        } else if (view == colorPickerNegativeButton) {
            hideColorLayout();
        } else if (view == colorPickerPositiveButton) {
            selectColorFromPicker();
        } else if (view == paletteNegativeButton) {
            hideColorLayout();
        } else if (view == palettePositiveButton) {
            selectColorFromPalette();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view == backgroundLabel) {
            showColorPickerLayout();
        }
        return true;
    }

    private void setupLabelColor(int color) {
        backgroundLabel.setBackgroundColor(color);
        if (ColorUtils.isColorDark(color)) {
            backgroundLabel.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            backgroundLabel.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    private void setupMainLayout() {
        mainLayout = (LinearLayout)findViewById(R.id.dialog_cover_background_main_layout);
        backgroundLabel = (TextView)findViewById(R.id.dialog_cover_background_color_label);
        mainNegativeButton = (AppCompatButton)findViewById(R.id.dialog_cover_background_negative_button);
        mainPositiveButton = (AppCompatButton)findViewById(R.id.dialog_cover_background_positive_button);
        backgroundLabel.setOnClickListener(this);
        backgroundLabel.setOnLongClickListener(this);
        mainNegativeButton.setOnClickListener(this);
        mainPositiveButton.setOnClickListener(this);
        setupLabelColor(oldColor);
    }

    private void setupColorPickerLayout() {
        colorPickerLayout = (LinearLayout)findViewById(R.id.dialog_cover_background_color_picker_layout);
        colorPicker = (ColorPicker)findViewById(R.id.dialog_cover_background_color_picker);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.dialog_cover_background_saturation_bar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.dialog_cover_background_value_bar);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);
        colorPicker.setOldCenterColor(oldColor);
        colorPicker.setNewCenterColor(oldColor);
        colorPicker.setColor(oldColor);
        colorPickerNegativeButton = (AppCompatButton)findViewById(R.id.dialog_cover_background_color_negative_button);
        colorPickerPositiveButton = (AppCompatButton)findViewById(R.id.dialog_cover_background_color_positive_button);
        colorPickerNegativeButton.setOnClickListener(this);
        colorPickerPositiveButton.setOnClickListener(this);
    }

    private void setupPaletteLayout() {
        paletteLayout = (LinearLayout)findViewById(R.id.dialog_cover_background_palette_layout);
        palette = (SpectrumPalette)findViewById(R.id.dialog_cover_background_palette);
        paletteNegativeButton = (AppCompatButton)findViewById(R.id.dialog_cover_background_palette_negative_button);
        palettePositiveButton = (AppCompatButton)findViewById(R.id.dialog_cover_background_palette_positive_button);
        palette.setSelectedColor(oldColor);
        palette.setOnColorSelectedListener(this);
        paletteNegativeButton.setOnClickListener(this);
        palettePositiveButton.setOnClickListener(this);
    }

    private void showPaletteLayout() {
        mainLayout.setVisibility(View.GONE);
        paletteLayout.setVisibility(View.VISIBLE);
        ColorDrawable cd = (ColorDrawable) backgroundLabel.getBackground();
        int colorCode = cd.getColor();
        palette.setSelectedColor(colorCode);
    }

    private void hideColorLayout() {
        mainLayout.setVisibility(View.VISIBLE);
        paletteLayout.setVisibility(View.GONE);
        colorPickerLayout.setVisibility(View.GONE);
    }

    private void showColorPickerLayout() {
        mainLayout.setVisibility(View.GONE);
        colorPickerLayout.setVisibility(View.VISIBLE);
    }

    private void selectColorFromPicker() {
        selectedColor = colorPicker.getColor();
        setupLabelColor(selectedColor);
        hideColorLayout();
    }

    private void selectColorFromPalette() {
        setupLabelColor(selectedColor);
        hideColorLayout();
    }

    @Override
    public void onColorSelected(@ColorInt int color) {
        selectedColor = color;
    }

    public interface OnBackgroundColorClickListener {
        void onCancel(Dialog dialog);
        void onAccept(Dialog dialog, int color);
    }

}
