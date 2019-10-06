package com.harunalrosyid.favoriteme.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harunalrosyid.favoriteme.entities.Movie;
import com.harunalrosyid.favoriteme.api.ApiInterface;
import com.harunalrosyid.favoriteme.api.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.harunalrosyid.favoriteme.BuildConfig.API_KEY;

@SuppressWarnings("ALL")
public class MovieModel extends ViewModel {

    private static final ApiInterface apiInterface = new ApiUtils().getApi();
    private final MutableLiveData<Movie> movie = new MutableLiveData<>();

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public void setMovie(int tv_id, String language) {
        apiInterface.getMovie(tv_id, API_KEY, language).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movie.postValue(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();

                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }
        });
    }


}
