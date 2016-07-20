package com.example.abhinavkumar.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhinavkumar on 7/19/16.
 */
//Part 2
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder> {
    private final String LOG_TAG = FlickrRecyclerViewAdapter.class.getSimpleName();
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context mContext, List<Photo> mPhotoList) {
        this.mPhotoList = mPhotoList;
        this.mContext = mContext;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, null);
        FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder((view));
        return flickrImageViewHolder;
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        Photo photoItem = mPhotoList.get(position);
        Log.d(LOG_TAG, "Processing: " + photoItem.getmTitle() + "-->" + Integer.toString(position));
        //draw the thumbnail
        Picasso.with(mContext).load(photoItem.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        //update the title
        holder.title.setText(photoItem.getmTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mPhotoList ? mPhotoList.size() : 0);
    }
}
