package de.philippveit.popmov.detail;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.ReviewDbResponse;
import de.philippveit.popmov.data.Video;
import de.philippveit.popmov.data.VideoDbResponse;
import de.philippveit.popmov.data.source.MovieDataSource;
import de.philippveit.popmov.data.source.MovieRepository;
import de.philippveit.popmov.util.MovieUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Philipp on 27.02.2018.
 */

public class DetailPresenter implements MvpContract.PresenterDetailOps {

    private MvpContract.ViewDetailOps mView;
    private MovieRepository mMovieRepository;



    public DetailPresenter(MvpContract.ViewDetailOps mView, MovieRepository movieRepository) {
        this.mView = mView;
        this.mMovieRepository = movieRepository;
    }

    @Override
    public void getVideo(Movie movie) {

        mMovieRepository.getTrailer(movie.getId().toString(),
                new Callback<VideoDbResponse>(){
                    @Override
                    public void onResponse(Call<VideoDbResponse> call, Response<VideoDbResponse> response) {
                        VideoDbResponse videoDbResponse = response.body();
                        if (videoDbResponse != null) {
                            List<Video> results = videoDbResponse.getResults();
                            for (Video video : results) {
                                video.setThumbnailUrl(MovieUtil.normalizeYoutubeLink(video.getKey()));
                            }
                            mView.showTrailer(results);
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoDbResponse> call, Throwable t) {
                        return;
                    }
        });
    }

    @Override
    public void getReviews(Movie movie) {
        mMovieRepository.getReviews(movie.getId().toString(),
                new Callback<ReviewDbResponse>() {
                    @Override
                    public void onResponse(Call<ReviewDbResponse> call, Response<ReviewDbResponse> response) {
                        ReviewDbResponse body = response.body();
                        if (body != null) {
                            mView.showReviews(body.getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewDbResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void saveMovieAsFavorite(Movie movie) {
        mMovieRepository.saveLocalMovie(movie);
    }

    @Override
    public void checkAndMarkIfFavorite(final Movie movie) {
        if (StringUtils.isEmpty(movie.getId().toString())){
            return;
        }
        mMovieRepository.getFavoriteMovies(new MovieDataSource.LoadMovieCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                for (Movie favMovie : movies ) {
                    if (favMovie.getId().equals(movie.getId())){
                        mView.displayAsFavorite();
                        return;
                    }
                }
                mView.displayAsNonFavorite();
            }

            @Override
            public void onDataNotAvailable() {
                mView.showErrorLoadingMessage();
            }
        });
    }

    private class SingleMovieCallback implements MovieDataSource.LoadMovieCallback {
        @Override
        public void onMoviesLoaded(List<Movie> movies) {
            if(movies != null && movies.size() >=1 ){
                mView.showMovie(movies.get(0));
            }
        }

        @Override
        public void onDataNotAvailable() {
            mView.showErrorLoadingMessage();
        }
    }

}
