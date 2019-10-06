package com.harunalrosyid.favoriteme.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harunalrosyid.favoriteme.entities.Tv;
import com.harunalrosyid.favoriteme.api.ApiInterface;
import com.harunalrosyid.favoriteme.api.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.harunalrosyid.favoriteme.BuildConfig.API_KEY;

public class TvShowModel extends ViewModel {

    private static final ApiInterface apiInterface = ApiUtils.getApi();
    private final MutableLiveData<Tv> tv = new MutableLiveData<>();

    public LiveData<Tv> getTv() {
        return tv;
    }


    public void setTv(int tv_id, String language) {
        apiInterface.getTv(tv_id, API_KEY, language).enqueue(new Callback<Tv>() {
            @Override
            public void onResponse(Call<Tv> call, Response<Tv> response) {
                if (response.isSuccessful()) {
                    tv.postValue(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();

                }
            }

            @Override
            public void onFailure(Call<Tv> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }
        });
    }


}
