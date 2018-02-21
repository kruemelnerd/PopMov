package de.philippveit.popmov;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import de.philippveit.popmov.MVP.MainMVP;
import de.philippveit.popmov.data.Movie;

/**
 * Created by pveit on 20.02.2018.
 */

public class DetailActivity extends AppCompatActivity implements MainMVP.ViewDetailOps {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_MOVIE = "extra_movie";
    private static final int DEFAULT_POSITION = -1;
    private static final String TAG = "DetailActivity";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


    }

    @Override
    public void showMovie(Movie movie) {

    }
}
