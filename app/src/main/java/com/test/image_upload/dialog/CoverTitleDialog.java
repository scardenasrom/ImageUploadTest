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

import com.test.image_upload.R;

public class CoverTitleDialog extends Dialog implements View.OnClickListener {

    private TextInputEditText editText;
    private AppCompatButton cancelButton;
    private AppCompatButton acceptButton;

    private String currentTitle;
    private OnCoverTitleClickListener onCoverTitleClickListener;

    public CoverTitleDialog(Context context, int themeResId, String currentTitle, OnCoverTitleClickListener onCoverTitleClickListener) {
        super(context, themeResId);
        this.currentTitle = currentTitle;
        this.onCoverTitleClickListener = onCoverTitleClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_cover_title);

//        inputLayout = (TextInputLayout)findViewById(R.id.cover_title_dialog_input_layout);
        editText = (TextInputEditText)findViewById(R.id.cover_title_dialog_edit_text);
        editText.setText(currentTitle);
        editText.setSelection(editText.getText().toString().length());
        cancelButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_negative_button);
        acceptButton = (AppCompatButton)findViewById(R.id.cover_title_dialog_positive_button);
        cancelButton.setOnClickListener(this);
        acceptButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == cancelButton) {
            onCoverTitleClickListener.onCancel(this);
        } else if (view == acceptButton) {
            String title = editText.getText().toString();
            if (!TextUtils.isEmpty(title.trim()))
                onCoverTitleClickListener.onAccept(this, title.trim());
        }
    }

    public interface OnCoverTitleClickListener {
        void onCancel(Dialog dialog);
        void onAccept(Dialog dialog, String title);
    }

}
