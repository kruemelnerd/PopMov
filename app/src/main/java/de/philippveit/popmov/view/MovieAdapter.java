package de.philippveit.popmov.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.philippveit.popmov.R;
import de.philippveit.popmov.data.Movie;

/**
 * Created by pveit on 17.02.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(int position, Movie item);
    }

    private Context mContext;
    private List<Movie> movieList;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movie_title_tv);
            thumbnail = (ImageView) view.findViewById(R.id.movie_thumbnail_iv);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public MovieAdapter(Context context, List<Movie> movies, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.movieList = movies;
        this.listener = onItemClickListener;

    }

    public void setMovieList(List<Movie> movies) {
        this.movieList = movies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());

        Picasso
                .with(mContext)
                .load(movie.getPosterPath())
                .fit()
                .placeholder(R.drawable.progress_animation)
                .into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(position, movie);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(position,movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}
