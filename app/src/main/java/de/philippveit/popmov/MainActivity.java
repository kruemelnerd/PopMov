package de.philippveit.popmov;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.philippveit.popmov.MVP.MainMVP;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.presenter.MainPresenter;
import de.philippveit.popmov.view.MovieAdapter;

public class MainActivity extends AppCompatActivity implements MainMVP.ViewOverviewOps {

    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    private MainMVP.PresenterOps mMoviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviePresenter = new MainPresenter(this);

        recyclerView = (RecyclerView) findViewById(R.id.mainActivity_recycler_view);

        //listener for onClick
        MovieAdapter.OnItemClickListener listener = new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Movie movie) {
                launchDetailActivity(position, movie);
            }
        };

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList, listener);

        int spanCount = 2;
        if (getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().
                heightPixels) {
            //Screen is switched to Landscape
            spanCount = 3;
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);

        mMoviePresenter.getTopRatedMovies();

        addToolbar();
    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);

                        switch (item.getItemId()) {
                            case R.id.nav_polpular_movies:
                                mMoviePresenter.getPopularMovies();
                                break;
                            case R.id.nav_top_rated_movies:
                                mMoviePresenter.getTopRatedMovies();
                                break;
                            case R.id.nav_favorites_movies:
                                mMoviePresenter.getFavMovies();
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchDetailActivity(int position, Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        MainActivity.this.startActivity(intent);
    }

    @Override
    public void showMovies(List<Movie> movies) {

        movieList = movies;
        movieAdapter.setMovieList(movieList);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
