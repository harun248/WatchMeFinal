package com.harunalrosyid.favoriteme.api;

import com.harunalrosyid.favoriteme.api.ApiClient;
import com.harunalrosyid.favoriteme.api.ApiInterface;

public class ApiUtils {

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static ApiInterface getApi() {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
