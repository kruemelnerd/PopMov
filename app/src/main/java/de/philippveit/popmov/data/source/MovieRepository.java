package de.philippveit.popmov.data.source;

import android.content.Context;

import de.philippveit.popmov.BuildConfig;

/**
 * Created by Philipp on 07.03.2018.
 */

public class MovieRepository {

    private static MovieRepository INSTANCE;
    private String API_KEY;

    private MovieRepository() {
        this.API_KEY = BuildConfig.TMDBORG_API_KEY;
    }

    public static MovieRepository getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = new MovieRepository();
        }
        return INSTANCE;
    }




}
