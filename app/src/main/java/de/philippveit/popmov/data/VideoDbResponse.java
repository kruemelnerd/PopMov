package de.philippveit.popmov.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by Philipp on 27.02.2018.
 */

public class VideoDbResponse {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("results")
    @Expose
    private List<Video> results = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("results", results).toString();
    }
}
