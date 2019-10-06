package com.harunalrosyid.watchmefinal.entities.tvshow;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<TvShow> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public List<TvShow> getTvs() {
        return mResults;
    }

}
