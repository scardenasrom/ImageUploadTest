package com.test.image_upload.activity;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import com.test.image_upload.R;
import com.test.image_upload.dialog.CoverTitleDialog;
import com.test.image_upload.util.OnDragTouchListener;
import com.test.image_upload.view.CoverTitleTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_cover_editor)
public class CoverEditorActivity extends AppCompatActivity {

    private static final String TITLE_TAG = "title";

    private String oldTitle;
    private float oldTitleX;
    private float oldTitleY;

    @ViewById(R.id.cover_editor_canvas) RelativeLayout coverCanvas;

    @AfterViews
    public void initialize() {

    }

    @Click(R.id.cover_editor_color_button)
    public void onColorButtonClick() {

    }

    @Click(R.id.cover_editor_title_button)
    public void onTitleButtonClick() {
        String currentTitle = "";
        CoverTitleTextView previousTitle = (CoverTitleTextView)coverCanvas.findViewWithTag(TITLE_TAG);
        if (previousTitle != null) {
            currentTitle = previousTitle.getText().toString();
        }
        showCoverTitleDialog(currentTitle);
    }

    private void showCoverTitleDialog(String currentTitle) {
        CoverTitleDialog coverTitleDialog = new CoverTitleDialog(CoverEditorActivity.this, R.style.DialogTheme, currentTitle, new CoverTitleDialog.OnCoverTitleClickListener() {
            @Override
            public void onCancel(Dialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onAccept(Dialog dialog, String title) {
                dialog.dismiss();
                writeTitle(title);
            }
        });
        coverTitleDialog.show();
    }

    private void writeTitle(String title) {
        CoverTitleTextView previousTitle = (CoverTitleTextView)coverCanvas.findViewWithTag(TITLE_TAG);
        if (previousTitle != null) {
            previousTitle.setText(title);
            previousTitle.refreshTouchListener();
        } else {
            CoverTitleTextView newTitle = new CoverTitleTextView(CoverEditorActivity.this, coverCanvas, new OnDragTouchListener.OnDraggableViewClickListener() {
                @Override
                public void onClick(View v) {
                    showCoverTitleDialog(((CoverTitleTextView)v).getText().toString());
                }

                @Override
                public void onLongClick(View v) {
                    handleTitleRemoval((CoverTitleTextView)v);
                }
            });
            int paddingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_small);
            newTitle.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
            newTitle.setText(title);
            newTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_xlarge));
            newTitle.setTag(TITLE_TAG);
            coverCanvas.addView(newTitle);
        }
    }

    @Click(R.id.cover_editor_image_button)
    public void onCoverImageButtonClick() {
        CoverTitleTextView coverTitleTextView = (CoverTitleTextView)coverCanvas.findViewWithTag(TITLE_TAG);
        if (coverTitleTextView != null) {
            Snackbar.make(coverCanvas, "Center X: " + coverTitleTextView.getCenterX() + ", Center Y: " + coverTitleTextView.getCenterY(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void handleTitleRemoval(CoverTitleTextView coverTitleTextView) {
        coverCanvas.removeView(coverTitleTextView);

        oldTitle = coverTitleTextView.getText().toString();
        oldTitleX = coverTitleTextView.getX();
        oldTitleY = coverTitleTextView.getY();

        Snackbar confirmTitleRemoval = Snackbar.make(coverCanvas, "Título eliminado", Snackbar.LENGTH_LONG);
        confirmTitleRemoval.setAction("Deshacer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreOldTitle();
            }
        });
        confirmTitleRemoval.setActionTextColor(getResources().getColor(R.color.white));
        confirmTitleRemoval.show();
    }

    private void restoreOldTitle() {
        CoverTitleTextView newTitle = new CoverTitleTextView(CoverEditorActivity.this, coverCanvas, new OnDragTouchListener.OnDraggableViewClickListener() {
            @Override
            public void onClick(View v) {
                showCoverTitleDialog(((CoverTitleTextView)v).getText().toString());
            }

            @Override
            public void onLongClick(View v) {
                handleTitleRemoval((CoverTitleTextView)v);
            }
        });
        int paddingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        newTitle.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
        newTitle.setText(oldTitle);
        newTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_xlarge));
        newTitle.setTag(TITLE_TAG);
        newTitle.setX(oldTitleX);
        newTitle.setY(oldTitleY);
        coverCanvas.addView(newTitle);
    }

}
