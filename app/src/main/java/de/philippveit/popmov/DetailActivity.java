package de.philippveit.popmov;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.contentProvider.FavoriteContract;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.Review;
import de.philippveit.popmov.presenter.DetailPresenter;

/**
 * Created by pveit on 20.02.2018.
 */

public class DetailActivity extends AppCompatActivity implements MvpContract.ViewDetailOps {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_MOVIE = "extra_movie";
    private static final int DEFAULT_POSITION = -1;
    private static final String TAG = "DetailActivity";

    private MvpContract.PresenterDetailOps mMoviePresenter;

    @BindView(R.id.textViewTitle)
    TextView mTextViewTitle;
    @BindView(R.id.textViewOverviewText)
    TextView mTextViewOverviewText;
    @BindView(R.id.textViewRating)
    TextView mTextViewRating;
    @BindView(R.id.textViewReleaseDate)
    TextView mTextViewReleaseDate;
    @BindView(R.id.textViewReviews)
    TextView mTextViewReviews;
    @BindView(R.id.imageViewThumbnail)
    ImageView mImageViewThumbnail;
    @BindView(R.id.imageViewBackdrop)
    ImageView mImageViewBackdrop;
    @BindView(R.id.imageViewPlayButton)
    ImageView mImageViewPlayButton;
    @BindView(R.id.progressBarThumbnail)
    ProgressBar mProgressBarThumbnail;
    @BindView(R.id.progressBarBackdrop)
    ProgressBar mProgressBarBackdrop;
    @BindView(R.id.imageButtonFavorite)
    ImageButton mImageButtonFavorite;

    private boolean isFavorite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mMoviePresenter = new DetailPresenter(this);

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

        loadImage(movie.getPosterPath(), mImageViewThumbnail, mProgressBarThumbnail);
        loadImage(movie.getBackdropPath(), mImageViewBackdrop, mProgressBarBackdrop);

        mMoviePresenter.getVideo(movie);
        mMoviePresenter.getReviews(movie);

        displayIsFavorite(movie);

        mImageButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText;
                if(isFavorite){
                    //Delete Favorite
                    getContentResolver().delete(FavoriteContract.FavoriteEntry.buildFavoriteUriWithId(movie.getId()), null, null);
                    messageText = getString(R.string.favorite_deleted);;
                    isFavorite = false;
                }else{
                    //Save Favorite
                    Uri uri = insertNewFavorite(movie);
                    messageText = getString(R.string.favorite_saved);
                    isFavorite = true;
                }
                displayIsFavorite(movie);
                Toast.makeText(DetailActivity.this, messageText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayIsFavorite(Movie movie){
        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.buildFavoriteUriWithId(movie.getId()), null, null, null, null);
        isFavorite = false;
        mImageButtonFavorite.setImageResource(R.drawable.ic_heart_outline);
        if(cursor.getCount() != 0){
            isFavorite = true;
            mImageButtonFavorite.setImageResource(R.drawable.ic_heart);
        }
    }

    private Uri insertNewFavorite(Movie movie){
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT, movie.getOverview());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        return getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values);
    }

    private void loadImage(String path, ImageView into, final ProgressBar progressBar) {
        Picasso.with(this)
                .load(path)
                .fit()
                .into(into, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        return;
                    }
                });
    }

    @Override
    public void showReviews(List<Review> reviews){

        for (Review review : reviews) {
            StringBuilder completeReview = new StringBuilder((String) mTextViewReviews.getText());
            completeReview.append(System.getProperty("line.separator"));
            completeReview.append(review.getAuthor()+":" + System.getProperty("line.separator"));
            completeReview.append(review.getContent());
            completeReview.append(System.getProperty("line.separator"));
            mTextViewReviews.setText(completeReview.toString());
        }
    }

    @Override
    public void showPlayImageOnBackdrop(final String youtubeKey) {
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
