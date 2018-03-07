package de.philippveit.popmov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.contentProvider.FavoriteContract;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.presenter.MainPresenter;
import de.philippveit.popmov.view.MovieAdapter;

public class MainActivity extends AppCompatActivity implements MvpContract.ViewOverviewOps {

    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    private MvpContract.PresenterMainOps mMoviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviePresenter = new MainPresenter(this);

        initToolbar();
        initDrawer();
        initRecyclerView();

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String catMovieLoaded = sharedPreferences.getString(getString(R.string.preference_key_category_movies_loaded), getString(R.string.preference_default_category_movies_loaded));

        String pop = this.getString(R.string.preference_popular_category_movies_loaded);
        String fav = this.getString(R.string.preference_fav_category_movies_loaded);

        if (pop.equals(catMovieLoaded)) {
            mMoviePresenter.getPopularMovies();
        } else if (fav.equals(catMovieLoaded)) {
            getFavorites();
        } else {
            mMoviePresenter.getTopRatedMovies();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void initDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        getSharedPreferences(getString(R.string.preference_key_category_movies_loaded), MODE_PRIVATE);
                        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

                        switch (item.getItemId()) {
                            case R.id.nav_polpular_movies:
                                preferences.edit().putString(getString(R.string.preference_key_category_movies_loaded), getString(R.string.preference_popular_category_movies_loaded)).apply();
                                mMoviePresenter.getPopularMovies();
                                break;
                            case R.id.nav_top_rated_movies:
                                preferences.edit().putString(getString(R.string.preference_key_category_movies_loaded), getString(R.string.preference_top_rated_category_movies_loaded)).apply();
                                mMoviePresenter.getTopRatedMovies();
                                break;
                            case R.id.nav_favorites_movies:
                                preferences.edit().putString(getString(R.string.preference_key_category_movies_loaded), getString(R.string.preference_fav_category_movies_loaded)).apply();
                                getFavorites();
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    private void initRecyclerView(){
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

    public void getFavorites() {
        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI, null, null, null, null);
        List<Movie> favoriteMovies = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            int indexJson = cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_JSON);
            String json = cursor.getString(indexJson);
            Movie movie = new Gson().fromJson(json, Movie.class);

            favoriteMovies.add(movie);
        }
        cursor.close();
        showMovies(favoriteMovies);
    }


    @Override
    public void showErrorLoadingMovies() {
        showError(R.string.error_loading_movies);
    }

    @Override
    public void showErrorParsingImages() {
        showError(R.string.error_parsing_images);
    }

    public void showError(int id) {
        String message = this.getString(id);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
