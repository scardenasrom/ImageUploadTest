package com.test.image_upload.activity;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.test.image_upload.BaseActivity;
import com.test.image_upload.R;
import com.test.image_upload.dialog.CoverTitleDialog;
import com.test.image_upload.model.CoverTitle;
import com.test.image_upload.util.OnDragTouchListener;
import com.test.image_upload.view.CoverTitleTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

@EActivity(R.layout.activity_cover_editor)
public class CoverEditorActivity extends BaseActivity {

    private static final String TITLE_TAG = "title";

    private CoverTitle currentCoverTitle = new CoverTitle();

    @ViewById(R.id.cover_editor_canvas) RelativeLayout coverCanvas;

    @AfterViews
    public void initialize() {
        setupToolbar("Crea tu portada");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleCoverEditExit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleCoverEditExit() {
        finish();
    }

    @Click(R.id.cover_editor_title_button)
    public void onTitleButtonClick() {
        showCoverTitleDialog();
    }

    private void showCoverTitleDialog() {
        CoverTitleDialog coverTitleDialog = new CoverTitleDialog(CoverEditorActivity.this, R.style.DialogTheme, currentCoverTitle, new CoverTitleDialog.OnCoverTitleClickListener() {
            @Override
            public void onCancel(Dialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onAccept(Dialog dialog, CoverTitle coverTitle) {
                dialog.dismiss();
                currentCoverTitle.setTitle(coverTitle.getTitle());
                currentCoverTitle.setColor(coverTitle.getColor());
                currentCoverTitle.setFont(coverTitle.getFont());
                writeTitle();
            }
        });
        coverTitleDialog.show();
    }

    private void writeTitle() {
        CoverTitleTextView previousTitle = (CoverTitleTextView)coverCanvas.findViewWithTag(TITLE_TAG);
        if (previousTitle != null) {
            previousTitle.setText(currentCoverTitle.getTitle());
            previousTitle.setTextColor(currentCoverTitle.getColor());
            previousTitle.refreshTouchListener();
            // TODO Load the selected font
        } else {
            CoverTitleTextView newCoverTitle = new CoverTitleTextView(CoverEditorActivity.this, coverCanvas, new OnDragTouchListener.OnDraggableViewClickListener() {
                @Override
                public void onClick(View v) {
                    showCoverTitleDialog();
                }

                @Override
                public void onLongClick(View v) {
                    handleTitleRemoval((CoverTitleTextView)v);
                }
            });
            newCoverTitle.setTag(TITLE_TAG);
            int paddingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_small);
            newCoverTitle.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
            newCoverTitle.setText(currentCoverTitle.getTitle());
            newCoverTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_xlarge));
            //TODO Load the selected font
//            String fontName = currentCoverTitle.getFont();
            String fontName = "CronosProRegular";
            CalligraphyUtils.applyFontToTextView(CoverEditorActivity.this, newCoverTitle, "fonts/" + fontName + ".otf");
            newCoverTitle.setTextColor(currentCoverTitle.getColor());
            coverCanvas.addView(newCoverTitle);
        }
    }

    private void handleTitleRemoval(CoverTitleTextView coverTitleTextView) {
        currentCoverTitle.setxCanvasCoordinate(coverTitleTextView.getX());
        currentCoverTitle.setyCanvasCoordinate(coverTitleTextView.getY());

        coverCanvas.removeView(coverTitleTextView);

        Snackbar confirmTitleRemovalSnackbar = Snackbar.make(coverCanvas, "Título eliminado", Snackbar.LENGTH_LONG);
        confirmTitleRemovalSnackbar.setAction("Deshacer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreOldTitle();
            }
        });
        confirmTitleRemovalSnackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (Snackbar.Callback.DISMISS_EVENT_ACTION != event)
                    currentCoverTitle = new CoverTitle();
            }
        });
        confirmTitleRemovalSnackbar.setActionTextColor(getResources().getColor(R.color.white));
        confirmTitleRemovalSnackbar.show();
    }

    private void restoreOldTitle() {
        CoverTitleTextView coverTitleTextView = new CoverTitleTextView(CoverEditorActivity.this, coverCanvas, new OnDragTouchListener.OnDraggableViewClickListener() {
            @Override
            public void onClick(View v) {
                showCoverTitleDialog();
            }

            @Override
            public void onLongClick(View v) {
                handleTitleRemoval((CoverTitleTextView)v);
            }
        });
        coverTitleTextView.setTag(TITLE_TAG);
        int paddingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        coverTitleTextView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
        coverTitleTextView.setText(currentCoverTitle.getTitle());
        coverTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_xlarge));
        //TODO Load the font
//        String fontName = currentCoverTitle.getFont();
        String fontName = "CronosProRegular";
        CalligraphyUtils.applyFontToTextView(CoverEditorActivity.this, coverTitleTextView, "fonts/" + fontName + ".otf");
        coverTitleTextView.setTextColor(currentCoverTitle.getColor());
        // Canvas position
        if (currentCoverTitle.getxCanvasCoordinate() != null && currentCoverTitle.getyCanvasCoordinate() != null) {
            coverTitleTextView.setX(currentCoverTitle.getxCanvasCoordinate());
            coverTitleTextView.setY(currentCoverTitle.getyCanvasCoordinate());
        }
        coverCanvas.addView(coverTitleTextView);
    }

}
