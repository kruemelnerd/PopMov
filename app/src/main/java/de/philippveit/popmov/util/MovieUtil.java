package de.philippveit.popmov.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.philippveit.popmov.data.Movie;

/**
 * Created by pveit on 18.02.2018.
 */

public class MovieUtil {

    public static final String DEFAULT_MOVIE_THUMBNAIL_SIZE = "w185"; // "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    public static final String DEFAULT_MOVIE_BACKPROP_SIZE = "w780"; //  "w300", "w780", "w1280" or "original"

    private static final String movieImageUrl = "http://image.tmdb.org/t/p/";

    private static final String youtubeUrl = "https://img.youtube.com/vi/{video_key}/0.jpg";

    public static String normalizeYoutubeLink(String videoKey) {

        return youtubeUrl.replace("{video_key}", videoKey);
    }

    public static String normalizeMovieDbImages(String imagename, String size) {
        if (StringUtils.isNotEmpty(imagename)) {
            StringBuilder completeImageUrl = new StringBuilder();
            completeImageUrl.append(movieImageUrl);
            completeImageUrl.append(size);
            completeImageUrl.append("/");
            completeImageUrl.append(imagename);
            return completeImageUrl.toString();
        } else {
            return "";
        }
    }

    public static String normalizeDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date result = df.parse(date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(result);
    }

    public static List<Movie> normalizeMovies(List<Movie> movies) throws ParseException {
        for (Movie movie : movies) {
            normalizeMovie(movie);
        }
        return movies;
    }

    public static Movie normalizeMovie(Movie movie) throws ParseException {
        movie.setPosterPath(normalizeMovieDbImages(movie.getPosterPath(), DEFAULT_MOVIE_THUMBNAIL_SIZE));
        movie.setBackdropPath(normalizeMovieDbImages(movie.getBackdropPath(), DEFAULT_MOVIE_BACKPROP_SIZE));
        movie.setReleaseDate(normalizeDate(movie.getReleaseDate()));
        return movie;
    }
}
