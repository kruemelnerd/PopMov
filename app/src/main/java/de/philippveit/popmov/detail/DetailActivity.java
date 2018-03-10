package de.philippveit.popmov.detail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.R;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.Review;
import de.philippveit.popmov.data.Video;
import de.philippveit.popmov.data.source.Injection;
import de.philippveit.popmov.data.source.MovieRepository;

public class DetailActivity extends AppCompatActivity implements MvpContract.ViewDetailOps {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_MOVIE = "extra_movie";
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
    @BindView(R.id.textViewTrailerLabel)
    TextView mTextViewTrailerLabel;
    @BindView(R.id.textViewReviewsLabel)
    TextView mTextViewReviewLabel;

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

    @BindView(R.id.recyclerViewReviews)
    RecyclerView mRecyclerViewReviews;
    @BindView(R.id.recyclerViewTrailer)
    RecyclerView mRecyclerViewTrailer;

    private boolean isFavorite;
    private List<Review> mReviewList;
    private ReviewAdapter mReviewAdapter;
    private List<Video> mTrailerList;
    private TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        MovieRepository movieRepository = Injection.provideMovieRepository(this);
        mMoviePresenter = new DetailPresenter(this, movieRepository);

        mImageViewPlayButton.setVisibility(View.GONE);
        mProgressBarThumbnail.setVisibility(View.VISIBLE);
        mProgressBarBackdrop.setVisibility(View.VISIBLE);

        initToolbar();
        initReviews();
        initTrailer();

        Intent intent = getIntent();

        Bundle data = intent.getExtras();
        Movie movie = (Movie) data.getParcelable(EXTRA_MOVIE);

        showMovie(movie);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initTrailer() {

        TrailerAdapter.OnClickListener onClickListener = new TrailerAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, Video video) {
                watchYoutubeVideo(DetailActivity.this, video.getKey());
            }
        };
        mTrailerList = new ArrayList<>();
        mTrailerAdapter = new TrailerAdapter(this, mTrailerList, onClickListener);
        mRecyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTrailer.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewTrailer.setAdapter(mTrailerAdapter);

    }

    private void initReviews() {
        mReviewList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewReviews.setAdapter(mReviewAdapter);
    }

    @Override
    public void showMovie(final Movie movie) {

        mTextViewOverviewText.setText(movie.getOverview());
        mTextViewTitle.setText(movie.getTitle());
        mTextViewRating.setText(movie.getVoteAverage().toString());
        mTextViewReleaseDate.setText(movie.getReleaseDate());

        loadImage(movie.getPosterPath(), mImageViewThumbnail, mProgressBarThumbnail);
        loadImage(movie.getBackdropPath(), mImageViewBackdrop, mProgressBarBackdrop);

        mTextViewTrailerLabel.setVisibility(View.GONE);
        mMoviePresenter.getVideo(movie);
        mTextViewReviewLabel.setVisibility(View.GONE);
        mMoviePresenter.getReviews(movie);
        mMoviePresenter.checkAndMarkIfFavorite(movie);

        mImageButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText;
                if (isFavorite) {
                    //Delete Favorite
                    mMoviePresenter.deleteMovieFromFavorite(movie);
                    messageText = getString(R.string.favorite_deleted);
                    isFavorite = false;
                } else {
                    //Save Favorite
                    mMoviePresenter.saveMovieAsFavorite(movie);
                    messageText = getString(R.string.favorite_saved);
                    isFavorite = true;
                }
                mMoviePresenter.checkAndMarkIfFavorite(movie);
                Toast.makeText(DetailActivity.this, messageText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void displayAsFavorite() {
        isFavorite = true;
        mImageButtonFavorite.setImageResource(R.drawable.ic_heart);
    }

    @Override
    public void displayAsNonFavorite() {
        isFavorite = false;
        mImageButtonFavorite.setImageResource(R.drawable.ic_heart_outline);
    }


    private void loadImage(String path, ImageView intoImageView, final ProgressBar progressBar) {
        if (StringUtils.isBlank(path)) {
            path = "isEmpty";
        }
        Picasso picasso = Picasso.with(this);
//        picasso.setIndicatorsEnabled(true);         // Enable Picasso debugging
        picasso
                .load(path)
                .fit()
                .error(R.drawable.ic_thumb_down)
                .into(intoImageView, new Callback() {
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
    public void showReviews(List<Review> reviews) {
        if (!reviews.isEmpty()) {
            mTextViewReviewLabel.setVisibility(View.VISIBLE);
            mReviewList = reviews;
            mReviewAdapter.setReviewList(mReviewList);
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showTrailer(List<Video> trailer) {
        if (!trailer.isEmpty()) {
            showPlayImageOnBackdrop(trailer.get(0).getKey());
            mTextViewTrailerLabel.setVisibility(View.VISIBLE);
            mTrailerList = trailer;
            mTrailerAdapter.setmTrailerList(mTrailerList);
            mTrailerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrorLoadingMessage() {
        Toast.makeText(this, getString(R.string.error_loading_single_movie), Toast.LENGTH_SHORT).show();
    }


    public void showPlayImageOnBackdrop(final String youtubeKey) {
        mImageViewPlayButton.setVisibility(View.VISIBLE);
        mImageViewPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(DetailActivity.this, youtubeKey);
            }
        });
    }

    public void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            if (appIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(appIntent);
            } else if (webIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(webIntent);
            } else {
                throw new ActivityNotFoundException();
            }

        } catch (ActivityNotFoundException ex) {
            showErrorLoadingMessage();
        }
    }
}
