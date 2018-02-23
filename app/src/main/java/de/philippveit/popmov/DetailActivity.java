package de.philippveit.popmov;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.philippveit.popmov.MVP.MainMVP;
import de.philippveit.popmov.data.Movie;

/**
 * Created by pveit on 20.02.2018.
 */

public class DetailActivity extends AppCompatActivity implements MainMVP.ViewDetailOps {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_MOVIE = "extra_movie";
    private static final int DEFAULT_POSITION = -1;
    private static final String TAG = "DetailActivity";

    private TextView mTextViewTitle;
    private TextView mTextViewOverviewText;
    private ImageView mImageViewThumbnail;
    private ImageView mImageViewBackdrop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        mTextViewOverviewText = (TextView) findViewById(R.id.textViewOverviewText);
        mImageViewBackdrop = (ImageView) findViewById(R.id.imageViewBackdrop);
        mImageViewThumbnail = (ImageView) findViewById(R.id.imageViewThumbnail);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        Movie movie = (Movie) data.getParcelable(EXTRA_MOVIE);

        //TODO: Use MVP
        showMovie(movie);

    }

    @Override
    public void showMovie(Movie movie) {

        mTextViewOverviewText.setText(movie.getOverview());
        mTextViewTitle.setText(movie.getTitle());

        //.memoryPolicy(MemoryPolicy.NO_STORE)

        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).setIndicatorsEnabled(true);

        // Not working

/*
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });
        builder.build().load(movie.getBackdropPath()).into(mImageViewBackdrop);
*/


       Picasso.with(this)
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.progress_animation)
                .into(mImageViewBackdrop);


        // Working fine
        Picasso.with(this)
                .load(movie.getPosterPath())
                .fit()
                .placeholder(R.drawable.progress_animation)
                .into(mImageViewThumbnail);



    }
}
