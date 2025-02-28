package com.harunalrosyid.watchmefinal.entities.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<Movie> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public List<Movie> getMovies() {
        return mResults;
    }

}
