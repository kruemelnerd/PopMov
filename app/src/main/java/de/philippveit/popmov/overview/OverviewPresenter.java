package de.philippveit.popmov.overview;

import java.text.ParseException;
import java.util.List;

import de.philippveit.popmov.BuildConfig;
import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.MovieDbResponse;
import de.philippveit.popmov.data.source.MovieService;
import de.philippveit.popmov.util.MovieUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pveit on 18.02.2018.
 */

public class OverviewPresenter implements MvpContract.PresenterOverviewOps {

    private MvpContract.ViewOverviewOps mMovieView;
    private String apiKey;

    public OverviewPresenter(MvpContract.ViewOverviewOps mMovieView) {
        this.mMovieView = mMovieView;
        this.apiKey = BuildConfig.TMDBORG_API_KEY;
    }

    @Override
    public void onCreate() {
        // Nothing to do yet
    }

    @Override
    public void getPopularMovies() {
        Call<MovieDbResponse> call = MovieService.getApi().getPopularMovies(apiKey, null, null);
        call.enqueue(new MovieCallback());
    }


    @Override
    public void getTopRatedMovies() {
        Call<MovieDbResponse> call = MovieService.getApi().getTopRatedMovies(apiKey, null, null);
        call.enqueue(new MovieCallback());
    }


    private class MovieCallback implements Callback<MovieDbResponse> {
        @Override
        public void onResponse(Call<MovieDbResponse> call, Response<MovieDbResponse> response) {
            MovieDbResponse movieDbResponse = response.body();
            if (movieDbResponse != null){
                List<Movie> movies = movieDbResponse.getMovies();
                try{
                    MovieUtil.normalizeMovies(movies);
                }catch (ParseException e) {
                    e.printStackTrace();
                    mMovieView.showErrorParsingImages();
                }
                mMovieView.showMovies(movies);
            }else {
                mMovieView.showErrorLoadingMovies();
            }
        }

        @Override
        public void onFailure(Call<MovieDbResponse> call, Throwable t) {
            t.printStackTrace();
            mMovieView.showErrorLoadingMovies();
        }
    }


}
