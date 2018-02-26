package de.philippveit.popmov;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.textViewTitle)
    TextView mTextViewTitle;
    @BindView(R.id.textViewOverviewText)
    TextView mTextViewOverviewText;
    @BindView(R.id.textViewRating)
    TextView mTextViewRating;
    @BindView(R.id.textViewReleaseDate)
    TextView mTextViewReleaseDate;
    @BindView(R.id.imageViewThumbnail)
    ImageView mImageViewThumbnail;
    @BindView(R.id.imageViewBackdrop)
    ImageView mImageViewBackdrop;
    @BindView(R.id.progressBarThumbnail)
    ProgressBar mProgressBarThumbnail;
    @BindView(R.id.progressBarBackdrop)
    ProgressBar mProgressBarBackdrop;
    @BindView(R.id.imageViewPlayButton)
    ImageView mImageViewPlayButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImageViewPlayButton.setVisibility(View.GONE);
        mProgressBarThumbnail.setVisibility(View.VISIBLE);
        mProgressBarBackdrop.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        Movie movie = (Movie) data.getParcelable(EXTRA_MOVIE);

        //Is a presenter necessary? Or is it more realistic to u
        showMovie(movie);

    }

    @Override
    public void showMovie(final Movie movie) {

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

        //TODO create a presenter to call theMovieDb.org-Api to check for a video Key
        // Then call the following function from the presenter
        showPlayImageOnBackdrop("YwBAqMDYFCU");

    }

    public void showPlayImageOnBackdrop(final String youtubeKey){
        mImageViewPlayButton.setVisibility(View.VISIBLE);
        mImageViewPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(DetailActivity.this, youtubeKey);
            }
        });
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
