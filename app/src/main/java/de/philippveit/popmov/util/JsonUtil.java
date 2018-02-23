package de.philippveit.popmov.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.MovieDbResponse;

/**
 * Created by pveit on 18.02.2018.
 */

public class JsonUtil {

    public static List<Movie> parseJson(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        MovieDbResponse movies = gson.fromJson(json, MovieDbResponse.class);
        if(movies == null){
            throw new JsonSyntaxException("No Movies found.");
        }
        return movies.getMovies();
    }
}
