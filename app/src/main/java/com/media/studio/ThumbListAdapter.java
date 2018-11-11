package com.media.studio;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/11/11 16:42<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class ThumbListAdapter extends RecyclerView.Adapter<ThumbListAdapter.ThumbListHolder> {

    private List<Bitmap> bitmaps;

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        notifyDataSetChanged();
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    @Override
    public ThumbListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumb, parent, false);
        return new ThumbListHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ThumbListHolder holder, int position) {
        holder.mThumbIv.setImageBitmap(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps == null ? 0 : bitmaps.size();
    }

    public class ThumbListHolder extends RecyclerView.ViewHolder {

        ImageView mThumbIv;

        public ThumbListHolder(View itemView) {
            super(itemView);
            mThumbIv = itemView.findViewById(R.id.thumb);
        }
    }
}
