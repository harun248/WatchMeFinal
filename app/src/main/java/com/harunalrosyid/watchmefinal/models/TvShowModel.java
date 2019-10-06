package com.harunalrosyid.watchmefinal.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harunalrosyid.watchmefinal.entities.tvshow.TvShow;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShowGenre;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvGenresResponse;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShowResponse;
import com.harunalrosyid.watchmefinal.api.ApiInterface;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.harunalrosyid.watchmefinal.BuildConfig.API_KEY;
import static com.harunalrosyid.watchmefinal.api.ApiUtils.getApi;

public class TvShowModel extends ViewModel {

    private static final ApiInterface apiInterface = getApi();
    private final MutableLiveData<ArrayList<TvShow>> lisTvs = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<TvShowGenre>> listTvGenres = new MutableLiveData<>();
    private final MutableLiveData<TvShow> tv = new MutableLiveData<>();

    public LiveData<ArrayList<TvShow>> getTvs() {
        return lisTvs;
    }

    public void setTvs(final String language) {
        apiInterface.getTvs(API_KEY, language).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> tvShows = new ArrayList<>(Objects.requireNonNull(response.body()).getTvs());
                    lisTvs.postValue(tvShows);
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();

                }
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                Log.i("MainActivity", "error", t);
            }

        });
    }

    public LiveData<ArrayList<TvShowGenre>> getGenres() {
        return listTvGenres;
    }

    public LiveData<TvShow> getTv() {
        return tv;
    }

    public void searchTvs(final String language, String query) {
        apiInterface.searchTv(API_KEY, language, query).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShow> tvShows = new ArrayList<>(Objects.requireNonNull(response.body()).getTvs());
                    lisTvs.postValue(tvShows);
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();

                }
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                Log.i("MainActivity", "error", t);
            }

        });
    }

    public void setTv(int tv_id, String language) {
        apiInterface.getTv(tv_id, API_KEY, language).enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                if (response.isSuccessful()) {
                    tv.postValue(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();

                }
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }

        });
    }

    public void setGenre(String language) {
        apiInterface.getTvGenres(API_KEY, language).enqueue(new Callback<TvGenresResponse>() {
            @Override
            public void onResponse(Call<TvGenresResponse> call, Response<TvGenresResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvShowGenre> tvShowGenres = new ArrayList<>(Objects.requireNonNull(response.body()).getGenres());
                    listTvGenres.postValue(tvShowGenres);
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();

                }
            }

            @Override
            public void onFailure(Call<TvGenresResponse> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }


        });
    }


}
