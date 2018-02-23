package de.philippveit.popmov.MVP;

import java.util.List;

import de.philippveit.popmov.data.Movie;

/**
 * Created by pveit on 18.02.2018.
 */

public interface MainMVP {
    /**
     * View mandatory methods. Available to the presenter
     *      Presenter -> View
     */
    interface ViewOverviewOps {
        void showMovies(List<Movie> movies);
    }

    interface ViewDetailOps {
        void showMovie(Movie movie);
    }


    /**
     * Operations offered from Presenter to View
     *      View -> Presenter
     */
    interface PresenterOps{
        void onCreate();
        void getPopularMovies();
        void getFavMovies();
        void getTopRatedMovies();
    }


    /**
     * Operations offered from Model to Presenter
     *      View -> Presenter
     */
    interface ModelOps {
        List<Movie> getPopularMovies();
        List<Movie> getTopRatedMovies();
        List<Movie> getFavMovies();
        Movie getSingleMovie(Movie movie);
    }



}
