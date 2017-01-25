package com.test.image_upload.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.image_upload.R;
import com.test.image_upload.adapter.AlbumPicturesAdapter;
import com.test.image_upload.model.AlbumPictureDTO;
import com.test.image_upload.util.ImageUtils;

import java.io.File;

public class AlbumPictureItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView imageView;
    private TextView pictureTitle;
    private TextView pictureInfo;
    private TextView pictureFooter;
    private ImageView editButton;
    private ImageView deleteButton;

    private Context context;
    private AlbumPicturesAdapter.OnPictureClickListener onPictureClickListener;

    private AlbumPictureDTO albumPictureDTO;

    public AlbumPictureItemView(View itemView, Context context, AlbumPicturesAdapter.OnPictureClickListener onPictureClickListener) {
        super(itemView);
        this.context = context;
        this.onPictureClickListener = onPictureClickListener;

        imageView = (ImageView) itemView.findViewById(R.id.view_album_picture_image);
        pictureTitle = (TextView)itemView.findViewById(R.id.view_album_picture_header);
        pictureInfo = (TextView)itemView.findViewById(R.id.view_album_picture_info);
        pictureFooter = (TextView)itemView.findViewById(R.id.view_album_picture_footer);
        editButton = (ImageView)itemView.findViewById(R.id.view_album_picture_edit_button);
        deleteButton = (ImageView)itemView.findViewById(R.id.view_album_picture_delete_button);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    public void bind(AlbumPictureDTO albumPictureDTO) {
        this.albumPictureDTO = albumPictureDTO;

        // Title
        if (albumPictureDTO.getTitle() != null && !TextUtils.isEmpty(albumPictureDTO.getTitle())) {
            pictureTitle.setText(albumPictureDTO.getTitle());
        } else {
            pictureTitle.setText("Título");
        }

        // Footer
        if (albumPictureDTO.getFooter() != null && !TextUtils.isEmpty(albumPictureDTO.getFooter())) {
            pictureFooter.setText(albumPictureDTO.getFooter());
        } else {
            pictureFooter.setText("Pie de foto");
        }

        if (albumPictureDTO.getFile() != null && albumPictureDTO.getFile().exists()) {
            imageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(albumPictureDTO.getFile(), 200, 200));
        }

        if (albumPictureDTO.getResolution() != null) {
            int height = albumPictureDTO.getResolution().getHeight();
            int width = albumPictureDTO.getResolution().getWidth();
            if (width > height) {
                pictureInfo.setText("La fotografía es apaisada. Resolución: " + width + "x" + height);
            } else if (width < height) {
                pictureInfo.setText("La fotografía es vertical. Resolución: " + width + "x" + height);
            } else {
                pictureInfo.setText("La fotografía es cuadrada. Resolución: " + width + "x" + height);
            }
            if (height < ImageUtils.REQ_HEIGHT_FOR_PRINT || width < ImageUtils.REQ_WIDTH_FOR_PRINT) {
                pictureInfo.setTextColor(context.getResources().getColor(R.color.red));
                pictureInfo.append(" La fotografía no tiene una resolución adecuada para ser imprimida.");
            } else {
                pictureInfo.setTextColor(context.getResources().getColor(R.color.grey));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == editButton) {
            onPictureClickListener.onEditClick(albumPictureDTO);
        } else if (view == deleteButton) {
            onPictureClickListener.onDeleteClick(albumPictureDTO);
        }
    }

}
