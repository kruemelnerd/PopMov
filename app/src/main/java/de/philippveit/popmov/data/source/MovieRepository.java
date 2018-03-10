package de.philippveit.popmov.data.source;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.philippveit.popmov.BuildConfig;
import de.philippveit.popmov.R;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.MovieDbResponse;
import de.philippveit.popmov.data.MovieFilterType;
import de.philippveit.popmov.data.ReviewDbResponse;
import de.philippveit.popmov.data.VideoDbResponse;
import de.philippveit.popmov.data.source.contentProvider.FavoriteCallback;
import de.philippveit.popmov.data.source.contentProvider.FavoriteContract;
import de.philippveit.popmov.util.MovieUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {


    private static MovieRepository INSTANCE;
    private Context mContext;
    private String API_KEY;
    boolean mDirty = false;

    private MovieRepository() {
    }

    private MovieRepository(Context context) {
        this.API_KEY = BuildConfig.TMDBORG_API_KEY;
        this.mContext = context;
    }

    public static MovieRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(context);
        }
        return INSTANCE;
    }

    public void refreshMovies() {
        mDirty = true;
    }

    public void getReviews(String movieId, Callback<ReviewDbResponse> callback) {
        Call<ReviewDbResponse> call = MovieService.getApi().getReviews(movieId, API_KEY);
        call.enqueue(callback);
    }

    public void getTrailer(String movieId, Callback<VideoDbResponse> callback) {
//        if(mDirty) {
        Call<VideoDbResponse> call = MovieService.getApi().getVideo(movieId, API_KEY);
        call.enqueue(callback);
//        }
    }

    public void getPopularMovies(MovieDataSource.LoadMovieCallback callback) {
        if (mDirty) {
            Call<MovieDbResponse> call = MovieService.getApi().getPopularMovies(this.API_KEY, null, null);
            call.enqueue(new MovieCallback(callback));
        } else {
            // TODO: Get from CP
        }
    }

    public void getTopRatedMovies(MovieDataSource.LoadMovieCallback callback) {
        if (mDirty) {
            Call<MovieDbResponse> call = MovieService.getApi().getTopRatedMovies(this.API_KEY, null, null);
            call.enqueue(new MovieCallback(callback));
        } else {
            // TODO: Get from CP
        }
    }

    public void saveLocalMovie(Movie movie, final FavoriteCallback callback) {
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
        String json = new Gson().toJson(movie);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_JSON, json);

        AsyncQueryHandler handler = new AsyncQueryHandler(mContext.getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                if (uri == null) {
                    callback.onFailure();
                } else {
                    callback.onResponseInsert(uri);
                }
            }
        };
        handler.startInsert(1, null, FavoriteContract.FavoriteEntry.CONTENT_URI, values);
    }

    public void deleteLocalMovie(Movie movie, final FavoriteCallback favoriteCallback) {
        AsyncQueryHandler handler = new AsyncQueryHandler(mContext.getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                if (result != 0) {
                    favoriteCallback.onResponseDelete(result);
                } else {
                    favoriteCallback.onFailure();
                }
            }
        };
        handler.startDelete(1, null, FavoriteContract.FavoriteEntry.buildFavoriteUriWithId(movie.getId()), null, null);
    }

    public void getFavoriteMovies(MovieDataSource.LoadMovieCallback callback) {
        Cursor cursor = mContext.getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI, null, null, null, null);
        List<Movie> favoriteMovies = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            int indexJson = cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_JSON);
            String json = cursor.getString(indexJson);
            Movie movie = new Gson().fromJson(json, Movie.class);

            favoriteMovies.add(movie);
        }
        cursor.close();
        sendMoviesToPresenter(favoriteMovies, callback);
    }


    public void saveMovieFilterType(MovieFilterType type) {
        SharedPreferences preferences = mContext.getApplicationContext().getSharedPreferences(this.getClass().getCanonicalName(), Context.MODE_PRIVATE);
        preferences.edit()
                .putInt(mContext.getString(R.string.preference_key_category_movies_loaded), type.getValue())
                .apply();
    }

    public MovieFilterType getMovieFilterType() {
        SharedPreferences preferences = mContext.getApplicationContext().getSharedPreferences(this.getClass().getCanonicalName(), Context.MODE_PRIVATE);
        int type = preferences.getInt(mContext.getString(R.string.preference_key_category_movies_loaded), MovieFilterType.TOP_RATED.getValue());
        return MovieFilterType.fromInt(type);
    }


    private class MovieCallback implements Callback<MovieDbResponse> {
        MovieDataSource.LoadMovieCallback callback;

        public MovieCallback(MovieDataSource.LoadMovieCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<MovieDbResponse> call, Response<MovieDbResponse> response) {
            MovieDbResponse movieDbResponse = response.body();
            if (movieDbResponse != null) {
                List<Movie> movies = movieDbResponse.getMovies();
                sendMoviesToPresenter(movies, callback);
            } else {
                callback.onDataNotAvailable();
            }
        }

        @Override
        public void onFailure(Call<MovieDbResponse> call, Throwable t) {
            t.printStackTrace();
            callback.onDataNotAvailable();
        }
    }

    private void sendMoviesToPresenter(List<Movie> movies, MovieDataSource.LoadMovieCallback callback) {
        try {
            //QUESTION: Should the normalize be in the Presenter?
            MovieUtil.normalizeMovies(movies);
        } catch (ParseException e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        }
        callback.onMoviesLoaded(movies);
    }
}