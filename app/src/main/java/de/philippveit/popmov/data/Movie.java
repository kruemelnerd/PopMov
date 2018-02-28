package de.philippveit.popmov.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pveit on 17.02.2018.
 */

public class Movie implements Parcelable{

    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("genre_ids")
    private List<Long> genreIds;
    @SerializedName("id")
    private Long id;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("vote_count")
    private Long voteCount;


    public Movie(Boolean adult, String backdropPath, List<Long> genreIds, Long id, String originalLanguage, String originalTitle, String overview, Double popularity, String posterPath, String releaseDate, String title, Boolean video, Double voteAverage, Long voteCount) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }
    public Movie() {
        this.adult = false;
        this.backdropPath = "";
        this.genreIds = new ArrayList<>();
        this.id = 0L;
        this.originalLanguage = "";
        this.originalTitle = "";
        this.overview = "";
        this.popularity = 0D;
        this.posterPath = "";
        this.releaseDate = "";
        this.title = "";
        this.video = false;
        this.voteAverage = 0D;
        this.voteCount = 0L;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("voteCount", voteCount).append("id", id).append("video", video).append("voteAverage", voteAverage).append("title", title).append("popularity", popularity).append("posterPath", posterPath).append("originalLanguage", originalLanguage).append("originalTitle", originalTitle).append("genreIds", genreIds).append("backdropPath", backdropPath).append("adult", adult).append("overview", overview).append("releaseDate", releaseDate).toString();
    }


    //Necessary for transmitting the Movie-Class in an Intent

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.adult ? 1 : 0);
        parcel.writeString(this.backdropPath);
        parcel.writeList(this.genreIds);
        parcel.writeLong(this.id);
        parcel.writeString(this.originalLanguage);
        parcel.writeString(this.originalTitle);
        parcel.writeString(this.overview);
        parcel.writeDouble(this.popularity);
        parcel.writeString(this.overview);
        parcel.writeString(this.posterPath);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.title);
        parcel.writeInt(this.video ? 1 : 0);
        parcel.writeDouble(this.voteAverage);
        parcel.writeLong(this.voteCount);
    }

    private void readFromParcel(Parcel in) {
        adult  = (in.readInt() == 0) ? false : true;
        backdropPath = in.readString();
        genreIds = new ArrayList<>();
        in.readList(genreIds, Movie.class.getClassLoader());
        this.id = in.readLong();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.video = (in.readInt() == 0) ? false : true;
        this.voteAverage = in.readDouble();
        this.voteCount = in.readLong();
    }

    public Movie(Parcel parcel) {
        readFromParcel(parcel);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
