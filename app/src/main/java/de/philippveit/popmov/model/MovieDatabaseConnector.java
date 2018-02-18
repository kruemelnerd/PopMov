package de.philippveit.popmov.model;

/**
 * Created by pveit on 18.02.2018.
 */

public interface MovieDatabaseConnector {
    String getJsonForPopularMovies();
    String getJsonForRatedMovies();
    String getMovie(int movieId);
}
