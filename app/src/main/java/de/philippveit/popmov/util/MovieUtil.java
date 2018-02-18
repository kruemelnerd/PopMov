package de.philippveit.popmov.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by pveit on 18.02.2018.
 */

public class MovieUtil {

    private static final String movieImageUrl = "https://image.tmdb.org/t/p/";
    private static final String movieImageSize = "w185"; // "w92", "w154", "w185", "w342", "w500", "w780", or "original"

    public static String normalizeMovieDbImages(String imagename){

        if(StringUtils.isNotEmpty(imagename)){
            StringBuilder completeImageUrl = new StringBuilder();
            completeImageUrl.append(movieImageUrl);
            completeImageUrl.append(movieImageSize);
            completeImageUrl.append("/");
            completeImageUrl.append(imagename);
            return completeImageUrl.toString();
        }else {
            return "";
        }
    }

}
