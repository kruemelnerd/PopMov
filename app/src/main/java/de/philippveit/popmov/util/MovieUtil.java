package de.philippveit.popmov.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by pveit on 18.02.2018.
 */

public class MovieUtil {

    public static final String DEFAULT_MOVIE_THUMBNAIL_SIZE = "w185"; // "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    public static final String DEFAULT_MOVIE_BACKPROP_SIZE = "w300"; //  "w300", "w780", "w1280" or "original"

    private static final String movieImageUrl = "https://image.tmdb.org/t/p/";

    @Deprecated
    public static String normalizeMovieDbImages(String imagename) {
        return normalizeMovieDbImages(imagename, DEFAULT_MOVIE_THUMBNAIL_SIZE);
    }

    public static String normalizeMovieDbImages(String imagename, String size){
        if(StringUtils.isNotEmpty(imagename)){
            StringBuilder completeImageUrl = new StringBuilder();
            completeImageUrl.append(movieImageUrl);
            completeImageUrl.append(size);
            completeImageUrl.append("/");
            completeImageUrl.append(imagename);
            return completeImageUrl.toString();
        }else {
            return "";
        }
    }

}
