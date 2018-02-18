package de.philippveit.popmov.presenter;

import java.util.List;

import de.philippveit.popmov.MVP.MainMVP;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.model.MovieModel;

/**
 * Created by pveit on 18.02.2018.
 */

public class MainPresenter implements MainMVP.PresenterOps {

    private MainMVP.RequieredViewOps mMovieView;
    private MainMVP.ModelOps mMovieModel;

    public MainPresenter(MainMVP.RequieredViewOps mMovieView) {
        this.mMovieView = mMovieView;
        this.mMovieModel = new MovieModel();
    }

    @Override
    public void onCreate() {
        this.mMovieModel = new MovieModel();
    }

    @Override
    public void getPopularMovies() {
        List<Movie> movies = mMovieModel.getPopularMovies();
        mMovieView.showMovies(movies);
    }

    @Override
    public void getMovie(Movie movie) {

    }
}
