package de.philippveit.popmov.model;

import de.philippveit.popmov.data.MovieDbResponse;
import de.philippveit.popmov.data.VideoDbResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Philipp on 25.02.2018.
 */

public class MovieService {

    private final static String BASE_URL = "https://api.themoviedb.org/";


    public interface TheMovieDbOrgRestClient {

        @GET("/3/movie/top_rated")
        Call<MovieDbResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") Integer page);

        @GET("/3/movie/popular")
        Call<MovieDbResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") Integer page);

        @GET("/3/movie/{movie_id}/videos")
        Call<VideoDbResponse> getVideo(@Path("movie_id") String movieId, @Query("api_key") String apiKey);
    }

    public static TheMovieDbOrgRestClient getApi() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit.create(TheMovieDbOrgRestClient.class);
    }

}
