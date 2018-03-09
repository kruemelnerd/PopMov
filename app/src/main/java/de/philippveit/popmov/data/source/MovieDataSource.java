package de.philippveit.popmov.data.source;

import java.util.List;

import de.philippveit.popmov.data.Movie;

public interface MovieDataSource {

    public interface LoadMovieCallback {

        public void onMoviesLoaded(List<Movie> movies);
        public void onDataNotAvailable();
    }


}