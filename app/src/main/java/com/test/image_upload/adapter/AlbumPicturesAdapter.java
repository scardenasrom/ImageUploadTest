package com.test.image_upload.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.test.image_upload.R;
import com.test.image_upload.model.AlbumPictureDTO;
import com.test.image_upload.view.AlbumPictureItemView;

import java.util.List;

public class AlbumPicturesAdapter extends RecyclerView.Adapter<AlbumPictureItemView> {

    private List<AlbumPictureDTO> albumPictureDTOs;
    private OnPictureClickListener onPictureClickListener;

    public AlbumPicturesAdapter(List<AlbumPictureDTO> albumPictureDTOs, OnPictureClickListener onPictureClickListener) {
        this.albumPictureDTOs = albumPictureDTOs;
        this.onPictureClickListener = onPictureClickListener;
    }

    public void setAlbumPictureDTOs(List<AlbumPictureDTO> albumPictureDTOs) {
        this.albumPictureDTOs = albumPictureDTOs;
        notifyDataSetChanged();
    }

    @Override
    public AlbumPictureItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumPictureItemView(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_album_picture, parent, false), parent.getContext(), onPictureClickListener);
    }

    @Override
    public void onBindViewHolder(AlbumPictureItemView holder, int position) {
        holder.bind(albumPictureDTOs.get(position));
    }

    @Override
    public int getItemCount() {
        return albumPictureDTOs.size();
    }

    public interface OnPictureClickListener {
        void onEditClick(AlbumPictureDTO albumPictureDTO);
        void onDeleteClick(AlbumPictureDTO albumPictureDTO);
    }

}
