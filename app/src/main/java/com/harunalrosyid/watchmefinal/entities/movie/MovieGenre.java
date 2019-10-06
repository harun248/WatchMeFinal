package com.harunalrosyid.watchmefinal.entities.movie;

import com.google.gson.annotations.SerializedName;

public class MovieGenre {

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
