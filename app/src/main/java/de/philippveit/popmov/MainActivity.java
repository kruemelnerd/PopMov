package de.philippveit.popmov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.view.MovieAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.mainActivity_recycler_view);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);

        prepareMovies();
    }


    private void prepareMovies(){

        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Toy Story");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Toy Story 2");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Die Schöne und das Biest");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Monty Pythons's Der Sinn des Lebens");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Krieg der Sterne - Die Rückkehr der Jedi-Ritter");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Black Panther");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Ip Man");
        movieList.add(movie);

        movie = new Movie();
        movie.setTitle("Furios 7");
        movieList.add(movie);



    }
}
