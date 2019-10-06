package com.harunalrosyid.watchmefinal.entities.tvshow;

import com.google.gson.annotations.SerializedName;

public class TvShowGenre {

    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;

    public Long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

}
