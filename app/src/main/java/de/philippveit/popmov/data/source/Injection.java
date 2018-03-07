package de.philippveit.popmov.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Philipp on 07.03.2018.
 */

public class Injection {

    public static MovieRepository provideMovieRepository( @NonNull Context context ) {
        checkNotNull(context);

        MovieRepository database = MovieRepository.getInstance(context);
        return database;
    }
}