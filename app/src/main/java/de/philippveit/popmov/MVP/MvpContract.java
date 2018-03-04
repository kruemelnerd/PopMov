package de.philippveit.popmov.MVP;

import java.util.List;

import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.Review;
import de.philippveit.popmov.data.Video;

/**
 * Created by pveit on 18.02.2018.
 */

public interface MvpContract {
    /**
     * View mandatory methods. Available to the presenter
     *      Presenter -> View
     */
    interface ViewOverviewOps {
        void showMovies(List<Movie> movies);
        void showErrorLoadingMovies();
        void showErrorParsingImages();
    }

    interface ViewDetailOps {
        void showMovie(Movie movie);
        void showReviews(List<Review> reviews);
        void showTrailer(List<Video> trailer);
    }


    /**
     * Operations offered from Presenter to View
     *      View -> Presenter
     */
    interface PresenterMainOps {
        void onCreate();
        void getPopularMovies();
        void getTopRatedMovies();
    }

    interface PresenterDetailOps {
        void onCreate();
        void getVideo(Movie movie);
        void getReviews(Movie movie);
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
