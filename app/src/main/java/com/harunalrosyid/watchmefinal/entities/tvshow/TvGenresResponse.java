package com.harunalrosyid.watchmefinal.entities.tvshow;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TvGenresResponse {

    @SerializedName("genres")
    private List<TvShowGenre> mGenres;

    public List<TvShowGenre> getGenres() {
        return mGenres;
    }

}
