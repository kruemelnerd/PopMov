package de.philippveit.popmov;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
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
    private TextView mTextViewRating;
    private TextView mTextViewReleaseDate;
    private ImageView mImageViewThumbnail;
    private ImageView mImageViewBackdrop;
    private ProgressBar mProgressBarThumbnail;
    private ProgressBar mProgressBarBackdrop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        mTextViewOverviewText = (TextView) findViewById(R.id.textViewOverviewText);
        mTextViewRating = (TextView) findViewById(R.id.textViewRating);
        mTextViewReleaseDate = (TextView) findViewById(R.id.textViewReleaseDate);
        mImageViewBackdrop = (ImageView) findViewById(R.id.imageViewBackdrop);
        mImageViewThumbnail = (ImageView) findViewById(R.id.imageViewThumbnail);
        mProgressBarThumbnail = (ProgressBar) findViewById(R.id.progressBarThumbnail);
        mProgressBarBackdrop = (ProgressBar) findViewById(R.id.progressBarBackdrop);
        mProgressBarThumbnail.setVisibility(View.VISIBLE);
        mProgressBarBackdrop.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        Movie movie = (Movie) data.getParcelable(EXTRA_MOVIE);

        //Is a presenter necessary? Or is it more realistic to u
        showMovie(movie);

    }

    @Override
    public void showMovie(Movie movie) {

        mTextViewOverviewText.setText(movie.getOverview());
        mTextViewTitle.setText(movie.getTitle());
        mTextViewRating.setText(movie.getVoteAverage().toString());
        mTextViewReleaseDate.setText(movie.getReleaseDate());

        Picasso.with(this)
                .load(movie.getBackdropPath())
                .into(mImageViewBackdrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBarBackdrop.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        return;
                    }
                });

        Picasso.with(this)
                .load(movie.getPosterPath())
                .fit()
                .into(mImageViewThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBarThumbnail.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        return;
                    }
                });


    }
}
