package de.philippveit.popmov.model;

import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import de.philippveit.popmov.MVP.MainMVP;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.util.JsonUtil;
import de.philippveit.popmov.util.MovieUtil;

/**
 * Created by pveit on 18.02.2018.
 */

public class MovieModel implements MainMVP.ModelOps {

    private final static String TAG = "MovieModel";

    private MovieDatabaseConnector db;

    public MovieModel() {
        this.db = new MovieDatabaseConnectorMockImpl();
    }

    @Override
    public List<Movie> getPopularMovies() {
        String json = db.getJsonForPopularMovies();
        List<Movie> movies;
        try {
            movies = JsonUtil.parseJson(json);
            for (Movie movie : movies) {
                movie.setPosterPath(MovieUtil.normalizeMovieDbImages(movie.getPosterPath()));
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Error during the parsing");
            e.printStackTrace();

            movies = new ArrayList<>();
        }
        return movies;
    }

    @Override
    public Movie getSingleMovie(Movie movie) {
        return null;
    }
}
