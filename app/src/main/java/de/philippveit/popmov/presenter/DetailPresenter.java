package de.philippveit.popmov.presenter;

import java.util.List;

import de.philippveit.popmov.BuildConfig;
import de.philippveit.popmov.MVP.MvpContract;
import de.philippveit.popmov.data.Movie;
import de.philippveit.popmov.data.ReviewDbResponse;
import de.philippveit.popmov.data.Video;
import de.philippveit.popmov.data.VideoDbResponse;
import de.philippveit.popmov.model.MovieService;
import de.philippveit.popmov.util.MovieUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Philipp on 27.02.2018.
 */

public class DetailPresenter implements MvpContract.PresenterDetailOps {

    private MvpContract.ViewDetailOps mView;

    private String API_KEY;
    private final String YOUTUBE_NAME = "youtube";


    public DetailPresenter(MvpContract.ViewDetailOps mView) {
        this.mView = mView;
        this.API_KEY = BuildConfig.TMDBORG_API_KEY;
    }

    @Override
    public void onCreate() {
        //Nothing to do yet
    }

    @Override
    public void getVideo(Movie movie) {
        Call<VideoDbResponse> call = MovieService.getApi().getVideo(movie.getId().toString(), API_KEY);

        call.enqueue(new Callback<VideoDbResponse>() {
            @Override
            public void onResponse(Call<VideoDbResponse> call, Response<VideoDbResponse> response) {
                VideoDbResponse videoDbResponse = response.body();
                if (videoDbResponse != null) {
                    List<Video> results = videoDbResponse.getResults();
                    for (Video video : results) {
                        video.setThumbnailUrl(MovieUtil.normalizeYoutubeLink(video.getKey()));
                    }
                    mView.showTrailer(results);
                }
            }

            @Override
            public void onFailure(Call<VideoDbResponse> call, Throwable t) {
                return;
            }
        });
    }

    @Override
    public void getReviews(Movie movie) {

        Call<ReviewDbResponse> call = MovieService.getApi().getReviews(movie.getId().toString(), API_KEY);
        call.enqueue(new Callback<ReviewDbResponse>() {
            @Override
            public void onResponse(Call<ReviewDbResponse> call, Response<ReviewDbResponse> response) {
                ReviewDbResponse body = response.body();
                if (body != null) {
                    mView.showReviews(body.getResults());
                }
            }

            @Override
            public void onFailure(Call<ReviewDbResponse> call, Throwable t) {

            }
        });
    }
}
