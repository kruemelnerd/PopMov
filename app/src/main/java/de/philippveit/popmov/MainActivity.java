package de.philippveit.popmov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.philippveit.popmov.MVP.MainMVP;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.presenter.MainPresenter;
import de.philippveit.popmov.view.MovieAdapter;

public class MainActivity extends AppCompatActivity implements MainMVP.RequieredViewOps {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    private MainMVP.PresenterOps mMoviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviePresenter = new MainPresenter(this);

        recyclerView = (RecyclerView) findViewById(R.id.mainActivity_recycler_view);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);

        mMoviePresenter.getPopularMovies();
    }

    @Override
    public void showMovies(List<Movie> movies) {

        movieList = movies;
        movieAdapter.setMovieList(movieList);
        movieAdapter.notifyDataSetChanged();
    }

}
