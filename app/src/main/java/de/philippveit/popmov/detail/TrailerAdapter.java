package de.philippveit.popmov.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.philippveit.popmov.R;
import de.philippveit.popmov.data.Video;

/**
 * Created by Philipp on 04.03.2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private static final String TAG = "TrailerAdapter";

    public class TrailerHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        ProgressBar progressBar;

        public TrailerHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageViewTrailerThumbnail);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBarTrailer);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public interface OnClickListener {
        void onItemClick(int position, Video video);
    }

    private List<Video> mTrailerList;
    private Context mContext;
    private OnClickListener onClickListener;

    public TrailerAdapter(Context mContext, List<Video> mTrailerList, OnClickListener onClickListener) {
        this.mTrailerList = mTrailerList;
        this.mContext = mContext;
        this.onClickListener = onClickListener;
    }

    public void setmTrailerList(List<Video> mTrailerList) {
        this.mTrailerList = mTrailerList;
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_trailer, parent, false);
        return new TrailerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrailerHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final Video video = mTrailerList.get(adapterPosition);
        Picasso.with(mContext)
                .load(video.getThumbnailUrl())
                .fit()
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(adapterPosition, video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }


}
