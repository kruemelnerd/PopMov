package de.philippveit.popmov.overview;


import java.util.List;

import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.MovieFilterType;
import de.philippveit.popmov.data.source.MovieDataSource;
import de.philippveit.popmov.data.source.MovieRepository;

public class OverviewPresenter implements MvpContract.PresenterOverviewOps {

    private MvpContract.ViewOverviewOps mMovieView;
    private MovieRepository mMovieRepository;

    boolean mFirstLoad = true;


    public OverviewPresenter(MvpContract.ViewOverviewOps mMovieView, MovieRepository movieRepository) {
        this.mMovieView = mMovieView;
        this.mMovieRepository = movieRepository;
    }

    @Override
    public void start() {
        loadMovies(true);
    }

    public void loadMovies(boolean forceUpdate) {

        loadAllMovies(forceUpdate || mFirstLoad);
        mFirstLoad = false;

    }


    public void loadAllMovies(boolean forceUpdate) {
        if (forceUpdate) {
            mMovieRepository.refreshMovies(); // Only sets the dirtyFlag.
        }
        MovieFilterType mCurrentFiltering = getFiltering();
        switch (mCurrentFiltering) {
            case TOP_RATED:
                mMovieRepository.getTopRatedMovies(new MovieCallback());
                break;
            case POPULAR:
                mMovieRepository.getPopularMovies(new MovieCallback());
                break;
            case FAVORITE:
                mMovieRepository.getFavoriteMovies(new MovieCallback());
                break;
        }
    }

    public void setFiltering(MovieFilterType type) {
        mMovieRepository.saveMovieFilterType(type);
    }

    public MovieFilterType getFiltering() {
        return mMovieRepository.getMovieFilterType();
    }

    private class MovieCallback implements MovieDataSource.LoadMovieCallback {
        @Override
        public void onMoviesLoaded(List<Movie> movies) {
            mMovieView.showMovies(movies);
        }

        @Override
        public void onDataNotAvailable() {
            mMovieView.showErrorLoadingMovies();
        }
    }

}